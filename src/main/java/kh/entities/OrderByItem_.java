package kh.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-25T18:24:45.722-0500")
@StaticMetamodel(OrderByItem.class)
public class OrderByItem_ {
	public static volatile SingularAttribute<OrderByItem, Integer> itemOrderId;
	public static volatile SingularAttribute<OrderByItem, Integer> quantity;
	public static volatile SingularAttribute<OrderByItem, CustomerOrder> customerOrder;
	public static volatile SingularAttribute<OrderByItem, Inventory> inventory;
}
