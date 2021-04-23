package com.db.trade;

import java.time.LocalDate;

public class Trade {
	private String tradeID;
	private int version;
	private String counterPartyID;
	private String bookID;
	private LocalDate maturityDate;
	private LocalDate createdDate;
	private char expired;

	Trade(String tradeID, int version, String counterPartyID, String bookID, LocalDate maturityDate, LocalDate createdDate, char expired) {
		this.tradeID = tradeID;
		this.version = version;
		this.counterPartyID = counterPartyID;
		this.bookID = bookID;
		this.maturityDate = maturityDate;
		this.createdDate = createdDate;
		this.expired = expired;
	}
	
	/** Getters Setters **/
	public String getTradeID() {
		return tradeID;
	}

	public int getVersion() {
		return version;
	}

	public String getCounterPartyID() {
		return counterPartyID;
	}
	
	public String getBookID() {
		return bookID;
	}

	public LocalDate getMaturityDate() {
		return maturityDate;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public char getExpired() {
		return expired;
	}
	
	public void setExpired(char expired) {
		this.expired = expired;
	}
	
	@Override
	public String toString() {
		return "Trade - [ " + tradeID + ", " + version + ", " + counterPartyID + ", " + bookID + ", " + maturityDate + ", " + createdDate + ", " + expired + " ]";
	}
}
