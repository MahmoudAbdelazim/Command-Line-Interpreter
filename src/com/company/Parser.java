package com.company;

import java.util.ArrayList;

public class Parser {
    ArrayList<String> args;
    String cmd;
    Terminal terminal;

    public Parser(Terminal terminal) {
        this.terminal = terminal;
        args = new ArrayList<>();
    }

    public boolean parse(String input) {
        // cp "source file" "dest file"
        //split the input by spaces unless there are double quotes "" or pipeline character |
        args.clear();
        StringBuilder curStr = new StringBuilder("");
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '"') { // if there's a " then continue to the ending "
                for (int j = i + 1; j < input.length(); j++) {
                    if (input.charAt(j) != '"') {
                        curStr.append(input.charAt(j));
                    } else {
                        i = j;
                        break;
                    }
                }
            } else if (input.charAt(i) == ' ') { // if there's a space, then an argument is completed
                if (!curStr.toString().isEmpty())
                    args.add(curStr.toString());
                curStr = new StringBuilder("");
            } else if (input.charAt(i) == '|') { // if there's a pipeline, then execute the current command
                if (!curStr.toString().isEmpty())
                    args.add(curStr.toString());
                curStr = new StringBuilder("");
                boolean success = execute();
                if (!success) {
                    System.out.println("Invalid Args, " + cmd + " takes:");
                    terminal.args(cmd);
                }
                args.clear();
            } else { // else, then it's a normal character of the current argument
                curStr.append(input.charAt(i));
            }
        }
        if (!curStr.toString().isEmpty()) {
            args.add(curStr.toString());
            boolean success = execute();
            if (!success) {
                System.out.println("Invalid Args, " + cmd + " takes:");
                terminal.args(cmd);
            }
            args.clear();
            return success;
        }
        return true;
    }

    public boolean execute() {
        cmd = args.get(0); // the command is the first argument

        if (cmd.equalsIgnoreCase("cd")) {
            if (args.size() == 2) {
                terminal.cd(args.get(1));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("ls")) {
            if (args.size() == 1) {
                terminal.ls();
            } else if (args.size() == 3 && (args.get(1).equals(">") || args.get(1).equals(">>"))) {
                terminal.ls(args.get(1), args.get(2));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("cp")) {
            if (args.size() == 3) {
                terminal.cp(args.get(1), args.get(2));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("cat")) {
            if (args.size() > 1) {
                String[] paths = new String[args.size() - 1];
                for (int i = 1; i < args.size(); i++) {
                    paths[i - 1] = args.get(i);
                }
                terminal.cat(paths);
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("more")) {
            if (args.size() == 2) {
                terminal.more(args.get(1));
            } else if (args.size() == 4 && (args.get(2).equals(">") || args.get(2).equals(">>"))) {
                terminal.more(args.get(1), args.get(2), args.get(3));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("mkdir")) {
            if (args.size() == 2) {
                terminal.mkdir(args.get(1));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("rmdir")) {
            if (args.size() == 2) {
                terminal.rmdir(args.get(1));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("rm")) {
            if (args.size() == 2) {
                terminal.rm(args.get(1));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("mv")) {
            if (args.size() == 3) {
                terminal.mv(args.get(1), args.get(2));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("args")) {
            if (args.size() == 2) {
                terminal.args(args.get(1));
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("date")) {
            if (args.size() == 1) {
                terminal.date();
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("help")) {
            if (args.size() == 1) {
                terminal.help();
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("pwd")) {
            if (args.size() == 1) {
                terminal.pwd();
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("clear")) {
            if (args.size() == 1) {
                terminal.clear();
            } else {
                return false;
            }
        } else if (cmd.equalsIgnoreCase("exit")) {
            return true;
        } else {
            System.out.println("Invalid command: " + cmd);
        }
        return true;
    }

    public String getCmd() {
        return cmd;
    }

    public ArrayList<String> getArguments() {
        return args;
    }
}
