package com.caritos.student_information_system.view.section;

import java.util.List;

import com.caritos.student_information_system.domain.Grade;
import com.caritos.student_information_system.domain.Section;
import com.caritos.student_information_system.view.AbstractWindow;
import com.caritos.student_information_system.view.ViewsUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * Need a way to add new categories for the transaction.
 *
 * Created by eladio on 4/9/15.
 */
public abstract class AddEditSectionView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Section> fieldGroup;

    public AddEditSectionView(String caption, Section section) {
        super(caption, "Save", "Cancel");
        fieldGroup = new BeanFieldGroup<>(Section.class);
        fieldGroup.setItemDataSource(section);
    }

    @Override
    protected AbstractOrderedLayout buildContents() {
        FormLayout layout = new FormLayout();
        layout.setWidth(null);
        layout.setSpacing(true);
        layout.setHeight("100%");

        ComboBox gradeCombo = new ComboBox("Grade");
        gradeCombo.setRequired(true);
        gradeCombo.setRequiredError("Grade is required!");
        gradeCombo.setItemCaptionPropertyId("name");
        BeanItemContainer<Grade> gradeContainer = new BeanItemContainer<>(Grade.class);
        gradeContainer.addAll(getGrades());
        gradeCombo.setContainerDataSource(gradeContainer);
        fieldGroup.bind(gradeCombo, "grade");
        layout.addComponent(gradeCombo);

        TextField nameField = new TextField("Section Name");
        fieldGroup.bind(nameField, "name");
        layout.addComponent(nameField);
        return layout;
    }

    @Override
    protected void okEvent(Button.ClickEvent event) {
        try {
            fieldGroup.commit();
            save(fieldGroup.getItemDataSource().getBean());
            ViewsUtil.showSuccessNotification("Section", "Section saved successfully.");
            UI.getCurrent().removeWindow(AddEditSectionView.this);
        } catch(FieldGroup.CommitException e) {
            Notification.show("Pls fill up the required fields.", Notification.Type.WARNING_MESSAGE);
        }

    }

    @Override
    protected void cancelEvent(Button.ClickEvent event) {}
    protected abstract List<Grade> getGrades();
    protected abstract void save(Section section);
}
