package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;
import utils.Constants.ParticipantRole;
import utils.Constants.ParticipantStatus;
import utils.Constants.*;

@Entity
public class ParticipantAccount extends Model {
    
    @ManyToOne
    public User user;
    
    @ManyToOne
    public Account account;
    
    @OneToMany
    public List<Expense> listExpenses;
    
    public ParticipantStatus status;
    
    public ParticipantRole role;

    public ParticipantAccount(User user, Account account,
            ParticipantStatus status, ParticipantRole role) {
        super();
        this.user = user;
        this.account = account;
        this.status = status;
        this.role = role;
        this.listExpenses=new ArrayList<Expense>();
    }
}
