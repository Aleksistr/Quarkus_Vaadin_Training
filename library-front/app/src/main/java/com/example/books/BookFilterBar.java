package com.example.books;

import com.example.books.entities.BookFilter;
import com.example.settings.category.entities.Category;
import com.example.settings.category.CategoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class BookFilterBar extends HorizontalLayout {

    private final CategoryService categoryService = new CategoryService();
    private final Binder<BookFilter> binder = new Binder<>(BookFilter.class);

    public BookFilterBar() {
        List<Category> categories = categoryService.getCategories();

        setWidthFull();
        setAlignItems(Alignment.END);

        // Define each fields end buttons
        TextField title = new TextField("Title");

        Select<Category> categorySelect = new Select<>("Category");
        categorySelect.setItems(categories);
        categorySelect.setRenderer(new ComponentRenderer<Component, Category>(category -> new Span(category.getName())));

        NumberField minPriceField = new NumberField("Min price");
        minPriceField.setSuffixComponent(VaadinIcon.EURO.create());
        minPriceField.setMin(0);

        NumberField maxPriceField = new NumberField("Max price");
        maxPriceField.setSuffixComponent(VaadinIcon.EURO.create());

        Button filterButton = new Button("Filter");
        filterButton.setSuffixComponent(VaadinIcon.FILTER.create());
        filterButton.addClickListener(event -> {
            BookFilter filter = new BookFilter();
            if (!binder.writeBeanIfValid(filter)) {
                Notification.show("Invalid filter");
                return;
            }

            fireEvent(new BookFilterEvent(this, filter));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setSuffixComponent(VaadinIcon.CLOSE.create());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(e -> {
            binder.readBean(new BookFilter());
            fireEvent(new BookFilterEvent(this, new BookFilter()));
        });

        add(title, categorySelect, minPriceField, maxPriceField, filterButton, cancelButton);

        // Define the binders
        binder.forField(title).bind(BookFilter::getTitle, BookFilter::setTitle);

        binder.forField(categorySelect).bind(BookFilter::getCategory, BookFilter::setCategory);

        binder.forField(minPriceField)
                .bind(BookFilter::getMinPrice, BookFilter::setMinPrice);

        binder.forField(maxPriceField)
                .bind(BookFilter::getMaxPrice, BookFilter::setMaxPrice);
    }

    // Custom event definition
    public static class BookFilterEvent extends ComponentEvent<BookFilterBar> {
        private final BookFilter filter;

        public BookFilterEvent(BookFilterBar source, BookFilter filter) {
            super(source, false);
            this.filter = filter;
        }

        public BookFilter getFilter() {
            return this.filter;
        }
    }

    public Registration addBookFilterListener(ComponentEventListener<BookFilterEvent> listener) {
        return addListener(BookFilterEvent.class, listener);
    }
}
