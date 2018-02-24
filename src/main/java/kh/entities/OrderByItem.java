package kh.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


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

	@Column(name="inventory_id", nullable=false)
	private Integer inventoryId;

	@Column(name="order_date", nullable=false)
	private Timestamp orderDate;

	private Integer quantity;

	//bi-directional many-to-one association to CustomerOrder
	@ManyToOne
	@JoinColumn(name="item_order_id", referencedColumnName="item_order_id", nullable=false, insertable=false, updatable=false)
	private CustomerOrder customerOrder;

	public OrderByItem() {
	}

	public Integer getItemOrderId() {
		return this.itemOrderId;
	}

	public void setItemOrderId(Integer itemOrderId) {
		this.itemOrderId = itemOrderId;
	}

	public Integer getInventoryId() {
		return this.inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Timestamp getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
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

}