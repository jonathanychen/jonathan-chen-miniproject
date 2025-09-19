package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.controller.RouteController;
import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class contains the unit tests for the RouteControllerUnitTests class.
 */
@SpringBootTest
public class RouteControllerUnitTests {

  private static Book book;

  @Mock
  private MockApiService mockApiService;

  private RouteController routeController;

  @BeforeAll
  public static void setUpClass() {
    book = new Book("Some title", 0);
  }

  /**
   * Setup function for test cases.
   */
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    routeController = new RouteController(mockApiService);

    book = new Book("Some title", 0);

    Mockito.when(mockApiService.getBooks()).thenReturn(new ArrayList<>(Arrays.asList(book)));
  }

  @Test
  public void indexTest() {
    assertEquals(
        "Welcome to the home page! In order to make an API call direct your browser"
            + "or Postman to an endpoint.",
        routeController.index());
  }

  @Test
  public void getBookFoundTest() {
    int id = 0;

    ResponseEntity<?> result = routeController.getBook(id);

    Mockito.verify(mockApiService).getBooks();

    Book resultBook = (Book) result.getBody();

    assertEquals(book, resultBook);
    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void getBookNotFoundTest() {
    int id = 1;

    ResponseEntity<?> result = routeController.getBook(id);

    Mockito.verify(mockApiService).getBooks();

    String resultMessage = (String) result.getBody();

    assertEquals("Book not found.", resultMessage);
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  public void getAvailableBooksTest() {
    Book book1 = new Book("Book 1", 1);
    Book book2 = new Book("Book 2", 2);

    book1.checkoutCopy();

    List<Book> books = new ArrayList<>(Arrays.asList(book1, book2));

    List<Book> expected = new ArrayList<>(Arrays.asList(book2));

    Mockito.when(mockApiService.getBooks()).thenReturn(books);

    ResponseEntity<?> result = routeController.getAvailableBooks();

    List<Book> actualBooks = (ArrayList<Book>) result.getBody();

    assertIterableEquals(expected, actualBooks);

  }

  @Test
  public void addCopyBookFoundTest() {
    ResponseEntity<?> result = routeController.addCopy(0);

    Book returnedBook = (Book) result.getBody();

    assertEquals(book.getCopiesAvailable(), returnedBook.getCopiesAvailable());
    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  public void addCopyBookNotFoundTest() {
    ResponseEntity<?> result = routeController.addCopy(420);

    String message = (String) result.getBody();

    assertEquals("Book not found.", message);
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  public void getRecommendationsTest() {
    List<Book> books = new ArrayList<>();
    for (int i = 1; i <= 15; i++) {
      Book newBook = new Book(String.format("Book %d", i), i);
      for (int j = 1; j <= i; j++) {
        newBook.addCopy();
        newBook.checkoutCopy();
      }
      books.add(newBook);
    }

    Mockito.when(mockApiService.getBooks()).thenReturn(books);

    ResponseEntity<?> result = routeController.getRecommendations();

    List<Book> recommendations = (List<Book>) result.getBody();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(recommendations.size(), 10);

    for (Book b : recommendations) {
      assertTrue(books.contains(b));
    }

    for (int i = 11; i <= 15; i++) {
      assertTrue(recommendations.contains(new Book(String.format("Book %d", i), i)));
    }
  }

  @Test
  public void getRecommendationsNotEnoughBooksTest() {
    Book book1 = new Book("Book 1", 1);
    Book book2 = new Book("Book 2", 2);

    List<Book> books = new ArrayList<>(Arrays.asList(book1, book2));

    Mockito.when(mockApiService.getBooks()).thenReturn(books);

    ResponseEntity<?> result = routeController.getRecommendations();

    String errorMessage = (String) result.getBody();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    assertEquals("Error occurred when getting recommended books", errorMessage);
  }

  @Test
  public void checkoutTest() {
    ResponseEntity<?> result = routeController.checkout(0);

    Book b = (Book) result.getBody();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(0, b.getId());
    assertEquals(1, b.getAmountOfTimesCheckedOut());
    assertEquals(1, b.getReturnDates().size());
    assertEquals(0, b.getCopiesAvailable());
  }

  @Test
  public void checkoutNotAvailableTest() {
    book.checkoutCopy();

    ResponseEntity<?> result = routeController.checkout(0);

    String notAvailableMessage = (String) result.getBody();

    assertEquals("No available copies for book with identifier 0", notAvailableMessage);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  public void checkoutBookNotFound() {
    ResponseEntity<?> result = routeController.checkout(15);

    String notFoundMessage = (String) result.getBody();

    assertEquals("Book not found.", notFoundMessage);
    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }
}
