package com.example.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import java.util.HashMap;
import java.util.Map;


public class RouteTabs extends Tabs {

    private final Map<Tab, Class<? extends Component>> routeMap = new HashMap<>();

    public Tab add(String label, Class<? extends Component> targetView) {
        RouterLink link = new RouterLink(label, targetView);
        Tab tab = new Tab(link);

        routeMap.put(tab, targetView);

        link.setHighlightCondition(HighlightConditions.sameLocation());
        link.setHighlightAction((l, highlight) -> {
            if (highlight) setSelectedTab(tab);
        });

        add(tab);
        return tab;
    }

    public void navigateTo(Tab tab) {
        Class<? extends Component> target = routeMap.get(tab);
        if (target != null) {
            UI.getCurrent().navigate(target);
        }
    }


    public Tab findTabForView(Class<? extends Component> viewClass) {
        return routeMap.entrySet().stream()
                .filter(e -> e.getValue().equals(viewClass))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


}
