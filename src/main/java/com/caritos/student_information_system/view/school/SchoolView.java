package com.caritos.student_information_system.view.school;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Category;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.repository.CategoryRepository;
import com.caritos.student_information_system.repository.OrganizationRepository;
import com.caritos.student_information_system.repository.SchoolRepository;
import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.ViewTypes;
import com.google.common.collect.Lists;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

@VaadinUIScope
@SpringView(SchoolView.VIEW_NAME)
public class SchoolView extends AbstractView<School> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "school";

	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	OrganizationRepository organizationRepository;

	/**
	 * @param viewType
	 */
	public SchoolView() {
		super(ViewTypes.SCHOOL, "Add School", "Edit School", "Delete School");
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
				new AddEditSchoolView("Add School", new School()) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected void save(School school) {
						schoolRepository.save(school);
						addGridRow(school);
					}

					@Override
					protected List<Category> getCategories() {
						return Lists.newArrayList(categoryRepo.findAll());
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
				new AddEditSchoolView("Edit School", (School) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void save(School school) {
						schoolRepository.save(school);
						refreshGrid();
					}

					@Override
					protected List<Category> getCategories() {
						return Lists.newArrayList(categoryRepo.findAll());
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

		ConfirmDialog.show(UI.getCurrent(), "Delete", "Delete selected school(s)?", "Delete", "Cancel", (ConfirmDialog dialog) -> {
					if (dialog.isConfirmed()) {
						for (Iterator<Object> iter = selected.iterator(); iter.hasNext();) {
							School sc = (School) iter.next();
							schoolRepository.delete(sc);
							removeGridRow(sc);
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
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			getGrid().setColumnOrder("name");
			generatedPropertyContainer().removeContainerProperty("organization");
		} else {
			getGrid().setColumnOrder("name", "organization");
			getGrid().getColumn("organization").setExpandRatio(1);
			
			
		}
		getGrid().getColumn("name").setExpandRatio(1);
		generatedPropertyContainer().removeContainerProperty("id");
		generatedPropertyContainer().removeContainerProperty("categories");
		generatedPropertyContainer().removeContainerProperty("students");
		generatedPropertyContainer().removeContainerProperty("administrators");
		generatedPropertyContainer().removeContainerProperty("grades");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractView#createContainer
	 * ()
	 */
	@Override
	protected Indexed createContainer() {
		ArrayList<School> schools = new ArrayList<School>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			schools = Lists.newArrayList(schoolRepository.findByOrganization(organization));
		} else if(user instanceof SuperUser) {
			schools = Lists.newArrayList(schoolRepository.findAll());
		}
		
		BeanItemContainer<School> ds = new BeanItemContainer<>(School.class);
		ds.addAll(schools);
		return ds;
	}

}
