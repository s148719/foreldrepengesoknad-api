package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;

@Component
public class HistorikkConnection extends AbstractRestConnection {

    private final HistorikkConfig config;

    public HistorikkConnection(RestOperations operations, HistorikkConfig config) {
        super(operations);
        this.config = config;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    public List<Melding> hentMeldinger() {
        List<Melding> meldinger = Optional.ofNullable(getForObject(config.historikkURI(), Melding[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
        LOG.trace(CONFIDENTIAL, "Fikk meldinger {}", meldinger);
        return meldinger;
    }

    @Override
    protected URI pingURI() {
        return config.pingURI();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}