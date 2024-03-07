package com.TaxUI.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SandBox extends Common {
    @FindBy(xpath = "//a[contains(@aria-label,'Sandbox')]")
    WebElement Sandbox;
    @FindBy(xpath = "//a[contains(@aria-label,'TaxUI QA')]")
    WebElement TaxUI;

    public SandBox() {
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void oktaSandBox() {
        clickOnElement(Sandbox);
    }

    public void taxUI() {
        clickOnElement(TaxUI);
    }
}