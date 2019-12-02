package org.mushi.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: CommonUtil
 * @Description: TODO
 * @Author: mushishi
 * @Date: 2019/11/26 15:12
 * @Version: V1.0
 **/
@Slf4j
@UtilityClass
public class CommonUtil {

    /**
     * 将时间串转换为数组列表
     * @param s
     * @return
     */
    public  List<String> timeStrToList(String s) {
        int l = s.length();
        List<String> time = new ArrayList<String>();
        for(int i=0;i<l/5;i++) {
            time.add(i, s.substring(i*5, (i+1)*5)) ;
        }
        //去重
        removeDuplicate(time);
        return time;
    }

    /**
     * 递归去除重复打卡时间
     * @param timeArr
     */
    public  void removeDuplicate(List<String> timeArr) {
        String before = null;
        boolean flag = false;
        for(int i=0;i<timeArr.size();i++) {
            if(null==before) {
                before = timeArr.get(i);
                continue;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date beginTime = sdf.parse(before);
                Date endTime = sdf.parse(timeArr.get(i));
                //10分钟之内为重复打卡
                if( (endTime.getTime() - beginTime.getTime())/(1000*60)<10) {
                    //重复保留当前时间，移除上次打卡时间
                    timeArr.remove(i-1);
                    flag = true;
                    break;
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //当前时间作为下一次的开始时间
            before=timeArr.get(i);
        }
        //找到重复打卡>继续去重
        if(flag) {
            removeDuplicate(timeArr);
        }
    }

    /**
     * 计算每个班的时间（超出半小时按半小时算，不足半小时直接忽略）
     * @param begin 上班时间
     * @param end 下班时间
     * @return
     */
    public  double calcTime(String begin, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date beginTime = sdf.parse(begin);
            Date endTime = sdf.parse(end);
            double tt = (endTime.getTime() - beginTime.getTime())/(1000*60);
            if(tt/60-(long)(tt/60)>=0.5) {
                return (long)(tt/60)+0.5;
            }else {
                return (long)(tt/60);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }




}
