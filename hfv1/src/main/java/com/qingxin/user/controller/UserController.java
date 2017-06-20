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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger logger = LogManager.getLogger(UserController.class);
	
	private static final String SUCCESS ="success";
	private static final String FRIENDS ="friends";
	private static final String COUNT ="count";
	private static final String RECIPIENTS ="recipients";
	
	@GET
	@Path("/sayHello")
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHello() {

		JSONObject result = new JSONObject();
		try {
			result.put(SUCCESS, "hello, welcome to happy friend v1");
		} catch (JSONException e) {
			logger.error(e);
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
			JSONArray array = (JSONArray) obj.get(FRIENDS);
			boolean success = userService.create(new User(array.get(0).toString()), new User(array.get(1).toString()));
			result.put(SUCCESS, success);

		} catch (JSONException e) {
			logger.error(e);
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			logger.error(e);
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (CreateConflictException e) {
			logger.error(e);
			return ResponseFactory.buildConflictExceptionResponse(obj);
		} catch (Exception e) {
			logger.error(e);
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

			result.put(SUCCESS, true);
			result.put(FRIENDS, friends);
			result.put(COUNT, friends.size());

		} catch (JSONException e) {
			logger.error(e);
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			logger.error(e);
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			logger.error(e);
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
			JSONArray array = (JSONArray) obj.get(FRIENDS);

			List<String> friends1 = userService.getFriends(new User(array.get(0).toString()));
			List<String> friends2 = userService.getFriends(new User(array.get(1).toString()));

			List<String> commonFriends = userService.getCommonFriends(friends1, friends2);

			result.put(SUCCESS, true);
			result.put(FRIENDS, commonFriends);
			result.put(COUNT, commonFriends.size());

		} catch (JSONException e) {
			logger.error(e);
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			logger.error(e);
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			logger.error(e);
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

			result.put(SUCCESS, success);
		} catch (JSONException e) {
			logger.error(e);
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			logger.error(e);
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			logger.error(e);
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
			result.put(SUCCESS, success);
			
		} catch (JSONException e) {
			logger.error(e);
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			logger.error(e);
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			logger.error(e);
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
			
			result.put(SUCCESS, true);
			result.put(RECIPIENTS, recipients);
			
		} catch (JSONException e) {
			logger.error(e);
			return ResponseFactory.buildBadRequestResponse(obj);
		} catch (UserNotFoundException e) {
			logger.error(e);
			return ResponseFactory.buildUserNotFoundExceptionResponse(obj);
		} catch (Exception e) {
			logger.error(e);
			return ResponseFactory.buildServerExceptionResponse(obj);
		}
		return ResponseFactory.buildSuccessResponse(result);
    }
	
}
