package com.hutech.tan.controller; 
 
import com.hutech.tan.model.SinhVien; 
import jakarta.validation.Valid; 
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SinhVienController {
    // Danh sách sinh viên lưu tạm (giả lập DB)
    private static final List<SinhVien> dsSinhVien = new ArrayList<>();

    // Hiển thị danh sách sinh viên
    @GetMapping("/sinhvien/list")
    public String listSinhVien(Model model) {
        model.addAttribute("dsSinhVien", dsSinhVien);
        return "sinhvien/home";
    }

    // Hiển thị form thêm mới
    @GetMapping("/sinhvien")
    public String showForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "sinhvien/form-sinhvien";
    }

    // Thêm mới sinh viên
    @PostMapping("/sinhvien")
    public String submitForm(@Valid SinhVien sinhVien, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "sinhvien/form-sinhvien";
        }
        dsSinhVien.add(sinhVien);
        model.addAttribute("message", "Sinh viên đã được thêm thành công!");
        model.addAttribute("sinhVien", sinhVien);
        return "sinhvien/result-sinhvien";
    }

    // Hiển thị form sửa
    @GetMapping("/sinhvien/edit/{index}")
    public String editSinhVien(@PathVariable int index, Model model) {
        if (index >= 0 && index < dsSinhVien.size()) {
            model.addAttribute("sinhVien", dsSinhVien.get(index));
            model.addAttribute("index", index);
            return "sinhvien/form-sinhvien";
        }
        return "redirect:/sinhvien/list";
    }

    // Xử lý cập nhật sinh viên
    @PostMapping("/sinhvien/edit/{index}")
    public String updateSinhVien(@PathVariable int index, @Valid SinhVien sinhVien, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("index", index);
            return "sinhvien/form-sinhvien";
        }
        if (index >= 0 && index < dsSinhVien.size()) {
            dsSinhVien.set(index, sinhVien);
        }
        return "redirect:/sinhvien/list";
    }

    // Xóa sinh viên
    @GetMapping("/sinhvien/delete/{index}")
    public String deleteSinhVien(@PathVariable int index) {
        if (index >= 0 && index < dsSinhVien.size()) {
            dsSinhVien.remove(index);
        }
        return "redirect:/sinhvien/list";
    }
}