package org.sosylab;

import static org.sosylab.model.Shapes.getAvailableShapes;
import static org.sosylab.model.Shapes.getShapeByName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.sosylab.model.Game;
import org.sosylab.model.Grid;
import org.sosylab.model.Shape;

/**
 * This class provides the utility to let a user play the Game of Life application interactively on
 * a shell.
 */
class Shell {

  private static final String PROMPT = "gol> ";
  private static final String ERROR = "Error! ";

  private static final String HELP = """
      Game of Life - possible commands:
      alive i j    set cell in column i and row j alive
      clear        kill all cells and reset generations
      dead i j     kill cell in column i and row j
      generate     compute next generation
      help         print this help
      new x y      start a new game with dimensions x times y
      print        print the gameboard
      quit         quit the program
      resize x y   resize current game to dimensions x times y
      shape name   load initial population""";

  private Grid game;

  /**
   * Launch the user interaction.
   *
   * @throws IOException thrown when reading from stdin fails
   */
  void run() throws IOException {
    BufferedReader stdin = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    execute(stdin);

  }

  /**
   * The main loop that handles the shell interaction. It takes commands from the user and executes
   * them.
   *
   * @param stdin the reader for a user input.
   * @throws IOException thrown when reading from stdin fails
   */
  private void execute(BufferedReader stdin) throws IOException {
    while (true) {
      System.out.print(PROMPT);
      String input = stdin.readLine();

      if (input == null) {
        break;
      }

      if (input.equals("")) {
        displayError("No command given");
        continue;
      }

      String[] tokens = input.trim().split("\\s+");

      Command command = parseCommand(tokens[0]);

      switch (command) {
        case NEW:
          handleCommandNew(tokens);
          break;
        case ALIVE:
          handleCommandAlive(tokens);
          break;
        case DEAD:
          handleCommandDead(tokens);
          break;
        case GENERATE:
          handleCommandGenerate(tokens);
          break;
        case PRINT:
          handleCommandPrint(tokens);
          break;
        case CLEAR:
          handleCommandClear(tokens);
          break;
        case RESIZE:
          handleCommandResize(tokens);
          break;
        case SHAPE:
          handleCommandShape(tokens);
          break;
        case HELP:
          System.out.println(HELP);
          break;
        case QUIT:
          if (checkCommandQuitTokens(tokens)) {
            return;
          }
          break;
        case UNKNOWN:
          displayError("Command not found");
          break;
        default:
          throw new AssertionError("Unhandled command: " + command);
      }
    }
  }

  /**
   * Parse a string-token and check whether it matches a {@link Command}. If successful, the
   * respective command gets returned, {@link Command#UNKNOWN} otherwise.
   *
   * @param token the string-token to be checked.
   * @return {@link Command} containing either the matched token or {@link Command#UNKNOWN}
   * otherwise.
   */
  private Command parseCommand(String token) {
    Command result = Command.UNKNOWN;

    String normalizedToken = token.toUpperCase();

    for (Command command : Command.values()) {
      if (command.getName().startsWith(normalizedToken)) {
        result = command;
        break;
      }
    }

    return result;
  }

  /**
   * Creates a new instance of the Game-application.
   *
   * @param tokens The passed arguments from the user.
   */
  private void handleCommandNew(String[] tokens) {
    if (tokens.length > 3) {
      displayError("Too many arguments for command \"NEW\"");
      return;
    }
    if (tokens.length < 3) {
      displayError("Missing argument(s) for command \"NEW\"");
      return;
    }

    int cols;
    int rows;
    try {
      cols = Integer.parseInt(tokens[1]);
      rows = Integer.parseInt(tokens[2]);
    } catch (NumberFormatException e) {
      displayError("Arguments of the \"NEW\" command must be numbers!");
      return;
    }
    if (cols <= 0 || rows <= 0) {
      displayError("Number of rows or columns must be greater than 0!");
      return;
    }

    game = new Game(cols, rows);
  }


  /**
   * Lets a user to set a selected cell on the field to the alive state.
   *
   * @param tokens The tokens to be checked.
   */
  private void handleCommandAlive(String[] tokens) {
    if (tokens.length > 3) {
      displayError("Too many arguments for command \"ALIVE\"");
      return;
    }
    if (tokens.length < 3) {
      displayError("Missing argument(s) for command \"ALIVE\"");
      return;
    }
    if (game == null) {
      displayError("No active game!");
      return;
    }

    int col;
    int row;
    try {
      col = Integer.parseInt(tokens[1]);
      row = Integer.parseInt(tokens[2]);
    } catch (NumberFormatException e) {
      displayError("Arguments of the \"ALIVE\" command must be numbers!");
      return;
    }

    if (col < 0 || row < 0 || col >= game.getColumns() || row >= game.getRows()) {
      displayError(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
      return;
    }
    game.setCellAlive(col, row);
  }

  /**
   * Lets a user to set a selected cell on the field to the dead state.
   *
   * @param tokens The tokens to be checked.
   */
  private void handleCommandDead(String[] tokens) {
    if (tokens.length > 3) {
      displayError("Too many arguments for command \"DEAD\"");
      return;
    }
    if (tokens.length < 3) {
      displayError("Missing argument(s) for command \"DEAD\"");
      return;
    }
    if (game == null) {
      displayError("No active game!");
      return;
    }

    int col;
    int row;
    try {
      col = Integer.parseInt(tokens[1]);
      row = Integer.parseInt(tokens[2]);
    } catch (NumberFormatException e) {
      displayError("Arguments of the \"DEAD\" command must be numbers!");
      return;
    }

    if (col < 0 || row < 0 || col >= game.getColumns() || row >= game.getRows()) {
      displayError(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
      return;
    }
    game.setCellDead(col, row);
  }

  /**
   * Lets a user to switch the game to the next generation.
   *
   * @param tokens The tokens to be checked. This method requires them to be empty.
   */
  private void handleCommandGenerate(String[] tokens) {
    if (tokens.length > 1) {
      displayError("Too many arguments for command \"GENERATE\"");
      return;
    }

    if (game == null) {
      displayError("No active game!");
      return;
    }
    game.next();
    System.out.println("Generation: " + game.getGenerations());
  }

  /**
   * Prints the current state of a running game field.
   *
   * @param tokens The tokens to be checked. This method requires them to be empty.
   */
  private void handleCommandPrint(String[] tokens) {
    if (tokens.length > 1) {
      displayError("Too many arguments for command \"PRINT\"");
      return;
    }

    if (game == null) {
      displayError("No active game!");
      return;
    }
    System.out.println(game);
  }

  /**
   * Sets the game field to default empty state.
   *
   * @param tokens The tokens to be checked. This method requires them to be empty.
   */
  private void handleCommandClear(String[] tokens) {
    if (tokens.length > 1) {
      displayError("Too many arguments for command \"CLEAR\"");
      return;
    }
    if (game == null) {
      displayError("No active game!");
      return;
    }
    game.clear();
  }

  /**
   * Lets a user to resize the game field.
   *
   * @param tokens The tokens to be checked.
   */
  private void handleCommandResize(String[] tokens) {
    if (tokens.length > 3) {
      displayError("Too many arguments for command \"RESIZE\"");
      return;
    }
    if (tokens.length < 3) {
      displayError("Missing argument(s) for command \"RESIZE\"");
      return;
    }
    if (game == null) {
      displayError("No active game!");
      return;
    }

    int cols;
    int rows;
    try {
      cols = Integer.parseInt(tokens[1]);
      rows = Integer.parseInt(tokens[2]);
    } catch (NumberFormatException e) {
      displayError("Arguments of the \"RESIZE\" command must be numbers!");
      return;
    }
    if (cols <= 0 || rows <= 0) {
      displayError("Number of rows or columns must be greater than 0!");
      return;
    }

    game.resize(cols, rows);
  }

  /**
   * Lets a user to add a selected shape to the field. The field will be cleared and the selected
   * shape will be placed in the middle of the game field, if the shape fits on the field.
   *
   * @param tokens The tokens to be checked.
   */
  private void handleCommandShape(String[] tokens) {
    if (game == null) {
      displayError("No active game!");
      return;
    }
    if (tokens.length > 2) {
      displayError("Too many arguments for command \"NEW\"");
      return;
    }
    if (tokens.length < 2) {
      displayError("Missing argument(s) for command \"NEW\"");
      return;
    }
    String shapeName = tokens[1].toLowerCase();
    Shape shape = getShapeByName(shapeName);

    if (shape == null) {
      displayError("Unknown shape: " + shapeName + ".");
      System.out.println("Available Shapes are: \n" + getAvailableShapes());
      return;
    }
    if (shape.getColumns() > game.getColumns() || shape.getRows() < shape.getRows()) {
      displayError(
          "The size of the shape may not exceed the size of the game field. \nThe shape \""
              + shapeName.substring(0, 1).toUpperCase() + shapeName.substring(1)
              + "\" requires min. " + shape.getColumns() + "x" + shape.getRows() + " field.");
      return;
    }
    game = new Game(game.getColumns(), game.getRows(), shape);
  }

  /**
   * Checks the quit command for its tokens. Returns an error message if they are not in accordance
   * to the rules.
   *
   * @param tokens The tokens to be checked. This method requires them to be empty.
   */
  private boolean checkCommandQuitTokens(String[] tokens) {
    if (tokens.length > 1) {
      displayError("Too many arguments for command \"QUIT\"");
      return false;
    }
    return true;
  }

  /**
   * Print an error-message in the required format to stdout.
   *
   * @param message The error message.
   */
  private void displayError(String message) {
    System.out.println(ERROR + message);
  }

  /**
   * The commands available to the user on a shell.
   */
  private enum Command {
    NEW("NEW"), ALIVE("ALIVE"), DEAD("DEAD"), GENERATE("GENERATE"), PRINT("PRINT"),
    CLEAR("CLEAR"), RESIZE("RESIZE"), SHAPE("SHAPE"), HELP("HELP"), QUIT("QUIT"), UNKNOWN;

    private final String name;

    Command() {
      name = ""; // optional assignment, to make the empty assignment explicit to the reader
    }

    Command(String cmd) {
      name = cmd;
    }

    public String getName() {
      return name;
    }
  }
}

