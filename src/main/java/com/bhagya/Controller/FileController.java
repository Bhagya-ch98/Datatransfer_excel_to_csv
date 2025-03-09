package com.bhagya.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bhagya.Entity.RowData;
import com.bhagya.Servcie.FileService;

import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("startRow") int startRow,
                             Model model) {
        try {
            System.out.println("Received File: " + file.getOriginalFilename());
            System.out.println("Start Row: " + startRow);

            List<RowData> data = fileService.processExcelOrCsv(file, startRow + 1);

            if (data == null || data.isEmpty()) {
                System.out.println("No data extracted!");
                model.addAttribute("error", "No data found in the specified range.");
                return "upload"; 
            }

            System.out.println("Extracted Data:");
            for (RowData row : data) {
                System.out.println(row.getColumn1() + ", " + row.getColumn2() + ", " + row.getColumn3());
            }

            model.addAttribute("data", data);
            return "display"; 

        } catch (Exception e) {
            e.printStackTrace(); 
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            return "upload"; 
        }
    }

    @GetMapping("/upload-excel")
    public String getUploadPage() {
        return "upload";
    }

    @GetMapping("/")
    public String getSelectionPage() {
        return "Chossefiletype";
    }
}
