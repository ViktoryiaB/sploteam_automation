import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;


public class SploteamAutoTestLogin{
    public static final String url = "https://frontend.beta-spltm.ru";
    WebDriver driver = new ChromeDriver();



    @Before
    public void setup(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        ((HasAuthentication) driver).register(UsernameAndPassword.of("sploteam", "Esthotim2#"));
        driver.get(url);
    }


    @After
    public void close() {
        driver.close();
    }


    @Test
    public void openMainPageAndAssertPageLoaded() {
        driver.findElement(By.className(HEADER_LOGO_CLASS));

    }


    //Locators
    public static final String HEADER_LOGO_CLASS = "header__logo";

}
