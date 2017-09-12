package korbit.tran.batch;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import korbit.tran.dao.TranConfigDAO;
import korbit.tran.service.CallAPIService;
import korbit.tran.util.Constants;
import korbit.tran.util.SendMail;
import korbit.tran.vo.OrderRetVO;
import korbit.tran.vo.TickerVO;
import korbit.tran.vo.TranConfigVO;

public class AccessTokenProcessor implements ItemProcessor<String, String> {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenProcessor.class);

	@Override
	public String process(String item) throws Exception {
		CallAPIService api = new CallAPIService();
		api.oauth();
		return item;
	}

}