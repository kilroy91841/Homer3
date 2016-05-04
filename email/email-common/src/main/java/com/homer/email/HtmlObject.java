package com.homer.email;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by arigolub on 5/3/16.
 */
public final class HtmlObject {

    private final HtmlTag tag;

    private List<HtmlObject> children = Lists.newArrayList();
    private List<String> classes = Lists.newArrayList();
    private List<String> styles = Lists.newArrayList();
    private String body;

    private HtmlObject(HtmlTag tag) {
        this.tag = tag;
    }

    public HtmlObject body(String body) {
        this.body = body;
        return this;
    }

    public HtmlObject child(HtmlObject object) {
        this.children.add(object);
        return this;
    }

    public HtmlObject withClass(String cssClass) {
        this.classes.add(cssClass);
        return this;
    }

    public HtmlObject withStyle(String styleName, String styleValue) {
        this.styles.add(styleName + ":" + styleValue);
        return this;
    }

    public static HtmlObject of(HtmlTag tag) {
        return new HtmlObject(tag);
    }

    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        addBodyOrChildren(sb);
        sb.append("</html>");
        return sb.toString();
    }

    private String toChildHtml() {
        StringBuilder sb = new StringBuilder("<" + tag + " ");
        if (classes.size() > 0) {
            sb.append("class=\"");
            sb.append(Joiner.on(" ").join(classes));
            sb.append("\"");
        }
        if (styles.size() > 0) {

        }
        sb.append(">");
        addBodyOrChildren(sb);
        sb.append("</" + tag + ">");
        return sb.toString();
    }

    private void addBodyOrChildren(StringBuilder sb) {
        if (body != null) {
            sb.append(body);
        } else {
            children.forEach(c -> {
                sb.append(c.toChildHtml());
            });
        }
    }

    @Override
    public String toString() {
        return "HtmlObject{" +
                "tag=" + tag +
                ", children=" + children +
                ", classes=" + classes +
                ", body='" + body + '\'' +
                '}';
    }
}
