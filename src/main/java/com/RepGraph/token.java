/**
 * The token class is used to represent a token (usually a word or a meaningful part of a word). Sentences are split into a sequence of tokens.
 * @since 29/08/2020
 */
package com.RepGraph;

public class token {

    /**
     * The token's position in a sentence.
     */
    private int index;

    /**
     * The token's standard form.
     */
    private String form;

    /**
     * The token's canonical form, dictionary form or citation form.
     */
    private String lemma;

    /**
     * Normalized form of names or named entities.
     */
    private String carg;

    /**
     * Default constructor of the token class.
     */
    public token(){}

    /**
     * Fully parameterised constructor of the token class.
     * @param index The token's index.
     * @param form The token's form.
     * @param lemma The token's lemma.
     * @param carg The token's carg.
     */
    public token(int index, String form, String lemma, String carg){
        this.index = index;
        this.form = form;
        this.lemma = lemma;
        this.carg = carg;
    }

    /**
     * Getter method for the token's index.
     * @return Integer The token's index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter method for the token's index.
     * @param index The token's index.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Getter method for the token's form.
     * @return String The token's form.
     */
    public String getForm() {
        return form;
    }

    /**
     * Setter method for the token's form.
     * @param form The token's form.
     */
    public void setForm(String form) {
        this.form = form;
    }

    /**
     * Getter method for the token's lemma.
     * @return String The token's lemma.
     */
    public String getLemma() {
        return lemma;
    }

    /**
     * Setter method for the token's lemma.
     * @param lemma The token's lemma.
     */
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    /**
     * Getter method for the token's carg.
     * @return String The token's carg.
     */
    public String getCarg() {
        return carg;
    }

    /**
     * Setter method for the token's carg.
     * @param carg The token's carg.
     */
    public void setCarg(String carg) {
        this.carg = carg;
    }
}
