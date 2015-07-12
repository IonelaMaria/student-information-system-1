package com.caritos.student_information_system.view;

import com.caritos.student_information_system.view.AppEventBus.BrowserResizeEvent;
import com.caritos.student_information_system.view.AppEventBus.CloseOpenWindowsEvent;
import com.caritos.student_information_system.view.AppEventBus.PostViewChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

public class AppNavigator extends Navigator {
	
	private static final long serialVersionUID = 1L;

	public AppNavigator(final SingleComponentContainer container) {
		super(UI.getCurrent(), container);
		initViewChangeListener();
	}

	public AppNavigator(final ComponentContainer container) {
		super(UI.getCurrent(), container);
		initViewChangeListener();
	}

	private void initViewChangeListener() {
		addViewChangeListener(new ViewChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean beforeViewChange(final ViewChangeEvent event) {
				// Since there's no conditions in switching between the views
				// we can always return true.
				return true;
			}

			@Override
			public void afterViewChange(final ViewChangeEvent event) {
				ViewTypes view = ViewTypes.getByViewName(event.getViewName());
				// Appropriate events get fired after the view is changed.
				ApplicationUI.current().post(new PostViewChangeEvent(view));
				ApplicationUI.current().post(new BrowserResizeEvent());
				ApplicationUI.current().post(new CloseOpenWindowsEvent());
			}
		});
	}

}
