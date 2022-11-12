package com.BasePackage;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CommonMethods {
	public WebDriver driver;

	public CommonMethods(WebDriver driver) {
		this.driver = driver;
	}

	public void verifyCurrentUrl(String actualUrl) {
		String currentUrl = driver.getCurrentUrl();
		try {
			Assert.assertEquals(actualUrl, currentUrl);
			System.out.println("Url Comparision Assertion is Passed");
			System.out.println("Current Url: " + currentUrl);
		} catch (Exception e) {
			System.out.println("Assertion Failed: Current Url is not matched with the expected Url");
			System.out.println("Current Url: " + currentUrl);
			System.out.println("Expected Url: " + actualUrl);
		}
	}
}