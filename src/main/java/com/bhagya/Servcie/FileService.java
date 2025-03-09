package com.bhagya.Servcie;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bhagya.Entity.RowData;
import com.opencsv.CSVReader;

@Service
public class FileService {

    public List<RowData> processExcelOrCsv(MultipartFile file, int startRow) throws Exception {
        String fileName = file.getOriginalFilename();
        List<RowData> data = new ArrayList<>();

        if (fileName != null && fileName.endsWith(".csv")) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                List<String[]> allRows = csvReader.readAll();
                for (int i = startRow - 1; i < allRows.size(); i++) {
                    String[] row = allRows.get(i);
                    data.add(new RowData(
                        row.length > 0 ? row[0] : "",
                        row.length > 1 ? row[1] : "",
                        row.length > 2 ? row[2] : ""
                    ));
                }
            }
        } else if (fileName != null && fileName.endsWith(".xlsx")) {
            try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = startRow - 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        data.add(new RowData(
                            getCellValue(row.getCell(0)),
                            getCellValue(row.getCell(1)),
                            getCellValue(row.getCell(2))
                        ));
                    }
                }
            }
        }
        return data;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown Value";
        }
    }
}
