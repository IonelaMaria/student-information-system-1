package com.caritos.student_information_system.view.transaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.caritos.student_information_system.domain.Category;
import com.caritos.student_information_system.domain.Student;
import com.caritos.student_information_system.domain.Transaction;
import com.caritos.student_information_system.view.AbstractWindow;
import com.caritos.student_information_system.view.ViewsUtil;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public abstract class AddEditTransactionView extends AbstractWindow {

	private static final long serialVersionUID = 1L;
	private BeanFieldGroup<Transaction> fieldGroup;

	public AddEditTransactionView(String caption, Transaction transaction) {
		super(caption, "Save", "Cancel");
		fieldGroup = new BeanFieldGroup<>(Transaction.class);
		fieldGroup.setItemDataSource(transaction);
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
		layout.setHeight("100%");

		TextField amountField = fieldGroup.buildAndBind("Amount", "amount", TextField.class);
		amountField.setNullRepresentation("");
		amountField.setRequired(true);
		amountField.setRequiredError("Amount is required!");
		layout.addComponent(amountField);

		TextField receiptNumberField = fieldGroup.buildAndBind("Receipt Number", "receiptNumber", TextField.class);
		receiptNumberField.setNullRepresentation("");
		layout.addComponent(receiptNumberField);
		
		PopupDateField dateField = new PopupDateField("Date");
		dateField.setRequired(true);
		dateField.setRequiredError("Date is required!");
		dateField.setResolution(Resolution.DAY);
		dateField.setConverter(new Converter<Date, java.time.LocalDate>() {
			private static final long serialVersionUID = 1L;

			@Override
			public LocalDate convertToModel(Date value,
					Class<? extends LocalDate> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
			}

			@Override
			public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
				return value != null ? Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
			}

			@Override
			public Class<LocalDate> getModelType() {
				return LocalDate.class;
			}

			@Override
			public Class<Date> getPresentationType() {
				return Date.class;
			}
		});
		fieldGroup.bind(dateField, "date");
		layout.addComponent(dateField);

		ComboBox studentCombo = new ComboBox("Student");
		studentCombo.setRequired(true);
		studentCombo.setRequiredError("Student is required!");
		studentCombo.setItemCaptionPropertyId("fullName");
		BeanItemContainer<Student> studentContainer = new BeanItemContainer<>(Student.class);
		studentContainer.addAll(getStudents());
		studentCombo.setContainerDataSource(studentContainer);
		fieldGroup.bind(studentCombo, "student");
		layout.addComponent(studentCombo);
		
		ComboBox categoryCombo = new ComboBox("Category");
		categoryCombo.setRequired(true);
		categoryCombo.setRequiredError("Category is required!");
		categoryCombo.setItemCaptionPropertyId("name");

		BeanItemContainer<Category> dsCat = new BeanItemContainer<>(Category.class);
		dsCat.addAll(getCategories());
		categoryCombo.setContainerDataSource(dsCat);
		fieldGroup.bind(categoryCombo, "category");
		layout.addComponent(categoryCombo);
		
		TextArea descArea = new TextArea("Description");
		descArea.setNullRepresentation("");
		descArea.setRows(5);
		descArea.setColumns(15);
		fieldGroup.bind(descArea, "description");
		layout.addComponent(descArea);
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
			ViewsUtil.showSuccessNotification("Transaction","Transaction saved successfully.");
			UI.getCurrent().removeWindow(AddEditTransactionView.this);
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

	protected abstract List<Student> getStudents();

	protected abstract List<Category> getCategories();

	protected abstract void save(Transaction trans);
}
