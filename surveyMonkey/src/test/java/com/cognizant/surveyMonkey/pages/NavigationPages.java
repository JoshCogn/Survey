package com.cognizant.surveyMonkey.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationPages {
	
	WebDriver driver;
	
	public NavigationPages(WebDriver driver) {
		this.driver = driver;
	}

	public void logIn() {
		driver.findElement(By.id("username")).sendKeys("imaclaine_cts");
		driver.findElement(By.id("password")).sendKeys("myctspasswordXX");
		driver.findElement(By.xpath("//button[text()='LOG IN']")).click();		
	}

	public void gotoIndividualResponses() {
		driver.navigate().to("https://www.surveymonkey.com/analyze/browse/fje2ADDjgQ3X0CbIXXRngXSTQNV8tfBotsDZIET_2FNh0_3D");		
	}
	
	

}
