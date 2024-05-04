package io.element36.cash36.ebics;

import static io.element36.cash36.ebics.TestTool.pp;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.element36.cash36.ebics.strategy.GeneratePaymentIds;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * We generate Ids on the backend; lets test the algo.
 */
public class GeneratePaymentIdTest {

  // service to be tested
  @Autowired GeneratePaymentIds paymentIds;

  @Test
  public void testMsgId() throws Exception {
    // Create a new message Id; test the null/default behaviour
    String id = paymentIds.getMsgId(null, null);
    pp("tx-id" + id);
    assertThat(id).isNotNull();
    // compliant with Ebics standard
    assertThat(id.length()).isBetween(10, 250);
  }

  @Test
  public void testPmtInfId() throws Exception {
    // generate a payment id. 
    String id = paymentIds.getPmtInfId(null, null);
    pp("tx-id" + id);
    assertThat(id).isNotNull();
    // should be OK with ebics standard.
    assertThat(id.length()).isBetween(1, 250);
  }
}
