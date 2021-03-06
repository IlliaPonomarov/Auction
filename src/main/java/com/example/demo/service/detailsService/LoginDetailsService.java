package com.example.demo.service.detailsService;

import com.example.demo.model.Login;
import com.example.demo.repo.LoginRepository;
import com.example.demo.service.details.LoginDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private LoginRepository loginRepository;

    public LoginDetailsService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void update(Login newUser){
        loginRepository.save(newUser);
    }

    public void delete(Login user){
        loginRepository.delete(user);
    }

    public void save(Login entity) {

        if (entity.validfoUsername(entity.getUsername())) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
                mapper.writeValue(new File("persons.json"), entity);

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            loginRepository.save(entity);
        }
        else
        {
            System.err.println("User not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Login login = loginRepository.findByUsername(username);

        if (login == null) throw new UsernameNotFoundException("Not found " + username);


        return new LoginDetails(login);
    }

    public Login findUserByUsername(String username){

        Login login = loginRepository.findByUsername(username);

        if (login == null) throw new UsernameNotFoundException("Not found " + username);


        return login;
    }


    public List<Login> findAll(){
        List<Login> logins = new ArrayList<>();
        loginRepository.findAll().forEach(index -> {
           
            logins.add(index);
        });

        return logins;
    }


    public boolean check(Login login){
        if (login.getRole().equals("ROLE_ADMIN"))
            return true;

        return false;
    }

}
