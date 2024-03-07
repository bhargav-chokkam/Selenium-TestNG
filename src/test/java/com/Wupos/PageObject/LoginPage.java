package com.Wupos.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends Common {
    @FindBy(xpath = "//*[@type='text']")
    WebElement userName;
    @FindBy(xpath = "//*[@type='password']")
    WebElement password;
    @FindBy(xpath = "//*[text()='Log In']")
    WebElement logIn;
    @FindBy(xpath = "//input[@id='swbUserFirstNames']")
    WebElement FLAFirstName;
    @FindBy(xpath = "//input[@id='swbUserLastNames']")
    WebElement FLALastName;
    @FindBy(xpath = "//*[@id='proceedSwbAlert']/span[text()='Proceed']")
    WebElement Proceed;

    public LoginPage() {
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void userNamePassword(String username, String password) {
        sendKeysToElement(userName, username);
        sendKeysToElement(this.password, password);
        clickOnElement(logIn);
//        waitUrlToBe(data.get("wuposHome"));
        refreshBrowser();
    }

    public void tryFLA() {
        trySendKeysToElement(FLAFirstName, "Koumya");
        trySendKeysToElement(FLALastName, "Singamsetti");
        tryClickOnElement(Proceed);
    }
}
