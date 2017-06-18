package com.qingxin.user.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.qingxin.service.UserService;
import com.qingxin.user.bean.User; 

@Path("/api/user/v1") 
public class UserController {
	@GET  
	@Path("/sayHello")
    @Produces(MediaType.APPLICATION_JSON)  
    public String sayHello() {  
		
		JSONObject  result = new JSONObject ();
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
    public String create(JSONObject obj) {
		UserService userService = UserService.getInstance();
		JSONObject result = new JSONObject();
		
		try {
			JSONArray array = (JSONArray) obj.get("friends");
			
			boolean success = userService.create(new User(array.get(0).toString()), new User(array.get(0).toString()));
			
			result.put("success", success);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result.toString();  
        
    }
}
