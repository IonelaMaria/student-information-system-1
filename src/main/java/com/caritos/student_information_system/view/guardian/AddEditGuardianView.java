package com.caritos.student_information_system.view.guardian;

import java.util.List;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Guardian;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public abstract class AddEditGuardianView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Guardian> fieldGroup;

	public AddEditGuardianView(String caption, Guardian guardian) {
		super(caption);
		
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			guardian.setOrganization(organization);
		}
		
		fieldGroup = new BeanFieldGroup<>(Guardian.class);
		fieldGroup.setItemDataSource(guardian);

	}

	protected abstract void save(Guardian guardian);
	
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
		
		TextField usernameField = fieldGroup.buildAndBind("Username", "username", TextField.class);
		usernameField.setNullRepresentation("");
		usernameField.setRequired(true);
		usernameField.setRequiredError("Username is required!");
		layout.addComponent(usernameField);
		
		TextField phoneNumberField = fieldGroup.buildAndBind("Phone Number", "phoneNumber", TextField.class);
		phoneNumberField.setNullRepresentation("");
		phoneNumberField.setRequired(false);
		layout.addComponent(phoneNumberField);
		
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
			ViewsUtil.showSuccessNotification("Guardian", "Guardian saved successfully.");
			UI.getCurrent().removeWindow(AddEditGuardianView.this);
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
