package com.auction;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MarketplaceCacheImplTest {

	private MarketplaceCache cache;
	private Bid item_1_bid;
	private Offer item_1_offer;
	private Bid item_2_bid;
	private Offer item_2_offer;

	private Order order_1;
	private Order order_2;

	@Before
	public void setup() {
		cache = new MarketplaceCacheImpl();

		// setup test data
		item_1_bid = new Bid.BidBuilder()
						.itemID(1)
						.pricePerUnit(20)
						.quantity(25)
						.userId("Buyer").build();

		item_1_offer = new Offer.OfferBuilder()
						.itemID(1)
						.pricePerUnit(20)
						.quantity(25)
						.userId("Buyer").build();

		item_2_bid = new Bid.BidBuilder()
						.itemID(2)
						.pricePerUnit(30)
						.quantity(25)
						.userId("Buyer").build();
		
		item_2_offer = new Offer.OfferBuilder()
						.itemID(2)
						.pricePerUnit(30)
						.quantity(25)
						.userId("Buyer").build();
		
		order_1 = new Order.OrderBuilder().itemID(1).quantity(25)
							.pricePerUnit(45).buyerID("Buyer").sellerID("Seller").build();

		order_2 = new Order.OrderBuilder().itemID(2).quantity(15)
							.pricePerUnit(25).buyerID("Buyer").sellerID("Seller").build();
	}

	@Test
	public void testAddAndGetBid() {

		cache.addBid(item_1_bid);

		List<Bid> items = cache.getBidForItem(item_1_bid.getItemId());
		assertEquals(1, items.size());
		assertEquals(item_1_bid, items.get(0));

		items = cache.getBidForUser(item_1_bid.getUserId());
		assertEquals(1, items.size());
		assertEquals(item_1_bid, items.get(0));

	}

	@Test
	public void testAddAndGetBid_2() {

		cache.addBid(item_1_bid);
		cache.addBid(item_2_bid);

		List<Bid> items = cache.getBidForItem(item_1_bid.getItemId());
		assertEquals(1, items.size());
		assertEquals(item_1_bid, items.get(0));

		items = cache.getBidForItem(item_2_bid.getItemId());
		assertEquals(1, items.size());
		assertEquals(item_2_bid, items.get(0));

		items = cache.getBidForUser(item_1_bid.getUserId());
		assertEquals(2, items.size());
		assertEquals(item_1_bid, items.get(0));
		assertEquals(item_2_bid, items.get(1));
	}

	@Test
	public void testAddAndGetBid_Same_Item() {

		cache.addBid(item_1_bid);
		cache.addBid(item_1_bid);

		List<Bid> items = cache.getBidForItem(item_1_bid.getItemId());
		assertEquals(2, items.size());
		assertEquals(item_1_bid, items.get(0));
		assertEquals(item_1_bid, items.get(1));

		items = cache.getBidForUser(item_1_bid.getUserId());
		assertEquals(2, items.size());
		assertEquals(item_1_bid, items.get(0));
		assertEquals(item_1_bid, items.get(1));
	}

	@Test
	public void testAddAndGetOffer() {
		cache.addOffer(item_1_offer);

		List<Offer> offers = cache.getOfferForItem(item_1_offer.getItemId());
		assertEquals(1, offers.size());
		assertEquals(item_1_offer, offers.get(0));

		offers = cache.getOfferForUser(item_1_offer.getUserId());
		assertEquals(1, offers.size());
		assertEquals(item_1_offer, offers.get(0));
	}

	@Test
	public void testAddAndGetOffer_2() {
		cache.addOffer(item_1_offer);
		cache.addOffer(item_2_offer);

		List<Offer> offers = cache.getOfferForItem(item_1_offer.getItemId());
		assertEquals(1, offers.size());
		assertEquals(item_1_offer, offers.get(0));

		offers = cache.getOfferForItem(item_2_offer.getItemId());
		assertEquals(1, offers.size());
		assertEquals(item_2_offer, offers.get(0));

		offers = cache.getOfferForUser(item_1_offer.getUserId());
		assertEquals(2, offers.size());
		assertEquals(item_1_offer, offers.get(0));
		assertEquals(item_2_offer, offers.get(1));

	}

	@Test
	public void testAddAndGetOffer_Same_Item() {

		cache.addOffer(item_1_offer);
		cache.addOffer(item_1_offer);

		List<Offer> offers = cache.getOfferForItem(item_1_offer.getItemId());
		assertEquals(2, offers.size());
		assertEquals(item_1_offer, offers.get(0));
		assertEquals(item_1_offer, offers.get(1));
	}

	@Test
	public void testAddOrderAndGetOrder() {
		cache.addOrder(order_1);

		List<Order> orders = cache.getOrdersForBuyer("Buyer");
		assertEquals(1, orders.size());
		assertEquals(order_1, orders.get(0));

		orders = cache.getOrdersForSeller("Seller");
		assertEquals(1, orders.size());
		assertEquals(order_1, orders.get(0));
	}

	@Test
	public void testAddOrderAndGetOrder_2() {
		cache.addOrder(order_1);
		cache.addOrder(order_2);

		List<Order> orders = cache.getOrdersForBuyer("Buyer");
		assertEquals(2, orders.size());
		assertEquals(order_1, orders.get(0));
		assertEquals(order_2, orders.get(1));

		orders = cache.getOrdersForSeller("Seller");
		assertEquals(2, orders.size());
		assertEquals(order_1, orders.get(0));
		assertEquals(order_2, orders.get(1));
	}

	@Test
	public void testRemoveOffer() {
		cache.addOffer(item_1_offer);

		List<Offer> offers = cache.getOfferForItem(item_1_offer.getItemId());
		assertEquals(1, offers.size());
		assertEquals(item_1_offer, offers.get(0));

		offers = cache.getOfferForUser(item_1_offer.getUserId());
		assertEquals(1, offers.size());
		assertEquals(item_1_offer, offers.get(0));

		// remove bid
		cache.removeOffer(item_1_offer);

		offers = cache.getOfferForItem(item_1_offer.getItemId());
		assertEquals(0, offers.size());
		offers = cache.getOfferForUser(item_1_offer.getUserId());
		assertEquals(0, offers.size());
	}

	@Test
	public void testRemoveBid() {

		cache.addBid(item_1_bid);

		List<Bid> items = cache.getBidForItem(item_1_bid.getItemId());
		assertEquals(1, items.size());
		assertEquals(item_1_bid, items.get(0));

		items = cache.getBidForUser(item_1_bid.getUserId());
		assertEquals(1, items.size());
		assertEquals(item_1_bid, items.get(0));

		// remove bid
		cache.removeBid(item_1_bid);

		items = cache.getBidForItem(item_1_bid.getItemId());
		assertEquals(0, items.size());
		items = cache.getBidForUser(item_1_bid.getUserId());
		assertEquals(0, items.size());
	}

}
