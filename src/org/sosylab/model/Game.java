package org.sosylab.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages a game.
 */
public class Game implements Grid {

  // Staying alive in this range
  private static final int STAY_ALIVE_MIN_NEIGHBORS = 2;
  private static final int STAY_ALIVE_MAX_NEIGHBORS = 3;

  // Condition for getting newly born
  private static final int NEWBORN_NEIGHBORS = 3;

  private int generation;
  private Cell[][] field;
  private final Map<Integer, List<Cell>> allNeighbors;
  private final Set<Cell> population;


  /**
   * Create a game of Game of Life.
   *
   * @param columns the number of columns in the new game field
   * @param rows    the number of rows in the new game field
   * @throws IllegalArgumentException if number of columns and rows is negative.
   */
  public Game(int columns, int rows) {
    if (columns <= 0 || rows <= 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    this.generation = 0;
    this.field = new Cell[rows][columns];
    this.allNeighbors = new HashMap<>();
    this.population = new HashSet<>();
    initializeFields();
  }

  /**
   * Create a game of Game of Life with the selected shape.
   *
   * @param columns the number of columns in the game field
   * @param rows    the number of rows in the game field
   * @param shape   the selected shpe
   */
  public Game(int columns, int rows, Shape shape) {
    this(columns, rows);
    placeShape(shape);
  }

  /**
   * Fills the empty instance-fields with a default values.
   */
  private void initializeFields() {
    for (int row = 0; row < field.length; row++) {
      for (int col = 0; col < field[0].length; col++) {
        field[row][col] = new Cell(col, row);
        allNeighbors.put(field[row][col].hashCode(), getNeighbors(field[row][col]));
        setCellDead(col, row);
      }
    }
  }

  /**
   * Places selected shape in the middle of the field.
   *
   * @param shape shape to be placed
   */
  public void placeShape(Shape shape) {
    int shapeColumns = shape.getColumns();
    int shapeRows = shape.getRows();
    int fieldColumns = this.getColumns();
    int fieldRows = this.getRows();
    int offsetRows = Math.floorDiv(fieldRows - shapeRows, 2);
    int offsetColumns = Math.floorDiv(fieldColumns - shapeColumns, 2);

    for (Cell cell : shape.getShapePopulation()) {
      this.population.add(new Cell(cell.getColumn() + offsetColumns, cell.getRow() + offsetRows));
    }
  }

  /**
   * Sets the counter of genrations to zero.
   */
  private void setGenerationToZero() {
    this.generation = 0;
  }

  /**
   * Get the cells that may change in the next generation.
   *
   * @return cells to recalculate
   */
  private Set<Cell> getCellsToRecalculate() {
    Set<Cell> cellsToRecalculate = new HashSet<>();
    for (Cell cell : population) {
      cellsToRecalculate.addAll(getNeighbors(cell));
    }
    cellsToRecalculate.addAll(population);
    return cellsToRecalculate;
  }

  /**
   * Calculates game state for the next generation.
   *
   * @param cellsToRecalculate cells that may change in the next generation
   */
  private void recalculateNext(Set<Cell> cellsToRecalculate) {
    Map<Integer, Integer> allAliveNeighbors = new HashMap<>();
    for (Cell cell : cellsToRecalculate) {
      int aliveNeighbors = countAliveNeighbors(cell);
      allAliveNeighbors.put(cell.hashCode(), aliveNeighbors);
    }
    for (Cell cell : cellsToRecalculate) {
      int aliveNeighbors = allAliveNeighbors.get(cell.hashCode());
      if (isCellAlive(cell.getColumn(), cell.getRow()) && (aliveNeighbors < STAY_ALIVE_MIN_NEIGHBORS
          || aliveNeighbors > STAY_ALIVE_MAX_NEIGHBORS)) {
        setCellDead(cell.getColumn(), cell.getRow());
      }
      if (!isCellAlive(cell.getColumn(), cell.getRow()) && aliveNeighbors == NEWBORN_NEIGHBORS) {
        setCellAlive(cell.getColumn(), cell.getRow());
      }
    }
  }

  /**
   * Count alive neighbors of the specified cell.
   *
   * @param cell the cell whose neighbors need to be counted
   * @throws IllegalArgumentException if number of columns and rows is negative or greater than the
   *                                  field size
   */
  private int countAliveNeighbors(Cell cell) throws IllegalArgumentException {
    if (cell.getColumn() >= this.getColumns() || cell.getRow() >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (cell.getColumn() < 0 || cell.getRow() < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    List<Cell> neighbors = allNeighbors.get(cell.hashCode());
    int aliveNeighborsCounter = 0;
    if (neighbors == null) {
      return aliveNeighborsCounter;
    }
    for (Cell neighbor : neighbors) {
      if (isCellAlive(neighbor.getColumn(), neighbor.getRow())) {
        aliveNeighborsCounter++;
      }
    }
    return aliveNeighborsCounter;
  }

  /**
   * Get neighbors of the specified cell.
   *
   * @param cell the cell whose neighbors need to be determined
   * @return neighbors of the specified cell
   * @throws IllegalArgumentException if number of columns and rows greater than the field size
   */
  private ArrayList<Cell> getNeighbors(Cell cell) throws IllegalArgumentException {
    int row = cell.getRow();
    int column = cell.getColumn();
    if (column >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    ArrayList<Cell> neighbors = new ArrayList<>();
    if (column != 0) {
      neighbors.add(new Cell(column - 1, row));
      if (row != 0) {
        neighbors.add(new Cell(column - 1, row - 1));
        //neighbors.add(new Cell(column, row - 1));
      }
      if (row != this.getRows() - 1) {
        neighbors.add(new Cell(column - 1, row + 1));
      }
    }
    if (column != this.getColumns() - 1) {
      neighbors.add(new Cell(column + 1, row));
      if (row != 0) {
        neighbors.add(new Cell(column + 1, row - 1));
      }
      if (row != this.getRows() - 1) {
        neighbors.add(new Cell(column + 1, row + 1));
      }
    }
    if (row != 0) {
      neighbors.add(new Cell(column, row - 1));
    }
    if (row != this.getRows() - 1) {
      neighbors.add(new Cell(column, row + 1));
    }
    return neighbors;
  }

  @Override
  public boolean isCellAlive(int col, int row) throws IllegalArgumentException {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    return population.contains(new Cell(col, row));
  }

  @Override
  public void setCellAlive(int col, int row) throws IllegalArgumentException {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    population.add(new Cell(col, row));
  }

  @Override
  public void setCellDead(int col, int row) throws IllegalArgumentException {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    population.remove(new Cell(col, row));
  }

  @Override
  public void resize(int newCols, int newRows) {
    int oldCols = getColumns();
    int oldRows = getRows();

    allNeighbors.clear();
    if (newCols < getColumns()) {
      for (int row = newRows; row < oldRows; row++) {
        for (int col = 0; col < oldCols; col++) {
          population.remove(field[row][col]);
        }
      }
    }
    if (newRows < field.length) {
      for (int row = 0; row < oldRows; row++) {
        for (int col = newCols; col < oldCols; col++) {
          population.remove(field[row][col]);
        }
      }
    }

    field = new Cell[newRows][newCols];
    for (int row = 0; row < newRows; row++) {
      for (int col = 0; col < newCols; col++) {
        field[row][col] = new Cell(col, row);
        allNeighbors.put(field[row][col].hashCode(), getNeighbors(field[row][col]));
      }
    }
  }

  @Override
  public int getRows() {
    return field.length;
  }

  @Override
  public int getColumns() {
    return field[0].length;
  }

  @Override
  public Collection<Cell> getPopulation() {
    return new HashSet<>(population);
  }

  @Override
  public void clear() {
    for (int row = 0; row < getRows(); row++) {
      for (int col = 0; col < getColumns(); col++) {
        setCellDead(col, row);
      }
    }
    this.setGenerationToZero();
  }

  @Override
  public void next() {
    Set<Cell> cellsToRecalculate = getCellsToRecalculate();
    recalculateNext(cellsToRecalculate);
    generation++;
  }

  @Override
  public int getGenerations() {
    return generation;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int row = 0; row < getRows(); row++) {
      for (int col = 0; col < getColumns(); col++) {
        if (isCellAlive(col, row)) {
          stringBuilder.append("X");
        } else {
          stringBuilder.append(".");
        }
      }
      stringBuilder.append("\n");
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }
}