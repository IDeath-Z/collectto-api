package com.collectto.api_collectto.domain.ports;

import java.util.List;

import com.collectto.api_collectto.domain.entities.Tag;

public interface TagRepository {
    List<Tag> findSuggestions(String prefix, int limit);
}
