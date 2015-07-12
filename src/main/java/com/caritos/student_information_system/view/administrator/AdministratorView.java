package com.caritos.student_information_system.view.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.AdministratorRepository;
import com.caritos.student_information_system.repository.OrganizationRepository;
import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.FilterChangeListenerImpl;
import com.caritos.student_information_system.view.ViewTypes;
import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@VaadinUIScope
@SpringView(AdministratorView.VIEW_NAME)
public class AdministratorView extends AbstractView<Administrator> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "administrator";
	@Autowired
	private OrganizationRepository organizationRepository;
//	@Autowired
//	private GradeRepository gradeRepository;
	@Autowired
	AdministratorRepository administratorRepository;

	public AdministratorView() {
		super(ViewTypes.ADMINISTRATOR, "Add Administrator", "Edit Administrator", "Delete Administrator");
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(
				new AddEditAdministratorView("Add Administrator", new Administrator()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Administrator administrator) {
						administrator.setPassword(administrator.generateRandomPassword());
						administratorRepository.save(administrator);
						addGridRow(administrator);
					}

					@Override
					protected List<Organization> getOrganizations() {
						return Lists.newArrayList(organizationRepository.findAll());
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
				new AddEditAdministratorView("Edit Administrator", (Administrator) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Administrator administrator) {
						administratorRepository.save(administrator);
						refreshGrid();
					}

					@Override
					protected List<Organization> getOrganizations() {
						return Lists.newArrayList(organizationRepository.findAll());
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
						for (Iterator<Object> iter = selected.iterator(); iter
								.hasNext();) {
							Administrator sel = (Administrator) iter.next();
							administratorRepository.delete(sel);
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
		getGrid().setColumnOrder("firstName", "lastName", "username", "password");
		getGrid().getColumn("firstName").setExpandRatio(1);
		getGrid().getColumn("lastName").setExpandRatio(1);
		getGrid().getColumn("username").setExpandRatio(1);
		getGrid().getColumn("password").setExpandRatio(1);
		
		// if you are an administrator, you are part of one organization
		// hence the organization column is not needed.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			generatedPropertyContainer().removeContainerProperty("organization");
		}
		
		generatedPropertyContainer().removeContainerProperty("id");
		generatedPropertyContainer().removeContainerProperty("name");
		generatedPropertyContainer().removeContainerProperty("email");
		generatedPropertyContainer().removeContainerProperty("ableToLogin");
		generatedPropertyContainer().removeContainerProperty("transactions");
		generatedPropertyContainer().removeContainerProperty("phoneNumber");
		generatedPropertyContainer().removeContainerProperty("fullName");
		generatedPropertyContainer().removeContainerProperty("role");
	}

	@Override
	protected Component createFilterableComponent(Object propertyId, Container.Filterable container) {
		Component comp = null;
		switch (propertyId.toString()) {
		case "firstName":
		case "lastName":
		case "username":
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
		ArrayList<Administrator> administrators = new ArrayList<Administrator>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			administrators = Lists.newArrayList(administratorRepository.findByOrganization(organization));
		} else if(user instanceof SuperUser) {
			administrators = Lists.newArrayList(administratorRepository.findAll());
		}
		
		BeanItemContainer<Administrator> administratorContainer = new BeanItemContainer<>(Administrator.class);
		administratorContainer.addAll(administrators);
		return administratorContainer;
	}
}
