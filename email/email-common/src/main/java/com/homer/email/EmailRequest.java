package com.homer.email;

import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by arigolub on 5/3/16.
 */
public class EmailRequest {

    private List<String> toAddresses;
    private String subject;
    private HtmlObject htmlObject;

    public EmailRequest(List<String> toAddresses, String subject, HtmlObject htmlObject) {
        this.toAddresses = toAddresses;
        this.subject = subject;
        this.htmlObject = htmlObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailRequest that = (EmailRequest) o;
        return Objects.equal(toAddresses, that.toAddresses) &&
                Objects.equal(subject, that.subject) &&
                Objects.equal(htmlObject, that.htmlObject);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(toAddresses, subject, htmlObject);
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "toAddresses=" + toAddresses +
                ", subject='" + subject + '\'' +
                ", htmlObject=" + htmlObject +
                '}';
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(List<String> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HtmlObject getHtmlObject() {
        return htmlObject;
    }

    public void setHtmlObject(HtmlObject htmlObject) {
        this.htmlObject = htmlObject;
    }
}
