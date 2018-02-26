package kh.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the order_by_item database table.
 * 
 */
@Entity
@Table(name="order_by_item")
@NamedQuery(name="OrderByItem.findAll", query="SELECT o FROM OrderByItem o")
public class OrderByItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="item_order_id", unique=true, nullable=false)
	private Integer itemOrderId;

	private Integer quantity;

	//bi-directional many-to-one association to CustomerOrder
	@ManyToOne(cascade={CascadeType.PERSIST})
	@JoinColumn(name="customer_order_id", nullable=false)
	@JsonIgnore
	private CustomerOrder customerOrder;

	//uni-directional one-to-one association to Inventory
	@OneToOne
	@JoinColumn(name="inventory_id", nullable=false)
	private Inventory inventory;

	public OrderByItem() {
	}

	public Integer getItemOrderId() {
		return this.itemOrderId;
	}

	public void setItemOrderId(Integer itemOrderId) {
		this.itemOrderId = itemOrderId;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public CustomerOrder getCustomerOrder() {
		return this.customerOrder;
	}

	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

}