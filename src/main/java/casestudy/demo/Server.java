package casestudy.demo;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static casestudy.demo.Example.makeOTP;
import static casestudy.demo.Example.sendMessage;
import static casestudy.demo.FileFind.FileFinder;
import static casestudy.demo.FileFind.WriteFile;


public class Server {

    static String increment_Acc(String acc){
        int i = Integer.parseInt(acc);
        i++;
        acc = Integer.toString(i);
        while(acc.length()<4){
            acc="0" + acc;
        }
        return acc;
    }

    public static void main(String[] args) throws IOException {

        String ACC_NUMBER="0000";
        Path directoryPath = Paths.get("user_data");
        Files.createDirectories(directoryPath);
        int port = 12694;

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            System.out.println("The server ip is : "+ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try (
                ServerSocket server = new ServerSocket(port);
//            Socket client = server.accept();
//            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ){
            System.out.println("The server is up and running.");
            String command;
            String[] header,values;
            BankAccount ba;
            String output,send;
            double input;
            int i =0;
            while(i==0){
//                command = in.readLine();
                command = "2,0001";
                header = command.split(",");
                if(header[0].equals("0")){

                    ACC_NUMBER=increment_Acc(ACC_NUMBER);
                    command=ACC_NUMBER+","+ command.substring(2);
                    System.out.println(command);
                    ba = new BankAccount(command);
//                    out.println(ACC_NUMBER);
                    WriteFile(ba);

                } else if (header[0].equals("1")) {
                    output=FileFinder(header[2]+".txt");
                    values = output.split(",");
                    switch (header[1]){
                        case "0":// The following if for checking balance. (1,0,ACC_NUM)
                            send = values[2];
                            System.out.println(send);
                            i++;
                            break;
                        case "1":// The following is for Deposit. (1,1,ACC_NUM,Amout)
                            input = Double.parseDouble(header[3]);
                            values[2]=Double.toString(Double.parseDouble(values[2])+input);
                            ba = new BankAccount(String.join(",",values));
                            WriteFile(ba);
                            i++;
                            break;
                        case "2":// The following is for Withdraw. (1,2,ACC_NUM,Amout)
                            input = Double.parseDouble(header[3]);
                            values[2]=Double.toString(Double.parseDouble(values[2])-input);
                            ba = new BankAccount(String.join(",",values));
                            WriteFile(ba);
                            i++;
                            break;
                        default:
                            System.out.println("Error occured.");
                        break;
                    }
                } else if (header[0].equals("2")) {
                    output=FileFinder(header[1]+".txt");
                    values = output.split(",");
                    String otp = makeOTP();
                    //send otp
                    sendMessage(values[3],otp);
                    if(true){
                        values[1]="2005";
                    }
                    ba = new BankAccount(String.join(",",values));
                    WriteFile(ba);
                    i++;
                }else {
                    System.out.println("Error.");
                }
            }

        }
        catch (IOException e){
            System.out.println("There has been an error.");
        }


    }
}
