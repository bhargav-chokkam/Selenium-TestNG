package com.testCases;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.BasePackage.BaseClass;

/*
 * We have test cases here under testCases Package and under BasePackage we have @BeforeMethod which will 
 * intilize browser and @AfterMethod which will close 
 * 
 */
public class LoginTestCases extends BaseClass {
	public String jsonFiles = "//src//main//java//com//data//";

	@Test(groups = { "HappyPath" }, dataProvider = "getData")

	public void loginHappyPath(String username, String password) throws InterruptedException {
		lpobj.enterUserName(username);
		lpobj.enterPassword(password);
		lpobj.clickLogin();
	}

	@Test(groups = "ErrorValidation", dataProvider = "getInvalidUsername")
	public void invalidUsername(HashMap<String, String> input) throws InterruptedException {
		lpobj.enterUserName(input.get("username"));
		lpobj.enterPassword(input.get("password"));
		lpobj.clickLogin();
		lpobj.toastMessage();
	}

	@Test(groups = "ErrorValidation", dataProvider = "getInvalidPassword")
	public void invalidPassword(HashMap<String, String> input) {
		lpobj.enterUserName(input.get("userName"));
		lpobj.enterPassword(input.get("password"));
		lpobj.clickLogin();
		lpobj.toastMessage();
	}

	@DataProvider
	public Object[][] getData() {
		Object[][] data = new Object[2][2];
		data[0][0] = "bhargavchokkam@25gmail.com";
		data[0][1] = "Dheyam@5562";
		data[1][0] = "bhargavchokkam@25gmail.com";
		data[1][1] = "Dheyam@5562";
		return data;
	}

	@DataProvider
	public Object[][] getInvalidUsername() {
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("username", "bhargakkam@gmail.com");
		map1.put("password", "Dheyam@5562");
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("username", "bhargachom@mail.com");
		map2.put("password", "Dheyam@5562");
		return new Object[][] { { map1 }, { map2 } };
	}

	@DataProvider
	public Object[][] getInvalidPassword() throws IOException {
		String jsonFileLocation = jsonFiles + "testData.json";
		List<HashMap<String, String>> data = readJsontoMap(jsonFileLocation);
		return new Object[][] { { data.get(0) }, { data.get(1) } };

	}

}
