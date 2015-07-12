package com.caritos.student_information_system.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.repository.PersonRepository;
import com.caritos.student_information_system.view.AppEventBus.UserLoginEvent;
import com.caritos.student_information_system.view.AppEventBus.UserLogoutEvent;
import com.caritos.student_information_system.view.dashboard.DashboardView;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("dashboard")
@Widgetset("com.caritos.student_information_system.widgetset.StudentInformationSystemWidgetSet")
@SpringUI("")
@Title("Student Information System")
public class ApplicationUI extends UI {

	@Autowired
	private SpringViewProvider viewProvider;
	@Autowired
	private PersonRepository userRepository;

	private AppEventBus appEventBus;

	public ApplicationUI() {
		appEventBus = new AppEventBus();
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		register(this);
		loadContent();
	}

	private void loadContent() {
		VaadinSession session = VaadinSession.getCurrent();
		Person user = session.getAttribute(Person.class);
		if (user != null) {
			removeStyleName("loginview");
			setContent(new MainView(viewProvider));
			getNavigator().navigateTo(DashboardView.VIEW_NAME);
		} else {
			// need this to auto wire needed in registration
			Navigator navigator = new Navigator(this, this);
			navigator.addProvider(viewProvider);
			setNavigator(navigator);

			getNavigator().navigateTo(LoginView.VIEW_NAME);
			addStyleName("loginview");
		}
	}

	@Subscribe
	public void userLogin(UserLoginEvent loginEvent) {
		List<Person> users = userRepository.findByUsernameAndPassword(loginEvent.getUsername(), loginEvent.getPassword());
		if (users != null && users.size() > 0) {
			VaadinSession.getCurrent().setAttribute(Person.class, users.get(0));
		} else {
			Notification.show("Invalid username/password combination.", Type.ERROR_MESSAGE);
			return;
		}
		loadContent();
	}

	@Subscribe
	public void logout(UserLogoutEvent event) {
		VaadinSession.getCurrent().close();
		Page.getCurrent().reload();
	}

	//
	public ApplicationUI register(Object obj) {
		appEventBus.register(obj);
		return this;
	}

	public ApplicationUI unregister(Object obj) {
		appEventBus.unregister(obj);
		return this;
	}

	public ApplicationUI post(IAppEvent obj) {
		appEventBus.post(obj);
		return this;
	}

	public static ApplicationUI current() {
		return (ApplicationUI) UI.getCurrent();
	}

}
