package korbit.tran.service;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import junit.framework.TestCase;
import korbit.tran.service.CallAPIService;
import korbit.tran.vo.OrderVO;

public class CallAPIServiceTest extends TestCase {
	private CallAPIService api = new CallAPIService();
	@Test
	public final void testCallApi() {
	}

	@Test
	public final void testCallGetApi() {
	}

	@Test
	public final void testOauth() {
	}

	@Test
	public final void testAccessToken() {
	}

	@Test
	public final void testRefreshToken() {
	}

	@Test
	public final void testOrdersBuy() {
	}

	@Test
	public final void testOrdersBuyMarket() {
	}

	@Test
	public final void testOrdersBuyLimit() {
	}

	@Test
	public final void testOrdersSell() {
	}

	@Test
	public final void testOrdersSellMarket() {
	}

	@Test
	public final void testOrdersSellLimit() {
	}

	@Test
	public final void testGetInvoiceToken() {
	}

	@Test
	public final void testGetBalances() {
	}

	@Test
	public final void testGetOrdersOpen() {
	}

	@Test
	public final void testOrdersCancel() {
	}

	@Test
	public final void testGetOrders() {
	}

	@Test
	public final void testGetOrdersComplete() throws ParseException {
		List<OrderVO> list = api.getOrdersComplete("eth_krw");
		System.out.println(list.size());
	}

	@Test
	public final void testGetTicker() {
	}

	@Test
	public final void testGetTransactionsStringString() {
	}

	@Test
	public final void testGetTransactionsString() {
	}

}
