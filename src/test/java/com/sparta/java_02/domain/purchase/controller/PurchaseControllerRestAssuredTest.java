package com.sparta.java_02.domain.purchase.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PurchaseControllerRestAssuredTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Test
  void 주문_성공() {
    // given
    String requestBody = """
        {
          "userId" : 1,
          "purchaseProducts" : [
            {
                "productId": 1,
                "quantity": 10
            }
          ]
        }
          """;
    // when & then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/purchases")
        .then().log().all()
        .statusCode(200)
        .body("result", IsEqual.equalTo(true));
  }

  @Test
  void 회원가입_모든필드입력() {
    String requestBody = """
        {
          "name" : "테스트2",
          "email" : "test2@test.com",
          "phoneNumber" : "01042335951",
          "password" : "테스트2"
        }
        """;
    // when & then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/users")
        .then().log().all()
        .statusCode(200)
        .body("result", IsEqual.equalTo(true));
  }


  @Test
  void 회원가입_필드누락() {
    String requestBody = """
        {
          "name" : "테스트3",
          "phoneNumber" : "01042335951",
          "password" : "테스트3"
        }
        """;
    // when & then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/users")
        .then().log().all()
        .statusCode(400)
        .body("error.errorCode", IsEqual.equalTo("VALIDATE_ERROR"));
  }

}
