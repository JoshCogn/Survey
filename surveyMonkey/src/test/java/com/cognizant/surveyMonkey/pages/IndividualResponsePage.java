package com.cognizant.surveyMonkey.pages;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognizant.surveyMonkey.excel.ExcelUtility;

public class IndividualResponsePage {
	
	WebDriver driver;
	
	public IndividualResponsePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean doesByElementExist(By bySelector) {
		return doesByElementExist(bySelector, driver);
	}
	
	public boolean doesByElementExist(By bySelector, SearchContext element) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		Boolean exists = !(element.findElements(bySelector).isEmpty());
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return exists;
	}

	
	public WebElement getVisibleRespondent() {
		List<WebElement> respondents = driver.findElements(By.className("respondent"));
		WebElement visibleRespondent = null;
		for (WebElement respondent : respondents) {
			//System.out.println("isDisplayed:" + respondent.isDisplayed());
			if (respondent.isDisplayed()) {
				//Boolean elementDoesntExist = respondent.findElements(By.cssSelector("div.respondent-profile")).isEmpty();
				Boolean elementExists = doesByElementExist(By.cssSelector("div.respondent-profile"));
				if (elementExists) {
					//System.out.println("...and contains data");
					visibleRespondent = respondent;
				} else {
					//System.out.println("...but it's empty");
				}
			}
		}
		return visibleRespondent;
	}


	public int getNumberOfRespondents() {
		String numOfRespondents = driver.findElement(By.cssSelector(".stats-header h4")).getText();

		Pattern r = Pattern.compile("(\\d+$)");
		Matcher m = r.matcher(numOfRespondents);
		if (m.find()) {
			numOfRespondents = m.group(1);
		} else {
			System.out.println("NO match for number of respondents");
			numOfRespondents = "1";
		}
		
		return Integer.parseInt(numOfRespondents);
	}

	public int getCurrentRespondentNumber() {
		String currentRespondentNumber = driver.findElement(By.cssSelector("a[view-role='respondentMenuBtnView']")).getText();

		Pattern r = Pattern.compile("#(\\d+)");
		Matcher m = r.matcher(currentRespondentNumber);
		if (m.find()) {
			currentRespondentNumber = m.group(1);
		} else {
			System.out.println("NO match for number of respondents");
			currentRespondentNumber = "1";
		}
		
		return Integer.parseInt(currentRespondentNumber);
	}
	
	
	
	public void getMultipleChoiceQuestion(WebElement questionElement, String qTitle, int respondentRow) {

		int questionCol = ExcelUtility.getColumnNumberOfLabel(qTitle,0);
		int optionCol;

		List<WebElement> responseElements = questionElement
				.findElements(By.cssSelector(".response-container li.response-list-item"));
		String response = null;

		for (WebElement responseElement : responseElements) {
			response = responseElement.findElement(By.cssSelector(".response-text")).getText();
			optionCol = ExcelUtility.getColumnNumberOfLabel(response, 1, questionCol);

			System.out.println(response);
			ExcelUtility.setCellData(response, respondentRow, optionCol);
		}
		boolean commentExists = doesByElementExist(By.cssSelector(".response-list-comment"),questionElement);
		if (commentExists) {
			//System.out.println("Comment exists:");
			response = questionElement.findElement(By.cssSelector(".response-text.other-item.ta-response-item")).getText();
			//System.out.println(response);
			optionCol = ExcelUtility.getColumnNumberOfLabel("Other / More details:", 1, questionCol);
			ExcelUtility.setCellData(response, respondentRow, optionCol);

		} else {
			//System.out.println("No Comment");
		}
		
	}
	
	public void getEssayQuestion(WebElement questionElement, String qTitle, int respondentRow) {
		String response;
		int questionCol = ExcelUtility.getColumnNumberOfLabel(qTitle,0);

		Boolean noResponse = doesByElementExist(By.cssSelector("div.no-response-text"), questionElement);

		if (noResponse) {
			response = "No response";
		} else {
			response = questionElement.findElement(By.cssSelector("p.response-text")).getText();
			ExcelUtility.setCellData(response, respondentRow, questionCol);
		}

	}
	
	public void getMatrixRatingQuestion(WebElement questionElement, String qTitle, int respondentRow) {
		int questionCol = ExcelUtility.getColumnNumberOfLabel(qTitle,0);
		System.out.println(qTitle + " is col: " + questionCol);
		int optionCol;
		String responseLabel = "none";
		String responseAnswer = "-1";
		List<WebElement> responseElements = questionElement
				.findElements(By.cssSelector(".response-container li"));
		// .response-container li  - to include comments
		int count = 0;
		for (WebElement responseElement : responseElements) {
			responseLabel = responseElement.findElement(By.className("response-text-label")).getText();
			responseAnswer = responseElement.findElement(By.className("response-text")).getText();
			
			optionCol = ExcelUtility.getColumnNumberOfLabel(responseLabel.replace("\u00a0",""), 1, questionCol);
			//System.out.println(responseLabel + ": " + responseAnswer);
			try {
				int intAnswer = Integer.parseInt(responseAnswer);
				ExcelUtility.setCellData(intAnswer, respondentRow, optionCol);
			} catch (Exception NumberFormatException) { // aka 
				ExcelUtility.setCellData(responseAnswer, respondentRow, optionCol);
			}
			count++;
		}
		
		
		
	}

	public void gotoRespondent(String respondentNum) {
		driver.findElement(By.xpath("//a[@view-role='respondentMenuBtnView']")).click();
		WebElement gotoInput = driver.findElement(By.cssSelector("input.goto-number-text"));
		gotoInput.click();
		gotoInput.clear();
		gotoInput.sendKeys(respondentNum);
		driver.findElement(By.cssSelector("button.goto-btn")).click();		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
	}
	
	
	
	
}
