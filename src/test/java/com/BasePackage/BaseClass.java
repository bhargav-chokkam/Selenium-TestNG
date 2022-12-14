package com.BasePackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pageObjects.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	public WebDriver driver;
	public LoginPage lpobj;

	public WebDriver LaunchBrowser() throws IOException {
		Properties objProperty = new Properties();
		System.out.println(System.getProperty("user.dir"));
		FileInputStream objFile = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//com//resources//Properties.properties");
		objProperty.load(objFile);
		String browser = System.getProperty("browser") != null ? System.getProperty("browser")
				: objProperty.getProperty("browser");
		if (browser.contains("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.contains("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browser.contains("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else {
			System.out.println("Passed browser not found. Supported Browsers are: ");
			System.out.println("Chrome");
			System.out.println("Firefox");
			System.out.println("Edge");
			System.exit(0);
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		return driver;
	}

	@Parameters({ "url" })
	@BeforeMethod(alwaysRun = true)
	// alwaysRun enables method to execute even if method is not part of group
	public void launchWebSite(String url) throws IOException {
		driver = LaunchBrowser();
		driver.get(url);
		lpobj = new LoginPage(driver);
	}

	@AfterMethod(alwaysRun = true)
	public void quiteBrowser() {
		driver.quit();
		System.out.println("***quiteBrowser executed***");
	}

	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";

	}

	public List<HashMap<String, String>> readJsontoMap(String filePath) throws IOException
	{
		String jsonContent = FileUtils.readFileToString(new File(System.getProperty("user.dir") + filePath),
				StandardCharsets.UTF_8);
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
	}

	public boolean compareString(String actualString, String expectedString) {
		try {
			Assert.assertEquals(actualString, expectedString);
			return true;
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
