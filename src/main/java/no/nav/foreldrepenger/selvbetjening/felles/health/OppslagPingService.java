package no.nav.foreldrepenger.selvbetjening.felles.health;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OppslagPingService extends AbstractPingService {

    public OppslagPingService(RestTemplate template, @Value("${FPSOKNAD_OPPSLAG_API_URL}") URI baseUri) {
        super(template, baseUri);
    }

    @Override
    protected URI pingURI(String message) {
        return UriComponentsBuilder
                .fromUri(baseUri())
                .path("/oppslag/ping")
                .queryParam("navn", message).build().toUri();
    }

    @Override
    protected Logger logger() {
        return getLogger(getClass());
    }
}