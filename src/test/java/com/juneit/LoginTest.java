package com.juneit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class LoginTest {
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


    @Test public void clickOnLoginButton () {
        driver.findElement (By.className (SIGNIN_BUTTON_CLASS)).click ();
    }

    public static final String SIGNIN_BUTTON_CLASS = "header__signIn";
}
