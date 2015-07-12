package com.caritos.student_information_system.view;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * Will be using this mainly for entities that are filterable. Any given entity
 * must override {@link Object#equals(Object)} and {@link Object#hashCode()} to
 * work.
 */
public class EntityFilter implements Container.Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object propertyId;
	private Object selectedValue;

	public EntityFilter(Object propertyId, Object selectedValue) {
		this.propertyId = propertyId;
		this.selectedValue = selectedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.Container.Filter#passesFilter(java.lang.Object,
	 * com.vaadin.data.Item)
	 */
	@Override
	public boolean passesFilter(Object itemId, Item item)
			throws UnsupportedOperationException {
		final Property<?> p = item.getItemProperty(propertyId);
		if (p == null) {
			return false;
		}
		Object propertyValue = p.getValue();
		if (propertyValue == null || selectedValue == null) {
			return false;
		}
		return propertyValue.equals(selectedValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.Container.Filter#appliesToProperty(java.lang.Object)
	 */
	@Override
	public boolean appliesToProperty(Object propertyId) {
		return this.propertyId.equals(propertyId);
	}
}
