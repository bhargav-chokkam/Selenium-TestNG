package com.Digital.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends Common {
    @FindBy(xpath = "//a[@id='menu-send-money']")
    WebElement sendMoney;
    @FindBy(xpath = "//a[@id='cash-pickup-retail']")
    WebElement pickUpCash;
    @FindBy(xpath = "//a[@id='menu-track-transfer']")
    WebElement tractTransfer;
    @FindBy(xpath = "//a[@id='menu-pay-bill']")
    WebElement payBill;

    public HomePage() {
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void sendMoney() {
        clickOnElement(sendMoney);
    }
}
