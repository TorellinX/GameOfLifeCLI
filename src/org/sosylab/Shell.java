package org.sosylab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.sosylab.model.Game;
import org.sosylab.model.Grid;

/**
 * This class provides the utility to let a user play the Game of Life application interactively on
 * a shell.
 */
class Shell {

  private static final String PROMPT = "gol> ";
  private static final String ERROR = "Error! ";

  private static final String HELP = """
      Game of Life - possible commands:
      alive i j    set cell in row i and column j alive
      clear        kill all cells and reset generations
      dead i j     kill cell in row i and column j
      generate     compute next generation
      help         print this help
      new x y      start new game with dimensions x times y
      print        print the gameboard
      quit         quit the program
      resize x y   resize current game to dimensions x times y
      shape name   load initial population""";

  private Grid game;

  /**
   * The main loop that handles the shell interaction. It takes commands from the user and executes
   * them.
   *
   * @throws IOException thrown when reading from stdin fails
   */
  void run() throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    execute(stdin);
  }

  private void execute(BufferedReader stdin) throws IOException {
    while (true) {
      System.out.print(PROMPT);
      String input = stdin.readLine();

      if (input == null) {
        break;
      }

      String[] tokens = input.trim().split("\\s+");
      String command = tokens[0].toUpperCase();
      char commandChar = command.charAt(0);

      switch (commandChar) {
        case 'N':
          handleCommandNew(tokens);
          break;
        case 'P':
          handleCommandPrint(tokens);
          break;
        case 'C':
          handleCommandClear(tokens);
          break;
        case 'H':
          System.out.println(HELP);
          break;
        case 'Q':
          return;
      }

    }
  }

  private void handleCommandNew(String[] tokens){
    if(tokens.length > 3) {
      System.out.println(ERROR + "Too many arguments for command \"NEW\"");
      return;
    }
    if(tokens.length < 3) {
      System.out.println(ERROR + "Missing argument(s) for command \"NEW\"");
      return;
    }

    int rows;
    int cols;
    try {
      rows = Integer.parseInt(tokens[1]);
      cols = Integer.parseInt(tokens[2]);
    }
    catch (NumberFormatException e) {
      System.out.println(ERROR + "Arguments of the \"NEW\" command must be numbers!");
      return;
    }
    if(rows <=0 || cols <= 0) {
      System.out.println(ERROR + "Number of rows or columns must be greater than 0!");
      return;
    }

    game = new Game(rows, cols);
    System.out.println(game.toString()); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
  }

  private void handleCommandPrint(String[] tokens){
    if(tokens.length > 1) {
      System.out.println(ERROR + "Too many arguments for command \"PRINT\"");
      return;
    }

    if(game == null) {
      System.out.println(ERROR + "No active game!");
      return;
    }
    System.out.println(game.toString());
  }

  private void handleCommandClear(String[] tokens){
    if(tokens.length > 1) {
      System.out.println(ERROR + "Too many arguments for command \"CLEAR\"");
      return;
    }

    if(game == null) {
      System.out.println(ERROR + "No active game!");
      return;
    }

    game.clear();
  }
}

