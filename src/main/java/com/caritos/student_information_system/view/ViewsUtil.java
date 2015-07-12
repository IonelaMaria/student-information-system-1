package com.caritos.student_information_system.view;

import java.util.Set;

import com.caritos.student_information_system.domain.Person;
import com.google.common.collect.Sets;
import com.vaadin.data.Container;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

public class ViewsUtil {
	@SuppressWarnings("unchecked")
	public static <E> Set<E> tableValue(Table table) {
		if (table.isMultiSelect()) {
			return (Set<E>) table.getValue();
		}
		return Sets.newHashSet();
	}

	public static Person currentUser() {
		VaadinSession sess = VaadinSession.getCurrent();
		Person user = sess.getAttribute(Person.class);
		return user;
	}

	public static <E> void removeGridRow(Container container, E itemId) {
		container.removeItem(itemId);
	}

	public static <E> void addGridRow(Container container, E itemId) {
		container.addItem(itemId);
	}

	public static void showSuccessNotification(String title, String msg) {
		Notification notification = new Notification(title);
		notification.setDescription("<span>" + msg + "</span>");
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray dark small closable login-help");
		notification.setPosition(Position.MIDDLE_CENTER);
		notification.show(Page.getCurrent());
		
	}
}
