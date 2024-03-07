package com.CTM.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CTMLaunch extends Common {

    @FindBy(xpath = "//input[@name='username']")
    WebElement username;
    @FindBy(xpath = "//input[@name='password']")
    WebElement password;
    @FindBy(xpath = "//input[@data-type='save']")
    WebElement SignIn;
    @FindBy(xpath = "//span[text()='CTM QA Environment']")
    WebElement CTM;
    @FindBy(xpath = "//input[@id='password']")
    WebElement ctmPassword;
    @FindBy(xpath = "//button[@id='submitBtn']")
    WebElement submitBtn;

    public CTMLaunch() {
        PageFactory.initElements(getDriverInstance(), this);
    }



    public void optionalOktaLogin() {
        try {
            trySendKeysToElement(username, readProperty("id"));
            trySendKeysToElement(password, readProperty("password"));
            tryClickOnElement(SignIn);
        } catch (Exception e) {
            System.out.println("Okta Login Skipped");
        }
    }
    public void loginCTM() {
        clickOnElement(CTM);
        switchWindowByTitle("Customer Transaction Management");
        trySendKeysToElement(ctmPassword, readProperty("CTMPass"));
        tryClickOnElement(submitBtn);
    }

}
