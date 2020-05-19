package com;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test {

	private static WebDriver driver = null;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "src\\driver\\81\\chromedriver.exe");
		driver = new ChromeDriver();
		JavascriptExecutor executor = (JavascriptExecutor) driver;

		// step 1: Go to UAT page
		driver.get("https://www.gobear.com/ph?x_session_type=UAT");
		Thread.sleep(1000);

		// Select travel section and click to show result
		FindElement(driver, By.xpath("//a[@aria-controls='Insurance']")).click();

		FindElement(driver, By.xpath("//a[@aria-controls='Travel']")).click();

		FindElement(driver, By.xpath("//button[@name='product-form-submit']")).click();

		FindElement(driver, By.xpath("//button[@data-role='cancel']")).click();

		FindElement(driver, By.xpath("//button[@data-role='end']")).click();

		// Verify number of card in result is larger than 3
		int numOfCard = FindElements(driver, By.xpath("//div[@class='col-sm-4 card-full']")).size();
		if (numOfCard < 3) {
			throw new Exception("Number of card in result is less than 3");
		}

		// Make sure the left side menu categories are functional. FYI, there are 3
		// categories: Filter, Sort and Details.
		// I am not really understand this step so I just verify it is displayed.
		try {
			FindElement(driver, By.cssSelector("#collapseFilterBtn"));
			FindElement(driver, By.cssSelector("#headingTwo"));
			FindElement(driver, By.cssSelector("#detailCollapse"));
		} catch (Exception ex) {
			throw new Exception("Missing Filter or Sort or Details field");
		}

		// Basic goal: test at least 1 option per option (Filter, Sort, Details)
		// changing at least 1 radio button, 1 range selector, 1 check box, 1 dropdown,
		// 1 calendar picker

		// drop-down
		FindElement(driver, By.xpath("//div[@data-gb-destination='Hong Kong']/div/button")).click();
		FindElement(driver, By.xpath("//span[text()='Australia']//parent::a/link")).click();

		// calendar picker
		FindElement(driver, By.xpath("//input[@name='dates-startdate']")).click();
		FindElement(driver, By.xpath("//td[text()='24']")).click();

		// checkbox
		executor.executeScript("arguments[0].click();", driver.findElement(By.cssSelector("#gb_checkbox_661")));

		// radio
		executor.executeScript("arguments[0].click();", driver.findElement(By.cssSelector("#gb_radio_4")));

		// range selector
		FindElement(driver, By.xpath("//a[@class='btn-ripple more']")).click();
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='slider-handle max-slider-handle round']")));
		WebElement elementMax = driver.findElement(By.xpath("//div[@class='slider-handle max-slider-handle round']"));
		WebElement elementMin = driver.findElement(By.xpath("//div[@class='slider-handle min-slider-handle round']"));
		int move = (elementMin.getLocation().x + elementMax.getLocation().x)/2;
		actions.moveToElement(elementMax).clickAndHold().moveByOffset(-move, 0).release().perform();

		// Verify country
		try {
			FindElement(driver, By.xpath("//h1[text()='Oops!']"));
		} catch (Exception ex) {
			throw new Exception("Actual result: The contries are showed.");
		}

		// Stretch goal*: write a test to ensure the left side menu is functional Video
		// recording to land on the page and menu to be tested can be seen here.

		driver.quit();
	}

	public static WebElement FindElement(WebDriver driver, By by) {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(by));
		actions.perform();
		return driver.findElement(by);
	}

	public static List<WebElement> FindElements(WebDriver driver, By by) {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return driver.findElements(by);
	}

}
