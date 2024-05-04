package io.element36.cash36.ebics;

import static io.element36.cash36.ebics.TestTool.pp;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.element36.cash36.ebics.dto.StatementDTO;
import io.element36.cash36.ebics.dto.TransactionDTO;
import io.element36.cash36.ebics.service.EbicsStatementService;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * Reads daily statement of a bank, according to EBICS specification. 
 * Means that if you call Service once, the backend will mark 
 * the statements you received. Next time the service is called it 
 * will only get new data if there are new transactions. 
 */
public class EbicsStatementServiceTest {

  // the service to be tested
  @Autowired EbicsStatementService ebicsStatementService;

  @Test
  public void testReadBankStatement() throws Exception {
    // get all non-fetched bank statements, print how many we found
    List<StatementDTO> statement = ebicsStatementService.getBankStatement();
    pp("no. statements: ", statement.size());
    
    assertThat(statement.size()).isBetween(1, 3);
    boolean reachedIn = false;
    boolean reachedOut = false;
    // go into each statement report
    for (StatementDTO account : statement) {
      // print the information and check if there 
      // are incoming and outgoing transactions, otherwise fail. 
      pp(
          "account: ",
          account.getBalanceCL(),
          account.getBalanceCLCurrency(),
          account.getBookingDate());

      for (TransactionDTO in : account.getIncomingTransactions()) {
        pp(
            "in: ",
            in.getAmount(),
            in.getCurrency(),
            in.getAddrLine(),
            in.getIban(),
            in.getReference());
        reachedIn = true;
      }
      for (TransactionDTO out : account.getOutgoingTransactions()) {
        pp(
            "outgoing: ",
            out.getAmount(),
            out.getCurrency(),
            out.getAddrLine(),
            out.getIban(),
            out.getReference());
        reachedOut = true;
      }
    }

    // check for in and outgoing transactions were found
    assertThat(reachedIn).isTrue();
    assertThat(reachedOut).isTrue();

    // test content (fields) of single transactions
    TestTool.testProxyStatements(statement);

  }
}
