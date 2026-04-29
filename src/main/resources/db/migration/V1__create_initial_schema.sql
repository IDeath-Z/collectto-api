CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    bio TEXT,
    profile_picture_url TEXT,
    profile_background_url TEXT,
    followers_count INT NOT NULL DEFAULT 0,
    following_count INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    birthday_date DATE NOT NULL,
    creation_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE collections (
    collection_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    cover_img_url TEXT,
    visibility VARCHAR(20) DEFAULT 'PRIVATE',
    followers_count INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_collection_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE items (
    item_id UUID PRIMARY KEY,
    collection_id UUID NOT NULL,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    acquisition_date DATE,
    last_used_date DATE,
    media_urls TEXT[],
    attributes JSONB,
    likes_count INT NOT NULL DEFAULT 0,
    comments_count INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_collection FOREIGN KEY (collection_id) REFERENCES collections(collection_id),
    CONSTRAINT fk_item_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE item_comments (
    comment_id UUID PRIMARY KEY,
    item_id UUID NOT NULL,
    user_id UUID NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_comment_item FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE collection_follows (
    user_id UUID NOT NULL,
    collection_id UUID NOT NULL,
    status VARCHAR(20),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, collection_id),
    CONSTRAINT fk_follow_collection FOREIGN KEY (collection_id) REFERENCES collections(collection_id),
    CONSTRAINT fk_follow_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE user_follows (
    follower_id UUID NOT NULL,
    followed_id UUID NOT NULL,
    status VARCHAR(20),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followed_id),
    CONSTRAINT fk_user_follower FOREIGN KEY (follower_id) REFERENCES users(user_id),
    CONSTRAINT fk_user_followed FOREIGN KEY (followed_id) REFERENCES users(user_id)
);

CREATE TABLE item_likes (
    item_id UUID NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (item_id, user_id),
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_like_item FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE notifications (
    notification_id UUID PRIMARY KEY,
    recipient_id UUID NOT NULL,
    actor_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    reference_id UUID,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_recipient FOREIGN KEY (recipient_id) REFERENCES users(user_id),
    CONSTRAINT fk_notification_actor FOREIGN KEY (actor_id) REFERENCES users(user_id)
);