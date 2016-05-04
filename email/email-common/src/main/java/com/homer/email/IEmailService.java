package com.homer.email;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/3/16.
 */
public interface IEmailService {

    @Nullable
    String sendEmail(EmailRequest emailRequest);
}
