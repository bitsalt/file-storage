package com.bitsalt.cloudstorage;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

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
    public void testAddFile() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        this.doLogin();
        Thread.sleep(2000);
        Assertions.assertEquals("Home", driver.getTitle());

        // add
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
    @Order(2)
    public void testDeleteFile() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/");
        Thread.sleep(2000);
        this.doLogin();
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
        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        Assertions.assertFalse(this.isElementPresent(By.className("uploadedFiles")));
    }


    private void doLogin() {
        driver.findElement(By.id("inputUsername")).sendKeys(this.existingUserUsername);
        driver.findElement(By.id("inputPassword")).sendKeys(this.existingUserPassword);
        driver.findElement(By.id("submit-button")).click();
    }


    private boolean isElementPresent(By locatorKey) {
        try {
            this.driver.findElement(locatorKey);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
}
