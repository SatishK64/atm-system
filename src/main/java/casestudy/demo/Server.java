package casestudy.demo;

import java.io.*;
import java.net.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
        Path last=null;
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("user_data"))) {
            for (Path path : directoryStream) {
                last = path;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ACC_NUMBER=(last.toString().substring(last.toString().length()-8,last.toString().length()-4));
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


            String command;
            String[] header,values;
            BankAccount ba;
            String output,send;
            double input;
            int i =0;
            while(true){
                try (
                        ServerSocket server = new ServerSocket(port);
                        Socket client = server.accept();
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                ){
                    System.out.println("The server is up and running.");
                command = in.readLine();
                if(command==null){
//                    System.out.println(command);
                    continue;
                }
                System.out.println("command: "+command);
                header = command.split(",");
                System.out.println(Arrays.toString(header));
                if(header[0].equals("0")){

                    ACC_NUMBER=increment_Acc(ACC_NUMBER);
                    command=ACC_NUMBER+","+ command.substring(2);
                    System.out.println(command);
                    ba = new BankAccount(command);
                    out.println(ACC_NUMBER);
                    WriteFile(ba);

                } else if (header[0].equals("1")) {
                    output=FileFinder(header[1]+".txt");
                    values = output.split(",");
                    System.out.println(output +"\n"+ Arrays.toString(values));
                    if(output.equals("Error")){
                        System.out.println("1");
                        out.println("1");
                    } else if (header[2].equals(values[1])){
                            System.out.println("2");
                            out.println("0");
                    }else {
                        System.out.println("0");
                        out.println("2");
                    }
                } else if (header[0].equals("2")) {
                    output=FileFinder(header[2]+".txt");
                    values = output.split(",");
                    switch (header[1]){
                        case "0":// The following if for checking balance. (2,0,ACC_NUM)
                            send = values[2];
                            System.out.println(send);
                            out.println(values[2]);
                            break;
                        case "1":// The following is for Deposit. (2,1,ACC_NUM,Amout)
                            input = Double.parseDouble(header[3]);
                            values[2]=Double.toString(Double.parseDouble(values[2])+input);
                            ba = new BankAccount(String.join(",",values));
                            WriteFile(ba);
                            break;
                        case "2":// The following is for Withdraw. (2,2,ACC_NUM,Amout)
                            input = Double.parseDouble(header[3]);
                            values[2]=Double.toString(Double.parseDouble(values[2])-input);
                            ba = new BankAccount(String.join(",",values));
                            WriteFile(ba);
                            break;
                        default:
                            System.out.println("Error occured.");
                        break;
                    }
                } else if (header[0].equals("3")) {
                    output=FileFinder(header[1]+".txt");
                    if(output.equals("Error.")){
                        System.out.println("Error.");
                    }
                    values = output.split(",");
                    String otp = makeOTP();
                    out.println(otp);
                    sendMessage(values[3],otp);
                }else if(header[0].equals("4")){
                    output=FileFinder(header[1]+".txt");
                    values = output.split(",");
                    values[1]=header[2];
//                    System.out.println(Arrays.toString(values));
                    ba = new BankAccount(String.join(",",values));
                    WriteFile(ba);
                }
                client.close();
                }
                catch (IOException e){
                    System.out.println("There has been an error.");
                }
            }




    }
}
