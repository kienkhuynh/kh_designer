package kh.entities;

import java.io.Serializable;
import javax.persistence.*;


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

	@Column(name="orignal_price", nullable=false)
	private double orignalPrice;

	private Integer quantity;

	@Column(name="sale_price", nullable=false)
	private double salePrice;

	@Column(name="style_id", nullable=false)
	private Integer styleId;

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

	public double getOrignalPrice() {
		return this.orignalPrice;
	}

	public void setOrignalPrice(double orignalPrice) {
		this.orignalPrice = orignalPrice;
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

	public Integer getStyleId() {
		return this.styleId;
	}

	public void setStyleId(Integer styleId) {
		this.styleId = styleId;
	}

}