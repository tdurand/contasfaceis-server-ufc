package controllers;

import play.*;
import play.libs.WS;
import play.mvc.*;
import utils.Constants.ParticipantRole;
import utils.Constants.ParticipantStatus;
import utils.Secure;

import java.util.*;

import com.google.gson.JsonObject;

import models.*;

@With(Secure.class)
public class ParticipantsAccounts extends Controller {
    
    public static ParticipantAccount create(User user,Account account,ParticipantStatus status,ParticipantRole role) {
        ParticipantAccount participantAccount=new ParticipantAccount(user, account, status, role);
        participantAccount.create();
        return participantAccount;
    }
    
}