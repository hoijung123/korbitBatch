package korbit.tran.batch;

import java.util.List;

import javax.inject.Inject;

import org.apache.http.NameValuePair;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import korbit.tran.dao.OrderDAO;
import korbit.tran.dao.OrdersBuyDAO;
import korbit.tran.dao.OrdersSellDAO;
import korbit.tran.dao.TranConfigDAO;
import korbit.tran.service.CallAPIService;
import korbit.tran.util.Constants;
import korbit.tran.util.SendMail;
import korbit.tran.vo.BalanceVO;
import korbit.tran.vo.OpenOrderVO;
import korbit.tran.vo.OrderRetVO;
import korbit.tran.vo.OrderVO;
import korbit.tran.vo.OrdersBuyVO;
import korbit.tran.vo.OrdersSellVO;
import korbit.tran.vo.TickerVO;
import korbit.tran.vo.TranConfigVO;
import korbit.tran.vo.TransactionVO;

public class TranCompleteChkProcessor implements ItemProcessor<String, String> {

	private static final Logger logger = LoggerFactory.getLogger(TranCompleteChkProcessor.class);
	private static final int SLEEP_TIME = 1000;

	@Inject
	private OrderDAO ordersDao;

	@Override
	public String process(String item) throws Exception {
		this.completeChk(Constants.ETH_KRW);
		Thread.sleep(SLEEP_TIME);
		this.completeChk(Constants.ETC_KRW);
		return item;
	}

	public String completeChk(String sCurrency_pair) throws ParseException {
		SendMail sendMail = new SendMail();
		CallAPIService api = new CallAPIService();

		List<OrderVO> list = api.getOrdersComplete(sCurrency_pair);

		for (OrderVO sub : list) {
			if (null == ordersDao.get(sub)) {
				ordersDao.register(sub);
				sendMail.sendMail("Korbit Sell Complete", sCurrency_pair + "/" + " " + sub.getSide() + " " + "/" + " Unit:" + sub.getFilled_amount() + "/" + " Price:" + sub.getPrice());
			}
		}		 
		return "item";
	}
}