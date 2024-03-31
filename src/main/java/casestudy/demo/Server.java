package casestudy.demo;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            String output;
            while(true){
//                command = in.readLine();
                command = "0,1705,200,satish";
                header = command.split(",");
                if(header[0].equals("0")){

                    ACC_NUMBER=increment_Acc(ACC_NUMBER);
                    ba = new BankAccount(ACC_NUMBER,header[1],Double.parseDouble(header[2]),header[3]);
//                    out.println(ACC_NUMBER);
                    WriteFile(ba);

                } else if (header[0].equals(1)) {
                    output=FileFinder(header[2]);
                    values = output.split(",");
                    switch (header[1]){
                        case "0":

                            System.out.println(output);
                            break;
                        case "1":


                    }
                }
            }

        }
        catch (IOException e){
            System.out.println("There has been an error.");
        }


    }
}
