package com.caritos.student_information_system.view.superuser;

import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.view.AbstractWindow;
import com.caritos.student_information_system.view.ViewsUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public abstract class AddEditSuperUserView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<SuperUser> fieldGroup;

	public AddEditSuperUserView(String caption, SuperUser superUser) {
		super(caption);
		fieldGroup = new BeanFieldGroup<>(SuperUser.class);
		fieldGroup.setItemDataSource(superUser);

	}

	protected abstract void save(SuperUser superUser);

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
			ViewsUtil.showSuccessNotification("SuperUser", "SuperUser saved successfully.");
			UI.getCurrent().removeWindow(AddEditSuperUserView.this);
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
}
