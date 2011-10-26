package utils;

import models.User;

import com.google.gson.JsonObject;

import controllers.Users;

import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;

public class Secure extends Controller {
    
    @Before(unless="index")
    static void auth() {
        JsonObject me = null;
        String access_token=params.get("access_token");
        if (access_token != null) {
            me = WS.url("https://graph.facebook.com/me?access_token=%s", WS.encode(access_token)).get().getJson().getAsJsonObject();
            if(me.get("error")==null) {
                User user=User.find("byFBid", me.get("id").getAsInt()).first();
                if(user==null) {
                    user=Users.create(me.get("id").getAsInt(),me.get("name").getAsString(),me.get("first_name").getAsString(),me.get("last_name").getAsString(),me.get("email").getAsString());
                }
                else if(user.email!=me.get("email").getAsString()) {
                    user.email=me.get("email").getAsString();
                    user.save();
                }
                
                session.put("uuid", user.id);
            }
            else {
                renderText(me);
            }
        }
        else {
            renderText("Need access_token");
        }
    }

}
