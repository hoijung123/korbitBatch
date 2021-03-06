package korbit.tran.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import korbit.tran.dao.OrdersBuyDAO;
import korbit.tran.dao.OrdersSellDAO;
import korbit.tran.dao.TickerDAO;
import korbit.tran.service.CallAPIService;
import korbit.tran.util.Constants;
import korbit.tran.vo.AccessTokenVO;
import korbit.tran.vo.OrdersBuyVO;
import korbit.tran.vo.OrdersSellVO;
import korbit.tran.vo.TickerDtlVO;
import korbit.tran.vo.TickerVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Inject
	private OrdersBuyDAO ordersBuyDao;
	@Inject
	private OrdersSellDAO ordersSellDao;
	
	@Inject
	private TickerDAO tickerDAO;	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	public String getToken(Locale locale, Model model) {		
		CallAPIService api = new CallAPIService();
		Constants.ACCESS_TOKEN_VO = api.accessToken();
		Constants.ACCESS_TOKEN_VO.setPub_time(new Date());
		
		model.addAttribute("ACCESS_TOKEN", Constants.ACCESS_TOKEN_VO.getAccess_token() );
		model.addAttribute("Pub_time", Constants.ACCESS_TOKEN_VO.getPub_time() );
		
		return "tran/getToken";
	}	
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/listOrdersOpen", method = RequestMethod.GET)
	public String listOrdersOpen(@RequestParam Map<String, String> params, Model model) throws ParseException {		
		String sCurrency_pair = "";
		sCurrency_pair = params.get("currency_pair");
		
		if (StringUtils.isEmpty(sCurrency_pair)) sCurrency_pair= Constants.ETH_KRW;
		
		CallAPIService api = new CallAPIService();
		model.addAttribute("ordersOpenList", api.getOrdersOpen(sCurrency_pair));
		
		model.addAttribute("currency_pair", sCurrency_pair);
		
		return "tran/listOrdersOpen";
	}	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/getBalances", method = RequestMethod.GET)
	public String getBalances(@RequestParam Map<String, String> params, Model model) throws ParseException {	
		
		CallAPIService api = new CallAPIService();
		model.addAttribute("balances", api.getBalances());
		
		model.addAttribute("ethTicker", api.getTicker(Constants.ETH_KRW));
		model.addAttribute("etcTicker", api.getTicker(Constants.ETC_KRW));
		
		return "tran/getBalances";
	}	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/listTransactions", method = RequestMethod.GET)
	public String listTransactions(@RequestParam Map<String, String> params, Model model) throws ParseException {		
		String sCurrency_pair = "";
		sCurrency_pair = params.get("currency_pair");
		
		if (StringUtils.isEmpty(sCurrency_pair)) sCurrency_pair= Constants.ETH_KRW;
		
		CallAPIService api = new CallAPIService();
		model.addAttribute("transactionList", api.getTransactions(sCurrency_pair));
		
		model.addAttribute("currency_pair", sCurrency_pair);
		
		return "tran/listTransactions";
	}	
	
	@RequestMapping(value = "/listOrdersBuy", method = RequestMethod.GET)
	public String listOrdersBuy(@RequestParam Map<String, String> params, Model model) throws ParseException {		
		String sCurrency_pair = "";
		sCurrency_pair = params.get("currency_pair");
		
		if (StringUtils.isEmpty(sCurrency_pair)) sCurrency_pair= Constants.ETH_KRW;
		
		OrdersBuyVO vo = new OrdersBuyVO();
		vo.setCurrency_pair(sCurrency_pair);
		model.addAttribute("ordersBuyList", ordersBuyDao.getOrdersBuyList(vo));
		
		model.addAttribute("currency_pair", sCurrency_pair);
		
		return "tran/listOrdersBuy";
	}	
	
	@RequestMapping(value = "/listOrdersSell", method = RequestMethod.GET)
	public String listOrdersSell(@RequestParam Map<String, String> params, Model model) throws ParseException {		
		String sCurrency_pair = "";
		sCurrency_pair = params.get("currency_pair");
		
		if (StringUtils.isEmpty(sCurrency_pair)) sCurrency_pair= Constants.ETH_KRW;
		
		OrdersSellVO vo = new OrdersSellVO();
		vo.setCurrency_pair(sCurrency_pair);
		model.addAttribute("ordersSellList", ordersSellDao.getOrdersSellList(vo));
		
		model.addAttribute("currency_pair", sCurrency_pair);
		
		return "tran/listOrdersSell";
	}
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/ordersCancel", method = RequestMethod.GET)
	public String ordersCancel(@RequestParam Map<String, String> params, Model model) throws ParseException {		
		String sCurrency_pair = "";
		sCurrency_pair = params.get("currency_pair");
		
		String id = params.get("id");
		
		if (StringUtils.isEmpty(sCurrency_pair)) sCurrency_pair= Constants.ETH_KRW;
		
		CallAPIService api = new CallAPIService();
		api.ordersCancel(Constants.ACCESS_TOKEN_VO.getAccess_token() , sCurrency_pair, id);
		
		return "redirect:listOrdersOpen?currency_pair=" + sCurrency_pair;
	}
	
	@RequestMapping(value = "/jsonTicker", method = RequestMethod.GET)
	public @ResponseBody TickerDtlVO jsonTicker(@RequestParam Map<String, String> params) {
		// VO객체에 SET한후 vo객체자체를 return
		String currency = params.get("currency");
		if (StringUtils.isEmpty(currency)) {
			currency = Constants.ETH_KRW;
		}
		TickerDtlVO vo = new TickerDtlVO();
		vo.setCurrency_pair(currency);
		TickerDtlVO lastestVo = tickerDAO.getLastestTicker(vo);
		return lastestVo;
	}
	
	@RequestMapping(value = "/jsonTickerList", method = RequestMethod.GET)
	public @ResponseBody List<TickerDtlVO> jsonTickerList(@RequestParam Map<String, String> params) {
		// VO객체에 SET한후 vo객체자체를 return
		String currency = params.get("currency");
		if (StringUtils.isEmpty(currency)) {
			currency = Constants.ETH_KRW;
		}
		TickerDtlVO vo = new TickerDtlVO();
		vo.setCurrency_pair(currency);
		List<TickerDtlVO> list = tickerDAO.getList(vo);
		return list;
	}	
	
}
