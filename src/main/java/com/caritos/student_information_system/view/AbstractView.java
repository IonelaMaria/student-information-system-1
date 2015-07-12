package com.caritos.student_information_system.view;

import java.util.Collection;

import javax.annotation.PostConstruct;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractView<T> extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private ViewTypes viewType;
	private HorizontalLayout headerLayout;
	private String addCaption;
	private String editCaption;
	private String deleteCaption;
	private boolean inProgress;
	private Button newBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Grid grid;
	private GeneratedPropertyContainer parentContainer;
	private HeaderRow headerRow;
	protected HorizontalLayout buttonsLayout = new HorizontalLayout();
	
	public AbstractView(ViewTypes viewType) {
		this(viewType, false);
	}

	public AbstractView(ViewTypes viewType, boolean inProgress) {
		this(viewType, "Add", "Edit", "Delete", inProgress);
	}

	public AbstractView(ViewTypes viewType, String addCaption,
			String editCaption, String deleteCaption) {
		this(viewType, addCaption, editCaption, deleteCaption, false);
	}

	public AbstractView(ViewTypes viewType, String addCaption,
			String editCaption, String deleteCaption, boolean inProgress) {
		this.viewType = viewType;
		this.addCaption = addCaption;
		this.editCaption = editCaption;
		this.deleteCaption = deleteCaption;
		this.inProgress = inProgress;
	}

	@PostConstruct
	void init() {
		setSizeFull();
		setSpacing(true);
		setMargin(true);

		if (!inProgress) {
			headerLayout = buildHeader();
			addComponent(headerLayout);
		}
		Component mainContent = buildMainContent();

		addComponent(mainContent);
		setExpandRatio(mainContent, 1.0f);
	}

	protected abstract void configureGrid();

	protected Grid getGrid() {
		return grid;
	}

	protected void refreshGrid() {
		// TODO some bug in refresh. will be slow. search documentation for the
		// right flush cache.
		createDataSource();
	}

	protected void addGridRow(T itemId) {
		ViewsUtil.addGridRow(grid.getContainerDataSource(), itemId);
	}

	protected void removeGridRow(T itemId) {
		ViewsUtil.removeGridRow(grid.getContainerDataSource(), itemId);
	}

	protected Component buildMainContent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setWidth("100%");

		grid = new Grid();
		grid.setWidth("100%");
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setImmediate(true);
		
		createDataSource();
		
		layout.addComponent(grid);
		grid.addSelectionListener(event -> {
			final Collection<Object> selected = grid.getSelectedRows();
			int size = 0;
			if (selected != null) {
				size = selected.size();
			} else {
				size = 1;
			}

			if (editBtn != null) {
				// edit button only require one item selected.
				if (size == 1) {
					editBtn.setEnabled(true);
				} else {
					editBtn.setEnabled(false);
				}
			}

			if (deleteBtn != null) {
				if (size > 0) {
					deleteBtn.setEnabled(true);
				} else {
					deleteBtn.setEnabled(false);
				}
			}
		});

		return layout;
	}

	protected GeneratedPropertyContainer generatedPropertyContainer() {
		return parentContainer;
	}

	protected abstract Indexed createContainer();

	private void createDataSource() {
		parentContainer = new GeneratedPropertyContainer(createContainer());
		grid.setContainerDataSource(parentContainer);
		if (!inProgress) {
			createFilterableRow();
			configureGrid();
		}
	}

	private void createFilterableRow() {
		if (headerRow != null) {
			return;
		}
		headerRow = grid.appendHeaderRow();
		Collection<?> propertyIds = grid.getContainerDataSource()
				.getContainerPropertyIds();
		boolean hasAdded = false;
		for (Object propertyId : propertyIds) {
			HeaderCell cell = headerRow.getCell(propertyId);
			Component component = createFilterableComponent(propertyId,
					(Container.Filterable) grid.getContainerDataSource());
			if (component != null) {
				cell.setComponent(component);
				hasAdded = true;
			}
		}
		if (!hasAdded) {
			grid.removeHeaderRow(headerRow);
			headerRow = null;
		}
	}

	/**
	 * Subclass may override
	 * 
	 * @param propertyId
	 * @return
	 */
	protected Component createFilterableComponent(Object propertyId, Container.Filterable container) {
		return null;
	}

	protected HorizontalLayout buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);

		Label titleLabel = new Label(viewType.getDescription());
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(titleLabel);

		HorizontalLayout buttonsLayout = buildButtons();
		header.addComponent(buttonsLayout);
		header.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_RIGHT);
		return header;
	}

	protected HorizontalLayout buildButtons() {

		buttonsLayout.setSpacing(true);
		buttonsLayout.addStyleName("toolbar");

		if (addCaption != null) {
			newBtn = new Button(addCaption);
			newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
			newBtn.addClickListener(new Button.ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					addEvent(event);
				}
			});
			buttonsLayout.addComponent(newBtn);
		}
		if (editCaption != null) {
			editBtn = new Button(editCaption);
			editBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			editBtn.setEnabled(false);
			editBtn.addClickListener(new Button.ClickListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					editEvent(event);
				}
			});
			buttonsLayout.addComponent(editBtn);
		}
		if (deleteCaption != null) {
			deleteBtn = new Button(deleteCaption);
			deleteBtn.addStyleName(ValoTheme.BUTTON_DANGER);
			deleteBtn.setEnabled(false);
			deleteBtn.addClickListener(new Button.ClickListener() {
				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					deleteEvent(event);
				}
			});
			buttonsLayout.addComponent(deleteBtn);
		}
		return buttonsLayout;
	}

	protected abstract void addEvent(ClickEvent event);

	protected abstract void editEvent(ClickEvent event);

	protected abstract void deleteEvent(ClickEvent event);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener
	 * .ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {

	}

	protected void styleFilterTextField(TextField textField) {
		textField.setWidth("100%");
		textField.addStyleName(ValoTheme.TEXTFIELD_TINY);
		textField.setInputPrompt("Filter");
	}
	
	protected void styleFilterComboBox(ComboBox comboBox) {
		comboBox.setWidth("100%");
		comboBox.addStyleName(ValoTheme.TEXTFIELD_TINY);
		comboBox.setInputPrompt("Filter");
	}
}
