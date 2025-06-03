import com.juneit.PropertiesLoader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementSelectionStateToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class SploteamTest_Victoria_refactoringbyT {
    private final PropertiesLoader properties = new PropertiesLoader ();
    private final WebDriver driver = new ChromeDriver ();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

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
        assertTrue(driver.findElement (By.className (MAIN_PHOTO_CLASS)).isDisplayed());
        assertTrue(driver.findElement (By.className (HEADER_LOGO_CLASS)).isDisplayed());
        assertTrue(driver.findElement (By.xpath (CREATEGAME_BUTTON_XPATH)).isDisplayed());
        assertTrue(driver.findElement (By.xpath (QA_BUTTON_XPATH)).isDisplayed());
        assertTrue(driver.findElement (By.xpath (RENTPLAYGROUND_BUTTON_XPATH)).isDisplayed());
        assertTrue(driver.findElement (By.className (CITYLOCATION_BUTTON_CLASS)).isDisplayed());
        assertEquals("Санкт-Петербург",driver.findElement(By.className(CITYLOCATION_BUTTON_CLASS)).getText());
        assertTrue(driver.findElement (By.className  (TYPEOFGAME_BUTTON_CLASS)).isDisplayed());
        assertTrue(driver.findElement (By.xpath (FOOTBALLTYPEOFGAME_BUTTON_XPATH)).isDisplayed());
        assertEquals("Футбол",driver.findElement(By.xpath(FOOTBALLTYPEOFGAME_BUTTON_XPATH)).getText());
        assertEquals("Приглашаем к сотрудничеству\nвладельцев площадок и тренеров",driver.findElement(By.className(COOPERATIONTITLE_TEXT_CLASS)).getText());
    }

    @Test public void assertCampSectionIsDisplayed () {
        assertTrue(driver.findElement (By.className (CAMP_BUTTON_CLASS)).isDisplayed());
        assertEquals("Новинка!",driver.findElement(By.xpath(CAMP_TEXT1_XPATH)).getText());
        assertEquals("Спортивные кемпы",driver.findElement(By.className(CAMP_TEXT2_CLASS)).getText());
        assertTrue("Image is not displayed", driver.findElement(By.className(CAMP_LOGOImageButton_CLASS)).isDisplayed());
    }

    @Test public void numberOfCampsPerMonthIsDisplayed () {
        assertTrue(driver.findElement(By.className(CAMP_BUTTON_CLASS)).isDisplayed());
        driver.findElement(By.className(CAMP_BUTTON_CLASS)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(MONTH_PICKER_CAMPS_FILTER_CLASS)));
        assertTrue(driver.findElement(By.className(TEXT_SPORTSCAMPS_CLASS)).isDisplayed());
        assertEquals("2024",driver.findElements(By.className(YEAR_PICKER_CAMPS_FILTER_CLASS)).get(2).getText());
        driver.findElements(By.className(YEAR_PICKER_CAMPS_FILTER_CLASS)).get(0).click();
        List <WebElement> campsYears = driver.findElements(By.className(YEAR_PICKER_CAMPS_FILTER_CLASS));
        List <WebElement> campsMonths = driver.findElements(By.className(MONTH_PICKER_CAMPS_FILTER_CLASS));
        List <WebElement> campCards;
        for (int i = 0; i < campsYears.size(); i++) {
            campsYears.get(i).click();

                for (int j = 1; j < campsMonths.size(); j++) {
                    campsMonths.get(j).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CAMPS_SEARCH_PAGE_RESULTS_CLASS)));
                    campCards = driver.findElements(By.className(CARD_COUNT_FILTER_CLASS));
                    if (campCards.isEmpty()) {
                        System.out.println("Number of camps for " + campsYears.get(i).getText() + " " + campsMonths.get(j).getText() + ": " + "На " + campsMonths.get(j).getText() + " месяц кэмпы не доступны.");
                        assertTrue(driver.findElement(By.xpath(MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH)).isDisplayed());
                        //assertEquals("Упс... не удалось найти ни одного кемпа.",  driver.findElement(By.xpath(MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH)).getText());
                    }
                    else{
                            System.out.println("Number of camps for " + campsYears.get(i).getText() + " " + campsMonths.get(j).getText() + " = " + campCards.size());
                        }
                    }

            }
    }


    @Test
    public void loginPositive () {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        logout();
    }

    @Test
    public void loginWithWrongEmail () {
        login("a54ysjtdi76ryi@gmail.com", "Ilovetesting123!!");

        WebElement wrongEmailError = driver.findElement (By.className (WRONG_EMAIL_ERROR_CLASS));

        assertTrue(wrongEmailError.isDisplayed());

        String expectedWrongEmailErrorMessage = "Такой логин или пароль не найдены";
        String actualWrongEmailErrorMessage = wrongEmailError.getText();

        assertEquals(expectedWrongEmailErrorMessage, actualWrongEmailErrorMessage);
    }

    @Test
    public void loginWithEmptyEmailPassword () {
        login("","");
        assertTrue(driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).isDisplayed());
        assertEquals("Войти",driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).getText());
    }

    @Test
    public void loginWithIncorrectFormatEmail () {
        login("testmyme1@gmailcom", "Ilovetesting123!!");

        WebElement wrongEmailError = driver.findElement (By.className (WRONG_EMAIL_ERROR_CLASS));
        assertTrue(wrongEmailError.isDisplayed());

        String expectedWrongEmailErrorMessage = "Неверное значение";
        String actualWrongEmailErrorMessage = wrongEmailError.getText();

        assertEquals(expectedWrongEmailErrorMessage, actualWrongEmailErrorMessage);
        driver.findElement(By.className(CLOSE_SIGNIN_WINDOW_BUTTON_CLASS)).click();
        assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }

    @Test
    public void loginWithWrongSixSymbolsPassword () {
        login("testmyme1@gmail.com", "Ilove");

        WebElement wrongPasswordError = driver.findElement (By.xpath (WRONG_PASSWORD_ERROR_XPATH));

        assertTrue(wrongPasswordError.isDisplayed());

        String expectedWrongPasswordErrorMessage = "Неверное значение";
        String actualWrongPasswordErrorMessage = wrongPasswordError.getText();

        assertEquals(expectedWrongPasswordErrorMessage, actualWrongPasswordErrorMessage);
        driver.findElement(By.className(CLOSE_SIGNIN_WINDOW_BUTTON_CLASS)).click();
        assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }

    @Test
    public void loginPersonalAccount () {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains(USER_NAME));
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("testmyme4@gmail.com"));
        assertEquals("Пополнить счет",driver.findElement(By.xpath(TOPUP_ACCOUNT_BUTTON_XPATH)).getText());
        assertTrue(driver.findElement(By.xpath(PERSONAL_ACCOUNT_XPATH)).getText().contains("Личный счёт"));
        assertEquals("Редактировать",driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).getText());
        logout();
    }

    @Test
    public void EditNamePersonalAccount () {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_FORMINPUT_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        assertEquals("BVA!!!",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        assertEquals("BVA!!!",driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_FORMINPUT_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys(USER_NAME);
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        assertEquals(USER_NAME,driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        assertEquals(USER_NAME ,driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        logout();
    }

    @Test
    public void EditNameOneDigitPhoneNumberPersonalAccount () {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_FORMINPUT_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath (EDIT_PHONENUMBER_PERSONAL_ACCOUNT_XPATH)).sendKeys("1");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Неверное значение",driver.findElement(By.className(ERROR_MESSAGE_PERSONAL_ACCOUNT_CLASS)).getText());
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        assertFalse(driver.findElement(By.className(AVATAR_NAME_CLASS)).getText().contains("BVA!!!"));
        logout();
    }

    @Test //HW5
    public void EditDateOfBirthPersonalAccount () {  //HW5
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();

        WebElement windowPicker2 = driver.findElements(By.className(CLEAR_FORM_DATEOFBIRTH_CLASS)).get(1);
        windowPicker2.click();
        if (windowPicker2.getAttribute("value").isEmpty()){
            windowPicker2.sendKeys("12.12.2012");

        } else {
            driver.findElement(By.xpath(CLEAR_BUTTON_FORM_DATEOFBIRTH_XPATH)).click();
            windowPicker2.click();
            windowPicker2.sendKeys("12.12.2012");
        }

        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(ACCOUNT_UPDATE_SUCCESSFUL_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        driver.findElements(By.className(CLEAR_FORM_DATEOFBIRTH_CLASS)).get(1).click();
        windowPicker2=driver.findElements(By.className(CLEAR_FORM_DATEOFBIRTH_CLASS)).get(1);
        windowPicker2.click();
        assertEquals("12.12.2012", windowPicker2.getAttribute("value"));
        logout();
    }


    @Test //HW5
    public void EditDateOfBirthNegativePersonalAccount () {   //HW5
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();

        WebElement windowPicker2 = driver.findElements(By.className(CLEAR_FORM_DATEOFBIRTH_CLASS)).get(1);
        windowPicker2.click();
        if (windowPicker2.getAttribute("value").isEmpty()){
            windowPicker2.sendKeys("01.01.1900");

        } else {
            driver.findElement(By.xpath(CLEAR_BUTTON_FORM_DATEOFBIRTH_XPATH)).click();
            windowPicker2.click();
            windowPicker2.sendKeys("01.01.1900");
        }

        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        driver.navigate().refresh();
        driver.findElements(By.className(CLEAR_FORM_DATEOFBIRTH_CLASS)).get(1).click();
        windowPicker2=driver.findElements(By.className(CLEAR_FORM_DATEOFBIRTH_CLASS)).get(1);
        windowPicker2.click();
        windowPicker2.getAttribute("value");
        System.out.println(windowPicker2.getAttribute("value"));
        String windowPicker2Value = windowPicker2.getAttribute("value");
        assertNotEquals("01.01.1900", windowPicker2Value);
        logout();
    }

    @Test
    public void EditEmptyNamePersonalAccount () {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_FORMINPUT_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Выберите значение",driver.findElement(By.xpath(ERROR_MESSAGE_NAME_PERSONAL_ACCOUNT_XPATH)).getText());
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        assertTrue(driver.findElement(By.className(AVATAR_NAME_CLASS)).getText().contains(USER_NAME));
        logout();
    }

    @Test
    public void assertArenaFilter () {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_PICKER_XPATH)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(ARENA_ON_GAME_CLASS)));
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(локатор для загрузки));

        WebElement arenaFilter = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        assertTrue(arenaFilter.isDisplayed());

        assertEquals("Все арены", arenaFilter.getText());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(ARENA_ON_GAME_CLASS)));
        System.out.println("Number of games before arena filter is" + driver.findElement(By.className(ARENA_ON_GAME_CLASS)));
        arenaFilter.click();
        String selectedArena = driver.findElements(By.className(ARENA_FILER_OPTIONS_CLASS)).get(0).getText();
        driver.findElements(By.className(ARENA_FILER_OPTIONS_CLASS)).get(0).click();
        assertEquals(selectedArena, arenaFilter.getText());

        assertFalse(driver.findElements(By.className(ARENA_ON_GAME_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size(); i++) {
            assertEquals("\"" + selectedArena + "\"", driver.findElements(By.className(ARENA_ON_GAME_CLASS)).get(i).getText());
        }
    }

    @Test
    public void assertSportTypeFilter () {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_PICKER_XPATH)).click();

        WebElement sportstypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));//TS nname
        assertTrue(sportstypeFilter.isDisplayed());
        assertEquals("Все виды спорта", sportstypeFilter.getText());
        System.out.println("Всего игр:"+driver.findElements(By.className(TYPEOFGAME_BUTTON_CLASS)).size());
        sportstypeFilter.click();
        driver.findElement(By.xpath(FOOTBALL_FILTER_XPATH)).click();//re-write test for index .get(0) and less hard coded values
        assertEquals("Футбол", sportstypeFilter.getText());

        assertFalse(driver.findElements(By.className(SPORT_TYPE_ON_GAME_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(SPORT_TYPE_ON_GAME_CLASS)).size(); i++) {
            assertEquals("Футбол", driver.findElements(By.className(SPORT_TYPE_ON_GAME_CLASS)).get(i).getText());
        }
    }

    @Test
    public void assertChangeCity (){
        //create string for Санкт-Петербург
        assertEquals("Санкт-Петербург", driver.findElement(By.className(CITY_HEADER_CLASS)).getText());
        driver.findElement(By.className(CITY_HEADER_CLASS)).click();
        String chosenCity = driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(3).getText();
        System.out.println(chosenCity);
        driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(3).click();
        wait.until(ExpectedConditions.not(textToBe(By.className(CITY_HEADER_CLASS), "Санкт-Петербург"))); //wait
        assertTrue(chosenCity.contains(driver.findElement(By.className(CITY_HEADER_CLASS)).getText()));
        driver.findElement(By.className(CITY_HEADER_CLASS)).click();
        driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(1).click();
        wait.until(ExpectedConditions.textToBe(By.className(CITY_HEADER_CLASS), "Санкт-Петербург")); //wait
    }


    @Test
    public void assertDateGamePlayerLevelFilter () throws InterruptedException { //TS a lot of hardcoded values. Refactor together to be more dynamic like arena filter test
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_PICKER2_XPATH)).click();

        WebElement alleventsFilter = driver.findElement(By.xpath(ALLEVENTS_FILTER_XPATH));//TS name
        assertTrue(alleventsFilter.isDisplayed());
        assertEquals("Все события", alleventsFilter.getText());
        sleep(3000);//TS wait

        alleventsFilter.click();
        driver.findElement(By.xpath(GAMES_ALLEVENTS_FILTER_XPATH)).click();//

        sleep(3000);//TS wait
        assertEquals("Игры",alleventsFilter.getText());

        assertFalse(driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).size(); i++) {
            assertEquals("Игра", driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).get(i).getText());
        }
        sleep(3000);//TS wait
        System.out.println("Всего игр:"+driver.findElements(By.className(EVENT_TYPE_ON_ALLEVENTS_CLASS)).size());

        WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));//TS name
        assertTrue(playerlevelFilter.isDisplayed());
        assertEquals("Любой уровень", playerlevelFilter.getText());
        sleep(3000);//TS wait
        playerlevelFilter.click();
        driver.findElement(By.xpath(MEDIUM_PLAYERLEVEL_ON_GAME_FILTER_XPATH)).click();
        sleep(3000);//TS wait
        assertEquals("Медиум",playerlevelFilter.getText());

        assertFalse(driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).size(); i++) {
            assertEquals("Уровень - Медиум", driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).get(i).getText());
        }
        sleep(3000);
        System.out.println("Всего игр c уровнем Медиум на 30 марта:"+driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).size());

    }


    @Test
    public void assertSurveyCityNegative () throws InterruptedException {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(PROFILE_HEADER_CLASS)).click();
        sleep(3000);//TS wait
        String chosenSurveyOption= driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CITY_CURRENT_SURVEY_XPATH)));
        sleep(3000);//TS wait
        assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();
        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(4).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(4).click();
        sleep(3000);//TS replace with wait
        assertEquals(chosenCitySurvey,driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.className(SAVECHANGES_SURVEY_CLASS)).click();

        WebElement SurveySaveNotCompleteError = driver.findElement (By.className (ERROR_MESSAGE_SAVECHANGES_SURVEY_CLASS));

        assertTrue(SurveySaveNotCompleteError.isDisplayed());

        String expectedSurveySaveNotCompleteErrorMessage = "Ответьте, пожалуйста, на все вопросы";
        String actualSurveySaveNotCompleteErrorMessage = SurveySaveNotCompleteError.getText();

        assertEquals(expectedSurveySaveNotCompleteErrorMessage, actualSurveySaveNotCompleteErrorMessage);
        System.out.println("Город Опроса после сообщения об ошибке при повторном захождении в Опрос: "+ driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());

        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();
        sleep(3000);
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).click();
        driver.navigate().refresh();
        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        System.out.println(driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        //BUG - The city in the CITY_CURRENT_SURVEY_XPATH should be the default city "Санкт-Петербург", not the newly selected city
        //TS согласна, в рамках выполнения заданий посчитаем что ваш менеджер говорит что фиксить не будем пусть так работает. Тогда добавим шаг чтобы вернуть изначальный город
        //driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
        logout();
    }


    @Test
    public void EditGenderPersonalAccount () {//TS получается что изменение пола просиходит только один раз а второй пол остается не проверенным? давай вместе перепишем на цикл
        login("testmyme1@gmailcom", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        //replace steps to check both gender

        WebElement genderField = driver.findElement(By.xpath(GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH));
        genderField.click();

        WebElement femaleGenderSelected= driver.findElement(By.xpath(GENDER_FEMALE_SELECT_PERSONAL_ACCOUNT_XPATH));
        WebElement maleGenderSelected= driver.findElement(By.xpath(GENDER_MALE_SELECT_PERSONAL_ACCOUNT_XPATH));

        String currentGenderValue = genderField.getText();

        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH)).click();

        genderField = driver.findElement(By.xpath(GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH));  //refresh reassign variable

        Assert.assertNotEquals (currentGenderValue,genderField.getText());
        System.out.println(genderField.getText());

        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();// logout
    }


    @Test
    public void assertArenaFilterForEachDate () throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(ARENA_FILER_CLASS)));
        //sleep(2000);
        WebElement arenaFilter = driver.findElements(By.className(ARENA_FILER_CLASS)).get(0);
        Assert.assertTrue(arenaFilter.isDisplayed());
        arenaFilter.click();
        driver.findElement(By.xpath(DEMO_ARENA_FILER_XPATH)).click();
        Assert.assertEquals("Demo", arenaFilter.getText());
        for (int i = 0; i < driver.findElements(By.className(DATE_PICKER_CLASS)).size(); i++) {
            driver.findElements(By.className(DATE_PICKER_CLASS)).get(i).click();
            if (driver.findElements(By.className(ARENA_ON_GAME_CLASS)).isEmpty()){
                Assert.assertTrue(driver.findElement(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)).isDisplayed());
                Assert.assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.", driver.findElement(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)).getText());
            } else {
                System.out.println("Number of games  before arena filter is" + driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size());
                for (int j = 0; j < driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size(); j++) {
                    Assert.assertEquals("\"Demo\"", driver.findElements(By.className(ARENA_ON_GAME_CLASS)).get(j).getText());
                }
            }
        }
    }

    @Test  //HW5
    public void assertArenaFilterForParticularDate () throws InterruptedException { //HW5
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(DATE_PICKER_CLASS)));

        for (int i = 0; i < driver.findElements(By.className(DATE_PICKER_CLASS)).size(); i++) {
            driver.findElements(By.className(DATE_PICKER_CLASS)).get(11).click();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)));

        WebElement arenaFilter = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        assertTrue(arenaFilter.isDisplayed());
        arenaFilter.click();

        for (int j = 0; j < driver.findElements(By.className(ARENA_FILER_OPTIONS_CLASS)).size(); j++) {
            driver.findElements(By.className(ARENA_FILER_OPTIONS_CLASS)).get(j).click();
            if (driver.findElements(By.className(CARD_COUNT_FILTER_CLASS)).isEmpty()){
                System.out.println("Number of games: " + driver.findElements(By.className(CARD_COUNT_FILTER_CLASS)).size());
                Assert.assertTrue(driver.findElement(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)).isDisplayed());
                Assert.assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.", driver.findElement(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)).getText());
            } else {
                    System.out.println("Number of games: " + driver.findElements(By.className(CARD_COUNT_FILTER_CLASS)).size());}
            }
        }

    @Test
    public void assertArenaFilterPlayerLevel () throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        sleep(2000);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className(ARENA_FILER_CLASS)));
        WebElement arenaFilter = driver.findElements(By.className(ARENA_FILER_CLASS)).get(2);
        Assert.assertTrue(arenaFilter.isDisplayed());
        arenaFilter.click();
        driver.findElement(By.xpath(MEDIUM_LEVEL_FILTER_XPATH)).click();
        Assert.assertEquals("Медиум", arenaFilter.getText());
        for (int i = 0; i < driver.findElements(By.className(DATE_PICKER_CLASS)).size(); i++) {
            driver.findElements(By.className(DATE_PICKER_CLASS)).get(i).click();
            if (driver.findElements(By.className(ARENA_ON_GAME_CLASS)).isEmpty()){
                Assert.assertTrue(driver.findElement(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)).isDisplayed());
                Assert.assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.", driver.findElement(By.className(EMPTY_GAMES_ERROR_MESSAGE_CLASS)).getText());
            } else {
                System.out.println("Number of games  before arena filter is" + driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size());
                for (int j = 0; j < driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size(); j++) {
                    assertEquals("Уровень - Медиум", driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).get(j).getText());
                }
            }
        }
    }

    /*
    WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));//TS name
    assertTrue(playerlevelFilter.isDisplayed());
    assertEquals("Любой уровень", playerlevelFilter.getText());
    sleep(3000);//TS wait
        playerlevelFilter.click();
        driver.findElement(By.xpath(MEDIUM_PLAYERLEVEL_ON_GAME_FILTER_XPATH)).click();
    sleep(3000);//TS wait
    assertEquals("Медиум",playerlevelFilter.getText());

    assertFalse(driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).isEmpty());
        for (int i = 0; i < driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).size(); i++) {
        assertEquals("Уровень - Медиум", driver.findElements(By.className(MEDIUM_PLAYERLEVEL_GAMES_CLASS)).get(i).getText());
    }
    sleep(3000);
        System.out.println("Всего игр c уровнем Медиум на 30 марта:"+driver.findElements(By.c
    */

    @Test
    public void assertChangeCityRefactor () {
        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.className(CITY_HEADER_CLASS)).getText());
        driver.findElement(By.className(CITY_HEADER_CLASS)).click();
        String chosenCity = driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(6).getText();
        int index = chosenCity.indexOf("\n");
        String onlyCity = chosenCity.substring(0, index);
        driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(6).click();
        wait.until(ExpectedConditions.textToBe(By.className(CITY_HEADER_CLASS), onlyCity)); //wait
        driver.findElement(By.className(CITY_HEADER_CLASS)).click();
        driver.findElements(By.className(CITY_OPTIONS_CLASS)).get(1).click();
        wait.until(ExpectedConditions.textToBe(By.className(CITY_HEADER_CLASS),"Санкт-Петербург")); //wait
    }

    //Extracted methods
    private void login(String userEmail, String userPassword) {
        driver.findElement(By.className (SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(INPUT_FORM_EMAIL_XPATH)).sendKeys(userEmail);
        driver.findElement(By.xpath(INPUT_FORM_PASSWORD_XPATH)).sendKeys(userPassword);
        driver.findElement(By.xpath (SUBMIT_LOGIN_BUTTON_XPATH)).click();
    }

    private void logout(){
        //добавить шаг зайти в личный кабинет
        driver.findElement(By.className(AVATAR_HEADER_CLASS)).click();
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();
    }

    public static final String USER_NAME = "BVA_";
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
    public static final String DATE_PICKER2_XPATH ="//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[9]/span[2]";
    public static final String DEMO_ARENA_FILER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div[2]/div[7]";
    public static final String ARENA_FILER_OPTIONS_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";
    public static final String ARENA_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div/div";
    public static final String ARENA_ON_GAME_CLASS = "EventCard_eventTypeRow__arena__3ljYS";
    public static final String TABSPANEL_ARENAS_OWN_GAME_PERSONAL_ACCOUNT_CLASS="Tabs_tabPanel__N-v3z";


    // Locators
    //public static final String DATE_PICKER_CLASS = "NavLink_navLink__text__zfi3X";
    public static final String EMPTY_GAMES_ERROR_MESSAGE_CLASS  = "SearchPage_tabPanelContainer__1Lxhs";
    public static final String ARENA_FILER_CLASS = "GrayRoundedSelect_controlValue__2EqXC";
    public static final String PLAYER_LEVEL_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";
    //*[@id="root"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[2]/div[3]


    public static final String CITY_HEADER_CLASS = "location__default";
    public static final String CITY_OPTIONS_CLASS  = "location__item";
    public static final String ALLSPORTSTYPE_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div[1]";
    public static final String FOOTBALL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div[2]/div[12]";
    public static final String SPORT_TYPE_ON_GAME_CLASS = "EventCard_sport__2ZcAA";




    //public static final String DATE_PICKER_GAME_CLASS ="NavLink_navLink__3OIyY";
    public static final String DATE_PICKER_CLASS = "NavLink_navLink__text__zfi3X";
    //public static final String ARENA_FILTER_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";


    public static final String ALLEVENTS_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div/div";
    public static final String EVENT_TYPE_ON_ALLEVENTS_CLASS ="EventCard_eventTypeRow__type__3TN0s";
    public static final String GAMES_ALLEVENTS_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div[2]/div[2]";
    public static final String PLAYERLEVEL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[1]/div";
    public static final String MEDIUM_LEVEL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[2]/div[3]";

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
    public static final String CLEAR_FORMINPUT_PERSONAL_ACCOUNT_BUTTON_CLASS ="FormInput_formInputClear__3DQxO";
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

    public static final String MONTH_PICKER_CAMPS_FILTER_CLASS="MonthSwitcher_item__2HxRH";
    public static final String YEAR_PICKER_CAMPS_FILTER_CLASS="YearSwitcher_year__3NlNA";
    public static final String TEXT_SPORTSCAMPS_CLASS="CampsSearchPage_pageTitle__1e8aL";
    public static final String MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH= "/html/body/div[1]/div[2]/div[2]/div/div/div[1]/div[1]";
    public static final String CARD_COUNT_FILTER_CLASS="Card_card__2jlaB";
    public static final String CAMPS_SEARCH_PAGE_RESULTS_CLASS="CampsSearchPage_searchResults__Yio_Q";

    public static final String CAMP_BUTTON_CLASS = "CampsButton_button__2sQY1"; //HW5 specific
    public static final String CAMP_TEXT1_XPATH = "//*[@id=\"root\"]/div[2]/section[1]/div/a/div/div[2]/div[1]"; //HW5 specific
    public static final String CAMP_TEXT2_CLASS = "CampsButton_title__55qxk"; //HW5 specific
    public static final String CAMP_LOGOImageButton_CLASS = "CampsButton_img__3CoXw"; //HW5 specific


    public static final String CLEAR_BUTTON_FORM_DATEOFBIRTH_XPATH = "/html/body/div[1]/div[2]/div/div[3]/div/div[2]/form/div[3]/span";
    public static final String ACCOUNT_UPDATE_SUCCESSFUL_MESSAGE_XPATH = "/html/body/div[3]/div/div/p";
    public static final String CLEAR_FORM_DATEOFBIRTH_CLASS = "FormInput_formInputField__1HTcx";
}

