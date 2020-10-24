package com.journaldev.readfileslinebyline;

import java.io.*;
import java.util.Arrays;

public class Parser {
  String file;
  String curCommand = null;
  BufferedReader reader;

  public Parser(String file) {
    this.file = file;
  }

  enum commandTypeTranslator {
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABLE,
    C_GOTO,
    C_IF,
    C_FUNCTION,
    C_RETURN,
    C_CALL,
    C_NULL,
  }

  String[] cArithmetic = {
    "add",
    "sub",
    "neg",
    "eq",
    "gt",
    "lt",
    "and",
    "or",
    "not",
  };

  public boolean hasMoreCommands() {
    try {
      this.curCommand = this.reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (this.curCommand != null) {
      return true;
    } else {
      return false;
    }
  }

  public String advance() {
    if (this.curCommand.startsWith("//")) {
      return null;
    }

    if (this.curCommand.equals("")) {
      return null;
    }

    return this.curCommand;
  }

  public commandTypeTranslator commandType(String command) {
    String firstCommand = command.split(" ")[0];

    if (firstCommand.equals("push")) {
      return commandTypeTranslator.C_PUSH;
    }

    if (firstCommand.equals("pop")) {
      return commandTypeTranslator.C_POP;
    }

    if (Arrays.asList(this.cArithmetic).contains(firstCommand)) {
      return commandTypeTranslator.C_ARITHMETIC;
    }

    if (firstCommand.equals("label")) {
      return commandTypeTranslator.C_LABLE;
    }

    if (firstCommand.equals("goto")) {
      return commandTypeTranslator.C_GOTO;
    }

    if (firstCommand.equals("if")) {
      return commandTypeTranslator.C_IF;
    }

    if (firstCommand.equals("function")) {
      return commandTypeTranslator.C_FUNCTION;
    }

    if (firstCommand.equals("return")) {
      return commandTypeTranslator.C_RETURN;
    }

    if (firstCommand.equals("call")) {
      return commandTypeTranslator.C_CALL;
    }

    return commandTypeTranslator.C_NULL;
  }

  public String arg1(String command, commandTypeTranslator commandType) {
    if (commandType == commandTypeTranslator.C_RETURN) {
      return null;
    }

    if (commandType == commandTypeTranslator.C_ARITHMETIC) {
      String firstItem = command.split(" ")[0];
      return firstItem;
    }

    String secondItem = command.split(" ")[1];
    return secondItem;
  }

  public String arg2(String command, commandTypeTranslator commandType) {
    if (
      commandType == commandTypeTranslator.C_PUSH ||
      commandType == commandTypeTranslator.C_POP ||
      commandType == commandTypeTranslator.C_FUNCTION ||
      commandType == commandTypeTranslator.C_CALL
    ) {
      String thirdCommand = command.split(" ")[2];
      return thirdCommand;
    }

    return null;
  }

  public static void readToBuffer(StringBuffer buffer, String filePath)
    throws IOException {
    InputStream is = new FileInputStream(filePath);
    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine();
    while (line != null) {
      if (!(line.startsWith("//") || line.equals(""))) {
        buffer.append(line);
        buffer.append("\n");
      }
      line = reader.readLine();
    }
    reader.close();
    is.close();
  }

  public void readFile() {
    try {
      StringBuffer sb = new StringBuffer();
      readToBuffer(sb, this.file);
      Reader inputString = new StringReader(sb.toString());
      this.reader = new BufferedReader(inputString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Parser myParser = new Parser("../MemoryAccess/BasicTest/BasicTest.vm");
    myParser.readFile();
    while (myParser.hasMoreCommands()) {
      String curCommand = myParser.advance();
      String readCommand = myParser.advance();
      System.out.println(readCommand);
      commandTypeTranslator commandType = myParser.commandType(readCommand);
      System.out.println(commandType);
      String arg1 = myParser.arg1(readCommand, commandType);
      if (arg1 != null) {
        System.out.println(arg1);
      }
      String arg2 = myParser.arg2(readCommand, commandType);
      if (arg2 != null) {
        System.out.println(arg2);
      }
    }
  }
}