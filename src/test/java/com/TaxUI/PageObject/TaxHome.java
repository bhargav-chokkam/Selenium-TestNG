package com.TaxUI.PageObject;

import Utility.Common;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TaxHome extends Common {
    private final ExtentTest spark;
    @FindBy(xpath = "//a[@href='/taxes/tax-definition']")
    WebElement taxDefinition;
    @FindBy(xpath = "//div[@col-id='ruleName']//span[@ref='eMenu']")
    WebElement ruleNameFilter;
    @FindBy(xpath = "//input[@id='filterText']")
    WebElement filterText;
    @FindBy(xpath = "//div[@row-index='0']//div[@col-id='ruleName']")
    WebElement filterFirstIndex;
    @FindBy(xpath = "//i[contains(@class,'eye')]")
    WebElement view;
    @FindBy(xpath = "//i[contains(@class,'trash')]")
    WebElement delete;
    @FindBy(xpath = "//i[contains(@class,'edit')]")
    WebElement edit;
    @FindBy(xpath = "//button[text()='Done']")
    WebElement done;

    public TaxHome() {
        spark = getSparkInstance();
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void clickTaxDefinition() {
        clickOnElement(taxDefinition);
        spark.addScreenCaptureFromPath(getScreenshot("TaxDefinition"), "TaxDefinition");
    }

    public void filterName(String name) {
        moveMouseToElement(ruleNameFilter);
        clickOnElement(ruleNameFilter);
        sendKeysToElement(filterText, name);
        clickOnElement(filterFirstIndex);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        spark.addScreenCaptureFromPath(getScreenshot("RuleFiltered"), "RuleFiltered");
    }

    public void viewRule() {
        clickOnElement(view);
        spark.addScreenCaptureFromPath(getScreenshot("ViewScreen"), "ViewScreen");
    }

    public void viewDone() {
        clickOnElement(done);
        spark.addScreenCaptureFromPath(getScreenshot("ViewScreenDone"), "ViewScreenDone");
    }

    public void editRule() {
        clickOnElement(edit);
        spark.addScreenCaptureFromPath(getScreenshot("EditScreen"), "EditScreen");
    }

    public void deleteRule() {
        clickOnElement(delete);
        spark.addScreenCaptureFromPath(getScreenshot("DeleteScreen"), "DeleteScreen");
    }
}