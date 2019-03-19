package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Behandling;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class InnsynTjenesteStub implements Innsyn {

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public URI pingURI() {
        return URI.create("http.//www.vg.no");
    }

    @Override
    public Uttaksplan hentUttaksplan(String saksnummer) {
        return null;
    }

    @Override
    public List<Sak> hentSaker() {
        LocalDateTime mottattdato = LocalDateTime.now().minusDays(7);

        Behandling behandling = new Behandling(mottattdato, mottattdato.plusHours(2), "AVSLU", "FP",
                "FORP_FODS", null, null, "4869", "NAV Torrevieja", Collections.emptyList());

        return Arrays.asList(
                new Sak("SAK", "LA8PV", "UBEH", now().minusYears(1), emptyList()),
                new Sak("FPSAK", "424242424", "LOP", mottattdato.toLocalDate(), singletonList(behandling)));
    }

    @Override
    public Vedtak hentVedtak(String saksnummer) {
        return null;
    }
}
