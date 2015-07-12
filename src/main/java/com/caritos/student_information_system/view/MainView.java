package com.caritos.student_information_system.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainView extends HorizontalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "main_view";

	public MainView(SpringViewProvider provider) {
		init(provider);
	}

	void init(SpringViewProvider provider) {
		setSizeFull();
		addStyleName("mainview");

		addComponent(new AppMenu());

		ComponentContainer content = new CssLayout();
		content.addStyleName("view-content");
		content.setSizeFull();
		addComponent(content);
		setExpandRatio(content, 1.0f);

		Navigator navigator = new AppNavigator(content);
		navigator.addProvider(provider);
		ApplicationUI.getCurrent().setNavigator(navigator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener
	 * .ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
