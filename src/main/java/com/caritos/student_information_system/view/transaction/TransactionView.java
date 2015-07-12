package com.caritos.student_information_system.view.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Category;
import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.domain.Student;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.domain.Transaction;
import com.caritos.student_information_system.repository.CategoryRepository;
import com.caritos.student_information_system.repository.StudentRepository;
import com.caritos.student_information_system.repository.TransactionRepository;
import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.CustomPropertyValueGenerator;
import com.caritos.student_information_system.view.FilterChangeListenerImpl;
import com.caritos.student_information_system.view.ViewTypes;
import com.google.common.collect.Lists;
import com.ibm.icu.text.NumberFormat;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@VaadinUIScope
@SpringView(TransactionView.VIEW_NAME)
public class TransactionView extends AbstractView<Transaction> {
	
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "transactions";
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private StudentRepository studentRepository;
	private BeanItemContainer<Transaction> dsGrid = new BeanItemContainer<>(Transaction.class);

	
	public TransactionView() {
		super(ViewTypes.TRANSACTION, "Add Credit Transaction", "Edit Transaction", "Delete Transaction");
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
		getGrid().setColumnOrder("date", "amount", "student", "category", "receiptNumber");
		getGrid().getColumn("date").setExpandRatio(1);
		getGrid().getColumn("amount").setExpandRatio(1);
		getGrid().getColumn("student").setExpandRatio(1);
		getGrid().getColumn("category").setExpandRatio(1);
		getGrid().getColumn("receiptNumber").setExpandRatio(1);
		
		setSummaryFooterCells();
		
		// no need to show id in the table
		generatedPropertyContainer().removeContainerProperty("transactionId");
		generatedPropertyContainer().removeContainerProperty("description");
		generatedPropertyContainer().addGeneratedProperty("category",
				new CustomPropertyValueGenerator<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {
						Transaction trans = (Transaction) itemId;
						Category category = categoryRepository.findOne(trans.getCategory().getCategoryId());
						return category != null ? category.getName() : "-";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});
		generatedPropertyContainer().addGeneratedProperty("student",
				new CustomPropertyValueGenerator<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {
						Transaction trans = (Transaction) itemId;
						Student student = studentRepository.findOne(trans.getStudent().getId());
						return student != null ? student.getFullName() : "-";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});
	}

	private void setSummaryFooterCells() {
		// TODO Auto-generated method stub
		final FooterRow footer = getGrid().appendFooterRow();
		footer.getCell("date").setHtml("total:");
		footer.join("amount", "student", "category", "receiptNumber");
		footer.getCell("amount").setHtml("<b>" + calculateTransactionAmount() + "</b>");

		// filter change count recalculate
		dsGrid.addItemSetChangeListener(new ItemSetChangeListener() {
			private static final long serialVersionUID = -5749941076142200737L;
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				footer.getCell("amount").setHtml("<b>" + calculateTransactionAmount() + "</b>");
			}
		});
	}

	private String calculateTransactionAmount() {
		double sum = 0.00;
		@SuppressWarnings("unchecked")
		List<Transaction> transactions = (List<Transaction>) getGrid().getContainerDataSource().getItemIds();
		for(Transaction transaction: transactions) {
			sum = sum + transaction.getAmount().doubleValue();
		}
		return NumberFormat.getCurrencyInstance().format(sum);
	}

	@Override
	protected Component createFilterableComponent(Object propertyId,
			Container.Filterable container) {
		Component comp = null;
		switch (propertyId.toString()) {
		case "student":
			ComboBox studentComboBox = new ComboBox();
			styleFilterComboBox(studentComboBox);
			studentComboBox.setImmediate(true);
			BeanItemContainer<Student> studentContainer = new BeanItemContainer<>(Student.class);
			studentContainer.addAll(getStudentsBasedOnUserType());
			studentComboBox.setItemCaptionPropertyId("fullName");
			studentComboBox.setContainerDataSource(studentContainer);
			studentComboBox.addValueChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = studentComboBox;
			break;
		case "category":
			ComboBox comboBox1 = new ComboBox();
			styleFilterComboBox(comboBox1);
			comboBox1.setImmediate(true);
			BeanItemContainer<Category> ds1 = new BeanItemContainer<>(Category.class);
			ds1.addAll(Lists.newArrayList(getCategoriesBasedOnUserType()));
			comboBox1.setContainerDataSource(ds1);
			comboBox1.setItemCaptionPropertyId("name");
			comboBox1.addValueChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = comboBox1;
			break;
		case "amount":
			// TODO improve this by making it a ComboBox instead of a TextField
			TextField amountField = new TextField();
			styleFilterTextField(amountField);
			amountField.setImmediate(true);
			amountField.setColumns(8);
			amountField.addTextChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = amountField;
			break;
		case "date":
			// TODO improve this by making it a ComboBox instead of a TextField
			TextField dateField = new TextField();
			styleFilterTextField(dateField);
			dateField.setImmediate(true);
			dateField.setColumns(8);
			dateField.addTextChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = dateField;
			break;
		case "receiptNumber":
			TextField receiptField = new TextField();
			styleFilterTextField(receiptField);
			receiptField.setImmediate(true);
			receiptField.setColumns(8);
			receiptField.addTextChangeListener(new FilterChangeListenerImpl(getGrid(), container, propertyId));
			comp = receiptField;
			break;
		}
		return comp;
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
		dsGrid.addAll(getTransactionsBasedOnUserType());
		return dsGrid;
	}

	private List<Transaction> getTransactionsBasedOnUserType() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Guardian) {
			List<Student> students = studentRepository.findByGuardian((Guardian)user);
			for(Student student: students) {
				transactions.addAll(student.getTransactions());
			}
		} else if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			List<Student> students = new ArrayList<Student>();
			for(School school: schools) {
				students.addAll(school.getStudents());
			}
			for(Student student: students) {
				transactions.addAll(student.getTransactions());
			}
		} else if(user instanceof SuperUser) {
			transactions = Lists.newArrayList(transactionRepository.findAll());
		}
		return transactions;
	}
	
	private List<Student> getStudentsBasedOnUserType() {
		List<Student> students = new ArrayList<Student>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Guardian) {
			students = studentRepository.findByGuardian((Guardian)user);
		} else if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				students.addAll(studentRepository.findBySchool(school));
			}
		} else if(user instanceof SuperUser) {
			students.addAll(Lists.newArrayList(studentRepository.findAll()));
		}
		return students;
	}
	private List<Category> getCategoriesBasedOnUserType() {
		List<Category> categories = new ArrayList<Category>();
		// setting the organization property for the upcoming administrator
		// since we already know it.
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		Person user = vaadinSession.getAttribute(Person.class);
		if(user instanceof Administrator) {
			Organization organization = ((Administrator)user).getOrganization();
			List<School> schools = organization.getSchools();
			for(School school: schools) {
				categories.addAll(categoryRepository.findBySchool(school));
			}
		} else if(user instanceof SuperUser) {
			categories.addAll(Lists.newArrayList(categoryRepository.findAll()));
		}
		return categories;
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
						new AddEditTransactionView("Add Transaction",
								new Transaction()) {

							private static final long serialVersionUID = 1L;

							@Override
							protected List<Student> getStudents() {
								return getStudentsBasedOnUserType();
							}

							@Override
							protected List<Category> getCategories() {
								return getCategoriesBasedOnUserType();
							}

							@Override
							protected void save(Transaction trans) {
								transactionRepository.save(trans);
								addGridRow(trans);
							}
						});
	}
	
	protected void addNegativeEvent(ClickEvent event) {
		UI.getCurrent().addWindow(
						new AddEditTransactionView("Add Transaction", new Transaction()) {

							private static final long serialVersionUID = 1L;

							@Override
							protected List<Student> getStudents() {
								return getStudentsBasedOnUserType();
							}

							@Override
							protected List<Category> getCategories() {
								return getCategoriesBasedOnUserType();
							}

							@Override
							protected void save(Transaction trans) {
								BigDecimal amount = trans.getAmount();
								double negativeAmount = amount.doubleValue() * -1;
								trans.setAmount(new BigDecimal(negativeAmount));
								transactionRepository.save(trans);
								addGridRow(trans);
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
				new AddEditTransactionView("Edit Transaction", (Transaction) selected.iterator().next()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected List<Student> getStudents() {
						return getStudentsBasedOnUserType();
					}

					@Override
					protected List<Category> getCategories() {
						return getCategoriesBasedOnUserType();
					}

					@Override
					protected void save(Transaction trans) {
						transactionRepository.save(trans);
						refreshGrid();
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

		ConfirmDialog.show(UI.getCurrent(), "Delete",
				"Delete selected transaction(s)?", "Delete", "Cancel", (
						ConfirmDialog dialog) -> {
					if (dialog.isConfirmed()) {
						for (Iterator<Object> iter = selected.iterator(); iter
								.hasNext();) {
							Transaction sel = (Transaction) iter.next();
							transactionRepository.delete(sel);
							removeGridRow(sel);
						}
					}
				});
	}

	@Override
	protected HorizontalLayout buildButtons() {
		// build the current three buttons
		super.buildButtons();
		// need to add a new credit button
		Button newBtn = new Button("Add Debit Transaction");
		newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		newBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				addNegativeEvent(event);
			}
		});
		// let's put this in the second'th position
		buttonsLayout.addComponent(newBtn, 1);
		return buttonsLayout;
	}
}
