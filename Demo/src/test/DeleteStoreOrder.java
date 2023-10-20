import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DeleteStoreOrder extends Reporter {
    RequestLoggingFilter requestLoggingFilter;
    ResponseLoggingFilter responseLoggingFilter;
    public PrintStream log;
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

    @Test(dependsOnMethods = "test_getStoreOrder", dataProvider = "deleteStoreOrder")
    public void test_deleteStoreOrder(HashMap<String, String> map) throws IOException {
        /* Deletes a Store order */

        ExtentTest deleteStoreOrderTest = report.startTest("deleteStoreOrder");
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging3.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        Object id = map.get("id");
        Response res= PetStoreApi.deleteStoreOrder(id);
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
