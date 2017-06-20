package com.qingxin.client;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RestClient {
	private static final Logger logger = LogManager.getLogger(RestClient.class);
	private RestClient(){
	}
	
	public static void main(String[] args) {
		logger.info(RestClient.sendHelloRequest());
		logger.info("Become      friend:" + RestClient.sendCreateRequest());
		logger.info("Get  friend   list:" + RestClient.getFriendsRequest());
		logger.info("Get  common friend:" + RestClient.getCommonFriendsRequest());
		logger.info("Subscribe   friend:" + RestClient.sendSubscribeRequest());
		logger.info("Block       friend:" + RestClient.sendBlockRequest());
		logger.info("Get special friend:" + RestClient.getRecipients());
	}

	private static String sendHelloRequest() {
		return sendGetRequest("sayHello");
	}

	private static String sendCreateRequest(){
		String jsonstr = "{friends:['andy@example.com','john@example.com']}";
		JSONObject obj;
		try {
			obj = new JSONObject(jsonstr);
			return sendPostRequest(obj,"create");
		} catch (JSONException e) {
			logger.error(e);
		}
		return null;
	}

	private static String getFriendsRequest() {
		String jsonstr = "{email:'andy@example.com'}";
		JSONObject obj;
		try {
			obj = new JSONObject(jsonstr);
			return sendPostRequest(obj,"getFriends");
		} catch (JSONException e) {
			logger.error(e);
		}
		return null;
	}
	
	private static String getCommonFriendsRequest() {
		String jsonstr = "{friends:['andy@example.com','john@example.com']}";
		JSONObject obj;
		try {
			obj = new JSONObject(jsonstr);
			return sendPostRequest(obj,"getCommonFriends");
		} catch (JSONException e) {
			logger.error(e);
		}
		return null;
	}
	
	private static String sendSubscribeRequest() {
		String jsonstr = "{'requestor': 'lisa@example.com','target': 'john@example.com'}";
		JSONObject obj;
		try {
			obj = new JSONObject(jsonstr);
			return sendPostRequest(obj,"subscribe");
		} catch (JSONException e) {
			logger.error(e);
		}
		return null;
	}
	
	private static String sendBlockRequest() {
		String jsonstr = "{'requestor': 'andy@example.com','target': 'john@example.com'}";
		JSONObject obj;
		try {
			obj = new JSONObject(jsonstr);
			return sendPostRequest(obj,"block");
		} catch (JSONException e) {
			logger.error(e);
		}
		return null;
	}
	
	private static String getRecipients() {
		String jsonstr = "{sender:'andy@example.com','text': 'Hello World! kate@example.com'}";
		JSONObject obj;
		try {
			obj = new JSONObject(jsonstr);
			return sendPostRequest(obj,"getRecipients");
		} catch (JSONException e) {
			logger.error(e);
		}
		return null;
	}
	
	private static String sendPostRequest(JSONObject obj, String functionPath) {
		ClientConfig cc = new DefaultClientConfig();
		Client client = Client.create(cc);
		WebResource resource = client.resource("http://127.0.0.1:8888/api/user/v1/" + functionPath);

		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, obj);

		return response.getEntity(String.class);
	}

	private static String sendGetRequest(String functionPath) {
		ClientConfig cc = new DefaultClientConfig();
		Client client = Client.create(cc);
		WebResource resource = client.resource("http://127.0.0.1:8888/api/user/v1/" + functionPath);

		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);

		return response.getEntity(String.class);
	}

}
