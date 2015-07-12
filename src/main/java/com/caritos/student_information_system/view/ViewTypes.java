package com.caritos.student_information_system.view;

import com.caritos.student_information_system.view.administrator.AdministratorView;
import com.caritos.student_information_system.view.category.CategoryView;
import com.caritos.student_information_system.view.dashboard.DashboardView;
import com.caritos.student_information_system.view.grade.GradeView;
import com.caritos.student_information_system.view.guardian.GuardianView;
import com.caritos.student_information_system.view.organization.OrganizationView;
import com.caritos.student_information_system.view.school.SchoolView;
import com.caritos.student_information_system.view.section.SectionView;
import com.caritos.student_information_system.view.student.StudentView;
import com.caritos.student_information_system.view.superuser.SuperUserView;
import com.caritos.student_information_system.view.transaction.TransactionView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ViewTypes {
	DASHBOARD(DashboardView.VIEW_NAME, "Dashboard", DashboardView.class, FontAwesome.DASHBOARD),
	SUPERUSER(SuperUserView.VIEW_NAME, "SuperUser", SuperUserView.class, FontAwesome.BOLT),
	ORGANIZATION(OrganizationView.VIEW_NAME, "Organization", OrganizationView.class, FontAwesome.BRIEFCASE),
	ADMINISTRATOR(AdministratorView.VIEW_NAME, "Administrator", AdministratorView.class, FontAwesome.USER),
	GUARDIAN(GuardianView.VIEW_NAME, "Guardian", GuardianView.class, FontAwesome.HOME),
	SCHOOL(SchoolView.VIEW_NAME, "School", SchoolView.class, FontAwesome.BUILDING),
	STUDENT(StudentView.VIEW_NAME, "Student", StudentView.class, FontAwesome.BOOK),
	TRANSACTION(TransactionView.VIEW_NAME, "Transaction", TransactionView.class, FontAwesome.MONEY),
	GRADE(GradeView.VIEW_NAME, "Grade", GradeView.class, FontAwesome.MORTAR_BOARD),
	SECTION(SectionView.VIEW_NAME, "Section", SectionView.class, FontAwesome.STAR),
	CATEGORY(CategoryView.VIEW_NAME, "Category", CategoryView.class, FontAwesome.TAG);

	private String viewName;
	private String description;
	private Class<? extends View> clazz;
	private Resource icon;

	ViewTypes(String viewName, String description, Class<? extends View> clazz, Resource icon) {
		this.viewName = viewName;
		this.description = description;
		this.clazz = clazz;
		this.icon = icon;
	}

	public Class<? extends View> getViewClass() {
		return clazz;
	}

	public String getViewName() {
		return viewName;
	}

	public String getDescription() {
		return description;
	}

	public Resource getIcon() {
		return icon;
	}

	public static ViewTypes getByViewName(String name) {
		for (ViewTypes viewType : ViewTypes.values()) {
			if (viewType.getViewName().equalsIgnoreCase(name)) {
				return viewType;
			}
		}
		return null;
	}
}
