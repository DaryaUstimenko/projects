package com.example.Books.service;

import com.example.Books.entity.Book;
import com.example.Books.entity.Category;
import com.example.Books.repository.BookRepository;
import com.example.Books.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "books", key = "#title + ':' + #author")
    public Book findBookByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    @Cacheable(value = "booksByCategory", key = "#categoryName")
    public List<Book> findBooksByCategoryName(String categoryName) {
        return bookRepository.findByCategoryName(categoryName);
    }

    @Transactional
    @CacheEvict(value = {"books", "booksByCategory"}, allEntries = true)
    public Book createBook(String title, String author, String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            category = new Category();
            category.setName(categoryName);

            category = categoryRepository.save(category);
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        return bookRepository.save(book);
    }

    @Transactional
    @CacheEvict(value = {"books", "booksByCategory"}, allEntries = true)
    public Book updateBook(Long id, String title, String author, String categoryName) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            category = new Category();
            category.setName(categoryName);
            category = categoryRepository.save(category);
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        return bookRepository.save(book);
    }

    @Transactional
    @CacheEvict(value = {"books", "booksByCategory"}, allEntries = true)
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

