package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    @Nonnull
    public SpendJson createSpend(@Nonnull SpendJson spend) {
        return xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepository.create(SpendEntity.fromJson(spend))
                )
        );
    }

    @Override
    @Nonnull
    public CategoryJson createCategory(@Nonnull CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        );
    }

    @Override
    public void removeCategory(@Nonnull CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}

