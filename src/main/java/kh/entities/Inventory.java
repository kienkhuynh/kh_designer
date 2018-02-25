package kh.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the inventory database table.
 * 
 */
@Entity
@Table(name="inventory")
@NamedQuery(name="Inventory.findAll", query="SELECT i FROM Inventory i")
public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="inventory_id", unique=true, nullable=false)
	private Integer inventoryId;

	@Column(name="min_quantity")
	private Integer minQuantity;

	@Column(name="original_price", nullable=false)
	private double originalPrice;

	private Integer quantity;

	@Column(name="sale_price", nullable=false)
	private double salePrice;

	@Column(name="style_code", length=32)
	private String styleCode;

	//bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name="item_id", nullable=false)
	@JsonIgnore
	private Item item;

	//bi-directional many-to-one association to OrderByItem
	@OneToMany(mappedBy="inventory", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<OrderByItem> orderByItems;

	public Inventory() {
	}

	public Integer getInventoryId() {
		return this.inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Integer getMinQuantity() {
		return this.minQuantity;
	}

	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

	public double getOriginalPrice() {
		return this.originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getSalePrice() {
		return this.salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getStyleCode() {
		return this.styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderByItem> getOrderByItems() {
		return this.orderByItems;
	}

	public void setOrderByItems(List<OrderByItem> orderByItems) {
		this.orderByItems = orderByItems;
	}

	public OrderByItem addOrderByItem(OrderByItem orderByItem) {
		getOrderByItems().add(orderByItem);
		orderByItem.setInventory(this);

		return orderByItem;
	}

	public OrderByItem removeOrderByItem(OrderByItem orderByItem) {
		getOrderByItems().remove(orderByItem);
		orderByItem.setInventory(null);

		return orderByItem;
	}

}