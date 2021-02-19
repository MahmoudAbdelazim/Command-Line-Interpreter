package com.company;

import java.io.*;
import java.time.*;
import java.util.*;

public class Terminal {

    private String pwd; // current directory location
    private String base; // base location

    public Terminal(String base) {
        this.base = base;
        pwd = base;
    }

    public void cd(String path) {
        String newPwd = pwd;
        if (path.length() > 1 && path.substring(0, 2).equals("~/")) { // if the path starts with ~/ then it's from the base path
            newPwd = base + path.substring(2, path.length());
        } else if (path.length() > 2 && (path.substring(0, 3).equals("D:/")
                || path.substring(0, 3).equals("C:/"))) { // then it's an absolute path
            newPwd = path;
        } else {
            newPwd += path;
        }
        newPwd += '/';
        File dir = new File(newPwd);
        if (dir.isDirectory()) { // check that the directory exists
            pwd = newPwd;
        } else {
            System.out.println("No such directory: " + path);
        }
        fixPwd();
    }

    private void fixPwd() { // fixes paths with back commands (..)
        String[] parts = pwd.split("/");
        ArrayList<String> newParts = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("..")) { // if there's a .. then remove the last part
                if (!newParts.isEmpty())
                    newParts.remove(newParts.size() - 1);
            } else {
                newParts.add(parts[i]);
            }
        }
        pwd = "";
        for (String part : newParts) {
            pwd += part + '/';
        }
        if (pwd.isEmpty()) pwd = base;
    }

    public void ls() {
        File dir = new File(pwd);

        File childs[] = dir.listFiles();
        if (childs != null) {
            for (File c : childs) {
                String file = c.getName();
                if (c.isDirectory()) System.out.print(file + "/  "); // print / character after directories' names
                else System.out.print(file + "  ");
            }
        }
        System.out.println();
    }

    public void ls(String redirection, String file) {
        PrintWriter output = null;
        try {
            if (redirection.equals(">")) {
                output = new PrintWriter(new FileWriter(pwd + file));
            } else if (redirection.equals(">>")) {
                output = new PrintWriter(new FileWriter(pwd + file, true));
            }
            if (output != null) {
                File dir = new File(pwd);
                File childs[] = dir.listFiles();
                if (childs != null) {
                    for (File c : childs) {
                        String f = c.getName();
                        if (c.isDirectory()) output.print(f + "/  "); // print / character after directories' names
                        else output.print(f + "  ");
                    }
                    output.println();
                }
                output.close();
            }
        } catch (IOException e) {
            System.out.println("No such file: " + file);
        }
    }

    public void cp(String sourcePath, String destinationPath) {
        try {
            File src = new File(pwd + sourcePath);
            if (!src.isFile()) { // check that the file exists
                System.out.println("No such file: " + sourcePath);
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(pwd + sourcePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(pwd + destinationPath));
            String line;
            while ((line = reader.readLine()) != null) { // copy the file line by line
                writer.append(line);
                writer.append("\n");
            }
            reader.close();
            writer.close();

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Couldn't copy specified file");
        } catch (IOException e) {
            System.out.println("Couldn't copy specified file");
        }
    }

    public void mv(String sourcePath, String destinationPath) {
        try {
            File src = new File(pwd + sourcePath);
            if (!src.isFile()) { // check that the file exists
                System.out.println("No such file: " + sourcePath);
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(pwd + sourcePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(pwd + destinationPath));
            String line;
            while ((line = reader.readLine()) != null) { // copy the file line by line
                writer.append(line);
                writer.append("\n");
            }
            reader.close();
            writer.close();
            src = new File(pwd + sourcePath);
            src.delete(); // delete the source file after finishing

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Couldn't move specified file");
        } catch (IOException e) {
            System.out.println("Couldn't move specified file");
        }
    }

    public void more(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pwd + path));
            String line;
            while ((line = reader.readLine()) != null) { // read the contents of the file line by line
                System.out.println(line);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("No such file: " + path);
        } catch (IOException e) {
            System.out.println("Couldn't read file: " + path);
        }
    }

    public void more(String path, String redirection, String file) {
        PrintWriter output = null;
        try {
            if (redirection.equals(">")) {
                output = new PrintWriter(new FileWriter(pwd + file));
            } else if (redirection.equals(">>")) {
                output = new PrintWriter(new FileWriter(pwd + file, true));
            }
        } catch (IOException ioException) {
            System.out.println("No such file: " + file);
        }
        if (output != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(pwd + path));
                String line;
                while ((line = reader.readLine()) != null) { // read the contents of the file line by line
                    output.println(line);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No such file: " + path);
            } catch (IOException e) {
                System.out.println("Couldn't read file: " + path);
            }
            output.close();
        }
    }

    public void cat(String[] paths) {
        for (int i = 0; i < paths.length; i++) { // read the contents of all files provided
            try {
                BufferedReader reader = new BufferedReader(new FileReader(pwd + paths[i]));
                String line;
                while ((line = reader.readLine()) != null) { // read the contents of the file line by line
                    System.out.println(line);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No such file: " + paths[i]);
            } catch (IOException e) {
                System.out.println("Couldn't read file: " + paths[i]);
            }
        }
    }

    public void mkdir(String dir) {
        String newDir = pwd + dir;
        File path = new File(newDir);
        if (!path.mkdir()) {
            System.out.println("couldn't create specified directory: " + dir);
        }
    }

    public void rmdir(String dir) {
        dir = pwd + dir;
        File path = new File(dir);
        if (!path.delete()) {
            System.out.println("No such directory: " + dir);
        }
    }

    public void rm(String path) {
        path = pwd + path;
        File file = new File(path);
        if (!file.delete()) {
            System.out.println("No such file: " + path);
        }
    }

    public void pwd() {
        System.out.println(pwd); // print the current directory path
    }

    public void clear() {
        try {
            Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
            System.out.println("Couldn't clear terminal");
        }
    }

    public void date() {
        System.out.println(LocalDateTime.now()); // print the current date and time
    }

    public void args(String cmd) {
        // prints the list of arguments for any command

        if (cmd.equalsIgnoreCase("cd")) {
            System.out.println("arg1: DirectoryPath");
        } else if (cmd.equalsIgnoreCase("ls")) {
            System.out.println("no args");
        } else if (cmd.equalsIgnoreCase("cp")) {
            System.out.println("arg1: SourcePath, arg2: DestinationPath");
        } else if (cmd.equalsIgnoreCase("cat")) {
            System.out.println("arg1: File1Path, arg2: File2Path, arg3: File3Path, ...");
        } else if (cmd.equalsIgnoreCase("more")) {
            System.out.println("arg1: FilePath");
        } else if (cmd.equalsIgnoreCase("mkdir")) {
            System.out.println("arg1: DirectoryPath");
        } else if (cmd.equalsIgnoreCase("rmdir")) {
            System.out.println("arg1: DirectoryPath");
        } else if (cmd.equalsIgnoreCase("rm")) {
            System.out.println("arg1: FilePath");
        } else if (cmd.equalsIgnoreCase("mv")) {
            System.out.println("arg1: SourcePath, arg2: DestinationPath");
        } else if (cmd.equalsIgnoreCase("args")) {
            System.out.println("arg1: Command");
        } else if (cmd.equalsIgnoreCase("date")) {
            System.out.println("no args");
        } else if (cmd.equalsIgnoreCase("help")) {
            System.out.println("no args");
        } else if (cmd.equalsIgnoreCase("pwd")) {
            System.out.println("no args");
        } else if (cmd.equalsIgnoreCase("clear")) {
            System.out.println("no args");
        }
    }

    public void help() {
        // prints all commands available and their usages

        System.out.println("args: List all command arguments" +
                "\ndate: Current date/time" +
                "\ncd: Change current directory" +
                "\nls: show the contents of the current directory" +
                "\ncp: copy a source file to a destination file" +
                "\nmv: move a source file to a destination file" +
                "\ncat: show the content of a file/files" +
                "\nmore: show to content of a file" +
                "\nmkdir: create a new directory" +
                "\nrmdir: remove a directory" +
                "\nrm: remove a file" +
                "\nhelp: show a list of commands and their usages" +
                "\npwd: show the path of the current directory" +
                "\nclear: clear the terminal screen" +
                "\nexit: Stop all");
    }

    public String getPwd() {
        return pwd;
    }
}
