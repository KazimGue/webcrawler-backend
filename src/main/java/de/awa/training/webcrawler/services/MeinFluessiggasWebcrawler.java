package de.awa.training.webcrawler.services;


import de.awa.training.webcrawler.repository.MeinFluessiggasRepository;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeinFluessiggasWebcrawler extends Crawler {

    @Autowired
    MeinFluessiggasRepository meinFluessiggasRepository;


    @Override
    public void tankcrawlen(String plz, WebDriver driver, ChromeOptions chromeOptions) throws NoSuchElementException {
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
        waitForAction(1.0);
        driver.close();
    }



}



// System.setProperty("webdriver.chrome.driver",
//System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe");
//ChromeOptions chromeOptions = new ChromeOptions();
// chromeOptions.addArguments("--startup-maximized");// --headless   --startup-maximized
// WebDriver driver = new ChromeDriver(chromeOptions);



