package com.caritos.student_information_system.view.category;

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
@SpringView(CategoryView.VIEW_NAME)
public class CategoryView extends AbstractView<Category> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "category";
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	
    public CategoryView() {
    	super(ViewTypes.CATEGORY, "Add Category", "Edit Category", "Delete Category");
    }

	@Override
	protected void configureGrid() {
		getGrid().setColumnOrder("name", "school");
		getGrid().getColumn("name").setExpandRatio(1);
		getGrid().getColumn("school").setExpandRatio(1);
		generatedPropertyContainer().removeContainerProperty("categoryId");
		generatedPropertyContainer().addGeneratedProperty("school", 
				new CustomPropertyValueGenerator<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						Category category = (Category) itemId;
						School school = schoolRepository.findOne(category.getSchool().getId());
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
		ArrayList<Category> categories = new ArrayList<Category>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				categories.addAll(school.getCategories());
			}
		} else if(user instanceof SuperUser) {
			categories = Lists.newArrayList(categoryRepository.findAll());
		}
		BeanItemContainer<Category> dsGrid = new BeanItemContainer<>(Category.class, categories);
		return dsGrid;
	}

	@Override
	protected void addEvent(ClickEvent event) {
		UI.getCurrent().addWindow(new AddEditCategoryView("Add Category", new Category()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<School> getSchools() {
				return Lists.newArrayList(schoolRepository.findAll());
			}

			@Override
			protected void save(Category category) {
				categoryRepository.save(category);
				addGridRow(category);
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
				new AddEditCategoryView("Edit Category", (Category) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected List<School> getSchools() {
						return Lists.newArrayList(schoolRepository.findAll());
					}

					@Override
					protected void save(Category category) {
						categoryRepository.save(category);
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
				"Delete selected category(s)?", "Delete", "Cancel", (
						ConfirmDialog dialog) -> {
					if (dialog.isConfirmed()) {
						for (Iterator<Object> iter = selected.iterator(); iter
								.hasNext();) {
							Category sel = (Category) iter.next();
							categoryRepository.delete(sel);
							removeGridRow(sel);
						}
					}
				});
	}
}
