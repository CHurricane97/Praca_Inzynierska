package com.example.town_application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Basic
    @Column(name = "login", nullable = false, length = 255)
    private String login;
    @Basic
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Basic
    @Column(name = "permission_level", nullable = false)
    private int permissionLevel;
    @OneToMany(mappedBy = "usersByUserId", cascade = CascadeType.ALL)
    private Collection<LoginRegister> loginRegistersByUserId;
    @ManyToOne(targetEntity = PersonalData.class)
    @JoinColumn(name = "personal_data_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PersonalData personalDataForUsers;


    public Users(String login, int permissionLevel, String password) {
        this.login = login;
        this.password = password;
        this.permissionLevel = permissionLevel;
    }

    public Users() {

    }
}
