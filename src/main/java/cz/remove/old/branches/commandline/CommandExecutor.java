package cz.remove.old.branches.commandline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class CommandExecutor {

    static List<String> execute(List<String> command, String directory) {
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
                addErrorLinesIntoOutputLines(outputLines, process);
            }
            return outputLines;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return outputLines;
    }

    private static void addErrorLinesIntoOutputLines(ArrayList<String> outputLines, Process process) throws IOException {
        String line;
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = errorReader.readLine()) != null) {
            outputLines.add(line);
        }
    }

}
