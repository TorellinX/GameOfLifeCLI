package org.sosylab;

import java.io.IOException;
import org.sosylab.model.Grid;

/**
 * This class provides the utility to let a user play the Game of Life application interactively on
 * a shell.
 */
class Shell {

  private static final String PROMPT = "gol";
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
    // TODO: add code here
  }

  // TODO: add code here

}

