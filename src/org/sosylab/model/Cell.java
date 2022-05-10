package org.sosylab.model;

import java.util.Objects;

/**
 * A two dimensional coordinate within a cell grid.
 */
public class Cell {

  /**
   * The x-coordinate of the cel.
   */
  private final int row;

  /**
   * The y-coordinate of the cell.
   */
  private final int column;

  /**
   * Constructs a new cell.
   *
   * @param row    The x-coordinate (row of cell).
   * @param column The y-coordinate (column of cell).
   */
  public Cell(int row, int column) {
    if (row < 0 || column < 0) {
      throw new IllegalArgumentException("Cell must not have negative coordinates");
    }

    this.row = row;
    this.column = column;
  }

  /**
   * Get the x-coordinate of a cell.
   *
   * @return The x-coordinate.
   */
  public int getRow() {
    return row;
  }

  /**
   * Get the y-coordinate of a cell.
   *
   * @return The y-coordinate.
   */
  public int getColumn() {
    return column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Cell cell = (Cell) other;
    return row == cell.row && column == cell.column;
  }

  @Override
  public String toString() {
    return String.format("<%s, %s>", row, column);
  }

}

