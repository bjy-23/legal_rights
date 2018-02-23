package com.wonders.bean;


public class FpsiLtScopeEnty   {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 792203803962842023L;

    private String fdId;

    private String tradeScope;

    private String tradeCate;

    private String tradeTypeId;

    private String tradeTypeGb;

    private String tradePackId;

    private String tradePackIdGb;

    private String tradePacks;

    private String tradeBulkId;

    private String tradeBulkIdGb;

    private String tradeBulks;

    private String tradeOtherGb;

    private String tradeOther;

    private String tradeOthers;
    
    private String tradeMilksGb;
    
    private String tradeMilks;

    private String ifSubTrade;

    private String tradePacksSub;

    private String tradeBulksSub;

    private String tradeOthersSub;
    
    private String scope;

    private FpsiLtInfoEnty fpsiLtInfoEnty;

    // Constructors

    /** default constructor */
    public FpsiLtScopeEnty() {
    }

    /** minimal constructor */
    public FpsiLtScopeEnty(String fdId) {
        this.fdId = fdId;
    }

    /** full constructor */
    public FpsiLtScopeEnty(String fdId, String tradeScope, String tradeCate, String tradeTypeId, String tradeTypeGb,
            String tradePackId, String tradePackIdGb, String tradePacks, String tradeBulkId, String tradeBulkIdGb,
            String tradeBulks, String tradeOtherGb, String tradeOther, String tradeOthers, String ifSubTrade,
            String tradeBulksSub, String tradeOthersSub) {
        this.fdId = fdId;
        this.tradeScope = tradeScope;
        this.tradeCate = tradeCate;
        this.tradeTypeId = tradeTypeId;
        this.tradeTypeGb = tradeTypeGb;
        this.tradePackId = tradePackId;
        this.tradePackIdGb = tradePackIdGb;
        this.tradePacks = tradePacks;
        this.tradeBulkId = tradeBulkId;
        this.tradeBulkIdGb = tradeBulkIdGb;
        this.tradeBulks = tradeBulks;
        this.tradeOtherGb = tradeOtherGb;
        this.tradeOther = tradeOther;
        this.tradeOthers = tradeOthers;
        this.ifSubTrade = ifSubTrade;
        this.tradeBulksSub = tradeBulksSub;
        this.tradeOthersSub = tradeOthersSub;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFdId() {
        return fdId;
    }

    public void setFdId(String fdId) {
        this.fdId = fdId;
    }

    public String getTradeScope() {
        return tradeScope;
    }

    public void setTradeScope(String tradeScope) {
        this.tradeScope = tradeScope;
    }

    public String getTradeCate() {
        return tradeCate;
    }

    public void setTradeCate(String tradeCate) {
        this.tradeCate = tradeCate;
    }

    public String getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(String tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }

    public String getTradeTypeGb() {
        return tradeTypeGb;
    }

    public void setTradeTypeGb(String tradeTypeGb) {
        this.tradeTypeGb = tradeTypeGb;
    }

    public String getTradePackId() {
        return tradePackId;
    }

    public void setTradePackId(String tradePackId) {
        this.tradePackId = tradePackId;
    }

    public String getTradePackIdGb() {
        return tradePackIdGb;
    }

    public void setTradePackIdGb(String tradePackIdGb) {
        this.tradePackIdGb = tradePackIdGb;
    }

    public String getTradePacks() {
        return tradePacks;
    }

    public void setTradePacks(String tradePacks) {
        this.tradePacks = tradePacks;
    }

    public String getTradeBulkId() {
        return tradeBulkId;
    }

    public void setTradeBulkId(String tradeBulkId) {
        this.tradeBulkId = tradeBulkId;
    }

    public String getTradeBulkIdGb() {
        return tradeBulkIdGb;
    }

    public void setTradeBulkIdGb(String tradeBulkIdGb) {
        this.tradeBulkIdGb = tradeBulkIdGb;
    }

    public String getTradeBulks() {
        return tradeBulks;
    }

    public void setTradeBulks(String tradeBulks) {
        this.tradeBulks = tradeBulks;
    }

    public String getTradeOtherGb() {
        return tradeOtherGb;
    }

    public void setTradeOtherGb(String tradeOtherGb) {
        this.tradeOtherGb = tradeOtherGb;
    }

    public String getTradeOther() {
        return tradeOther;
    }

    public void setTradeOther(String tradeOther) {
        this.tradeOther = tradeOther;
    }

    public String getTradeOthers() {
        return tradeOthers;
    }

    public void setTradeOthers(String tradeOthers) {
        this.tradeOthers = tradeOthers;
    }

    public String getTradeMilksGb() {
        return tradeMilksGb;
    }

    public void setTradeMilksGb(String tradeMilksGb) {
        this.tradeMilksGb = tradeMilksGb;
    }

    public String getTradeMilks() {
        return tradeMilks;
    }

    public void setTradeMilks(String tradeMilks) {
        this.tradeMilks = tradeMilks;
    }

    public String getIfSubTrade() {
        return ifSubTrade;
    }

    public void setIfSubTrade(String ifSubTrade) {
        this.ifSubTrade = ifSubTrade;
    }

    public String getTradePacksSub() {
        return tradePacksSub;
    }

    public void setTradePacksSub(String tradePacksSub) {
        this.tradePacksSub = tradePacksSub;
    }

    public String getTradeBulksSub() {
        return tradeBulksSub;
    }

    public void setTradeBulksSub(String tradeBulksSub) {
        this.tradeBulksSub = tradeBulksSub;
    }

    public String getTradeOthersSub() {
        return tradeOthersSub;
    }

    public void setTradeOthersSub(String tradeOthersSub) {
        this.tradeOthersSub = tradeOthersSub;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public FpsiLtInfoEnty getFpsiLtInfoEnty() {
        return fpsiLtInfoEnty;
    }

    public void setFpsiLtInfoEnty(FpsiLtInfoEnty fpsiLtInfoEnty) {
        this.fpsiLtInfoEnty = fpsiLtInfoEnty;
    }
}