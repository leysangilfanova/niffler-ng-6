package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().spendUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        try {
            Response<SpendJson> response = spendApi.addSpend(spend).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to create spend: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating spend", e);
        }
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpends(String username, CurrencyValues filterCurrency, Date from, Date to) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .getSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public void removeSpend(String username, List<String> ids) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .removeSpend(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(202, response.code());
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        try {
            Response<CategoryJson> response = spendApi.addCategory(category).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to create category: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating category", e);
        }
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi
                    .updateCategory(category)
                    .execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi
                    .getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Deleting a category is not supported by API");
    }
}
