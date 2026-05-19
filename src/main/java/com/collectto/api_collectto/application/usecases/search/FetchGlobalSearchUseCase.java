package com.collectto.api_collectto.application.usecases.search;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.SocialContext;
import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchGlobalSearchUseCase {

    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;

    public record Input(String query, int page, int size) {}

    public interface SearchSummary {
        SocialContext type();
    }

    public record UserSummary(UUID id, String username, String profilePictureUrl) implements SearchSummary {
        @Override public SocialContext type() { return SocialContext.USER; }
    }
    
    public record CollectionSummary(UUID id, String name, String coverImgUrl) implements SearchSummary {
        @Override public SocialContext type() { return SocialContext.COLLECTION; }
    }
    
    public record ItemSummary(UUID id, String name, String mediaUrl) implements SearchSummary {
        @Override public SocialContext type() { return SocialContext.ITEM; }
    }
    
    public record Output(List<SearchSummary> content, int page, int size, boolean hasNext) {}

    public Output execute(Input input) {
        if (input.query() == null || input.query().trim().isEmpty()) {
            return new Output(List.of(), input.page(), input.size(), false);
        }

        String searchTerm = input.query().trim();
        DomainPageRequest pageRequest = new DomainPageRequest(input.page(), input.size(), SortBy.CREATED_AT_DESC);

        DomainPageResult<User> usersResult = userRepository.searchActiveUsers(searchTerm, pageRequest);
        DomainPageResult<Collection> collectionsResult = collectionRepository.searchPublicCollections(searchTerm, pageRequest);
        DomainPageResult<Item> itemsResult = itemRepository.searchPublicItems(searchTerm, pageRequest);

        List<SearchSummary> combinedFeed = new ArrayList<>();

        for (User u : usersResult.content()) {
            combinedFeed.add(new UserSummary(u.getId(), u.getUsername(), u.getProfilePictureUrl()));
        }

        for (Collection c : collectionsResult.content()) {
            combinedFeed.add(new CollectionSummary(c.getId(), c.getName(), c.getCoverImageUrl()));
        }

        for (Item i : itemsResult.content()) {
            String firstMedia = (i.getMediaURLs() != null && !i.getMediaURLs().isEmpty()) ? i.getMediaURLs().get(0) : null;
            combinedFeed.add(new ItemSummary(i.getId(), i.getName(), firstMedia));
        }

        int currentPage = input.page();
        boolean hasNext = (currentPage + 1 < usersResult.totalPages()) 
                       || (currentPage + 1 < collectionsResult.totalPages()) 
                       || (currentPage + 1 < itemsResult.totalPages());

        return new Output(
            combinedFeed, 
            input.page(), 
            input.size(), 
            hasNext
        );
    }
}