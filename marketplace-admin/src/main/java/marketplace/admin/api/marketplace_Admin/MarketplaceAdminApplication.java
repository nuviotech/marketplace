package marketplace.admin.api.marketplace_Admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import okhttp3.OkHttpClient;


@SpringBootApplication
public class MarketplaceAdminApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MarketplaceAdminApplication.class, args);
	}
	
	@Bean
	public OkHttpClient getOkHttpClient() {
		return new OkHttpClient();
	}

}
