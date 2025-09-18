package dev.coms4156.project.individualproject.controller;

import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class defines the routes that our API supports.
 */
@RestController
public class RouteController {

  private final MockApiService mockApiService;

  public RouteController(MockApiService mockApiService) {
    this.mockApiService = mockApiService;
  }

  @GetMapping({ "/", "/index" })
  public String index() {
    return "Welcome to the home page! In order to make an API call direct your browser"
        + "or Postman to an endpoint.";
  }

  /**
   * Returns the details of the specified book.
   *
   * @param id An {@code int} representing the unique identifier of the book to
   *           retrieve.
   *
   * @return A {@code ResponseEntity} containing either the matching {@code Book}
   *         object with an
   *         HTTP 200 response, or a message indicating that the book was not
   *         found with an HTTP 404 response.
   */
  @GetMapping({ "/book/{id}" })
  public ResponseEntity<?> getBook(@PathVariable int id) {
    for (Book book : mockApiService.getBooks()) {
      if (book.getId() == id) {
        return new ResponseEntity<>(book, HttpStatus.OK);
      }
    }

    return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
  }

  /**
   * Get and return a list of all the books with available copies.
   *
   * @return A {@code ResponseEntity} containing a list of available {@code Book}
   *         objects with an
   *         HTTP 200 response if sucessful, or a message indicating an error
   *         occurred with an
   *         HTTP 500 response.
   */
  @GetMapping({ "/books/available" })
  public ResponseEntity<?> getAvailableBooks() {
    try {
      List<Book> availableBooks = new ArrayList<>();

      for (Book book : mockApiService.getBooks()) {
        if (book.hasCopies()) {
          availableBooks.add(book);
        }
      }

      return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>("Error occurred when getting all available books",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Get and return a list of 10 books, where 5 are the most popular books, and 5
   * are
   * randomly selected.
   *
   * @return A {@code ResponseEntity} containing a list of 10 {@code Book} objects
   *         with an HTTP 200 response if successful, or a message indicating an
   *         error
   *         with an HTTP 500 response.
   */
  @GetMapping({ "/books/recommendations" })
  public ResponseEntity<?> getRecommendations() {
    try {
      List<Book> recommendations = new ArrayList<>();

      List<Book> allBooks = mockApiService.getBooks();
      if (allBooks.size() < 10) {
        throw new Exception("Not enough books to recommend");
      }

      Collections.sort(allBooks, new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
          return b2.getAmountOfTimesCheckedOut() - b1.getAmountOfTimesCheckedOut();
        }
      });

      for (int i = 0; i < 5; i++) {
        recommendations.add(allBooks.get(i));
      }

      List<Book> remaining = allBooks.subList(5, allBooks.size());
      Collections.shuffle(remaining);

      for (int i = 0; i < 5; i++) {
        recommendations.add(remaining.get(i));
      }

      return new ResponseEntity<>(recommendations, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error occurred when getting recommended books",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Adds a copy to the {@code} Book object if it exists.
   *
   * @param bookId An {@code Integer} representing the unique id of the book.
   * @return A {@code ResponseEntity} containing the updated {@code Book} object
   *         with an
   *         HTTP 200 response if successful or HTTP 404 if the book is not found,
   *         or a message indicating an error occurred with an HTTP 500 code.
   */
  @PatchMapping({ "/book/{bookId}/add" })
  public ResponseEntity<?> addCopy(@PathVariable Integer bookId) {
    try {
      for (Book book : mockApiService.getBooks()) {
        if (bookId.equals(book.getId())) {
          book.addCopy();
          return new ResponseEntity<>(book, HttpStatus.OK);
        }
      }

      return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>(
          String.format("Error occurred when retrieving book with identifier %d.", bookId),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Checks out a copy of a {@code Book} object if it exists and is available.
   *
   * @param bookId An {@code Integer} representing the unique id of the book.
   * @return A {@code ResponseEntity} containing the updated {@code Book} object
   *         with an HTTP 200 response if successful or HTTP 404 if the book is 
   *         not found, or a message indicating an error with an HTTP 500 code.
   */
  @PostMapping({ "/checkout" })
  public ResponseEntity<?> checkout(@RequestParam Integer bookId) {
    try {
      for (Book book : mockApiService.getBooks()) {
        if (bookId.equals(book.getId())) {
          String result = book.checkoutCopy();
          if (result == null) {
            return new ResponseEntity<>(
                String.format("No available copies for book with identifier %d", bookId), HttpStatus.OK);
          }

          return new ResponseEntity<>(book, HttpStatus.OK);
        }
      }

      return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>(
          String.format("Error occurred when retrieving book with identifier %d.", bookId), 
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
