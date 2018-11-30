package no.nav.foreldrepenger.selvbetjening.config;

import static java.util.Arrays.asList;

import java.net.URI;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.collect.ImmutableMap;

import no.nav.foreldrepenger.selvbetjening.filters.ApiKeyInjectingClientInterceptor;
import no.nav.foreldrepenger.selvbetjening.filters.CorsInterceptor;
import no.nav.foreldrepenger.selvbetjening.tjeneste.StatusCodeConvertingResponseErrorHandler;
import no.nav.foreldrepenger.selvbetjening.tjeneste.TokenHandler;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingConfig;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.OppslagConfig;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {

    @Value("${apikeys.key:x-nav-apiKey}")
    private String key;

    @Value("${FPSOKNAD_MOTTAK_API_URL}")
    private URI mottakServiceUri;

    @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_MOTTAK_API_APIKEY_PASSWORD}")
    String innsendingApiKey;

    @Value("${FORELDREPENGESOKNAD_API_FPSOKNAD_OPPSLAG_API_APIKEY_PASSWORD}")
    String oppslagApiKey;

    @Inject
    CorsInterceptor corsInterceptor;

    @Bean
    public RestTemplate restTemplate(TokenHandler tokenHandler, ClientHttpRequestInterceptor... interceptors) {
        RestTemplate template = new RestTemplate();
        template.setInterceptors(asList(interceptors));
        template.setErrorHandler(new StatusCodeConvertingResponseErrorHandler(tokenHandler));
        return template;
    }

    @Bean
    public ClientHttpRequestInterceptor apiKeyInjectingClientInterceptor(OppslagConfig oppslag,
            InnsendingConfig innsending) {
        return new ApiKeyInjectingClientInterceptor(key,
                ImmutableMap.<URI, String>builder()
                        .put(innsending.getURI(), innsendingApiKey)
                        .put(oppslag.getURI(), oppslagApiKey)
                        .build());

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }
}