package com.juneit;

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
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class SploteamTest_Victoria_Diplom_old {
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

    @Test
    public void loginPositive () {
        login(properties.gmailAccount, properties.gmailPassword);
        assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        assertEquals("BVA_",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());

        logout();
    }


    @Test
    public void ArchiveEventsGameStatusCheck () { //Test #1
        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains(properties.gmailAccount));

        WebElement archiveEvents = driver.findElement(By.xpath (ARCHIVE_EVENTS_PERSONAL_ACCOUNT_XPATH));
        archiveEvents.click();
        //driver.findElement(By.xpath(ARCHIVE_EVENTS_GAMESSECTION_PERSONAL_ACCOUNT_XPATH)).click();

        List<WebElement> listOfPastGames = driver.findElements(By.className(PASTGAMECARDS_ARCHIVE_EVENTS_CLASS));

        for (WebElement pastgame : listOfPastGames) {
            Assert.assertTrue (pastgame.getText().contains("Игра состоялась"));
        }
        logout();
    }

    //Locators

    public static final String ARCHIVE_EVENTS_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div/div[5]";
    public static final String ARCHIVE_EVENTS_GAMESSECTION_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div[1]";
    public static final String ARCHIVE_PASTGAMESTATUS_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div/div/div[1]/div/div[3]/span";
    public static final String PASTGAMECARDS_ARCHIVE_EVENTS_CLASS ="EventCard_bottomRow__1CodI";


    @Test
    public void ArchiveEventsSportsTypePastGamesCountFinder () { //Test #1  Узнать количество сыгранных игр по типу спорта, пример, Футбол
        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains(properties.gmailPassword));

        WebElement archiveEvents = driver.findElement(By.xpath (ARCHIVE_EVENTS_PERSONAL_ACCOUNT_XPATH));
        archiveEvents.click();
        //driver.findElement(By.xpath(ARCHIVE_EVENTS_GAMESSECTION_PERSONAL_ACCOUNT_XPATH)).click();

        List<WebElement> listOfPastGamesBySportType = driver.findElements(By.className(PASTGAMECARDS_SPORTTYPE_ARCHIVE_EVENTS_CLASS));

        for (WebElement pastgametype : listOfPastGamesBySportType) {
            if (pastgametype.getText().contains("Футбол")) {
                int count = driver.findElements(By.className(PASTGAMECARDS_SPORTTYPE_ARCHIVE_EVENTS_CLASS)).size();
                System.out.println("Count of past games by chosen sports type is equal:" + count);
                //for (int i = 0; i < count; i++) {
                    //Assert.assertEquals("Футбол", driver.findElements(By.className(PASTGAMECARDS_SPORTTYPE_ARCHIVE_EVENTS_CLASS)).get(i).getText());
                    //System.out.println("Count of past games by chosen sports type is equal:" + listOfPastGamesBySportType);
                }
            else {
                assertNotEquals("Футбол", driver.findElement(By.className(PASTGAMECARDS_SPORTTYPE_ARCHIVE_EVENTS_CLASS)).getText());//  zero?????
                System.out.println("Count of past games by chosen sports type is equal:" + listOfPastGamesBySportType);
            }
            }


        logout();
    }

    //Locators
    public static final String PASTGAMECARDS_SPORTTYPE_ARCHIVE_EVENTS_CLASS ="EventCard_sport__2ZcAA";
    

    @Test
    public void NotificationsCheck () { //Test #2
        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains(properties.gmailPassword));

        driver.findElement(By.xpath (NOTIFICATIONS_BELL_WIDGET_ICON_XPATH)).click();

        WebElement unreadbellNotifications = driver.findElement(By.className(NOTIFICATIONS_BELL_WIDGET_LABEL_CLASS));
        int countunreadbellnotifications = Integer.parseInt(unreadbellNotifications.getText());

        WebElement unreadmessageNotifications = driver.findElement(By.xpath(NOTIFICATIONS_UNREAD_MESSAGES_XPATH));
        assertTrue(unreadmessageNotifications.isDisplayed());

        WebElement unreadmessagecountNotifications = driver.findElement(By.xpath(NOTIFICATIONS_UNREAD_COUNT_XPATH));
        int countunreadmessages = Integer.parseInt(unreadmessagecountNotifications.getText());

        if (countunreadbellnotifications == countunreadmessages) {
            System.out.println("Counts are equal: " + countunreadbellnotifications);
        } else if (countunreadbellnotifications > countunreadmessages) {
            System.out.println("unreadbellNotifications is greater: " + countunreadbellnotifications);
        } else {
            System.out.println("unreadmessagecountNotifications is greater: " + countunreadmessages);
        }
        assertEquals (countunreadbellnotifications,countunreadmessages=Integer.parseInt(unreadmessagecountNotifications.getText()));

        driver.findElement(By.xpath(NOTIFICATIONS_UNREAD_HIGHLIGHTALLBOX_XPATH)).click ();
        WebElement unreadmessagecountheaderNotifications = driver.findElement(By.xpath(NOTIFICATIONS_HEADER_SELECTEDVALUE_XPATH));
        int countunreadmessageheaderNotifications = Integer.parseInt(unreadmessagecountheaderNotifications.getText());
        assertEquals (countunreadmessageheaderNotifications,countunreadmessages=Integer.parseInt(unreadmessagecountNotifications.getText()));
        driver.findElement(By.xpath(NOTIFICATIONS_UNREAD_HIGHLIGHTALLBOX_XPATH)).click ();

        logout();
    }

    //Locators
    public static final String NOTIFICATIONS_BELL_WIDGET_ICON_XPATH ="//*[@id=\"root\"]/header/div/div/div[2]/div[1]/img";
    public static final String NOTIFICATIONS_BELL_WIDGET_LABEL_CLASS ="Widget_label__3nbvW";
    public static final String NOTIFICATIONS_UNREAD_MESSAGES_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div[2]/div[1]";
    public static final String NOTIFICATIONS_UNREAD_COUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div[2]/div[2]";
    public static final String NOTIFICATIONS_UNREAD_HIGHLIGHTALLBOX_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[1]/div/label/span[2]";
    public static final String NOTIFICATIONS_HEADER_SELECTEDVALUE_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[1]/div[1]/div/span";





    @Test
    public void CreateAndCancelOwnGameFutureDateSetFilters () { //Test #3 Создать и удалить игру в Личном кабинете на определенную дату (на 8-й день, считая от сегодня);
        // арена, вид спорта, уровень игрока - второй номер по порядку в списке выпадающего фильтра. Предварительное условие - наличие средств на личном счете минимум 5000 рублей.

        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains(properties.gmailPassword));

        driver.findElement(By.xpath(MY_GAMES_PERSONAL_ACCOUNT_XPATH)).click();

        driver.findElement(By.xpath(CREATE_OWN_GAME_PERSONAL_ACCOUNT_XPATH)).click();

        for (int i = 0; i < driver.findElements(By.className(DATE_PICKER_OWN_GAME_CLASS)).size(); i++) {
            driver.findElements(By.className(DATE_PICKER_CLASS)).get(7).click();
            }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(TABSPANEL_ARENAS_OWN_GAME_PERSONAL_ACCOUNT_CLASS)));
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(локатор для загрузки));

        //WebElement arenaFilter = driver.findElement(By.xpath(ALL_ARENAS_CREATE_OWN_GAME_PERSONAL_ACCOUNT_XPATH));
        WebElement arenaFilter = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        assertTrue(arenaFilter.isDisplayed());
        arenaFilter.click();
        WebElement selectedArena = driver.findElements(By.className(ARENA_FILER_OPTIONS_CLASS)).get(1);
        selectedArena.click();
        //assertEquals(selectedArena.getText(), arenaFilter.getText());

        WebElement sportstypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));
        assertTrue(sportstypeFilter.isDisplayed());
        sportstypeFilter.click();
        WebElement selectedSportsType = driver.findElements(By.className(SPORTS_TYPE_FILTER_OPTIONS_CLASS)).get(1);
        selectedSportsType.click();
        //assertEquals(selectedSportsType.getText(), sportstypeFilter.getText());

        WebElement alleventsFilter = driver.findElement(By.xpath(ALLEVENTS_FILTER_XPATH));
        assertTrue(alleventsFilter.isDisplayed());
        alleventsFilter.click();
        WebElement selectedEventFilter = driver.findElements(By.className(EVENTS_TYPE_FILTER_OPTIONS_CLASS)).get(1);
        selectedEventFilter.click();
        //assertEquals(selectedEventFilter.getText(), alleventsFilter.getText());

        WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));
        assertTrue(playerlevelFilter.isDisplayed());
        playerlevelFilter.click();
        WebElement selectedplayerklevelFilter = driver.findElements(By.className(PLAYER_LEVEL_FILTER_OPTIONS_CLASS)).get(1);
        selectedplayerklevelFilter.click();
        //assertEquals(selectedplayerklevelFilter.getText(), playerlevelFilter.getText());

        //assertEquals(driver.findElement(By.xpath(SELECTED_ARENA_AFTER_APPLIED_FILTERS_XPATH)).getText(),selectedArena.getText());//

        /*assertFalse(driver.findElements(By.className(CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS)).isEmpty());*/

        if (driver.findElements(By.className(CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS)).isEmpty()){
            Assert.assertTrue(driver.findElement(By.className(EMPTY_EVENT_TIME_OPTIONS_ERROR_MESSAGE_CLASS)).isDisplayed());
            Assert.assertEquals("Нет доступных вариантов. Попробуйте изменить дату или фильтр.", driver.findElement(By.className(EMPTY_EVENT_TIME_OPTIONS_ERROR_MESSAGE_CLASS)).getText());
        } else {
            driver.findElements(By.className(CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS)).get(0).click();
        }
        WebElement gametimeFilter = driver.findElement(By.xpath(TIME_FILTER_SELECTED_ARENA_OWN_GAME_XPATH));
        assertTrue(gametimeFilter.isDisplayed());
        gametimeFilter.click();
        WebElement selectedgametimeFilter = driver.findElements(By.className(TIME_FILTER_SELECTED_ARENA_OPTIONS_CLASS)).get(3);
        selectedgametimeFilter.click();
        //assertEquals(gametimeFilter.getText(), selectedgametimeFilter.getText());

        driver.findElement(By.xpath(CREATE_GAME_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(COMMON_PAGE_TITLE_GAME_CREATION_CLASS)));

        driver.findElement(By.xpath(CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_XPATH)).click();
        driver.findElement(By.xpath(NO_I_HAVE_PLAYERS_TO_PLAY_OPTION_XPATH)).click();
        //driver.findElement(By.xpath(CHOOSE_VALUE_WHICH_TYPE_OF_GAME_TO_PLAY_XPATH)).click();   //how to choose an option from hidden filter?

        WebElement typeofgameOption = driver.findElement(By.xpath(CHOOSE_VALUE_WHICH_TYPE_OF_GAME_TO_PLAY_XPATH));

        /*public boolean isClickable(WebElement typeofgameOption)
        {
            try{
                WebDriverWait wait = new WebDriverWait(driver, 6);
                wait.until(ExpectedConditions.elementToBeClickable(typeofgameOption));
                return typeofgameOption.isDisplayed() && typeofgameOption.isEnabled();
            }
            catch (Exception e){
                return false;
            }
        }
        */

        driver.findElement(By.xpath(SECOND_OPTION_DROPDOWN_TYPE_OF_GAME_TO_PLAY_XPATH)).click();

        driver.findElement(By.xpath(PAY_TO_CREATE_OWN_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(PAY_FOR_GAME_FROM_PERSONAL_ACCOUNT_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(LEAVE_CLOSED_GAME_XPATH)).click();
        driver.findElement(By.xpath(DO_YOU_WANT_TO_LEAVE_GAME_MESSAGE_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(CLOSED_GAME_CANCELLED_MESSAGE_XPATH)).isDisplayed();
        //Do we need to check go back button, whether the game is still in the list?

        logout();

        }




    /*assertFalse(driver.findElements(By.className(ARENA_ON_GAME_CLASS)).isEmpty());
       for (int i = 0; i < driver.findElements(By.className(ARENA_ON_GAME_CLASS)).size(); i++) {
           assertEquals("\"" + selectedArena + "\"", driver.findElements(By.className(ARENA_ON_GAME_CLASS)).get(i).getText());
       }*/

      /*assertEquals("Все арены", arenaFilter.getText());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(ARENA_ON_GAME_CLASS)));*/

     /*wait.until(ExpectedConditions.presenceOfElementLocated(By.className(TABSPANEL_ARENAS_OWN_GAME_PERSONAL_ACCOUNT_CLASS)));
    wait.until(ExpectedConditions.invisibilityOfElementLocated(локатор для загрузки));*/

    //Locators
    public static final String MY_GAMES_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div/div[2]";
    public static final String CREATE_OWN_GAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div[2]";

    public static final String ALL_ARENAS_CREATE_OWN_GAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div";
    //*[@id="root"]/div[2]/div[1]/div/div[1]/div
//*[@id="root"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div
    public static final String DATE_PICKER_OWN_GAME_CLASS ="NavLink_navLink__3OIyY";
    public static final String DATE_PICKER_8THDAYFROMTODAY_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[8]/span[2]";
    public static final String TABSPANEL_ARENAS_OWN_GAME_PERSONAL_ACCOUNT_CLASS="Tabs_tabPanel__N-v3z";
    public static final String SPORTS_TYPE_FILTER_OPTIONS_CLASS="GrayRoundedSelect_dropdownItem__18xe6";
    public static final String EVENTS_TYPE_FILTER_OPTIONS_CLASS= "GrayRoundedSelect_dropdownItem__18xe6";
    public static final String PLAYER_LEVEL_FILTER_OPTIONS_CLASS= "GrayRoundedSelect_dropdownItem__18xe6";

    public static final String SELECTED_ARENA_AFTER_APPLIED_FILTERS_XPATH ="//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div/div[1]/span[1]";
    public static final String CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS="create-event__time";
    public static final String EMPTY_EVENT_TIME_OPTIONS_ERROR_MESSAGE_CLASS= "search__no-results";


    public static final String LEAVE_CLOSED_GAME_XPATH="//*[@id=\"root\"]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div/div/div[2]/span";
    public static final String DO_YOU_WANT_TO_LEAVE_GAME_MESSAGE_BUTTON_XPATH="//html/body/div[3]/div/div/div/div/button[1]";
    public static final String CLOSED_GAME_CANCELLED_MESSAGE_XPATH="//*[@id=\"root\"]/div[2]/div[1]";
    public static final String CLOSED_GAME_CANCELLED_MESSAGE_CLASS="ViewEventPage_pageAlert__2Fb_Z";
    //ViewEventPage_pageAlert__2Fb_Z ViewEventPage_error__MsHTw//
    public static final String TIME_FILTER_SELECTED_ARENA_OWN_GAME_XPATH="//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div/div[3]/div[2]";
    public static final String TIME_FILTER_SELECTED_ARENA_OPTIONS_CLASS="Select_dropdownItem__2T2FU";
    public static final String CREATE_GAME_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div/div[3]/div[4]/button";
    public static final String COMMON_PAGE_TITLE_GAME_CREATION_CLASS="Common_pageTitle__6GYuY";
    public static final String CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[1]/div/div/div[1]/div";
    public static final String NO_I_HAVE_PLAYERS_TO_PLAY_OPTION_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[1]/div[1]/div/div[1]/div";
    public static final String CHOOSE_VALUE_WHICH_TYPE_OF_GAME_TO_PLAY_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[2]/div/div/div[1]/div";
    public static final String SECOND_OPTION_DROPDOWN_TYPE_OF_GAME_TO_PLAY_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[1]/div[1]/div/div[1]/div";
    public static final String PAY_TO_CREATE_OWN_GAME_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[6]/button";
    public static final String PAY_FOR_GAME_FROM_PERSONAL_ACCOUNT_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div/div[2]/div/div[4]/span[3]/form[1]/button[2]";



    @Test
    public void districtsdatamassivecheckSanktPetersburgSurvey () throws InterruptedException { //Test #4
        login("testmyme1@gmailcom", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(PROFILE_HEADER_CLASS)).click();

        String[] arrData = {"Alpha", "Beta", "Gamma", "Delta", "Sigma"};

        String chosenSurveyOption= driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CITY_CURRENT_SURVEY_XPATH)));
        //wait.until(ExpectedConditions.invisibilityOfElementLocated(локатор для загрузки));
        sleep(3000);//TS wait


        //assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();

        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).click();

        sleep(3000);//TS replace with wait
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CITY_CURRENT_SURVEY_XPATH)));

        assertEquals(chosenCitySurvey,driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());

        /*
        String[] arrData = {"Адмиралтейский", "Василеостровский", "Выборгский", "Калининский", "Кировский", "Колпинский", "Красногвардейский", "Красносельский", "Кронштадтский", "Курортный","Московский", "Невский", "Область", "Петроградский", "Петродворцовый", "Приморский","Пушкинский", "Фрунзенский", "Центральный"};
        for(int i = 0; i< arrData.length; i++){
            System.out.println(arrData[i]);
        }

        class UsingForEach {
            public static void main(String[] args) {
                String[] arrData = {"Alpha", "Beta", "Gamma", "Delta", "Sigma"};
                //The conventional approach of using the for loop
                System.out.println("Using conventional For Loop:");
                for(int i=0; i< arrData.length; i++){
                    System.out.println(arrData[i]);
                }
                System.out.println("\nUsing Foreach loop:");
                //The optimized method of using the for loop - also called the foreach loop
                for (String strTemp : arrData){
                    System.out.println(strTemp);
                }
            }
        } */

        driver.findElement(By.className(SAVECHANGES_SURVEY_CLASS)).click();


        logout();

    }



    @Test
    public void EditNamePersonalAccount () {
        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();

        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_XPATH)).click();
        driver.navigate().refresh();
        assertEquals("BVA!!!",driver.findElement(By.className(AVATAR_NAME_CLASS)).getText());
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        assertEquals("BVA!!!",driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
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
        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath (EDIT_PHONENUMBER_PERSONAL_ACCOUNT_XPATH)).sendKeys("1");
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        assertEquals("Неверное значение",driver.findElement(By.className(ERROR_MESSAGE_PERSONAL_ACCOUNT_CLASS)).getText());
        driver.navigate().refresh();
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        assertFalse(driver.findElement(By.className(AVATAR_NAME_CLASS)).getText().contains("BVA!!!"));
        logout();
    }

    @Test
    public void EditEmptyNamePersonalAccount () {
        login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className (CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
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
        login("testmyme1@gmailcom", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        driver.findElement(By.className(PROFILE_HEADER_CLASS)).click();
        String chosenSurveyOption= driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();
        sleep(3000);//TS wait
        //assertEquals("Выберите районы города", driver.findElement(By.className(CHOOSEDISTRICTS_SURVEY_OPTION_CLASS)).getText());//can be skipped
        assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();
        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(3).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(3).click();
        sleep(3000);//TS replace with wait
        assertTrue(chosenCitySurvey.contains(driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText()));//can be replaced with assertEquals
        driver.findElement(By.className(SAVECHANGES_SURVEY_CLASS)).click();

        WebElement SurveySaveNotCompleteError = driver.findElement (By.className (ERROR_MESSAGE_SAVECHANGES_SURVEY_CLASS));

        assertTrue(SurveySaveNotCompleteError.isDisplayed());

        String expectedSurveySaveNotCompleteErrorMessage = "Ответьте, пожалуйста, на все вопросы";
        String actualSurveySaveNotCompleteErrorMessage = SurveySaveNotCompleteError.getText();

        assertEquals(expectedSurveySaveNotCompleteErrorMessage, actualSurveySaveNotCompleteErrorMessage);
        System.out.println("Город Опроса после сообщения об ошибке при повторном захождении в Опрос: "+ driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        //BUG - The city in the CITY_CURRENT_SURVEY_XPATH should be the default city "Санкт-Петербург", not the newly selected city
        //TS согласна, в рамках выполнения заданий посчитаем что ваш менеджер говорит что фиксить не будем пусть так работает. Тогда добавим шаг чтобы вернуть изначальный город
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();




    }





    @Test
    public void assertArenaFilterForEachDate () throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        sleep(2000);
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


    @Test
    public void assertArenaFilterPlayerLevel () throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAME_BUTTON_XPATH)).click();
        sleep(2000);
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
        driver.findElement(By.className(AVATAR_HEADER_CLASS)).click();
        driver.findElement(By.className (SIGNOUT_ACCOUNT_BUTTON_CLASS)).click();

    }





    public static final String USER_NAME = "BVA_";
    //Locators
    public static final String GENDER_CURRENT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div/div[2]";
    //public static final String SELECT_GENDER_CLASS ="Select_dropdownItem__3GhCz";
    public static final String GENDER_FEMALE_SELECT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[2]";
    public static final String GENDER_MALE_SELECT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[1]";


    public static final String MORE_GAME_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[2]/div/div[3]/a";
    public static final String DATE_PICKER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[3]/span[2]";
    public static final String DATE_PICKER2_XPATH ="//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[9]/span[2]";
    public static final String DEMO_ARENA_FILER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div[2]/div[7]";
    public static final String ARENA_FILER_OPTIONS_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";
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

    // Locators
    public static final String DATE_PICKER_CLASS = "NavLink_navLink__text__zfi3X";
    public static final String EMPTY_GAMES_ERROR_MESSAGE_CLASS  = "SearchPage_tabPanelContainer__1Lxhs";
    public static final String ARENA_FILER_CLASS = "GrayRoundedSelect_controlValue__2EqXC";
    public static final String PLAYER_LEVEL_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";
    //*[@id="root"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[2]/div[3]
}

