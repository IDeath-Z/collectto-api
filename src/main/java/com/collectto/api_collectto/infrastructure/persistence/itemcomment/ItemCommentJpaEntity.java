package com.collectto.api_collectto.infrastructure.persistence.itemcomment;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "item_comments")
public class ItemCommentJpaEntity {

    @Id
    @Column(name = "comment_id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_item"))
    private ItemJpaEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_author"))
    private UserJpaEntity author;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

}
