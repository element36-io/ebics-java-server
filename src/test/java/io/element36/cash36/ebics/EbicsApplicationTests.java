package io.element36.cash36.ebics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EbicsApplicationTests {

  @Test
  public void contextLoads() {
    // Simple test if spring context are loading
    log.debug("Ebics spring context loaded");
  }
}
