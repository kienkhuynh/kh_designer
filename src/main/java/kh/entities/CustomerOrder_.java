package kh.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-25T18:26:53.280-0500")
@StaticMetamodel(CustomerOrder.class)
public class CustomerOrder_ {
	public static volatile SingularAttribute<CustomerOrder, Integer> customerOrderId;
	public static volatile SingularAttribute<CustomerOrder, Integer> customerId;
	public static volatile SingularAttribute<CustomerOrder, Boolean> processStatus;
	public static volatile ListAttribute<CustomerOrder, OrderByItem> orderByItems;
}
