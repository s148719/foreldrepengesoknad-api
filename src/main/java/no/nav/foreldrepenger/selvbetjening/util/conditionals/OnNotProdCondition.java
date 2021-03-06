package no.nav.foreldrepenger.selvbetjening.util.conditionals;

import org.springframework.core.type.AnnotatedTypeMetadata;

import no.nav.foreldrepenger.selvbetjening.util.Cluster;

public class OnNotProdCondition extends OnClusterCondition {

    @Override
    protected Cluster[] clusters(AnnotatedTypeMetadata md) {
        return Cluster.notProdClusters();
    }
}