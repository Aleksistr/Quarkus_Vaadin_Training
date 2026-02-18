package org.acme.category;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CategoryService {

    @Inject
    private CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.listAll();
    }

    public Category createCategory(Category category){
        categoryRepository.persist(category);
        return category;
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
