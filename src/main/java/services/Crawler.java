package services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.NoSuchElementException;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public abstract class Crawler{

    List<String>preise = new ArrayList<>();

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


    public abstract void tankcrawlen(String plz, WebDriver driver, ChromeOptions chromeOptions) throws NoSuchElementException;

    public void allePLZCrawlen() throws ParseException,NoSuchElementException {
        start(16, 07, 0, "51570");
        start(16, 8, 0, "52152");
        start(16, 9, 0, "53804");
        start(16, 10, 0, "54290");
        start(16, 11, 0, "55543");
        start(16, 12, 0, "56072");
        start(16, 13, 0, "57539");
        start(16, 14, 0, "58119");

    }

    public static void waitForAction(final double time) {
        try {
            Thread.sleep((int) (time * 1000));
        } catch (final InterruptedException e) {
            e.printStackTrace();

        }
    }
}
