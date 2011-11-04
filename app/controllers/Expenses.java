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
public class Expenses extends Controller {
    
    public static void deleteExpense(Long expenseId) {
        //Retrieve user
        User owner=User.findById(Long.parseLong(session.get("uuid")));
        //Retrieve expense
        Expense expense=Expense.findById(expenseId);
        if(expense==null) {
            renderJSON("{\"error\":\"This expense doesn't exist\"}");
        }
        
        if(!expense.owner.user.equals(owner)) {
            renderJSON("{\"error\":\"This expense doesn't belong to you, you can't delete it\"}");
        }
        else {
            ParticipantAccount ownerParticipant=ParticipantAccount.findById(expense.owner.id);
            Account accountOfExpense=Account.findById(expense.account.id);
            
            ownerParticipant.listExpenses.remove(expense);
            accountOfExpense.listExpenses.remove(expense);
            
            accountOfExpense.save();
            ownerParticipant.save();
        }
    }

}