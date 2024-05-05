package io.element36.cash36.ebics;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.element36.cash36.ebics.dto.PaymentStatusReportDTO;
import io.element36.cash36.ebics.service.EbicsPaymentStatusService;

@RunWith(SpringRunner.class)
@SpringBootTest
/** 
 * Call StatusReport (z01 document) from the banking backend. 
 */
public class EbicsPaymentStatusServiceTest {

  // the service to be tested
  @Autowired EbicsPaymentStatusService ebicsPaymentStatusService;

  @Test
  public void getStatus() {
    // Get Status of our payments, and print it. Its informative 
    // because we do not rely on status messages, but
    // for debugging purpuse is might be interesting to 
    // see the status of each transaction. 
    List<PaymentStatusReportDTO> transactions = ebicsPaymentStatusService.getStatusReport();
    for (PaymentStatusReportDTO tx : transactions) {
      // print message id
      System.out.println("msg-id: " + tx.getMsgId());
    }
  }
}
