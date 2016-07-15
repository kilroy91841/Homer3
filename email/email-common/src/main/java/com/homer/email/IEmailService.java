package com.homer.email;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/3/16.
 */
public interface IEmailService {

    static final String COMMISSIONER_EMAIL = "arigolub@gmail.com";

    @Nullable
    String sendEmail(EmailRequest emailRequest);
}
