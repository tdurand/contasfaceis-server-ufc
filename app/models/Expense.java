package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Expense extends Model {
    
    @ManyToOne
    public ParticipantAccount owner;
    
    @ManyToOne
    public Account account;
    
    public Float amount;
    public String description;
    
    
    public Expense(ParticipantAccount owner, Account account, Float amount,
            String description) {
        super();
        this.owner = owner;
        this.account = account;
        this.amount = amount;
        this.description = description;
    }
    
}
