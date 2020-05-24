package cz.harag.psi.sp.ui;

import javafx.scene.Node;
import javafx.scene.web.WebView;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.commons.text.StringEscapeUtils;

/**
 * @author Patrik Harag
 * @version 2020-05-24
 */
public class MailView {

    private final WebView contentView;

    public MailView() {
        this.contentView = new WebView();
    }

    public void show(MimeMessageParser parser) {
        String content;
        if (parser.hasHtmlContent()) {
            content = parser.getHtmlContent();
        } else {
            content = "<pre>" + StringEscapeUtils.escapeHtml4(parser.getPlainContent()) + "</pre>";
        }
        contentView.getEngine().loadContent(content);
    }

    public void clean() {
        contentView.getEngine().loadContent("");
    }

    public Node getNode() {
        return contentView;
    }

}
