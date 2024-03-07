package com.Digital.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends Common {
    @FindBy(xpath = "//input[@id='txtEmailAddr']")
    WebElement userName;
    @FindBy(xpath = "//input[@id='txtKey']")
    WebElement password;
    @FindBy(xpath = "//div[contains(@class,'continue')]")
    WebElement continueButton;

    public LoginPage() {
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void enterUserName(String user) {
        sendKeysToElement(userName, user);
    }

    public void enterPassword(String pass) {
        sendKeysToElement(password, pass);
    }

    public void submitForm() {
        clickOnElement(continueButton);
    }
}
