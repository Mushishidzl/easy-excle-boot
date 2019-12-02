package org.mushi.handler;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.mushi.model.UserWorkInfo;
import org.mushi.utils.DayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: WriteExcelHandler
 * @Description: 写入excel
 * @Author: mushishi
 * @Date: 2019/11/26 15:25
 * @Version: V1.0
 **/
@Slf4j
@Service
public class WriteExcelHandler {

    @Autowired
    ReadExcelHangler readExcelHangler;


    /**
     * 导出Excel文件到磁盘
     *
     * @param filePath
     * @param sheetData  数据
     */
    public void exportToFile(String filePath, String sheetData,Map<String,Object> map){
        Workbook workbook = exportWorkbook(sheetData,map);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileOutputStream!=null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 导出Excel对象
     *
     * @param sheetData  Excel数据
     * @return Workbook
     */
    public static Workbook exportWorkbook(String sheetData,Map<String,Object> map){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("信息表");

        XSSFRow row = sheet.createRow(0);
        row.setHeight((short) 800);

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headerStyle .setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerFont.setFontHeightInPoints((short)12);
        headerStyle.setFont(headerFont);
        // 居中
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        // 缺卡
        XSSFCellStyle missingStyle = workbook.createCellStyle();
        missingStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        missingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        missingStyle.setWrapText(true);
        // 居中
        missingStyle.setAlignment(HorizontalAlignment.CENTER);
        missingStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 休息
        XSSFCellStyle restStyle = workbook.createCellStyle();
        restStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        restStyle .setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 居中
        restStyle.setAlignment(HorizontalAlignment.CENTER);
        restStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        XSSFCellStyle normalStyle = workbook.createCellStyle();
        // 居中
        normalStyle.setAlignment(HorizontalAlignment.CENTER);
        normalStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        List<String> monthDay = DayUtils.getMonthDay(2019,1);
        for(int i=0;i<monthDay.size();i++){
            XSSFCell cell = row.createCell(i);
            sheet.setColumnWidth(i,4000);

            XSSFRichTextString text = new XSSFRichTextString(monthDay.get(i));
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);
        }

        final int[] rowNum = {1};
        map.forEach((key, value) -> {
            XSSFRow row1 = sheet.createRow(rowNum[0]);
            row1.setHeight((short) 500);
            row1.createCell(0).setCellValue(key);
            // 遍历
            List<UserWorkInfo> infos = (List<UserWorkInfo>) value;
            for(int i=0;i<infos.size();i++){
                UserWorkInfo userWorkInfo = infos.get(i);
                String desc = userWorkInfo.getDesc();
                XSSFCell cell = row1.createCell(i + 1);
                if(desc.equals("缺卡")){
                    cell.setCellStyle(missingStyle);
                    String time = StrUtil.isNotBlank(userWorkInfo.getBeginTime())?userWorkInfo.getBeginTime():userWorkInfo.getEndTime();
                    cell.setCellValue("打卡时间:"+time);
                }else if(desc.equals("休息")){
                    cell.setCellStyle(restStyle);
                    cell.setCellValue(desc);
                }else if(desc.equals("正常")){
                    cell.setCellStyle(normalStyle);
                    cell.setCellValue(desc);
                }
            }
            rowNum[0]++;
        });

        return workbook;
    }






}
