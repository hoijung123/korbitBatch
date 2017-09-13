package korbit.tran.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import korbit.tran.service.CallAPIService;
import korbit.tran.util.Constants;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
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
	
}
