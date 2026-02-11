package com.example.settings;

import com.example.settings.category.CategoryTabView;
import com.example.ui.MainLayout;
import com.example.ui.RouteTabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Layout
@ParentLayout(MainLayout.class)
@PageTitle(SettingsLayout.TITLE)
@Route(value = SettingsLayout.ROUTE, layout = MainLayout.class)
public class SettingsLayout extends VerticalLayout implements RouterLayout {

    public static final String ROUTE = "settings";
    public static final String TITLE = "Library Settings";

    public SettingsLayout() {
        setSizeFull();

        RouteTabs routeTabs = new RouteTabs();
        routeTabs.add(new RouterLink(CategoryTabView.TITLE, CategoryTabView.class));

        add(routeTabs);
    }

}
