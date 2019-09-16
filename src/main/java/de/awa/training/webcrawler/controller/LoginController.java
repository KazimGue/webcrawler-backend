package de.awa.training.webcrawler.controller;

import de.awa.training.webcrawler.entity.UnternehmenEntity;
import de.awa.training.webcrawler.model.Login;
import de.awa.training.webcrawler.repository.UnternehemensRepository;
import de.awa.training.webcrawler.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    UnternehemensRepository unternehemensRepository;

    @Autowired
    LoginService loginService;


    @CrossOrigin("http://localhost:3000")
    @PostMapping("/login")
    public String loginCheck(@RequestBody Login login) {
        // Json Objekt vom Frontend (LoginName und Passwort) annehmen und in der Methode checken
        String returnStatement = loginService.checkLoginDaten(login);
        return returnStatement;
    }

    @GetMapping("/get")
    public String getUser(){
        return "Get User Information";
    }


}
