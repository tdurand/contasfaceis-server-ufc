package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;
import utils.Constants.*;

@Entity
public class ParticipantAccount extends Model {
    
    @Required
    @ManyToOne
    public User user;
    
    @Required
    @ManyToOne
    public Account account;
    
    @Required
    @OneToMany
    public List<Expense> listExpenses;
    
    public ParticipantStatus status;
    
    public ParticipantRole role;
    
    
}
