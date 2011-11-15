package models.results;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.*;

public class Transaction extends Model {
    @Expose
	private ResultParticipant beneficiary;
    @Expose
	private ResultParticipant payor;
    @Expose
	private float amount;
	
	public Transaction(ResultParticipant beneficiary, ResultParticipant payor, float amount) {
		super();
		this.beneficiary = beneficiary;
		this.payor = payor;
		this.amount = amount;

	}
	
	public static double floor(double a, int n) {
		double p = Math.pow(10.0, n);
		return Math.floor((a*p)+0.5) / p;
	}
	
	public static float Round(double Rval, int Rpl) {
	    return new BigDecimal(Rval).setScale(Rpl, BigDecimal.ROUND_FLOOR ).floatValue();
	    }


	public void effectuerTransaction()
	{
		
		beneficiary.finalBalance-=amount;
		payor.finalBalance+=amount;
		
		
	}
	
	public void roundTransaction()
	{
		beneficiary.finalBalance=Round(beneficiary.finalBalance, 2);
		beneficiary.balance=Round(beneficiary.balance, 2);
		beneficiary.amountSpent=Round(beneficiary.amountSpent, 2);
		
		payor.finalBalance=Round(payor.finalBalance, 2);
		payor.balance=Round(payor.balance, 2);
		payor.amountSpent=Round(payor.amountSpent, 2);
		
		amount=Round(amount, 2);
		
		
	}


	public ResultParticipant getBeneficiaire() {
		return beneficiary;
	}


	public void setBeneficiaire(ResultParticipant beneficiaire) {
		this.beneficiary = beneficiaire;
	}


	public ResultParticipant getPayeur() {
		return payor;
	}


	public void setPayeur(ResultParticipant payeur) {
		this.payor = payeur;
	}


	public float getMontant() {
		return amount;
	}


	public void setMontant(float montant) {
		this.amount = montant;
	}

	@Override
	public String toString() {
		return "Transaction [beneficiaire=" + beneficiary + ", payeur="
				+ payor + ", montant=" + amount + "]";
	}
	
	
	
	
	
	
    
}
