package com.CTM.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SendMoney extends Common {
    @FindBy(xpath = "//input[@id='AccountNumber']")
    WebElement accountNumber;

    public SendMoney() {
        PageFactory.initElements(getDriverInstance(), this);
    }


    public void enterAgentAccount(String account) {
        sendKeysToElement(accountNumber, account);
    }
}
