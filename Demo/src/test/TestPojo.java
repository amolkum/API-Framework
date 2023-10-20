import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CommonFunctions;
import utils.Log;
import utils.PojoClass;
import utils.Reporter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TestPojo extends Reporter {

    ExtentTest addStoreOrderTest = report.startTest("Update Store Order");
    RequestLoggingFilter requestLoggingFilter;
    ResponseLoggingFilter responseLoggingFilter;
    public PrintStream log ;

    @Test
    public void test_addStoreOrder() throws IOException {

        // final Logger logger = LogManager.getLogger(SampleTest.class.getName());
        //DOMConfigurator.configure(System.getProperty("user.dir")+"\\src\\test\\log4j.xml");
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging5.txt")),true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        PojoClass pojo = new PojoClass();

        pojo.setId("1");
        pojo.setUserName("amolk");
        pojo.setFirstName("Amol");
        pojo.setLastName("Kumbhare");
        pojo.setEmail("abc@gmail.com");
        pojo.setPassword("123");
        pojo.setPhoneNumber("1234567890");
        pojo.setExpectedResponse("200");
        pojo.setUserStatus("1");

        Response res = given()
                .contentType("application/json").filters(requestLoggingFilter,responseLoggingFilter)
                .body(pojo)
                .when()
                .put("https://petstore.swagger.io/v2/user/amol")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();
        Log.info("Extracted the response");
        int getStatusCode1 = res.getStatusCode();
        System.out.println("Status code is :" + getStatusCode1);
        try {
            CommonFunctions.assertStatusCode(res.getStatusCode(), pojo.getExpectedResponse(), "Status Code Check", addStoreOrderTest);
            //CommonFunctions.assertValue(name,pojo.getFirstName(),  "First Name Check", addStoreOrderTest);
        } catch (AssertionError e) {
            addStoreOrderTest.log(LogStatus.INFO, res.body().toString());
            // e.printStackTrace();
            Assert.fail();
        }

    }
}
