package com.example.newsservice.mapper;

import com.example.newsservice.entity.Category;
import com.example.newsservice.web.model.request.UpsertCategoryRequest;
import com.example.newsservice.web.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    Category upsertRequestToCategory(UpsertCategoryRequest request);

    CategoryResponse categoryToResponse(Category category);

}
