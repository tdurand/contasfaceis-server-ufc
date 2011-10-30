package controllers;

import play.*;
import play.libs.WS;
import play.mvc.*;
import utils.Secure;

import java.util.*;

import com.google.gson.JsonObject;

import flexjson.JSONSerializer;

import models.*;

@With(Secure.class)
public class Users extends Controller {
    
    public static User create(Integer FBid,String name,String firstName,String lastName,String email) {
        User user=new User(FBid, name, firstName, lastName, email);
        user.create();
        return user;
    }
    
    public static User createWithEmail(String email) {
        User user=new User(email);
        user.create();
        return user;
    }
    
    public static void listOfAccounts() {
        User user=User.findById(Long.parseLong(session.get("uuid")));
        renderJSON(new JSONSerializer().exclude("*.class","user","account.creator").rootName("listAccounts").serialize(user.listParticipantAccount));
    }
    
    public static void listAllUsers() {
        renderJSON(new JSONSerializer().exclude("*.class").serialize(User.findAll()));
    }
}