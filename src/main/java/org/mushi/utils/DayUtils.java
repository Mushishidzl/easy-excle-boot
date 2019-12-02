package org.mushi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.DateUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * @ClassName: csad
 * @Description: TODO
 * @Author: mushishi
 * @Date: 2019/11/26 14:56
 * @Version: V1.0
 **/
@Slf4j
@UtilityClass
public class DayUtils {


    /**
     *    周六、周天
     *    {
     *     "code": 0,
     *     "type": {
     *         "type": 1,
     *         "name": "周六",
     *         "week": 6
     *     },
     *     "holiday": null
     *     }
     */


    /**
     * 节假日
     *  {
     *     "code": 0,
     *     "type": {
     *         "type": 2,
     *         "name": "元旦",
     *         "week": 2
     *     },
     *     "holiday": {
     *         "holiday": true,
     *         "name": "元旦",
     *         "wage": 3,
     *         "date": "2019-01-01"
     *     }
     * }
     *
     *
     */

    /**
     * 工作日
     * {
     *     "code": 0,
     *     "type": {
     *         "type": 0,
     *         "name": "周五",
     *         "week": 5
     *     },
     *     "holiday": null
     * }
     *
     *
     */


    /**
     * 判断该日期是否为节假日
     *
     * @param date 1,正常工作日  2,节假日 3,节假日后补班 4,周末
     * @return
     */
    public int getIfHoliday(String date) {
        int flag = 1;
        try {
            // http://timor.tech/api/holiday/info/2019-11-27
            String result = HttpUtil.get("http://timor.tech/api/holiday/info/" + date);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            if (jsonObject.get("holiday") == null) {
                flag = 1;
            } else {
                Object holiday = jsonObject.get("holiday");
                JSONObject holidayDO = JSONUtil.parseObj(holiday);
                if (holidayDO != null && holidayDO.size() > 0) {
                    Boolean object = (Boolean) holidayDO.get("holiday");
                    if (object) {
                        //节假日
                        flag = 2;
                    } else {
                        //节假日补班
                        flag = 3;
                    }
                }
            }
            LocalDate localDate = LocalDate.parse(date);
            int value = localDate.getDayOfWeek().getValue();
            if (value == 6 || value == 7) {
                //如果不是节假日补班情况，则返回周末
                if (flag != 3) {
                    //周末
                    flag = 4;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取是否工作日异常.", e);
        }
        return flag;
    }


    /**
     * 获取当月的所有工作日
     *
     * @param year
     * @param month
     * @return
     */
    public List<String> getWolkdayInMonth(int year, int month) {
        List<String> list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        // 不设置的话默认为当年
        calendar.set(Calendar.YEAR, year);
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 设置为当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 当月最大天数
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daySize; i++) {
            String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
            int ifHoliday = getIfHoliday(format);
            if (ifHoliday != 2) {
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                //周末休息
                // 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
                if ((week == Calendar.SATURDAY || week == Calendar.SUNDAY)) {
                    //如果是补班，则属于正常上班时间
                    if (ifHoliday == 3) {
                        list.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                    }
                } else {
                    // 得到当天是一个月的第几天
                    list.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                }

            }
            //在第一天的基础上加1
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }


    public List<String> getMonthDay(int year, int month) {
        List<String> list = CollUtil.newArrayList();
        list.add("姓名");
        Calendar calendar = Calendar.getInstance();
        // 不设置的话默认为当年
        calendar.set(Calendar.YEAR, year);
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 设置为当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 当月最大天数
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daySize; i++) {
            String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
            DateTime dateTime = DateUtil.parseDate(format);
            String dayNum = DateUtil.format(dateTime, "dd");
            Map<String, Object> monthDayMap = getMonthDayMap(format);
            String o = (String) monthDayMap.get(dayNum);
            log.info("描述：{}",o);
            list.add(o);

            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }


    public Map<String, Object> getMonthDayMap(String date) {
        Map<String, Object> map = MapUtil.newHashMap();
        DateTime dateTime = DateUtil.parseDate(date);
        String dayNum = DateUtil.format(dateTime, "dd");
        int flag = 1;
        try {
            // http://timor.tech/api/holiday/info/2019-11-27
            String result = HttpUtil.get("http://timor.tech/api/holiday/info/" + date);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            Object type = jsonObject.get("type");
            JSONObject typeMap = JSONUtil.parseObj(type);
            if (typeMap != null && typeMap.size() > 0) {
                Integer daytype = (Integer) typeMap.get("type");
                if (daytype == 0) {
                    map.put(dayNum, dayNum);
                } else {
                    map.put(dayNum, typeMap.get("name"));
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取是否工作日异常.", e);
        }
        return null;
    }

}
