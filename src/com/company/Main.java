package com.company;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Terminal terminal = new Terminal("D:/"); // base directory
        Parser parser = new Parser(terminal);
        String input;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println();
            System.out.print(terminal.getPwd() + "$  "); // print the current directory path followed by $
            input = in.nextLine();
            parser.parse(input);
        } while (!input.equalsIgnoreCase("exit"));
    }
}
