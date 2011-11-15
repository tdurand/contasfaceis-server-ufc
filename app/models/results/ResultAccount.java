package models.results;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import com.google.gson.annotations.Expose;

import models.results.*;

import java.text.DecimalFormat;
import java.util.*;
import java.lang.Math;

public class ResultAccount extends Model {
    @Expose
	public List<ResultParticipant> resultParticipants; 
	
	public float allTransactions[][];
	@Expose
	public List<Transaction> transactionsToResolve;
	@Expose
	public float unitCost;
	
	 
	 public ResultAccount() {

		this.resultParticipants = new ArrayList();
	}
	
	 public ResultAccount(int nbpers)
	 {
		 this.resultParticipants = new ArrayList();
		 
		 for (int i = 0; i < nbpers; i++) {
			 
			 this.resultParticipants.add(new ResultParticipant());
			
		}
		 
	 }



	public void addResultParticipant(ResultParticipant personne)
	 {
		 resultParticipants.add(personne);
	 }
	
	public void addPersonne(String nom, String email, float coeff, float montantDepense)
	{
		ResultParticipant personne=new ResultParticipant(nom,email,coeff,montantDepense);
		resultParticipants.add(personne);
	}
	
	public void solve()
	{
		transactionsToResolve=new ArrayList<Transaction>();
		
		Transaction transactionTampon;
	
		
		initSoldes();
		
		int i=0;
		
		while(sumAbsSolde()>0.1)
		{
			
			calculMatrice();
			transactionTampon=rechercherTransaction();			
			transactionTampon.effectuerTransaction();
			
			i++;
	
		}
		
		unitCost=montantUnitaire();
		unitCost=Round(unitCost, 2);
		arrondirGroupe();
		
	}
	public void arrondirGroupe()
	{
		
		for (Iterator iterator = transactionsToResolve.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			
			transaction.roundTransaction();
			
		}
		
	}
	
	public static double floor(double a, int n) {
        double p = Math.pow(10.0, n);
        return Math.floor((a*p)+0.5) / p;
    }
	
	
	public float sumDepense()
	{
		float total = 0;
		
		for (Iterator iterator = resultParticipants.iterator(); iterator.hasNext();) {
			ResultParticipant personne = (ResultParticipant) iterator.next();
			
			total+=personne.amountSpent;
		}
		
		return total;
	}
	
	public void initSoldes()
	{
			
		for (Iterator iterator = resultParticipants.iterator(); iterator.hasNext();) 
		{
			ResultParticipant personne = (ResultParticipant) iterator.next();
			
			personne.finalBalance=personne.amountSpent-(sumDepense()/sumCoeff())*personne.coeff;
			personne.balance=personne.amountSpent-(sumDepense()/sumCoeff())*personne.coeff;

			
		}
		
	}
	
	
	public float sumAbsSolde()
	{
		float total = 0;
		
		for (Iterator iterator = resultParticipants.iterator(); iterator.hasNext();) {
			ResultParticipant personne = (ResultParticipant) iterator.next();
			
			total+=Math.abs(personne.finalBalance);
		}
		
		return total;
	}
	
	public float sumCoeff()
	{
		float total = 0;
		
		for (Iterator iterator = resultParticipants.iterator(); iterator.hasNext();) {
			ResultParticipant personne = (ResultParticipant) iterator.next();
			
			total+=personne.coeff;
		}
		
		return total;
	}
	
	public float montantUnitaire()
	{
		return (sumDepense()/sumCoeff());
		
	}
	
	public void calculMatrice()
	{
		allTransactions=new float[resultParticipants.size()][resultParticipants.size()];
		
		for (int i = 0; i < resultParticipants.size(); i++) 
		{
			
			for (int j = 0; j < resultParticipants.size(); j++) 
			{
				
				allTransactions[i][j]=resultParticipants.get(i).finalBalance+resultParticipants.get(j).finalBalance;
				
			}
			
		}
	}
	
	public Transaction rechercherTransaction()
	{
		boolean transactionTrouve=false;
		float temp=sumDepense();
		Transaction transactionTampon = null;
		
		
		//recherche de niveau1
		for (int i = 0; i < resultParticipants.size(); i++) 
		{
			
			for (int j = 0; j < resultParticipants.size(); j++) 
			{
				if(allTransactions[i][j]>=-0.01 
						&& allTransactions[i][j]<temp 
						&& i!=j 
						&& resultParticipants.get(i).finalBalance!=0 
						&& resultParticipants.get(j).finalBalance!=0 
						&& isSigneContraire(resultParticipants.get(i).finalBalance,resultParticipants.get(j).finalBalance))
				{
					
						transactionTrouve=true;
				        temp=allTransactions[i][j];
				        
				   		if(resultParticipants.get(i).finalBalance>0) 
						{
							transactionTampon=new Transaction(resultParticipants.get(i),resultParticipants.get(j),Math.abs(resultParticipants.get(j).finalBalance));
							
						
						}
						else
						{
							transactionTampon=new Transaction(resultParticipants.get(j),resultParticipants.get(i),Math.abs(resultParticipants.get(i).finalBalance));
							
							
						}			
					
					
				}
				
				
				
			}
			
		}
		
		//niveau2
		if(transactionTrouve==false)
		{
			for (int i = 0; i < resultParticipants.size(); i++) 
			{
				
				for (int j = 0; j < resultParticipants.size(); j++) 
				{
					if(Math.abs(allTransactions[i][j])>=0.01 
							&& Math.abs(allTransactions[i][j])<temp 
							&& i!=j 
							&& resultParticipants.get(i).finalBalance!=0 
							&& resultParticipants.get(j).finalBalance!=0 
							&& isSigneContraire(resultParticipants.get(i).finalBalance,resultParticipants.get(j).finalBalance))
					{
						transactionTrouve=true;
				        temp=allTransactions[i][j];
				        
				        if(resultParticipants.get(i).finalBalance>0) 
						{
							transactionTampon=new Transaction(resultParticipants.get(i),resultParticipants.get(j),Math.min(Math.abs(resultParticipants.get(i).finalBalance), Math.abs(resultParticipants.get(j).finalBalance)));
						
						}
						else
						{
							transactionTampon=new Transaction(resultParticipants.get(j),resultParticipants.get(i),Math.min(Math.abs(resultParticipants.get(i).finalBalance), Math.abs(resultParticipants.get(j).finalBalance)));
							
						}			
						
					}
					
				}
			}
		}
		
		transactionsToResolve.add(transactionTampon);
		
		return transactionTampon;
			
		
	}
	
	public static float Round(float Rval, int Rpl) {
        float p = (float)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float)tmp/p;
        }
	
	double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
    return Double.valueOf(twoDForm.format(d));
    }
	
	
	
	
	public boolean isSigneContraire(float a,float b)
	{
		if((a<0 && b>0) || (a>0 && b<0))
		{
		return true;
		}
		else
		{
		return false;
		}

	}
	

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResultAccount [resultParticipants=" + resultParticipants
                + ", transactionsToResolve=" + transactionsToResolve
                + ", unitCost=" + unitCost + "]";
    }
	
	
	
	
    
}
