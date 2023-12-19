package cz.remove.old.branches;

public class ArgumentParser {

    private boolean helpRequested;
    private boolean skipLastCommitAuthorCheck = false;
    private boolean doNotAsk;
    private int olderThanDays = 30;
    private String branchNamePrefix = "feature";
    private boolean forceDelete;
    public ArgumentParser(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "--help":
                    helpRequested = true;
                    return;
                case "-l":
                case "--skip-last-commit-author":
                    skipLastCommitAuthorCheck = true;
                    break;
                case "-a":
                case "--do-not-ask":
                    doNotAsk = true;
                    break;
                case "-o":
                case "--older-than-days":
                    if (i + 1 < args.length) {
                        try {
                            olderThanDays = Integer.parseInt(args[i + 1]);
                            i++;  // Skip the next argument since it's already processed
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid value for --older-than-days (-d): " + args[i + 1]);
                            printUsageAndExit();
                        }
                    } else {
                        System.err.println("--older-than-days (-d) option requires a value.");
                        printUsageAndExit();
                    }
                    break;
                case "-p":
                case "--branch-name-prefix":
                    if (i + 1 < args.length) {
                            branchNamePrefix = args[i + 1].replaceAll("\"","").replaceAll("\'","");
                            i++;  // Skip the next argument since it's already processed
                    } else {
                        printUsageAndExit();
                    }
                    break;
                case "-D":
                case "--force-delete":
                    forceDelete = true;
                    break;

                default:
                    System.err.println("Invalid argument: " + args[i]);
                    printUsageAndExit();
            }
        }
    }

    public boolean isHelpRequested() {
        return helpRequested;
    }

    public boolean isSkipLastCommitAuthorCheck() {
        return skipLastCommitAuthorCheck;
    }

    public boolean isDoNotAsk() {
        return doNotAsk;
    }

    public int getOlderThanDays() {
        return olderThanDays;
    }

    public String getBranchNamePrefix() {
        return branchNamePrefix;
    }

    public boolean isForceDelete() {
        return forceDelete;
    }

    public void printUsageAndExit() {
        System.out.println("Usage: OldBranchRemover [options]");
        System.out.println("Options:");
        System.out.println("  -h, --help                        Show help message.");
        System.out.println("  -l, --skip-last-commit-author     Skip last commit author check. By default only branches from other users are deleted.");
        System.out.println("  -a, --do-not-ask                  Do not ask anything, just do it.");
        System.out.println("  -o, --older-than-days <days>      Set how many days the branch must be old to be deleted. (default: 30)");
        System.out.println("  -p, --branch-name-prefix <prefix> Set a name prefix to filter the branches (default: 'feature'). If set to '', all branches will be processed.");
        System.out.println("  -D, --force-delete                Forcefully deletes the local branch, regradless of its merged status.");
        System.exit(0);
    }


}