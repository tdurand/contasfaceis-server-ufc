package models.results;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import com.google.gson.annotations.Expose;

import java.util.*;

public class ResultParticipant extends Model {
    @Expose
	public String name;
    @Expose
	public String email;
	public float coeff;
	@Expose
	public float amountSpent;
	
	public float finalBalance;
	@Expose
	public float balance;
	
    public ResultAccount resultAccount;
	
	public ResultParticipant(String nom, String email, float coeff, float montantDepense) {
		super();
		this.name = nom;
		this.email = email;
		this.coeff = coeff;
		this.amountSpent = montantDepense;
	}

	public ResultParticipant() {
		// TODO Auto-generated constructor stub
	}
	
	public void calculSolde(float montant_unitaire)
	{
		finalBalance=amountSpent-montant_unitaire;
	}

	@Override
	public String toString() {
		return "Personne [nom=" + name + ", email=" + email + ", coeff=" + coeff
				+ ", montant_depense=" + amountSpent + ", solde=" + finalBalance
				+ "]";
	}
    
}
