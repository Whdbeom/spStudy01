package com.sparta.java_02.domain.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequestTest;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequestTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void create() throws Exception {
    //given - 데이터세팅
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1L
        , purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    //when - 테스트케이스 실행 & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
    ;

  }

  @Test
  void 유저없음체크() throws Exception {
    //given - 데이터세팅
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(null,
        purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    //when - 테스트케이스 실행 & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error.errorCode").value("VALIDATE_ERROR"))
    ;
  }

  @Test
  void 수량체크() throws Exception {
    //given - 데이터세팅
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1L,
        purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    //when - 테스트케이스 실행 & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.error.errorCode").value("OUT_OF_STOCK_PRODUCT"))
    ;
  }
}