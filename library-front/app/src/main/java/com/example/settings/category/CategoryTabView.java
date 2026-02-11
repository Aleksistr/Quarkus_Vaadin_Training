package com.example.settings.category;

import com.example.settings.SettingsLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle(CategoryTabView.TITLE)
@Route(value = CategoryTabView.ROUTE, layout = SettingsLayout.class)
public class CategoryTabView extends HorizontalLayout {

    public static final String ROUTE = "settings/category";
    public static final String TITLE = "Category";

    public CategoryTabView() {
        setSizeFull();

    }

}
