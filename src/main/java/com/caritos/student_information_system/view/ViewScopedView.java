package com.caritos.student_information_system.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(ViewScopedView.VIEW_NAME)
public class ViewScopedView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "view";
	
	@Autowired
	private ViewGreeter viewGreeter;
	@Autowired
	private Greeter uiGreeter;
	
	@PostConstruct
	void init() {
		setMargin(true);
		setSpacing(true);
		addComponent(new Label("This is a view scoped view"));
		addComponent(new Label(uiGreeter.sayHello()));
		addComponent(new Label(viewGreeter.sayHello()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// this view is constructed in the init() method
	}

}
