package com.caritos.student_information_system.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.navigator.annotation.VaadinViewScope;

@SpringComponent
@VaadinViewScope
public class ViewGreeter {
	public String sayHello() {
		return "Hello form a view scoped bean " + toString();
	}
}
