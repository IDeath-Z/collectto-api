package com.collectto.api_collectto.application.usecases.search;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Tag;
import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.ports.TagRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchTagSearchUseCase {

    private final TagRepository tagRepository;

    public record Input(String query, int page, int size) {}
    public record TagSummary(UUID id, String name, int usageCount) {}
    public record Output(List<TagSummary> content, int totalPages, long totalElements, int currentPage) {}
    

    public Output execute(Input input) {
        String searchTerm = input.query() == null ? "" : input.query().trim();

        DomainPageRequest pageRequest = new DomainPageRequest(input.page(), input.size(), SortBy.CREATED_AT_DESC);
        DomainPageResult<Tag> result = tagRepository.findByName(searchTerm, pageRequest);

        List<TagSummary> summaries = result.content().stream()
            .map(tag -> new TagSummary(tag.getId(), tag.getName(), tag.getUsageCount()))
            .toList();

        return new Output(summaries, result.totalPages(), result.totalElements(), result.page());
    }
}
