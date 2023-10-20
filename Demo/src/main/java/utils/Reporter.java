package utils;




import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;



public class Reporter {
	public static ExtentReports report = new ExtentReports("reports/TestReport.html");
	
	@AfterMethod
	public void reportTeardown() {
		report.flush();

	}
}
