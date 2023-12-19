package cz.remove.old.branches;


import java.io.File;

public class Main {

    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser(args);

        if (argumentParser.isHelpRequested()) {
            argumentParser.printUsageAndExit();
        }

        String repositoryPath = new File(System.getProperty("user.dir")).getAbsolutePath();

        OldBranchesRemover.removeOldBranches(repositoryPath, argumentParser);

    }

}
