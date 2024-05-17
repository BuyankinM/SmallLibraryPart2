package ru.buyankin.spring.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buyankin.spring.models.Book;
import ru.buyankin.spring.models.Reader;
import ru.buyankin.spring.repositories.ReadersRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReadersService {

    private final ReadersRepository readersRepository;

    @Autowired
    public ReadersService(ReadersRepository readersRepository) {
        this.readersRepository = readersRepository;
    }

    public List<Reader> index() {
        return readersRepository.findAll();
    }

    public Reader getReader(int id) {
        return readersRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Reader reader) {
        readersRepository.save(reader);
    }

    @Transactional
    public void update(int id, Reader reader) {
        reader.setId(id);
        readersRepository.save(reader);
    }

    @Transactional
    public void delete(int id) {
        readersRepository.deleteById(id);
    }

    public Optional<Reader> checkUniqueName(Reader reader) {
        return readersRepository.findByName(reader.getName());
    }

    public List<Book> getBooksByReaderId(int id) {
        Optional<Reader> reader = readersRepository.findById(id);
        if (reader.isPresent()) {
            Hibernate.initialize(reader.get().getBooks());

            reader.get().getBooks().forEach(book -> {
                long diff = new Date().getTime() - book.getIssueDate().getTime();

                // 864000000 милисекунд = 10 суток
                if (diff > 864000000)
                    book.setOverdue(true); // книга просрочена
            });

            return reader.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
