package casestudy.demo;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class Example {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC789d954d1a63fd8ce2d72f6c8f71b34a";
    public static final String AUTH_TOKEN = "90f33a3fe8b1e32ed5b9dd0e9948d853";
    public static void sendMessage(String phoneNum,String OTP){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+91"+phoneNum),
                new com.twilio.type.PhoneNumber("+13192545845"),
                "your otp is :"+OTP

        ).create();
    }
    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+916309413050"),
                new com.twilio.type.PhoneNumber("+13192545845"),
                "your otp is so and so"

        ).create();

        System.out.println(message.getSid());
    }
}