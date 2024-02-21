package com.geely.space.commons.web.api.docs.swagger;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.Constants;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
@AutoConfigureBefore(SwaggerConfig.class)
@AutoConfigureAfter(SpringDocConfiguration.class)
public class CommonsSpringDocSwaggerAutoConfiguration {

    protected static class CommonsSwaggerIndexTransformer extends SwaggerIndexPageTransformer {

        private SpringDocConfigProperties springDocConfigProperties;

        /**
         * Instantiates a new Swagger index transformer.
         */
        public CommonsSwaggerIndexTransformer(SpringDocConfigProperties springDocConfigProperties,
                                              SwaggerUiConfigProperties swaggerUiConfig,
                                              SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                              ObjectMapper objectMapper) {
            super(swaggerUiConfig, swaggerUiOAuthProperties, objectMapper);
            this.springDocConfigProperties = springDocConfigProperties;
            swaggerUiConfig.setDisableSwaggerDefaultUrl(true);
        }

        /**
         * @link https://github.com/springdoc/springdoc-openapi/issues/763
         */
        @Override
        protected String overwriteSwaggerDefaultUrl(String html) {
            return html.replace(Constants.SWAGGER_UI_DEFAULT_URL, springDocConfigProperties.getApiDocs().getPath());
        }

    }

    /**
     *
     * @param springDocConfigProperties spring doc configurations
     * @param swaggerUiConfig           the swagger ui config
     * @param swaggerUiOAuthProperties  the swagger ui o auth properties
     * @param objectMapper              the object mapper
     * @return swagger index page transformer
     */
    @Bean
    SwaggerIndexTransformer indexPageTransformer(SpringDocConfigProperties springDocConfigProperties,
                                                 SwaggerUiConfigProperties swaggerUiConfig,
                                                 SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                                 ObjectMapper objectMapper) {
        return new CommonsSwaggerIndexTransformer(
                springDocConfigProperties, swaggerUiConfig, swaggerUiOAuthProperties, objectMapper);
    }

}
