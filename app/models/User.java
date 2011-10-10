package models;

public class User {
    
    public int FBid;
    public String name;
    public String firstName;
    public String lastName;
    
    public User(int fBid, String name, String firstName, String lastName) {
        super();
        FBid = fBid;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
