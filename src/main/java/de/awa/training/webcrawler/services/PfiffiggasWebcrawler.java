package de.awa.training.webcrawler.services;

import de.awa.training.webcrawler.entity.*;
import de.awa.training.webcrawler.repository.PreiseingabeUnternehmenRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PfiffiggasWebcrawler extends Crawler{




    @Autowired
    PreiseingabeUnternehmenRepository preiseingabeUnternehmenRepository;

    @Autowired
    MailService mailService;

    private final int UnternehmensID = 1;

    public int getUnternehmensID() {
        return UnternehmensID;
    }

    public void tankcrawlen(String plz,WebDriver driver, ChromeOptions chromeOptions){
        try {
            String preis2700;
            String preis4850;
            String preis6400;

            driver.get("https://pfiffiggas.de/#!/mein-preis");

            Select element = new Select(driver.findElement(By.tagName("Select")));
            element.selectByVisibleText("1.2t (2700 Liter)");

            WebElement text = driver.findElement(By.cssSelector("input[class='form-control ng-pristine ng-untouched ng-valid ng-isolate-scope ng-empty ng-valid-maxlength']"));
            text.sendKeys(plz);
            waitForAction(3.0);

            text = driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div[1]/span"));
            System.out.println("PfiffiggasPreis "+ text.getText());
            preis2700 = text.getText();
            waitForAction(2.0);

            element.selectByVisibleText("2.1t (4850 Liter)");
            waitForAction(3.0);
            text = driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div[1]/span"));
            System.out.println("PfiffiggasPreis "+ text.getText());
            preis4850 = text.getText();
            waitForAction(2.0);
            element.selectByVisibleText("2.9t (6400 Liter)");
            waitForAction(3.0);
            text = driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/div/div[2]/div/div/div[2]/div/div[1]/div/div[1]/span"));
            System.out.println("PfiffiggasPreis "+ text.getText());
            preis6400 = text.getText();
            waitForAction(2.0);
            driver.close();

            PreiseingabeUnternehmenEntity preiseingabeUnternehmenEntity = new PreiseingabeUnternehmenEntity();
            preiseInDatenbankschreiben(preiseingabeUnternehmenRepository,preiseingabeUnternehmenEntity,preis2700,preis4850,preis6400,plz);

        }catch (NoSuchElementException e){
            System.out.println("Crawler bei Pfiffiggas nicht erfolgreich für " + plz);
            mailService.mailSendenException("Crawler konnte nicht durchgeführt werden", "Crawler Fehler", "Pfiffiggas");

        }finally {
            driver.quit();
        }


   }

}

