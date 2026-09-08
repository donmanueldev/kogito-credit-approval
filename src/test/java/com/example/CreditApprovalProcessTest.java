package com.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KogitoApplication.class)
class CreditApprovalProcessTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void configureRestAssured() {
        RestAssured.port = port;
    }

    @Test
    void approvesEligibleApplicationAtTheDebtRatioBoundary() {
        evaluate(application(750, 2_500, 875, 10_000, false))
                .body("approvalStatus", equalTo("APROBADO"))
                .body("decisionReason", equalTo("APROBACION_AUTOMATICA"))
                .body("debtRatio", equalTo(0.35F));
    }

    @Test
    void sendsMidRangeScoreToManualReview() {
        evaluate(application(749, 2_500, 600, 10_000, false))
                .body("approvalStatus", equalTo("REVISION_MANUAL"))
                .body("decisionReason", equalTo("REVISION_REQUERIDA"));
    }

    @Test
    void sendsLowerScoreBoundaryToManualReview() {
        evaluate(application(650, 2_500, 600, 10_000, false))
                .body("approvalStatus", equalTo("REVISION_MANUAL"))
                .body("decisionReason", equalTo("REVISION_REQUERIDA"));
    }

    @Test
    void rejectsLowScore() {
        evaluate(application(649, 2_500, 600, 10_000, false))
                .body("approvalStatus", equalTo("RECHAZADO"))
                .body("decisionReason", equalTo("POLITICA_CREDITICIA_NO_CUMPLIDA"));
    }

    @Test
    void fraudTakesPriorityOverApproval() {
        evaluate(application(800, 2_500, 600, 10_000, true))
                .body("approvalStatus", equalTo("RECHAZADO"))
                .body("decisionReason", equalTo("FRAUDE_CONFIRMADO"));
    }

    @Test
    void fraudTakesPriorityEvenWhenTheRestOfTheInputIsInvalid() {
        evaluate(application("", -1, 0, 0, 0, true))
                .body("approvalStatus", equalTo("RECHAZADO"))
                .body("decisionReason", equalTo("FRAUDE_CONFIRMADO"));
    }

    @Test
    void highScoreWithUnfavorableDebtRatioRequiresReview() {
        evaluate(application(750, 2_500, 876, 10_000, false))
                .body("approvalStatus", equalTo("REVISION_MANUAL"));
    }

    @Test
    void zeroIncomeIsRejectedAsInvalidInput() {
        evaluate(application(800, 0, 0, 10_000, false))
                .body("approvalStatus", equalTo("RECHAZADO"))
                .body("decisionReason", equalTo("DATOS_INVALIDOS"))
                .body("debtRatio", equalTo(null));
    }

    @Test
    void incompleteApplicationIsRejectedAsInvalidInput() {
        evaluate(Map.of())
                .body("approvalStatus", equalTo("RECHAZADO"))
                .body("decisionReason", equalTo("DATOS_INVALIDOS"));
    }

    private io.restassured.response.ValidatableResponse evaluate(Map<String, Object> payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post("/creditApproval")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }

    private static Map<String, Object> application(int score, int income, int debt, int amount, boolean fraud) {
        return application("CUST-001", score, income, debt, amount, fraud);
    }

    private static Map<String, Object> application(String customerId, int score, int income, int debt, int amount, boolean fraud) {
        return Map.of(
                "customerId", customerId,
                "creditScore", score,
                "monthlyIncome", income,
                "monthlyDebt", debt,
                "requestedAmount", amount,
                "fraudConfirmed", fraud);
    }
}
