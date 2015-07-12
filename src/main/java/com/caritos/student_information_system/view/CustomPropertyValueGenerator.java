package com.caritos.student_information_system.view;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.filter.UnsupportedFilterException;

/**
 * {@link PropertyValueGenerator#modifyFilter(Filter)} defaults to throw
 * {@link UnsupportedFilterException} if a property is a generator. Custom
 * filter and a subclass of {@link PropertyValueGenerator} is created to
 * override {@link PropertyValueGenerator#modifyFilter(Filter)} to return the
 * correct filter.
 */
public abstract class CustomPropertyValueGenerator<T> extends
		PropertyValueGenerator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Filter modifyFilter(Filter filter) throws UnsupportedFilterException {
		return filter;
	}
}
