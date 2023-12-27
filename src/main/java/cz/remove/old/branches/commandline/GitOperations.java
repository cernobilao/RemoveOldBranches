package cz.remove.old.branches.commandline;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.remove.old.branches.Main;

public class GitOperations {

    private final String repositoryPath;
    private final String startBranch;

    public GitOperations(String repositoryPath) {
        this.repositoryPath = repositoryPath;
        this.startBranch = getCurrentBranch();
    }

    public List<String> listAllBranches() {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("branch");
        List<String> branches = CommandExecutor.execute(command, repositoryPath);
        return branches.stream()
                .map(element -> element.replace("* ", "").trim())
                .collect(Collectors.toList());
    }

    public String getCurrentBranch() {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("branch");
        command.add("--show-current");
        List<String> resultLines = CommandExecutor.execute(command, repositoryPath);
        if (resultLines.isEmpty()) {
            Main.stopProgram("Could not detect current branch. Is the repository path correct?");
        }
        return resultLines.get(0);
    }

    public String getBranchLastCommitDate(String branchName) {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("show");
        command.add("-s");
        command.add("--format=%ci");
        command.add(branchName);
        List<String> resultLines = CommandExecutor.execute(command, repositoryPath);
        if (resultLines.isEmpty()) {
            Main.stopProgram("Could not get the date of the last commit.");
        }
        return resultLines.get(0).substring(0, 10);
    }

    public String getBranchLastCommitDeveloperEmail(String branchName) {
        // git show -s --format=%ci --date=format:"%Y-%m-%d" <branch-name>
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("log");
        command.add("-1");
        command.add("--format=%ae");
        command.add(branchName);
        List<String> resultLines = CommandExecutor.execute(command, repositoryPath);
        if (resultLines.isEmpty()) {
            Main.stopProgram("Could not get last commit author email. Use -l param to skip last commit author check.");
        }
        return resultLines.get(0);
    }

    public List<String> deleteGitBranchIfMerged(String branchName) {
        return deleteGitBranch(branchName, false);
    }

    public List<String> deleteGitBranchWithForce(String branchName) {
        return deleteGitBranch(branchName, true);
    }

    private List<String> deleteGitBranch(String branchName, boolean force) {
        checkoutStartBranch();

        List<String> command = new ArrayList<>();
        startNewGitCommand(command);
        command.add("branch");
        if (force) {
            command.add("-D");
        } else {
            command.add("-d");
        }
        command.add(branchName);
        List<String> executeOutput = CommandExecutor.execute(command, repositoryPath);
        System.out.println(executeOutput);
        return executeOutput;
    }

    public List<String> checkoutStartBranch() {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("checkout");
        command.add(startBranch);
        return CommandExecutor.execute(command, repositoryPath);
    }

    public List<String> getNotMergedBranchNames() {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("branch");
        command.add("--no-merged");
        List<String> trimmedListOfBranches = CommandExecutor.execute(command, repositoryPath).stream()
                .map(String::trim)
                .collect(Collectors.toList());
        return trimmedListOfBranches;
    }

    public List<String> forceFetchBranch(String branchName) {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("fetch");
        command.add("origin");
        CommandExecutor.execute(command, repositoryPath);

        startNewGitCommand(command);
        command.add("checkout");
        command.add(branchName);
        CommandExecutor.execute(command, repositoryPath);

        startNewGitCommand(command);
        command.add("reset");
        command.add("--hard");
        command.add("origin/" + branchName);
        CommandExecutor.execute(command, repositoryPath);

        startNewGitCommand(command);
        command.add("pull");
        return CommandExecutor.execute(command, repositoryPath);
    }

    public boolean isLocalBranchSynchronizedWithOrigin(String branchName) {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("fetch");
        command.add("origin");
        CommandExecutor.execute(command, repositoryPath);

        startNewGitCommand(command);
        command.add("checkout");
        command.add(branchName);
        CommandExecutor.execute(command, repositoryPath);

        startNewGitCommand(command);
        command.add("status");
        List<String> resultLines = CommandExecutor.execute(command, repositoryPath);
        if (resultLines.isEmpty()) {
            Main.stopProgram("Could not detect if the branch is synchronized with origin. Is the origin set correctly?");
        }
        return !resultLines.get(1).contains("have diverged");
    }

    public Boolean makeSureThereAreNoUntrackedFiles() {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("status");
        List<String> resultLines = CommandExecutor.execute(command, repositoryPath);
        if (!resultLines.isEmpty()) {
            if (resultLines.get(resultLines.size() - 1).contains("untracked files present")) {
                Main.stopProgram("Untracked files present. Make sure there are no local "
                        + "changes.");
            }
        }
        return !resultLines.get(1).contains("have diverged");
    }

    public String getCurrentGitLocalUserEmail() {
        List<String> command = new ArrayList<>();
        startGitCommand(command);
        command.add("config");
        command.add("user.email");
        List<String> resultLines = CommandExecutor.execute(command, repositoryPath);
        if (resultLines.isEmpty()) {
            Main.stopProgram("Could not get current git user email. Use -l param to skip last commit author check.");
        }
        return resultLines.get(0);
    }

    private static void startGitCommand(List<String> command) {
        command.add("git");
    }

    private static void startNewGitCommand(List<String> command) {
        command.clear();
        startGitCommand(command);
    }
}
