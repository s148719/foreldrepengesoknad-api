package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public interface Minidialog extends Pingable {

    List<MinidialogInnslag> hentMinidialoger();

    List<MinidialogInnslag> hentMinidialoger(Fødselsnummer fnr);

}
