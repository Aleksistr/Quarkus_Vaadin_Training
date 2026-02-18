package com.example.settings.category.components;

import com.example.settings.category.Category;
import com.example.settings.category.CategoryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CreateCategoryFormComponent extends FormLayout {

    private final CategoryService categoryService = new CategoryService();
    private final Binder<Category> binder = new Binder<>(Category.class);

    private Runnable onSaveCallBack;

    public CreateCategoryFormComponent() {

        setRowSpacing("20px");

        H4 h4 = new H4("Create Category");
        add(h4);

        TextField nameField = new TextField();
        nameField.setPlaceholder("Category name");
        nameField.setRequired(true);
        nameField.setWidthFull();

        add(nameField);

        binder.forField(nameField)
                .asRequired("Category name is required")
                .bind(Category::getName, Category::setName);

        HorizontalLayout buttons = new HorizontalLayout();

        Button saveButton = new Button("Save", e -> {
            saveCategory();
        });

        Button cancelButton = new Button("Cancel", e -> {
           binder.readBean(null);
        });

        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        buttons.add(saveButton, cancelButton);
        add(buttons);
    }

    private void saveCategory() {
        Category category = new Category();

        if (!binder.writeBeanIfValid(category)) {
            Notification.show("Invalid category");
            return;
        }

        try {
            Category created = categoryService.createCategory(category);
            Notification.show("Category created: " + created.getName());

            if (onSaveCallBack != null) {
                onSaveCallBack.run();
            }

            binder.readBean(null);
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }

    public void setOnSaveCallBack(Runnable onSaveCallBack) {
        this.onSaveCallBack = onSaveCallBack;
    }
}
