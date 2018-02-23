package kh.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * [{"itemId":1,"name":"T-shirt","searchKeywords":"|men|t-shirt|","skuCpu":"12345_23445"},{"itemId":2,"name":"T-shirt","searchKeywords":
 * "|woman|women|t-shirt|","skuCpu":"12345_23446"},
 * {"itemId":3,"name":"T-shirt","searchKeywords":"|kids|kid|t-shirt|","skuCpu":"12345_23447"}]
 */
/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@Table(name="item")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="item_id", unique=true, nullable=false)
	private Integer itemId;

	@Column(nullable=false, length=128)
	private String name;

	@Column(name="search_keywords", nullable=false, length=128)
	private String searchKeywords;

	@Column(name="sku_cpu", nullable=false, length=32)
	private String skuCpu;

	public Item() {
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSearchKeywords() {
		return this.searchKeywords;
	}

	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

	public String getSkuCpu() {
		return this.skuCpu;
	}

	public void setSkuCpu(String skuCpu) {
		this.skuCpu = skuCpu;
	}

}