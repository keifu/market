package com.auction;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an order in a market auction
 * 
 * @author Keith
 *
 */
public class Order {
	//generates unique id for order(for simplicity)
	private static AtomicInteger sequenceGenerator = new AtomicInteger();
	
	private final Integer id = sequenceGenerator.incrementAndGet();
	private final Integer itemId;
	private final Integer quantity;
	private final Integer pricePerUnit;
	private final String buyerId;
	private final String sellerId;
	
	public Order(Integer itemId, Integer quantity, Integer pricePerUnit, String buyerId, String sellerId){
		this.itemId = Objects.requireNonNull(itemId);
		this.quantity = Objects.requireNonNull(quantity);
		this.pricePerUnit = Objects.requireNonNull(pricePerUnit);
		this.buyerId = Objects.requireNonNull(buyerId);
		this.sellerId = Objects.requireNonNull(sellerId);
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Integer getPricePerUnit() {
		return pricePerUnit;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public String getSellerId() {
		return sellerId;
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
		Order other = (Order) obj;
		return id.equals(other.id);

	}

	@Override
	public String toString() {
		
		return "[ID = " + id + ", ItemID = " + itemId + ", PricePerUnit = " + pricePerUnit
				+ ", Quantity = " + quantity + ", buyerId = " + buyerId
				+ ", sellerID = " + sellerId + "]";
	}
	
	public static class OrderBuilder {

		private Integer itemId;
		private Integer quantity;
		private Integer pricePerUnit;
		private String buyerId;
		private String sellerId;

		public OrderBuilder itemID(Integer itemId) {
			this.itemId = itemId;
			return this;
		}

		public OrderBuilder quantity(Integer quantity) {
			this.quantity = quantity;
			return this;
		}

		public OrderBuilder pricePerUnit(Integer pricePerUnit) {
			this.pricePerUnit = pricePerUnit;
			return this;
		}

		public OrderBuilder buyerID(String buyerId) {
			this.buyerId = buyerId;
			return this;
		}
		
		public OrderBuilder sellerID(String sellerId) {
			this.sellerId = sellerId;
			return this;
		}

		public Order build() {
			return new Order(this.itemId, this.quantity, this.pricePerUnit, this.buyerId, this.sellerId);
		}
	}
}
