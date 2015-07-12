package com.caritos.student_information_system.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class AppEventBus implements SubscriberExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(AppEventBus.class);
	private EventBus eventBus;

	public AppEventBus() {
		eventBus = new EventBus(this);
	}

	public void post(IAppEvent event) {
		eventBus.post(event);
	}

	public void register(Object object) {
		eventBus.register(object);
	}

	public void unregister(Object object) {
		eventBus.unregister(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.common.eventbus.SubscriberExceptionHandler#handleException
	 * (java.lang.Throwable,
	 * com.google.common.eventbus.SubscriberExceptionContext)
	 */
	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		logger.error(exception.getMessage(), exception);
	}

	//
	public static class UserLoginEvent implements IAppEvent {
		private String username;
		private String password;

		public UserLoginEvent(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}

	public static class UserLogoutEvent implements IAppEvent {

	}

	public static class UserProfileUpdateEvent implements IAppEvent {
	}

	public static final class PostViewChangeEvent implements IAppEvent {
		private final ViewTypes view;

		public PostViewChangeEvent(final ViewTypes view) {
			this.view = view;
		}

		public ViewTypes getView() {
			return view;
		}
	}

	public static class BrowserResizeEvent implements IAppEvent {

	}

	public static class CloseOpenWindowsEvent implements IAppEvent {
	}
}
