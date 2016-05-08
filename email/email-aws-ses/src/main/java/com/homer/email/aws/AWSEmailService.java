package com.homer.email.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.homer.email.EmailRequest;
import com.homer.email.IEmailService;

import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/3/16.
 */
public class AWSEmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(AWSEmailService.class);

    private static String EMAIL_FROM_ADDRESS;

    static {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration("email.properties");
            EMAIL_FROM_ADDRESS = config.getString("fromAddress");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public String sendEmail(EmailRequest emailRequest) {
        if (emailRequest == null) {
            throw new IllegalArgumentException("Email request must have subject");
        }
        if (emailRequest.getToAddresses().size() == 0) {
            throw new IllegalArgumentException("Email request must have at least one recipient");
        }
        if (emailRequest.getHtmlObject() == null) {
            throw new IllegalArgumentException("Email request must have html object");
        }

        logger.info("Sending email: " + emailRequest);

        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(emailRequest.getToAddresses());

        // Create the subject and body of the message.
        Content subject = new Content().withData(emailRequest.getSubject());
        String html = emailRequest.getHtmlObject().toHtml();
        Content textBody = new Content().withData(html);
        Body body = new Body().withHtml(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(EMAIL_FROM_ADDRESS).withDestination(destination).withMessage(message);

        try
        {
            logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials.
            // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials
            // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables
            // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY.
            // For more information, see http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();

            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your sandbox
            // status, sending limits, and Amazon SES identity-related settings are specific to a given AWS
            // region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
            // the US West (Oregon) region. Examples of other regions that Amazon SES supports are US_EAST_1
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
            Region REGION = Region.getRegion(Regions.EU_WEST_1);
            client.setRegion(REGION);

            // Send the email.
            SendEmailResult result = client.sendEmail(request);
            logger.info("Email sent!");
            return result.getMessageId();
        }
        catch (Exception ex)
        {
            logger.error("The email was not sent. Error message: " + ex.getMessage());
            return null;
        }
    }
}
