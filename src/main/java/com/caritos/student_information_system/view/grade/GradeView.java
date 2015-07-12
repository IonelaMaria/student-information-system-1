package com.caritos.student_information_system.view.grade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Grade;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.GradeRepository;
import com.caritos.student_information_system.repository.SchoolRepository;
import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.CustomPropertyValueGenerator;
import com.caritos.student_information_system.view.ViewTypes;
import com.google.common.collect.Lists;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

/**
 * TODO remove category menu item if not logged in as Administrator or SuperUser
 *
 * Created by eladio on 4/9/15.
 */
@VaadinUIScope
@SpringView(GradeView.VIEW_NAME)
public class GradeView extends AbstractView<Grade> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "grade";
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private GradeRepository gradeRepository;
	
    public GradeView() {
    	super(ViewTypes.GRADE, "Add Grade", "Edit Grade", "Delete Grade");
    }

	@Override
	protected void configureGrid() {
		getGrid().setColumnOrder("name", "school");
		getGrid().getColumn("name").setExpandRatio(1);
		getGrid().getColumn("school").setExpandRatio(1);
		generatedPropertyContainer().removeContainerProperty("gradeId");
		generatedPropertyContainer().removeContainerProperty("sections");
		generatedPropertyContainer().removeContainerProperty("students");
		generatedPropertyContainer().addGeneratedProperty("school", 
				new CustomPropertyValueGenerator<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						Grade grade = (Grade) itemId;
						School school = schoolRepository.findOne(grade.getSchool().getId());
						return school != null ? school.getName() : "-";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
		});
	}

	@Override
	protected Indexed createContainer() {
		ArrayList<Grade> grades = new ArrayList<Grade>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				grades.addAll(school.getGrades());
			}
		} else if(user instanceof SuperUser) {
			grades = Lists.newArrayList(gradeRepository.findAll());
		}
		
		BeanItemContainer<Grade> gradeContainer = new BeanItemContainer<>(Grade.class, grades);
		return gradeContainer;
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(new AddEditGradeView("Add Grade", new Grade()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<School> getSchools() {
				return Lists.newArrayList(schoolRepository.findAll());
			}

			@Override
			protected void save(Grade grade) {
				gradeRepository.save(grade);
				addGridRow(grade);
			}
		});
	}

	@Override
	protected void editEvent(ClickEvent event) {
		Collection<Object> selected = getGrid().getSelectedRows();
		if (selected == null || selected.isEmpty()) {
			return;
		}
		UI.getCurrent().addWindow(
				new AddEditGradeView("Edit Category", (Grade) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected List<School> getSchools() {
						return Lists.newArrayList(schoolRepository.findAll());
					}

					@Override
					protected void save(Grade category) {
						gradeRepository.save(category);
						refreshGrid();
					}
				});
	}

	@Override
	protected void deleteEvent(ClickEvent event) {
		Collection<Object> selected = getGrid().getSelectedRows();
		if (selected == null || selected.isEmpty()) {
			return;
		}

		ConfirmDialog.show(UI.getCurrent(), "Delete",
				"Delete selected grade(s)?", "Delete", "Cancel", (
						ConfirmDialog dialog) -> {
					if (dialog.isConfirmed()) {
						for (Iterator<Object> iter = selected.iterator(); iter
								.hasNext();) {
							Grade sel = (Grade) iter.next();
							gradeRepository.delete(sel);
							removeGridRow(sel);
						}
					}
				});
	}
}
