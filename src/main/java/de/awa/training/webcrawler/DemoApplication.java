package de.awa.training.webcrawler;

import de.awa.training.webcrawler.entity.PostleitzahlenEntity;
import de.awa.training.webcrawler.entity.UnternehmenEntity;
import de.awa.training.webcrawler.repository.PLZRepository;
import de.awa.training.webcrawler.repository.UnternehemensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.awa.training.webcrawler.services.Fluessiggas123;
import de.awa.training.webcrawler.services.MeinFluessiggasWebcrawler;
import de.awa.training.webcrawler.services.PfiffiggasWebcrawler;

import java.text.ParseException;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class DemoApplication {


    @Autowired
    PfiffiggasWebcrawler pfiffiggasWebcrawler;

    @Autowired
    MeinFluessiggasWebcrawler meinFluessiggasWebcrawler;

    @Autowired
    Fluessiggas123 fluessiggas123;

    @Autowired
    PLZRepository plzRepository;

    @Autowired
    UnternehemensRepository unternehemensRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    //befüllt die Datenbank mit Testdaten falls die noch nicht vorhanden sind und führt die Crawler aus.
    @PostConstruct
    private void init() {
        String[]postleitzahlen = {"51570","52152","53804", "54290","55543","56072","57539","58119"};
        String [][]unternehmen = {
                {"Pfiffigas","Fischenicher Straße 23","50321","Brühl","pfiffiggas","pfiffiggas"},
                {"Meinflüssiggas","Stau 169","262122","Oldenburg","meinfluessiggas","meinfluessiggas"},
                {"123Flüssiggas", "Blumenstraße 70","99092 ","Erfurt","fluessiggas123", "fluessiggas123"},
                {"Beispielunternehmen", "ABC str.90","50000", "Köln","bespiel","beispiel"},
                {"Testunternehmen", "DEF str.100","60000", "Köln","test","test"},
        };


        if(plzRepository.findAll().isEmpty()) {
            for (String plz : postleitzahlen) {
                PostleitzahlenEntity entity = new PostleitzahlenEntity();
                entity.setPlz(plz);
                plzRepository.save(entity);
            }
        }
        if(unternehemensRepository.findAll().isEmpty()){
            Integer i = Integer.valueOf(1);
            for (String[]einezelnesUnternehmen:unternehmen) {
                UnternehmenEntity entity = new UnternehmenEntity();
                entity.setId(i);
                entity.setName(einezelnesUnternehmen[0]);
                entity.setAdresse(einezelnesUnternehmen[1]);
                entity.setPlz(einezelnesUnternehmen[2]);
                entity.setOrt(einezelnesUnternehmen[3]);
                entity.setBenutzername(einezelnesUnternehmen[4]);
                entity.setPasswort(einezelnesUnternehmen[5].hashCode());
                unternehemensRepository.save(entity);
                i++;
            }
        }
        try {
            pfiffiggasWebcrawler.allePLZCrawlen();
            //meinFluessiggasWebcrawler.allePLZCrawlen();
            //fluessiggas123.allePLZCrawlen();


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
