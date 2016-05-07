package com.auction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * Implementation of a market place auction
 * 
 * @author Keith
 *
 */
public class MarketplaceAuctionImpl implements MarketplaceAuction {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	private final MarketplaceCache cache;
	
	private Lock lock;

	public MarketplaceAuctionImpl() {
		lock = new ReentrantLock();
		cache = new MarketplaceCacheImpl();
	}

	@Override
	public void addBid(Bid bid) {
		
		try{
			lock.lock();
		
			logger.info("Bid entered: " + bid);
			
			cache.addBid(bid);

			attemptToMatchBid(bid);
			
		}finally{
			lock.unlock();
		}
	
	}

	/**
	 * Attempt to match the bid. If bid is match, an order is placed
	 * 
	 * @return true if matched, false otherwise
	 */
	private void attemptToMatchBid(Bid bid) {

		List<Offer> offerList = cache.getOfferForItem(bid.getItemId());

		// see if it matches any offers
		for (Offer offer : offerList) {
			if (matches(bid, offer)) {
				//bid is matched, so place order
				placeOrder(bid, offer);
				//bid is matched and order placed. No need to match other offers
				return;
			}
		}

	}

	private  void placeOrder(Bid bid, Offer offer) {
				
		Integer orderPrice = Math.min(bid.getPricePerUnit(),offer.getPricePerUnit());

		Order order = new Order.OrderBuilder()
							   .itemID(bid.getItemId())
							   .quantity(bid.getQuantity())
							   .pricePerUnit(orderPrice)
							   .buyerID(bid.getUserId())
							   .sellerID(offer.getUserId()).build();
		// add order
		cache.addOrder(order);
		// remove bid
		cache.removeBid(bid);
		logger.info("Bid removed: " + bid);

		if (bid.getQuantity() == offer.getQuantity()) {
			// offer quantity reduced to 0, so remove
			cache.removeOffer(offer);
			logger.info("Offer removed: " + offer);
		} else {
			// reduce quantity
			offer.setQuantity(offer.getQuantity() - bid.getQuantity());
			logger.info("Offer reduced: " + offer);
		}
		
		logger.info("Placed order: " + order);
	}

	/**
	 * Whether bid and offer match
	 * 
	 * @param bid
	 * @param offer
	 * @return
	 */
	boolean matches(Bid bid, Offer offer) {
		return bid.getItemId().equals(offer.getItemId())
				&& bid.getPricePerUnit() >= offer.getPricePerUnit()
				&& bid.getQuantity() <= offer.getQuantity();
	}

	@Override
	public void addOffer(Offer offer) {
				
		try{
			lock.lock();

			logger.info("Offer entered: " + offer);
			
			cache.addOffer(offer);

			attemptToMatchOffer(offer);
		}finally{
			lock.unlock();
		}
	}

	/**
	 * Attempt to match the offer. If offer is matched, an order is placed
	 * 
	 * @return true if matched, false otherwise
	 */
	private boolean attemptToMatchOffer(Offer offer) {

		List<Bid> bidList = cache.getBidForItem(offer.getItemId());

		// see if it matches any bid items
		for (Bid bidItem : bidList) {
			if (matches(bidItem, offer)) {
				//offer is match, placed order
				placeOrder(bidItem, offer);
				//offer is matched and order placed. No need to match other bids
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Bid> getBidForUser(String userId) {
		return cache.getBidForUser(userId);
	}

	@Override
	public List<Offer> getOfferForUser(String userId) {
		return cache.getOfferForUser(userId);
	}

	@Override
	public List<Order> getOrdersForSeller(String userId) {
		return cache.getOrdersForSeller(userId);
	}

	@Override
	public List<Order> getOrdersForBuyer(String userId) {
		return cache.getOrdersForBuyer(userId);
	}

	@Override
	public Integer getBidPrice(Integer itemId) {

		List<Bid> bidList = cache.getBidForItem(itemId);

	    Optional<Bid> op = bidList
	              			.stream()    
	              			.max((bid1, bid2)
	              					-> Integer.compare(bid1.getPricePerUnit(), bid2.getPricePerUnit()));

		Integer bidPrice = null;
		if(op.isPresent()){
			bidPrice = op.get().getPricePerUnit();
		}
		return bidPrice;
	}

	@Override
	public Integer getOfferPrice(Integer itemId) {

		List<Offer> offerList = cache.getOfferForItem(itemId);

		Optional<Offer> op = offerList
	              			.stream()    
	              			.min((offer1, offer2)
	              					-> Integer.compare(offer1.getPricePerUnit(), offer2.getPricePerUnit()));

		Integer offerPrice = null;
		if(op.isPresent()){
			offerPrice = op.get().getPricePerUnit();
		}
		return offerPrice;
	}
	
}
