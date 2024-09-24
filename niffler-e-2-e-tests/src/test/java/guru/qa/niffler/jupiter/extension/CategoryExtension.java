package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    private final Faker faker = new Faker();
    private final CategoryDbClient categoryDbClient = new CategoryDbClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (userAnno.categories().length > 0) {
                        Category anno = userAnno.categories()[0];
                        String categoryName = getCategoryName(anno);

                        CategoryJson category = createCategoryJson(categoryName, userAnno.username(), false);
                        CategoryJson createdCategory = categoryDbClient.createCategoryJson(category);

                        if (anno.archived()) {
                            createdCategory = updateCategoryArchivedStatus(createdCategory, true);
                        }

                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                    }
                });
    }

    private String getCategoryName(Category anno) {
        return anno.title().isEmpty() ? faker.animal().name() : anno.title();
    }

    private CategoryJson createCategoryJson(String name, String username, boolean archived) {
        return new CategoryJson(null, name, username, archived);
    }

    private CategoryJson updateCategoryArchivedStatus(CategoryJson category, boolean archived) {
        return new CategoryJson(category.id(), category.name(), category.username(), archived);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            CategoryJson updatedCategory = updateCategoryArchivedStatus(category, true);
            categoryDbClient.updateCategory(updatedCategory);
        }
    }
}
