package com.auction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

/**
 * Simulates a market auction with buyers and sellers
 *
 * @author Keith
 *
 */
public class MarketplaceAuctionSimulationTest {

	private MarketplaceAuctionImpl marketplaceAuction;
	// arrive rate of offers
	private static final long OFFER_INTERVAL_MILLIS = 100L;
	// arrival rate of bids
	private static final long BID_INTEVAL_MILLIS = 100L;

	@Before
	public void setup() {
		marketplaceAuction = new MarketplaceAuctionImpl();
	}
	
	@Test
	public void testSimulation()  {
		
		CountDownLatch countDownLatch = new CountDownLatch(2);
		
		Buyer buyer = new Buyer(countDownLatch);
		Seller seller = new Seller(countDownLatch);
		
		buyer.start();
		seller.start();
		
		try {
			//wait for buyer and sellers to finish
			countDownLatch.await();
			
			//all bids and offers should be matched and removed
			List<Bid> bidItems = marketplaceAuction.getBidForUser("Buyer");
			assertEquals(0, bidItems.size());
			
			List<Offer> offerItems = marketplaceAuction.getOfferForUser("Seller");
			assertEquals(0, offerItems.size());
			
			//orders should be placed
			List<Order> ordersForBuyer = marketplaceAuction.getOrdersForBuyer("Buyer");
			assertEquals(10, ordersForBuyer.size());
			
			List<Order> ordersForSeller = marketplaceAuction.getOrdersForSeller("Seller");
			assertEquals(10, ordersForSeller.size());
			
		} catch (InterruptedException e) {
			fail("simulation failed!");
		}

	}

	private class Buyer extends Thread {
		
		private CountDownLatch countDownLatch;
		
		public Buyer(CountDownLatch countDownLatch){
			this.countDownLatch = countDownLatch;
		}
		
		@Override
		public void run() {
			
			try{
				//create 10 bids
				for (int i = 0; i < 10; i++) {
					   
						//create bid, increasing the price on each iteration
					   Bid item = new Bid.BidBuilder()
					   					 .itemID(1)
							             .pricePerUnit(10 * (i+1))
							             .quantity(10)
							             .userId("Buyer")
							             .build();
					
	                    marketplaceAuction.addBid(item);
	                    try {
							sleep(BID_INTEVAL_MILLIS);
						} catch (InterruptedException e) {
						}
				}
			}finally{
				this.countDownLatch.countDown();
			}			

		}
	}
	
	private class Seller extends Thread {
		
		private CountDownLatch countDownLatch;
		
		public Seller(CountDownLatch countDownLatch){
			this.countDownLatch = countDownLatch;
		}
		
		@Override
		public void run() {
			
			try{
				//create 10 offers
				for (int i = 10; i > 0; i--) {	   
						//create bid, decreasing the price on each iteration
					   Offer offer = new Offer.OfferBuilder()
					   					 .itemID(1)
							             .pricePerUnit(10 * i)
							             .quantity(10)
							             .userId("Seller")
							             .build();
					
	                    marketplaceAuction.addOffer(offer);
	                    try {
							sleep(OFFER_INTERVAL_MILLIS);
						} catch (InterruptedException e) {
						}
				}
			}finally{
				this.countDownLatch.countDown();
			}
		
		}
	}

}
