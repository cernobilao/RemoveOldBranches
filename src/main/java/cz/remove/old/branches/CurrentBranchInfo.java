package cz.remove.old.branches;

public class CurrentBranchInfo {

    private final String name;

    private final String lastCommitDeveloperEmail;

    private final boolean isNotFullyMerged;

    public CurrentBranchInfo(String name, String lastCommitDeveloperEmail, boolean isNotFullyMerged) {
        this.name = name;
        this.lastCommitDeveloperEmail = lastCommitDeveloperEmail;
        this.isNotFullyMerged = isNotFullyMerged;
    }

    public String getName() {
        return name;
    }

    public String getLastCommitDeveloperEmail() {
        return lastCommitDeveloperEmail;
    }

    public boolean isNotFullyMerged() {
        return isNotFullyMerged;
    }
}
