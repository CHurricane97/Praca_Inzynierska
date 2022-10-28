package com.example.town_application.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class DataService {

    private final AuthService authService;

    @Autowired
    public DataService(AuthService authService) {
        this.authService = authService;
    }


    public <T extends JpaRepository> ResponseEntity<Object> getAll(T repository, String authToken, int permissionLevel){
        Map<String, Object> resp = new LinkedHashMap<>();
        if (authService.isUserAuthenticated(authToken, permissionLevel)) {
            resp.put("status", "success");
            resp.put("answer", repository.findAll());
            return ResponseEntity.ok(resp);
        } else {
            resp.put("status", "error");
            resp.put("answer", "User authentication failed.");
            return ResponseEntity.badRequest().body(resp);
        }
    }

    public <T extends JpaRepository> ResponseEntity<Object> getOne(T repository, String id, String authToken, boolean parseInt, int permissionLevel) {
        try {
            Map<String, Object> resp = new LinkedHashMap<>();
            if (authService.isUserAuthenticated(authToken, permissionLevel)) {
                resp.put("status", "success");
                resp.put("answer", repository.findById(parseInt? Integer.parseInt(id) : id).orElseThrow(NoSuchElementException::new) );
                return ResponseEntity.ok().body(resp);
            } else {
                resp.put("status", "error");
                resp.put("answer", "User authentication failed.");
                return ResponseEntity.badRequest().body(resp);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Wrong id format." + (e.getMessage().length() > 0 ? "Integer.parseInt(): Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        } catch (Throwable e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Data not found. " + (e.getMessage() != null && e.getMessage().length() > 0 ? " Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        }
    }

    public <T extends JpaRepository> ResponseEntity<Object> deleteOne(T repository, String id, String authToken, boolean parseInt, int permissionLevel) {
        try {
            Map<String, Object> resp = new LinkedHashMap<>();
            if (authService.isUserAuthenticated(authToken, permissionLevel)) {
                resp.put("status", "success");
                resp.put("answer", "Succesfully deleted.");
                repository.deleteById(parseInt? Integer.parseInt(id) : id);
                return ResponseEntity.ok(resp);
            } else {
                resp.put("status", "error");
                resp.put("answer", "User authentication failed.");
                return ResponseEntity.badRequest().body(resp);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Wrong id format." + (e.getMessage() != null && e.getMessage().length() > 0 ? "Integer.parseInt(): Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        } catch (EmptyResultDataAccessException e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Data not found. " + (e.getMessage() != null && e.getMessage().length() > 0 ? " Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        }
    }

    public <T extends JpaRepository, G> ResponseEntity<Object> post(T repository, G entity, String authToken, int permissionLevel) {
        try {
            Map<String, Object> resp = new LinkedHashMap<>();
            if (authService.isUserAuthenticated(authToken, permissionLevel)) {
                resp.put("status", "success");
                resp.put("answer", "Successfully added.");
                repository.save(entity);
                return ResponseEntity.ok(resp);
            } else {
                resp.put("status", "error");
                resp.put("answer", "User authentication failed.");
                return ResponseEntity.badRequest().body(resp);
            }
        } catch (Exception e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Exception occurred." + (e.getMessage() != null && e.getMessage().length() > 0 ? " Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        }
    }

    public <T extends JpaRepository, G> ResponseEntity<Object> put(T repository, G newEntity, String id, String authToken, boolean parseInt, int permissionLevel) {
        try {
            Map<String, Object> resp = new LinkedHashMap<>();
            if (authService.isUserAuthenticated(authToken, permissionLevel)) {
                resp.put("status", "success");
                resp.put("answer", "Successfully updated." );
                G entity = (G) repository.findById(parseInt?Integer.parseInt(id):id).orElseThrow(NoSuchElementException::new);
                Class<?> cls = newEntity.getClass();
                Method[] methods = cls.getMethods();
                for(Method setter : methods){
                    if(setter.getName().contains("set")){
                        for(Method getter: methods){
                            if(getter.getName().equals("get" +setter.getName().substring(3))){
                                System.out.println("Invoking" + entity.toString() + "." + setter.getName() + "( " + getter.getName() + "(" + newEntity.toString() + ")"); //TODO: remove this line
                                setter.invoke(entity, getter.invoke(newEntity));
                            }
                        }
                    }
                }
                repository.save(entity);
                return ResponseEntity.ok().body(resp);
            } else {
                resp.put("status", "error");
                resp.put("answer", "User authentication failed.");
                return ResponseEntity.badRequest().body(resp);
            }
        } catch (NumberFormatException e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Wrong id format." + (e.getMessage().length() > 0 ? "Integer.parseInt(): Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        } catch (Throwable e) {
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("status", "error");
            resp.put("answer", "Data not found. " + (e.getMessage() != null && e.getMessage().length() > 0 ? " Exception message: " + e.getMessage() : ""));
            return ResponseEntity.badRequest().body(resp);
        }
    }

}

