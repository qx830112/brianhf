package com.qingxin.client;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RestClient {
	
	public static void main(String args[]) {
		System.out.println(RestClient.sendHelloRequest());
		System.out.println("Create a friend connection:" + RestClient.sendCreateRequest());
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
			e.printStackTrace();
		}
		return null;
	}

	private static String sendPostRequest(JSONObject obj, String functionPath) {
		ClientConfig cc = new DefaultClientConfig();
		Client client = Client.create(cc);
		WebResource resource = client.resource("http://127.0.0.1:8888/api/user/v1/" + functionPath);

		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, obj);

		String returnedString = response.getEntity(String.class);
		return returnedString;
	}

	private static String sendGetRequest(String functionPath) {
		ClientConfig cc = new DefaultClientConfig();
		Client client = Client.create(cc);
		WebResource resource = client.resource("http://127.0.0.1:8888/api/user/v1/" + functionPath);

		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);

		String returnedString = response.getEntity(String.class);
		return returnedString;
	}

}
