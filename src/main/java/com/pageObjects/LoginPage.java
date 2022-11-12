package com.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.UtilityPackage.CommonMethods;

public class LoginPage extends CommonMethods {
	WebDriver driver;
	public HomePage hp;
	public CommonMethods cm;

	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		cm = new CommonMethods(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@id='userEmail']")
	WebElement userNameText;

	@FindBy(xpath = "//input[@id='userPassword']")
	WebElement passWord;

	@FindBy(xpath = "//input[@name='login']")
	WebElement loginButton;

	@FindBy(xpath = "//div[contains(@class,'toast-message')]")
	WebElement toastMessage;
	@FindBy(xpath = "//*[text()='Automation']")
	WebElement automationIcon;

	public void enterUserName(String userName) {
		userNameText.sendKeys(userName);
	}

	public void enterPassword(String password) {
		passWord.sendKeys(password);
	}

	public HomePage clickLogin() {
		loginButton.click();
		HomePage hp = new HomePage(driver);
		return hp;
	}

	public void toastMessage() {
		explicitWait(toastMessage);
		toastMessage.isEnabled();
	}
}
