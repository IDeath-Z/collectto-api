ALTER TABLE tags ADD COLUMN usage_count INTEGER NOT NULL DEFAULT 0;

CREATE OR REPLACE FUNCTION increment_tag_usage() RETURNS TRIGGER AS $$
BEGIN
    UPDATE tags SET usage_count = usage_count + 1 WHERE tag_id = NEW.tag_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION decrement_tag_usage() RETURNS TRIGGER AS $$
BEGIN
    UPDATE tags SET usage_count = usage_count - 1 WHERE tag_id = OLD.tag_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_collection_tags_increment
    AFTER INSERT ON collection_tags
    FOR EACH ROW EXECUTE FUNCTION increment_tag_usage();

CREATE TRIGGER trg_collection_tags_decrement
    AFTER DELETE ON collection_tags
    FOR EACH ROW EXECUTE FUNCTION decrement_tag_usage();

CREATE TRIGGER trg_item_tags_increment
    AFTER INSERT ON item_tags
    FOR EACH ROW EXECUTE FUNCTION increment_tag_usage();

CREATE TRIGGER trg_item_tags_decrement
    AFTER DELETE ON item_tags
    FOR EACH ROW EXECUTE FUNCTION decrement_tag_usage();

CREATE INDEX idx_tags_usage_count ON tags(usage_count DESC);