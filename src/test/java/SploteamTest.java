package com.juneit;

//import org.jspecify.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SploteamTest {
    private final PropertiesLoader properties = new PropertiesLoader();
    private final WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    @Before
    public void setup() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        ((HasAuthentication) driver).register(UsernameAndPassword.of(properties.username,
                properties.password));
        driver.get(properties.baseUrl);
    }


    @After
    public void close() {
        driver.close();
    }


    @Test
    public void clickOnLoginButton() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
    }

    public static final String SIGNIN_BUTTON_CLASS = "header__signIn";

    @Test
    public void assertMainPageIsLoaded() {
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(LOGO_HEADER_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(MAIN_PAGE_GAMES_TITLE_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(CAMPS_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(CREATE_GAMES_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(QA_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(RENT_VENUE_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(CITY_LOCATION_BUTTON_CLASS)).isDisplayed());
        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.className(CITY_LOCATION_BUTTON_CLASS)).getText());
        Assert.assertTrue(driver.findElement(By.className(TYPEOFGAME_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(FOOTBALL_TYPEOFGAME_BUTTON_XPATH)).isDisplayed());
        Assert.assertEquals("Футбол", driver.findElement(By.xpath(FOOTBALL_TYPEOFGAME_BUTTON_XPATH)).getText());
        Assert.assertEquals("Приглашаем к сотрудничеству\nвладельцев площадок и тренеров", driver.findElement(By.className(COOPERATIONTITLE_TEXT_CLASS)).getText());

    }


    @Test
    public void assertCampSectionIsDisplayed() {
        Assert.assertTrue(driver.findElement(By.className(CAMPS_BUTTON_CLASS)).isDisplayed());
        Assert.assertEquals("Новинка!", driver.findElement(By.xpath(CAMP_TEXT1_XPATH)).getText());
        Assert.assertEquals("Спортивные кемпы", driver.findElement(By.className(CAMP_TEXT2_CLASS)).getText());
        Assert.assertTrue("Image logo is not displayed", driver.findElement(By.className(CAMP_LOGOImage_BUTTON_CLASS)).isDisplayed());
    }


    @Test
    public void assertCooperationRequestSendPositiv() {
        Assert.assertTrue(driver.findElement(By.name(FORM_INPUT_NAME)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.name(EMAIL_OR_PHONE_NAME)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(COOPERATION_SECTION_YOUR_NAME_XPATH)).isDisplayed());
        Assert.assertEquals("Ваше имя", driver.findElement(By.xpath(COOPERATION_SECTION_YOUR_NAME_XPATH)).getText());
        Assert.assertTrue(driver.findElement(By.xpath(COOPERATION_SECTION_EMAIL_PHONE_XPATH)).isDisplayed());
        Assert.assertEquals("E-mail или телефон", driver.findElement(By.xpath(COOPERATION_SECTION_EMAIL_PHONE_XPATH)).getText());
        Assert.assertTrue(driver.findElement(By.className(COOPERATION_SECTION_SEND_REQUEST_CLASS_BUTTON)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(COOPERATION_SECTION_TEXT_CLASS)).isDisplayed());
        Assert.assertEquals("Присоединяйтесь к самой крупной сети игроков, готовых придти к вам уже сегодня!", driver.findElement(By.className(COOPERATION_SECTION_TEXT_CLASS)).getText());
        driver.findElement(By.name(FORM_INPUT_NAME)).sendKeys("test");
        driver.findElement(By.name(EMAIL_OR_PHONE_NAME)).sendKeys("test2");
    }


    @Test
    public void CountLinksByTagNameMainPage() {

        List<WebElement> linkElements = driver.findElements(By.tagName("a"));
        System.out.println("Total number of links " + linkElements.size());

    }

    @Test
    public void CountExternalLinksMainPage() {
        List<WebElement> linkElements = driver.findElements(By.xpath("/html/body//a[contains(@href,\"https\")]"));
    }

    @Test
    public void assertLoginPositive() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        WebElement nameHeaderElement = driver.findElement(By.className(NAME_HEADER_CLASS));
        Assert.assertTrue(nameHeaderElement.isDisplayed());
        Assert.assertEquals("BVA_", nameHeaderElement.getText());
        WebElement logoAvatarLoginElement = driver.findElement(By.className(LOGIN_AVATAR_LOGO_TEXT_CLASS));
        Assert.assertTrue(logoAvatarLoginElement.isDisplayed());
        Assert.assertEquals("B", logoAvatarLoginElement.getText());
        nameHeaderElement.click();
        driver.findElement(By.className(LOGOUT_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());

    }

    @Test
    public void assertLoginNegativeWrongEmail() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys("stn@gmail.com");
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertEquals("Такой логин или пароль не найдены", driver.findElement(By.className(WRONG_EMAIL_ERROR_MESSAGE_CLASS)).getText());
        driver.findElement(By.className(CLOSE_SIGNIN_WINDOW_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }

    @Test
    public void assertLoginNegativeWrongPassword() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys("Iiiiii123!!");
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertEquals("Неверный пароль", driver.findElement(By.className(WRONG_EMAIL_ERROR_MESSAGE_CLASS)).getText());
        driver.findElement(By.className(CLOSE_SIGNIN_WINDOW_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }


    @Test
    public void loginWithEmptyEmailPassword() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys("");
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys("");
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).isDisplayed());
        Assert.assertEquals("Войти", driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).getText());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }


    @Test
    public void loginWithLessThanSixSymbolsPassword() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys("123vv");
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        WebElement wrongPasswordError = driver.findElement(By.xpath(WRONG_PASSWORD_ERROR_MESSAGE_XPATH));
        Assert.assertTrue(wrongPasswordError.isDisplayed());
        String expectedWrongPasswordErrorMessage = "Неверное значение";
        String actualWrongPasswordErrorMessage = wrongPasswordError.getText();
        Assert.assertEquals(expectedWrongPasswordErrorMessage, actualWrongPasswordErrorMessage);
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).getText().contains("Войти"));
    }


    @Test
    public void loginPersonalAccountLoadPositive() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(NAME_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_", driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        driver.findElement(By.className(NAME_HEADER_CLASS)).click();
        Assert.assertEquals("BVA_", driver.findElement(By.xpath(PROFILECARD_NAME_XPATH)).getText());
        Assert.assertEquals(properties.gmailAccount, driver.findElement(By.xpath(PROFILECARD_CONTACTS_EMAIL_XPATH)).getText());
        Assert.assertEquals("Пополнить счет", driver.findElement(By.xpath(TOPUP_ACCOUNT_BUTTON_XPATH)).getText());
        Assert.assertTrue(driver.findElement(By.xpath(PERSONAL_ACCOUNT_XPATH)).getText().contains("Личный счёт"));
        Assert.assertEquals("Редактировать", driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).getText());
        driver.findElement(By.className(LOGOUT_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());

    }

    @Test
    public void EditNamePersonalAccountCheck() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_", driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        driver.findElement(By.className(NAME_HEADER_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className(CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA!!!");
        driver.findElement(By.xpath(SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Ваш профиль успешно обновлён", driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_WINDOW_BUTTON_XPATH)).click();
        driver.navigate().refresh();
        Assert.assertEquals("BVA!!!", driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        driver.findElement(By.className(AVATAR_HEADER_CLASS)).click();
        Assert.assertEquals("BVA!!!", driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className(CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("BVA_");
        driver.findElement(By.xpath(SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Ваш профиль успешно обновлён", driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_WINDOW_BUTTON_XPATH)).click();
        driver.navigate().refresh();
        Assert.assertEquals("BVA_", driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        driver.findElement(By.className(AVATAR_HEADER_CLASS)).click();
        Assert.assertEquals("BVA_", driver.findElement(By.className(PROFILE_CHANGED_NAME_CLASS)).getText());
        driver.findElement(By.className(LOGOUT_BUTTON_CLASS)).click();
    }


    @Test
    public void EditEmptyNamePersonalAccountNegative() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_", driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        driver.findElement(By.className(NAME_HEADER_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.className(CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_XPATH)).sendKeys("");
        driver.findElement(By.xpath(SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Выберите значение", driver.findElement(By.xpath(ERROR_MESSAGE_NAME_PERSONAL_ACCOUNT_XPATH)).getText());
        driver.navigate().refresh();
        driver.findElement(By.className(AVATAR_HEADER_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(NAME_HEADER_CLASS)).getText().contains("BVA_"));
        driver.findElement(By.className(LOGOUT_BUTTON_CLASS)).click();

    }


    @Test //*not ready yet
    public void TopUpPersonalAccount() {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        Assert.assertEquals("BVA_", driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        driver.findElement(By.className(NAME_HEADER_CLASS)).click();
        Assert.assertEquals("Пополнить счет", driver.findElement(By.xpath(TOPUP_ACCOUNT_BUTTON_XPATH)).getText());
        driver.findElement(By.xpath(TOPUP_ACCOUNT_BUTTON_XPATH)).click();
        driver.findElement(By.className(SUM_TOPUP_FIELD_TEXT_PERSONAL_ACCOUNT_CLASS)).isDisplayed();
        driver.findElement(By.className(SUM_TOPUP_FIELD_PERSONAL_ACCOUNT_CLASS)).click();
        driver.findElement(By.className(SUM_TOPUP_FIELD_PERSONAL_ACCOUNT_CLASS)).sendKeys("1000");
        driver.findElement(By.xpath(CONFIRM_TOPUP_ACCOUNT_BUTTON_XPATH)).click();

        //Thread.sleep(2000);
        //sleep(3000);
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String strUrl = driver.getCurrentUrl();
        System.out.println("Current Url is:" + strUrl);
        //driver.navigate().refresh();
        //Assert.assertEquals("BVA_",driver.findElement(By.className(NAME_HEADER_CLASS)).getText());
        //driver.findElement(By.className (LOGOUT_BUTTON_CLASS)).click();
    }


    @Test
    public void assertArenaFilterForEvents() {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(BEFORE_LAST_DATE_SELECTION_CALENDAR_XPATH)).click();
        //sleep(3000);//cтатичное ожидание
        //Assert.assertEquals("Все арены", arenaFilterElement.getText());
        driver.navigate().refresh();
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 3));
        WebElement arenaFilterElement = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement, "Все арены"));
        arenaFilterElement.click();
        driver.findElement(By.xpath(ARENA_FILTER_OPTION_PESOK_XPATH)).click();
        wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement, "Песок"));
        int eventsCount = driver.findElements(By.className(ARENA_NAME_EVENT_CARD_CLASS)).size();
        System.out.println(eventsCount);

        for (int i = 0; i < eventsCount; i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("\"Песок\"", driver.findElements(By.className(ARENA_NAME_EVENT_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
        }


    }

    @Test
    public void assertArenaFilterForEventsVersion2() {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(BEFORE_LAST_DATE_SELECTION_CALENDAR_XPATH)).click();
        WebElement arenaFilterElement = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 3));
        wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement, "Все арены"));
        arenaFilterElement.click();
        WebElement chosenOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(6);
        String chosenOptionText = chosenOption.getText();
        chosenOption.click();
        wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement, chosenOptionText));
        for (int i = 0; i < driver.findElements(By.className(ARENA_NAME_EVENT_CARD_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("\"" + chosenOptionText + "\"", driver.findElements(By.className(ARENA_NAME_EVENT_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
        }
    }

    @Test
    public void assertEventTypeFilter() throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("GrayRoundedSelect_select__1yKm5"), 9));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 4));
        driver.findElement(By.xpath(BEFORE_LAST_DATE_SELECTION_CALENDAR_XPATH)).click();
        sleep(3000);//cтатичное ожидание
        //Assert.assertEquals("Все арены", arenaFilterElement.getText());
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("GrayRoundedSelect_select__1yKm5"), 9));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 4));
        driver.navigate().refresh();
        WebElement eventFilterElement = driver.findElement(By.xpath(EVENT_TYPE_FILTER_XPATH));
        wait.until(ExpectedConditions.textToBePresentInElement(eventFilterElement, "Все события"));
        eventFilterElement.click();
        driver.findElement(By.xpath(EVENT_TYPE_FILTER_OPTION_GAME_XPATH)).click();
        wait.until(ExpectedConditions.textToBePresentInElement(eventFilterElement, "Игры"));
        int eventsCount = driver.findElements(By.className(EVENT_NAME_GAME_EVENT_CARD_CLASS)).size();
        System.out.println(eventsCount);

        for (int i = 0; i < eventsCount; i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("Игра", driver.findElements(By.className(EVENT_NAME_GAME_EVENT_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
        }


    }


    @Test
    public void assertMainPageScroll() throws InterruptedException {
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.className(LOGO_HEADER_CLASS)).isDisplayed());

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)));
        sleep(3000);
        js.executeScript("window.scrollBy(0, 500);");
        sleep(3000);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        sleep(3000);
    }


    @Test
    public void assertGameSportsTypePlayerLevelFilters3() throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("GrayRoundedSelect_select__1yKm5"), 9));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 4));
        driver.findElement(By.xpath(EVENT_DATE_FILTER_OPTION_XPATH)).click();
        sleep(3000);//cтатичное ожидание

        WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));
        wait.until(ExpectedConditions.textToBePresentInElement(playerlevelFilter, "Любой уровень"));
        playerlevelFilter.click();
        WebElement chosenplayerOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(2);
        String chosenplayerOptionText = chosenplayerOption.getText();
        chosenplayerOption.click();
        Assert.assertEquals("Медиум", playerlevelFilter.getText());

        int playermediumlevelCount = driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).size();
        System.out.println(playermediumlevelCount);
        for (int i = 0; i < driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("Уровень - " +  chosenplayerOptionText, driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
        }

        WebElement sportstypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));
        Assert.assertTrue(sportstypeFilter.isDisplayed());
        Assert.assertEquals("Все виды спорта", sportstypeFilter.getText());
        sportstypeFilter.click();
        WebElement chosenSportOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(5);
        String chosenSportOptionText = chosenSportOption.getText();
        chosenSportOption.click();
        Assert.assertEquals(chosenSportOptionText, sportstypeFilter.getText());

        int sporttypebeachvolleyballCount = driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).size();
        System.out.println(sporttypebeachvolleyballCount);
        for (int i = 0; i < driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals(chosenSportOptionText , driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).get(i).getText());//какое действие мы выполняем
        }
    }

    @Test
    public void assertGameSportsTypePlayerLevelFilters2() throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("GrayRoundedSelect_select__1yKm5"), 9));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 4));
        driver.findElement(By.xpath(EVENT_DATE_FILTER_OPTION_XPATH)).click();
        sleep(3000);//cтатичное ожидание
        //Assert.assertEquals("Все арены", arenaFilterElement.getText());
        driver.navigate().refresh();
        WebElement eventFilterElement = driver.findElement(By.xpath(EVENT_TYPE_FILTER_XPATH));
        wait.until(ExpectedConditions.textToBePresentInElement(eventFilterElement, "Все события"));
        eventFilterElement.click();
        WebElement chosenOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(1);
        String chosenOptionText = chosenOption.getText();
        chosenOption.click();
        wait.until(ExpectedConditions.textToBePresentInElement(eventFilterElement, chosenOptionText));


        WebElement playerlevelFilter = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));
        wait.until(ExpectedConditions.textToBePresentInElement(playerlevelFilter, "Любой уровень"));
        playerlevelFilter.click();
        WebElement chosenplayerOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(2);
        String chosenplayerOptionText = chosenplayerOption.getText();
        chosenplayerOption.click();
        wait.until(ExpectedConditions.textToBePresentInElement(playerlevelFilter, chosenplayerOptionText));

        Assert.assertEquals("Медиум", playerlevelFilter.getText());
        int playermediumlevelCount = driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).size();
        System.out.println(playermediumlevelCount);
        for (int i = 0; i < driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("Уровень - " + "Медиум", driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
        }


        WebElement sportstypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));
        Assert.assertTrue(sportstypeFilter.isDisplayed());
        Assert.assertEquals("Все виды спорта", sportstypeFilter.getText());
        sportstypeFilter.click();
        int sportstypefiltertCount = driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).size();
        System.out.println(sportstypefiltertCount);
        for (int i = 0; i < sportstypefiltertCount; i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("Игра", driver.findElements(By.className(EVENT_NAME_GAME_EVENT_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
        }


        WebElement sporttypeFilter = driver.findElement(By.xpath(ALLSPORTSTYPE_FILTER_XPATH));
        sporttypeFilter.click();
        sleep(3000);//cтатичное ожидание
        WebElement chosensporttypeOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(5);
        String chosensporttypeOptionText = chosensporttypeOption.getText();
        chosensporttypeOption.click();
        wait.until(ExpectedConditions.textToBePresentInElement(sporttypeFilter, chosensporttypeOptionText));

        Assert.assertEquals("Пляжный волейбол", playerlevelFilter.getText());
        int sporttypebeachvolleyballCount = driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).size();
        System.out.println(sporttypebeachvolleyballCount);
        for (int i = 0; i < driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
            Assert.assertEquals("\"" + chosensporttypeOptionText + "\"", driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).get(i).getText());//какое действие мы выполняем
        }
        //driver.findElement(By.xpath(BEACHVOLLEYBALL_FILTER_XPATH)).click();
        //Assert.assertEquals("Пляжный волейбол", sportstypeFilter.getText());

        Assert.assertFalse(driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).isEmpty());
        for (int j = 0; j < driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).size(); j++) {
            System.out.println(": " + j + " - " + driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).get(j).getText());
            // Assert.assertEquals("Пляжный волейбол", driver.findElements(By.className(SPORT_TYPE_GAME_CLASS)).get(j).getText());
        }


    }

    @Test
    public void assertSurveyCityNegative() throws InterruptedException {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        //login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        Assert.assertTrue(driver.findElement(By.className(AVATAR_HEADER_CLASS)).isDisplayed());
        driver.findElement(By.className (AVATAR_HEADER_CLASS)).click();
        driver.findElement(By.className(PROFILE_HEADER_TAB_CLASS)).click();
        sleep(3000);
        String chosenSurveyOption = driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).getText();
        System.out.println(chosenSurveyOption);
        driver.findElements(By.className(SURVEY_OPTION_HEADER_CLASS)).get(3).click();
        sleep(3000);
        Assert.assertEquals("Выберите районы города", driver.findElement(By.className(CHOOSE_DISTRICTS_SURVEY_OPTION_CLASS)).getText());


        Assert.assertEquals("Санкт-Петербург", driver.findElement(By.xpath(CURRENT_CITY_SURVEY_XPATH)).getText());
        driver.findElement(By.xpath(CURRENT_CITY_SURVEY_XPATH)).click();
        String chosenCitySurvey = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(5).getText();
        System.out.println(chosenCitySurvey);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(5).click();
        sleep(3000);
        Assert.assertTrue(chosenCitySurvey.contains(driver.findElement(By.xpath(CURRENT_CITY_SURVEY_XPATH)).getText()));
        driver.findElement(By.className(SAVECHANGES_SURVEY_CLASS_BUTTON)).click();

        WebElement SurveySaveNotCompleteError = driver.findElement(By.className(ERROR_MESSAGE_SAVECHANGES_SURVEY_CLASS));

        Assert.assertTrue(SurveySaveNotCompleteError.isDisplayed());

        String expectedSurveySaveNotCompleteErrorMessage = "Ответьте, пожалуйста, на все вопросы";
        String actualSurveySaveNotCompleteErrorMessage = SurveySaveNotCompleteError.getText();

        Assert.assertEquals(expectedSurveySaveNotCompleteErrorMessage, actualSurveySaveNotCompleteErrorMessage);
        driver.findElement(By.xpath(CURRENT_CITY_SURVEY_XPATH)).click();
        String chosenCitySurveyDefault = driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).getText();
        System.out.println(chosenCitySurveyDefault);
        driver.findElements(By.className(CITY_OPTIONS_SURVEY_CLASS)).get(1).click();
        sleep(3000);
        Assert.assertTrue(chosenCitySurveyDefault.contains(driver.findElement(By.xpath(CURRENT_CITY_SURVEY_XPATH)).getText()));
        driver.findElement(By.className(SAVECHANGES_SURVEY_CLASS_BUTTON)).click();

        logout();
        //driver.findElement(By.className(LOGOUT_BUTTON_CLASS)).click();
        //Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());

    }

    @Test
    public void EditGenderPersonalAccount () throws InterruptedException {

        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        //login(properties.gmailAccount, properties.gmailPassword);
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        WebElement nameHeaderElement = driver.findElement(By.className(NAME_HEADER_CLASS));
        Assert.assertTrue(nameHeaderElement.isDisplayed());
        Assert.assertEquals("BVA_", nameHeaderElement.getText());

        driver.findElement(By.className(NAME_HEADER_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains("BVA_"));
        Assert.assertTrue(driver.findElement(By.xpath(PROFILE_CARD_XPATH)).getText().contains(properties.gmailAccount));
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();


        WebElement genderElement = driver.findElement(By.xpath(CURRENT_GENDER_PERSONAL_ACCOUNT_XPATH));
        genderElement.click();

        WebElement femaleGenderSelectOption= driver.findElement(By.xpath(FEMALE_GENDER_SELECT_PERSONAL_ACCOUNT_XPATH));
        WebElement maleGenderSelectOption= driver.findElement(By.xpath(MALE_GENDER_SELECT_PERSONAL_ACCOUNT_XPATH));

        String currentGenderValue = genderElement.getText();

        if(genderElement.getText().equals(femaleGenderSelectOption.getText()))
        {
            maleGenderSelectOption.click();
        }
        else
        {
            femaleGenderSelectOption.click();
        }
        driver.findElement(By.xpath (SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH)).click();
        Assert.assertEquals("Ваш профиль успешно обновлён",driver.findElement(By.xpath(EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH)).getText());
        driver.findElement(By.xpath (EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_WINDOW_BUTTON_XPATH)).click();
        driver.navigate().refresh();
        driver.findElement(By.className(NAME_HEADER_CLASS)).click();
        driver.findElement(By.className(EDIT_ACCOUNT_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(CURRENT_GENDER_PERSONAL_ACCOUNT_XPATH)).click();

        genderElement = driver.findElement(By.xpath(CURRENT_GENDER_PERSONAL_ACCOUNT_XPATH));

        Assert.assertNotEquals (currentGenderValue,genderElement.getText());
        System.out.println(genderElement.getText());

        logout();
    }

    @Test
    public void assertEventTypeFilterForEachDate() {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 3));
        wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(EVENT_TYPE_FILTER_XPATH)),"Все события"));
        driver.findElement(By.xpath(EVENT_TYPE_FILTER_XPATH)).click();
        driver.findElement(By.xpath(GAME_FILTER_OPTION_XPATH)).click();
        wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(EVENT_TYPE_FILTER_XPATH)),"Игры"));

        for (int i = 1; i < driver.findElements(By.className(DATE_CREATE_GAME_PAGE_CLASS)).size(); i++){
            driver.findElements(By.className(DATE_CREATE_GAME_PAGE_CLASS)).get(i).click();
            if (driver.findElements(By.className(EVENT_TYPE_EVENT_CARD_CLASS)).isEmpty()){
                Assert.assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.", driver.findElement(By.xpath(NO_GAMES_MESSAGE_XPATH)).getText());
            } else {
                for (int j = 0; j < driver.findElements(By.className(EVENT_TYPE_EVENT_CARD_CLASS)).size(); j++) {
                    Assert.assertEquals("Игра", driver.findElements(By.className(EVENT_TYPE_EVENT_CARD_CLASS)).get(j).getText());//какое действие мы выполняем
                }
            }
        }
    }


    @Test
    public void assertArenaFilterForEventsForEachArena() {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(BEFORE_LAST_DATE_SELECTION_CALENDAR_XPATH)).click();
        WebElement arenaFilterElement = driver.findElement(By.xpath(ARENA_FILTER_XPATH));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 3));
        wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement,"Все арены"));
        arenaFilterElement.click();
        int arenaOptionsCount = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).size();
        arenaFilterElement.click();

        for (int k = 1; k < arenaOptionsCount; k++){
            arenaFilterElement.click();
            WebElement chosenOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(k);
            String chosenOptionText = chosenOption.getText();
            chosenOption.click();
            wait.until(ExpectedConditions.textToBePresentInElement(arenaFilterElement, chosenOptionText));
            if (driver.findElements(By.className(EVENT_TYPE_EVENT_CARD_CLASS)).isEmpty()){
                Assert.assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.", driver.findElement(By.xpath(NO_GAMES_MESSAGE_XPATH)).getText());
            } else {
                for (int i = 0; i < driver.findElements(By.className(ARENA_NAME_EVENT_CARD_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
                    Assert.assertEquals("\"" + chosenOptionText + "\"", driver.findElements(By.className(ARENA_NAME_EVENT_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
                }
            }
        }

    }

    @Test
    public void assertPlayerLevelFilterForDateChosen() {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        driver.findElement(By.xpath(DATE_SELECTION_CALENDAR_XPATH)).click();
        WebElement playerLevelFilterElement = driver.findElement(By.xpath(PLAYERLEVEL_FILTER_XPATH));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 3));
        wait.until(ExpectedConditions.textToBePresentInElement(playerLevelFilterElement,"Любой уровень"));
        playerLevelFilterElement.click();
        int playerLevelOptionsCount = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).size();
        playerLevelFilterElement.click();

        for (int k = 1; k < playerLevelOptionsCount; k++){
            playerLevelFilterElement.click();
            WebElement chosenOption = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS)).get(k);
            String chosenOptionText = chosenOption.getText();
            chosenOption.click();
            wait.until(ExpectedConditions.textToBePresentInElement(playerLevelFilterElement, chosenOptionText));
            if (driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).isEmpty()){
                Assert.assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.", driver.findElement(By.xpath(NO_GAMES_MESSAGE_XPATH)).getText());
            } else {
                for (int i = 0; i < driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).size(); i++) {//для каких элемнтов мы выполняем действие: от 0 до eventsCount с шагом 1
                    Assert.assertEquals("Уровень - " + chosenOptionText, driver.findElements(By.className(PLAYERLEVEL_GAME_CARD_CLASS)).get(i).getText());//какое действие мы выполняем
                }
            }
        }

    }
    
    @Test
    public void assertRefundToPersonalAccountCheckbox() throws InterruptedException {
        sleep(3000);
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(properties.gmailAccount);
        driver.findElement(By.name(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(properties.gmailPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
        WebElement nameHeaderElement = driver.findElement(By.className(NAME_HEADER_CLASS));
        nameHeaderElement.click();
        wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.className(PERSON_NAME_SURNAME_CLASS)), "BVA_"));
        if (driver.findElement(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_INPUT_CLASS)).isSelected()){
            driver.findElement(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_CLASS)).click();
            wait.until(ExpectedConditions.elementSelectionStateToBe(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_INPUT_CLASS), false));
            Assert.assertFalse(driver.findElement(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_INPUT_CLASS)).isSelected());
        } else {
            driver.findElement(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_CLASS)).click();
            wait.until(ExpectedConditions.elementSelectionStateToBe(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_INPUT_CLASS), true));
            Assert.assertTrue(driver.findElement(By.className(REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_INPUT_CLASS)).isSelected());
        }



    }


    @Test
    public void numberOfCampsPerMonthDisplayed() {
        assertTrue(driver.findElement(By.className(CAMPS_BUTTON_CLASS)).isDisplayed());
        driver.findElement(By.className(CAMPS_BUTTON_CLASS)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(MONTH_PICK_CAMPS_FILTER_CLASS)));
        assertTrue(driver.findElement(By.className(TEXT_SPORTSCAMPS_CLASS)).isDisplayed());
        assertEquals("2024", driver.findElements(By.className(YEAR_PICK_CAMPS_FILTER_CLASS)).get(2).getText());
        driver.findElements(By.className(YEAR_PICK_CAMPS_FILTER_CLASS)).get(2).click();
        List<WebElement> campsMonths = driver.findElements(By.className(MONTH_PICK_CAMPS_FILTER_CLASS));
        List<WebElement> campCards;

            for (int i = 1; i < campsMonths.size(); i++) {
                campsMonths.get(i).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CAMPS_SEARCH_PAGE_RESULTS_CLASS)));
                campCards = driver.findElements(By.className(CARD_COUNT_FILTER_CLASS));
                if (campCards.isEmpty()) {
                    System.out.println("На " + campsMonths.get(i).getText() + " месяц кэмпы не доступны.");
                    assertTrue(driver.findElement(By.xpath(MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH)).isDisplayed());
                    assertEquals("Упс... не удалось найти ни одного кемпа.",  driver.findElement(By.xpath(MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH)).getText());
                } else {
                    System.out.println("На " + campsMonths.get(i).getText() + " = " + campCards.size());
                }
        }
    }


    @Test
    public void assertSportTypeFilterForEachDate() throws InterruptedException {
        driver.findElement(By.xpath(MORE_GAMES_BUTTON_XPATH)).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("GrayRoundedSelect_select__1yKm5"), 9));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("GrayRoundedSelect_select__1yKm5"), 4));

        List<WebElement> eventDates = driver.findElements(By.className(DATE_PICK_CLASS));
        List<WebElement> eventCards;
        WebElement sportsTypeFilter = driver.findElement(By.xpath(SPORTSTYPE_FILTER_OPTION_ARROW_XPATH));
        sportsTypeFilter.click();
        List<WebElement> sportTypes = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS));
        sportsTypeFilter.click(); // closing the dropbox before starting the cycle

        String sportType_temp;

        for (int j = 1; j < sportTypes.size(); j++) {

            sportsTypeFilter.click();
            sportTypes = driver.findElements(By.className(DROP_DOWN_OPTIONS_CLASS));
            sportType_temp = sportTypes.get(j).getText();
            sportTypes.get(j).click();

            for (int i = 1; i < eventDates.size(); i++) {

                eventDates.get(i).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className(EMPTY_GAMES_ERROR_MESSAGE_PANEL_CLASS)));
                eventCards = driver.findElements(By.className(SPORT_TYPE_GAME_CLASS));

                if (eventCards.isEmpty()) {

                    System.out.println("На " + eventDates.get(i).getText() + sportType_temp + " игр нет.");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(NO_GAMES_MESSAGE_XPATH)));
                    assertEquals("Упс... на этот день нет ни одной игры, но Вы можете найти подходящую игру в другой день.",driver.findElement(By.xpath(NO_GAMES_MESSAGE_XPATH)).getText());
                } else {

                    System.out.println("На " + eventDates.get(i).getText() + "  " + sportType_temp + " = " + eventCards.size());
                }
            }
        }


        }


        //Extracted methods
    private void login(String userEmail, String userPassword) {
        driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).click();
        driver.findElement(By.xpath(EMAIL_INPUT_LOGIN_XPATH)).sendKeys(userEmail);
        driver.findElement(By.xpath(PASSWORD_INPUT_LOGIN_NAME)).sendKeys(userPassword);
        driver.findElement(By.xpath(SUBMIT_LOGIN_BUTTON_XPATH)).click();
    }


    private void logout() {
        //добавить шаг зайти в личный кабинет
        driver.findElement(By.className(AVATAR_HEADER_CLASS)).click();
        driver.findElement(By.className(LOGOUT_BUTTON_CLASS)).click();
        Assert.assertTrue(driver.findElement(By.className(SIGNIN_BUTTON_CLASS)).isDisplayed());
    }


    public static final String USER_NAME = "BVA_";
        //Locators
    public static final String LOGO_HEADER_CLASS = "header__logo";
    public static final String MAIN_PAGE_GAMES_TITLE_CLASS = "all-games__title";
    public static final String CAMPS_BUTTON_CLASS = "CampsButton_button__2sQY1";
    public static final String MORE_GAMES_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[2]/div/div[3]/a";
    public static final String CREATE_GAMES_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[3]/div/div[2]/a";
    public static final String RENT_VENUE_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[4]/div/div[2]/a";
    public static final String QA_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/div/a";
    public static final String CITY_LOCATION_BUTTON_CLASS = "location__default";
    public static final String TYPEOFGAME_BUTTON_CLASS = "games-list";
    public static final String FOOTBALL_TYPEOFGAME_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/section[1]/div/div[1]/a[2]";
    public static final String COOPERATIONTITLE_TEXT_CLASS = "cooperation__title";
    public static final String CAMP_TEXT1_XPATH = "//*[@id=\"root\"]/div[2]/section[1]/div/a/div/div[2]/div[1]";
    public static final String CAMP_TEXT2_CLASS = "CampsButton_title__55qxk";
    public static final String CAMP_LOGOImage_BUTTON_CLASS = "CampsButton_img__3CoXw";
    public static final String COOPERATION_SECTION_YOUR_NAME_XPATH = "//*[@id=\"root\"]/div[2]/section[5]/div/form/div[1]/label";
    public static final String COOPERATION_SECTION_EMAIL_PHONE_XPATH = "//*[@id=\"root\"]/div[2]/section[5]/div/form/div[2]/label";
    public static final String COOPERATION_SECTION_SEND_REQUEST_CLASS_BUTTON = "cooperation-form__submit";
    public static final String COOPERATION_SECTION_TEXT_CLASS = "cooperation__text";

    public static final String EMAIL_INPUT_LOGIN_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/div[1]/input";
    public static final String PASSWORD_INPUT_LOGIN_NAME = "password";
    public static final String SUBMIT_LOGIN_BUTTON_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/button";
    public static final String NAME_HEADER_CLASS = "profileText__name";
    public static final String LOGOUT_BUTTON_CLASS = "LKLayout_exit__1QjSv";
    public static final String LOGIN_AVATAR_LOGO_TEXT_CLASS = "UserBlock_initials__9jNSe";
    public static final String WRONG_EMAIL_ERROR_MESSAGE_CLASS = "form_error__2xL0z";

    public static final String FORM_INPUT_NAME = "name";
    public static final String EMAIL_OR_PHONE_NAME = "email_or_phone";
    public static final String WRONG_PASSWORD_ERROR_MESSAGE_XPATH = "/html/body/div[3]/div/div/div/div[2]/form/p";
    public static final String CLOSE_SIGNIN_WINDOW_BUTTON_CLASS = "modal__close";
    public static final String AVATAR_HEADER_CLASS = "UserBlock_avatarWrapper__1pZ8H";
    public static final String PROFILE_CARD_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]";
    public static final String PROFILECARD_NAME_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[1]/b";
    public static final String PROFILECARD_CONTACTS_EMAIL_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[2]/div[3]/span[2]";
    public static final String TOPUP_ACCOUNT_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[3]/div[3]/button";
    public static final String PERSONAL_ACCOUNT_XPATH="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[3]";
    public static final String EDIT_ACCOUNT_BUTTON_CLASS ="OrangeLink_orangeLink__34ZRK";

    public static final String CLEAR_NAME_PERSONAL_ACCOUNT_BUTTON_CLASS ="FormInput_formInputClear__3DQxO";
    public static final String EDIT_NAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[1]/input";
    public static final String SAVECHANGES_EDIT_ACCOUNT_BUTTON_XPATH = "//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/button";
    public static final String EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_XPATH = "/html/body/div[3]/div/div/p";
    public static final String EDIT_NAME_PERSONAL_ACCOUNT_MESSAGE_CLOSE_WINDOW_BUTTON_XPATH = "/html/body/div[3]/div/div/button";
    public static final String PROFILE_CHANGED_NAME_CLASS="ProfileCard_name__2yGm-";
    public static final String ERROR_MESSAGE_NAME_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/p";
    //public static final String TOPUP_PERSONAL_ACCOUNT_BUTTON_CLASS="button header__signIn";
    public static final String SUM_TOPUP_FIELD_TEXT_PERSONAL_ACCOUNT_CLASS="FormInput_formInputLabel__2cdm1";
    public static final String SUM_TOPUP_FIELD_PERSONAL_ACCOUNT_CLASS="FormInput_formInputField__1HTcx";
    public static final String CONFIRM_TOPUP_ACCOUNT_BUTTON_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[3]/div[3]/form[1]/button";


    public static final String BEFORE_LAST_DATE_SELECTION_CALENDAR_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[6]/span[2]";
    public static final String DATE_SELECTION_CALENDAR_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[2]/span[2]";
    //*[@id="root"]/div[2]/div[1]/div/div[1]/div/div[4]/span[2]
    public static final String ARENA_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div/div";
    public static final String ARENA_FILTER_OPTION_PESOK_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[1]/div[2]/div[2]";
    public static final String ARENA_NAME_EVENT_CARD_CLASS = "EventCard_eventTypeRow__arena__3ljYS";
    public static final String EVENT_TYPE_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div[1]/div";
    public static final String EVENT_TYPE_FILTER_OPTION_GAME_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div[2]/div[2]";
    public static final String EVENT_NAME_GAME_EVENT_CARD_CLASS = "EventCard_eventTypeRow__type__3TN0s";

    public static final String EVENT_DATE_FILTER_OPTION_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[1]/div/div[4]/span[2]";

    public static final String PLAYERLEVEL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]";
    //*[@id="root"]/div[2]/div[1]/div/div[3]/div[1]/div[3]
    //*[@id="root"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div/div
    public static final String MEDIUM_PLAYERLEVEL_GAME_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[3]/div[2]/div[3]";


    public static final String PLAYERLEVEL_GAME_CARD_CLASS ="EventCard_level__LxpwV";
    public static final String ALLSPORTSTYPE_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div/div";
    public static final String BEACHVOLLEYBALL_FILTER_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div/div[3]/div/div[2]/div[1]/div[1]/div[2]";
    public static final String SPORT_TYPE_GAME_CLASS = "EventCard_sport__2ZcAA";
    public static final String DROP_DOWN_OPTIONS_CLASS = "GrayRoundedSelect_dropdownItem__18xe6";
    public static final String PERSONAL_ACCOUNT_TEXT_XPATH = "//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[3]/div[1]";

    public static final String SURVEY_OPTION_HEADER_CLASS = "Tabs_tab__3e8GV";
    public static final String PROFILE_HEADER_TAB_CLASS = "Tabs_tabList__1pP5W";
    public static final String CHOOSE_DISTRICTS_SURVEY_OPTION_CLASS ="QuizQuestion_quizQuestion__title__20Rec";
    public static final String CURRENT_CITY_SURVEY_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div[2]/div/div/div[1]/div/div";
    public static final String CITY_OPTIONS_SURVEY_CLASS  = "Select_dropdownItem__2T2FU";
    public static final String SAVECHANGES_SURVEY_CLASS_BUTTON  = "Quiz_quiz__button__1gMQ7";
    public static final String ERROR_MESSAGE_SAVECHANGES_SURVEY_CLASS  = "Quiz_quiz__errorBlock__I9tK3";
    public static final String CURRENT_GENDER_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div/div[2]";
    public static final String FEMALE_GENDER_SELECT_PERSONAL_ACCOUNT_XPATH ="//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[2]";

    public static final String MALE_GENDER_SELECT_PERSONAL_ACCOUNT_XPATH= "//*[@id=\"root\"]/div[2]/div/div[3]/div/div[2]/form/div[2]/div[2]/div[1]";

    public static final String NO_GAMES_MESSAGE_XPATH ="//*[@id=\"root\"]/div[2]/div[1]/div/div[4]/div/div";
    public static final String DATE_CREATE_GAME_PAGE_CLASS = "NavLink_navLink__3OIyY";
    public static final String EVENT_TYPE_EVENT_CARD_CLASS="EventCard_eventTypeRow__type__3TN0s";
    //public static final String GAME_FILTER_OPTION_XPATH="//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div/div";
    public static final String GAME_FILTER_OPTION_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div[2]/div[2]";

    public static final String REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_CLASS = "Checkbox_label__WAgkq";
    public static final String REFUND_MONEY_TO_PERSONAL_ACCOUNT_CHECKBOX_INPUT_CLASS = "Checkbox_input__1h1oW";
    public static final String PERSON_NAME_SURNAME_CLASS = "ProfileCard_name__2yGm-";



    public static final String MONTH_PICK_CAMPS_FILTER_CLASS = "MonthSwitcher_item__2HxRH";
    public static final String YEAR_PICK_CAMPS_FILTER_CLASS = "YearSwitcher_year__3NlNA";
    public static final String TEXT_SPORTSCAMPS_CLASS = "CampsSearchPage_pageTitle__1e8aL";
    //public static final String MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH = "/html/body/div[1]/div[2]/div[2]/div/div/div[1]/div[1]";
    public static final String MONTH_NO_CAMPS_ERROR_MESSAGE_XPATH = "// *[@id=\"root\"]/div[2]/div[2]/div/div/div[1]/div[1]";
    public static final String CARD_COUNT_FILTER_CLASS = "Card_card__2jlaB";
    public static final String CAMPS_SEARCH_PAGE_RESULTS_CLASS = "CampsSearchPage_searchResults__Yio_Q";
    public static final String DATE_PICK_CLASS = "NavLink_navLink__text__zfi3X";
    public static final String EMPTY_GAMES_ERROR_MESSAGE_PANEL_CLASS = "SearchPage_tabPanelContainer__1Lxhs";
    public static final String SPORTSTYPE_FILTER_CLASS = "GrayRoundedSelect_controlValue__2EqXC";
    public static final String SPORTSTYPE_FILTER_OPTION_ARROW_XPATH = "//*[@id=\"root\"]/div[2]/div[1]/div/div[3]/div[1]/div[2]/div[1]/i[2]";


//*[@id="root"]/div[2]/div[1]/div/div[3]/div[1]/div[4]/div/div
}
