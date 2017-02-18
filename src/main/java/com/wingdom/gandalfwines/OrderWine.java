package com.wingdom.gandalfwines;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.TestNG;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.*;


public class OrderWine {
	
	public static String driverType = "chrome";
	
	public static String cardNumber;
	public static String cardType;
	public static String cardName;
	public static String cardExp;
	public static String cardCVV;
	public static String testURL = "http://gandalfwines.ewinerysolutions.com/";
	
	public static Logger logger = LoggerFactory.getLogger(OrderWine.class);
	
	public static void main(String args[]){
		
		List<String>firefoxFiles = new ArrayList<String>();
		List<String>chromeFiles = new ArrayList<String>();
		
		//direct file paths for running locally in eclipse
		File firefoxTestXML = new File("src/main/resources/firefox.xml");
		File chromeTestXML = new File("src/main/resources/chrome.xml");
		
		//if running as a jar, we need the new location of the xml files
		if(!firefoxTestXML.exists() || !chromeTestXML.exists()){
			firefoxTestXML = new File(System.getProperty("user.dir") + "/firefox.xml");
			chromeTestXML = new File(System.getProperty("user.dir") + "/chrome.xml");
			
			InputStream ffis = OrderWine.class.getResourceAsStream("/firefox.xml");
			InputStream chis = OrderWine.class.getResourceAsStream("/chrome.xml");
			
			try{
				firefoxTestXML.setWritable(true);
				chromeTestXML.setWritable(true);
				
				java.nio.file.Files.copy(ffis, firefoxTestXML.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				java.nio.file.Files.copy(chis, chromeTestXML.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
			}catch(IOException e){
				OrderWine.logger.error("Couldn't copy XML files while runnig as jar", e);
			}
		}//fi
		
		//runs chrome first, after chrome runs, firefox runs. 
		//I would like it to ask for one or the other,
		//but that isnt implemented yet.
		if(driverType == "chrome"){
			chromeFiles.add(chromeTestXML.getAbsolutePath());
			
			TestNG chromeTest = new TestNG();
			chromeTest.setTestSuites(chromeFiles);
			chromeTest.run();
			driverType = "firefox";
		}else if(driverType == "firefox"){
			firefoxFiles.add(firefoxTestXML.getAbsolutePath());
			
			TestNG firefoxTest = new TestNG();
			firefoxTest.setTestSuites(firefoxFiles);
			firefoxTest.run();
		}else{
			OrderWine.logger.error("No driver type was specified, eding now");
			System.exit(0);
		}
		
		System.exit(0);
	}//main
	
	//if Chrome is specified/available, run on Chrome, if not, run on Firefox
	public static WebDriver setDriver(String driverType){
		WebDriver driver;
		
		if(driverType.equals("chrome")){
			////new chrome driver
			driver = new ChromeDriver();
		}else{
			//webdrivermanager firefox setup (not necessary, but replaces the built in firefox with the latest)
			FirefoxDriverManager.getInstance().setup();
			//new firefox driver
			driver = new FirefoxDriver();
		}
		return driver;
	}
}
