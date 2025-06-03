import com.juneit.PropertiesLoader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static java.lang.Thread.sleep;

public class SploteamTest_Victoria {
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
        Assert.assertTrue(driver.findElement (By.xpath (QA_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement (By.xpath (RENTPLAYGROUND_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement (By.className (CITYLOCATION_BUTTON_CLASS)).isDisplayed());
        Assert.assertEquals("Санкт-Петербург",driver.findElement(By.className(CITYLOCATION_BUTTON_CLASS)).getText());
        Assert.assertTrue(driver.findElement (By.className  (TYPEOFGAME_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement (By.xpath (FOOTBALLTYPEOFGAME_BUTTON_XPATH)).isDisplayed());
        Assert.assertEquals("Футбол",driver.findElement(By.xpath(FOOTBALLTYPEOFGAME_BUTTON_XPATH)).getText());
        Assert.assertEquals("Приглашаем к сотрудничеству\nвладельцев площадок и тренеров",driver.findElement(By.className(COOPERATIONTITLE_TEXT_CLASS)).getText());
    }

    @Test public void assertCampSectionIsDisplayed () {
        Assert.assertTrue(driver.findElement (By.className (CAMP_BUTTON_CLASS)).isDisplayed());
        Assert.assertEquals("Новинка!",driver.findElement(By.xpath(CAMP_TEXT1_XPATH)).getText());
        Assert.assertEquals("Спортивные кемпы",driver.findElement(By.className(CAMP_TEXT2_CLASS)).getText());
        Assert.assertTrue("Image is not displayed", driver.findElement(By.className(CAMP_LOGOImageButton_CLASS)).isDisplayed());
    }

    @Test
    public void loginPositive () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
    }

    @Test
    public void loginWithWrongEmail () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("a54ysjtdi76ryi@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();

        WebElement wrongEmailError = driver.findElement (By.className (WRONG_EMAIL_ERROR_CLASS));

        Assert.assertTrue(wrongEmailError.isDisplayed());

        String expectedWrongEmailErrorMessage = "Такой логин или пароль не найдены";
        String actualWrongEmailErrorMessage = wrongEmailError.getText();

        Assert.assertEquals(expectedWrongEmailErrorMessage, actualWrongEmailErrorMessage);
    }

    @Test
    public void loginWithEmptyEmailPassword () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();

        Assert.assertTrue(driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).isDisplayed());
        Assert.assertEquals("Войти",driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).getText());

    }

    @Test
    public void loginWithIncorrectFormatEmail () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme1@gmailcom");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();

        WebElement wrongEmailError = driver.findElement (By.className (WRONG_EMAIL_ERROR_CLASS));

        Assert.assertTrue(wrongEmailError.isDisplayed());

        String expectedWrongEmailErrorMessage = "Неверное значение";
        String actualWrongEmailErrorMessage = wrongEmailError.getText();

        Assert.assertEquals(expectedWrongEmailErrorMessage, actualWrongEmailErrorMessage);
        driver.findElement(By.className(CLOSE_SIGNIN_WINDOW_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }

    @Test
    public void loginWithWrongSixSymbolsPassword () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme1@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilove");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();

        WebElement wrongPasswordError = driver.findElement (By.xpath (WRONG_PASSWORD_ERROR_XPATH));

        Assert.assertTrue(wrongPasswordError.isDisplayed());

        String expectedWrongPasswordErrorMessage = "Неверное значение";
        String actualWrongPasswordErrorMessage = wrongPasswordError.getText();

        Assert.assertEquals(expectedWrongPasswordErrorMessage, actualWrongPasswordErrorMessage);
        driver.findElement(By.className(CLOSE_SIGNIN_WINDOW_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }

    @Test
    public void loginPersonalAccount () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("BVA_"));
        Assert.assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("testmyme4@gmail.com"));
        Assert.assertEquals("Пополнить счет",driver.findElement(By.xpath(TOPUP_ACCOUNT_BUTTON_XPATH)).getText());
        Assert.assertTrue(driver.findElement(By.xpath(PERSONAL_ACCOUNT_XPATH)).getText().contains("Личный счёт"));
        Assert.assertEquals("Редактировать",driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).getText());
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
    }

    @Test
    public void EditNamePersonalAccount () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        Assert.assertEquals("BVA!!!",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        Assert.assertEquals("BVA!!!",driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA_");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        Assert.assertEquals("BVA_",driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
    }

    @Test
    public void EditNameOneDigitPhoneNumberPersonalAccount () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath (EDIT_PHONENUMBER_PERSONAL_ACCOUNT_XPATH)).sendKeys("1");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Неверное значение",driver.findElement(By.className(ERROR_MESSAGE_PERSONAL_ACCOUNT_CLASS)).getText());
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        Assert.assertFalse(driver.findElement(By.className(AVATAR_NAME_CLASS)).getText().contains("BVA!!!"));
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
    }

    @Test
    public void EditEmptyNamePersonalAccount () {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Выберите значение",driver.findElement(By.xpath(ERROR_MESSAGE_NAME_PERSONAL_ACCOUNT_XPATH)).getText());
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_NAME_CLASS)).getText().contains("BVA_"));
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
    }


    @Test
    public void assertArenaFilter () throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_PICKER_XPATH)).click();

        WebElement arenaFilter = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        Assert.assertTrue(arenaFilter.isDisplayed());

        Assert.assertEquals("Все арены", arenaFilter.getText());
        sleep(5000);
        System.out.println("Number of games before arena filter is" + driver.findElement(By.className(ARENA_ON_GAME_CLASS)));
        arenaFilter.click();
        driver.findElement(By.xpath(DEMO_ARENA_FILER_XPATH)).click();
        Assert.assertEquals("Demo", arenaFilter.getText());

        Assert.assertFalse(driver.findElements(By.className(ARENA_ON_GAME_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size(); i++) {
            Assert.assertEquals("\"Demo\"", driver.findElements(By.className(ARENA_ON_GAME_CLASS)).get(i).getText());
        }
    }

    @Test
    public void assertSporttypeFilter () {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_PICKER_XPATH)).click();

        WebElement sportstypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));
        Assert.assertTrue(sportstypeFilter.isDisplayed());
        Assert.assertEquals("Все виды спорта", sportstypeFilter.getText());
        System.out.println("Всего игр:"+driver.findElements(By.className(TYPEOFGAME_BUTTON_CLASS)).size());
        sportstypeFilter.click();
        driver.findElement(By.xpath(FOOTBALL_FILTER_XPATH)).click();
        Assert.assertEquals("Футбол", sportstypeFilter.getText());

        Assert.assertFalse(driver.findElements(By.className(SPORT_TYPE_ON_GAME_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(SPORT_TYPE_ON_GAME_CLASS)).size(); i++) {
            Assert.assertEquals("Футбол", driver.findElements(By.className(SPORT_TYPE_ON_GAME_CLASS)).get(i).getText());
        }
    }

    @Test
    public void assertChangeCity () throws InterruptedException {
        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.className(CITY_HEADER_CLASS)).getText());
        driver.findElement(By.className(CITY_HEADER_CLASS)).click();
        String chosenCity = driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(3).getText();
        System.out.println(chosenCity);
        driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(3).click();
        sleep(3000);
        Assert.assertTrue(chosenCity.contains(driver.findElement(By.className(CITY_HEADER_CLASS)).getText()));
        driver.findElement(By.className(CITY_HEADER_CLASS)).click();
        driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(1).click();
        sleep(3000);
        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.className(CITY_HEADER_CLASS)).getText());
    }


    @Test
    public void assertDateGamePlayerLevelFilter () throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_PICKER2_XPATH)).click();

        WebElement alleventsFilter = driver.findElement(By.xpath(ALLEVENTS_FILTER_XPATH));
        Assert.assertTrue(alleventsFilter.isDisplayed());
        Assert.assertEquals("Все события", alleventsFilter.getText());
        sleep(3000);
        alleventsFilter.click();
        driver.findElement(By.xpath(GAMES_ALLEVENTS_FILTER_XPATH)).click();
        sleep(3000);
        Assert.assertEquals("Игры",alleventsFilter.getText());

        Assert.assertFalse(driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).size(); i++) {
            Assert.assertEquals("Игра", driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).get(i).getText());
        }
        sleep(3000);
        System.out.println("Всего игр:"+driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).size());

        WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));
        Assert.assertTrue(playerlevelFilter.isDisplayed());
        Assert.assertEquals("Любой уровень", playerlevelFilter.getText());
        sleep(3000);
        playerlevelFilter.click();
        driver.findElement(By.xpath(MEDIUM_PLAYERLEVEL_ON_GAME_FILTER_XPATH)).click();
        sleep(3000);
        Assert.assertEquals("Медиум",playerlevelFilter.getText());

        Assert.assertFalse(driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).size(); i++) {
            Assert.assertEquals("Уровень - Медиум", driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).get(i).getText());
        }
        sleep(3000);
        System.out.println("Всего игр c уровнем Медиум на 30 марта:"+driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).size());

    }


    @Test
    public void assertSurveyCityNegative () throws InterruptedException {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());

        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("BVA_"));
        Assert.assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("testmyme4@gmail.com"));

        driver.findElement(By.className(PROFILE_HEADER_CLASS)).click();
        String chosenSurveyOption= driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();
        sleep(3000);
        Assert.assertEquals("Выберите районы города", driver.findElement(By.className(CHOOSEDISTRICTS_SURVEY_OPTION_CLASS)).getText());


        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();
        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(3).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(3).click();
        sleep(3000);
        Assert.assertTrue(chosenCitySurvey.contains(driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText()));
        driver.findElement(By.className(SAVECHANGES_SURVEY_CLASS)).click();

        WebElement SurveySaveNotCompleteError = driver.findElement (By.className (ERROR_MESSAGE_SAVECHANGES_SURVEY_CLASS));

        Assert.assertTrue(SurveySaveNotCompleteError.isDisplayed());

        String expectedSurveySaveNotCompleteErrorMessage = "Ответьте, пожалуйста, на все вопросы";
        String actualSurveySaveNotCompleteErrorMessage = SurveySaveNotCompleteError.getText();

        Assert.assertEquals(expectedSurveySaveNotCompleteErrorMessage, actualSurveySaveNotCompleteErrorMessage);
        System.out.println("Город Опроса после сообщения об ошибке при повторном захождении в Опрос: "+ driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        //BUG - The city in the CITY_CURRENT_SURVEY_XPATH should be the default city "Санкт-Петербург", not the newly selected city
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();


        /*
        If one needs to return the system to initial Default City Option in Survey:
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).click();
        sleep(3000);
        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
        ???
         */

        /*
        Alternatively instead of setting the system to Default Survey City reveal the bug
        via depicting the fact that the city in the survey has been saved despite the ErrorMessage:
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(PROFILE_HEADER_CLASS)).click();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();
        sleep(3000);
        Assert.assertFalse(driver.findElements(By.xpath(CITY_CURRENT_SURVEY_XPATH)).isEmpty());
        for (int i = 0; i < driver.findElements(By.xpath(CITY_CURRENT_SURVEY_XPATH)).size(); i++) {
            Assert.assertEquals("Санкт-Петербург", driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(i).getText());
        }
        ???
        */

    }



    @Test
    public void EditGenderPersonalAccount () throws InterruptedException {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys("testmyme4@gmail.com");
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys("Ilovetesting123!!");
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();


        WebElement genderField = driver.findElement(By.xpath(GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH));
        genderField.click();

        WebElement femaleGenderSelected= driver.findElement(By.xpath(GENDER_FEMALE_SELECT_PERSONAL_ACCOUNT_XPATH));
        WebElement maleGenderSelected= driver.findElement(By.xpath(GENDER_MALE_SELECT_PERSONAL_ACCOUNT_XPATH));

        String currentGenderValue = genderField.getText();

        if(genderField.getText().equals(femaleGenderSelected.getText()))
        {
            maleGenderSelected.click();
        }
        else
        {
            femaleGenderSelected.click();
        }
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH)).click();

        genderField = driver.findElement(By.xpath(GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH));  //refresh reassign variable

        Assert.assertNotEquals (currentGenderValue,genderField.getText());
        System.out.println(genderField.getText());

        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
    }


    //Locators
    public static final String GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div/div[2]";
    //public static final String SELECT_GENDER_CLASS ="Select_dropdownItem__3GhCz";
    public static final String GENDER_FEMALE_SELECT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[2]";
    public static final String GENDER_MALE_SELECT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[1]";
/*
    female
    //*[@id="root"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[2]     when selected from drop down
    //*[@id="root"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div/div[2]    visible

    male
    //*[@id="root"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[1]     when selected from drop down
    //*[@id="root"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div/div[2]      visible

    //*[@id="root"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[1]/div[2]  initially visible default
    */

    public static final String MORE_GAME_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[2]/div/div[3]/a";
    public static final String DATE_PICKER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[3]/span[2]";
    public static final String DATE_PICKER2_XPATH ="//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[11]/span[2]";
    public static final String DEMO_ARENA_FILER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div[2]/div[7]";
    public static final String ARENA_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div/div";
    public static final String ARENA_ON_GAME_CLASS = "EventCard_eventTypeRow__arena__3ljYS";


    public static final String CITY_HEADER_CLASS = "location__default";
    public static final String CITY_OPTIONS_CLASS  = "location__item";
    public static final String ALLSPORTSTYPE_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div[1]";
    public static final String FOOTBALL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div[2]/div[12]";
    public static final String SPORT_TYPE_ON_GAME_CLASS = "EventCard_sport__2ZcAA";

    public static final String ALLEVENTS_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div/div";
    public static final String EVENT_TYPE_ON_ALLEVENTS_CLASS ="EventCard_eventTypeRow__type__3TN0s";
    public static final String GAMES_ALLEVENTS_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div[2]/div[2]";
    public static final String PLAYERLEVEL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[1]/div";
    public static final String MEDIUM_PLAYERLEVEL_ON_GAME_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[2]/div[3]";
    public static final String MEDIUM_PLAYERLEVEL_GAMES_CLASS = "EventCard_level__LxpwV";

    public static final String PROFILE_HEADER_CLASS = "Tabs_tabList__1pP5W";
    public static final String SURVEY_OPTION_HEADER_CLASS = "Tabs_tab__3e8GV";

    //public static final String SURVEY_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div/div[4]";
    //*[@id="root"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[1]/p
    public static final String CHOOSEDISTRICTS_SURVEY_OPTION_CLASS ="QuizQuestion_quizQuestion__title__20Rec";
    public static final String CITY_CURRENT_SURVEY_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div";

    public static final String CITY_OPTIONS_SURVEY_CLASS  = "Select_dropdownItem__2T2FU";
    public static final String SAVECHANGES_SURVEY_CLASS  = "Quiz_quiz__button__1gMQ7";
    public static final String ERROR_MESSAGE_SAVECHANGES_SURVEY_CLASS  = "Quiz_quiz__errorBlock__I9tK3";

    //Locators
    public static final String WRONG_EMAIL_ERROR_CLASS = "form_error__2xL0z";
    public static final String WRONG_PASSWORD_ERROR_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/p";
    public static final String TOPUP_ACCOUNT_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[3]/div[3]/button";
    public static final String PROFILE_CARD_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]";
    public static final String EDIT_ACCOUNT_BUTTON_CLASS ="OrangeLink_orangeLink__34ZRK";
    public static final String SIGNOUT_ACCOUNT_BUTTON_CLASS ="LKLayout_exit__1QjSv";
    public static final String PERSONAL_ACCOUNT_XPATH="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[3]";
    public static final String CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS ="FormInput_formInputClear__3DQxO";
    public static final String EDIT_NAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[1]/input";
    public static final String SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/button";
    public static final String EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH = "/html/body/div[3]/div/div/p";
    public static final String EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH = "/html/body/div[3]/div/div/button";
    public static final String EDIT_PHONENUMBER_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[4]/input";
    public static final String ERROR_MESSAGE_PERSONAL_ACCOUNT_CLASS ="form_error__2xL0z";
    public static final String ERROR_MESSAGE_NAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/p";
    public static final String CLOSE_SIGNIN_WINDOW_BUTTON_CLASS = "modal__close";


    //Locators
    public static final String SIGNIN_BUTTON_CLASS = "header__signIn";
    public static final String INPUT_FORM_EMAIL_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/div[1]/input";
    public static final String INPUT_FORM_PASSWORD_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/div[2]/input";
    public static final String SUBMIT_LOGIN_BUTTON_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/button";
    public static final String AVATAR_HEADER_CLASS = "UserBlock_avatarWrapper__1pZ8H";
    public static final String AVATAR_NAME_CLASS = "profileText__name";
    public static final String PROFILE_CHANGED_NAME_CLASS="ProfileCard_name__2yGm-";

    //Locators
    public static final String MAIN_PHOTO_CLASS = "all-games-img";
    public static final String HEADER_LOGO_CLASS = "header__logo";
    public static final String CREATEGAME_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[3]/div/div[2]/a";
    public static final String QA_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/div/a";
    public static final String RENTPLAYGROUND_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[4]/div/div[2]/a";
    public static final String CITYLOCATION_BUTTON_CLASS = "location__default";
    public static final String TYPEOFGAME_BUTTON_CLASS = "games-list";
    public static final String FOOTBALLTYPEOFGAME_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[1]/div/div[1]/a[2]";
    public static final String COOPERATIONTITLE_TEXT_CLASS = "cooperation__title";

    //Locators
    public static final String CAMP_BUTTON_CLASS = "CampsButton_button__2sQY1";
    public static final String CAMP_TEXT1_XPATH = "//*[@id=\"root\"]/div[2]/section[1]/div/a/div/div[2]/div[1]";
    public static final String CAMP_TEXT2_CLASS = "CampsButton_title__55qxk";
    public static final String CAMP_LOGOImageButton_CLASS = "CampsButton_img__3CoXw";
}

