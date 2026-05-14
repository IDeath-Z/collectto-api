-- 1. Collections
ALTER TABLE collections DROP CONSTRAINT fk_collection_user;
ALTER TABLE collections ADD CONSTRAINT fk_collection_user 
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

-- 2. Items
ALTER TABLE items DROP CONSTRAINT fk_item_collection;
ALTER TABLE items ADD CONSTRAINT fk_item_collection 
    FOREIGN KEY (collection_id) REFERENCES collections(collection_id) ON DELETE CASCADE;

ALTER TABLE items DROP CONSTRAINT fk_item_user;
ALTER TABLE items ADD CONSTRAINT fk_item_user 
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

-- 3. Item Comments
ALTER TABLE item_comments DROP CONSTRAINT fk_comment_author;
ALTER TABLE item_comments ADD CONSTRAINT fk_comment_author 
    FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE item_comments DROP CONSTRAINT fk_comment_item;
ALTER TABLE item_comments ADD CONSTRAINT fk_comment_item 
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE;

-- 4. Collection Follows
ALTER TABLE collection_follows DROP CONSTRAINT fk_follow_collection;
ALTER TABLE collection_follows ADD CONSTRAINT fk_follow_collection 
    FOREIGN KEY (collection_id) REFERENCES collections(collection_id) ON DELETE CASCADE;

ALTER TABLE collection_follows DROP CONSTRAINT fk_collection_follower;
ALTER TABLE collection_follows ADD CONSTRAINT fk_collection_follower 
    FOREIGN KEY (follower_id) REFERENCES users(user_id) ON DELETE CASCADE;

-- 5. User Follows
ALTER TABLE user_follows DROP CONSTRAINT fk_user_follower;
ALTER TABLE user_follows ADD CONSTRAINT fk_user_follower 
    FOREIGN KEY (follower_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE user_follows DROP CONSTRAINT fk_user_followed;
ALTER TABLE user_follows ADD CONSTRAINT fk_user_followed 
    FOREIGN KEY (followed_id) REFERENCES users(user_id) ON DELETE CASCADE;

-- 6. Item Likes
ALTER TABLE item_likes DROP CONSTRAINT fk_like_liker;
ALTER TABLE item_likes ADD CONSTRAINT fk_like_liker 
    FOREIGN KEY (liker_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE item_likes DROP CONSTRAINT fk_like_item;
ALTER TABLE item_likes ADD CONSTRAINT fk_like_item 
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE;

-- 7. Notifications
ALTER TABLE notifications DROP CONSTRAINT fk_notification_recipient;
ALTER TABLE notifications ADD CONSTRAINT fk_notification_recipient 
    FOREIGN KEY (recipient_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE notifications DROP CONSTRAINT fk_notification_actor;
ALTER TABLE notifications ADD CONSTRAINT fk_notification_actor 
    FOREIGN KEY (actor_id) REFERENCES users(user_id) ON DELETE CASCADE;

-- 8. Collection Tags
ALTER TABLE collection_tags DROP CONSTRAINT fk_collection_tags_collection;
ALTER TABLE collection_tags ADD CONSTRAINT fk_collection_tags_collection 
    FOREIGN KEY (collection_id) REFERENCES collections(collection_id) ON DELETE CASCADE;

ALTER TABLE collection_tags DROP CONSTRAINT fk_collection_tags_tag;
ALTER TABLE collection_tags ADD CONSTRAINT fk_collection_tags_tag 
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE;

-- 9. Item Tags
ALTER TABLE item_tags DROP CONSTRAINT fk_item_tags_item;
ALTER TABLE item_tags ADD CONSTRAINT fk_item_tags_item 
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE;

ALTER TABLE item_tags DROP CONSTRAINT fk_item_tags_tag;
ALTER TABLE item_tags ADD CONSTRAINT fk_item_tags_tag 
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE;