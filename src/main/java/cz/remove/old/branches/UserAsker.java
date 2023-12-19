package cz.remove.old.branches;

import java.util.Scanner;

public class UserAsker {

    static String askForSquashMessage() {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter message for squash commit:");

        String squashMessage = scanner.nextLine();  // Read user input
        System.out.println("Message for squash commit is: " + squashMessage);  // Output user input
        return squashMessage;
    }

    static boolean askWhetherToContinue(String question) {
        Scanner kbd = new Scanner(System.in);
        String decision;
        while (true) {
            System.out.println(question + " (y/n)");
            decision = kbd.nextLine();
            switch (decision) {
                case "y":
                    return true;
                default:
                    return false;
            }
        }
    }

}
