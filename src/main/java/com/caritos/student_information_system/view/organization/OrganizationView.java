package com.caritos.student_information_system.view.organization;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.repository.OrganizationRepository;
import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.FilterChangeListenerImpl;
import com.caritos.student_information_system.view.ViewTypes;
import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@VaadinUIScope
@SpringView(OrganizationView.VIEW_NAME)
public class OrganizationView extends AbstractView<Organization> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "organization";
	@Autowired
	private OrganizationRepository organizationRepository;


	public OrganizationView() {
		super(ViewTypes.ORGANIZATION, "Add Organization", "Edit Organization", "Delete Organization");
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(
				// TODO wrong, should be Student not Administrator
				new AddEditOrganizationView("Add Organization", new Organization()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Organization organization) {
						organizationRepository.save(organization);
						addGridRow(organization);
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractView#editEvent(com
	 * .vaadin.ui.Button.ClickEvent)
	 */
	@Override
	protected void editEvent(ClickEvent event) {
		Collection<Object> selected = getGrid().getSelectedRows();
		if (selected == null || selected.isEmpty()) {
			return;
		}
		UI.getCurrent().addWindow(
				new AddEditOrganizationView("Edit Organization", (Organization) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Organization organization) {
						organizationRepository.save(organization);
						refreshGrid();
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractView#deleteEvent(
	 * com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	protected void deleteEvent(ClickEvent event) {
		Collection<Object> selected = getGrid().getSelectedRows();
		if (selected == null || selected.isEmpty()) {
			return;
		}
		ConfirmDialog.show(UI.getCurrent(), "Delete", "Delete selected organization(s)?", "Delete", "Cancel", (ConfirmDialog dialog) -> {
					if (dialog.isConfirmed()) {
						for (Iterator<Object> iter = selected.iterator(); iter.hasNext();) {
							Organization sel = (Organization) iter.next();
							organizationRepository.delete(sel);
							removeGridRow(sel);
						}
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractView#configureGrid
	 * (com.vaadin.ui.Grid)
	 */
	@Override
	protected void configureGrid() {
		getGrid().setColumnOrder("name");
		getGrid().getColumn("name").setExpandRatio(1);
		generatedPropertyContainer().removeContainerProperty("id");
	}

	@Override
	protected Component createFilterableComponent(Object propertyId, Container.Filterable container) {
		Component comp = null;
		switch (propertyId.toString()) {
		case "name":
			TextField textField = new TextField();
			styleFilterTextField(textField);
			textField.setImmediate(true);
			textField.setColumns(8);
			textField.addTextChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = textField;
			break;
		}
		return comp;
	}

	@Override
	protected Indexed createContainer() {
		
		BeanItemContainer<Organization> organizationContainer = new BeanItemContainer<>(Organization.class);
		organizationContainer.addAll(Lists.newArrayList(organizationRepository.findAll()));
		return organizationContainer;
	}

}
