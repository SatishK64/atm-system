package casestudy.demo;
import java.io.*;
import java.nio.file.*;


public class BankAccount {
    private String accountNumber;
    private double balance;
    private String ownerName;
    private String pin;

    public BankAccount(String accountNumber,String pin, double balance, String ownerName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerName = ownerName;
        this.pin=pin;
    }

    // Getters and setters

    public String toCSV() {
        return accountNumber+","+pin+","+balance+","+ownerName;
    }

    public String getFileName() {
        return  accountNumber + ".txt";
    }

    public BankAccount fromCSV(String csv) {
        String[] fields = csv.split(",");
        String accountNumber = fields[0];
        String pin = fields[1];
        double balance = Double.parseDouble(fields[2]);
        String ownerName = fields[3];

        return new BankAccount(accountNumber, pin, balance, ownerName);
    }


    public static void main(String[] args) {
        Path directoryPath;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            // Create a directory to store individual user text files
            directoryPath = Paths.get("user_data");
            Files.createDirectories(directoryPath);

            while (true) {
                System.out.println("Enter account number (type 'exit' to quit):");
                String accountNumber = br.readLine();
                if (accountNumber.equalsIgnoreCase("exit")) break;

                System.out.println("Enter balance:");
                double balance = Double.parseDouble(br.readLine());

                System.out.println("Enter owner's name:");
                String ownerName = br.readLine();

                System.out.println("Enter the pin:");
                String pin = br.readLine();

                BankAccount account = new BankAccount(accountNumber, pin, balance, ownerName);

                // Create a new text file for each user
                String fileName = account.getFileName();
                Path filePath = directoryPath.resolve(fileName);
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath.toFile())))) {
                    writer.println(account.toCSV());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // List all user text files in the directory
        System.out.println("User data files:");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path path : directoryStream) {
                System.out.println(path.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


