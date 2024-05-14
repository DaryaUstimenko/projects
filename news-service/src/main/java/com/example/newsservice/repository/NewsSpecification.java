package com.example.newsservice.repository;

import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.entity.User;
import com.example.newsservice.web.model.request.NewsFilterRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public interface NewsSpecification {

    static Specification<News> withFilter(NewsFilterRequest filter) {
        return Specification.where(byCategoryName(filter.getCategoryName()))
                .and(byUsername(filter.getUsername()));
    }

    static Specification<News> byCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(categoryName)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(News.Fields.category).get(Category.Fields.name), categoryName);
        };
    }

    static Specification<News> byUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(username)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(News.Fields.author).get(User.Fields.username), username);
        };
    }

}
