package com.kuui.kas.application.common.service;

import com.kuui.kas.application.common.repository.CommonRepository;
import com.kuui.kas.application.teacher.domain.Teacher;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final DataSource dataSource;

    public void export(HttpServletResponse response) {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "select * from kuui.asset";

            Statement pstmt = connection.createStatement();
            ResultSet rs = pstmt.executeQuery(sql);

            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Asset");

            writeHeaderLine(rs, sheet);
            writeDataLines(rs, sheet);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=exported_data.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();
            rs.close();
            pstmt.close();

        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    public void writeHeaderLine(ResultSet resultSet, Sheet sheet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();

        Row headerRow = sheet.createRow(0);

        for (int i = 1; i <= numberOfColumns; i++) {
            String columnName = metaData.getColumnName(i);
            Cell headerCell = headerRow.createCell(i - 1);
            headerCell.setCellValue(translator(columnName));
        }
    }

    public void writeDataLines(ResultSet resultSet, Sheet sheet) throws SQLException {
        int rowIndex = 1;

        while (resultSet.next()) {
            Row row = sheet.createRow(rowIndex++);

            int numberOfColumns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                Object valueObject = resultSet.getObject(i);
                Cell cell = row.createCell(i - 1);

                if (valueObject instanceof Boolean) {
                    cell.setCellValue((Boolean) valueObject);
                } else if (valueObject instanceof Double) {
                    cell.setCellValue((Double) valueObject);
                } else if (valueObject instanceof Float) {
                    cell.setCellValue((Float) valueObject);
                } else if (valueObject instanceof Integer) {
                    cell.setCellValue((Integer) valueObject);
                } else if (valueObject instanceof Long) {
                    cell.setCellValue((Long) valueObject);
                } else if (valueObject instanceof String) {
                    cell.setCellValue((String) valueObject);
                } else if (valueObject != null) {
                    cell.setCellValue(valueObject.toString());
                }
            }
        }
    }

    public String translator(String name) {
        if(name.equals("asset_id")) {
            name = "자산 ID";
        }else if (name.equals("asset_ctg")) {
            name = "자산 카테고리";
        }else if (name.equals("asset_name")) {
            name = "자산 이름";
        }else if (name.equals("asset_no")) {
            name = "자산 번호";
        }else if (name.equals("asset_position")) {
            name = "자산 보관 위치";
        }else if (name.equals("asset_reg_date")) {
            name = "자산 등록 일자";
        }else if (name.equals("asset_remain_cnt")) {
            name = "자산 잔여 수량";
        }else if (name.equals("asset_total_cnt")) {
            name = "자산 전체 수량";
        }else if (name.equals("asset_upd_date")) {
            name = "자산 최종 수정 일자";
        }else if (name.equals("reg_teacher_name")) {
            name = "자산 등록자 이름";
        }else if (name.equals("upd_teacher_name")) {
            name = "자산 최종 수정자 이름";
        }


        return name;
    }
}
