package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public abstract class J_BaseEntity {

    @Id
    private String conceptUri;

    @Version
    private Long version;

    public J_BaseEntity(String conceptUri) {
        this.conceptUri = conceptUri;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Objects.requireNonNull(conceptUri, "conceptUri must be non null"));
    }
}
