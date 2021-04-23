package com.db.trade;

import java.time.LocalDate;

/* 
 * This is the main class
 * Passed dummy data and created some records
 * and added to the store
 */
public class TradeMain {
	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		LocalDate validMaturityDate = LocalDate.now();
		LocalDate invalidMaturityDate = LocalDate.parse("2015-03-14");
		LocalDate createdDate = LocalDate.parse("2015-03-14");
		
		TradeStore tradeStore = new TradeStore();
		
		// Run the scheduler which will poll every 60 seconds
		// and update expire flag if maturity date is before today
		tradeStore.autoUpdateExpiryFlag();
		
		// Creating trade objects
		Trade t1 = new Trade("1", 1, "CP-1", "B1", validMaturityDate, today, 'N');
		Trade t2 = new Trade("2", 2, "CP-2", "B1", validMaturityDate, today, 'N');
		Trade t3 = new Trade("2", 3, "CP-1", "B1", validMaturityDate, createdDate, 'N');	//will override existing id 2
		Trade t4 = new Trade("4", 3, "CP-3", "B2", validMaturityDate, today, 'N');
		Trade t5 = new Trade("1", 2, "CP-1", "B3", invalidMaturityDate, today, 'N');	// will be rejected due to invalid maturity date
		Trade t6 = new Trade("4", 2, "CP-3", "B2", validMaturityDate, today, 'N');		// lower version, will throw Exception
		
		// Adding trade data to store
		tradeStore.addTrade(t1);
		tradeStore.addTrade(t2);
		tradeStore.addTrade(t3);		// will override existing one
		tradeStore.addTrade(t4);		
		tradeStore.addTrade(t5);		// will be rejected due to invalid maturity date
		try {
			tradeStore.addTrade(t6);	// lower version, will throw Exception
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
}
