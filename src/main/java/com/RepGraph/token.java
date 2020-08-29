package com.RepGraph;

public class token {
    private String index;
    private String form;
    private String lemma;
    private String carg;

    public token(){}

    public token(String index, String form, String lemma, String carg){
        this.index = index;
        this.form = form;
        this.lemma = lemma;
        this.carg = carg;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getCarg() {
        return carg;
    }

    public void setCarg(String carg) {
        this.carg = carg;
    }
}
