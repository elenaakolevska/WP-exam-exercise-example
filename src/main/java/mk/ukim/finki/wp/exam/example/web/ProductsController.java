package mk.ukim.finki.wp.exam.example.web;

import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class ProductsController {

    private final ProductService service;
    private final CategoryService categoryService;

    public ProductsController(ProductService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }


    @GetMapping({"/", "/products"})
    public String showProducts(@RequestParam(required = false) String nameSearch, @RequestParam(required = false) Long categoryId, Model model) {
        List<Product> products;
        List<Category> categories = this.categoryService.listAll();

        if (nameSearch == null && categoryId == null) {
            products=this.service.listAllProducts();
        } else {
            products=this.service.listProductsByNameAndCategory(nameSearch, categoryId);
        }
        model.addAttribute("categories",categories);
        model.addAttribute("products", products);

        return "list.html";
    }

    @GetMapping("/products/add")
    @PreAuthorize("hasRole('ADMIN')")

    public String showAdd(Model model) {
        List<Category> categories = this.categoryService.listAll();
        model.addAttribute("categories",categories);
        return "form.html";
    }

    @GetMapping("/products/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEdit(@PathVariable Long id, Model model) {
        Product product = this.service.findById(id);
        List<Category> categories = this.categoryService.listAll();

        model.addAttribute("product", product);
        model.addAttribute("categories",categories);
        return "form.html";
    }


    @PostMapping("/products")
    public String create(@RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam List<Long> categories) {
        this.service.create(name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}")

    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam List<Long> categories) {
        this.service.update(id, name, price, quantity, categories);
        return "redirect:/products";
    }
    @PostMapping("/products/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")


    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/products";
    }
}
