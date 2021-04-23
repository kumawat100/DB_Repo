package com.db.trade;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TradeStoreUnitTests {
	TradeStore tradeStore = new TradeStore();
	LocalDate today = LocalDate.now();
	LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);
	LocalDate validMaturityDate = LocalDate.now();
	LocalDate invalidMaturityDate = LocalDate.parse("2015-03-14");
	LocalDate createdDate = LocalDate.parse("2015-03-14");
	
	@Before
	public void init() {
		// Setup Data for unit tests
		tradeStore.clearAllData();
		Trade t1 = new Trade("1", 1, "CP-1", "B1", validMaturityDate, today, 'N');
		Trade t2 = new Trade("2", 2, "CP-2", "B1", validMaturityDate, today, 'N');
		Trade t3 = new Trade("2", 3, "CP-1", "B1", validMaturityDate, createdDate, 'N');	//will override existing one
		Trade t4 = new Trade("4", 3, "CP-3", "B2", validMaturityDate, today, 'N');
		
		tradeStore.addTrade(t1);
		tradeStore.addTrade(t2);
		tradeStore.addTrade(t3);		//will override existing one
		tradeStore.addTrade(t4);
		System.out.println();
	}
	
	@After
	public void destroy() {
		tradeStore.clearAllData();
	}
	
	@Test
	public void addTradeTest() {
		Trade t1 = new Trade("5", 2, "CP-1", "B3", validMaturityDate, today, 'N');
		tradeStore.addTrade(t1);
		Assert.assertEquals(4, tradeStore.getCount());
		
		// Adding invalid trade
		Trade t2 = new Trade("5", 2, "CP-1", "B3", invalidMaturityDate, today, 'N');
		tradeStore.addTrade(t2);
		Assert.assertEquals(4, tradeStore.getCount());
	}
	
	@Test
	public void getCountTest() {
		Assert.assertEquals(3, tradeStore.getCount());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void lowerVersionExceptionTest() {
		Trade t1 = new Trade("4", 2, "CP-3", "B2", validMaturityDate, today, 'N');
		tradeStore.isVersionValid(t1);
	}
	
	@Test
	public void sameVersionReceivedTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B3", validMaturityDate, today, 'N');
		Assert.assertTrue(tradeStore.isVersionValid(t1));
	}
	
	@Test
	public void higherVersionReceivedTest() {
		Trade t1 = new Trade("1", 2, "CP-1", "B3", validMaturityDate, today, 'N');
		Assert.assertTrue(tradeStore.isVersionValid(t1));
	}
	
	@Test
	public void maturityDateTest() {
		Trade t1 = new Trade("1", 2, "CP-1", "B3", invalidMaturityDate, today, 'N');
		Assert.assertFalse(tradeStore.isMaturityDateValid(t1));
		
		Trade t2 = new Trade("1", 2, "CP-1", "B3", validMaturityDate, today, 'N');
		Assert.assertTrue(tradeStore.isMaturityDateValid(t2));		
	}
	
	@Test
	public void isTradeValidTest() {
		// Version valid, Maturity Date invalid
		Trade t1 = new Trade("1", 2, "CP-1", "B3", invalidMaturityDate, today, 'N');
		Assert.assertFalse(tradeStore.isTradeValid(t1));
		
		// Version and maturity both valid
		Trade t2 = new Trade("1", 2, "CP-1", "B3", validMaturityDate, today, 'N');
		Assert.assertTrue(tradeStore.isTradeValid(t2));	
	}
	
	@Test
	public void getCounterPartyIDTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", validMaturityDate, today, 'N');
		Assert.assertEquals("CP-1", t1.getCounterPartyID());
	}
	
	@Test
	public void getBookIDTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", validMaturityDate, today, 'N');
		Assert.assertEquals("B1", t1.getBookID());
	}
	
	@Test
	public void getCreatedDateTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", validMaturityDate, today, 'N');
		Assert.assertEquals(today, t1.getCreatedDate());
	}
	
	@Test
	public void getExpiredTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", validMaturityDate, today, 'N');
		Assert.assertEquals('N', t1.getExpired());
	}
	
	@Test
	public void isMaturityDateCrossedTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", yesterday, today, 'N');
		Assert.assertTrue(tradeStore.new UpdateExpiryFlag().isMaturityDateCrossed(t1));
		
		Trade t2 = new Trade("1", 1, "CP-1", "B1", today, today, 'N');
		Assert.assertFalse(tradeStore.new UpdateExpiryFlag().isMaturityDateCrossed(t2));
	}
	
	/*@Test
	public void expireFlagUpdateTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", today, today, 'N');
		tradeStore.addTrade(t1);
		tradeStore.new UpdateExpiryFlag().run();
		//Assert.assertEquals("Y", t1.getExpired());
	}*/
	
	@Test
	public void setExpiredTest() {
		Trade t1 = new Trade("1", 1, "CP-1", "B1", today, today, 'N');
		Assert.assertEquals('N', t1.getExpired());
		t1.setExpired('Y');
		Assert.assertEquals('Y', t1.getExpired());
	}
}
