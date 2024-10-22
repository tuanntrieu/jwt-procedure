package com.example.demojwt.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class ExcelImportUtil {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelImportUtil() {
        this.workbook = new XSSFWorkbook();
    }

    private static boolean isExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Date) {
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.cloneStyleFrom(style);
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateCellStyle);
            return;
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void createExampleExcelFile(HttpServletResponse response) throws IOException {
        sheet = workbook.createSheet("Student");
        Row row = sheet.createRow(0);
        CellStyle styleHeader = workbook.createCellStyle();
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setBold(true);
        fontHeader.setFontHeight(16);
        styleHeader.setFont(fontHeader);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setWrapText(true);  // Enable wrap text for header
        createCell(row, 0, "Họ tên", styleHeader);
        createCell(row, 1, "Địa chỉ", styleHeader);
        createCell(row, 2, "Ngày sinh", styleHeader);
        createCell(row, 3, "Giới tính", styleHeader);
        createCell(row, 4, "Tên đăng nhập", styleHeader);
        createCell(row, 5, "Mật khẩu", styleHeader);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        style.setWrapText(true);

        row = sheet.createRow(1);
        createCell(row, 0, "Nguyễn A", style);
        createCell(row, 1, "Hà Nội", style);
        createCell(row, 2, new Date("01/01/2001"), style);
        createCell(row, 3, "Nam", style);
        createCell(row, 4, "eigenvalue", style);
        createCell(row, 5, "123456", style);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
