package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;

public class CategoryDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return categoryDao.findAllByUsername(username);
    }

    public void deleteCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }
}
