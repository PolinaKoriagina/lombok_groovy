import lombok.UserData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    void singleUser() {
        // @formatter:off
        given()
                .spec(Specs.request)
                .when()
                .get("/users/2")
                .then()
                .spec(Specs.responseSpec)
                .log().body();
        // @formatter:on
    }

    @Test
    void listOfUsers() {
        given()
                .spec(Specs.request)
                .when()
                .get("users?page=2")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .body("total", equalTo(12));
    }

    @Test
    void checkUserNameWithLombock() {

        UserData data = given()
                .spec(Specs.request)
                .when()
                .get("/users/5")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .extract().as(UserData.class);

        assertEquals("Charles", data.getUser().getFirstName());
    }

    @Test
    void updateUser() {
        given()
                .spec(Specs.request)
                .body("{\"name\": \"morpheus\"," +
                        "\"job\": \"zion resident\"}")
                .when()
                .put("users?page=2")
                .then()
                .spec(Specs.responseSpec)
                .body("name", is("morpheus")
                        , "job", is("zion resident"));
    }

    @Test
    void deleteUser() {
        given()
                .spec(Specs.request)
                .when()
                .delete("users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void loginSuccessful() {
        given()
                .spec(Specs.request)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"cityslicka\"}")
                .when()
                .post("login")
                .then()
                .spec(Specs.responseSpec)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void checkEmailUsingGroovy() {
        // @formatter:off
        given()
                .spec(Specs.request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("charles.morris@reqres.in"));
        // @formatter:on
    }

    @Test
    void checkUserNameByGroovy() {

        given()
                .spec(Specs.request)
                .when()
                .get("/users/")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .body("data.findAll{it.first_name}.first_name.flatten().",
                        hasItem("Charles"));

    }
}
