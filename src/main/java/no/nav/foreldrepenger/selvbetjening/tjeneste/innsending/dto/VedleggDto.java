package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class VedleggDto {
    public String type;
    public VedleggMetadataDto metadata;
    public byte[] vedlegg;

    public VedleggDto() {
        metadata = new VedleggMetadataDto();
    }

    public class VedleggMetadataDto {
        public String id;
        public String dokumentType;
        public String innsendingsType;
    }
}
