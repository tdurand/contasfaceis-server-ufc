package controllers;

import play.*;
import play.libs.WS;
import play.mvc.*;

import java.util.*;

import com.google.gson.JsonObject;

import models.*;

public class Application extends Controller {

    public static void index() {
        JsonObject me = null;
        String access_token=params.get("access_token");
        if (access_token != null) {
            me = WS.url("https://graph.facebook.com/me?access_token=%s", WS.encode(access_token)).get().getJson().getAsJsonObject();
        }
        renderText(me);
    }

}