package com.cognizant.surveyMonkey;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Selenium {
	
	public WebDriver getDriver() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\695136\\Software\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.surveymonkey.com/user/sign-in/?ut_source=megamenu");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);		
		
		return driver;
	}
}
