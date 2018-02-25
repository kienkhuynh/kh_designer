package kh.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


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

	//bi-directional many-to-one association to Inventory
	@OneToMany(mappedBy="item", cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	private List<Inventory> inventories;

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

	public List<Inventory> getInventories() {
		return this.inventories;
	}

	public void setInventories(List<Inventory> inventories) {
		this.inventories = inventories;
	}

	public Inventory addInventory(Inventory inventory) {
		getInventories().add(inventory);
		inventory.setItem(this);

		return inventory;
	}

	public Inventory removeInventory(Inventory inventory) {
		getInventories().remove(inventory);
		inventory.setItem(null);

		return inventory;
	}

}