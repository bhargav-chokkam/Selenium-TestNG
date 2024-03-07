package com.Wupos.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends Common {

    @FindBy(xpath = "//*[@id=\"makePaymentLink\"]/div/div[2]")
    WebElement MakePaymentButton;
    @FindBy(linkText = "Start a new transaction")
    WebElement NewTrxn;
    @FindBy(id = "closePinEntry")
    WebElement closeDialog;

    public HomePage() {
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void selectMakePayment() {
        tryClickOnElement(MakePaymentButton);
        tryClickOnElement(NewTrxn);
    }

    public void tryCloseDialog() {
        tryClickOnElement(closeDialog);
    }

}
