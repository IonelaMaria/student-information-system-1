package com.caritos.student_information_system.view.organization;

import com.caritos.student_information_system.domain.Organization;
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

public abstract class AddEditOrganizationView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Organization> fieldGroup;

	public AddEditOrganizationView(String caption, Organization organization) {
		super(caption);
		fieldGroup = new BeanFieldGroup<>(Organization.class);
		fieldGroup.setItemDataSource(organization);

	}

	protected abstract void save(Organization organization);

	@Override
	protected AbstractOrderedLayout buildContents() {
		FormLayout layout = new FormLayout();
		layout.setWidth(null);
		layout.setSpacing(true);
		layout.setHeight("100%");

		TextField nameField = fieldGroup.buildAndBind("Organization Name", "name", TextField.class);
		nameField.setNullRepresentation("");
		nameField.setRequired(true);
		nameField.setRequiredError("Name is required!");
		layout.addComponent(nameField);

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
			ViewsUtil.showSuccessNotification("Organization", "Organization saved successfully.");
			UI.getCurrent().removeWindow(AddEditOrganizationView.this);
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
