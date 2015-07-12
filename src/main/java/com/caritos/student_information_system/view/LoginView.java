package com.caritos.student_information_system.view;

import javax.annotation.PostConstruct;

import com.caritos.student_information_system.view.AppEventBus.UserLoginEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.VaadinUIScope;
import com.vaadin.spring.navigator.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@VaadinUIScope
@SpringView(LoginView.VIEW_NAME)
@SuppressWarnings("serial")
public class LoginView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "login";

	public LoginView() {
	}

	@PostConstruct
	void init() {
		setSizeFull();
		Component loginForm = buildLoginForm();
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}

	private Component buildLoginForm() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.setSpacing(true);
		Responsive.makeResponsive(layout);
		layout.addStyleName("login-panel");

		layout.addComponent(buildLabels());
		layout.addComponent(buildFields());
		layout.addComponent(new CheckBox("Remember me", false));
		return layout;
	}

	private Component buildFields() {
		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addStyleName("fields");

		final TextField username = new TextField("Username");
		username.setRequired(true);
		username.setRequiredError("Username is required.");
		username.setIcon(FontAwesome.USER);
		username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final PasswordField password = new PasswordField("Password");
		password.setRequired(true);
		password.setRequiredError("Username is required.");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final Button signin = new Button("Sign In");
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		signin.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				try {
					username.validate();
					password.validate();
				} catch (Exception e) {
					Notification.show(e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), Notification.Type.WARNING_MESSAGE);
					return;
				}
				String usernameValue = username.getValue();
				String passwordValue = password.getValue();
				try {
					ApplicationUI.current().post(new UserLoginEvent(usernameValue, passwordValue));
				} catch (Exception e) {
					Notification.show(e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), Notification.Type.WARNING_MESSAGE);
				}
			}
		});

//		Button registerBtn = new Button("Register");
//		registerBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		registerBtn.addClickListener(new Button.ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				UI.getCurrent().addWindow(
//						new AddEditAdministratorView("Register", new User()) {
//
//							private static final long serialVersionUID = 1L;
//
//							@Override
//							protected void save(User user) {
//								userRepository.save(user);
//							}
//						});
//			}
//		});
//		fields.addComponents(username, password, signin, registerBtn);
		
		fields.addComponents(username, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
//		fields.setComponentAlignment(registerBtn, Alignment.BOTTOM_LEFT);
		return fields;
	}

	private Component buildLabels() {
		CssLayout labels = new CssLayout();
		labels.addStyleName("labels");

		Label welcome = new Label("Welcome");
		welcome.setSizeUndefined();
		welcome.addStyleName(ValoTheme.LABEL_H4);
		welcome.addStyleName(ValoTheme.LABEL_COLORED);
		labels.addComponent(welcome);

		Label title = new Label("Student Information System");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H3);
		title.addStyleName(ValoTheme.LABEL_LIGHT);
		labels.addComponent(title);
		return labels;
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
		// do nothing for now
	}
}
