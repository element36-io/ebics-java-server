package io.element36.cash36.ebics.service;

import java.io.IOException;
import java.util.List;

import io.element36.cash36.ebics.dto.StatementDTO;

public interface EbicsStatementService {
    
  enum TxType { CRDT, DBIT }
  List<StatementDTO> getBankStatement() throws IOException;
}
