package com.caritos.student_information_system.view.school;

import java.util.List;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Category;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.School;
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

public abstract class AddEditSchoolView extends AbstractWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<School> fieldGroup;

	public AddEditSchoolView(String caption, School school) {
		super(caption);
		
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			school.setOrganization(organization);
		}
		
		fieldGroup = new BeanFieldGroup<>(School.class);
		fieldGroup.setItemDataSource(school);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractWindow#buildContents
	 * ()
	 */
	@Override
	protected AbstractOrderedLayout buildContents() {

		FormLayout layout = new FormLayout();
		layout.setWidth(null);
		layout.setSpacing(true);

		TextField nameField = fieldGroup.buildAndBind("School Name", "name",
				TextField.class);
		nameField.setNullRepresentation("");
		nameField.setRequired(true);
		nameField.setRequiredError("School name is required!");
		layout.addComponent(nameField);
		
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
		// ListSelect categoryList = new ListSelect("Categories");
		// categoryList.setItemCaptionPropertyId("name");
		// categoryList.setMultiSelect(true);
		// categoryList.setWidth("100%");
		// BeanItemContainer<Category> ds = new
		// BeanItemContainer<>(Category.class);
		// ds.addAll(getCategories());
		// categoryList.setContainerDataSource(ds);
		// fieldGroup.bind(categoryList, "categories");
		// layout.addComponent(categoryList);
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
			ViewsUtil.showSuccessNotification("School",
					"School saved successfully.");
			UI.getCurrent().removeWindow(AddEditSchoolView.this);
		} catch (CommitException e) {
			Notification.show("Pls fill up the required fields.",
					Type.WARNING_MESSAGE);
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

	protected abstract List<Category> getCategories();

	protected abstract void save(School school);
    protected abstract List<Organization> getOrganizations();
}
