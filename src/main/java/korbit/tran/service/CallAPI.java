package korbit.tran.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import korbit.tran.util.Constants;
import korbit.tran.vo.BalanceVO;
import korbit.tran.vo.OpenOrderVO;
import korbit.tran.vo.TickerVO;

public class CallAPI {

	// public static void main(String[] args) throws ParseException {
	// // TODO Auto-generated method stub
	// CallAPIService callAPIService = new CallAPIService();
	// String accessToken = callAPIService.oauth();
	// // callAPIService.getBalances(accessToken);
	// // callAPIService.ordersBuy(accessToken, "eth_krw", "limit", "0.01", "1000");
	// // callAPIService.ordersSell(accessToken, "eth_krw", "limit", "0.01",
	// "1000");
	// List<String> listOrdersId = callAPIService.getOrdersOpen(accessToken,
	// "eth_krw");
	// for (String s : listOrdersId) {
	// callAPIService.ordersCancel(accessToken, "eth_krw", s);
	// }
	// }

	public String callApi(ArrayList postParameters, String path, String parse) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost request = new HttpPost(path);
			request.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

			HttpResponse response = httpClient.execute(request);
			System.out.println(response.toString());
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
			ObjectMapper om = new ObjectMapper();
			JsonNode json = om.readTree(result);
			String token = json.path(parse).getValueAsText();
			httpClient.getConnectionManager().shutdown();

			return token;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String callGetApi(String path, ArrayList<NameValuePair> params) throws ParseException {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet();

			NameValuePair pairToken = params.stream().filter(player -> player.getName().contains("access_token"))
					.findFirst().orElse(null);

			if (null != pairToken) {
				params.remove(pairToken);
			}

			int i = 0;
			for (NameValuePair pair : params) {
				if (0 == i) {
					path += "?" + pair.getName() + "=" + pair.getValue();
				} else {
					path += "&" + pair.getName() + "=" + pair.getValue();
				}
				i++;
			}

			request = new HttpGet(path);
			request.addHeader("Authorization", "Bearer " + pairToken.getValue());

			HttpResponse response = httpClient.execute(request);
			System.out.println(response.toString());
			String result = EntityUtils.toString(response.getEntity());
			System.out.println("Result=> " + result);

			JSONParser jsonParser = new JSONParser();
			httpClient.getConnectionManager().shutdown();

			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* 사용자인증 */
	public String oauth() {
		String accessToken = "";

		ArrayList<NameValuePair> oauthParameters = new ArrayList<NameValuePair>();
		oauthParameters.add(new BasicNameValuePair("client_id", Constants.clientID));
		oauthParameters.add(new BasicNameValuePair("client_secret", Constants.clientSecret));
		oauthParameters.add(new BasicNameValuePair("username", Constants.username));
		oauthParameters.add(new BasicNameValuePair("password", Constants.password));
		oauthParameters.add(new BasicNameValuePair("grant_type", "password"));

		accessToken = callApi(oauthParameters, Constants.oauthPath, "access_token");
		System.out.println("access token :" + accessToken);

		return accessToken;
	}

	/* Buy */
	public String ordersBuy(String currency_pair, String type, String amount, String price) {
		String accessToken = this.oauth();

		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("access_token", accessToken));
		parameters.add(new BasicNameValuePair("currency_pair", currency_pair));
		parameters.add(new BasicNameValuePair("type", type));
		if ("limit".equals(type)) {
			parameters.add(new BasicNameValuePair("price", price));
			parameters.add(new BasicNameValuePair("coin_amount", amount));
		} else if ("market".equals(type)) {
			parameters.add(new BasicNameValuePair("flat_amount", amount));
		}
		parameters.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));

		String invoiceToken = callApi(parameters, Constants.ordersBuyPath, "token");
		return invoiceToken;
	}

	/* Buy 시장가 */
	public String ordersBuyMarket(String currency_pair, Float amount) {
		return this.ordersBuy(currency_pair, Constants.TRAN_TYPE_MARKET, amount.toString(), "");
	}

	/* Buy 지정가 */
	public String ordersBuyLimit(String currency_pair, String amount, String price) {
		return this.ordersBuy(currency_pair, Constants.TRAN_TYPE_LIMIT, amount, price);
	}

	/* Sell */
	public String ordersSell(String currency_pair, String type, String amount, String price) {
		String accessToken = this.oauth();
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("access_token", accessToken));
		parameters.add(new BasicNameValuePair("currency_pair", currency_pair));
		parameters.add(new BasicNameValuePair("type", type));
		parameters.add(new BasicNameValuePair("price", price));
		parameters.add(new BasicNameValuePair("coin_amount", amount));
		parameters.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));

		String invoiceToken = callApi(parameters, Constants.ordersSellPath, "token");
		return invoiceToken;
	}

	/* Sell 시장가 */
	public String ordersSellMarket(String currency_pair, Float amount) {
		return this.ordersSell(currency_pair, Constants.TRAN_TYPE_MARKET, amount.toString(), "");
	}

	/* Sell 지정가 */
	public String ordersSellLimit(String currency_pair, String amount, String price) {
		return this.ordersSell(currency_pair, Constants.TRAN_TYPE_LIMIT, amount, price);
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
		ArrayList<NameValuePair> invoiceParameters = new ArrayList<NameValuePair>();
		invoiceParameters.add(new BasicNameValuePair("access_token", accessToken));

		String json = callGetApi(Constants.balancePath, invoiceParameters);
		
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
	public List<String> getOrdersOpen(String accessToken, String currency_pair) throws ParseException {
		List<String> listOrdersId = new ArrayList<String>();

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("currency_pair", currency_pair));

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

}