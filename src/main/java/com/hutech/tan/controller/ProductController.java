package com.hutech.tan.controller;

import com.hutech.tan.model.Product;
import com.hutech.tan.repository.CategoryRepository;
import com.hutech.tan.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // READ - danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/list";
    }

    // CREATE - form thêm
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/form";
    }

    // CREATE + UPDATE (XỬ LÝ ẢNH)
    @PostMapping("/save")
    public String save(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "categoryId", required = false) Long categoryId) throws IOException {

        // Gắn category từ id gửi lên
        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(product::setCategory);
        }

        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String uploadDir = "C:/uploads/products/";

            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(uploadDir + fileName);
            imageFile.transferTo(file);

            product.setImage(fileName);
        } else if (product.getId() != null) {
            // Khi edit ma khong chon anh moi: giu nguyen anh cu
            productRepository.findById(product.getId())
                    .ifPresent(existing -> product.setImage(existing.getImage()));
        }

        productRepository.save(product);
        return "redirect:/product";
    }

    // DETAIL - xem chi tiet
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/detail";
    }

    // UPDATE - form sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/product";
    }
}
