package cz.remove.old.branches.commandline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Common {

    public static List<String> execute(List<String> command, String directory) {
        ArrayList<String> outputLines = new ArrayList<>();
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(directory));
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    outputLines.add(line);
                }
            }
            return outputLines;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return outputLines;
    }

    public static void stopProgram(String reason){
        System.out.println("ERROR: "+reason);
        System.exit(1);
    }

}
