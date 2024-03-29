package controllers;

import java.util.Iterator;
import java.util.List;

import models.Account;
import models.Expense;
import models.ParticipantAccount;
import models.User;
import models.results.ResultAccount;
import models.results.ResultParticipant;
import play.mvc.Controller;
import play.mvc.With;
import utils.Constants;
import utils.Constants.ParticipantRole;
import utils.Constants.ParticipantStatus;
import utils.Secure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import flexjson.JSONSerializer;

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
    
    public static void delete(Long accountId) {
        //NB: deprecated if we set up an other way to be ADMIN
        
        //Retrieve user
        User creator=User.findById(Long.parseLong(session.get("uuid")));
        
        //Retrieve account
        Account accountToDelete=Account.findById(accountId);
        if(accountToDelete!=null) {
            if(accountToDelete.creator.user.equals(creator)) {
                for (Iterator iterator = accountToDelete.listParticipants.iterator(); iterator.hasNext();) {
                    ParticipantAccount participantAccount = (ParticipantAccount) iterator.next();
                    User userTemp=User.findById(participantAccount.user.id);
                    userTemp.listParticipantAccount.remove(participantAccount);
                    userTemp.save();
                }
                accountToDelete.delete();
                renderJSON("{\"success\":\"Account delete\"}");
            }
            else {
                renderJSON("{\"error\":\"Not allowed to delete an account if you're not ADMIN\"}");
            }
        }
        else {
            renderJSON("{\"error\":\"This account doesn't exist\"}");
        }
        
    }
    
    
    public static void addExpense(Long accountId,String description,Float amount) {
        if(description==null) {
            renderJSON("{\"error\":\"The description is not correct\"}");
        }
        if(amount==null) {
            renderJSON("{\"error\":\"The amount is not correct\"}");
        }
        //Retrieve user
        User owner=User.findById(Long.parseLong(session.get("uuid")));
        //Retrieve account
        Account account=Account.findById(accountId);
        if(account==null) {
            renderJSON("{\"error\":\"This account doesn't exist\"}");
        }
        //Retrieve ParticipantAccount
        ParticipantAccount participantAccount=ParticipantAccount.find("SELECT p FROM ParticipantAccount p WHERE p.user.id=? AND p.account.id=?",owner.id,account.id).first();
        if(participantAccount==null) {
            renderJSON("{\"error\":\"You don't participate to this account\"}");
        }
        
        Expense expense=new Expense(participantAccount, account, amount, description);
        expense.create();
        
        account.listExpenses.add(expense);
        account.save();
        
        participantAccount.listExpenses.add(expense);
        participantAccount.save();
        
        renderJSON("{\"success\":\"The expense was successfully registered\"}");
    }
    
    public static void clear(Long accountId) {
        //Retrieve user
        User creator=User.findById(Long.parseLong(session.get("uuid")));
        
        //Retrieve account
        Account accountToClear=Account.findById(accountId);
        if(accountToClear!=null) {
            if(accountToClear.creator.user.equals(creator)) {
                List<Expense> expensesToDelete=Expense.find("SELECT e FROM Expense e WHERE e.account.id=?",accountId).fetch();
                
                for (Iterator iterator = expensesToDelete.iterator(); iterator
                        .hasNext();) {
                    Expense expense = (Expense) iterator.next();
                    ParticipantAccount ownerParticipant=ParticipantAccount.findById(expense.owner.id);
                    
                    ownerParticipant.listExpenses.remove(expense);
                    accountToClear.listExpenses.remove(expense);
                    
                    accountToClear.save();
                    ownerParticipant.save();
                }
                renderJSON("{\"success\":\"Account cleared\"}");
            }
            else {
                renderJSON("{\"error\":\"Not allowed to clear an account if you're not ADMIN\"}");
            }
        }
        else {
            renderJSON("{\"error\":\"This account doesn't exist\"}");
        }
            
    }
    
    public static void getAllParticipantsOfAccount(Long accountId) {
        //Retrieve user
        User owner=User.findById(Long.parseLong(session.get("uuid")));
        //Retrieve account
        Account account=Account.findById(accountId);
        if(account==null) {
            renderJSON("{\"error\":\"This account doesn't exist\"}");
        }
        //Retrieve ParticipantAccount
        ParticipantAccount participantAccount=ParticipantAccount.find("SELECT p FROM ParticipantAccount p WHERE p.user.id=? AND p.account.id=?",owner.id,account.id).first();
        if(participantAccount==null) {
            renderJSON("{\"error\":\"You don't participate to this account\"}");
        }
        renderJSON(new JSONSerializer().exclude("*.class","account").rootName("listOfParticipants").serialize(account.listParticipants));
    }
    
    public static void getAllExpensesOfAccount(Long accountId) {
        //Retrieve user
        User owner=User.findById(Long.parseLong(session.get("uuid")));
        //Retrieve account
        Account account=Account.findById(accountId);
        if(account==null) {
            renderJSON("{\"error\":\"This account doesn't exist\"}");
        }
        //Retrieve ParticipantAccount
        ParticipantAccount participantAccount=ParticipantAccount.find("SELECT p FROM ParticipantAccount p WHERE p.user.id=? AND p.account.id=?",owner.id,account.id).first();
        if(participantAccount==null) {
            renderJSON("{\"error\":\"You don't participate to this account\"}");
        }
        renderJSON(new JSONSerializer().exclude("*.class","account","owner.account").rootName("listOfExpenses").serialize(account.listExpenses));
    }
    
    public static void result(Long accountId) {
      //Retrieve user
        User owner=User.findById(Long.parseLong(session.get("uuid")));
        //Retrieve account
        Account account=Account.findById(accountId);
        if(account==null) {
            renderJSON("{\"error\":\"This account doesn't exist\"}");
        }
        //Retrieve ParticipantAccount
        ParticipantAccount participantAccount=ParticipantAccount.find("SELECT p FROM ParticipantAccount p WHERE p.user.id=? AND p.account.id=?",owner.id,account.id).first();
        if(participantAccount==null) {
            renderJSON("{\"error\":\"You don't participate to this account\"}");
        }
        
        //Resolve Account
        ResultAccount resultAccount=new ResultAccount();
        float totalExpenseofParticipant=0;
        
        for (Iterator iterator = account.listParticipants.iterator(); iterator.hasNext();) {
            ParticipantAccount aParticipantAccount = (ParticipantAccount) iterator.next();
            
            if(aParticipantAccount.status.equals(Constants.ParticipantStatus.CONFIRMED)) {
                
                for (Iterator iterator2 = aParticipantAccount.listExpenses.iterator(); iterator2
                .hasNext();) {
                    Expense anExpense = (Expense) iterator2.next();
                    
                    totalExpenseofParticipant+=anExpense.amount;
                }
                
                resultAccount.addResultParticipant(new ResultParticipant(aParticipantAccount.user.name, aParticipantAccount.user.email, 1, totalExpenseofParticipant));
                
                totalExpenseofParticipant=0;
            }
        }
        
        resultAccount.solve();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        
        renderJSON(gson.toJson(resultAccount));
    }
    
}