import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.*;
import utils.Constants;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class SampleTest extends Reporter {

    RequestSpecification requestSpecification;
    FilterableRequestSpecification requestSpecification1;
    Response response;
    ValidatableResponse validatableResponse;
    public PrintStream log;
    RequestLoggingFilter requestLoggingFilter;
    ResponseLoggingFilter responseLoggingFilter;


    @Test(dataProvider = "updateStoreOrder")
    public void test_addStoreOrder(HashMap<String, String> map) throws IOException {
        /* Adds a Store order */
        //final Logger logger = LogManager.getLogger(SampleTest.class.getName());
        //DOMConfigurator.configure(System.getProperty("user.dir")+"\\src\\test\\log4j.xml");
        ExtentTest addStoreOrderTest = report.startTest("Update Store Order");

        HashMap<String, Object> testData = new HashMap<>();
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging6.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);

        Object id = map.get("id");
        Object petId = map.get("userName");
        Object firstName = map.get("firstName");
        Object lastName = map.get("lastName");
        Object email = map.get("email");
        Object password = map.get("password");
        Object phoneNumber = map.get("phone");
        Object userStatus = map.get("1");

        testData.put("id", "1");
        testData.put("petId", "12");
        testData.put("firstName", "Amol");
        testData.put("lastName", "Kumbhare");
        testData.put("email", "Abc@gmail.com");
        testData.put("password", "123");
        testData.put("phone", "1234567890");
        testData.put("userStatus", "1");

        Response res=PetStoreApi.put(testData);

        System.out.println("Response string is :" + res.jsonPath().getString("message"));
        /*Response res = given()
                .contentType("application/json").filters(requestLoggingFilter, responseLoggingFilter).config(RestAssuredConfig.config().logConfig(LogConfig.logConfig().blacklistHeader("Set-Cookie")))
                .body(testData)
                .when().log().all()
                .put("https://petstore.swagger.io/v2/user/amol")
                .then().log().all()
                .statusCode(200)
                .log().body()
                .extract().response();*/

        try {
            CommonFunctions.assertStatusCode(res.getStatusCode(), map.get("ExpectedResponse"), "Status Code Check", addStoreOrderTest);
            CommonFunctions.assertValue(map.get("id"), res.jsonPath().getString("message"), "ID Check", addStoreOrderTest);
        } catch (AssertionError e) {
            addStoreOrderTest.log(LogStatus.INFO, res.body().toString());
            e.printStackTrace();
            Assert.fail();
        }

    }

    @DataProvider(name = "updateStoreOrder")
    public static Iterator<Object[]> csvReader() throws IOException {

        List<Object[]> list = new ArrayList<Object[]>();

        int rows = ExcelUtility.getRowCount(Constants.UpdateStoreOrderData, Constants.UpdateStoreOrderDataSheet);
        int colCount = ExcelUtility.getCellCount(Constants.UpdateStoreOrderData, Constants.UpdateStoreOrderDataSheet, 1);
        int subtractRows = 1;
        String[] keys = new String[colCount];

        //Get keys for hash map
        for (int i = 0; i < colCount; i++) {
            keys[i] = ExcelUtility.getCellData(Constants.UpdateStoreOrderData, Constants.UpdateStoreOrderDataSheet, 0, i);
        }


        for (int i = 0; i < rows; i++) {
            if (ExcelUtility.getCellData(Constants.UpdateStoreOrderData, Constants.UpdateStoreOrderDataSheet, i, 1).toLowerCase().contains("no")) {
                subtractRows++;
            }
        }


        for (int row = 1; row < rows; row++) {

            if (ExcelUtility.getCellData(Constants.UpdateStoreOrderData, Constants.UpdateStoreOrderDataSheet, row, 1).toLowerCase().contains("no")) {
                continue;
            }

            Map<String, String> testData = new HashMap<String, String>();
            int keyCounter = 0;
            for (int col = 0; col < colCount; col++) {

                testData.put(keys[keyCounter], ExcelUtility.getCellData(Constants.UpdateStoreOrderData, Constants.UpdateStoreOrderDataSheet, row, col));
                keyCounter++;
            }

            list.add(new Object[]{testData});


        }

        return list.iterator();

    }


}