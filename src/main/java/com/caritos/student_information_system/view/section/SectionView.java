package com.caritos.student_information_system.view.section;

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
import com.caritos.student_information_system.domain.Section;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.GradeRepository;
import com.caritos.student_information_system.repository.SectionRepository;
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
@SpringView(SectionView.VIEW_NAME)
public class SectionView extends AbstractView<Section> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "section";
	@Autowired
	private GradeRepository gradeRepository;
	@Autowired
	private SectionRepository sectionRepository;
	
    public SectionView() {
    	super(ViewTypes.SECTION, "Add Section", "Edit Section", "Delete Section");
    }

	@Override
	protected void configureGrid() {
		getGrid().setColumnOrder("name", "grade");
		getGrid().getColumn("name").setExpandRatio(1);
		getGrid().getColumn("grade").setExpandRatio(1);
		generatedPropertyContainer().removeContainerProperty("students");
		generatedPropertyContainer().addGeneratedProperty("school", 
				new CustomPropertyValueGenerator<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						Section section = (Section) itemId;
						School school = section.getGrade().getSchool();
						return school != null ? school.getName() : "-";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
		});
		generatedPropertyContainer().addGeneratedProperty("grade", 
				new CustomPropertyValueGenerator<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						Section section = (Section) itemId;
						Grade grade = section.getGrade();
						return grade != null ? grade.getName() : "-";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
		});
	}

	@Override
	protected Indexed createContainer() {
		ArrayList<Section> sections = new ArrayList<Section>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				List<Grade> grades = school.getGrades();
				for(Grade grade: grades) {
					sections.addAll(grade.getSections());
				}
			}
		} else if(user instanceof SuperUser) {
			sections = Lists.newArrayList(sectionRepository.findAll());
		}
		
		BeanItemContainer<Section> sectionContainer = new BeanItemContainer<>(Section.class, sections);
		return sectionContainer;
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(new AddEditSectionView("Add Section", new Section()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Grade> getGrades() {
				return Lists.newArrayList(gradeRepository.findAll());
			}

			@Override
			protected void save(Section section) {
				sectionRepository.save(section);
				addGridRow(section);
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
				new AddEditSectionView("Edit Section", (Section) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected List<Grade> getGrades() {
						return Lists.newArrayList(gradeRepository.findAll());
					}

					@Override
					protected void save(Section section) {
						sectionRepository.save(section);
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
						for (Iterator<Object> iter = selected.iterator(); iter.hasNext();) {
							Section sel = (Section) iter.next();
							sectionRepository.delete(sel);
							removeGridRow(sel);
						}
					}
				});
	}
}
