package com.example.demojwt.util;

import com.example.demojwt.dto.request.StudentDto;
import com.example.demojwt.exception.NotFoundException;
import com.example.demojwt.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ExcelImportUtil {

    private final UserService userService;

    public static boolean isExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private void createCell(XSSFWorkbook workbook, XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {
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
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Student");
        Row row = sheet.createRow(0);
        CellStyle styleHeader = workbook.createCellStyle();
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setBold(true);
        fontHeader.setFontHeight(16);
        styleHeader.setFont(fontHeader);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setWrapText(true);
        createCell(workbook, sheet, row, 0, "Họ tên", styleHeader);
        createCell(workbook, sheet, row, 1, "Địa chỉ", styleHeader);
        createCell(workbook, sheet, row, 2, "Ngày sinh", styleHeader);
        createCell(workbook, sheet, row, 3, "Giới tính", styleHeader);
        createCell(workbook, sheet, row, 4, "Tên đăng nhập", styleHeader);
        createCell(workbook, sheet, row, 5, "Mật khẩu", styleHeader);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        style.setWrapText(true);

        row = sheet.createRow(1);
        createCell(workbook, sheet, row, 0, "Nguyễn A", style);
        createCell(workbook, sheet, row, 1, "Hà Nội", style);
        createCell(workbook, sheet, row, 2, "01/01/2001", style);
        createCell(workbook, sheet, row, 3, "Nam", style);
        createCell(workbook, sheet, row, 4, "eigenvalue", style);
        createCell(workbook, sheet, row, 5, "123456", style);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public List<StudentDto> extractFromFile(InputStream inputStream) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new NotFoundException("Sheet is not found");
            }
            Iterator<Row> rows = sheet.iterator();
            List<StudentDto> stuList = new ArrayList<>();
            if (rows.hasNext()) {
                rows.next();
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (isRowEmpty(currentRow)) {
                    continue;
                }
                StudentDto student = new StudentDto();
                String name = "", address = "", gender = "", username = "", password = "";
                String birthday = "";
                Date brthDate = null;
                boolean isAdd = true;

                for (int cellIdx = 0; cellIdx < 6; cellIdx++) {
                    Cell currentCell = currentRow.getCell(cellIdx);

                    switch (cellIdx) {
                        case 0:
                            name = getCellValueAsString(currentCell);
                            student.setName(name);
                            break;
                        case 1:
                            address = getCellValueAsString(currentCell);
                            student.setAddress(address);
                            break;
                        case 2:
                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                birthday = currentCell.getStringCellValue();
                                student.setBirthday(new Date(birthday));
                            } else if (currentCell.getCellType() == CellType.NUMERIC) {
                                birthday = String.valueOf(currentCell.getNumericCellValue());
                                if (birthday.matches("^(\\d{1,2}[/](\\d{1,2})[/](\\d{4}))$")) {
                                    student.setBirthday(new Date(birthday));
                                }
                            } else {
                                brthDate = currentCell.getDateCellValue();
                                student.setBirthday(brthDate);
                            }
                            break;
                        case 3:
                            gender = getCellValueAsString(currentCell);
                            student.setGender(StringUtils.capitalize(gender));
                            break;
                        case 4:
                            username = getCellValueAsString(currentCell);
                            if (isAdd) {
                                if (userService.existsByUsername(username)) {
                                    isAdd = false;
                                }
                            }
                            if (isAdd) {
                                for (StudentDto tmp : stuList) {
                                    if (username.equals(tmp.getUsername())) {
                                        isAdd = false;
                                    }
                                    break;
                                }
                            }
                            student.setUsername(username);
                            break;
                        case 5:
                            password = getCellValueAsString(currentCell).toLowerCase();
                            student.setPassword(password);
                            break;
                        default:
                            break;
                    }
                }
                student.setRole("ROLE_STUDENT");
                if (isAdd) {
                    stuList.add(student);
                }
                isAdd = true;
            }
            workbook.close();
            return stuList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }

    }

    public String validateData(InputStream inputStream) {
        StringBuilder message = new StringBuilder("");
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new NotFoundException("Sheet is not found");
            }
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) {
                rows.next();
            }
            int row = 1;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                row++;
                if (isRowEmpty(currentRow)) {
                    continue;
                }
                String name = "", address = "", gender = "", username = "", password = "";
                String birthday = "";
                Date brthDate = null;
                for (int cellIdx = 0; cellIdx < 6; cellIdx++) {
                    Cell currentCell = currentRow.getCell(cellIdx);
                    switch (cellIdx) {
                        case 0:
                            name = getCellValueAsString(currentCell);
                            if (name.isEmpty()) {
                                message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                            }
                            break;
                        case 1:
                            address = getCellValueAsString(currentCell);
                            if (address.isEmpty()) {
                                message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                            }
                            break;
                        case 2:

                            if (currentCell.getCellType().equals(CellType.STRING)) {
                                birthday = currentCell.getStringCellValue();
                                if (birthday.isEmpty()) {
                                    message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                                } else {
                                    if (!birthday.matches("^(\\d{1,2}[/](\\d{1,2})[/](\\d{4}))$")) {
                                        message.append("Birthday is invalid at row " + row + ", ");
                                    }
                                }
                            } else if (currentCell.getCellType() == CellType.NUMERIC) {
                                birthday = String.valueOf(currentCell.getNumericCellValue());
                                if (!birthday.matches("^(\\d{1,2}[/](\\d{1,2})[/](\\d{4}))$")) {
                                    message.append("Birthday is invalid at row " + row + ", ");
                                }
                            } else {
                                try {
                                    brthDate = currentCell.getDateCellValue();
                                } catch (Exception e) {
                                    message.append("Birthday is invalid at row " + row + ", ");
                                }
                            }
                            if (getCellValueAsString(currentCell).isEmpty()) {
                                message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                            }
                            break;
                        case 3:
                            gender = getCellValueAsString(currentCell).toLowerCase();
                            if (gender.isEmpty()) {
                                message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                            } else if (!gender.equals("nam") && !gender.equals("nữ")) {
                                message.append("Gender is invalid at row " + row + ", ");
                            }
                            break;
                        case 4:
                            username = getCellValueAsString(currentCell);
                            if (username.isEmpty()) {
                                message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                            }
                            break;
                        case 5:
                            password = getCellValueAsString(currentCell);
                            if (password.isEmpty()) {
                                message.append(getEmptyErrorMessage(cellIdx, row) + ", ");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
        return message.toString().trim();
    }

    public String checkUsername(InputStream inputStream) {
        StringBuilder message = new StringBuilder("");
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new NotFoundException("Sheet is not found");
            }
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) {
                rows.next();
            }
            int row = 1;
            List<String> usernames = new ArrayList<>();
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                row++;
                if (isRowEmpty(currentRow)) {
                    continue;
                }
                String username = "";
                Cell currentCell = currentRow.getCell(4);
                username = getCellValueAsString(currentCell);
                String finalUsername = username;
                int finalRow = row;
                if (userService.existsByUsername(username)) {
                    message.append("Username at row " + finalRow + " is already taken, ");
                }
                usernames.stream().forEach(usernameTmp -> {
                    if (usernameTmp.equals(finalUsername)) {
                        message.append("Duplicate username at row " + finalRow + ",");

                    }
                });
                usernames.add(username);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
        return message.toString().trim();
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                String tmp = String.valueOf(cell.getNumericCellValue());
                if (tmp.endsWith(".0")) {
                    return tmp.substring(0, tmp.length() - 2);
                }
                return tmp;
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return "";
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private String getEmptyErrorMessage(int cellIdx, int row) {
        switch (cellIdx) {
            case 0:
                return "Name is required at row " + row;
            case 1:
                return "Address is required at row " + row;
            case 2:
                return "Birthday is required at row " + row;
            case 3:
                return "Gender is required at row " + row;
            case 4:
                return "Username is required at row " + row;
            case 5:
                return "Password is required at row " + row;
            default:
                return "Unexpected error at row " + row;
        }
    }


}
