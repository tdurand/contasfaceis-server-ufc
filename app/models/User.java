package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="usersFB")
public class User extends Model {
    
    public int FBid;
    public String name;
    public String firstName;
    public String lastName;
    
    @OneToMany
    public List<ParticipantAccount> listParticipantAccount;
    
    public User(int fBid, String name, String firstName, String lastName) {
        super();
        FBid = fBid;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
