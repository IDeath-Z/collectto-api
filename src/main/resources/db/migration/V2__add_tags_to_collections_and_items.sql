CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE tags (
    tag_id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE collection_tags (
    collection_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (collection_id, tag_id),
    CONSTRAINT fk_collection_tags_collection FOREIGN KEY (collection_id) REFERENCES collections(collection_id),
    CONSTRAINT fk_collection_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);
CREATE TABLE item_tags (
    item_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (item_id, tag_id),
    CONSTRAINT fk_item_tags_item FOREIGN KEY (item_id) REFERENCES items(item_id),
    CONSTRAINT fk_item_tags_tag FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);

CREATE INDEX idx_tags_name ON tags USING gin(name gin_trgm_ops);
CREATE INDEX idx_collection_tags_tag_id ON collection_tags(tag_id);
CREATE INDEX idx_item_tags_tag_id ON item_tags(tag_id);