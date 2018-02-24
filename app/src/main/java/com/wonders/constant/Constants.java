package com.wonders.constant;

public class Constants {
    public static String TYPE = "";//生产环节
    public static final String SOP_LT_ITEM = "sopLtItem";
    public static final String SOP_LT = "sopLt";
    public static final String SOP_LT_ITEM_LIST = "sopLtItemList";
    public static final String ETPS_ID = "etpsId";
    public static final String ETPS_NAME = "etpsName";
    public static final String ADDRESS = "address";
    public static final String LIC_NO = "licNo";
    public static final String PLAN_ID = "planId";
    public static final String PLAN_TYPE = "planType";
    public static final String DOC_TYPE = "docType";
    public static final String GROUPS = "groups";
    public static final String CHILDREN = "children";
    public static final String GRADE = "grade";
    public static final String ALL_USER_NAME = "allUserName";
    public static final String TASK_TYPE = "taskType";
    public static final String QUERY_TYPE = "queryType";
    public static final String PARAMS = "params";
    public static final String TITLE = "title";
    public static final String PRINT_SOFTWARE_NAME = "com.dynamixsoftware.printershare";
    public static final String JDGL = "监督管理";
    public static final String QYXXCX = "企业信息查询";
    public static final String JGJLCX = "监管记录查询";
    public static final String WSDY = "文书打印";
    public static final String SZ = "设置";
    public static final String RCJC = "日常检查";
    public static final String HFRW = "回访任务";
    public static final String HZ = "汇总";
    public static final String BJ = "办结";
    /**
     * 计划状态
     */
    public interface PlanStatus {
        public final static String REGISTED_PLAN = "001";//已登记
        public final static String DISTRIBUTED_PLAN = "002";//已分配
        public final static String FINISHED_PLAN = "003";//已办结
        public final static String NO_REVISIT_PLAN = "004";//未回访
        public final static String REVISIT_PLAN = "005";//已回访
        public final static String UNCHECK_PLAN = "006";//未检查
    }
}
