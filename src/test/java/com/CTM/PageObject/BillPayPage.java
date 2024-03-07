package com.CTM.PageObject;

import Utility.Common;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.Map;

public class BillPayPage extends Common {

    @FindBy(xpath = "//*[@for='AccountNumber']")
    WebElement agentAccount;
    @FindBy(xpath = "//*[@for='CallingPhone']")
    WebElement agentPhone;
    @FindBy(xpath = "//*[@for='BingoCardSequenceNumber']")
    WebElement agentBingo;
    @FindBy(xpath = "//*[@id='continueButtonAgentInfo']")
    WebElement continueButton;
    @FindBy(xpath = "//*[@value='Proceed Without Customer(Alt+P)']")
    WebElement proceedWithOutCustomerButton;
    @FindBy(xpath = "//*[@id='quickPay']")
    WebElement billPayButton;
    @FindBy(xpath = "//*[@for='attentionCompanypopup']")
    WebElement company;
    @FindBy(linkText = "Bill Payment Search (F12)")
    WebElement billPaymentSearchButton1;
    @FindBy(xpath = "//*[@name='companySearchbtn']")
    WebElement billPaymentSearchButton2;

    Map<String, String> data;

    public BillPayPage() {
        data = getDataObject();
        PageFactory.initElements(getDriverInstance(), this);
    }

    public void clickMakePayment() {
//        waitTillVisibilityOfElement(Duration.ofSeconds(10), billPayButton);
        switchToFrameByNamOrID("filterIFrame");
        clickOnElement(billPayButton);
    }

    public void agentInfo() {
        sendKeysToElement(agentAccount, data.get("AgentAccount"));
        sendKeysToElement(agentPhone, data.get("AgentPhone"));
        sendKeysToElement(agentBingo, data.get("AgentBingo"));
        clickOnElement(continueButton);
    }

    public void customerSearch() {
        clickOnElement(proceedWithOutCustomerButton);
    }

    public void billerSearch(String companyName) {
        clickOnElement(billPaymentSearchButton1);
        trySendKeysToElement(company, companyName);
        clickOnElement(billPaymentSearchButton2);

    }


}
