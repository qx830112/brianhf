package com.qingxin.user.factory;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResponseFactory {
	private static final String ERR_MESSAGE ="ErrMessage";
	private static final String ERR_CODE ="ErrorCode";
	
	private ResponseFactory(){
		
	}
	public static Response buildSuccessResponse(JSONObject result) {
		return Response.ok(new GenericEntity<String>(result.toString()){}).build();
	}
	
	public static Response buildUserNotFoundExceptionResponse(JSONObject obj) {
		try {
			obj.put(ERR_MESSAGE, "Cannot find the user");
			obj.put(ERR_CODE,"1001");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.NOT_FOUND).entity(new GenericEntity<String>(obj.toString()){}).build();
	}
	
	public static Response buildConflictExceptionResponse(JSONObject obj) {
		try {
			obj.put(ERR_MESSAGE, "One user is in the block list of the other");
			obj.put(ERR_CODE,"1002");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.CONFLICT).entity(new GenericEntity<String>(obj.toString()){}).build();
	}

	public static Response buildBadRequestResponse(JSONObject obj) {
		try {
			obj.put(ERR_MESSAGE, "Bad request");
			obj.put(ERR_CODE,"5001");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<String>(obj.toString()){}).build();
	}
	
	public static Response buildServerExceptionResponse(JSONObject obj) {
		try {
			obj.put(ERR_MESSAGE, "There is an error in the server");
			obj.put(ERR_CODE,"5002");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new GenericEntity<String>(obj.toString()){}).build();
	}

	
}
