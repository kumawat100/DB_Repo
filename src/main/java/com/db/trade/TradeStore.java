package com.db.trade;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
 * TradeStore class has all the functional methods
 * All the trades are being stored in a HashMap allTradesData
 */
public class TradeStore {
	// HashMap to contain all trades data
	HashMap<String, Trade> allTradesData = new HashMap<>();

	// Method to add trade to hashMap if trade is valid
	public void addTrade(Trade t1) {
		if (isTradeValid(t1)) {
			allTradesData.put(t1.getTradeID(), t1);
			System.out.println("New Trade Added - " + t1);
		} else {
			System.out.println("Invalid Trade, Rejected - " + t1);
		}
	}

	// Method to check if trade is valid or not based on version and maturity date
	public boolean isTradeValid(Trade t1) {
		return isVersionValid(t1) && isMaturityDateValid(t1);
	}

	// if the lower trade version is being received by the store it will reject the
	// trade and throw an exception
	public boolean isVersionValid(Trade t1) {
		// Check if same trade exists
		if (isTradeAlreadyExists(t1)) {
			Trade oldTrade = allTradesData.get(t1.getTradeID());
			// Exception thrown in case current trade version is lower than old one
			if (t1.getVersion() < oldTrade.getVersion()) {
				throw new IllegalArgumentException("Lower version of trade received for " + t1);
			}
		}
		return true;
	}

	// Returns true of maturity date is today or any later date
	public boolean isMaturityDateValid(Trade t1) {
		return !(t1.getMaturityDate().isBefore(LocalDate.now()));
	}

	// Returns true if a trade with same trade ID already exists in store
	public boolean isTradeAlreadyExists(Trade t1) {
		Trade oldTrade = allTradesData.get(t1.getTradeID());
		return oldTrade != null ? true : false;
	}

	// Returns the count of total trades in store
	public int getCount() {
		return allTradesData.size();
	}

	// Removes all trades from store
	public void clearAllData() {
		allTradesData.clear();
	}

	// This method will constantly poll trades for maturity date
	public void autoUpdateExpiryFlag() {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// checking and updating expire flag every 60 seconds
		final ScheduledFuture<?> updateTask = scheduler.scheduleAtFixedRate
				(new UpdateExpiryFlag(), 0, 60, TimeUnit.SECONDS);

		// Shutdown set for after 30 days
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				updateTask.cancel(true);
				scheduler.shutdown();
			}
		}, 30, TimeUnit.DAYS);
	}

	// This runnable class will traverse through all trades
	// and update expire flag for trades whose maturityDate crosses todays date
	class UpdateExpiryFlag implements Runnable {
		@Override
		public void run() {
			for (Map.Entry<String, Trade> entry : allTradesData.entrySet()) {
				Trade t1 = entry.getValue();
				if (isMaturityDateCrossed(t1) && t1.getExpired() == 'N') {
					t1.setExpired('Y');
					System.out.println("Expire flag updated for : " + t1);
					allTradesData.put(t1.getTradeID(), t1);
				}
			}
		}

		// This method will return true if maturity date is passed today's date
		public boolean isMaturityDateCrossed(Trade t1) {
			return LocalDate.now().isAfter(t1.getMaturityDate());
		}
	}
}
