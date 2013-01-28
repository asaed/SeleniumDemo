package com.asaed.seleniumdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WhenSearchingForDrupalUsingGoogleTest {

	private String baseUrl;
	private WebDriver driver;
	private ScreenshotHelper screenshotHelper;

	@BeforeClass
	public static void setup() throws IOException {
		System.setProperty("webdriver.base.url", "http://www.google.com");
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\akrem\\AppData\\Local\\Google\\ChromeDriver\\chromedriver.exe");

	}


	@Before
	public void openBrowser() {

		baseUrl = System.getProperty("webdriver.base.url");

		// Option 1: but could be slow
		driver = new ChromeDriver();

		screenshotHelper = new ScreenshotHelper();
	}

	@After
	public void saveScreenshotAndCloseBrowser() throws IOException {
		screenshotHelper.saveScreenshot("screenshot.png");
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

	private class ScreenshotHelper {

		public void saveScreenshot(String screenshotFileName)
				throws IOException {
			File screenshot = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(screenshotFileName));
		}
	}
}