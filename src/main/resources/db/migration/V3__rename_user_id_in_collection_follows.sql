ALTER TABLE collection_follows RENAME COLUMN user_id TO follower_id;
ALTER TABLE collection_follows RENAME CONSTRAINT fk_follow_user TO fk_collection_follower;
ALTER TABLE collection_follows DROP COLUMN status;

ALTER TABLE item_likes RENAME COLUMN user_id TO liker_id;
ALTER TABLE item_likes RENAME CONSTRAINT fk_like_user TO fk_like_liker; 

ALTER TABLE item_comments RENAME COLUMN user_id TO author_id;
ALTER TABLE item_comments RENAME CONSTRAINT fk_comment_user TO fk_comment_author;