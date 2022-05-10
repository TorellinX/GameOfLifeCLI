package org.sosylab.model;

import java.util.Collection;

/**
 * Interface between GameBoard and Game. More accurately, for Game of Life this is an
 * interface between the grid of cells and the user interface.
 */
public interface Grid {

  /**
   * Gets the status of a cell (alive or dead).
   *
   * @param row x-position.
   * @param col y-position.
   * @return <code>true</code> if the cell is alive, <code>false</code> otherwise.
   */
  boolean isCellAlive(int row, int col);

  /**
   * Sets a cell alive.
   *
   * @param row x-position.
   * @param col y-position.
   */
  void setCellAlive(int row, int col);

  /**
   * Puts a cell into a dead state.
   *
   * @param row x-position.
   * @param col y-position.
   */
  void setCellDead(int row, int col);

  /**
   * Resizes the cell grid in x and y direction.
   *
   * @param rows New number of rows.
   * @param cols New number of columns.
   */
  void resize(int rows, int cols);

  /**
   * Gets the dimension of the cell grid in y direction.
   *
   * @return Number of rows.
   */
  int getRows();

  /**
   * Gets the dimension of the cell grid in x direction.
   *
   * @return Number of columns.
   */
  int getColumns();

  /**
   * Gets all living cells.
   *
   * @return Set of all cells which are alive.
   */
  Collection<Cell> getPopulation();

  /**
   * Clears the grid.
   */
  void clear();

  /**
   * Computes the next generation.
   */
  void next();

  /**
   * Gets the number of generations in this game.
   *
   * @return The current generation.
   */
  int getGenerations();

  /**
   * Gets the string representation of the current game state.
   *
   * @return The matrix as string.
   */
  String toString();

}
