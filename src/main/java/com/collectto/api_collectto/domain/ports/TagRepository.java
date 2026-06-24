package com.collectto.api_collectto.domain.ports;

import java.util.List;

import com.collectto.api_collectto.domain.entities.Tag;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface TagRepository {
    
    List<Tag> findSuggestions(String prefix, int limit);
    DomainPageResult<Tag> findByName(String name, DomainPageRequest pageRequest);
}
