package com.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import Util.CommonMethods;

public class HomePage {
	WebDriver driver;
	public CommonMethods cm;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		cm = new CommonMethods(driver);
	}

	public String returnCurrenturl() {
		String currentUrl = driver.getCurrentUrl();
		return currentUrl;
	}

}
