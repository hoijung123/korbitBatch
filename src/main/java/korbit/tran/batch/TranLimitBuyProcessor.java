package korbit.tran.batch;

import java.util.List;

import javax.inject.Inject;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import korbit.tran.dao.OrdersBuyDAO;
import korbit.tran.dao.OrdersSellDAO;
import korbit.tran.dao.TranConfigDAO;
import korbit.tran.service.CallAPIService;
import korbit.tran.util.Constants;
import korbit.tran.util.SendMail;
import korbit.tran.vo.BalanceVO;
import korbit.tran.vo.OrderRetVO;
import korbit.tran.vo.OrdersBuyVO;
import korbit.tran.vo.OrdersSellVO;
import korbit.tran.vo.TranConfigVO;

public class TranLimitBuyProcessor implements ItemProcessor<String, String> {

	private static final Logger logger = LoggerFactory.getLogger(TranLimitBuyProcessor.class);
	private static final int SLEEP_TIME = 2000;

	@Inject
	private TranConfigDAO tranConfigDAO;
	@Inject
	private OrdersBuyDAO ordersBuyDao;
	@Inject
	private OrdersSellDAO ordersSellDao;

	@Override
	public String process(String item) throws Exception {
		this.tranCoin(Constants.ETH_KRW);
		Thread.sleep(SLEEP_TIME);
		this.tranCoin(Constants.ETC_KRW);
		return item;
	}

	public String tranCoin(String sCurrency_pair) {
		SendMail sendMail = new SendMail();
		CallAPIService api = new CallAPIService();

		OrdersBuyVO ordersBuyVO = new OrdersBuyVO();
		ordersBuyVO.setCurrency_pair(sCurrency_pair);
		List<OrdersBuyVO> listOrdersBuyVo = ordersBuyDao.getOrdersBuyList(ordersBuyVO);

		TranConfigVO vo = new TranConfigVO();
		vo.setCurrency(sCurrency_pair);
		vo.setTran_type("B");
		TranConfigVO tranConfigBuyVO = tranConfigDAO.getTranConfig(vo);
		String sTranBuyYn = tranConfigBuyVO.getTran_yn();

		BalanceVO balanceVO = null;

		float currency_balance = 0;
		try {
			balanceVO = api.getBalances();
			currency_balance = api.getBalances().getKrw().getAvailable();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		boolean isBuy = false;
		try {

			for (OrdersBuyVO sub : listOrdersBuyVo) {
				if ("N".equals(sTranBuyYn)) {
					continue;
				}
				
				if (isBuy)
				{
					continue;
				}

				if (currency_balance < (sub.getCoin_amount() * sub.getPrice()) ) {
					continue;
				}
				
				if (!Constants.SUCCESS.equals(sub.getStatus())) {
					OrdersBuyVO buyVO = new OrdersBuyVO();
					buyVO.setCurrency_pair(sCurrency_pair);
					buyVO.setCoin_amount(sub.getCoin_amount());
					buyVO.setPrice(sub.getPrice());
					OrderRetVO ret = api.ordersBuyLimit(buyVO);
					if (null != ret) {
						OrdersBuyVO buyVOUpt = new OrdersBuyVO();
						buyVOUpt.setCurrency_pair(sub.getCurrency_pair());
						buyVOUpt.setBuy_seq(sub.getBuy_seq());
						buyVOUpt.setOrder_id(ret.getOrderId());
						buyVOUpt.setStatus(ret.getStatus());
						ordersBuyDao.updateOrdersBuy(buyVOUpt);
					}
					if (Constants.SUCCESS.equals(ret.getStatus())) {
						isBuy = true; 
						OrdersSellVO sellVO = new OrdersSellVO();
						sellVO.setCurrency_pair(sCurrency_pair);
						sellVO.setSell_seq(sub.getBuy_seq());
						sellVO.setStatus("N");
						ordersSellDao.updateOrdersSell(sellVO);
						sendMail.sendMail("Korbit Buy", sCurrency_pair + "/" + " Buy " + "/" + " Unit:"
								+ sub.getCoin_amount() + "/" + " Price:" + sub.getPrice());
					} else {
						System.out.println("Buy Fail ==>" + ret.getStatus());
					}
				} else {
					System.out.println("Tran is Not Setting");
				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

		logger.info("tranCoin --> " + sCurrency_pair);
		return "item";
	}

}