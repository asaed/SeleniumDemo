package com.asaed.seleniumdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FasterWhenSearchingForDrupalUsingGoogleTest {
	private static ChromeDriverService service;

	private String baseUrl;
	private WebDriver driver;

	// Cannot use Screenshot because RemoteWebDriver doesn't support it
	// private ScreenshotHelper screenshotHelper;

	@BeforeClass
	public static void setup() throws IOException {
		System.setProperty("webdriver.base.url", "http://www.google.com");

		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File("C:\\Users\\akrem\\AppData\\Local\\Google\\ChromeDriver\\chromedriver.exe"))
				.usingAnyFreePort()
				.build();
		service.start();
	}

	@AfterClass
	public static void createAndStopService() {
		service.stop();
	}

	@Before
	public void openBrowser() {

		baseUrl = System.getProperty("webdriver.base.url");

		// Option 2: a bit faster for large test suite
		driver = new RemoteWebDriver(service.getUrl(),
				DesiredCapabilities.chrome());
	}

	@After
	public void saveScreenshotAndCloseBrowser() throws IOException {
		driver.quit();
	}

	@Test
	public void pageTitleAfterSearchShouldBeginWithDrupal() throws IOException {
		driver.get(baseUrl);
		assertEquals(
				"The page title should equal Google at the start of the test.",
				"Google", driver.getTitle());
		WebElement searchField = driver.findElement(By.name("q"));
		searchField.sendKeys("Drupal!");
		searchField.submit();
		assertTrue(
				"The page title should start with the search string after the search.",
				(new WebDriverWait(driver, 10))
						.until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								return d.getTitle().toLowerCase()
										.startsWith("drupal!");
							}
						}));
	}

}