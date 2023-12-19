# Remove Old Branches
Remove Old Branches is a command-line tool that simplifies the process of cleaning a repository from old local branches.

## Build
Navigate to the root directory of the project and run the following command:
```
mvn clean install
```
The runnable jar will be created in the `target` directory:
```
.\target\old-branch-remover-1.0-SNAPSHOT-jar-with-dependencies.jar
```
## Installation

### Windows
Run **InstallToPathVarOnWindows.bat** to add the DevToolKit directory with runnable bat file to your PATH variable. This will allow you to run OldBranchRemover from any directory.
### Linux
alias OldBranchRemover="java -jar %~dp0\old-branch-remover\target\old-branch-remover-1.0-SNAPSHOT-jar-with-dependencies.jar"
## Usage

Navigate to the root directory of your Git repository and run OldBranchRemover.bat:
```
cd C:\Path\To\Git\Repo
OldBranchRemover
```
Use arguments to customize executed operations. For example to remove only branches older than 60 days and ignore the author of the last commit use:
```
OldBranchRemover -o 60 -l
```
The following options are available:
```
-h, --help                          Show help message
-l, --skip-last-commit-author       Skip last commit author check
-a, --do-not-ask                    Do not ask anything, just do it.
-o, --older-than-days <days>"       Set how many days the branch must be old to be deleted (default: 30)"
-p, --branch-name-prefix <prefix>"  Set a name prefix to filter the branches (default: 'feature'). If set to '', all branches will be processed.
-D, --force-delete                  Forcefully deletes the local branch, regradless of its merged status.
```




  