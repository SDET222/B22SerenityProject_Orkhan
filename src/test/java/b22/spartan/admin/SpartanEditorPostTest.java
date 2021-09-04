package b22.spartan.admin;

import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import utilities.SpartanNewBase;
import utilities.SpartanUtil;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.Matchers.*;

import java.util.*;


@SerenityTest
public class SpartanEditorPostTest extends SpartanNewBase {

    @Test
    public void postSpartanAsEditor() {
        Map<String, Object> bodyMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("bodyMap = " + bodyMap);

        given()
                .auth().basic("editor", "editor")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .log().body()
                .when()
                .post("/spartans")
                .prettyPrint();

        Ensure.that("Status code is", vRes -> vRes.statusCode(201));
        Ensure.that("Content type is ContentType.JSON", vRes -> vRes.contentType(ContentType.JSON));
        Ensure.that("Success message", vRes -> vRes.body("success", is("A Spartan is Born!")));
        Ensure.that("ID is not null", v -> v.body("data.id", notNullValue()));
        Ensure.that("Name is correct", v -> v.body("data.name", is(bodyMap.get("name"))));
        Ensure.that("Gender is correct", v -> v.body("data.gender", is(bodyMap.get("gender"))));
        Ensure.that("Phone is correct", v -> v.body("data.phone", is(bodyMap.get("phone"))));
        String id = lastResponse().jsonPath().getString("data.id");
        Ensure.that("ends with ...id", v -> v.header("Location", endsWith(id)));

    }
        @ParameterizedTest (name = "New Spartan {index} - name: {0}")
        @CsvFileSource(resources = "/MOCK_DATA.csv",numLinesToSkip = 1)
        public void postSpartanWithCSV(String name,String gender,long phone){

            System.out.println("name = " + name);
            System.out.println("gender = " + gender);
            System.out.println("phone = " + phone);

            Map<String, Object> bodyMap = new LinkedHashMap<>();
            bodyMap.put("name",name);
            bodyMap.put("gender",gender);
            bodyMap.put("phone", phone);

            given()
                    .auth().basic("editor", "editor")
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(bodyMap)
                    .log().body()
                    .when()
                    .post("/spartans")
                    .prettyPrint();

            Ensure.that("Status code is", vRes -> vRes.statusCode(201));
            Ensure.that("Content type is ContentType.JSON", vRes -> vRes.contentType(ContentType.JSON));
            Ensure.that("Success message", vRes -> vRes.body("success", is("A Spartan is Born!")));
            Ensure.that("ID is not null", v -> v.body("data.id", notNullValue()));
            Ensure.that("Name is correct", v -> v.body("data.name", is(bodyMap.get("name"))));
            Ensure.that("Gender is correct", v -> v.body("data.gender", is(bodyMap.get("gender"))));
            Ensure.that("Phone is correct", v -> v.body("data.phone", is(bodyMap.get("phone"))));
            String id = lastResponse().jsonPath().getString("data.id");
            Ensure.that("ends with ...id", v -> v.header("Location", endsWith(id)));

        }




}
