package de.awa.training.webcrawler.services;


import de.awa.training.webcrawler.entity.PreiseingabeUnternehmenEntity;
import de.awa.training.webcrawler.repository.PreiseingabeUnternehmenRepository;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeinFluessiggasWebcrawler extends Crawler {


    @Autowired
    PreiseingabeUnternehmenRepository preiseingabeUnternehmenRepository;

    @Autowired
    MailService mailService;

    private final int UnternehmensID = 2;

    public int getUnternehmensID() {
        return UnternehmensID;
    }

    @Override
    public void tankcrawlen(String plz, WebDriver driver, ChromeOptions chromeOptions){

        try {
            String preis2700;
            String preis4850;
            String preis6400;

            driver.get("https://www.meinfluessiggas.de/");
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("window.scrollBy(0,300)");

            //Plz und Füllstand eintragen die preise sind für alle drei Tankgrößen identisch Füllstand bei 30%
            waitForAction(2.0);
            driver.findElement(By.cssSelector("input[placeholder='Ihre PLZ']")).sendKeys(plz);
            waitForAction(2.0);
            waitForAction(2.0);
            driver.findElement(By.id("options_2_text")).sendKeys("0");
            System.out.println("MeinFlüssiggas " + driver.findElement(By.id("priceNetto")).getText());
            waitForAction(1.0);
            System.out.println("MeinFlüssiggas " + driver.findElement(By.id("priceNetto")).getText());
            waitForAction(1.0);
            System.out.println("MeinFlüssiggas " + driver.findElement(By.id("priceNetto")).getText());
            preis2700 = driver.findElement(By.id("priceNetto")).getText();
            preis4850 = driver.findElement(By.id("priceNetto")).getText();
            preis6400 = driver.findElement(By.id("priceNetto")).getText();
            waitForAction(1.0);
            driver.close();

            PreiseingabeUnternehmenEntity preiseingabeUnternehmenEntity = new PreiseingabeUnternehmenEntity();
            preiseInDatenbankschreiben(preiseingabeUnternehmenRepository,preiseingabeUnternehmenEntity,preis2700,preis4850,preis6400,plz);

        }catch (NoSuchElementException e) {
            //e.printStackTrace();
            System.out.println("Crawler bei MeinFlüssiggas nicht erfolgreich für " + plz);
            mailService.mailSendenException("Crawler konnte nicht durchgeführt werden", "Crawler Fehler", "MeinFluessiggas");


        }finally {
            driver.quit();
        }
    }
}



// System.setProperty("webdriver.chrome.driver",
//System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe");
//ChromeOptions chromeOptions = new ChromeOptions();
// chromeOptions.addArguments("--startup-maximized");// --headless   --startup-maximized
// WebDriver driver = new ChromeDriver(chromeOptions);



