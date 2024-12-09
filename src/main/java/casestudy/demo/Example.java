package casestudy.demo;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;
import java.net.URI;
import java.math.BigDecimal;

public class Example {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "TWILIO_ACCT_ID";
    public static final String AUTH_TOKEN = "TWILIO_AUTH_TOKEN";
    public static void sendMessage(String phoneNum,String OTP){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message msg = Message.creator(
                new com.twilio.type.PhoneNumber("+91"+phoneNum),
                new com.twilio.type.PhoneNumber("+12563635513"),
                "your otp is : "+OTP

        ).create();
    }
    public static String makeOTP(){
        Random rand = new Random();
        int x = rand.nextInt(9999);
        String s = Integer.toString(x);
        while(s.length()<4){
            s="0"+s;
        }
        return s;
    }
    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+918309686570"),
                new com.twilio.type.PhoneNumber("+13192545845"),
                "your otp is so and so"

        ).create();

        System.out.println(message.getSid());
        for (int i =0;i<10;i++)
            System.out.println(makeOTP());
    }
}