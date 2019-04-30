package com.teamand.bookstore.model;

public class ResponseCurrency {


    private boolean success;
    private String terms;
    private String privacy;
    private int timestamp;
    private String source;
    private QuotesBean quotes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public QuotesBean getQuotes() {
        return quotes;
    }

    public void setQuotes(QuotesBean quotes) {
        this.quotes = quotes;
    }

    public static class QuotesBean {

        private double USDVND;

        public double getUSDVND() {
            return USDVND;
        }

        public void setUSDVND(double USDVND) {
            this.USDVND = USDVND;
        }
    }
}
