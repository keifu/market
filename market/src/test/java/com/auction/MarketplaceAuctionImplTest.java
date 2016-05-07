package com.auction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MarketplaceAuctionImplTest {

	private MarketplaceAuctionImpl marketAuction;

	private Offer item_1_offer_1;
	private Offer item_1_offer_2;
	private Offer item_1_offer_3;
	private Offer item_1_offer_4;
	private Bid item_1_bid_1;
	private Bid item_1_bid_2;
	private Bid item_1_bid_3;

	@Before
	public void setup() {
		marketAuction = new MarketplaceAuctionImpl();

		// test data as per example in spec
		item_1_bid_1 = new Bid.BidBuilder()
							.itemID(1)
							.pricePerUnit(25)
							.quantity(10)
							.userId("Buyer").build();

		item_1_offer_1 = new Offer.OfferBuilder()
							.itemID(1)
							.pricePerUnit(25)
							.quantity(5)
							.userId("Seller").build();

		item_1_offer_2 = new Offer.OfferBuilder()
							.itemID(1)
							.pricePerUnit(24)
							.quantity(10)
							.userId("Seller").build();

		item_1_bid_2 = new Bid.BidBuilder()
							.itemID(1)
							.pricePerUnit(24)
							.quantity(5)
							.userId("Buyer").build();

		//for other scenarios
		item_1_offer_3 = new Offer.OfferBuilder()
							.itemID(1)
							.pricePerUnit(5)
							.quantity(1)
							.userId("Seller").build();

		item_1_offer_4 = new Offer.OfferBuilder()
							.itemID(1)
							.pricePerUnit(25)
							.quantity(15)
							.userId("Seller").build();
		
		item_1_bid_3 = new Bid.BidBuilder()
							.itemID(1)
							.pricePerUnit(30)
							.quantity(5)
							.userId("Buyer").build();
	}

	@Test(expected = NullPointerException.class)
	public void testAddBid_Null() {
		marketAuction.addBid(null);
	}

	@Test
	public void testAddAndGetBid() {

		marketAuction.addBid(item_1_bid_1);
		marketAuction.addBid(item_1_bid_2);

		List<Bid> items = marketAuction
				.getBidForUser(item_1_bid_1.getUserId());
		assertEquals(2, items.size());
		assertEquals(item_1_bid_1, items.get(0));
		assertEquals(item_1_bid_2, items.get(1));
	}

	@Test(expected = NullPointerException.class)
	public void testAddOffer_Null() {
		marketAuction.addBid(null);
	}

	@Test
	public void testAddAndGetOffer() {

		marketAuction.addOffer(item_1_offer_1);
		marketAuction.addOffer(item_1_offer_2);

		List<Offer> offers = marketAuction.getOfferForUser(item_1_offer_1
				.getUserId());
		assertEquals(2, offers.size());
		assertEquals(item_1_offer_1, offers.get(0));
		assertEquals(item_1_offer_2, offers.get(1));
	}

	@Test
	public void testGetBidPrice() {
		marketAuction.addBid(item_1_bid_1);
		marketAuction.addBid(item_1_bid_2);
		marketAuction.addBid(item_1_bid_3);

		Integer bidPrice = marketAuction.getBidPrice(item_1_bid_1.getItemId());
		assertEquals(Integer.valueOf(30), bidPrice);
	}
	
	@Test
	public void testGetBidPrice_No_Item() {
		
		Integer bidPrice = marketAuction.getBidPrice(Integer.valueOf(123456789));
		assertEquals(null, bidPrice);
	}
	
	@Test
	public void testGetOfferPrice() {
		marketAuction.addOffer(item_1_offer_1);
		marketAuction.addOffer(item_1_offer_2);
		marketAuction.addOffer(item_1_offer_3);

		Integer bidPrice = marketAuction.getOfferPrice(item_1_offer_1
				.getItemId());
		assertEquals(Integer.valueOf(5), bidPrice);
	}
	
	@Test
	public void testGetOfferPrice_No_Item() {
		Integer bidPrice = marketAuction.getOfferPrice(Integer.valueOf(123456789));
		assertEquals(null, bidPrice);
	}

	public void testMatches() {

		// bid quanity > offer quanity
		boolean match = marketAuction.matches(item_1_bid_1, item_1_offer_2);
		assertEquals(false, match);

		// match
		match = marketAuction.matches(item_1_bid_1, item_1_offer_2);
		assertEquals(true, match);

		// bid price < offer quanity
		match = marketAuction.matches(item_1_bid_2, item_1_offer_1);
		assertEquals(false, match);
	}

	@Test
	public void testScenario_Spec_Example() {

		// As per example in the spec

		// Bid entered: itemId: 1, quantity: 10, pricePerUnit: 25, user: Buyer
		marketAuction.addBid(item_1_bid_1);
		Integer currentBidPrice = marketAuction.getBidPrice(item_1_bid_1
				.getItemId());
		assertEquals(Integer.valueOf(25), currentBidPrice);

		// Offer entered: itemId: 1, quantity: 5, pricePerUnit: 25, user: Seller
		// not matched as offer quantity < bid quantity
		marketAuction.addOffer(item_1_offer_1);
		currentBidPrice = marketAuction.getBidPrice(item_1_bid_1.getItemId());
		assertEquals(Integer.valueOf(25), currentBidPrice);
		int currentOfferPrice = marketAuction.getOfferPrice(item_1_offer_1
				.getItemId());
		assertEquals(25, currentOfferPrice);

		// Offer entered: itemId: 1, quantity: 10, pricePerUnit: 24, user: Seller
		// matched
		marketAuction.addOffer(item_1_offer_2);
		List<Bid> items = marketAuction
				.getBidForUser(item_1_bid_1.getUserId());
		assertEquals(0, items.size());
		currentBidPrice = marketAuction.getBidPrice(item_1_bid_1.getItemId());
		assertEquals(null, currentBidPrice);
		currentOfferPrice = marketAuction.getOfferPrice(item_1_offer_1
				.getItemId());
		assertEquals(25, currentOfferPrice);

		List<Order> orders = marketAuction.getOrdersForBuyer(item_1_bid_1
				.getUserId());
		assertEquals(1, orders.size());
		Order order = orders.get(0);
		Order expectedOrder = new Order.OrderBuilder()
									.itemID(item_1_bid_1.getItemId())
									.buyerID(item_1_bid_1.getUserId())
									.sellerID(item_1_offer_1.getUserId())
									.pricePerUnit(Integer.valueOf(24))
									.quantity(Integer.valueOf(10)).build();
		isSameOrder(expectedOrder, order);

		orders = marketAuction.getOrdersForSeller(item_1_offer_1.getUserId());
		assertEquals(1, orders.size());
		order = orders.get(0);
		isSameOrder(expectedOrder, order);

		// Bid entered: itemId: 1, quantity: 5, pricePerUnit: 24, user: Buyer
		// not matched as bid price < offer price
		marketAuction.addBid(item_1_bid_2);

		currentBidPrice = marketAuction.getBidPrice(item_1_bid_2.getItemId());
		assertEquals(Integer.valueOf(24), currentBidPrice);
		currentOfferPrice = marketAuction.getOfferPrice(item_1_offer_1
				.getItemId());
		assertEquals(25, currentOfferPrice);

		orders = marketAuction.getOrdersForBuyer(item_1_bid_2.getUserId());
		assertEquals(1, orders.size());
		orders = marketAuction.getOrdersForSeller(item_1_offer_1.getUserId());
		assertEquals(1, orders.size());

	}
	
	private void isSameOrder(Order expected,  Order actual){
		assertEquals(expected.getItemId(), actual.getItemId());
		assertEquals(expected.getPricePerUnit(), actual.getPricePerUnit());
		assertEquals(expected.getQuantity(), actual.getQuantity());
		assertEquals(expected.getBuyerId(), actual.getBuyerId());
		assertEquals(expected.getSellerId(), actual.getSellerId());
	}

	@Test
	public void testScenario_Partial_Fill() {

		// Bid entered: itemId: 1, quantity: 10, pricePerUnit: 25, user: Buyer
		marketAuction.addBid(item_1_bid_1);
		Integer currentBidPrice = marketAuction.getBidPrice(item_1_bid_1
				.getItemId());
		assertEquals(Integer.valueOf(25), currentBidPrice);

		// Offer entered: itemId: 1, quantity: 15, pricePerUnit: 25, user: Buyer
		// //partial fill
		marketAuction.addOffer(item_1_offer_4);

		List<Order> orders = marketAuction.getOrdersForBuyer(item_1_bid_1
				.getUserId());
		assertEquals(1, orders.size());
		Order order = orders.get(0);
		
		Order expectedOrder = new Order.OrderBuilder()
								.itemID(item_1_bid_1.getItemId())
								.buyerID(item_1_bid_1.getUserId())
								.sellerID(item_1_offer_1.getUserId())
								.pricePerUnit(Integer.valueOf(25))
								.quantity(Integer.valueOf(10)).build();
		
		isSameOrder(expectedOrder,order);

		orders = marketAuction.getOrdersForSeller(item_1_offer_4.getUserId());
		assertEquals(1, orders.size());
		order = orders.get(0);
		isSameOrder(expectedOrder,order);

		List<Offer> offers = marketAuction.getOfferForUser(item_1_offer_4
				.getUserId());
		assertEquals(1, offers.size());
		//offer has been reduced
		assertEquals(Integer.valueOf(5), offers.get(0).getQuantity());

	}

	@Test
	public void testShouldOnlyMatchFirstOffer() {
		
		Offer offer_1 = new Offer.OfferBuilder().itemID(1)
				.pricePerUnit(25).quantity(50)
				.userId("Seller").build();

		Offer offer_2 =  new Offer.OfferBuilder().itemID(1)
				.pricePerUnit(25).quantity(100)
				.userId("Seller").build();
		
		Bid bid = new Bid.BidBuilder().itemID(1)
				.pricePerUnit(25).quantity(50)
				.userId("Buyer").build();

		marketAuction.addOffer(offer_1);
		marketAuction.addOffer(offer_2);
		
		marketAuction.addBid(bid);
		
		List<Offer> offers = marketAuction.getOfferForUser(offer_2.getUserId());
		assertEquals(1, offers.size());
		assertEquals(offer_2, offers.get(0));
			
	}
	
	@Test
	public void testShouldOnlyMatchFirstBid() {
		
		Bid bid_1 = new Bid.BidBuilder().itemID(1)
				.pricePerUnit(25).quantity(50)
				.userId("Buyer").build();

		Bid bid_2 = new Bid.BidBuilder().itemID(1)
				.pricePerUnit(25).quantity(100)
				.userId("Buyer").build();
		
		Offer offer = new Offer.OfferBuilder().itemID(1)
				.pricePerUnit(25).quantity(50)
				.userId("Seller").build();

		marketAuction.addBid(bid_1);
		marketAuction.addBid(bid_2);
		
		marketAuction.addOffer(offer);
		
		List<Bid> bids = marketAuction.getBidForUser(bid_2.getUserId());
		assertEquals(1, bids.size());
		assertEquals(bid_2, bids.get(0));
			
	}
}
