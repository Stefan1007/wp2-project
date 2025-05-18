package com.quiz.main.controller;

import com.quiz.main.service.QuizImportExportService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for handling quiz import and export operations
 */
@Controller
@RequestMapping("/admin/quizzes")
public class QuizImportExportController {

    @Autowired
    private QuizImportExportService quizImportExportService;

    /**
     * Display the import/export interface
     */
    @GetMapping("/import-export")
    public String showImportExportPage() {
        return "admin/importExport";
    }

    /**
     * Handle quiz import
     */
    @PostMapping("/import")
    public String importQuizzes(@RequestParam("file") MultipartFile file, 
                               RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
            return "redirect:/admin/quizzes/import-export";
        }

        try {
            int importedCount = quizImportExportService.importQuizzes(file);
            redirectAttributes.addFlashAttribute("message", 
                    "Successfully imported " + importedCount + " quizzes");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", 
                    "Failed to import quizzes: " + e.getMessage());
        }

        return "redirect:/admin/quizzes/import-export";
    }

    /**
     * Export all quizzes
     */
    @GetMapping("/export-all")
    public void exportAllQuizzes(HttpServletResponse response) throws IOException {
        // Generate timestamp for the filename
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "quizzes_export_" + timestamp + ".json";
        
        // Set response headers
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        
        // Write the JSON to the response
        String jsonData = quizImportExportService.exportAllQuizzes();
        response.getOutputStream().write(jsonData.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    /**
     * Export a specific quiz
     */
    @GetMapping("/{quizId}/export")
    public void exportQuiz(@PathVariable Long quizId, HttpServletResponse response) throws IOException {
        try {
            // Generate timestamp for the filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "quiz_" + quizId + "_export_" + timestamp + ".json";
            
            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            
            // Write the JSON to the response
            String jsonData = quizImportExportService.exportQuiz(quizId);
            response.getOutputStream().write(jsonData.getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found");
        }
    }
} 