package com.wingdom.gandalfwines;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Assert;
import org.testng.annotations.*;

public class HappyPath {
	public static WebDriver driver;
	public static WebDriverWait wait;
	
	@BeforeClass
	@Parameters({"driverType"})
	public void setup(String driverType){
		driver = OrderWine.setDriver(driverType);
		wait = new WebDriverWait(driver, 60);
		driver.manage().window().maximize();
		driver.get(OrderWine.testURL);
	}
	
	/*
	 * 2 bottles of red wine
	 * 1 case of white wine
	 * billing and shipping
	 * Visa: 4111 1111 1111 1111
	 * Exp: 1/1/2020
	 * CVV: 999
	 * Cameron Yeager
	 */
	
	@Test(priority=0)
	public void buyRedWine(){
		driver.findElement(By.cssSelector("a[title*=\"Buy Wine\"]")).click();
		
		/*
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("alt[title*=\"_QA - Base3\"]")));
		//select a with title buy wine, might not be the right way to do this?
		Actions action= new Actions(driver);
		WebElement we = driver.findElement(By.cssSelector("a[title*=\"Buy Wine\"]"));
		action.moveToElement(we).perform();
		//By locator = By.cssSelector("a[title*=\"Reds\"]");
		driver.findElement(By.cssSelector("a[title*=\"Reds\"]")).click();
		*/
		
	}
	
	@Test(dependsOnMethods = {"buyRedWine"},priority=1)
	public void buyWhiteWine(){
		
	}
	
	@Test(dependsOnMethods = {"buyRedWine", "buyWhiteWine"}, priority = 2)
	public void checkout(){
		
	}
	
	@AfterClass
	public void tearDown(){
		driver.quit();
	}
}
