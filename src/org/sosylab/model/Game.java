package org.sosylab.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: add JavaDoc and implement the Grid-interface accordingly
 */
public class Game implements Grid {

  // Staying alive in this range
  private static final int STAY_ALIVE_MIN_NEIGHBORS = 2;
  private static final int STAY_ALIVE_MAX_NEIGHBORS = 3;

  // Condition for getting newly born
  private static final int NEWBORN_NEIGHBORS = 3;

  // TODO: add instance variables
  private int generation;

  private final Cell[][] field;
  private final Map<Integer, List<Cell>> neighborsField;
  private final Map<Integer, State> states;
  private final Set<Cell> population;


  /**
   * TODO: add JavaDoc
   */
  public Game(int cols, int rows) {
    if (cols <= 0 || rows <= 0) {
      throw new IllegalArgumentException("Number of columns and rows must be positive");
    }
    this.generation = 0;
    this.field = new Cell[rows][cols];
    this.neighborsField = new HashMap<>();
    this.states = new HashMap<>();
    this.population = new HashSet<>();
    initializeFields();
  }


  private void initializeFields() {
    for (int row = 0; row < field.length; row++) {
      for (int col = 0; col < field[0].length; col++) {
        field[row][col] = new Cell(col, row);
        neighborsField.put(field[row][col].hashCode(), calculateNeighbors(field[row][col]));
        setCellDead(col, row);
      }
    }
  }


  // TODO: implement the methods from the Grid-interface

  @Override
  public boolean isCellAlive(int col, int row) {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    return states.get(field[row][col].hashCode()) == State.ALIVE;
  }

  @Override
  public void setCellAlive(int col, int row) {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    states.put(field[row][col].hashCode(), State.ALIVE);
    population.add(new Cell(col, row));
  }

  @Override
  public void setCellDead(int col, int row) {
    if (col >= this.getColumns() || row >= this.getRows()) {
      throw new IllegalArgumentException(
          "Parameters for column and row may not exceed the maximum number of columns and rows");
    }
    if (col < 0 || row < 0) {
      throw new IllegalArgumentException("Number of column and row may not be negative");
    }
    states.put(field[row][col].hashCode(), State.DEAD);
    population.remove(new Cell(col, row));
  }

  @Override
  public void resize(int cols, int rows) {
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
     /*
    List<Cell> population = new ArrayList<>();
    for (int row = 0; row < field.length; row++) {
      for (int column = 0; column < field[0].length; column++) {
        if (isCellAlive(column, row)) {
          population.add(new Cell(column, row));
        }
      }
    }
    return population;
    */
    return population;
  }

  @Override
  public void clear() {
    for (int row = 0; row < getRows(); row++) {   //RRRRRRRRRRRRRRRRRR
      for (int col = 0; col < getColumns(); col++) { //RRRRRRRRRRRRRRRRRR
        setCellDead(col, row);
      }
    }
    this.setGenerationToZero();
  }

  private void setGenerationToZero() {
    this.generation = 0;
  }

  @Override
  public void next() {
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Set<Cell> cellsToRecalculate = createCellsToRecalculate();
    Map<Integer, State> oldStates = Map.copyOf(states);
    recalculateState(cellsToRecalculate, oldStates);



    generation++;
  }

  private Set<Cell> createCellsToRecalculate() {
    Set<Cell> cellsToRecalculate = new HashSet<>();
    // List<Cell> population = new ArrayList<>(getPopulation());

    for (Cell cell : population) {
      cellsToRecalculate.addAll(calculateNeighbors(cell));
    }
    cellsToRecalculate.addAll(population);
    return cellsToRecalculate;
  }

  private void recalculateState(Set<Cell> cellsToRecalculate, Map<Integer, State> oldStates) {
    for (Cell cell : cellsToRecalculate) {
      int aliveNeighbors = countAliveNeighbors(cell, oldStates);
      if (isCellAlive(cell.getColumn(), cell.getRow()) && (aliveNeighbors < STAY_ALIVE_MIN_NEIGHBORS
          || aliveNeighbors > STAY_ALIVE_MAX_NEIGHBORS)) {
        setCellDead(cell.getColumn(), cell.getRow());
      } if (!isCellAlive(cell.getColumn(), cell.getRow()) && aliveNeighbors == NEWBORN_NEIGHBORS) {
          setCellAlive(cell.getColumn(), cell.getRow());
      }
    }
  }

  private int countAliveNeighbors(Cell cell, Map<Integer, State> oldStates){
    List<Cell> neighbors = neighborsField.get(cell.hashCode());
    int aliveNeighborsCounter = 0;
    if (neighbors == null){
      return aliveNeighborsCounter;
    }
    for (Cell neighbor : neighbors) {
      if(oldStates.get(neighbor.hashCode()) == State.ALIVE) {
        aliveNeighborsCounter++;
      }
    }
    return aliveNeighborsCounter;
  }




  @Override
  public int getGenerations() {
    return generation;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < getRows(); i++) {
      for (int j = 0; j < getColumns(); j++) {
        if (isCellAlive(j, i)) {
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

  // You may add further private and public methods as needed.
  // The methods provided in the interface are however sufficient.

  private ArrayList<Cell> calculateNeighbors(Cell cell) {     //  какой класс?
    int row = cell.getRow();
    int column = cell.getColumn();
    ArrayList<Cell> neighbors = new ArrayList<>();    // может быть в итоге пустым!!!!!!!!!
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
}

