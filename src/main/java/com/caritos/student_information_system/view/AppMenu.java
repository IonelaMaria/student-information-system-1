package com.caritos.student_information_system.view;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.SuperUser;
import com.caritos.student_information_system.domain.Person;
import com.caritos.student_information_system.view.AppEventBus.PostViewChangeEvent;
import com.caritos.student_information_system.view.AppEventBus.UserLogoutEvent;
import com.caritos.student_information_system.view.AppEventBus.UserProfileUpdateEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

public class AppMenu extends CustomComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String ID = "application-menu";
	private static final String STYLE_VISIBLE = "valo-menu-visible";
	private MenuItem settingsItem;

	public AppMenu() {
		addStyleName("valo-menu");
		setId(ID);
		setSizeUndefined();
		ApplicationUI.current().register(this);
	}

	@Override
	public void attach() {
		super.attach();
		setCompositionRoot(buildContent());
	}

	private Component buildContent() {
		final CssLayout menuContent = new CssLayout();
		menuContent.addStyleName("sidebar");
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.addStyleName("no-vertical-drag-hints");
		menuContent.addStyleName("no-horizontal-drag-hints");
		menuContent.setWidth(null);
		menuContent.setHeight("100%");

		menuContent.addComponent(buildTitle());
		menuContent.addComponent(buildUserMenu());
		menuContent.addComponent(buildToggleButton());
		menuContent.addComponent(buildMenuItems());

		return menuContent;
	}

	private Component buildToggleButton() {
		Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
			/**
				 *
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
					getCompositionRoot().removeStyleName(STYLE_VISIBLE);
				} else {
					getCompositionRoot().addStyleName(STYLE_VISIBLE);
				}
			}
		});
		valoMenuToggleButton.setIcon(FontAwesome.LIST);
		valoMenuToggleButton.addStyleName("valo-menu-toggle");
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return valoMenuToggleButton;
	}

	private Component buildUserMenu() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");
		// final User user = getCurrentUser();
		settingsItem = settings.addItem("", new ThemeResource(
				"img/profile-pic-300px.jpg"), null);
		updateUserName(null);
//		settingsItem.addItem("Edit Profile", new Command() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void menuSelected(final MenuItem selectedItem) {
//			}
//		});
//		settingsItem.addItem("Preferences", new Command() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void menuSelected(final MenuItem selectedItem) {
//			}
//		});
//		settingsItem.addSeparator();
		settingsItem.addItem("Sign Out", new Command() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				ApplicationUI.current().post(new UserLogoutEvent());
			}
		});
		return settings;
	}

	private Component buildTitle() {
		Label logo = new Label("Student Information System", ContentMode.HTML);
		logo.setSizeUndefined();
		HorizontalLayout logoWrapper = new HorizontalLayout(logo);
		logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
		logoWrapper.addStyleName("valo-menu-title");
		return logoWrapper;
	}

	private Component buildMenuItems() {
		CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");
		menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);

		for (final ViewTypes view : getUsersViewTypes()) {
			Component menuItemComponent = new ValoMenuItemButton(view);

			menuItemsLayout.addComponent(menuItemComponent);
		}
		return menuItemsLayout;
	}

	private List<ViewTypes> getUsersViewTypes(){
		Person user = getCurrentUser();
		List<ViewTypes> viewableViewTypes = new ArrayList<>();

		if( user instanceof SuperUser){
			viewableViewTypes = getSuperuserViewTypes();
		}else if( user instanceof Administrator){
			viewableViewTypes = getAdministratorViewTypes();
		}else if( user instanceof Guardian){
			viewableViewTypes = getGuardianViewTypes();
		}

		return viewableViewTypes;
	}

	private List<ViewTypes> getSuperuserViewTypes(){
		final List<ViewTypes> viewableViewTypes = getAdministratorViewTypes();
		viewableViewTypes.add(1, ViewTypes.SUPERUSER);
		viewableViewTypes.add(2, ViewTypes.ORGANIZATION);
		return viewableViewTypes;
	}


	private List<ViewTypes> getAdministratorViewTypes(){
		final List<ViewTypes> viewableViewTypes = getGuardianViewTypes();
		viewableViewTypes.add(1, ViewTypes.ADMINISTRATOR);
		viewableViewTypes.add(2, ViewTypes.GUARDIAN);
		viewableViewTypes.add(5, ViewTypes.SCHOOL);
		viewableViewTypes.add(6, ViewTypes.GRADE);
		viewableViewTypes.add(7, ViewTypes.SECTION);
		viewableViewTypes.add(8, ViewTypes.CATEGORY);
		return viewableViewTypes;
	}

	private List<ViewTypes> getGuardianViewTypes(){
		final List<ViewTypes> viewableViewTypes = new ArrayList<>();
		viewableViewTypes.add(ViewTypes.DASHBOARD);
		viewableViewTypes.add(ViewTypes.STUDENT);
		viewableViewTypes.add(ViewTypes.TRANSACTION);
		return viewableViewTypes;
	}


	@Subscribe
	public void updateUserName(final UserProfileUpdateEvent event) {
		Person user = getCurrentUser();
		settingsItem.setText(user.getFirstName() + " " + user.getLastName());
	}

	@Subscribe
	public void postViewChange(final PostViewChangeEvent event) {
		// After a successful view change the menu can be hidden in mobile view.
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}

	public final class ValoMenuItemButton extends Button {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private static final String STYLE_SELECTED = "selected";

		private final ViewTypes view;

		public ValoMenuItemButton(final ViewTypes view) {
			this.view = view;
			setPrimaryStyleName("valo-menu-item");
			setIcon(view.getIcon());
			setCaption(view.getDescription());
			addClickListener(new ClickListener() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					UI.getCurrent().getNavigator()
							.navigateTo(view.getViewName());
				}
			});
			ApplicationUI.current().register(this);
		}

		@Subscribe
		public void postViewChange(final PostViewChangeEvent event) {
			removeStyleName(STYLE_SELECTED);
			if (event.getView() == view) {
				addStyleName(STYLE_SELECTED);
			}
		}
	}

	private Person getCurrentUser() {
		return (Person) VaadinSession.getCurrent().getAttribute(Person.class.getName());
	}
}
