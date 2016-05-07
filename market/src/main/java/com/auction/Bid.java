package com.auction;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Represents a bid
 * 
 * @author Keith
 *
 */
public class Bid {
	//generates unique id for bids (for simplicity)
	private static AtomicInteger sequenceGenerator = new AtomicInteger();
	
	private final Integer id = sequenceGenerator.incrementAndGet();
	private Integer itemId;
	private Integer quantity;
	private Integer pricePerUnit;
	private String userId;
	
	public Bid(Integer itemId, Integer quantity, Integer pricePerUnit,
			String userId) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		this.userId = userId;
	}
		
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Integer getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Integer pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bid other = (Bid) obj;
		return id.equals(other.id);

	}

	@Override
	public String toString() {
		return "[ID = " + id + ", ItemID = " + itemId + ", PricePerUnit = " + pricePerUnit
				+ ", Quantity = " + quantity + ", userId = " + userId + "]";
	}

	public static class BidBuilder {

		private Integer itemId;
		private Integer quantity;
		private Integer pricePerUnit;
		private String userId;

		public BidBuilder itemID(Integer itemId) {
			this.itemId = itemId;
			return this;
		}

		public BidBuilder quantity(Integer quantity) {
			this.quantity = quantity;
			return this;
		}

		public BidBuilder pricePerUnit(Integer pricePerUnit) {
			this.pricePerUnit = pricePerUnit;
			return this;
		}

		public BidBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Bid build() {
			return new Bid(this.itemId, this.quantity, this.pricePerUnit,
					this.userId);
		}
	}

}
