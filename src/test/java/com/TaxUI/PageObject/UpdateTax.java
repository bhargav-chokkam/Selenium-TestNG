package com.TaxUI.PageObject;

import Utility.Common;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class UpdateTax extends Common {
    private final ExtentTest spark;
    @FindBy(xpath = "//input[@name='comment']")
    WebElement commentField;
    @FindBy(xpath = "//button[text()='Confirm']")
    WebElement confirmButton;
    @FindBy(xpath = "//button[text()='Update']")
    WebElement updateButton;


    public UpdateTax() {
        spark = getSparkInstance();
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void setComment(String comment) {
        clearElementField(commentField);
        sendKeysToElement(commentField, comment);
        spark.addScreenCaptureFromPath(getScreenshot("UpdateScreen"), "UpdateScreen");
    }

    public void clickOnConfirmButton() {
        clickOnElement(confirmButton);
        spark.addScreenCaptureFromPath(getScreenshot("ConfirmButton"), "ConfirmButton");
    }

    public void clickOnUpdateButton() {
        clickOnElement(updateButton);
        waitTillInvisibilityOf(Duration.ofSeconds(2000), updateButton);
        spark.addScreenCaptureFromPath(getScreenshot("UpdateButton"), "UpdateButton");
    }
}
