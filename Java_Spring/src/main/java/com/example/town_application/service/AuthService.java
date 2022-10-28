package com.example.town_application.service;

import com.example.town_application.model.AuthToken;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {


    private final List<AuthToken> authTokens;
    private final SecureRandom random;
    private final UsersRepository userRepository;
    private final PersonalDataRepository personalDataRepository;

    public AuthService(UsersRepository userRepository, PersonalDataRepository personalDataRepository) {
        this.userRepository = userRepository;
        this.personalDataRepository = personalDataRepository;
        this.authTokens = new ArrayList<>();
        this.random = new SecureRandom();
        authTokens.add(new AuthToken("permission1", "testMail1", 1));//TODO: remove this two lines
        authTokens.add(new AuthToken("permission0", "testMail0", 0));//for testing
    }


    public boolean isUserAuthenticated(String token, int permissionLevel) {
        return authTokens.stream().anyMatch(stream -> stream.getAuthToken().equals(token)
                && stream.getPermissionLevel() >= permissionLevel);
    }

    public Optional<AuthToken> getTokenForEmail(String email){
        return authTokens.stream().filter(authToken -> authToken.getEmail().equals(email)).findAny();
    }


    public Optional<AuthToken> getTokenObjForTokenStr(String token) {
        return authTokens.stream().filter(authToken -> authToken.getAuthToken().equals(token)).findAny();
    }

    public AuthToken createToken(String email, int permissionLevel){
        AuthToken newToken = new AuthToken(email, permissionLevel);
        authTokens.add(newToken);
        return newToken;
    }

    public AuthToken login(String email, String password, int permissionLevel) throws NoSuchAlgorithmException {
        Optional<Users> user = Optional.ofNullable(userRepository.findByLogin(email));
        if (user.isEmpty()) throw new IllegalArgumentException("Could not login, user does not exist.");
        if (user.get().getPermissionLevel() < permissionLevel)
            throw new IllegalArgumentException("Could not login, user permissions insufficient");
        String hashPswd = hashPassword(password);
        if (!user.get().getPassword().equals(hashPswd))
            throw new IllegalArgumentException("Wrong password.");
        return createToken(email, permissionLevel);
    }



    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(password.getBytes());

        byte[] bytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


    public void registerUser(Users user, PersonalData personalData) throws NoSuchAlgorithmException {
        if (userRepository.findByLogin(user.getLogin()) != null)
            throw new IllegalArgumentException("Account already registered.");
        user.setPassword(hashPassword(user.getPassword()));
        user.setPersonalDataForUsers(personalData);
        personalData.setName("dupa");
        personalData.setPesel("2131");
        personalData.setSurname("2121");

        personalDataRepository.save(personalData);

        userRepository.save(user);
    }

    public void revoke(String token) {
        Optional<AuthToken> authToken = getTokenObjForTokenStr(token);
        if (authToken.isEmpty()) throw new IllegalArgumentException("Token does not exist.");
        authTokens.remove(authToken);
    }
}
