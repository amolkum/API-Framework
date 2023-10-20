import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SampleTest1 extends Reporter {
    FilterableRequestSpecification requestSpecification1;
    RequestLoggingFilter requestLoggingFilter;
    ResponseLoggingFilter responseLoggingFilter;
    public PrintStream log;
    Response response;
    ValidatableResponse validatableResponse;
    @Test(dataProvider = "getStoreInv")
    public void test_getStoreInventory(Map<String, String> testData) throws IOException {
        /* Returns results of store inventory */

        final Logger logger = LogManager.getLogger(StoreTests.class.getName());
        DOMConfigurator.configure(System.getProperty("user.dir") + "\\src\\test\\log4j.xml");

        ExtentTest inventoryTest = report.startTest("getStoreInventory");
        System.out.println(testData);
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);

        Response res= PetStoreApi.getInventory();

        /*Response res = given()
                .contentType("application/json").filters(requestLoggingFilter, responseLoggingFilter)
                .when()
                .get("https://petstore.swagger.io/v2/store/inventory")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        logger.info("Getting The status code");

        //String body = res.body().asString();*/

        try {
            CommonFunctions.assertStatusCode(res.getStatusCode(), testData.get("ExpectedResponse"), "Status Code Check", inventoryTest);

        } catch (AssertionError e) {
            inventoryTest.log(LogStatus.FAIL, Integer.toString(response.getStatusCode()));
            //inventoryTest.log(LogStatus.INFO, body);
        }
        report.endTest(inventoryTest);
        report.flush();
    }

    @DataProvider(name = "getStoreInv")
    public static Iterator<Object[]> getStoreInv() throws IOException {

        List<Object[]> list = new ArrayList<Object[]>();

        int rows = ExcelUtility.getRowCount(Constants.getStoreInvData, Constants.getStoreInvDataSheet);
        int colCount = ExcelUtility.getCellCount(Constants.getStoreInvData, Constants.getStoreInvDataSheet, 1);
        int subtractRows = 1;
        String[] keys = new String[colCount];

        //Get keys for hash map
        for (int i = 0; i < colCount; i++) {
            keys[i] = ExcelUtility.getCellData(Constants.getStoreInvData, Constants.getStoreInvDataSheet, 0, i);
        }

        for (int i = 0; i < rows; i++) {
            if (ExcelUtility.getCellData(Constants.getStoreInvData, Constants.getStoreInvDataSheet, i, 1).toLowerCase().contains("no")) {
                subtractRows++;
            }
        }

        for (int row = 1; row < rows; row++) {

            if (ExcelUtility.getCellData(Constants.getStoreInvData, Constants.getStoreInvDataSheet, row, 1).toLowerCase().contains("no")) {
                continue;
            }

            Map<String, String> testData = new HashMap<String, String>();
            int keyCounter = 0;
            for (int col = 0; col < colCount; col++) {

                testData.put(keys[keyCounter], ExcelUtility.getCellData(Constants.getStoreInvData, Constants.getStoreInvDataSheet, row, col));
                keyCounter++;
            }
            list.add(new Object[]{testData});

        }
        return list.iterator();
    }


}
