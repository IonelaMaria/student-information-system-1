package com.caritos.student_information_system.view.category;

import com.caritos.student_information_system.domain.Category;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.view.AbstractWindow;
import com.caritos.student_information_system.view.ViewsUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

import java.util.List;

/**
 * Need a way to add new categories for the transaction.
 *
 * Created by eladio on 4/9/15.
 */
public abstract class AddEditCategoryView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Category> fieldGroup;

    public AddEditCategoryView(String caption, Category category) {
        super(caption, "Save", "Cancel");
        fieldGroup = new BeanFieldGroup<>(Category.class);
        fieldGroup.setItemDataSource(category);
    }

    @Override
    protected AbstractOrderedLayout buildContents() {
        FormLayout layout = new FormLayout();
        layout.setWidth(null);
        layout.setSpacing(true);
        layout.setHeight("100%");

        ComboBox schoolCombo = new ComboBox("School");
        schoolCombo.setRequired(true);
        schoolCombo.setRequiredError("School is required!");
        schoolCombo.setItemCaptionPropertyId("name");

        BeanItemContainer<School> dsSchool= new BeanItemContainer<>(School.class);
        dsSchool.addAll(getSchools());
        schoolCombo.setContainerDataSource(dsSchool);
        fieldGroup.bind(schoolCombo, "school");
        layout.addComponent(schoolCombo);

        TextField nameField = new TextField("Category Name");
        fieldGroup.bind(nameField, "name");
        layout.addComponent(nameField);
        return layout;
    }

    @Override
    protected void okEvent(Button.ClickEvent event) {
        try {
            fieldGroup.commit();
            save(fieldGroup.getItemDataSource().getBean());
            ViewsUtil.showSuccessNotification("Category", "Category saved successfully.");

            UI.getCurrent().removeWindow(AddEditCategoryView.this);
        } catch(FieldGroup.CommitException e) {
            Notification.show("Pls fill up the required fields.", Notification.Type.WARNING_MESSAGE);
        }

    }

    @Override
    protected void cancelEvent(Button.ClickEvent event) {}

    protected abstract List<School> getSchools();
    protected abstract void save(Category category);
}
