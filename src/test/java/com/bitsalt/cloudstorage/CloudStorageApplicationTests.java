package com.bitsalt.cloudstorage;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.nio.charset.Charset;
import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void getLoginPageOnInitialVisit() {
		driver.get("http://localhost:" + this.port + "/");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void testUnauthorizedLogin() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/");
		driver.findElement(By.id("inputUsername")).sendKeys("badUserName");
		driver.findElement(By.id("inputPassword")).sendKeys("badPassword");
		driver.findElement(By.id("submit-button")).click();
		Thread.sleep(2000);
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(3)
	public void testSignup() throws InterruptedException {
		// Signup
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputFirstName")).sendKeys(this.getRandomStringForSignup(8));
		driver.findElement(By.id("inputLastName")).sendKeys(this.getRandomStringForSignup(10));
		driver.findElement(By.id("inputUsername")).sendKeys(this.getRandomStringForSignup(8));
		driver.findElement(By.id("inputPassword")).sendKeys(this.getRandomStringForSignup(12));
		Thread.sleep(2000);
		driver.findElement(By.id("submit-button")).click();

		// Redirect to login page should happen on reload
		Thread.sleep(5000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(4)
	public void testNoDuplicateUsers() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputFirstName")).sendKeys("testFirstName");
		driver.findElement(By.id("inputLastName")).sendKeys("testLastName");
		driver.findElement(By.id("inputUsername")).sendKeys("testUserName");
		driver.findElement(By.id("inputPassword")).sendKeys("testPassword");
		Thread.sleep(2000);
		driver.findElement(By.id("submit-button")).click();

		// Redirect to login page should happen on reload
		Thread.sleep(2000);
		Assertions.assertEquals("Sign Up", driver.getTitle());
		WebElement resultTag = driver.findElement(By.id("error-msg"));
		Assertions.assertEquals("The username already exists.", resultTag.getText());
	}

	@Test
	@Order(5)
	public void testValidLoginAndLogout() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/");
		driver.findElement(By.id("inputUsername")).sendKeys("testUserName");
		driver.findElement(By.id("inputPassword")).sendKeys("testPassword");
		driver.findElement(By.id("submit-button")).click();
		Thread.sleep(2000);
		Assertions.assertEquals("Home", driver.getTitle());

		driver.findElement(By.id("logoutButton")).click();
		Assertions.assertEquals("Login", driver.getTitle());
//	}
//
//	@Test
//	@Order(6)
//	public void testAddNote() throws InterruptedException {
//		driver.get("http://localhost:" + this.port + "/");
//		driver.findElement(By.id("inputUsername")).sendKeys("testUsername");
//		driver.findElement(By.id("inputPassword")).sendKeys("testPassword");
//		Thread.sleep(2000);
//		driver.findElement(By.id("submit-button")).click();
//		Thread.sleep(4000);

		driver.findElement(By.id("#nav-notes-tab")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("#addNewNote")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("#note-title")).sendKeys("test Note Title");
		driver.findElement(By.id("#note-description")).sendKeys("test Note Description");
		driver.findElement(By.id("#note-submit")).click();
		Thread.sleep(2000);

		Assertions.assertEquals("Result", driver.getTitle());
		WebElement resultTag = driver.findElement(By.tagName("h1"));
		Assertions.assertEquals("Success", resultTag.getText());

//		driver.findElement(By.id("logoutButton")).click();
//	}
//
//	@Test
//	@Order(7)
//	public void testEditNote() throws InterruptedException {
//		this.doLogin();
//		Thread.sleep(4000);

		driver.findElement(By.id("#nav-notes-tab")).click();
		Thread.sleep(2000);

		WebElement editButton = driver.findElement(By.className("btn-success"));
		editButton.click();
		Thread.sleep(2000);
		driver.findElement(By.id("#note-title")).sendKeys("-2");
		driver.findElement(By.id("#note-description")).sendKeys("-2");
		driver.findElement(By.id("#note-submit")).click();
		Thread.sleep(2000);

		Assertions.assertEquals("Result", driver.getTitle());
		resultTag = driver.findElement(By.tagName("h1"));
		Assertions.assertEquals("Success", resultTag.getText());

//		driver.findElement(By.id("logoutButton")).click();
//	}
//
//	@Test
//	@Order(8)
//	public void testDeleteNote() throws InterruptedException {
//		this.doLogin();
//		Thread.sleep(4000);

		driver.findElement(By.id("#nav-notes-tab")).click();
		Thread.sleep(2000);

		WebElement deleteButton = driver.findElement(By.className("btn-danger"));
		deleteButton.click();

		Assertions.assertEquals("Result", driver.getTitle());
		resultTag = driver.findElement(By.tagName("h1"));
		Assertions.assertEquals("Success", resultTag.getText());

		driver.findElement(By.id("logoutButton")).click();
	}


	private void doLogin() {
		driver.get("http://localhost:" + this.port + "/");
		driver.findElement(By.id("inputUsername")).sendKeys("testUsername");
		driver.findElement(By.id("inputPassword")).sendKeys("testPassword");
		driver.findElement(By.id("submit-button")).click();
	}

	private String getRandomStringForSignup(int length) {
		byte[] array = new byte[length];
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));
	}
}
