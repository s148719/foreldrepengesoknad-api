package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.SøknadDto;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "true")
public class InnsendingstjenesteStub implements Innsending {

    private static final Logger LOG = getLogger(InnsendingstjenesteStub.class);

    @Inject
    private ObjectMapper mapper;

    @Override
    public ResponseEntity<Kvittering> sendInn(Søknad søknad, MultipartFile ...vedlegg)  {
        søknad.opprettet = now();
        return postStub(søknad);
    }

    private ResponseEntity<Kvittering> postStub(Søknad søknad)  {
        SøknadDto dto;
        if (søknad instanceof Engangsstønad) {
            dto = new EngangsstønadDto((Engangsstønad) søknad);
        } else if (søknad instanceof Foreldrepengesøknad) {
            dto = new ForeldrepengesøknadDto((Foreldrepengesøknad) søknad);
        } else {
            throw new BadRequestException("Unknown application type");
        }

        try {
            LOG.info("Posting JSON (stub): {}", mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(Kvittering.STUB, OK);
    }
}