package controllers;

import play.*;
import play.libs.WS;
import play.mvc.*;
import utils.Constants.ParticipantRole;
import utils.Constants.ParticipantStatus;
import utils.Secure;

import java.util.*;

import com.google.gson.JsonObject;

import flexjson.JSONSerializer;

import models.*;

@With(Secure.class)
public class Accounts extends Controller {
    
    public static void create(String name,String currency,List<String> emails) {
        
        //Retrieve user
        User creator=User.findById(Long.parseLong(session.get("uuid")));
        //Create Account
        Account newAccount=new Account(null, name, currency);
        newAccount.create();
        //Create Creator participant
        ParticipantAccount creatorParticipant=ParticipantsAccounts.create(creator, newAccount, ParticipantStatus.CONFIRMED, ParticipantRole.ADMIN);
        
        //Associate Account and participant
        newAccount.creator=creatorParticipant;
        newAccount.listParticipants.add(creatorParticipant);
            //persist
            newAccount.save();
        
        //Update bidirectional link
        creator.listParticipantAccount.add(creatorParticipant);
        creator.save();
        
        User userTemp;
        //Iterate over the emails, and create user if they don't exist
        for (Iterator iterator = emails.iterator(); iterator.hasNext();) {
            String email = (String) iterator.next();
            
            userTemp=User.find("byEmail", email).first();
            //if user doesn't exit, whe create it with is email
            if(userTemp==null) {
                userTemp=Users.createWithEmail(email);
            }
            
            
            ParticipantAccount newParticipantAccount=ParticipantsAccounts.create(userTemp, newAccount, ParticipantStatus.PENDING, ParticipantRole.USER);
            //update the bidirectionnals link
            userTemp.listParticipantAccount.add(newParticipantAccount);
            userTemp.save();
            
            newAccount.listParticipants.add(newParticipantAccount);
            newAccount.save();
        }
        
        renderJSON(new JSONSerializer().include("listParticipants").exclude("*.class").serialize(newAccount));
       
    }
    
    
}