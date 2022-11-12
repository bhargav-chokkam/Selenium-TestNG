package com.BasePackage;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReport {
	public static ExtentReports getExtendReport() {
		String reportPath = System.getProperty("user.dir") + "//reports//index.html";
		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
		spark.config().setDocumentTitle("Test Results");
		spark.config().setReportName("Bhargav Chokkam");
		ExtentReports extent = new ExtentReports();
		extent.attachReporter(spark);
		return extent;
	}
}
