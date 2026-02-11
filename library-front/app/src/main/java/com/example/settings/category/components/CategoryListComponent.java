package com.example.settings.category.components;

import com.example.settings.category.Category;
import com.example.settings.category.CategoryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class CategoryListComponent extends VerticalLayout {

    private final CategoryService categoryService = new CategoryService();
    private final VirtualList<Category> virtualList = new VirtualList<>();


    public CategoryListComponent() {
        setSizeFull();

        virtualList.setItems(categoryService.getCategories());

        // Define category item renderer
        virtualList.setRenderer(new ComponentRenderer<>( category -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(new Span(category.getName()));

            Button deleteButton = new Button("Delete", event -> {
                deleteCategory(category);
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            horizontalLayout.add(deleteButton);
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

            return horizontalLayout;
        }));

        add(virtualList);

    }

    private void deleteCategory(Category category) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Confirmation - Delete Category");
        confirmDialog.setText("Are you sure you want to delete this category ".concat(category.getName()).concat("?"));

        confirmDialog.setCancelable(true);

        confirmDialog.setCancelText("No");
        confirmDialog.setConfirmText("Yes");
        confirmDialog.addConfirmListener(e -> {
            this.removeCategory(category);
        });

        confirmDialog.open();
    }

    private void removeCategory(Category category) {
        categoryService.deleteCategory(category);
        this.refreshList();
    }

    public void refreshList() {
        virtualList.setItems(categoryService.getCategories());
    }
}
