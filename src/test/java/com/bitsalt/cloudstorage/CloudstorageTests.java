package com.bitsalt.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.nio.charset.Charset;
import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudstorageTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private int nameSuffix = 1; // names don't have to be unique, but this will ensure it.

    // user data to test prevention of duplicate users
    private String duplicateUsername = "testUserName";
    private String duplicatePassword = "testPassword";

    // user data for all other tests
    private String existingUserUsername = "testUserName";
    private String existingUserPassword = "testPassword";


    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.chrome.whitelistedIps", "127.0.0.1");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        driver.get("http://localhost:" + this.port + "/");
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void getLoginPageOnInitialPageload() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(2)
    public void noPageAccessWithoutLogin() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/file/view/1");
        Thread.sleep(2000);
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    @Order(3)
    public void signupTest() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/signup");
        Thread.sleep(2000);

        // signup
        this.signupUser(this.existingUserUsername, this.existingUserPassword);
        Assertions.assertEquals("Login", driver.getTitle());
        WebElement resultTag = driver.findElement(By.id("success-msg"));
        Assertions.assertEquals("You successfully signed up! Please login.", resultTag.getText());

        // login
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        // logout
        driver.findElement(By.id("logoutButton")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("Login", driver.getTitle());

        // home not accessible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    @Order(4)
    public void testNoDuplicateUsers() throws InterruptedException {
        WebElement resultTag;

        driver.get("http://localhost:" + this.port + "/signup");
        Thread.sleep(2000);

        // first signup
        this.signupUser("duplicateUsername", "duplicatePassword");
        Thread.sleep(2000);
        Assertions.assertEquals("Login", driver.getTitle());
        resultTag = driver.findElement(By.id("success-msg"));
        Assertions.assertEquals("You successfully signed up! Please login.", resultTag.getText());

        // signup again...
        driver.get("http://localhost:" + this.port + "/signup");
        Thread.sleep(2000);
        this.signupUser("duplicateUsername", "duplicatePassword");
        Thread.sleep(2000);
        Assertions.assertEquals("Sign Up", driver.getTitle());
        resultTag = driver.findElement(By.id("error-msg"));
        Assertions.assertEquals("The username already exists.", resultTag.getText());
    }

    @Test
    @Order(5)
    public void testUnauthorizedLogin() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        doLogin("badUsername", "badPassword");
        Thread.sleep(2000);
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    @Order(6)
    public void testValidLoginAndLogout() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        // check login with user set up in signupTest(). If this passes, tests for
        // notes/files/creds should be fine with this user
        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        driver.findElement(By.id("logoutButton")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(7)
    public void testAddNote() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        // add
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("addNewNote")).click();
        Thread.sleep(2000);

        try {
            driver.findElement(By.id("note-title")).sendKeys("test note title");
            driver.findElement(By.id("note-description")).sendKeys("test note description");
            driver.findElement(By.className("btn-primary")).click();
        } catch(Exception e) {
            System.out.println(e);
        }

        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // note is visible
        driver.get("http://localhost:" + this.port + "/");
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        Assertions.assertTrue(driver.findElement(By.className("noteTitle")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.className("noteDescription")).isDisplayed());
    }

    @Test
    @Order(8)
    public void testEditNote() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        // edit note
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("button[class='btn btn-success']")).click();
        Thread.sleep(2000);

        try {
            driver.findElement(By.id("note-title")).clear();
            driver.findElement(By.id("note-title")).sendKeys("changed note title");
            driver.findElement(By.id("note-description")).clear();
            driver.findElement(By.id("note-description")).sendKeys("changed note description");
            driver.findElement(By.className("btn-primary")).click();
        } catch(Exception e) {
            System.out.println(e);
        }

        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // note is visible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("changed note title", driver.findElement(By.className("noteTitle")).getText());
        Assertions.assertEquals("changed note description", driver.findElement(By.className("noteDescription")).getText());
    }

    @Test
    @Order(9)
    public void testDeleteNote() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("a[class='btn btn-danger']")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // note is not visible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        Assertions.assertFalse(this.isElementPresent(By.className("noteTitle")));
        Assertions.assertFalse(this.isElementPresent(By.className("noteDescription")));
    }

    @Test
    @Order(10)
    public void testAddCredential() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());
        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("newCredButton")).click();
        Thread.sleep(2000);


        try {
            driver.findElement(By.id("credential-url")).sendKeys("testurl.com");
            driver.findElement(By.id("credential-username")).sendKeys("testUsername");
            driver.findElement(By.id("credential-password")).sendKeys("testPassword");
            driver.findElement(By.id("credentialSubmitButton")).click();
        } catch(Exception e) {
            System.out.println(e);
        }

        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // credential is visible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("testurl.com", driver.findElement(By.className("credentialUrl")).getText());
        Assertions.assertEquals("testUsername", driver.findElement(By.className("credentialUsername")).getText());
        Assertions.assertEquals("** hidden **", driver.findElement(By.className("credentialPassword")).getText());
    }

    @Test
    @Order(11)
    public void testEditCredential() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("button[class='btn btn-success']")).click();
        Thread.sleep(2000);

        try {
            driver.findElement(By.id("credential-url")).clear();
            driver.findElement(By.id("credential-url")).sendKeys("differenturl.com");
            driver.findElement(By.id("credential-username")).clear();
            driver.findElement(By.id("credential-username")).sendKeys("anotherUsername");
            driver.findElement(By.id("credential-password")).clear();
            driver.findElement(By.id("credential-password")).sendKeys("anotherPassword");
            driver.findElement(By.id("credentialSubmitButton")).click();
        } catch (Exception e) {
            System.out.println(e);
        }

        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // contains edited content
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("differenturl.com", driver.findElement(By.className("credentialUrl")).getText());
        Assertions.assertEquals("anotherUsername", driver.findElement(By.className("credentialUsername")).getText());
        Assertions.assertEquals("** hidden **", driver.findElement(By.className("credentialPassword")).getText());
    }

    @Test
    @Order(12)
    public void canDeleteCredential() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("a[class='btn btn-danger']")).click();

        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(1000);
        Assertions.assertFalse(this.isElementPresent(By.className("credentialUrl")));
        Assertions.assertFalse(this.isElementPresent(By.className("credentialUsername")));
    }

    @Test
    @Order(13)
    public void testAddFile() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        // add file
        try {
            driver.findElement(By.id("fileUpload")).sendKeys("/Users/jeff/Downloads/README.txt");
            driver.findElement(By.id("fileSubmit")).click();
        } catch(Exception e) {
            System.out.println(e);
        }

        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // file is visible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        Assertions.assertTrue(driver.findElement(By.className("uploadedFiles")).isDisplayed());
    }

    @Test
    @Order(14)
    public void testDeleteFile() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);

        this.doLogin(this.existingUserUsername, this.existingUserPassword);
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        driver.findElement(By.cssSelector("a[class='btn btn-danger']")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("Result", driver.getTitle());
        WebElement resultTag = driver.findElement(By.tagName("h1"));
        Assertions.assertEquals("Success", resultTag.getText());

        // note is not visible
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        Assertions.assertFalse(this.isElementPresent(By.className("uploadedFiles")));
    }


    private void signupUser(String username, String password) {
        // nameSuffix makes first/last names unique, although not a requirement
        driver.findElement(By.id("inputFirstName")).sendKeys("Joe_" + this.nameSuffix);
        driver.findElement(By.id("inputLastName")).sendKeys("User_" + this.nameSuffix);
        driver.findElement(By.id("inputUsername")).sendKeys(username);
        driver.findElement(By.id("inputPassword")).sendKeys(password);
        driver.findElement(By.id("submit-button")).click();
        this.nameSuffix++;
    }


    private void doLogin(String username, String password) {
        driver.findElement(By.id("inputUsername")).sendKeys(username);
        driver.findElement(By.id("inputPassword")).sendKeys(password);
        driver.findElement(By.id("submit-button")).click();
    }

    private void doLogin() {
        driver.findElement(By.id("inputUsername")).sendKeys(this.existingUserUsername);
        driver.findElement(By.id("inputPassword")).sendKeys(this.existingUserPassword);
        driver.findElement(By.id("submit-button")).click();
    }

    private String getRandomStringForSignup(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    private boolean isElementPresent(By locatorKey) {
        try {
            this.driver.findElement(locatorKey);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private boolean isElementVisible(String cssLocator){
        return this.driver.findElement(By.cssSelector(cssLocator)).isDisplayed();
    }
}
