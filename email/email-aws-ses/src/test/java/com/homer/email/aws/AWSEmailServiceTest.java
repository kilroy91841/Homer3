package com.homer.email.aws;

import com.google.common.collect.Lists;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by arigolub on 5/3/16.
 */
public class AWSEmailServiceTest {

    private AWSEmailService service = new AWSEmailService();

    @Test
    public void testEmail() {
        HtmlObject htmlObject =
                HtmlObject.of(HtmlTag.DIV).withClass("container")
                    .child(
                            HtmlObject.of(HtmlTag.DIV).withClass("row").child(
                                    HtmlObject.of(HtmlTag.DIV).withClass("col-md-6").child(
                                        HtmlObject.of(HtmlTag.P).body("Column 1")
                                    )
                            ).child(
                                    HtmlObject.of(HtmlTag.DIV).withClass("col-md-6").child(
                                            HtmlObject.of(HtmlTag.P).body("Column 2")
                                    )
                            )
                    )
                    .child(
                            HtmlObject.of(HtmlTag.P).body("And this is a second P")
                    );
        System.out.println(htmlObject.toHtml());
        //EmailRequest emailRequest = new EmailRequest(Lists.newArrayList("arigolub@gmail.com"), "Test Email", htmlObject);
        //assertNotNull(service.sendEmail(emailRequest));
    }
}
