package com.caritos.student_information_system.view.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Grade;
import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.domain.Section;
import com.caritos.student_information_system.domain.Student;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.GradeRepository;
import com.caritos.student_information_system.repository.GuardianRepository;
import com.caritos.student_information_system.repository.SchoolRepository;
import com.caritos.student_information_system.repository.SectionRepository;
import com.caritos.student_information_system.repository.StudentRepository;
import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.FilterChangeListenerImpl;
import com.caritos.student_information_system.view.ViewTypes;
import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@VaadinUIScope
@SpringView(StudentView.VIEW_NAME)
public class StudentView extends AbstractView<Student> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "student";
	private BeanItemContainer<Student> studentContainer = new BeanItemContainer<>(Student.class);
	
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private GuardianRepository guardianRepository;
	@Autowired
	private GradeRepository gradeRepository;

	public StudentView() {
		super(ViewTypes.STUDENT, "Add Student", "Edit Student", "Delete Student");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractView#addEvent(com
	 * .vaadin.ui.Button.ClickEvent)
	 */
	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(
				new AddEditStudentView("Add Student", new Student()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Student student) {
						studentRepository.save(student);
						addGridRow(student);
					}

					@Override
					protected List<School> getSchools() {
						return getSchoolsBasedOnUserType();
					}

					@Override
					protected List<Guardian> getGuardians() {
						return getGuardiansBasedOnUserType();
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
				new AddEditStudentView("Edit Student", (Student) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(Student user) {
						studentRepository.save(user);
						refreshGrid();
					}

					@Override
					protected List<School> getSchools() {
						return getSchoolsBasedOnUserType();
					}
					
					@Override
					protected List<Guardian> getGuardians() {
						return getGuardiansBasedOnUserType();
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
							Student sel = (Student) iter.next();
							studentRepository.delete(sel);
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
		getGrid().setColumnOrder("firstName", "lastName", "guardian", "school", "grade", "section", "enrolled");
		getGrid().getColumn("firstName").setExpandRatio(1);
		getGrid().getColumn("lastName").setExpandRatio(1);
		getGrid().getColumn("guardian").setExpandRatio(1);
		getGrid().getColumn("school").setExpandRatio(1);
		getGrid().getColumn("grade").setExpandRatio(1);
		getGrid().getColumn("section").setExpandRatio(1);
		getGrid().getColumn("enrolled").setExpandRatio(1);
		
		setSummaryFooterCells();
		
		generatedPropertyContainer().removeContainerProperty("id");
		generatedPropertyContainer().removeContainerProperty("username");
		generatedPropertyContainer().removeContainerProperty("email");
		generatedPropertyContainer().removeContainerProperty("transactions");
		generatedPropertyContainer().removeContainerProperty("phoneNumber");
		generatedPropertyContainer().removeContainerProperty("password");
		generatedPropertyContainer().removeContainerProperty("fullName");
		generatedPropertyContainer().removeContainerProperty("role");
//		generatedPropertyContainer().addGeneratedProperty("grade", new CustomPropertyValueGenerator<String>() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public String getValue(Item item, Object itemId, Object propertyId) {
//				Student user = (Student) itemId;
//				Grade grade = user.getGrade();
//				return grade != null ? grade.getName() : "-";
//			}
//
//			@Override
//			public Class<String> getType() {
//				return String.class;
//			}
//		});
//		generatedPropertyContainer().addGeneratedProperty("guardian",
//				new CustomPropertyValueGenerator<String>() {
//
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public String getValue(Item item, Object itemId, Object propertyId) {
//						Student user = (Student) itemId;
//						User guardian = user.getGuardian();
//						return guardian != null ? guardian.getName() : "-";
//					}
//
//					@Override
//					public Class<String> getType() {
//						return String.class;
//					}
//				});
	}

	private void setSummaryFooterCells() {
		final FooterRow footer = getGrid().appendFooterRow();
		footer.getCell("firstName").setHtml("total:");
		footer.join("lastName", "guardian", "school", "grade", "section", "enrolled");
		footer.getCell("lastName").setHtml("<b>" + getGrid().getContainerDataSource().getItemIds().size() + "</b>");

		// filter change count recalculate
		studentContainer.addItemSetChangeListener(new ItemSetChangeListener() {
			private static final long serialVersionUID = -5749941076142200737L;
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				footer.getCell("lastName").setHtml("<b>" + getGrid().getContainerDataSource().getItemIds().size() + "</b>");
			}
		});
	}

	@Override
	protected Component createFilterableComponent(Object propertyId, Container.Filterable container) {
		Component comp = null;
		switch (propertyId.toString()) {
		case "firstName":
		case "lastName":
		case "guardian":
			TextField textField = new TextField();
			styleFilterTextField(textField);
			textField.setImmediate(true);
			textField.setColumns(8);
			textField.addTextChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = textField;
			break;
		case "school":
			ComboBox schoolComboBox = new ComboBox();
			styleFilterComboBox(schoolComboBox);
			schoolComboBox.setImmediate(true);
			BeanItemContainer<School> schoolContainer = new BeanItemContainer<>(School.class);
			schoolContainer.addAll(getSchoolsBasedOnUserType());
			schoolComboBox.setItemCaptionPropertyId("name");
			schoolComboBox.setContainerDataSource(schoolContainer);
			schoolComboBox.addValueChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = schoolComboBox;
			break;
		case "grade":
			ComboBox gradeComboBox = new ComboBox();
			styleFilterComboBox(gradeComboBox);
			gradeComboBox.setImmediate(true);
			BeanItemContainer<Grade> gradeContainer = new BeanItemContainer<>(Grade.class);
			gradeContainer.addAll(getGradesBasedOnUserType());
			gradeComboBox.setItemCaptionPropertyId("name");
			gradeComboBox.setContainerDataSource(gradeContainer);
			gradeComboBox.addValueChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = gradeComboBox;
			break;
		case "section":
			ComboBox sectionComboBox = new ComboBox();
			styleFilterComboBox(sectionComboBox);
			sectionComboBox.setImmediate(true);
			BeanItemContainer<Section> sectionContainer = new BeanItemContainer<>(Section.class);
			sectionContainer.addAll(getSectionsBasedOnUserType());
			sectionComboBox.setItemCaptionPropertyId("name");
			sectionComboBox.setContainerDataSource(sectionContainer);
			sectionComboBox.addValueChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = sectionComboBox;
			break;
		case "enrolled":
			TextField enrolledField = new TextField();
			styleFilterTextField(enrolledField);
			enrolledField.setImmediate(true);
			enrolledField.setColumns(8);
			enrolledField.addTextChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = enrolledField;
			break;
		}
		return comp;
	}

	@Override
	protected Indexed createContainer() {
		studentContainer.removeAllItems();
		studentContainer.addAll(getStudentsBasedOnUserType());
		return studentContainer;
	}

	private List<Student> getStudentsBasedOnUserType() {
		List<Student> students = new ArrayList<Student>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Guardian) {
			students.addAll(studentRepository.findByGuardian((Guardian)user));
		} else if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				students.addAll(school.getStudents());
			}
		} else if(user instanceof SuperUser) {
			students = Lists.newArrayList(studentRepository.findAll());
		}
		return students;
	}
	
	private List<School> getSchoolsBasedOnUserType() {
		List<School> schools = new ArrayList<School>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			schools.addAll(schoolRepository.findByOrganization(organization));
		} else if(user instanceof SuperUser) {
			schools.addAll(Lists.newArrayList(schoolRepository.findAll()));
		}
		return schools;
	}
	
	private List<Guardian> getGuardiansBasedOnUserType() {
		List<Guardian> guardians = new ArrayList<Guardian>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			guardians.addAll(guardianRepository.findByOrganization(organization));
		} else if(user instanceof SuperUser) {
			guardians.addAll(Lists.newArrayList(guardianRepository.findAll()));
		}
		return guardians;
	}
	
	private List<Grade> getGradesBasedOnUserType() {
		List<Grade> grades = new ArrayList<Grade>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				grades.addAll(gradeRepository.findBySchool(school));
			}
		} else if(user instanceof SuperUser) {
			grades.addAll(Lists.newArrayList(gradeRepository.findAll()));
		}
		return grades;
	}
	
	private List<Section> getSectionsBasedOnUserType() {
		List<Section> sections = new ArrayList<Section>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			List<Grade> grades = new ArrayList<Grade>();
			for(School school: schools) {
				grades.addAll(school.getGrades());
			}
			for(Grade grade: grades) {
				sections.addAll(sectionRepository.findByGrade(grade));
			}
		} else if(user instanceof SuperUser) {
			sections.addAll(Lists.newArrayList(sectionRepository.findAll()));
		}
		return sections;
	}
}
 