package cz.remove.old.branches;

import java.util.Scanner;

public class UserAsker {

    static boolean askWhetherToContinue(String question) {
        Scanner kbd = new Scanner(System.in);
        String decision;
        while (true) {
            System.out.println(question + " (y/n, CTRL+C to exit)");
            decision = kbd.nextLine();
            switch (decision) {
                case "y":
                    return true;
                default:
                    System.out.println("You have not confirmed by typing letter \"y\".");
                    return false;
            }
        }
    }

}
