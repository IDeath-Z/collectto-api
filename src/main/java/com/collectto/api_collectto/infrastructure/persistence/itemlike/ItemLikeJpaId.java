package com.collectto.api_collectto.infrastructure.persistence.itemlike;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ItemLikeJpaId implements Serializable {

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "liker_id")
    private UUID likerId;
}
