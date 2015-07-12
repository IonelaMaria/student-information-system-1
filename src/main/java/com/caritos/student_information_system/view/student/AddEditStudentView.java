package com.caritos.student_information_system.view.student;

import java.util.List;

import com.caritos.student_information_system.domain.Grade;
import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.domain.Section;
import com.caritos.student_information_system.domain.Student;
import com.caritos.student_information_system.view.AbstractWindow;
import com.caritos.student_information_system.view.ViewsUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public abstract class AddEditStudentView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Student> fieldGroup;

	public AddEditStudentView(String caption, Student student) {
		super(caption);
		fieldGroup = new BeanFieldGroup<>(Student.class);
		fieldGroup.setItemDataSource(student);

	}

	protected abstract void save(Student student);


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

        ComboBox guardianCombo = new ComboBox("Guardian");
        guardianCombo.setRequired(false);
//        guardianCombo.setRequiredError("Guardian is required!");
        guardianCombo.setItemCaptionPropertyId("name");
        BeanItemContainer<Guardian> dsGuardian= new BeanItemContainer<>(Guardian.class);
        dsGuardian.addAll(getGuardians());
        guardianCombo.setContainerDataSource(dsGuardian);
        fieldGroup.bind(guardianCombo, "guardian");
        layout.addComponent(guardianCombo);

        ComboBox schoolCombo = new ComboBox("School");
        schoolCombo.setRequired(true);
        schoolCombo.setRequiredError("School is required!");
        schoolCombo.setItemCaptionPropertyId("name");

        BeanItemContainer<School> dsOrganization= new BeanItemContainer<>(School.class);
        dsOrganization.addAll(getSchools());
        schoolCombo.setContainerDataSource(dsOrganization);
        fieldGroup.bind(schoolCombo, "school");
        layout.addComponent(schoolCombo);
        
        ComboBox gradeCombo = new ComboBox("Grade");
        gradeCombo.setEnabled(false);
        fieldGroup.bind(gradeCombo, "grade");
        layout.addComponent(gradeCombo);
        
        ComboBox sectionCombo = new ComboBox("Section");
        sectionCombo.setEnabled(false);
        fieldGroup.bind(sectionCombo, "section");
        layout.addComponent(sectionCombo);
        
        schoolCombo.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -3415866279005559460L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				// Look at the selected School 
				// and decide if we need to enable the Grade comboBox
				School  school = (School) event.getProperty().getValue();
				if(school != null && school.getName().length() > 0 && school.getGrades().size() > 0) {
					gradeCombo.setEnabled(true);
					List<Grade> grades = school.getGrades();
					BeanItemContainer<Grade> dsGrade= new BeanItemContainer<>(Grade.class);
					dsGrade.addAll(grades);
					gradeCombo.setContainerDataSource(dsGrade);
					gradeCombo.setItemCaptionPropertyId("name");
				} else {
					gradeCombo.clear();
					gradeCombo.setEnabled(false);
				}
			}
        });
        
        gradeCombo.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6822906434029651194L;
			public void valueChange(ValueChangeEvent event) {
				// Look at the selected Grade 
				// and decide if we need to enable the Section comboBox
				Grade grade = (Grade) event.getProperty().getValue();
				if(grade != null && grade.getName().length() > 0 && grade.getSections().size() > 0) {
					sectionCombo.setEnabled(true);
					List<Section> sections = grade.getSections();
					BeanItemContainer<Section> dsSection= new BeanItemContainer<>(Section.class);
					dsSection.addAll(sections);
					sectionCombo.setContainerDataSource(dsSection);
					sectionCombo.setItemCaptionPropertyId("name");
				} else {
					sectionCombo.clear();
					sectionCombo.setEnabled(false);
				}
			}
		});
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
			UI.getCurrent().removeWindow(AddEditStudentView.this);
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
	
	protected abstract List<School> getSchools();
	protected abstract List<Guardian> getGuardians();
}
