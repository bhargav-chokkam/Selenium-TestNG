package com.CTM.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CTMHomePage extends Common {

    @FindBy(linkText = "Bill Payment/Quick Pay (F7)")
    WebElement billPay;

    public CTMHomePage() {
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void selectBillPay() {
        clickOnElement(billPay);
    }
}
