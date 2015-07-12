package com.caritos.student_information_system.view.dashboard;

import com.caritos.student_information_system.view.AbstractView;
import com.caritos.student_information_system.view.ViewTypes;
import com.vaadin.data.Container.Indexed;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@VaadinUIScope
@SpringView(DashboardView.VIEW_NAME)
public class DashboardView extends AbstractView<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "dashboard";

	public DashboardView() {
		super(ViewTypes.DASHBOARD, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caritos.student_information_system.view.AbstractView#buildMainContent
	 * ()
	 */
	@Override
	protected Component buildMainContent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setSizeFull();

		layout.addComponent(new Label("This page is still in progress."));

		return layout;
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
		return null;
	}

}
