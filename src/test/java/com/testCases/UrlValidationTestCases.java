package com.testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.BasePackage.BaseClass;
import com.BasePackage.Retry;
import com.pageObjects.HomePage;

public class UrlValidationTestCases extends BaseClass {

	@Test(groups = { "HappyPath" })
	public void urlValidationHappyPath() throws InterruptedException {
		lpobj.enterUserName("bhargavchokkam@25gmail.com");
		lpobj.enterPassword("Dheyam@5562");
		HomePage hp = lpobj.clickLogin();
		Thread.sleep(5000);
		String currentUrl = hp.returnCurrenturl();
		boolean assertValue = compareString(currentUrl, "https://rahulshettyacademy.com/client/dashboard/dash");
		Assert.assertTrue(assertValue);
	}

	@Test(groups = { "ErrorValidation" }, retryAnalyzer = Retry.class)
	public void invalidUrlValidation() throws InterruptedException {
		lpobj.enterUserName("bhargavchokkam@25gmail.com");
		lpobj.enterPassword("Dheyam@5562");
		HomePage hp = lpobj.clickLogin();
		Thread.sleep(5000);
		String currentUrl = hp.returnCurrenturl();
		boolean assertValue = compareString(currentUrl, "https://rahulshettyacademy.com/client/dashboard/");
		Assert.assertTrue(assertValue);

	}

}
