ALTER TABLE collection_follows RENAME COLUMN user_id TO follower_id;
ALTER TABLE collection_follows RENAME CONSTRAINT fk_follow_user TO fk_collection_follower;
ALTER TABLE collection_follows DROP COLUMN status;