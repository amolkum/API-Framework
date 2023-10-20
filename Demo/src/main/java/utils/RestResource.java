package utils;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static utils.SpecBuilder.*;

public class RestResource {
    static RequestLoggingFilter requestLoggingFilter;
    static ResponseLoggingFilter responseLoggingFilter;
    public static PrintStream log;

    public static Response put(String path, Object requestPlaylist){
        return given(getRequestSpec()).
                body(requestPlaylist).
                //auth().oauth2(token).
                when().put(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response getMethod1(String path,Object pathParam) throws IOException {
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging1.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        return given(getRequestSpecA()).filters(requestLoggingFilter, responseLoggingFilter).pathParam("id",pathParam).
                when().get(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }
    public static Response postMethod(String path, Object requestPetStore) throws IOException {
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging1.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        return given(getRequestSpec()).filters(requestLoggingFilter, responseLoggingFilter).
                body(requestPetStore).
                when().post(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }
    public static Response get(String path) throws IOException {
        log = new PrintStream(Files.newOutputStream(Paths.get("test_logging.txt")), true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        return given(getRequestSpec()).contentType("application/json").filters(requestLoggingFilter, responseLoggingFilter).
               // auth().oauth2(token).
                when().get(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response deleteMethod(String path, Object requestPetList){
        return given(getRequestSpec()).pathParam("id",requestPetList).
               // auth().oauth2(token).
                //body(requestPetList).
                when().delete(path).
                then().spec(getResponseSpec()).
                extract().
                response();
    }
}
