package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.coms4156.project.individualproject.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class contains the unit tests for the Book class.
 */
@SpringBootTest
public class BookUnitTests {

  public static Book book;

  @BeforeEach
  public void setUpBookForTesting() {
    book = new Book("When Breath Becomes Air", 0);
  }

  @Test
  public void equalsBothAreTheSameTest() {
    Book cmpBook = book;
    assertEquals(cmpBook, book);
  }

  @Test
  public void equalsDifferentBooksTest() {
    Book cmpBook = new Book("Some other title", 15);
    assertNotEquals(cmpBook, book);
  }

  @Test
  public void hasCopiesTest() {
    assertTrue(book.hasCopies());

    book.deleteCopy();

    assertFalse(book.hasCopies());
  }

  @Test
  public void deleteCopyTest() {
    assertTrue(book.deleteCopy());
    assertFalse(book.deleteCopy());
  }

  @Test
  public void hasMultipleAuthorsTest() {
    assertFalse(book.hasMultipleAuthors());

    book.setAuthors(new ArrayList<>(Arrays.asList("author1", "author2")));

    assertTrue(book.hasMultipleAuthors());
  }

  @Test
  public void setTitleTest() {
    String newTitle = "Example title";

    book.setTitle(newTitle);

    assertEquals(newTitle, book.getTitle());
  }

  @Test
  public void setAuthorsTest() {
    List<String> authors = Arrays.asList("author1", "author2");
    List<String> authorsRef = new ArrayList<>(authors);
    List<String> expected = new ArrayList<>(authors);

    book.setAuthors(authorsRef);

    authorsRef.remove(authorsRef.size() - 1);

    assertIterableEquals(expected, authors);
  }

  @Test
  public void getAuthorsTest() {
    List<String> authors = new ArrayList<>(Arrays.asList());

    book.setAuthors(authors);

    List<String> retrievedAuthors = book.getAuthors();

    retrievedAuthors.add("some author");

    assertIterableEquals(authors, book.getAuthors());
  }

  @Test
  public void setLanguageTest() {
    String newLanguage = "French";

    book.setLanguage(newLanguage);

    assertEquals(newLanguage, book.getLanguage());
  }

  @Test
  public void setShelvingLocationTest() {
    String shelvingLocation = "someLocation";

    book.setShelvingLocation(shelvingLocation);

    assertEquals(shelvingLocation, book.getShelvingLocation());
  }

  @Test
  public void setPublicationDateTest() {
    String publicationDate = "some date";

    book.setPublicationDate(publicationDate);

    assertEquals(publicationDate, book.getPublicationDate());
  }

  @Test
  public void setPublisherTest() {
    String publisher = "me";

    book.setPublisher(publisher);

    assertEquals(publisher, book.getPublisher());
  }

  @Test
  public void setReturnDatesTest() {
    List<String> returnDates = Arrays.asList("date1", "date2");
    List<String> returnDatesRef = new ArrayList<>(Arrays.asList("date1", "date2"));
    List<String> expected = new ArrayList<>(returnDates);

    book.setReturnDates(returnDatesRef);

    returnDatesRef.remove(returnDatesRef.size() - 1);

    assertIterableEquals(expected, returnDates);
  }

  @Test
  public void getReturnDatesTest() {
    List<String> returnDates = new ArrayList<>(Arrays.asList("date1"));
    List<String> expected = new ArrayList<>(returnDates);

    book.setReturnDates(returnDates);

    List<String> retrievedReturnDates = book.getReturnDates();

    retrievedReturnDates.add("some return date");

    assertIterableEquals(expected, book.getReturnDates());
  }

  @Test
  public void setSubjectsTest() {
    List<String> subjects = Arrays.asList("geography", "history", "coding");
    List<String> subjectsRef = new ArrayList<>(subjects);

    List<String> expected = new ArrayList<>(subjects);

    book.setSubjects(subjectsRef);

    subjectsRef.remove(subjectsRef.size() - 1);

    assertIterableEquals(expected, subjects);
  }

  @Test
  public void getSubjectsTest() {
    List<String> subjects = new ArrayList<>(Arrays.asList("math", "science"));
    List<String> expected = new ArrayList<>(subjects);

    book.setSubjects(subjects);

    List<String> retrievedSubjects = book.getSubjects();

    retrievedSubjects.add("gaming");

    assertIterableEquals(expected, book.getSubjects());
  }

  @Test
  public void setTotalCopiesTest() {
    int totalCopies = 5;
    book.setTotalCopies(totalCopies);

    assertEquals(totalCopies, book.getTotalCopies());
  }

  @Test
  public void toStringTest() {
    assertEquals("(0)\tWhen Breath Becomes Air", book.toString());
  }

  @Test
  public void checkoutCopyTest() {
    assertEquals(0, book.getAmountOfTimesCheckedOut());
    assertTrue(book.hasCopies());

    assertNotNull(book.checkoutCopy());

    assertEquals(1, book.getAmountOfTimesCheckedOut());
    assertFalse(book.hasCopies());

    assertNull(book.checkoutCopy());
  }

  @Test
  public void returnCopyTest() {
    book.checkoutCopy();
    book.setReturnDates(new ArrayList<>(Arrays.asList("some date")));

    assertEquals(1, book.getReturnDates().size());
    assertFalse(book.hasCopies());
    assertEquals(0, book.getCopiesAvailable());

    assertTrue(book.returnCopy("some date"));

    assertEquals(0, book.getReturnDates().size());
    assertTrue(book.hasCopies());
    assertEquals(1, book.getCopiesAvailable());

    assertFalse(book.returnCopy("some other date"));

  }
}
