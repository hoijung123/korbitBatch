package korbit.tran.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import korbit.tran.AbstractTest;
import korbit.tran.service.CallAPIService;
import korbit.tran.vo.OrderVO;

public class OrderDAOImplTest extends AbstractTest {

	@Inject
	private OrderDAOImpl dao;

	private CallAPIService api = new CallAPIService();

	@Test
	public final void testGetOrderVO() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTime() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRegister() throws ParseException {
		OrderVO vo = new OrderVO();
		List<OrderVO> list = api.getOrdersComplete("eth_krw");

		for (OrderVO sub : list) {
			if (null == dao.get(sub)) {
				dao.register(sub);
			}
		}
	}

	@Test
	public final void testGetK() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetList() {
		logger.info("" + dao.getList());
		fail("Not yet implemented"); // TODO
	}

}
