package com.auction;

import java.util.List;

/**
 * In memory cache for storing items
 * 
 * @author Keith
 *
 */
public class MarketplaceCacheImpl implements MarketplaceCache{

	// map of user bids
	private MultiValueMap<String, Bid> userBidMap = new MultiValueMap<>();
	// map of user offers
	private MultiValueMap<String, Offer> userOfferMap = new MultiValueMap<>();
	// map of buyer orders
	private MultiValueMap<String, Order> buyerOrderMap = new MultiValueMap<>();
	// map of seller orders
	private MultiValueMap<String, Order> sellerOrderMap = new MultiValueMap<>();
	// map of item ID -> bid items
	private MultiValueMap<Integer, Bid> bidItemMap = new MultiValueMap<>();
	// map of  item ID -> offer items
	private MultiValueMap<Integer, Offer> offerItemMap = new MultiValueMap<>();
	
	public MarketplaceCacheImpl() {}

	@Override
	public void addBid(Bid bid) {		
		userBidMap.put(bid.getUserId(), bid);		
		bidItemMap.put(bid.getItemId(), bid);	
	}

	@Override
	public void addOffer(Offer offer) {
		userOfferMap.put(offer.getUserId(), offer);
		offerItemMap.put(offer.getItemId(), offer);
	}

	@Override
	public List<Bid> getBidForUser(String userId) {
		return userBidMap.get(userId);
	}

	@Override
	public List<Offer> getOfferForUser(String userId) {
		return userOfferMap.get(userId);

	}
	
	@Override
	public List<Bid> getBidForItem(Integer itemID) {
		return bidItemMap.get(itemID);

	}
	
	@Override
	public List<Offer> getOfferForItem(Integer itemID) {
		return offerItemMap.get(itemID);

	}
		
	@Override
	public void addOrder(Order order) {
		buyerOrderMap.put(order.getBuyerId(), order);
		sellerOrderMap.put(order.getSellerId(), order);
	}

	@Override
	public List<Order> getOrdersForSeller(String userId) {
		return sellerOrderMap.get(userId);
	}

	@Override
	public List<Order> getOrdersForBuyer(String userId) {
		return buyerOrderMap.get(userId);
	}

	@Override
	public void removeOffer(Offer offer) {
		userOfferMap.removeValue(offer.getUserId(), offer);
		offerItemMap.removeValue(offer.getItemId(), offer);
	}

	@Override
	public void removeBid(Bid bid) {
		userBidMap.removeValue(bid.getUserId(), bid);
		bidItemMap.removeValue(bid.getItemId(), bid);
	}

}
