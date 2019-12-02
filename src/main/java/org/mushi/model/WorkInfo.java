package org.mushi.model;

import lombok.Data;

/**
 * @ClassName: WorkStatus
 * @Description: 考勤记录
 * @Author: mushishi
 * @Date: 2019/11/27 14:57
 * @Version: V1.0
 **/
@Data
public class WorkInfo {


   /**
    * 年份
    */
   private String yearNum;

   /**
    * 月份
    */
   private String monthNum;

   /**
    * 几号
    */
   private String dayNum;

   /**
    * 工号
    */
   private String userNo;

   /**
    * 姓名
    */
   private String username;

    /**
     * 状态    0：正常  1：迟到  2：早退  3：上班缺卡  4：下班缺卡  5：上下班缺卡  6：休息  7：加班  8：请假
     */
   private int status;

   /**
    * 上班时间
    */
   private String beginTime;

   /**
    * 下班时间
    */
   private String endTime;

   /**
    * 当天工作的时间
    */
   private String totalTime;

   /**
    * 描述
    */
   private String desc;








}
