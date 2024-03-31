package casestudy.demo;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFind {

    public static String FileFinder(String File){
        String directoryPath = "D:\\Java Project\\demo\\user_data";
        File file = new File(directoryPath, File);

        try {
            if (file.exists()) {
                System.out.println("File " + File + " exists.");

                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                if ((line = bufferedReader.readLine()) != null) {
                    return line;
                }

                bufferedReader.close();
            } else {
                System.out.println("File " + File + " does not exist.");
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Permission denied to access the file: " + File);
        }

        return "Error";
    }

    public static void WriteFile(BankAccount account){
        String directoryPath = "D:\\Java Project\\demo\\user_data";
        String fileName = account.getFileName();
        Path filePath = Paths.get(directoryPath, fileName);
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath.toFile())))) {
            writer.println(account.toCSV());
        } catch (IOException e) {
            System.out.println("There has been an error in writing file.");
        }
    }

    public static void main(String[] args) {
        String avail = FileFinder("0001.txt");
        System.out.println(avail);
        String not = FileFinder("0003.txt");
        System.out.println(not);
    }

}
