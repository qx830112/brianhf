package com.qingxin.user.controller;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.qingxin.service.UserService;
import com.qingxin.user.bean.User;
import com.qingxin.user.exception.CreateConflictException;
import com.qingxin.user.exception.UserNotFoundException;
import com.qingxin.user.factory.ResponseFactory;

@Path("/api/user/v1")
public class UserController {
	@GET
	@Path("/sayHello")
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHello() {

		JSONObject result = new JSONObject();
		try {
			result.put("success", "hello, welcome to happy friend v1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(JSONObject obj) {
		UserService userService = UserService.getInstance();
		JSONObject result = new JSONObject();

		try {
			JSONArray array = (JSONArray) obj.get("friends");
			boolean success = userService.create(new User(array.get(0).toString()), new User(array.get(1).toString()));
			result.put("success", success);

		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (CreateConflictException e) {
			e.printStackTrace();
			return ResponseFactory.buildConflictExceptionResponse(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.buildServerExceptionResponse(obj);
		}

		return ResponseFactory.buildSuccessResponse(result);
	}

	@POST
	@Path("/getFriends")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFriends(JSONObject obj) {
		JSONObject result = new JSONObject();
		UserService userService = UserService.getInstance();
		try {
			String mailAdress = obj.get("email").toString();
			List<String> friends = userService.getFriends(new User(mailAdress));

			result.put("success", true);
			result.put("friends", friends);
			result.put("count", friends.size());

		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.buildServerExceptionResponse(obj);
		}

		return ResponseFactory.buildSuccessResponse(result);
	}

	@POST
	@Path("/getCommonFriends")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommonFriends(JSONObject obj) {
		JSONObject result = new JSONObject();
		UserService userService = UserService.getInstance();
		try {
			JSONArray array = (JSONArray) obj.get("friends");

			List<String> friends1 = userService.getFriends(new User(array.get(0).toString()));
			List<String> friends2 = userService.getFriends(new User(array.get(1).toString()));

			List<String> commonFriends = userService.getCommonFriends(friends1, friends2);

			result.put("success", true);
			result.put("friends", commonFriends);
			result.put("count", commonFriends.size());

		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.buildServerExceptionResponse(obj);
		}

		return ResponseFactory.buildSuccessResponse(result);
	}

	@POST
	@Path("/subscribe")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response subscribe(JSONObject obj) {
		UserService userService = UserService.getInstance();
		JSONObject result = new JSONObject();

		try {
			String requestor = obj.get("requestor").toString();
			String target = obj.get("target").toString();

			boolean success = userService.subscribe(new User(requestor), new User(target));

			result.put("success", success);
		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.buildServerExceptionResponse(obj);
		}
		return ResponseFactory.buildSuccessResponse(result);
	}

	@POST
	@Path("/block")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response block(JSONObject obj) {
		UserService userService = UserService.getInstance();
		JSONObject result = new JSONObject();

		try {
			String requestor = obj.get("requestor").toString();
			String target = obj.get("target").toString();

			boolean success = userService.block(new User(requestor), new User(target));
			result.put("success", success);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.buildServerExceptionResponse(obj);
		}
		return ResponseFactory.buildSuccessResponse(result);
	}

	@POST  
	@Path("/getRecipients")
	@Consumes(MediaType.APPLICATION_JSON) 
    @Produces(MediaType.APPLICATION_JSON)  
    public Response getRecipients(JSONObject obj) {  
		JSONObject result = new JSONObject();
		UserService userService = UserService.getInstance();
		try {
			String mailAdress = obj.get("sender").toString();
			String text = obj.get("text").toString();
			Set<String> recipients = userService.getRecipients(new User(mailAdress),text);
			
			result.put("success", true);
			result.put("recipients", recipients);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.buildServerExceptionResponse(obj);
		}
		return ResponseFactory.buildSuccessResponse(result);
    }
	
}
