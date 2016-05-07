package com.auction;

import java.util.List;

/**
 * Contract for a market place auction
 * 
 * @author Keith
 *
 */
public interface MarketplaceAuction {
	
	/**
	 * Add a bid
	 * @param bid
	 */
	public void addBid(Bid bid);
	
	/**
	 * add an offer
	 * @param offer
	 */
	public void addOffer(Offer bid);
	
	/**
	 * Get list of bids for given user ID
	 * @param userId
	 * @return
	 */
	public List<Bid> getBidForUser(String userId);
	
	/**
	 * Get list of offers for given user ID
	 * @param userId
	 * @return
	 */
	public List<Offer> getOfferForUser(String userId);
	
	/**
	 * Get list of orders for user ID
	 * @param userId
	 * @return
	 */
	public List<Order> getOrdersForSeller(String userId);
	
	/**
	 * Get list of orders for user ID
	 * @param userId
	 * @return
	 */
	public List<Order> getOrdersForBuyer(String userId);
	
	/**
	 * Get bid price for item ID
	 * @param itemId
	 * @return
	 */
	public Integer getBidPrice(Integer itemId);
	
	/**
	 * Get offer price for item ID
	 * @param itemId
	 * @return
	 */
	public Integer getOfferPrice(Integer itemId);
}
