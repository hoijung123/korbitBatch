package korbit.tran.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
//import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import korbit.tran.batch.TranProcessor;
import korbit.tran.dao.ConfigDAO;
import korbit.tran.dao.TranConfigDAO;
import korbit.tran.util.Constants;
import korbit.tran.util.SendMail;
import korbit.tran.vo.AccessTokenVO;
import korbit.tran.vo.BalanceVO;
import korbit.tran.vo.ConfigVO;
import korbit.tran.vo.OpenOrderVO;
import korbit.tran.vo.OrderRetVO;
import korbit.tran.vo.OrdersBuyVO;
import korbit.tran.vo.OrdersSellVO;
import korbit.tran.vo.TickerVO;

public class CallAPIService {

	private static final Logger logger = LoggerFactory.getLogger(CallAPIService.class);

	public String callApi(ArrayList postParameters, String path, String parse) {
		SendMail sendMail = new SendMail();
		String result = "";
		HttpResponse response = null;
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(path);
			request.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

			response = httpClient.execute(request);
			System.out.println(response.toString());
			result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
			ObjectMapper om = new ObjectMapper();
			JsonNode json = om.readTree(result);

			httpClient.close();

			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			sendMail.sendMail("apicall - Error : ", "response => " + response.toString() + " " + e.getMessage());
			return null;
		}
	}

	public String callGetApi(String path, ArrayList<NameValuePair> params) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet();

		NameValuePair pairToken = params.stream().filter(player -> player.getName().contains("access_token"))
				.findFirst().orElse(null);

		if (null != pairToken) {
			params.remove(pairToken);
		}

		int i = 0;
		String result = "";
		for (NameValuePair pair : params) {
			if (0 == i) {
				path += "?" + pair.getName() + "=" + pair.getValue();
			} else {
				path += "&" + pair.getName() + "=" + pair.getValue();
			}
			i++;
		}

		request = new HttpGet(path);
		if (null != pairToken) {
			request.addHeader("Authorization", "Bearer " + pairToken.getValue());
		}

		HttpResponse response;
		try {
			response = httpClient.execute(request);
			System.out.println(response.toString());
			result = EntityUtils.toString(response.getEntity());
			// System.out.println("Result=> " + result);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	/* 사용자인증 */
	public String oauth() {
		String accessToken = "";

		// logger.debug("Time => " + (new Date().getTime() -
		// Constants.ACCESS_TOKEN_VO.getPub_time().getTime()) / 1);
		if (null == Constants.ACCESS_TOKEN_VO) {
			AccessTokenVO accessTokenVO = this.accessToken();

			Constants.ACCESS_TOKEN_VO = new AccessTokenVO();
			BeanUtils.copyProperties(accessTokenVO, Constants.ACCESS_TOKEN_VO);
			Constants.ACCESS_TOKEN_VO.setPub_time(new Date());
		} else if (null == Constants.ACCESS_TOKEN_VO.getPub_time()) {
			AccessTokenVO accessTokenVO = this.accessToken();
			Constants.ACCESS_TOKEN_VO = new AccessTokenVO();
			BeanUtils.copyProperties(accessTokenVO, Constants.ACCESS_TOKEN_VO);
			Constants.ACCESS_TOKEN_VO.setPub_time(new Date());
		} else if ((new Date().getTime() - Constants.ACCESS_TOKEN_VO.getPub_time().getTime()) / 60000 > 30) {
			AccessTokenVO accessTokenVO = this.accessToken();
			Constants.ACCESS_TOKEN_VO = new AccessTokenVO();
			BeanUtils.copyProperties(accessTokenVO, Constants.ACCESS_TOKEN_VO);
		}
		/*
		 * else if (1000 > new
		 * Integer(Constants.ACCESS_TOKEN_VO.getExpires_in()).intValue()) {
		 * AccessTokenVO accessTokenVO = this.accessToken(); Constants.ACCESS_TOKEN_VO =
		 * new AccessTokenVO(); BeanUtils.copyProperties(accessTokenVO,
		 * Constants.ACCESS_TOKEN_VO); }
		 */

		accessToken = Constants.ACCESS_TOKEN_VO.getAccess_token();
		// logger.debug("Expires_in :" + Constants.ACCESS_TOKEN_VO.getExpires_in());
		// logger.debug("access token :" + accessToken);

		return accessToken;
	}

	/* 사용자인증 */
	public AccessTokenVO accessToken() {

		ArrayList<NameValuePair> oauthParameters = new ArrayList<NameValuePair>();
		oauthParameters.add(new BasicNameValuePair("client_id", Constants.clientID));
		oauthParameters.add(new BasicNameValuePair("client_secret", Constants.clientSecret));
		oauthParameters.add(new BasicNameValuePair("username", Constants.username));
		oauthParameters.add(new BasicNameValuePair("password", Constants.password));
		oauthParameters.add(new BasicNameValuePair("grant_type", "password"));

		String json = callApi(oauthParameters, Constants.oauthPath, "access_token");

		ObjectMapper mapper = new ObjectMapper();
		AccessTokenVO accessTokenVO = null;
		try {
			accessTokenVO = mapper.readValue(json, AccessTokenVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return accessTokenVO;
	}

	/* 토큰 갱신 */
	public AccessTokenVO refreshToken() {

		ArrayList<NameValuePair> oauthParameters = new ArrayList<NameValuePair>();
		oauthParameters.add(new BasicNameValuePair("client_id", Constants.clientID));
		oauthParameters.add(new BasicNameValuePair("client_secret", Constants.clientSecret));
		oauthParameters.add(new BasicNameValuePair("refresh_token", Constants.ACCESS_TOKEN_VO.getRefresh_token()));
		oauthParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));

		String json = callApi(oauthParameters, Constants.oauthPath, "access_token");

		ObjectMapper mapper = new ObjectMapper();
		AccessTokenVO accessTokenVO = null;
		try {
			accessTokenVO = mapper.readValue(json, AccessTokenVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return accessTokenVO;
	}

	/* Buy */
	public OrderRetVO ordersBuy(String currency_pair, String type, String amount, String price) {
		String accessToken = this.oauth();

		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("access_token", accessToken));
		parameters.add(new BasicNameValuePair("currency_pair", currency_pair));
		parameters.add(new BasicNameValuePair("type", type));
		if ("limit".equals(type)) {
			parameters.add(new BasicNameValuePair("price", price));
			parameters.add(new BasicNameValuePair("coin_amount", amount));
		} else if ("market".equals(type)) {
			parameters.add(new BasicNameValuePair("fiat_amount", amount));
		}
		parameters.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));

		String json = callApi(parameters, Constants.ordersBuyPath, "token");

		ObjectMapper mapper = new ObjectMapper();
		OrderRetVO orderRetVO = null;
		try {
			orderRetVO = mapper.readValue(json, OrderRetVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderRetVO;
	}

	/* Buy 시장가 */
	public OrderRetVO ordersBuyMarket(String currency_pair, String amount) {
		return this.ordersBuy(currency_pair, Constants.TRAN_TYPE_MARKET, amount, "");
	}

	/* Buy 지정가 */
	public OrderRetVO ordersBuyLimit(OrdersBuyVO buyVO) {
		return this.ordersBuy(buyVO.getCurrency_pair(), Constants.TRAN_TYPE_LIMIT, buyVO.getCoin_amount().toString(),
				buyVO.getPrice().toString());
	}

	/* Sell */
	public OrderRetVO ordersSell(String currency_pair, String type, String amount, String price) {
		String accessToken = this.oauth();
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("access_token", accessToken));
		parameters.add(new BasicNameValuePair("currency_pair", currency_pair));
		parameters.add(new BasicNameValuePair("type", type));
		parameters.add(new BasicNameValuePair("price", price));
		parameters.add(new BasicNameValuePair("coin_amount", amount));
		parameters.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));

		String json = callApi(parameters, Constants.ordersSellPath, "token");
		ObjectMapper mapper = new ObjectMapper();
		OrderRetVO orderRetVO = null;
		try {
			orderRetVO = mapper.readValue(json, OrderRetVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orderRetVO;
	}

	/* Sell 시장가 */
	public OrderRetVO ordersSellMarket(String currency_pair, Float amount) {
		return this.ordersSell(currency_pair, Constants.TRAN_TYPE_MARKET, amount.toString(), "");
	}

	/* Sell 지정가 */
	public OrderRetVO ordersSellLimit(OrdersSellVO sellVO) {
		return this.ordersSell(sellVO.getCurrency_pair(), Constants.TRAN_TYPE_LIMIT, sellVO.getCoin_amount().toString(),
				sellVO.getPrice().toString());
	}

	public String getInvoiceToken(String accessToken) {

		ArrayList<NameValuePair> invoiceParameters = new ArrayList<NameValuePair>();
		invoiceParameters.add(new BasicNameValuePair("access_token", accessToken));
		invoiceParameters.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
		invoiceParameters.add(new BasicNameValuePair("amount", "500"));
		invoiceParameters.add(new BasicNameValuePair("currency", Constants.currency));
		invoiceParameters.add(new BasicNameValuePair("payment_token", Constants.paymentToken));
		invoiceParameters.add(new BasicNameValuePair("reference", "can be any value under 50 letters"));

		String invoiceToken = callApi(invoiceParameters, Constants.invoicePath, "token");
		return invoiceToken;
	}

	/* 지갑조회 */
	public BalanceVO getBalances() throws ParseException {
		String accessToken = this.oauth();
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", accessToken));

		String json = callGetApi(Constants.balancePath, params);

		BalanceVO balanceVO = null;
		try {
			balanceVO = mapper.readValue(json, BalanceVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return balanceVO;
	}

	/* 미체결주문조회 */
	public List<OpenOrderVO> getOrdersOpen(String currency_pair) throws ParseException {
		String accessToken = this.oauth();
		List<String> listOrdersId = new ArrayList<String>();

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("currency_pair", currency_pair));
		params.add(new BasicNameValuePair("limit", "100"));

		String json = callGetApi(Constants.ordersOpenPath, params);

		ObjectMapper mapper = new ObjectMapper();
		List<OpenOrderVO> openOrders = null;
		try {
			openOrders = mapper.readValue(json, new TypeReference<List<OpenOrderVO>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return openOrders;
	}

	/* 주문취소 */
	public String ordersCancel(String accessToken, String currency_pair, String id) {

		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("access_token", accessToken));
		parameters.add(new BasicNameValuePair("currency_pair", currency_pair));
		parameters.add(new BasicNameValuePair("id", id));
		parameters.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));

		String invoiceToken = callApi(parameters, Constants.ordersCancelPath, "token");
		return invoiceToken;
	}

	/* 미체결주문조회 */
	public List<String> getOrders(String accessToken, String currency_pair, String status, String id)
			throws ParseException {
		List<String> listOrdersId = new ArrayList<String>();

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("currency_pair", currency_pair));

		JSONArray invoiceToken = null;
		String json = callGetApi(Constants.ordersOpenPath, params);

		ObjectMapper mapper = new ObjectMapper();
		List<OpenOrderVO> openOrders;
		try {
			openOrders = mapper.readValue(json, new TypeReference<List<OpenOrderVO>>() {
			});
			for (OpenOrderVO sub : openOrders) {
				listOrdersId.add(sub.getId());
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listOrdersId;
	}

	/* 최종 체결 가격 */
	public TickerVO getTicker(String currency_pair) throws ParseException {

		ObjectMapper mapper = new ObjectMapper();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("currency_pair", currency_pair));

		String json = callGetApi(Constants.tickerPath, params);

		TickerVO tickerVO = null;
		try {
			tickerVO = mapper.readValue(json, TickerVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tickerVO;
	}

	/* 체결주문조회 */
	public List<String> getTransactions(String currency_pair, String orderId) {
		String accessToken = this.oauth();

		List<String> listOrdersId = new ArrayList<String>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("currency_pair", currency_pair));
		params.add(new BasicNameValuePair("order_id", orderId));

		String json = callGetApi(Constants.transactionsPath, params);

		ObjectMapper mapper = new ObjectMapper();
		List<OpenOrderVO> openOrders;
		try {
			openOrders = mapper.readValue(json, new TypeReference<List<OpenOrderVO>>() {
			});
			for (OpenOrderVO sub : openOrders) {
				listOrdersId.add(sub.getId());
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listOrdersId;
	}
}