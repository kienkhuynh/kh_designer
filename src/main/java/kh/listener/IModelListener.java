package kh.listener;

/*
 * Interface to model changes
 * 
 * @author kh
 *
 * @param <T>: The model type
 */
public interface IModelListener<T> {
	
	/**
	 * Called when object is updated.
	 * 
	 * @param object the object being updated
	 */
	public void updated(T object);
	
	/**
	 * Called when object is deleted.
	 * 
	 * @param object the object being deleted
	 */
	public void deleted(T object);
}
