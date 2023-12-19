package cz.remove.old.branches;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import cz.remove.old.branches.commandline.Common;
import cz.remove.old.branches.commandline.GitOperations;


public class OldBranchesRemover {

    public static void removeOldBranches(String repositoryPath, ArgumentParser argumentParser) {
        System.out.println("Repository path: [" + repositoryPath + "]");
        GitOperations gitOperations = new GitOperations(repositoryPath);
        startBranchShouldNotBeOneOfProcessedBranches(argumentParser, gitOperations.getCurrentBranch());
        gitOperations.makeSureThereAreNoUntrackedFiles();
        String developerEmailToSkip = getDeveloperEmailToSkip(argumentParser, gitOperations);

        System.out.println(
                "Processing branches only with the name prefix: [" + argumentParser.getBranchNamePrefix() + "]");

        List<String> notMergedBranchNames = gitOperations.getNotMergedBranchNames();

        List<String> branches = gitOperations.listAllBranches();

        for (String branchName : branches) {
            System.out.print("Checking branch: [" + branchName + "]");
            boolean isNotFullyMerged = notMergedBranchNames.contains(branchName);
            if (isNotFullyMerged && !argumentParser.isForceDelete()) {
                System.out.println(" SKIPPED - Branch is not fully merged. Use -D param to override.");
                continue;
            }
            if (!branchName.startsWith(argumentParser.getBranchNamePrefix()) && !argumentParser.getBranchNamePrefix()
                    .isEmpty()) {
                System.out.println(" SKIPPED - Does not fit the prefix. Use -p param to change the prefix.");
                continue;
            }
            String lastCommitDeveloperEmail = gitOperations.getBranchLastCommitDeveloperEmail(branchName);
            if (developerEmailToSkip != null) {
                if (lastCommitDeveloperEmail.toLowerCase().contains(developerEmailToSkip)) {
                    System.out.println(
                            " SKIPPED - Last commit author has same email as current git user. Use -l param to skip author check. "
                                    + lastCommitDeveloperEmail);
                    continue;
                }
            }
            System.out.println();
            CurrentBranchInfo currentBranchInfo = new CurrentBranchInfo(branchName, lastCommitDeveloperEmail,
                    isNotFullyMerged);
            if (!gitOperations.islocalBranchSynchronizedWithOrigin(branchName)) {
                forcePullBranchDialog(gitOperations, argumentParser, currentBranchInfo);
            }

            ifTheBranchIsTooOldDelete(gitOperations, argumentParser, currentBranchInfo);
        }

    }

    private static String getDeveloperEmailToSkip(ArgumentParser argumentParser, GitOperations gitOperations) {
        if (argumentParser.isSkipLastCommitAuthorCheck()) {
            return null;
        }
        String developerEmailToSkip = gitOperations.getCurrentGitLocalUserEmail();
        System.out.println("Current git user: [" + developerEmailToSkip + "]");
        return developerEmailToSkip;
    }

    private static void startBranchShouldNotBeOneOfProcessedBranches(ArgumentParser argumentParser,
            String startBranch) {
        if (startBranch.startsWith(argumentParser.getBranchNamePrefix()) && !argumentParser.getBranchNamePrefix()
                .isEmpty()) {
            Common.stopProgram("The current branch match prefix of processed branches. Make sure that you start on a branch that "
                    + "you don't want to delete. For example master branch. Current branch: "
                    + startBranch);
        }
    }

    private static void ifTheBranchIsTooOldDelete(GitOperations gitOperations, ArgumentParser argumentParser,
            CurrentBranchInfo currentBranchInfo) {
        String lastCommitDate = gitOperations.getBranchLastCommitDate(currentBranchInfo.getName());
        try {
            LocalDate commitDate = LocalDate.parse(lastCommitDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate oldBranchDate = LocalDate.now().minusDays(argumentParser.getOlderThanDays());
            if (commitDate.isBefore(oldBranchDate)) {
                deleteBranchDialog(gitOperations, argumentParser, currentBranchInfo, commitDate);
            } else {
                System.out.println(
                        "The commit date is not old enough. " + commitDate + " Last commit date should be older than: "
                                + argumentParser.getOlderThanDays());
            }
        } catch (Exception e) {
            Common.stopProgram("Could not get the date of the last commit.");
        }
    }

    private static void forcePullBranchDialog(GitOperations gitOperations, ArgumentParser argumentParser,
            CurrentBranchInfo currentBranchInfo) {
        System.out.println(
                "Local branch is not synchronized with origin. "
                        + "\nReady to FORCE PULL branch: [" + currentBranchInfo.getName() + "]"
                        + "\nLast commit author: [" + currentBranchInfo.getLastCommitDeveloperEmail() + "]");
        if (argumentParser.isDoNotAsk()) {
            gitOperations.forceFetchBranch(currentBranchInfo.getName());
        } else if (UserAsker.askWhetherToContinue(
                "WARNING! Differences on the local branch will be lost. Do you want to continue?")) {
            gitOperations.forceFetchBranch(currentBranchInfo.getName());
        }
    }

    private static void deleteBranchDialog(GitOperations gitOperations, ArgumentParser argumentParser,
            CurrentBranchInfo currentBranchInfo,
            LocalDate commitDate) {
        System.out.println(
                "The last commit date is older than " + argumentParser.getOlderThanDays() + " days. "
                        + commitDate + "."
                        + "\nReady to DELETE branch: [" + currentBranchInfo.getName() + "]"
                        + "\nLast commit author: [" + currentBranchInfo.getLastCommitDeveloperEmail() + "]");
        if (currentBranchInfo.isNotFullyMerged()) {
            if (argumentParser.isDoNotAsk()) {
                gitOperations.deleteGitBranchWithForce(currentBranchInfo.getName());
            } else if (UserAsker.askWhetherToContinue(
                    "WARNING! The branch is not fully merged. Differences on the local branch will be lost. Do you "
                            + "want to continue?")) {
                gitOperations.deleteGitBranchWithForce(currentBranchInfo.getName());
            }
        } else {
            if (argumentParser.isDoNotAsk()) {
                gitOperations.deleteGitBranchIfMerged(currentBranchInfo.getName());
            } else if (UserAsker.askWhetherToContinue(
                    "WARNING! Differences on the local branch will be lost. Do you want to continue?")) {
                gitOperations.deleteGitBranchIfMerged(currentBranchInfo.getName());
            }
        }
    }
}
