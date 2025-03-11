package org.example.events.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI OpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Events API")
                        .description("Events API, desafio 3 implemented in aws EC2 instance")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact().name("Guilherme Weissheimer").email("guilherme.weisshe.pb@compasso.com.br"))
                );
    }
}
