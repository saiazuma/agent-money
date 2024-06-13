// 帳本的class

package com.example.agentmoney;

public class Ledger {

    private String type;
    private String moneyStr;
    private int moneyInt;
    private String date;

    public Ledger() {

    }

    public Ledger(String type, String moneyStr, int moneyInt, String date) {
        this.type = type;
        this.moneyStr = moneyStr;
        this.moneyInt = moneyInt;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    public int getMoneyInt() {
        return moneyInt;
    }

    public void setMoneyInt(int moneyInt) {
        this.moneyInt = moneyInt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
