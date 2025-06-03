package com.juneit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class SploteamTestOld {
    private final PropertiesLoader properties = new PropertiesLoader ();
    private final WebDriver driver = new ChromeDriver ();

    @Before public void setup () {
        driver.manage ().window ().maximize ();
        driver.manage ().timeouts ().implicitlyWait (Duration.ofSeconds (5));
        ((HasAuthentication) driver).register (UsernameAndPassword.of (properties.username,
                properties.password));
        driver.get (properties.baseUrl);
    }


    @After public void close () {
        driver.close ();
    }


    @Test public void assertMainPageIsLoaded () {
        Assert.assertTrue(driver.findElement (By.className (MAIN_PHOTO_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement (By.className (HEADER_LOGO_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement (By.xpath (CREATEGAME_BUTTON_XPATH)).isDisplayed());
    }

//Locators
public static final String MAIN_PHOTO_CLASS = "all-games-img";
public static final String HEADER_LOGO_CLASS = "header__logo";
public static final String CREATEGAME_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[3]/div/div[2]/a";
}
