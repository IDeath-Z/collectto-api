package com.collectto.api_collectto.presentation.dto.social;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import com.collectto.api_collectto.application.usecases.search.FetchGlobalSearchUseCase.UserSummary;
import com.collectto.api_collectto.application.usecases.search.FetchGlobalSearchUseCase.CollectionSummary;
import com.collectto.api_collectto.application.usecases.search.FetchGlobalSearchUseCase.ItemSummary;

public record GlobalSearchResponse(
    
    @ArraySchema(schema = @Schema(oneOf = {UserSummary.class, CollectionSummary.class, ItemSummary.class}))
    @Schema(description = "List of search results, which can be a mix of users, collections, and items.")
    List<Object> content,

    @Schema(description = "Number of elements requested per page")
    int size,

    @Schema(description = "Current page number")
    int currentPage,

    @Schema(description = "Defines if there is a next page available")
    boolean hasNext
) {}