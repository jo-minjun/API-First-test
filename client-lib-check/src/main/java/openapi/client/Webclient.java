package openapi.client;

import minjun.openapi.ApiClient;
import minjun.openapi.api.StoreApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Webclient {

    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }

    @Bean
    public StoreApi storeApi(ApiClient apiClient) {
        return new StoreApi(apiClient);
    }
}
