package com.caritos.student_information_system.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@VaadinUIScope
@SpringView(UIScopedView.VIEW_NAME)
public class UIScopedView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "ui";
	
	@Autowired
	private Greeter greeter;

	@PostConstruct
	void init() {
		setMargin(true);
		setSpacing(true);
		addComponent(new Label("This is a UI scoped view.  Greeter says: " + greeter.sayHello()));
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method
	}
	
	

}
