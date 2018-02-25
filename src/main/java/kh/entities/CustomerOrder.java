package kh.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="customer_order_code", nullable=false, length=32)
	private String customerOrderCode;

	@Column(name="process_status")
	private Boolean processStatus;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="customer_id")
	@JsonIgnore
	private Customer customer;

	//bi-directional many-to-one association to OrderByItem
	@OneToMany(mappedBy="customerOrder", fetch=FetchType.EAGER)
	private List<OrderByItem> orderByItems;

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

	public Boolean getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(Boolean processStatus) {
		this.processStatus = processStatus;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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