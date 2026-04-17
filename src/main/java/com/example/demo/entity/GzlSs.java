package com.example.demo.entity;

public class GzlSs {
    private Integer id;
    private String statDate;   // 统计日期
    private String statTime;   // 统计时间
    private String deptName;   // 部门
    private String groupName; // 小组
    private String staffName; // 人员
    private String shiftType; // 排班
    private Double standardWorkload; // 标准工作量
    private Integer surveyCount;      // 当日查勘量
    private Integer surveyUnfinished; // 查勘未完成
    private Integer damageSubmit;     // 定损提交
    private Integer damageFinish;     // 定损完成
    private Integer damagePay;         // 定损支付
    private Integer firstFollow;      // 首跟
    private Integer mediation;        // 调解
    private Integer caseClose;        // 结案
    private Double totalWorkload;     // 合计工作量

    // getter & setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getStatDate() { return statDate; }
    public void setStatDate(String statDate) { this.statDate = statDate; }
    public String getStatTime() { return statTime; }
    public void setStatTime(String statTime) { this.statTime = statTime; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getShiftType() { return shiftType; }
    public void setShiftType(String shiftType) { this.shiftType = shiftType; }
    public Double getStandardWorkload() { return standardWorkload; }
    public void setStandardWorkload(Double standardWorkload) { this.standardWorkload = standardWorkload; }
    public Integer getSurveyCount() { return surveyCount; }
    public void setSurveyCount(Integer surveyCount) { this.surveyCount = surveyCount; }
    public Integer getSurveyUnfinished() { return surveyUnfinished; }
    public void setSurveyUnfinished(Integer surveyUnfinished) { this.surveyUnfinished = surveyUnfinished; }
    public Integer getDamageSubmit() { return damageSubmit; }
    public void setDamageSubmit(Integer damageSubmit) { this.damageSubmit = damageSubmit; }
    public Integer getDamageFinish() { return damageFinish; }
    public void setDamageFinish(Integer damageFinish) { this.damageFinish = damageFinish; }
    public Integer getDamagePay() { return damagePay; }
    public void setDamagePay(Integer damagePay) { this.damagePay = damagePay; }
    public Integer getFirstFollow() { return firstFollow; }
    public void setFirstFollow(Integer firstFollow) { this.firstFollow = firstFollow; }
    public Integer getMediation() { return mediation; }
    public void setMediation(Integer mediation) { this.mediation = mediation; }
    public Integer getCaseClose() { return caseClose; }
    public void setCaseClose(Integer caseClose) { this.caseClose = caseClose; }
    public Double getTotalWorkload() { return totalWorkload; }
    public void setTotalWorkload(Double totalWorkload) { this.totalWorkload = totalWorkload; }
}