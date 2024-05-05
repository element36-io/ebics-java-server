package io.element36.cash36.ebics;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.element36.cash36.ebics.dto.TxResponse;
import io.element36.cash36.ebics.service.EbicsPaymentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EbicsPaymentServiceTests {

  // Service to be tested
  @Autowired EbicsPaymentService ebicsPaymentService;

  @Test
  /**
   * Test the payment call which triggers an OUTGOING payment (incoming done by simulatePayment below)
   */
  public void makePayment() throws Exception {
    //	    String makePayment(String msgId, String pmtInfId, String sourceIban, String sourceBic,
    // BigDecimal amount,
    //                String currency, String receipientIban, String receipientBankName, String
    // recipientBankPostAccount,
    //                String receipientName, String purpose, String ourReference, String
    // receipientStreet, String receipientStreetNr,
    //                String receipientZip, String receipientCity, String receipientCountry,
    //                String clearingSystemMemberId, boolean nationalPayment) throws Exception;
    // values taken from
    // https://www.iban.com/structure


    // execute a payment via the configured service, get the response. 
    TxResponse txResponse =
        ebicsPaymentService.makePayment(
            "4711",
            "abc",
            "BE71096123456769",
            "",
            new BigDecimal(100),
            "EUR",
            "DE75512108001245126199",
            "Testbank",
            "Test Person",
            "Test Purpose",
            "our Ref",
            "Rec Street",
            "Street-No.",
            "1000",
            "TestCity",
            "DE",
            "clearingSystemMemberId",
            false);

    System.out.println(txResponse);
    String fileName=txResponse.getEbicsDocumentPath().trim();
    String content=TestTool.readLineByLineJava8(fileName);
    //System.out.println("Payment File content:\n:"+content);

    // we replace timestamps to that we can compare the Ebics XML response.
    // Everything should be like we defined in MSG; but of course the timestamps
    // are different. So we replace them to make a String comparison

    content=TestTool.findAndReplaceTagContent("CreDtTm", "2021-07-16T14:43:03", content);
    content=TestTool.findAndReplaceTagContent("ReqdExctnDt", "2021-07-16", content);		
    org.assertj.core.api.Assertions.assertThat(content).isEqualTo(TestTool.PAIN1);
  }

  @Test
  /** 
   * Test the simulate Payment service call, which simulates in INCOMING payment. 
   */
  public void simulatePayment() throws Exception {
    //	    String makePayment(String msgId, String pmtInfId, String sourceIban, String sourceBic,
    // BigDecimal amount,
    //                String currency, String receipientIban, String receipientBankName, String
    // recipientBankPostAccount,
    //                String receipientName, String purpose, String ourReference, String
    // receipientStreet, String receipientStreetNr,
    //                String receipientZip, String receipientCity, String receipientCountry,
    //                String clearingSystemMemberId, boolean nationalPayment) throws Exception;
    // values taken from
    // https://www.iban.com/structure

    TxResponse txResponse =
        ebicsPaymentService.simulatePayment(
            "4711",
            "abc",
            "BE71096123456769",
            "",
            new BigDecimal(100),
            "EUR",
            "DE75512108001245126199",
            "Testbank",
            "Test Person",
            "Test Purpose",
            "our Ref",
            "Rec Street",
            "Street-No.",
            "1000",
            "TestCity",
            "DE",
            "clearingSystemMemberId",
            false);

    System.out.println(txResponse);
    String fileName=txResponse.getEbicsDocumentPath();
    String content=TestTool.readLineByLineJava8(fileName);
    System.out.println("Payment File content:\n:"+content);

    // we replace timestamps to that we can compare the Ebics XML response.
    // Everything should be like we defined in MSG; but of course the timestamps
    // are different. So we replace them to make a String comparison

    content=TestTool.findAndReplaceTagContent("CreDtTm", "2021-07-16T15:12:31", content);
    content=TestTool.findAndReplaceTagContent("ReqdExctnDt", "2021-07-16", content);
    org.assertj.core.api.Assertions.assertThat(content).isEqualTo(TestTool.PAIN2);
  }
}
