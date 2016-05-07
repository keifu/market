package com.auction;

import java.util.List;

public interface MarketplaceCache {

	/**
	 * Add a bid
	 * @param bid
	 */
	public void addBid(Bid bid);
	
	/**
	 * add an offer
	 * @param offer
	 */
	public void addOffer(Offer offer);
	
	/**
	 * Get list of bids for given user ID
	 * @param userId
	 * @return
	 */
	public List<Bid> getBidForUser(String userId);
	
	/**
	 * Get list of bids for item ID
	 * @param userId
	 * @return
	 */
	public List<Bid> getBidForItem(Integer itemID);
	
	/**
	 * Get list of offers for item ID
	 * @param userId
	 * @return
	 */
	public List<Offer> getOfferForItem(Integer itemID);
	
	/**
	 * Get list of offers for given user Id
	 * @param userId
	 * @return
	 */
	public List<Offer> getOfferForUser(String userId);
	
	/**
	 * Add an order
	 * 
	 * @param order
	 */
	public void addOrder(Order order);
	
	/**
	 * Get list of orders for user ID
	 * @param userId
	 * @return
	 */
	public List<Order> getOrdersForSeller(String userId);
	
	/**
	 * Get list of orders user ID
	 * @param userId
	 * @return
	 */
	public List<Order> getOrdersForBuyer(String userId);
	
	/**
	 * Remove offer
	 * 
	 * @param offer
	 */
	public void removeOffer(Offer offer);
	
	/**
	 * Remove bid
	 * 
	 * @param offer
	 */
	public void removeBid(Bid item);
	

}
