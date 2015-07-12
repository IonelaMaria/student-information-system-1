package com.caritos.student_information_system.view.superuser;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.SuperUserRepository;
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
@SpringView(SuperUserView.VIEW_NAME)
public class SuperUserView extends AbstractView<SuperUser> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "superuser";
	@Autowired
	SuperUserRepository superUserRepository;

	public SuperUserView() {
		super(ViewTypes.SUPERUSER, "Add SuperUser", "Edit SuperUser", "Delete SuperUser");
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(
				new AddEditSuperUserView("Add SuperUser", new SuperUser()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void save(SuperUser superUser) {
						superUser.setPassword(superUser.generateRandomPassword());
						superUserRepository.save(superUser);
						addGridRow(superUser);
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
				new AddEditSuperUserView("Edit SuperUser", (SuperUser) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(SuperUser superUser) {
						superUserRepository.save(superUser);
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
		ConfirmDialog.show(UI.getCurrent(), "Delete", "Delete selected student(s)?", "Delete", "Cancel", (
						ConfirmDialog dialog) -> {
					if (dialog.isConfirmed()) {
						for (Iterator<Object> iter = selected.iterator(); iter.hasNext();) {
							SuperUser superUser = (SuperUser) iter.next();
							superUserRepository.delete(superUser);
							removeGridRow(superUser);
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
		getGrid().setColumnOrder("firstName", "lastName", "username", "password");
		getGrid().getColumn("firstName").setExpandRatio(1);
		getGrid().getColumn("lastName").setExpandRatio(1);
		getGrid().getColumn("username").setExpandRatio(1);
		getGrid().getColumn("password").setExpandRatio(1);
		generatedPropertyContainer().removeContainerProperty("id");
		generatedPropertyContainer().removeContainerProperty("organization");
		generatedPropertyContainer().removeContainerProperty("phoneNumber");
		generatedPropertyContainer().removeContainerProperty("school");
		generatedPropertyContainer().removeContainerProperty("fullName");
		generatedPropertyContainer().removeContainerProperty("name");
		generatedPropertyContainer().removeContainerProperty("students");
	}

	@Override
	protected Component createFilterableComponent(Object propertyId, Container.Filterable container) {
		Component comp = null;
		switch (propertyId.toString()) {
		case "firstName":
		case "lastName":
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
		BeanItemContainer<SuperUser> superUserContainer = new BeanItemContainer<>(SuperUser.class);
		superUserContainer.addAll(Lists.newArrayList(superUserRepository.findAll()));
		return superUserContainer;
	}

}
