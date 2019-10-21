package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;

public class GCPCloudStorage implements Storage {

    private static final String BUCKET_FORELDREPENGER = "foreldrepengesoknad";
    private static final String BUCKET_FORELDREPENGER_MELLOMLAGRING = "mellomlagring";

    private static final Logger LOG = LoggerFactory.getLogger(GCPCloudStorage.class);

    private final com.google.cloud.storage.Storage storage;

    public GCPCloudStorage() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public String ping() {
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://localhost");
    }

    @Override
    public void put(String directory, String key, String value) {
        writeString(BUCKET_FORELDREPENGER, directory, key, value);
    }

    @Override
    public void putTmp(String directory, String key, String value) {
        writeString(BUCKET_FORELDREPENGER_MELLOMLAGRING, directory, key, value);
    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.ofNullable(readString(BUCKET_FORELDREPENGER, directory, key));
    }

    @Override
    public Optional<String> getTmp(String directory, String key) {
        return Optional.ofNullable(readString(BUCKET_FORELDREPENGER_MELLOMLAGRING, directory, key));
    }

    @Override
    public void delete(String directory, String key) {
        deleteString(BUCKET_FORELDREPENGER, directory, key);
    }

    private void deleteString(String bucketName, String directory, String key) {
        LOG.info("Fjerner object fra bøtte {}, katalog {}", bucketName, directory);
        storage.delete(BlobId.of(bucketName, fileName(directory, key)));
        LOG.info("Fjernet objekt {} fra bøtte {}", directory, bucketName);
    }

    @Override
    public void deleteTmp(String directory, String key) {
        deleteString(BUCKET_FORELDREPENGER_MELLOMLAGRING, directory, key);
    }

    private void writeString(String bucketName, String directory, String key, String value) {
        LOG.info("Lagrer objekt i bøtte {}, katalog {}", bucketName, directory);
        Blob blob = storage.create(
                BlobInfo.newBuilder(BlobId.of(bucketName, fileName(directory, key)))
                        .setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE).build(),
                value.getBytes(StandardCharsets.UTF_8));
        LOG.info("Lagret objekt {} i bøtte {}", blob, bucketName);
    }

    private String readString(String bucketName, String directory, String key) {
        String path = fileName(directory, key);
        try {
            LOG.info("Henter objekt fra bøtte {}, katalog {}", bucketName, directory);
            String value = new String(storage.get(bucketName, path).getContent(), StandardCharsets.UTF_8);
            LOG.info("Hentet objekt {} fra bøtte {}", value, bucketName);
            return value;
        } catch (Exception e) {
            LOG.trace("Kunne ikke hente {}, finnes sannsynligvis ikke", path, e);
            return null;
        }
    }

    private static String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
