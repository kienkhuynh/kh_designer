package kh.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-25T18:24:45.721-0500")
@StaticMetamodel(Inventory.class)
public class Inventory_ {
	public static volatile SingularAttribute<Inventory, Integer> inventoryId;
	public static volatile SingularAttribute<Inventory, Double> originalPrice;
	public static volatile SingularAttribute<Inventory, Integer> quantity;
	public static volatile SingularAttribute<Inventory, Double> salePrice;
	public static volatile SingularAttribute<Inventory, String> styleCode;
	public static volatile SingularAttribute<Inventory, Item> item;
}
