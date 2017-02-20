package com.wingdom.gandalfwines;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.Assert;
import org.testng.annotations.*;

import com.google.common.base.Predicate;

public class HappyPath {
	public static WebDriver driver;
	public static WebDriverWait wait;
	public String basePrice = null;
	
	@BeforeClass
	@Parameters({"driverType"})
	public void setup(String driverType){
		driver = OrderWine.setDriver(driverType);
		wait = new WebDriverWait(driver, 20);
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
	public void buyRedWine() throws InterruptedException{
		
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		
		//clicking the buy wines button with any of these methods doesn't work
		//driver.findElement(By.xpath("//a[@href='/page?pageid=15BBE3AA-24CD-42DA-B9B6-46230B765CB8']")).click();
		//driver.findElement(By.cssSelector("a[title='Buy Wine']")).click();
		//driver.findElement(By.cssSelector("a[href='/page?pageid=15BBE3AA-24CD-42DA-B9B6-46230B765CB8']")).click();
		
		//using an action to hover over it and click red or white wine doesnt work
		//Its a bug in firefox and chrome, https://github.com/SeleniumHQ/selenium/issues/2552
		/*Actions action = new Actions(driver);
		WebElement buyWine = driver.findElement(By.cssSelector("a[title='Buy Wine']"));
		action.moveToElement(buyWine).build().perform();
		WebElement reds = driver.findElement(By.cssSelector("a[title='Reds']"));
		action.moveToElement(reds).click().build().perform();
		*/
		
		//Using xpath does! But then we get all wines on one page
		driver.findElement(By.xpath("/html/body/div[1]/header/div/div[5]/div/ul/li[1]/a")).click();
		
		//Reds (on the all wine page) is never 'visible' to the driver
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Reds")));
		//driver.findElement(By.linkText("Reds")).click();
		///driver.findElement(By.xpath("html/body/div[1]/div[2]/div/div[1]/ul/li[2]/a")).click();
		
		//Since we can't tell the difference between quantity fields for bottle and case, just add a single bottle twice
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button")));
		//add red wasnt always visible, so have to scroll so it
		WebElement firstRed = driver.findElement(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstRed);
		
		Thread.sleep(4000);//wait then click the button twice, yes twice, because it doesnt always work, but a 2nd click wont hurt anything
		driver.findElement(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button")).click();//add red to cart
		driver.findElement(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button")).click();
		
		//if dob check appears
		if(driver.findElements(By.id("DOBSelector")).size() > 0){
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select[name='BirthMonth']")));
			Select month = new Select(driver.findElement(By.cssSelector("select[name='BirthMonth']")));
			month.selectByValue("2");
			Select day = new Select(driver.findElement(By.cssSelector("select[name='BirthDay']")));
			day.selectByValue("14");
			Select year = new Select(driver.findElement(By.cssSelector("select[name='BirthYear']")));
			year.selectByValue("1990");
			//clicking the submit button with css doesnt work
			//driver.findElement(By.cssSelector("button[type='submit']")).click();
			//driver.findElement(By.xpath("//button[contains(.,'Submit')]"));
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id='auth']/div[3]/button[2]")).click();
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("baseprice")));//wait for cart to appear
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Close')]"));//click close button
		
		Thread.sleep(4000);//wait then click the button twice, yes twice, because it doesnt always work, but a 2nd click wont hurt anything
		driver.findElement(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button")).click();//add red to cart
		driver.findElement(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("baseprice")));//wait for cart to appear
		
		//all prices are the same, span, class=baseprice, so we have to do it with xpath
		//basePrice = driver.findElement(By.xpath("//*[@id='cart-summary']/tbody/tr[2]/td[4]/span")).getText();//save base price
		
		driver.findElement(By.xpath("//button[contains(.,'Close')]"));//click close button
		
		Thread.sleep(4000);
		//add red wasnt always visible, so have to scroll so it
		WebElement firstWhite = driver.findElement(By.xpath("//*[@id='8752C561-0015-4BA4-9898-17FA647CE26D']/form[1]/div/div[3]/button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstWhite);
		
		
		//add a single white wine case
		
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
