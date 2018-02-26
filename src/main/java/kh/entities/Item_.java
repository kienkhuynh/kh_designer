package kh.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-25T18:24:45.722-0500")
@StaticMetamodel(Item.class)
public class Item_ {
	public static volatile SingularAttribute<Item, Integer> itemId;
	public static volatile SingularAttribute<Item, String> name;
	public static volatile SingularAttribute<Item, String> searchKeywords;
	public static volatile ListAttribute<Item, Inventory> inventories;
}
