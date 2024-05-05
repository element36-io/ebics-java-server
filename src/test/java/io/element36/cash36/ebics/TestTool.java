package io.element36.cash36.ebics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.element36.cash36.ebics.dto.StatementDTO;
import io.element36.cash36.ebics.dto.TransactionDTO;

/**
 * Some test tools and test data to generate payments and test methods to check
 * if the
 * content of the payments was captured properly by the backend.
 */
public class TestTool {

	/**
	 * pp is pretty print; converts an object to String, e.g. for logging and
	 * testing
	 */
	public static String pp(Object... objects) {
		String result = "  ";
		if (objects == null) {
			result = "..null";
		} else {
			for (Object o : objects)
				result += o + "; ";
		}

		System.out.println(result);
		return result;
	}

	/**
	 * Utility function to get a file as a String
	 */
	public static String readLineByLineJava8(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

	/**
	 * Tests statements of a bank acount using its DTO. Overall, this method tests
	 * various properties of
	 * the StatementDTO objects, including balance, IBAN, transaction details, and
	 * address lines.
	 * The assertions ensure that the properties match the expected values, helping
	 * to verify the
	 * correctness of the StatementDTO objects.
	 */
	public static void testProxyStatements(List<StatementDTO> statements) {
		// assertThat(statements.size()).isEqualTo(2);
		// assertThat(statements.size()).isEqualTo(2);

		StatementDTO statement = statements.get(0);

		// do various test on the data, start with header
		pp(statement);
		// check from account
		assertThat(statement.getBalanceCL()).isEqualTo(new BigDecimal("80097.2"));
		assertThat(statement.getIban()).isEqualTo("CH4308307000289537312");

		// pick first transaction
		TransactionDTO tx = statement.getOutgoingTransactions().get(0);
		assertThat(tx.getCurrency()).isEqualTo("CHF");
		assertThat(tx.getAmount()).isEqualTo(new BigDecimal("745.25"));
		assertThat(tx.getAddrLine().get(0))
				.isEqualTo("VISECA CARD SERVICES SA \nHagenholzstrasse 56 \nPostfach 7007 \n8050 Zuerich");

		// tx.getAddrLine().get(0) // VISECA CARD SERVICES S.A.

		// pick second transaction.
		statement = statements.get(1);
		assertThat(statement.getBalanceCL()).isEqualTo(new BigDecimal("110"));
		assertThat(statement.getIban()).isEqualTo("CH2108307000289537320"); // pegging account
		tx = statement.getIncomingTransactions().get(0);
		assertThat(tx.getCurrency()).isEqualTo("CHF");
		assertThat(tx.getAmount()).isEqualTo(new BigDecimal("100"));
		assertThat(tx.getAddrLine().get(0)).isEqualTo("element36 AG \nBahnmatt 25 \n6340 Baar");
	}

	/**
	 * Utility function to replace the .innerHTML of a tag
	 */
	public static String findAndReplaceTagContent(String tag, String replaceWith, String inputString) {
		Pattern p = Pattern.compile("<" + tag + ">(.+?)</" + tag + ">");
		Matcher matcher = p.matcher(inputString);
		return matcher.replaceAll("<" + tag + ">" + replaceWith + "</" + tag + ">");
	}

	// PAIN file is a payment instruction sent to the bank backend. One is an
	// ingoing, the other an
	// outgoing transaction. Both will be sent to the backend for processing.
	// We later look if this information is captured in bank statements.
	public static final String PAIN1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<Document xmlns=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd  pain.001.001.03.ch.02.xsd\">\n"
			+ "    <CstmrCdtTrfInitn>\n"
			+ "        <GrpHdr>\n"
			+ "            <MsgId>4711</MsgId>\n"
			+ "            <CreDtTm>2021-07-16T14:43:03</CreDtTm>\n"
			+ "            <NbOfTxs>1</NbOfTxs>\n"
			+ "            <CtrlSum>100</CtrlSum>\n"
			+ "            <InitgPty>\n"
			+ "                <Nm>element36 AG</Nm>\n"
			+ "            </InitgPty>\n"
			+ "        </GrpHdr>\n"
			+ "        <PmtInf>\n"
			+ "            <PmtInfId>abc</PmtInfId>\n"
			+ "            <PmtMtd>TRF</PmtMtd>\n"
			+ "            <BtchBookg>true</BtchBookg>\n"
			+ "            <PmtTpInf>\n"
			+ "                <SvcLvl>\n"
			+ "                    <Cd>SEPA</Cd>\n"
			+ "                </SvcLvl>\n"
			+ "            </PmtTpInf>\n"
			+ "            <ReqdExctnDt>2021-07-16</ReqdExctnDt>\n"
			+ "            <Dbtr>\n"
			+ "                <Nm>element36 AG</Nm>\n"
			+ "            </Dbtr>\n"
			+ "            <DbtrAcct>\n"
			+ "                <Id>\n"
			+ "                    <IBAN>BE71096123456769</IBAN>\n"
			+ "                </Id>\n"
			+ "            </DbtrAcct>\n"
			+ "            <DbtrAgt>\n"
			+ "                <FinInstnId>\n"
			+ "                    <BIC></BIC>\n"
			+ "                </FinInstnId>\n"
			+ "            </DbtrAgt>\n"
			+ "            <CdtTrfTxInf>\n"
			+ "                <PmtId>\n"
			+ "                    <InstrId>INSTRID-4711</InstrId>\n"
			+ "                    <EndToEndId>E2E-4711</EndToEndId>\n"
			+ "                </PmtId>\n"
			+ "                <Amt>\n"
			+ "                    <InstdAmt Ccy=\"EUR\">100</InstdAmt>\n"
			+ "                </Amt>\n"
			+ "                <Cdtr>\n"
			+ "                    <Nm>Test Person</Nm>\n"
			+ "                    <PstlAdr>\n"
			+ "                        <Ctry>DE</Ctry>\n"
			+ "                        <AdrLine>Rec Street Street-No.</AdrLine>\n"
			+ "                        <AdrLine>1000 TestCity</AdrLine>\n"
			+ "                    </PstlAdr>\n"
			+ "                </Cdtr>\n"
			+ "                <CdtrAcct>\n"
			+ "                    <Id>\n"
			+ "                        <IBAN>DE75512108001245126199</IBAN>\n"
			+ "                    </Id>\n"
			+ "                </CdtrAcct>\n"
			+ "                <RmtInf>\n"
			+ "                    <Ustrd>Test Purpose</Ustrd>\n"
			+ "                </RmtInf>\n"
			+ "            </CdtTrfTxInf>\n"
			+ "        </PmtInf>\n"
			+ "    </CstmrCdtTrfInitn>\n"
			+ "</Document>\n"
			+ "";

	// PAIN file is a payment instruction sent to the bank backend. We later look if
	// this information
	// is captured in bank statements.
	public static final String PAIN2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<Document xmlns=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd  pain.001.001.03.ch.02.xsd\">\n"
			+ "    <CstmrCdtTrfInitn>\n"
			+ "        <GrpHdr>\n"
			+ "            <MsgId>4711</MsgId>\n"
			+ "            <CreDtTm>2021-07-16T15:12:31</CreDtTm>\n"
			+ "            <NbOfTxs>1</NbOfTxs>\n"
			+ "            <CtrlSum>100</CtrlSum>\n"
			+ "            <InitgPty>\n"
			+ "                <Nm>element36 AG</Nm>\n"
			+ "            </InitgPty>\n"
			+ "        </GrpHdr>\n"
			+ "        <PmtInf>\n"
			+ "            <PmtInfId>abc</PmtInfId>\n"
			+ "            <PmtMtd>TRF</PmtMtd>\n"
			+ "            <BtchBookg>true</BtchBookg>\n"
			+ "            <PmtTpInf>\n"
			+ "                <SvcLvl>\n"
			+ "                    <Cd>SEPA</Cd>\n"
			+ "                </SvcLvl>\n"
			+ "            </PmtTpInf>\n"
			+ "            <ReqdExctnDt>2021-07-16</ReqdExctnDt>\n"
			+ "            <Dbtr>\n"
			+ "                <Nm>element36 AG</Nm>\n"
			+ "            </Dbtr>\n"
			+ "            <DbtrAcct>\n"
			+ "                <Id>\n"
			+ "                    <IBAN>BE71096123456769</IBAN>\n"
			+ "                </Id>\n"
			+ "            </DbtrAcct>\n"
			+ "            <DbtrAgt>\n"
			+ "                <FinInstnId>\n"
			+ "                    <BIC></BIC>\n"
			+ "                </FinInstnId>\n"
			+ "            </DbtrAgt>\n"
			+ "            <CdtTrfTxInf>\n"
			+ "                <PmtId>\n"
			+ "                    <InstrId>INSTRID-4711</InstrId>\n"
			+ "                    <EndToEndId>E2E-4711</EndToEndId>\n"
			+ "                </PmtId>\n"
			+ "                <Amt>\n"
			+ "                    <InstdAmt Ccy=\"EUR\">100</InstdAmt>\n"
			+ "                </Amt>\n"
			+ "                <Cdtr>\n"
			+ "                    <Nm>Test Person</Nm>\n"
			+ "                    <PstlAdr>\n"
			+ "                        <Ctry>DE</Ctry>\n"
			+ "                        <AdrLine>Rec Street Street-No.</AdrLine>\n"
			+ "                        <AdrLine>1000 TestCity</AdrLine>\n"
			+ "                    </PstlAdr>\n"
			+ "                </Cdtr>\n"
			+ "                <CdtrAcct>\n"
			+ "                    <Id>\n"
			+ "                        <IBAN>DE75512108001245126199</IBAN>\n"
			+ "                    </Id>\n"
			+ "                </CdtrAcct>\n"
			+ "                <RmtInf>\n"
			+ "                    <Ustrd>Test Purpose</Ustrd>\n"
			+ "                </RmtInf>\n"
			+ "            </CdtTrfTxInf>\n"
			+ "        </PmtInf>\n"
			+ "    </CstmrCdtTrfInitn>\n"
			+ "</Document>\n"
			+ "";
}
