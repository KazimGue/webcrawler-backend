package de.awa.training.webcrawler.controller;

import de.awa.training.webcrawler.entity.KontaktanfrageEntity;
import de.awa.training.webcrawler.model.Daten;
import de.awa.training.webcrawler.model.Anfrage;
import de.awa.training.webcrawler.model.KontaktAnfrage;
import de.awa.training.webcrawler.model.PreisDaten;
import de.awa.training.webcrawler.repository.*;
import de.awa.training.webcrawler.services.MailService;
import de.awa.training.webcrawler.services.NeuerAnfragenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnfragenController {



    @Autowired
    KontaktanfrageRepository kontaktanfrageRepository;

    @Autowired
    NeuerAnfragenService neuerAnfragenService;

    @Autowired
    private MailService mailService;

    @CrossOrigin("http://localhost:3000")
    @PostMapping("/preis/anfrage")
    public List<Daten> anfrageerhalten(@RequestBody Anfrage test){
        String behaelter = test.getBehaelter();
        Integer plzID = neuerAnfragenService.holePLZid(test.getPlz());
        Integer tankgröße = neuerAnfragenService.tankgrößeInIndexumwandeln(behaelter);
        List<PreisDaten>preisliste = neuerAnfragenService.sammlePreise(plzID,tankgröße);
        List<Daten> liste = neuerAnfragenService.preisUnternehmenZuweisen(preisliste);
        liste = neuerAnfragenService.filtereUnternehmenOhnePreis(liste);
        return liste;
    }


    // Unternehmensanfrage aus dem Frontend entgegennehmen und in der Datenbank speichern
    // Zusätzlich eine Benachrichtigungsmail ans Team schicken mit den nötigen Informationen
    @CrossOrigin("http://localhost:3000")
    @PostMapping("/kontaktUnternehmen")
    public String unternehmensAnfrage (@RequestBody KontaktAnfrage kontaktAnfrage){
        String antwort = neuerAnfragenService.kontaktanfrageSpeichernSchicken(kontaktAnfrage);
        return antwort;
 }

}

