# Remove Old Branches

Remove Old Branches is a command-line tool that simplifies the process of cleaning up a git repository from outdated
local branches.
The tool checks all the local branches one by one and deletes the current branch if it is too old. By default, it
only deletes branches with "feature" prefix and from a different author. Compatible with both Windows and Linux
operating systems. It is written in Java and uses Maven for build automation.

## Build

Navigate to the root directory of the project and run the following command:

```
mvn clean install
```

The runnable jar will be created in the `target` directory:

```
.\target\RemoveOldBranches-1.0-RELEASE-jar-with-dependencies.jar
```

## Installation

### Windows

Run **InstallToPathVarOnWindows.bat** to add the RemoveOldBranches directory with runnable bat file to your PATH
variable. This will allow you to run RemoveOldBranches from any directory. You might need to restart your computer.

### Linux

Navigate to the root directory of the project and run the following command:

```
alias RemoveOldBranches="java -jar $(pwd)/target/RemoveOldBranches-1.0-RELEASE-jar-with-dependencies.jar"
```

## Usage

Navigate to the root directory of your Git repository and run RemoveOldBranches:

```
cd C:\Path\To\Git\Repo
RemoveOldBranches
```

Use arguments to customize executed operations. For example to remove only branches older than 60 days and ignore the
author of the last commit use:

```
RemoveOldBranches -o 60 -l
```

The following options are available:

```
-h, --help                          Show help message
-l, --skip-last-commit-author       Skip last commit author check. By default only branches from other users are deleted.
-a, --do-not-ask                    Do not ask anything, just do it.
-o, --older-than-days <days>        Set how many days the branch must be old to be deleted (default: 30)
-p, --branch-name-prefix <prefix>   Set a name prefix to filter the branches (default: 'feature'). If set to '', all branches will be processed.
-D, --force-delete                  Forcefully deletes the local branch, regradless of its merged status.
```




  