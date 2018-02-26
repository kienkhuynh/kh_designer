package kh.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


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
	@Column(name="customer_order_id", unique=true, nullable=false)
	private Integer customerOrderId;

	@Column(name="customer_id")
	private Integer customerId;

	@Column(name="process_status")
	private Boolean processStatus;

	//bi-directional many-to-one association to OrderByItem
	@OneToMany(mappedBy="customerOrder", cascade={CascadeType.PERSIST}, fetch=FetchType.EAGER)
	private List<OrderByItem> orderByItems;

	public CustomerOrder() {
	}

	public Integer getCustomerOrderId() {
		return this.customerOrderId;
	}

	public void setCustomerOrderId(Integer customerOrderId) {
		this.customerOrderId = customerOrderId;
	}

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Boolean getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(Boolean processStatus) {
		this.processStatus = processStatus;
	}

	public List<OrderByItem> getOrderByItems() {
		return this.orderByItems;
	}

	public void setOrderByItems(List<OrderByItem> orderByItems) {
		this.orderByItems = orderByItems;
	}

	public OrderByItem addOrderByItem(OrderByItem orderByItem) {
		getOrderByItems().add(orderByItem);
		orderByItem.setCustomerOrder(this);

		return orderByItem;
	}

	public OrderByItem removeOrderByItem(OrderByItem orderByItem) {
		getOrderByItems().remove(orderByItem);
		orderByItem.setCustomerOrder(null);

		return orderByItem;
	}

}