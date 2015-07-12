package com.caritos.student_information_system.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeNotifier;
import com.vaadin.ui.Grid;

/**
 * Any filter that needs {@link FieldEvents.TextChangeListener} and
 * {@link Property.ValueChangeListener} may use this implementation. Apply this
 * to
 * {@link TextChangeNotifier#addTextChangeListener(com.vaadin.event.FieldEvents.TextChangeListener)}
 * or
 * {@link ValueChangeNotifier#addValueChangeListener(com.vaadin.data.Property.ValueChangeListener)}
 * component.
 */
public class FilterChangeListenerImpl implements FieldEvents.TextChangeListener, Property.ValueChangeListener {

	private static final long serialVersionUID = 1L;
	private List<Container.Filter> listTextChange;
	private List<Container.Filter> listValueChange;
	private Container.Filterable container;
	private Object propertyId;
	private Grid grid;

//	public FilterChangeListenerImpl(Container.Filterable container, Object propertyId) {
//		listTextChange = new ArrayList<>();
//		listValueChange = new ArrayList<>();
//		this.propertyId = propertyId;
//		this.container = container;
//	}
	
	public FilterChangeListenerImpl(Grid grid, Container.Filterable container, Object propertyId) {
		listTextChange = new ArrayList<>();
		listValueChange = new ArrayList<>();
		this.propertyId = propertyId;
		this.container = container;
		this.grid = grid;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.event.FieldEvents.TextChangeListener#textChange(com.vaadin
	 * .event.FieldEvents.TextChangeEvent)
	 */
	@Override
	public void textChange(TextChangeEvent change) {
		removeFilters(listTextChange);
		if (!change.getText().isEmpty()) {
			Container.Filter currentFilter = new SimpleStringFilter(propertyId, change.getText(), true, false);
			listTextChange.add(currentFilter);
			container.addContainerFilter(currentFilter);
		}
		// fix for defect #51 (grid resize issue)
		grid.recalculateColumnWidths();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin
	 * .data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		removeFilters(listValueChange);
		Object value = event.getProperty().getValue();
		if (value != null) {
			Container.Filter currentFilter = new EntityFilter(propertyId, value);
			listValueChange.add(currentFilter);
			container.addContainerFilter(currentFilter);
		}
		// fix for defect #51 (grid resize issue)
		grid.recalculateColumnWidths();
	}

	private void removeFilters(List<Container.Filter> list) {
		if (!list.isEmpty()) {
			for (Container.Filter currentFilter : list) {
				container.removeContainerFilter(currentFilter);
			}
			list.clear();
		}
	}

}
