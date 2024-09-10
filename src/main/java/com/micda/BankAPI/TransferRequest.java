package com.micda.BankAPI;

public class TransferRequest {

	private String CreditorId;  //Identifiant du compte receveur
	private Double amount;      // Montant à transfer
	public String getCreditorId() {
		return CreditorId;
	}
	public void setCreditorId(String creditorId) {
		CreditorId = creditorId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}
