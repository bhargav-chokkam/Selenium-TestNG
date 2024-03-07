package com.Digital.TestCases;

import Utility.BaseClass;
import com.Digital.PageObject.LoginPage;
import org.testng.annotations.Test;

public class USSendMoney extends BaseClass {
    @Test(description = "Login US", groups = {"LoginPage"})
    public void loginValidCredentials() {
        com.getUrl(com.readProperty("DigitalUAT"));
        LoginPage loginPage = new LoginPage();
        loginPage.enterUserName(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.submitForm();
        com.waitUrlToBe(data.get("HomeUrl"));
    }
}

