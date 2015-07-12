package com.caritos.student_information_system.view;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "";
	
	@PostConstruct
	void init() {
		addComponent(new Label("This is the default view"));
		
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method
	}

}
