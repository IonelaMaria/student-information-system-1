package com.caritos.student_information_system.view.administrator;

import java.util.List;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.view.AbstractWindow;
import com.caritos.student_information_system.view.ViewsUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public abstract class AddEditAdministratorView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Administrator> fieldGroup;

	public AddEditAdministratorView(String caption, Administrator administrator) {
		super(caption);
		
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			administrator.setOrganization(organization);
		}
		
		fieldGroup = new BeanFieldGroup<>(Administrator.class);
		fieldGroup.setItemDataSource(administrator);

	}

	protected abstract void save(Administrator administrator);

	@Override
	protected AbstractOrderedLayout buildContents() {
		FormLayout layout = new FormLayout();
		layout.setWidth(null);
		layout.setSpacing(true);
		layout.setHeight("100%");

		TextField firstNameField = fieldGroup.buildAndBind("First Name", "firstName", TextField.class);
		firstNameField.setNullRepresentation("");
		firstNameField.setRequired(true);
		firstNameField.setRequiredError("First name is required!");
		layout.addComponent(firstNameField);

		TextField lastNameField = fieldGroup.buildAndBind("Last Name", "lastName", TextField.class);
		lastNameField.setNullRepresentation("");
		lastNameField.setRequired(true);
		lastNameField.setRequiredError("Last name is required!");
		layout.addComponent(lastNameField);

		TextField userNameField = fieldGroup.buildAndBind("Username", "username", TextField.class);
		userNameField.setNullRepresentation("");
		userNameField.setRequired(true);
		userNameField.setRequiredError("Username is required!");
		layout.addComponent(userNameField);
		
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof SuperUser) {
	        ComboBox organizationCombo = new ComboBox("Organization");
	        organizationCombo.setRequired(true);
	        organizationCombo.setRequiredError("Organization is required!");
	        organizationCombo.setItemCaptionPropertyId("name");

	        BeanItemContainer<Organization> dsOrganization= new BeanItemContainer<>(Organization.class);
	        dsOrganization.addAll(getOrganizations());
	        organizationCombo.setContainerDataSource(dsOrganization);
	        fieldGroup.bind(organizationCombo, "organization");
	        layout.addComponent(organizationCombo);
	        
		} 
		return layout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractWindow#okEvent(com
	 * .vaadin.ui.Button.ClickEvent)
	 */
	@Override
	protected void okEvent(ClickEvent event) {
		try {
			fieldGroup.commit();
			save(fieldGroup.getItemDataSource().getBean());
			ViewsUtil.showSuccessNotification("User", "User saved successfully.");
			UI.getCurrent().removeWindow(AddEditAdministratorView.this);
		} catch (CommitException e) {
			Notification.show("Pls fill up the required fields.", Type.WARNING_MESSAGE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractWindow#cancelEvent
	 * (com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	protected void cancelEvent(ClickEvent event) {

	}
	
    protected abstract List<Organization> getOrganizations();
}
