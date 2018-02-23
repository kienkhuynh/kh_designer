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

	@Column(name="item_id", nullable=false)
	private Integer itemId;

	@Column(name="order_date", nullable=false)
	private Timestamp orderDate;

	private Integer quantity;

	@Column(name="style_code", nullable=false)
	private Integer styleCode;

	public OrderByItem() {
	}

	public Integer getItemOrderId() {
		return this.itemOrderId;
	}

	public void setItemOrderId(Integer itemOrderId) {
		this.itemOrderId = itemOrderId;
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getStyleCode() {
		return this.styleCode;
	}

	public void setStyleCode(Integer styleCode) {
		this.styleCode = styleCode;
	}

}