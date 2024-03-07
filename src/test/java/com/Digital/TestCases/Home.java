package com.Digital.TestCases;

import Utility.BaseClass;
import com.Digital.PageObject.HomePage;
import com.Digital.PageObject.LoginPage;
import org.testng.annotations.Test;

public class Home extends BaseClass {

    @Test(groups = {"SendMoney"})
    public void sendMoneyValidation() {
        com.getUrl(com.readProperty("baseUrlDigitalUS"));
        LoginPage loginPage = new LoginPage();
        loginPage.enterUserName(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.submitForm();
        com.waitUrlToBe(data.get("HomeUrl"));
        HomePage homePage = new HomePage();
        homePage.sendMoney();
        com.waitUrlToBe(data.get("sendMoneyStart"));
    }
}