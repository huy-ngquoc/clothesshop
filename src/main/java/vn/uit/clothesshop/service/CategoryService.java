package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.uit.clothesshop.customexception.NotFoundException;
import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.repository.CategoryRepository;
import vn.uit.clothesshop.utils.Message;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;
    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    } 
    public Category findById(int categoryId) {
        Category category = categoryRepo.findById(categoryId).orElse(null);
        if(category==null) {
            throw new NotFoundException(Message.categoryNotFound);
        }
        return category;
    }
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }
    public void handleSaveCategory(Category category) {
        categoryRepo.save(category);
    }
}
