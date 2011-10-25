package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Account extends Model {
    
    @OneToMany
    public List<ParticipantAccount> listParticipants;
    
    @OneToOne
    public ParticipantAccount creator;
    
    @OneToMany
    public List<Expense> listExpenses;
    
    public String name;
    
    public String currency;

    public Account(ParticipantAccount creator, String name, String currency) {
        super();
        this.creator = creator;
        this.name = name;
        this.currency = currency;
        this.listParticipants = new ArrayList<ParticipantAccount>();
        this.listExpenses = new ArrayList<Expense>();
    }
    
    
}
