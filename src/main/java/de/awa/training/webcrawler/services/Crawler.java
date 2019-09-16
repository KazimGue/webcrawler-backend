package de.awa.training.webcrawler.services;

import de.awa.training.webcrawler.entity.EntityInterface;
import de.awa.training.webcrawler.entity.PostleitzahlenEntity;
import de.awa.training.webcrawler.repository.PLZRepository;
import de.awa.training.webcrawler.repository.UnternehemensRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;



public abstract class Crawler{

    @Autowired
    PLZRepository plzRepository;

    @Autowired
    UnternehemensRepository unternehemensRepository;

    private int UnternehmensID;

    // starte den Crawler für die KindKlassen die, die Methode tankcrawlen implementieren
    public void start(int std, int min, int sek, String plz) throws ParseException, NoSuchElementException {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, std);
        today.set(Calendar.MINUTE, min);
        today.set(Calendar.SECOND, sek);
        Timer timer = new Timer();
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() throws NoSuchElementException {
                tankcrawlen(plz, driver, chromeOptions);
            }
        };
        timer.scheduleAtFixedRate(timerTask, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

    // Definiert wie jede KindKlasse eine Internetseite crawlet.
    public abstract void tankcrawlen(String plz, WebDriver driver, ChromeOptions chromeOptions) throws NoSuchElementException;

    // Führt die start methode für alle Postleitzahlen aus die wir in der Mehode definieren
    public void allePLZCrawlen() throws ParseException,NoSuchElementException {
        int std = 9;
        int min = 20;

        start(9, 15, 0, "51570");
/*        start(9, 16, 0, "52152");
        start(9, 17, 0, "53804");
        start(9, 18, 0, "54290");
        start(9, 19, 0, "55543");
        start(9, 20, 0, "56072");
        start(9, 21, 0, "57539");
        start(9, 22, 0, "58119");*/
    }

    //Definiert wie die Preise die gecrawlt werden in der Datenbank abgespeichert werden.
    public void preiseInDatenbankschreiben(JpaRepository repository,
                                           EntityInterface entityInterface,
                                           String preis2700,
                                           String preis4850,
                                           String preis6400,
                                           String plz){

        Date date = new Date(new java.util.Date().getTime());
        Integer plzID = suchePLZ(plz);
        entityInterface.setPreis2700Liter(preis2700);
        entityInterface.setPreis4850Liter(preis4850);
        entityInterface.setPreis6400Liter(preis6400);
        entityInterface.setPostleitzahlenId(plzID);
        entityInterface.setUnternehmenId(getUnternehmensID());
        entityInterface.setDatum(date);
        repository.save(entityInterface);
    }

    //sucht zu den Postleitzahlen String den richtigen Preis raus.
    public Integer suchePLZ(String plz){
        List<PostleitzahlenEntity> liste = plzRepository.findAll();
        Integer plzID = null;
        for (PostleitzahlenEntity element: liste) {
            if(element.getPlz().equals(plz)){
                plzID = element.getId();
            }
        }
        return plzID;
    }

    //lässt den Thread für den wert an millisekunden stehen, der der Methode übergeben wird.
    public static void waitForAction(final double time) {
        try {
            Thread.sleep((int) (time * 1000));
        } catch (final InterruptedException e) {
            e.printStackTrace();

        }
    }

    public abstract int getUnternehmensID();
}
