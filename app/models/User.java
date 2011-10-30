package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
    public String email;
    
    @OneToMany
    public List<ParticipantAccount> listParticipantAccount;
    
    public User(int fBid, String name, String firstName, String lastName, String email) {
        super();
        this.FBid = fBid;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.listParticipantAccount=new ArrayList<ParticipantAccount>();
    }

    public User(String email) {
        super();
        this.email = email;
        this.listParticipantAccount=new ArrayList<ParticipantAccount>();
    }
}
