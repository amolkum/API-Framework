
package utils;

import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class CommonFunctions {
	public static void assertValue(String actual, String expected, String message, ExtentTest report) {
	    try {
	    	Assert.assertEquals(actual, expected);
			report.log(LogStatus.PASS, message + " actual: " + actual + ", expected: " + expected );
	    } catch (AssertionError e) {
	        report.log(LogStatus.FAIL, message + " (actual: " + actual + ", expected: " + expected + ")");
	        report.log(LogStatus.INFO, e.getMessage().toString());
	    }
	}
	public static void assertStatusCode(int actual, String expected, String message, ExtentTest report) {
		try {
			String code = Integer.toString(actual);
			Assert.assertEquals(code, expected);
			report.log(LogStatus.PASS, message + " actual: " + actual + ", expected: " + expected);
		} catch (AssertionError e) {
			 report.log(LogStatus.FAIL, message + " (actual: " + expected + ", expected: " + expected + ")");
	        report.log(LogStatus.INFO, e.getMessage().toString());
		}
	}
}

