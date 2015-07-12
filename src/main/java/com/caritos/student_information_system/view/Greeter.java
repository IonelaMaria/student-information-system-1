package com.caritos.student_information_system.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinUIScope;

@SpringComponent
@VaadinUIScope
public class Greeter {
	
	public String sayHello() {
		return "Hello from bean " + toString();
	}

}
