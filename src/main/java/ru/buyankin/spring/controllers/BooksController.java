package ru.buyankin.spring.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.buyankin.spring.models.Book;
import ru.buyankin.spring.models.Reader;
import ru.buyankin.spring.services.BooksService;
import ru.buyankin.spring.services.ReadersService;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final ReadersService readersService;

    @Autowired
    public BooksController(BooksService booksService, ReadersService readersService) {
        this.booksService = booksService;
        this.readersService = readersService;
    }

    @GetMapping()
    public String books(Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "books_per_page", defaultValue = "0") int booksPerPage,
                        @RequestParam(value = "sort_by_year", defaultValue = "false") boolean sortByYear) {

        model.addAttribute("books", booksService.index(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id,
                           @ModelAttribute("reader") Reader reader,
                           Model model) {

        Book book = booksService.getBook(id);
        model.addAttribute("book", book);
        model.addAttribute("readers", readersService.index());
        model.addAttribute("readerOfBook", book.getOwner());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/search")
    public String searchBook() {
        return "books/search";
    }

    @PostMapping("/search")
    public String handleSearch(@RequestParam("query") String query, Model model) {
        model.addAttribute("books", booksService.findBooksByTitlePrefix(query));
        return "books/search";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.getBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String patch(@ModelAttribute("book") @Valid Book book,
                        BindingResult bindingResult,
                        @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PostMapping("/free/{id}")
    public String free(@PathVariable("id") int id) {
        booksService.freeBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/assign/{id}")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("reader") Reader reader) {
        booksService.assignBook(id, reader.getId());
        return "redirect:/books/" + id;
    }
}