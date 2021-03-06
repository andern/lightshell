/*
 * Copyright (C) 2012, 2013 Andreas Halle
 *
 * This file is part of lightshell.
 *
 * lightshell is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * lightshell is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public license
 * along with lightshell. If not, see <http://www.gnu.org/licenses/>.
 */
package lightshell;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Shell implementation.
 * 
 * @author  Andreas Halle
 * @version 0.1.0
 * @see     lightshell.Command
 */
public class Shell {
    private String welcome;
    private String prompt = "CHANGEPROMPT";
    private Set<Command> cmds = new LinkedHashSet<Command>();



    /**
     * Cut a {@code String} into several strings. Each of the new strings will
     * all be shorter than {@code maxlen} characters. Newlines in the output
     * are taken into account.
     *
     * @param  s
     *         Input {@code String}. Can contain newlines.
     * @param  maxlen
     *         All output strings will contain less characters than this.
     * @return an {@code Array} of strings with a given max length.
     */
    public static String[] cut(String s, int maxlen) {
        String endl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        String delim = "";
        String[] lines = s.split(endl);
        for (String line : lines) {
            sb.append(delim).append(cutLine(line, maxlen));
            delim = endl;
        }

        return sb.toString().split(endl);
    }



    /**
     * Cut a {@code String} into several lines that all have less characters
     * than the given max length. Each line is separated with the OS's line
     * separator. Input {@code String} should not contain newlines.
     *
     * @param  s
     *         Input {@code String}. Can not contain newlines.
     * @param  maxlen
     *         All lines in the output contain less characters than this.
     * @return a {@code String} with lines with a given max length.
     */
    public static String cutLine(String s, int maxlen) {
        String endl = System.getProperty("line.separator");
        int len = 0;

        StringBuilder sb = new StringBuilder();
        String delim = "";
        String[] words = s.split(" ");
        for (String word : words) {
            int wlen = word.length() + delim.length();
            len += wlen;

            if (len > maxlen) {
                len = wlen;
                sb.append(endl);
                delim = "";
            }

            sb.append(delim).append(word);
            delim = " ";
        }
        return sb.toString();
    }



    /**
     * Indent a {@code String} in such a way that it has a title and a
     * corresponding description indented to separate the title and its
     * description. An example:
     * <p>
     * The output looks something like this:
     * <blockquote><pre>
     * title        Here is a description of the
     *              title. In this description,
     *              each line has less characters
     *              than the given max length
     *              (indentation included).
     * </pre></blockquote>
     *
     * @param  title
     *         A {@code String} containing a title.
     * @param  desc
     *         A {@code String} containing a title's description.
     * @param  indentLen
     *         Indent the beginning of each description line with this many
     *         spaces.
     * @param  maxlen
     *         The maximum amount of characters on each line in the description
     *         (including indentation).
     * @return
     *         a nicely formatted {@code String} with a title and description.
     */
    public static String indent(String title, String desc, int indentLen,
                                int maxlen) {
        return indent(title, cut(desc, maxlen), indentLen);
    }



    /**
     * Add one of your implemented commands. When commands are added here
     * they will show up in the help menu and they can also be executed
     * from the shell prompt.
     *
     * @param cmd Implemented object that extends {@code Command}.
     */
    public void addCommand(Command cmd) {
        cmds.add(cmd);
    }



    /**
     * Return the shell's current prompt.
     *
     * @return current prompt.
     */
    public String getPrompt() { return prompt; }


    /**
     * Return the shell's current welcome message.
     *
     * @return Current welcome message.
     */
    public String getWelcomeMsg() { return welcome; }



    /**
     * Parse a string (should come from console or gui-console), do what the
     * input says the user wants to do and output some result.
     *
     * @param  str
     *         {@code String} to parse. Hopefully contains some command.
     * @return
     *         some (hopefully) informative {@code String}.
     */
    public String parse(String input) {
        String str = input.trim().replaceAll("\\s+", " ");//Remove extra spaces
        if (str.matches("^help.*")) return help(input);

        Command res = resolve(str);

        String e = "Unknown command. Type 'help' for a list of commands.";
        if (res == null) return e;

        return res.execute();
    }

    public void run() {
        Scanner s = new Scanner(System.in);
        System.out.println(welcome);
        for (;;) {
            System.out.format("%s> ", prompt);
            String strcmd = s.nextLine();

            String res = parse(strcmd);
            if (res != null) System.out.println(res);
        }
    }



    /**
     * Set the prompt of the shell. That is, change the text that appears
     * before the > sign in the input area.
     *
     * @param prompt
     *        Change the current prompt to this prompt.
     */
    public void setPrompt(String prompt) { this.prompt = prompt; }



    /**
     * The message set here is shown when the shell's run()-command is
     * executed.
     *
     * @param msg
     *        Change the current welcome message to this message.
     */
    public void setWelcomeMsg(String msg) { welcome = msg; }



    /*
     * Find a Command in a Set of commands from a String. Return null if no
     * matching commands where found.
     */
    private Command commandFromString(String strCmd, Set<Command> cmds) {
        for (Command c : cmds) {
            Set<String> alias = c.getAliases();
            String name = c.getName();
            if (name == null) continue;
            if ((alias!=null && alias.contains(strCmd)) || name.equals(strCmd))
                return c;
        }
        return null;
    }

    private Command commandFromString(String strCmd) {
        return commandFromString(strCmd, cmds);
    }


    private Command resolve(Command cmd) {
        String arg = cmd.getArg();
        if (arg == null) return cmd;

        String[] args = arg.split(" ");

        Set<Command> subcmds = cmd.getSubCommands();
        if (subcmds == null) return cmd;

        Command subcmd = commandFromString(args[0], subcmds);
        if (subcmd == null) return cmd;

        int idx = arg.indexOf(" ");
        if (idx == -1) subcmd.setArg(null);
        else subcmd.setArg(arg.substring(idx+1));

        return resolve(subcmd);
    }

    private Command resolve(String arg) {
        int idx = arg.indexOf(" ");
        String newArg = (idx == -1) ? null : arg.substring(idx+1);
        String strCmd = (idx == -1) ? arg : arg.substring(0, idx);

        Command cmd = commandFromString(strCmd);
        if (cmd == null) return null;

        cmd.setArg(newArg);
        return resolve(cmd);
    }



    /* Output a list of available commands with its short help. */
    private String generateCommandList() {
        StringBuffer sb = new StringBuffer();
        String delim = "";
        for (Command c : cmds) {
            sb.append(delim);
            sb.append(indent(c.getName(), c.getShortHelp(), 20, 47));
            delim = System.getProperty("line.separator");
        }
        return sb.toString();
    }



    private String help(String arg) {
        int idx = arg.indexOf(" ");
        if (idx == -1) return generateCommandList();

        String strCmd = (idx == -1) ? arg : arg.substring(idx+1);
        Command cmd = resolve(strCmd);
        if (cmd == null) return "help: Unknown command.";

        return cmd.getHelp();
    }


    private static String indent(String title, String[] lines, int indentLen) {
        StringBuilder sb = new StringBuilder();
        String format = String.format("%%-%ds %%s", indentLen);

        String s = title;
        String delim = "";
        for (String line : lines) {
            sb.append(delim);
            sb.append(String.format(format, s, line));
            s = "";
            delim = System.getProperty("line.separator");
        }
        return sb.toString();
    }
}
