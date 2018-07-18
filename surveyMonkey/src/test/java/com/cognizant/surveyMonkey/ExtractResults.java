package com.cognizant.surveyMonkey;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognizant.surveyMonkey.Excel.ExcelUtility;
import com.cognizant.surveyMonkey.Pages.IndividualResponsePage;
import com.cognizant.surveyMonkey.Pages.NavigationPages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractResults {

	private static WebDriver driver;
	private static Selenium selenium;

	private static IndividualResponsePage individualResponsePage;
	private static NavigationPages navigationPages;

	public static void main(String[] args) throws InterruptedException {

		ExtractResults er = new ExtractResults();

		ExcelUtility.setExcelFileSheet("Sheet1");

		selenium = new Selenium();
		driver = selenium.getDriver();

		individualResponsePage = new IndividualResponsePage(driver);
		navigationPages = new NavigationPages(driver);

		navigationPages.logIn();
		navigationPages.gotoIndividualResponses();

		int numOfRespondents = individualResponsePage.getNumberOfRespondents();
		System.out.println("Number of Respondents: " + numOfRespondents);

		int currentRespondentNumber = individualResponsePage.getCurrentRespondentNumber();
		// WebElement buttonPreviousRespondent = driver
		// .findElement(By.xpath("//a[contains(@title,'Previous respondent')]"));
		WebElement buttonNextRespondent;

		individualResponsePage.gotoRespondent("1");

		// numOfRespondents
		for (int i = 1; i <= numOfRespondents; i++) {
			System.out.println("Respondent " + i);
			WebElement respondentElement = individualResponsePage.getVisibleRespondent();

			er.getRespondentResponse(respondentElement, i);

			buttonNextRespondent = driver.findElement(By.cssSelector(".wds-button--arrow-right"));
			if (i < numOfRespondents) {
				buttonNextRespondent.click();
			}
			Thread.sleep(2000);
		}
		
	}

	public void getRespondentResponse(WebElement respondentElement, int respondentNumber) {
		String email = respondentElement.findElement(By.xpath(".//span[text()='Email:']/following-sibling::span"))
				.getText();
		System.out.println(email);
		int respondentCol = (respondentNumber - 1) * 2 + 2;
		ExcelUtility.setCellData("Respondent #" + respondentNumber, 0, respondentCol);
		ExcelUtility.setCellData(email, 1, respondentCol);

		List<WebElement> questionElements = respondentElement
				.findElements(By.cssSelector(".response-question-container"));

		int i = 1;
		for (WebElement questionElement : questionElements) {

			System.out
					.println("-----" + questionElement.findElement(By.className("question-title")).getText() + "-----");
			String qTitle = questionElement.findElement(By.className("question-title")).getText();
			if (i < 5) {
				individualResponsePage.getMatrixRatingQuestion(questionElement, qTitle, respondentCol);
			}
			if (i == 5) {
				individualResponsePage.getMultipleChoiceQuestion(questionElement, qTitle, respondentCol);
			}
			if (i == 6) {
				individualResponsePage.getEssayQuestion(questionElement, qTitle, respondentCol);
			}
			i++;
		}
	}
}
