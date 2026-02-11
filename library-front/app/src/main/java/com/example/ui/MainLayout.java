package com.example.ui;

import com.example.books.BookListView;
import com.example.settings.SettingsLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle drawerToggle = new DrawerToggle();
        H1 h1 = new H1("Library");

        addToNavbar(drawerToggle, h1);

        final VerticalLayout menuBar = new VerticalLayout();
        menuBar.add(new RouterLink(BookListView.TITLE, BookListView.class));
        menuBar.add(new RouterLink(SettingsLayout.TITLE, SettingsLayout.class));

        addToDrawer(menuBar);
    }
}
