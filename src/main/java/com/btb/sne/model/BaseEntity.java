package com.btb.sne.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;

@Node
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity {

    @Id
    private String conceptUri;

    @Version
    private Long version;

    public BaseEntity(String conceptUri) {
        this.conceptUri = conceptUri;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Objects.requireNonNull(conceptUri, "conceptUri must be non null"));
    }
}
