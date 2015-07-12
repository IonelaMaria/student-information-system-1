package com.caritos.student_information_system.view.guardian;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.GuardianRepository;
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
@SpringView(GuardianView.VIEW_NAME)
public class GuardianView extends AbstractView<Guardian> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "guardian";
	@Autowired
	OrganizationRepository organizationRepository;
	@Autowired
	GuardianRepository guardianRepository;
	
	public GuardianView() {
		super(ViewTypes.GUARDIAN, "Add Guardian", "Edit Guardian", "Delete Guardian");
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(
				new AddEditGuardianView("Add Guardian", new Guardian()) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void save(Guardian guardian) {
						guardian.setPassword(guardian.generateRandomPassword());
						guardianRepository.save(guardian);
						addGridRow(guardian);
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
				new AddEditGuardianView("Edit Guardian", (Guardian) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Guardian guardian) {
						guardianRepository.save(guardian);
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
						for (Iterator<Object> iter = selected.iterator(); iter.hasNext();) {
							Guardian sel = (Guardian) iter.next();
							guardianRepository.delete(sel);
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
		getGrid().setColumnOrder("firstName", "lastName", "username", "password", "phoneNumber", "balance");
		getGrid().getColumn("firstName").setExpandRatio(1);
		getGrid().getColumn("lastName").setExpandRatio(1);
		getGrid().getColumn("username").setExpandRatio(1);
		getGrid().getColumn("password").setExpandRatio(1);
		getGrid().getColumn("phoneNumber").setExpandRatio(1);
		getGrid().getColumn("balance").setExpandRatio(1);
		
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
		ArrayList<Guardian> guardians = new ArrayList<Guardian>();
		ArrayList<Guardian> guardiansWithUpdatedBalance = new ArrayList<Guardian>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			guardians = Lists.newArrayList(guardianRepository.findByOrganization(organization));
		} else if(user instanceof SuperUser) {
			guardians = Lists.newArrayList(guardianRepository.findAll());
		}
		
		// update the balance column for all the guardians
		for(Guardian guardian: guardians) {
			guardian.setBalance();
			guardiansWithUpdatedBalance.add(guardian);
		}
		
		BeanItemContainer<Guardian> guardianContainer = new BeanItemContainer<>(Guardian.class);
		guardianContainer.addAll(guardiansWithUpdatedBalance);
		return guardianContainer;
	}

}
