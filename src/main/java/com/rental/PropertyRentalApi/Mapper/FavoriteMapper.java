package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.response.PropertySummaryResponse;
import com.rental.PropertyRentalApi.Entity.Favorites;
import com.rental.PropertyRentalApi.Entity.UploadsImages;
import com.rental.PropertyRentalApi.Entity.UsersProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(config = MapperConfiguration.class)  // remove uses = { RoleMapper.class }
public interface FavoriteMapper {

    @Mapping(target = ".", source = "property")
    @Mapping(target = "images", expression = "java(map(favorite.getProperty().getImages()))")
    PropertySummaryResponse toPropertySummaryResponse(Favorites favorite);

    default List<PropertySummaryResponse> mapFavorites(Set<Favorites> favorites) {
        if (favorites == null) return List.of();
        return favorites.stream()
                .map(this::toPropertySummaryResponse)
                .toList();
    }

    default List<String> map(Set<UploadsImages> images) {
        if (images == null) return List.of();
        return images.stream()
                .map(UploadsImages::getUrls)
                .toList();
    }

    default String map(UsersProfile profile) {
        if (profile == null) return null;
        return profile.getUrls();
    }
}