package kh.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the customer_order database table.
 * 
 */
@Entity
@Table(name="customer_order")
@NamedQuery(name="CustomerOrder.findAll", query="SELECT c FROM CustomerOrder c")
public class CustomerOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="customer_order_code", nullable=false, length=32)
	private String customerOrderCode;

	@Column(name="item_order_id", nullable=false)
	private Integer itemOrderId;

	public CustomerOrder() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerOrderCode() {
		return this.customerOrderCode;
	}

	public void setCustomerOrderCode(String customerOrderCode) {
		this.customerOrderCode = customerOrderCode;
	}

	public Integer getItemOrderId() {
		return this.itemOrderId;
	}

	public void setItemOrderId(Integer itemOrderId) {
		this.itemOrderId = itemOrderId;
	}

}