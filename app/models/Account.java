package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Account extends Model {
    
    @OneToMany
    public List<ParticipantAccount> listParticipants;
    
    @OneToMany
    public List<Expense> listExpenses;
    
    public String name;
    
    public String currency;
    
    
}
