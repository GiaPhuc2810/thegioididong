package com.hutech.tan.controller;

import com.hutech.tan.model.Category;
import com.hutech.tan.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("rootCategories", categoryRepository.findByParentIsNull());
        return "category/list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam(required = false) Long parentId, Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("selectedParentId", parentId);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("rootCategories", categoryRepository.findByParentIsNull());
        return "category/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category category,
                       BindingResult result,
                       @RequestParam(value = "parentId", required = false) Long parentId,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("rootCategories", categoryRepository.findByParentIsNull());
            model.addAttribute("selectedParentId", parentId);
            return "category/form";
        }
        if (parentId != null) {
            categoryRepository.findById(parentId).ifPresent(category::setParent);
        } else {
            category.setParent(null);
        }
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
        model.addAttribute("category", category);
        model.addAttribute("selectedParentId", category.getParent() != null ? category.getParent().getId() : null);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("rootCategories", categoryRepository.findByParentIsNull());
        return "category/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
