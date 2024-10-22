package com.example.demojwt.util;

import com.example.demojwt.dto.response.StudentResponseDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExcelExportUtil {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<StudentResponseDto> list;

    public ExcelExportUtil(List<StudentResponseDto> list) {
        this.list = list;
        workbook = new XSSFWorkbook();
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

    private void createHeaderRow() {
        sheet = workbook.createSheet("Student");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Mã sinh viên", style);
        createCell(row, 1, "Họ tên", style);
        createCell(row, 2, "Địa chỉ", style);
        createCell(row, 3, "Ngày sinh", style);
        createCell(row, 4, "Giới tính", style);
        createCell(row, 5, "Tên đăng nhập", style);
        createCell(row, 6, "Chức vụ", style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (StudentResponseDto responseDto : list) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, 0, responseDto.getId(), style);
            createCell(row, 1, responseDto.getName(), style);
            createCell(row, 2, responseDto.getAddress(), style);
            createCell(row, 3, responseDto.getBirthday(), style);
            createCell(row, 4, responseDto.getGender(), style);
            createCell(row, 5, responseDto.getUserName(), style);
            createCell(row, 6, responseDto.getRole(), style);
        }
    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


}
