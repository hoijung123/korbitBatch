package korbit.tran.service;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import junit.framework.TestCase;
import korbit.tran.vo.OrderVO;

public class CallAPIServiceTest extends TestCase {
	private CallAPIService api = new CallAPIService();
	@Test
	public final void testCallApi() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCallGetApi() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOauth() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAccessToken() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRefreshToken() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersBuy() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersBuyMarket() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersBuyLimit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersSell() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersSellMarket() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersSellLimit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetInvoiceToken() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBalances() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetOrdersOpen() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testOrdersCancel() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetOrders() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetOrdersComplete() throws ParseException {
		List<OrderVO> list = api.getOrdersComplete("eth_krw");
		System.out.println(list.size());
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTicker() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTransactionsStringString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTransactionsString() {
		fail("Not yet implemented"); // TODO
	}

}
