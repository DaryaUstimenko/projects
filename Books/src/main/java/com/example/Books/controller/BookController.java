package com.example.Books.controller;

import com.example.Books.entity.Book;
import com.example.Books.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{title}/{author}")
    public ResponseEntity<Book> getBook(@PathVariable String title, @PathVariable String author) {
        Book book = bookService.findBookByTitleAndAuthor(title, author);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable String categoryName) {
        List<Book> books = bookService.findBooksByCategoryName(categoryName);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book.getTitle(), book.getAuthor(), book.getCategory().getName());
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book.getTitle(), book.getAuthor(), book.getCategory().getName());
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

