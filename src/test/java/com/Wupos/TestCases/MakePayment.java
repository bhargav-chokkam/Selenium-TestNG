package com.Wupos.TestCases;

import Utility.BaseClass;
import com.Wupos.PageObject.HomePage;
import com.Wupos.PageObject.LoginPage;
import com.Wupos.PageObject.MakePaymentPage;
import org.testng.annotations.Test;

public class MakePayment extends BaseClass {

    @Test(groups = {"MakePayment"})
    public void MakePaymentTransaction() {
        com.getUrl(com.readProperty("WUPOS3"));
        LoginPage lp = new LoginPage();
        lp.userNamePassword(data.get("username"), data.get("password"));
        lp.tryFLA();
        HomePage hp = new HomePage();
        hp.tryCloseDialog();
        hp.selectMakePayment();
        MakePaymentPage mp = new MakePaymentPage();
        mp.customerLookup(data.get("CustomerLookUpBy"));
    }
}
