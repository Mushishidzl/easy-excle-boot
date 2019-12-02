package org.mushi.handler;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.mushi.model.UserWorkInfo;
import org.mushi.utils.CommonUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName: ReadExcelHangler
 * @Description: TODO
 * @Author: mushishi
 * @Date: 2019/11/26 15:13
 * @Version: V1.0
 **/
@Slf4j
@Service
public class ReadExcelHangler {

    private final static String SHEET_NAME= "考勤记录";


    /**
     * 考勤记录表
     * @return
     */
    public Map<String,Object> readExcel() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\深圳奥联考勤2019-1.xls");
        try {
            //载入excel
            Workbook wb = WorkbookFactory.create(file);
            //获取考勤记录工作表
            log.info("获取的sheet为：{}",SHEET_NAME);
            Sheet sheet = wb.getSheet(SHEET_NAME);
            //行数
            int rowCount = sheet.getPhysicalNumberOfRows();
            int cellCount = 0;
            int yearNum = 0;
            int monthNum = 0;
            String uName = "";
            String userNo = "";

            Map<String,Object> resultMap = new HashMap<>();
            Map<String,Object> map = new HashMap<>();
            for (int i = 2; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                //当前行存储打卡日期
                if (i == 2) {
                    String time = row.getCell(i).getStringCellValue();
                    log.info("时间: " + time + "                           ");
                    //从本月结束日期截取本月天数
                    cellCount = Integer.parseInt(time.substring(time.length() - 2, time.length()));
                    // 获取本月的月份和年份
                    yearNum = Integer.parseInt(time.substring(0,4));
                    monthNum = Integer.parseInt(time.substring(time.length() - 5, time.length()-3));
                }
                if (i <= 3){
                    continue;
                }
                //姓名
                if (i > 3 && i % 2 == 0) {
                    uName = row.getCell(10).getStringCellValue();
                    userNo = row.getCell(2).getStringCellValue();
                } else {
                    double totalTimeForMonth = 0;
                    // 打卡异常日
                    List<String> errorDay = new ArrayList<String>();
                    List<UserWorkInfo> userWorkInfos = new ArrayList<>();
                    for (int j = 0; j < cellCount; j++) {
                        Cell cell = row.getCell(j);
                        // 表格内有字符串
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            String value = cell.getStringCellValue();
                            List<String> time = CommonUtil.timeStrToList(value);
                            log.info("打卡时间：{}",time);
                            if (time.size() % 2 != 0) {
                                // 缺卡
                                UserWorkInfo workInfo = new UserWorkInfo();
                                workInfo.setUsername(uName);
                                workInfo.setUserNo(userNo);
                                workInfo.setDayNum(j + 1);
                                workInfo.setBeginTime(time.get(0));
                                workInfo.setTotalWorkTime(-1);
                                workInfo.setDesc("缺卡");
                                userWorkInfos.add(workInfo);
                                log.info("时间异常，请检查！" + time.toString());
                                errorDay.add((j + 1) + "号");
                                continue;
                            }
                            double totalTime = 0;
                            for (int t = 0; t < time.size(); t += 2) {
                                UserWorkInfo workInfo = new UserWorkInfo();
                                workInfo.setBeginTime(time.get(t));
                                workInfo.setEndTime(time.get(t + 1));
                                double tempTime = CommonUtil.calcTime(time.get(t), time.get(t + 1));
                                log.info("工号：{},{} ,{}年,{}月，{}号,上班时间:{},下班时间:{},时长: {}小时",userNo,uName,yearNum,monthNum,j+1,time.get(t),time.get(t + 1),tempTime);
                                totalTime = totalTime + tempTime;
                                workInfo.setUsername(uName);
                                workInfo.setUserNo(userNo);
                                workInfo.setWorkTime(totalTime);
                                workInfo.setDayNum(j + 1);
                                workInfo.setDesc("正常");
                                userWorkInfos.add(workInfo);
                            }

                            totalTimeForMonth = totalTimeForMonth + totalTime;
                        }else if(cell.getCellTypeEnum() == CellType.BLANK){
                            // 表格内不存在数据、判断是否为休息日、节假日
                            UserWorkInfo workInfo = new UserWorkInfo();
                            workInfo.setUsername(uName);
                            workInfo.setUserNo(userNo);
                            workInfo.setDayNum(j + 1);
                            workInfo.setTotalWorkTime(0);
                            workInfo.setDesc("休息");
                            userWorkInfos.add(workInfo);
                        }
                    }
                    map.put(uName,userWorkInfos);
                    log.info("【" + uName + "】：本月共上班 【" + totalTimeForMonth + "】小时");
                    if (errorDay.size() > 0) {
                        log.info("打卡异常共计 " + errorDay.size() + "天未累加，分别为：" + errorDay.toString());
                    }
                }

            }
            return map;
        } catch (FileNotFoundException e) {
            log.error("文件未找到！");
            e.printStackTrace();
        } catch (EncryptedDocumentException e) {
            log.error("解析文件失败！");
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            log.error("错误的格式！");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO异常！");
            e.printStackTrace();
        }
        return null;
    }



    public Map<String,Object> readErrorWxcel(){

        return MapUtil.newHashMap();
    }







}
