package com.Wupos.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

public class MakePaymentPage extends Common {


    @FindBy(xpath = "//*[@id='senderLookupBy']")
    WebElement senderLookupDropDown;
    @FindBy(xpath = "//*[@id='lookupPhoneNumber']")
    WebElement phNoLookupTextBox;
    @FindBy(xpath = "//*[@id='lookupPhoneNumberLbw']")
    WebElement phNoLookupSearchIcon;
    @FindBy(xpath = "//table[@id='senderlookupMP']/tbody")
    List<WebElement> senderLookupValues;

    Map<String, String> data;

    public MakePaymentPage() {
        this.data = getDataObject();
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void customerLookup(String lookUpBy) {
        Select dropDown = new Select(senderLookupDropDown);

        switch (lookUpBy) {
            case "Email":
//                trySelectByVisibleText(dropDown, "Email");
//                trySendKeysToElement(emailLookupTextBox, data.get("Email"));
//                break;
            case "Phone":
                trySelectByVisibleText(dropDown, "Phone");
                trySendKeysToElement(phNoLookupTextBox, data.get("Phone"));
                tryClickOnElement(phNoLookupSearchIcon);
                tryDropDownValueClick(senderLookupValues, data.get("CustomerLookUpName"));
                tryDropDownValueClick(senderLookupValues, data.get("CompanyLookUp"));
                break;

        }
    }
}
