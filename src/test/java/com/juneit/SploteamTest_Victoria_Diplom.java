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
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class SploteamTest_Victoria_Diplom {
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

    @Test
    //Test #1 Проверка статуса отображенных в Архиве игр, статус "Игра состоялась"

    public void ArchiveEventsGameStatusCheck () {
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("testmyme4@gmail.com"));

        WebElement archiveEvents = driver.findElement(By.xpath (ARCHIVE_EVENTS_PERSONAL_ACCOUNT_XPATH));
        archiveEvents.click();

        List<WebElement> listOfPastGames = driver.findElements(By.className(PASTGAMECARDS_ARCHIVE_EVENTS_CLASS));

        for (WebElement pastgame : listOfPastGames) {
            Assert.assertTrue (pastgame.getText().contains("Игра состоялась"));
        }
        logout();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(SIGNIN_BUTTON_CLASS)));
    }

    @Test
    /*
    //Test #2 Проверка, что цифра на колокольчике Уведомлений совпадает с цифрой количества
              непрочитанных сообщений в Уведомлениях на странице Личного кабинета
    */
    public void NotificationsCheck () {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(SIGNIN_BUTTON_CLASS)));
        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("testmyme4@gmail.com"));

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
        assertEquals (countunreadbellnotifications,countunreadmessages);

        driver.findElement(By.xpath(NOTIFICATIONS_UNREAD_HIGHLIGHTALLBOX_XPATH)).click ();
        WebElement unreadmessagecountheaderNotifications = driver.findElement(By.xpath(NOTIFICATIONS_HEADER_SELECTEDVALUE_XPATH));
        int countunreadmessageheaderNotifications = Integer.parseInt(unreadmessagecountheaderNotifications.getText());
        countunreadmessages = Integer.parseInt(unreadmessagecountNotifications.getText());
        assertEquals(countunreadmessageheaderNotifications, countunreadmessages);
        driver.findElement(By.xpath(NOTIFICATIONS_UNREAD_HIGHLIGHTALLBOX_XPATH)).click ();

        logout();

    }

    @Test
    /*Test #3 Создать и покинуть закрытую игру в Личном кабинете на определенную дату;
    (на 8-й день, считая от сегодня, предварительное условие - наличие свободных слотов для создания игры);
    арена, вид спорта, уровень игрока - второй номер по порядку в списке выпадающего фильтра.
    Предварительное условие - наличие средств на личном счете минимум 5000 рублей.*/

    public void CreateAndLeaveOwnGameFutureDateSetFilters () throws InterruptedException {

        login("testmyme4@gmail.com", "Ilovetesting123!!");
        driver.findElement(By.className (AVATAR_NAME_CLASS)).click();
        assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("testmyme4@gmail.com"));

        driver.findElement(By.xpath(MY_GAMES_PERSONAL_ACCOUNT_XPATH)).click();

        driver.findElement(By.xpath(CREATE_OWN_GAME_PERSONAL_ACCOUNT_XPATH)).click();

        for (int i = 0; i < driver.findElements(By.className(DATE_PICKER_OWN_GAME_CLASS)).size(); i++) {
            driver.findElements(By.className(DATE_PICKER_CLASS)).get(7).click();
            }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(TABSPANEL_ARENAS_OWN_GAME_PERSONAL_ACCOUNT_CLASS)));

        WebElement arenaFilterElement = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        assertTrue(arenaFilterElement.isDisplayed());
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 3));
        wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement, "Все арены"));
        arenaFilterElement.click();

        String selectedArena = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(1).getText();
        driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(1).click();
        assertEquals(selectedArena, arenaFilterElement.getText());

        WebElement sportstypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));
        assertTrue(sportstypeFilter.isDisplayed());
        sportstypeFilter.click();
        String selectedSportsType = driver.findElements(By.className(SPORTS_TYPE_FILTER_OPTIONS_CLASS)).get(1).getText();
        driver.findElements(By.className(SPORTS_TYPE_FILTER_OPTIONS_CLASS)).get(1).click();
        assertEquals(selectedSportsType, sportstypeFilter.getText());

        WebElement alleventsFilter = driver.findElement(By.xpath(ALLEVENTS_FILTER_XPATH));
        assertTrue(alleventsFilter.isDisplayed());
        alleventsFilter.click();
        String selectedEventFilter = driver.findElements(By.className(EVENTS_TYPE_FILTER_OPTIONS_CLASS)).get(1).getText();
        driver.findElements(By.className(EVENTS_TYPE_FILTER_OPTIONS_CLASS)).get(1).click();
        assertEquals(selectedEventFilter, alleventsFilter.getText());

        WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));
        assertTrue(playerlevelFilter.isDisplayed());
        playerlevelFilter.click();
        String selectedplayerlevelFilter = driver.findElements(By.className(PLAYER_LEVEL_FILTER_OPTIONS_CLASS)).get(1).getText();
        driver.findElements(By.className(PLAYER_LEVEL_FILTER_OPTIONS_CLASS)).get(1).click();
        assertEquals(selectedplayerlevelFilter, playerlevelFilter.getText());

        assertFalse(driver.findElements(By.className(CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS)).isEmpty());
        driver.findElements(By.className(CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS)).get(0).click();

        WebElement gametimeFilter = driver.findElement(By.xpath(TIME_FILTER_SELECTED_ARENA_OWN_GAME_XPATH));
        assertTrue(gametimeFilter.isDisplayed());
        gametimeFilter.click();
        String selectedgametimeFilter = driver.findElements(By.className(TIME_FILTER_SELECTED_ARENA_OPTIONS_CLASS)).get(2).getText();
        driver.findElements(By.className(TIME_FILTER_SELECTED_ARENA_OPTIONS_CLASS)).get(2).click();
        assertEquals(selectedgametimeFilter, gametimeFilter.getText());

        driver.findElement(By.xpath(CREATE_GAME_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(COMMON_PAGE_TITLE_GAME_CREATION_CLASS)));

        WebElement comboBoxHelpFindPlayers = driver.findElement(By.xpath(CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_XPATH));
        comboBoxHelpFindPlayers.click();
        WebElement toClickTesting = driver.findElements(By.className(CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_TYPE_OF_GAME_OPTION_CLASS)).get(1);
        toClickTesting.click();

        WebElement comboBoxTypeOfGameToPlay = driver.findElement(By.xpath(CHOOSE_VALUE_WHICH_TYPE_OF_GAME_TO_PLAY_XPATH));
        comboBoxTypeOfGameToPlay.click();
        toClickTesting=driver.findElements(By.className(CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_TYPE_OF_GAME_OPTION_CLASS)).get(1);
        toClickTesting.click();

        driver.findElement(By.xpath(PAY_TO_CREATE_OWN_GAME_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(PAY_FOR_GAME_FROM_PERSONAL_ACCOUNT_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(LEAVE_CLOSED_GAME_XPATH)).click();
        driver.findElement(By.xpath(DO_YOU_WANT_TO_LEAVE_GAME_MESSAGE_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(CLOSED_GAME_CANCELLED_MESSAGE_XPATH)).isDisplayed();
        driver.findElement(By.xpath(BACK_PERSONAL_ACCOUNT_BUTTON_XPATH)).click();

        logout();
        }


    @Test
    //Test #4 Проверка,что на странице Опроса город СПб содержит в себе отображение всех ожидаемых районов

    public void districtsdatamassivecheckSanktPetersburgSurvey () throws InterruptedException  {

        String[] arrayStPDistrictsExpected = {"Адмиралтейский", "Василеостровский", "Выборгский", "Калининский", "Кировский", "Колпинский", "Красногвардейский", "Красносельский", "Кронштадтский", "Курортный","Московский", "Невский", "Область", "Петроградский", "Петродворцовый", "Приморский","Пушкинский", "Фрунзенский", "Центральный"};
        //arrayStPDistrictsExpected.length
        String cityToCheck = "Санкт-Петербург";

        login("testmyme4@gmail.com","Ilovetesting123!!");
        driver.findElement(By.className(AVATAR_NAME_CLASS)).click();
        sleep(1000);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className(AVATAR_NAME_CLASS))) в данном случае не срабатывает, поэтому sleep

        String chosenSurveyOption= driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();

        assertEquals(cityToCheck, driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();

        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).click();

        assertEquals(chosenCitySurvey,driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());

        //List<String> listOfAreas = new List
        // конвертировать массив в List методом Arrays.asList
        List<String> listDistrictsExpected = Arrays.asList(arrayStPDistrictsExpected);

        // находим все элементы по классу похожие на нужные названия районов (их будет больше, потому что они заданы в системе классом, который более общий чем надо)
        List<WebElement> foundWebElementsOnPage =driver.findElements(By.className(DISTRICTS_NAMES_StP_CLASS));

        int i=0;
        System.out.println("Count of expected areas is " + listDistrictsExpected.size());
        System.out.println("Count of found elements is " + foundWebElementsOnPage.size());

        for (WebElement tempElement: foundWebElementsOnPage)
        {
            // проверяю, есть ли текст найденного элемента (tempElement) в списке ожидаемых районов (listDistrictsExpected)
            if (listDistrictsExpected.contains(tempElement.getText()))
            {
                // Если найден - то повысить счетчик и печатать сообщение
                i++;
                System.out.println(tempElement.getText() + " ["+ i +"]: is a correct district name");
            }
            else
            {
                //Если нет - напечатать, какой там текст и не повышать счетчик
                System.out.println(tempElement.getText() + " ["+ i +"]: is not a district of " + chosenCitySurvey);
            }
        }

        // Сравниваем число найденных районов (счетчик i) и число предзаданных районов в listDistrictsExpected
        assertEquals(i,listDistrictsExpected.size());
        logout();
    }

    @Test
    //Test #5 Проверка, что на странице Опроса для города СПб все checkboxes доступны и clickable (loop)

    public void clickThroughTimesAndCheckBoxesAvailableOnPageSurvey () throws InterruptedException  { //Test #4

        String cityToCheck = "Санкт-Петербург";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(SIGNIN_BUTTON_CLASS)));
        login("testmyme4@gmail.com","Ilovetesting123!!");
        driver.findElement(By.className(AVATAR_NAME_CLASS)).click();
        sleep(1000);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className(AVATAR_NAME_CLASS)))

        driver.findElement(By.className(PROFILE_HEADER_CLASS)).click();

        String chosenSurveyOption= driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CITY_CURRENT_SURVEY_XPATH)));

        assertEquals(cityToCheck, driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).click();

        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CITY_CURRENT_SURVEY_XPATH)));

        assertEquals(chosenCitySurvey,driver.findElement(By.xpath(CITY_CURRENT_SURVEY_XPATH)).getText());

        // найти все элементы по классу похожие на временные слоты
        List<WebElement> foundWebElements_checkBoxes =driver.findElements(By.className(TIMES_SLOTS_AND_CHECK_BOXES_AVAILABLE));

        int i=0;

        System.out.println("Count of check boxes found: " + foundWebElements_checkBoxes.size());

        // Пройти по всем найденным элементам и попробовать их кликать - если успешно - увеличивать счетчик

        for (WebElement tempElement: foundWebElements_checkBoxes)
        {
            tempElement.click();
            i++;
            System.out.println("The checkbox with label " + tempElement.getText() + " ["+ i +"]: is clicked");
        }

        // Сравнить число найденных чекбоксов и число нажатых (счетчик i)
        assertEquals(i,foundWebElements_checkBoxes.size());
        logout();
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

    //Locators
    //public static final String ARENA_FILER_OPTIONS_CLASS = "DATE_PICKER_CLASS";
    public static final String DROP_DOWN_OPTIONS_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";
    public static final String ARENA_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div/div";
    public static final String ALLSPORTSTYPE_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div[1]";
    public static final String ALLEVENTS_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div/div";
    public static final String PLAYERLEVEL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[1]/div";
    public static final String PROFILE_HEADER_CLASS = "Tabs_tabList__1pP5W";
    public static final String SURVEY_OPTION_HEADER_CLASS = "Tabs_tab__3e8GV";
    public static final String CITY_CURRENT_SURVEY_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div";
    public static final String CITY_OPTIONS_SURVEY_CLASS  = "Select_dropdownItem__2T2FU";
    public static final String PROFILE_CARD_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]";
    public static final String SIGNOUT_ACCOUNT_BUTTON_CLASS ="LKLayout_exit__1QjSv";
    public static final String SIGNIN_BUTTON_CLASS = "header__signIn";
    public static final String INPUT_FORM_EMAIL_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/div[1]/input";
    public static final String INPUT_FORM_PASSWORD_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/div[2]/input";
    public static final String SUBMIT_LOGIN_BUTTON_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/button";
    public static final String AVATAR_HEADER_CLASS = "UserBlock_avatarWrapper__1pZ8H";
    public static final String AVATAR_NAME_CLASS = "profileText__name";
    public static final String DATE_PICKER_CLASS = "NavLink_navLink__text__zfi3X";

    //Locators specific for Test1
    public static final String ARCHIVE_EVENTS_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div/div[5]";
    public static final String PASTGAMECARDS_ARCHIVE_EVENTS_CLASS ="EventCard_bottomRow__1CodI";

    //Locators specific for Test2
    public static final String NOTIFICATIONS_BELL_WIDGET_ICON_XPATH ="//*[@id=\"root\"]/header/div/div/div[2]/div[1]/img";
    public static final String NOTIFICATIONS_BELL_WIDGET_LABEL_CLASS ="Widget_label__3nbvW";
    public static final String NOTIFICATIONS_UNREAD_MESSAGES_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div[2]/div[1]";
    public static final String NOTIFICATIONS_UNREAD_COUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div[2]/div[2]";
    public static final String NOTIFICATIONS_UNREAD_HIGHLIGHTALLBOX_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[1]/div/label/span[2]";
    public static final String NOTIFICATIONS_HEADER_SELECTEDVALUE_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[1]/div[1]/div/span";

    //Locators specific for Test3
    public static final String MY_GAMES_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div/div[2]";
    public static final String CREATE_OWN_GAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div[2]";
    public static final String DATE_PICKER_OWN_GAME_CLASS ="NavLink_navLink__3OIyY";
    public static final String TABSPANEL_ARENAS_OWN_GAME_PERSONAL_ACCOUNT_CLASS="Tabs_tabPanel__N-v3z";
    public static final String SPORTS_TYPE_FILTER_OPTIONS_CLASS="GrayRoundedSelect_dropdownItem__18xe6";
    public static final String EVENTS_TYPE_FILTER_OPTIONS_CLASS= "GrayRoundedSelect_dropdownItem__18xe6";
    public static final String PLAYER_LEVEL_FILTER_OPTIONS_CLASS= "GrayRoundedSelect_dropdownItem__18xe6";
    public static final String CREATE_EVENT_TIME_OPTIONS_FILTER_CLASS="create-event__time";
    public static final String LEAVE_CLOSED_GAME_XPATH="//*[@id=\"root\"]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div/div/div[2]/span";
    public static final String DO_YOU_WANT_TO_LEAVE_GAME_MESSAGE_BUTTON_XPATH="//html/body/div[3]/div/div/div/div/button[1]";
    public static final String CLOSED_GAME_CANCELLED_MESSAGE_XPATH="//*[@id=\"root\"]/div[2]/div[1]";
    public static final String TIME_FILTER_SELECTED_ARENA_OWN_GAME_XPATH="//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div/div[3]/div[2]";
    public static final String TIME_FILTER_SELECTED_ARENA_OPTIONS_CLASS="Select_dropdownItem__2T2FU";
    public static final String CREATE_GAME_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div/div[3]/div[4]/button";
    public static final String COMMON_PAGE_TITLE_GAME_CREATION_CLASS="Common_pageTitle__6GYuY";
    public static final String CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[1]/div/div/div[1]/div";
    public static final String CHOOSE_VALUE_FIND_PLAYERS_FOR_GAME_TYPE_OF_GAME_OPTION_CLASS="react-select__option";
    public static final String CHOOSE_VALUE_WHICH_TYPE_OF_GAME_TO_PLAY_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[2]/div/div/div[1]/div";
    public static final String PAY_TO_CREATE_OWN_GAME_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div/div/div[2]/form/div[6]/button";
    public static final String PAY_FOR_GAME_FROM_PERSONAL_ACCOUNT_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div/div[2]/div/div[4]/span[3]/form[1]/button[2]";
    public static final String BACK_PERSONAL_ACCOUNT_BUTTON_XPATH="//*[@id=\"root\"]/div[2]/div[2]/div[1]/span";

    //Locators specific for Test4, Test5
    public static final String DISTRICTS_NAMES_StP_CLASS ="Checkbox_label__WAgkq";
    public static final String TIMES_SLOTS_AND_CHECK_BOXES_AVAILABLE ="Checkbox_labelText__3VNZc";

}

