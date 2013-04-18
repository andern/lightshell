package test;

import lightshell.Shell;
import test.commands.Conditions;
import test.commands.Exit;
import test.commands.Warranty;

public class Main {
    public static void main(String[] args) {

        Conditions cmd1 = new Conditions();
        Warranty cmd2 = new Warranty();
        Exit cmd3 = new Exit();

        Shell shell = new Shell();
        shell.addCommand(cmd1);
        shell.addCommand(cmd2);
        shell.addCommand(cmd3);

        shell.setWelcomeMsg("Welcome to a lightshell test.");
        shell.setPrompt("lightshell");
        shell.run();
    }
}
