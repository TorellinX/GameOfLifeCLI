package org.sosylab.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests for the implementation of the {@link Grid} interface.
 */
public class GridTest {

  private static final int WORLD_TOTAL_ROWS = 5;

  private static final int WORLD_TOTAL_COLUMNS = 5;

  Grid newWorld(int row, int col) {
    return new Game(row, col);
  }

  private Grid newWorld() {
    return newWorld(WORLD_TOTAL_ROWS, WORLD_TOTAL_COLUMNS);
  }

  private Grid newWorld(Set<Cell> cells) {
    Grid world = newWorld();

    for (Cell cell : cells) {
      world.setCellAlive(cell.getRow(), cell.getColumn());
    }

    return world;
  }

  @Test
  public void newWorld_whenColumnIsZero_throwsException() {
    try {
      newWorld(WORLD_TOTAL_ROWS, 0);
      fail("Implementation of Grid may not be instantiated with zero columns");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of rows and columns must be positive", e.getMessage());
    }
  }

  @Test
  public void newWorld_whenRowIsZero_throwsException() {
    try {
      newWorld(0, WORLD_TOTAL_COLUMNS);
      fail("Implementation of Grid may not be instantiated with zero rows");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of rows and columns must be positive", e.getMessage());
    }
  }

  @Test
  public void cells_whenInstantiatingClass_initWithEmptyCells() {
    Grid world = newWorld();
    worldIsExactly(world, Collections.emptySet());
  }

  @Test
  public void isAlive_whenRowIsNegative_throwsException() {
    try {
      newWorld().isCellAlive(-1, 0);
      fail("Parameter for row is negative");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of row and column may not be negative", e.getMessage());
    }
  }

  @Test
  public void isAlive_whenRowIsTooLarge_throwsException() {
    try {
      newWorld().isCellAlive(WORLD_TOTAL_ROWS, 0);
      fail("Parameter for row exceeds the total number of rows");
    } catch (IllegalArgumentException e) {
      assertEquals(
          "Parameters for row and column may not exceed the maximum number of rows and columns",
          e.getMessage());
    }
  }

  @Test
  public void isAlive_whenColIsNegative_throwsException() {
    try {
      newWorld().isCellAlive(0, -1);
      fail("Parameter for column is negative");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of row and column may not be negative", e.getMessage());
    }
  }

  @Test
  public void isAlive_whenColIsTooLarge_throwsException() {
    try {
      newWorld().isCellAlive(0, WORLD_TOTAL_COLUMNS);
      fail("Parameter for column exceeds the total number of columns");
    } catch (IllegalArgumentException e) {
      assertEquals(
          "Parameters for row and column may not exceed the maximum number of rows and columns",
          e.getMessage());
    }
  }

  @Test
  public void setAlive_whenRowIsNegative_throwsException() {
    try {
      newWorld().setCellAlive(-1, 0);
      fail("Parameter for row is negative");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of row and column may not be negative", e.getMessage());
    }
  }

  @Test
  public void setAlive_whenRowIsTooLarge_throwsException() {
    try {
      newWorld().setCellAlive(WORLD_TOTAL_ROWS, 0);
      fail("Parameter for rows is out of bounds");
    } catch (IllegalArgumentException e) {
      assertEquals(
          "Parameters for row and column may not exceed the maximum number of rows and columns",
          e.getMessage());
    }
  }

  @Test
  public void setAlive_whenColIsNegative_throwsException() {
    try {
      newWorld().setCellAlive(0, -1);
      fail("Parameter for col is negative");
    } catch (IllegalArgumentException e) {
      assertEquals("Number of row and column may not be negative", e.getMessage());
    }
  }

  @Test
  public void setAlive_whenColIsTooLarge_throwsException() {
    try {
      newWorld().setCellAlive(0, WORLD_TOTAL_COLUMNS);
      fail("Parameter for column is out of bounds");
    } catch (IllegalArgumentException e) {
      assertEquals(
          "Parameters for row and column may not exceed the maximum number of rows and columns",
          e.getMessage());
    }
  }

  @Test
  public void setAlive_whenSettingCells_setsOnlyTheCells() {
    Set<Cell> cells = new HashSet<>();
    addCell(cells, 1, 2);
    addCell(cells, 1, 3);
    addCell(cells, 2, 4);

    Grid world = newWorld();
    for (Cell cell : cells) {
      world.setCellAlive(cell.getRow(), cell.getColumn());
    }

    worldIsExactly(world, cells);
  }

  @Test
  public void resize_whenSizeIsValid_resizes() {
    int newRowSize = 3;
    int newColumnSize = 5;

    Grid world = newWorld(WORLD_TOTAL_ROWS, WORLD_TOTAL_COLUMNS);
    world.resize(newRowSize, newColumnSize);

    assertEquals(world.getRows(), newRowSize);
    assertEquals(world.getColumns(), newColumnSize);
  }

  @Test
  public void resize_always_keepsCellsInRange() {
    Set<Cell> cells = new HashSet<>();
    addCell(cells, 1, 1);
    addCell(cells, 2, 0);
    addCell(cells, 1, 2);
    addCell(cells, 2, 4);
    addCell(cells, 3, 4);
    addCell(cells, 4, 4);

    int newRowSize = 3;
    int newColumnSize = 5;

    Grid world = newWorld(cells);

    world.resize(newRowSize, newColumnSize);

    // cells that are out of range are deleted.
    removeCell(cells, 3, 4);
    removeCell(cells, 4, 4);

    worldIsExactly(world, cells);
  }

  @Test
  public void getColumns_returnsColumns() {
    Grid world = newWorld();
    assertEquals(world.getColumns(), WORLD_TOTAL_COLUMNS);
  }

  @Test
  public void getRows_returnsRows() {
    Grid world = newWorld();
    assertEquals(world.getRows(), WORLD_TOTAL_ROWS);
  }

  @Test
  public void clear_resetsGrid() {
    Set<Cell> cells = new HashSet<>();
    addCell(cells, 1, 1);
    addCell(cells, 0, 2);
    addCell(cells, 2, 1);
    addCell(cells, 4, 2);

    Grid world = newWorld(cells);
    world.clear();

    worldIsExactly(world, Collections.emptySet());
  }

  @Test
  public void next_killsLonelyCell() {
    Grid world = newWorld();
    world.setCellAlive(3, 3);

    world.next();

    worldIsExactly(world, Collections.emptySet());
  }

  @Test
  public void next_fourBlockSurvives() {
    Set<Cell> block = new HashSet<>();
    addCell(block, 1, 1);
    addCell(block, 1, 2);
    addCell(block, 2, 1);
    addCell(block, 2, 2);

    Grid world = newWorld(block);

    world.next();

    worldIsExactly(world, block);
  }

  @Test
  public void next_lineTwitches() {
    Set<Cell> line = new HashSet<>();
    addCell(line, 1, 2);
    addCell(line, 2, 2);
    addCell(line, 3, 2);

    Set<Cell> row = new HashSet<>();
    addCell(row, 2, 1);
    addCell(row, 2, 2);
    addCell(row, 2, 3);

    Grid world = newWorld(line);

    world.next();

    worldIsExactly(world, row);

    world.next();

    worldIsExactly(world, line);
  }

  @Test
  public void getGenerations_isInitiallyZero() {
    Grid world = newWorld();
    assertEquals(world.getGenerations(), 0);
  }

  @Test
  public void getGenerations_whenInvokingNext_hasCorrectValue() {
    int numberGenerations = 3;

    Grid world = newWorld();
    for (int i = 0; i < numberGenerations; i++) {
      world.next();
    }

    assertEquals(world.getGenerations(), numberGenerations);
  }

  private void addCell(Set<Cell> cells, int x, int y) {
    cells.add(new Cell(x, y));
  }

  private void removeCell(Set<Cell> cells, int x, int y) {
    cells.remove(new Cell(x, y));
  }

  private void worldIsExactly(Grid world, Set<Cell> cells) {
    for (Cell activeCell : cells) {
      assertTrue(world.isCellAlive(activeCell.getRow(), activeCell.getColumn()),
          "Cell " + activeCell + " must be active");
    }

    for (int x = 0; x < world.getRows(); x++) {
      for (int y = 0; y < world.getColumns(); y++) {
        if (world.isCellAlive(x, y)) {
          Cell cell = new Cell(x, y);
          assertTrue(cells.contains(cell), "Cell " + cell + " must not be active");
        }
      }
    }
  }
}

