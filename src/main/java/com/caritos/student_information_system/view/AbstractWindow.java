package com.caritos.student_information_system.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractWindow extends Window {

	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	private String okCaption;
	private String cancelCaption;

	public AbstractWindow(String caption) {
		this(caption, "Save", "Cancel");
	}

	public AbstractWindow(String caption, String okCaption, String cancelCaption) {
		super(caption);
		this.okCaption = okCaption;
		this.cancelCaption = cancelCaption;
		Responsive.makeResponsive(this);
		center();
		setModal(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(true);
		setClosable(true);
		setWidth("40%");
		setHeight("75%");
	}

	@Override
	public void attach() {
		super.attach();
		if (mainLayout == null) {
			mainLayout = new VerticalLayout();
			mainLayout.setSizeFull();
			mainLayout.setMargin(new MarginInfo(true, false, false, false));
			mainLayout.setSpacing(true);

			Component contents = buildContents();
			mainLayout.addComponent(contents);
			mainLayout.setComponentAlignment(contents, Alignment.MIDDLE_CENTER);
			mainLayout.setExpandRatio(contents, 1.0f);
			mainLayout.addComponent(buildButtons());
		}
		setContent(mainLayout);
	}

	private AbstractOrderedLayout buildButtons() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		layout.setWidth("100%");
		layout.setSpacing(true);

		HorizontalLayout wrapLayout = new HorizontalLayout();
		wrapLayout.setSpacing(true);

		Button okBtn = new Button(okCaption);
		okBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		okBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				okEvent(event);
			}
		});
		wrapLayout.addComponent(okBtn);
		//
		Button cancelBtn = new Button(cancelCaption);
		cancelBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				cancelEvent(event);
				UI.getCurrent().removeWindow(AbstractWindow.this);
			}
		});
		wrapLayout.addComponent(cancelBtn);

		layout.addComponent(wrapLayout);
		layout.setComponentAlignment(wrapLayout, Alignment.TOP_RIGHT);
		return layout;
	}

	protected abstract AbstractOrderedLayout buildContents();

	protected abstract void okEvent(ClickEvent event);

	protected abstract void cancelEvent(ClickEvent event);
}
