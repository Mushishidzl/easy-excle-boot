package org.mushi.model;

/**
 * @ClassName: UserWorkInfo
 * @Description: TODO
 * @Author: mushishi
 * @Date: 2019/11/26 14:19
 * @Version: V1.0
 **/
public class UserWorkInfo {

    /**
     * 月份
     */
    private Integer monthNum;

    /**
     * 几号
     */
    private Integer dayNum;

    /**
     * 是否休息日
     */
    private boolean noWork;

    /**
     * 员工编号
     */
    private String userNo;


    /**
     * 员工名称
     */
    private String username;

    /**
     * 上班打卡时间
     */
    private String beginTime;

    /**
     * 下班打卡时间
     */
    private String endTime;


    private double workTime;

    /**
     * 工作总时长
     */
    private double totalWorkTime;

    private String desc;



    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(double workTime) {
        this.workTime = workTime;
    }


    public Integer getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(Integer monthNum) {
        this.monthNum = monthNum;
    }

    public double getTotalWorkTime() {
        return totalWorkTime;
    }

    public void setTotalWorkTime(double totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public boolean isNoWork() {
        return noWork;
    }

    public void setNoWork(boolean noWork) {
        this.noWork = noWork;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
