import java.io.*;

import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hamcrest.CoreMatchers;
import utils.CommonFunctions;
import utils.Reporter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

//import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.*;


import static io.restassured.RestAssured.given;
import static utils.Routes.GetStoreInventory;

public class StoreTests extends Reporter {
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

        Response res=PetStoreApi.getInventory();

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

    @Test(dataProvider = "addStoreOrder")
    public void test_addStoreOrder(HashMap<String, String> map) throws IOException {
        /* Adds a Store order */
        ExtentTest addStoreOrderTest = report.startTest("Add Store Order");
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        HashMap<String, Object> testData = new HashMap<>();
        Object id = map.get("id");
        Object petId = map.get("petId");
        Object quantity = map.get("quantity");
        Object shipDate = map.get("shipDate");
        Object status = map.get("status");
        Object complete = map.get("complete");

		testData.put("id", id);
        testData.put("petId", petId);
        testData.put("quantity", quantity);
        testData.put("shipDate", shipDate);
        testData.put("status", status);
        testData.put("complete", complete);
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging1.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        Response res=PetStoreApi.post(testData);

        /*Response res = given()
                .contentType("application/json").filters(requestLoggingFilter, responseLoggingFilter)
                .body(testData)
                .when()
                .post("https://petstore.swagger.io/v2/store/order")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();*/

        try {
            CommonFunctions.assertStatusCode(res.getStatusCode(), map.get("ExpectedResponse"), "Status Code Check", addStoreOrderTest);
            //CommonFunctions.assertValue(map.get("id"), res.jsonPath().getString("id"), "ID Check", addStoreOrderTest);
        } catch (AssertionError e) {
            addStoreOrderTest.log(LogStatus.INFO, res.body().toString());
            e.printStackTrace();
            Assert.fail();
        }
        report.endTest(addStoreOrderTest);
        report.flush();
    }

    @Test(dependsOnMethods = "test_addStoreOrder", dataProvider = "getStoreOrder")
    public void test_getStoreOrder(HashMap<String, String> map) throws IOException {
        ExtentTest getStoreOrderTest = report.startTest("getStoreOrder");
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging2.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        Object  id = map.get("id");
        //Response res=PetStoreApi.getStoreOrder(id);
        Response res = given()
                .contentType("application/json").filters(requestLoggingFilter, responseLoggingFilter)
                .pathParam("id", id)
                .when()
                .get("https://petstore.swagger.io/v2/store/order/{id}")
                .then()
                .extract().response();
        try {
            CommonFunctions.assertStatusCode(res.getStatusCode(), map.get("ExpectedResponse"), "Status Code Check", getStoreOrderTest);
			//CommonFunctions.assertValue(res.jsonPath().getString("id"), map.get("id"), "ID Check", getStoreOrderTest);
            //getStoreOrderTest.log(LogStatus.INFO, res.body().asString());
        } catch (AssertionError e) {
            getStoreOrderTest.log(LogStatus.INFO, res.body().toString());
            Assert.fail();
        }
    }
    @Test(dependsOnMethods = "test_getStoreOrder", dataProvider = "deleteStoreOrder")
    public void test_deleteStoreOrder(HashMap<String, String> map) throws IOException {
        /* Deletes a Store order */

        ExtentTest deleteStoreOrderTest = report.startTest("deleteStoreOrder");
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging3.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        Object id = map.get("id");
        Response res=PetStoreApi.deleteStoreOrder(id);
       /* Response res = given()
                .contentType("application/json").filters(requestLoggingFilter, responseLoggingFilter)
                .pathParam("id", id)
                .when()
                .delete("https://petstore.swagger.io/v2/store/order/{id}")
                .then()
                .log().all()
                .extract().response();*/
        try {
            CommonFunctions.assertStatusCode(res.getStatusCode(), map.get("ExpectedResponse"), "Status Code Check", deleteStoreOrderTest);
            //CommonFunctions.assertValue(res.jsonPath().getString("message"), map.get("id"), "ID Check", deleteStoreOrderTest);
            //deleteStoreOrderTest.log(LogStatus.INFO, res.body().asString());
        } catch (AssertionError e) {
            deleteStoreOrderTest.log(LogStatus.INFO, res.body().toString());
            Assert.fail();
        }
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

    @DataProvider(name = "addStoreOrder")
    public static Iterator<Object[]> csvReader() throws IOException {

        List<Object[]> list = new ArrayList<Object[]>();

        int rows = ExcelUtility.getRowCount(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet);
        int colCount = ExcelUtility.getCellCount(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, 1);
        int subtractRows = 1;
        String[] keys = new String[colCount];

        //Get keys for hash map
        for (int i = 0; i < colCount; i++) {
            keys[i] = ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, 0, i);
        }
        for (int i = 0; i < rows; i++) {
            if (ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, i, 1).toLowerCase().contains("no")) {
                subtractRows++;
            }
        }

        for (int row = 1; row < rows; row++) {

            if (ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, row, 1).toLowerCase().contains("no")) {
                continue;
            }

            Map<String, String> testData = new HashMap<String, String>();
            int keyCounter = 0;
            for (int col = 0; col < colCount; col++) {

                testData.put(keys[keyCounter], ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, row, col));
                keyCounter++;
            }

            list.add(new Object[]{testData});


        }


        return list.iterator();

    }


    @DataProvider(name = "getStoreOrder")
    public static Iterator<Object[]> getOrder() throws IOException {

        List<Object[]> list = new ArrayList<Object[]>();

        int rows = ExcelUtility.getRowCount(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet);
        int colCount = ExcelUtility.getCellCount(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, 1);
        int subtractRows = 1;
        String[] keys = new String[colCount];

        //Get keys for hash map
        for (int i = 0; i < colCount; i++) {
            keys[i] = ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, 0, i);
        }


        for (int i = 0; i < rows; i++) {
            if (ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, i, 1).toLowerCase().contains("no")) {
                subtractRows++;
            }
        }


        for (int row = 1; row < rows; row++) {

            if (ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, row, 1).toLowerCase().contains("no")) {
                continue;
            }

            Map<String, String> testData = new HashMap<String, String>();
            int keyCounter = 0;
            for (int col = 0; col < colCount; col++) {

                testData.put(keys[keyCounter], ExcelUtility.getCellData(Constants.AddStoreOrderData, Constants.AddStoreOrderDataSheet, row, col));
                keyCounter++;
            }

            list.add(new Object[]{testData});


        }


        return list.iterator();

    }


    @DataProvider(name = "deleteStoreOrder")
    public static Iterator<Object[]> deleteOrder() throws IOException {

        List<Object[]> list = new ArrayList<Object[]>();

        int rows = ExcelUtility.getRowCount(Constants.DeleteStoreOrderData, Constants.DeleteStoreOrderDataSheet);
        int colCount = ExcelUtility.getCellCount(Constants.DeleteStoreOrderData, Constants.DeleteStoreOrderDataSheet, 1);
        int subtractRows = 1;
        String[] keys = new String[colCount];

        //Get keys for hash map
        for (int i = 0; i < colCount; i++) {
            keys[i] = ExcelUtility.getCellData(Constants.DeleteStoreOrderData, Constants.DeleteStoreOrderDataSheet, 0, i);
        }


        for (int i = 0; i < rows; i++) {
            if (ExcelUtility.getCellData(Constants.DeleteStoreOrderData, Constants.DeleteStoreOrderDataSheet, i, 1).toLowerCase().contains("no")) {
                subtractRows++;
            }
        }


        for (int row = 1; row < rows; row++) {

            if (ExcelUtility.getCellData(Constants.DeleteStoreOrderData, Constants.DeleteStoreOrderDataSheet, row, 1).toLowerCase().contains("no")) {
                continue;
            }

            Map<String, String> testData = new HashMap<String, String>();
            int keyCounter = 0;
            for (int col = 0; col < colCount; col++) {

                testData.put(keys[keyCounter], ExcelUtility.getCellData(Constants.DeleteStoreOrderData, Constants.DeleteStoreOrderDataSheet, row, col));
                keyCounter++;
            }

            list.add(new Object[]{testData});


        }


        return list.iterator();

    }


}
