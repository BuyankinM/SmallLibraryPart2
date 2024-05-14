package ru.buyankin.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buyankin.spring.models.Book;
import ru.buyankin.spring.models.Reader;
import ru.buyankin.spring.repositories.BooksRepository;
import ru.buyankin.spring.repositories.ReadersRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final ReadersRepository readersRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, ReadersRepository readersRepository) {
        this.booksRepository = booksRepository;
        this.readersRepository = readersRepository;
    }

    public List<Book> index(int page, int booksPerPage, boolean sortByYear) {
        List<Book> books;

        if (booksPerPage == 0 && !sortByYear)
            books = booksRepository.findAll();
        else if (booksPerPage == 0)
            books = booksRepository.findAll(Sort.by("year"));
        else if (!sortByYear)
            books = booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        else
            books = booksRepository
                    .findAll(PageRequest.of(page, booksPerPage, Sort.by("year")))
                    .getContent();

        return books;
    }

    public Book getBook(int id) {
        return booksRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void freeBook(int id) {
        Book book = booksRepository.findById(id).get();
        book.setOwner(null);
        booksRepository.save(book);
    }

    @Transactional
    public void assignBook(int id, int readerId) {
        Book book = booksRepository.findById(id).get();
        Reader reader = readersRepository.findById(readerId).get();
        book.setOwner(reader);
        booksRepository.save(book);
    }
}
