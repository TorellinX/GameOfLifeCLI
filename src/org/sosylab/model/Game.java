package org.sosylab.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
  private final List<List<Character>> field;


  /**
   * TODO: add JavaDoc
   */
  public Game(int rows, int cols) {
    // TODO: implement the constructor
    this.generation = 0;
    this.field = new ArrayList<>(rows);
    initializeField(rows, cols);
  }

  private void initializeField(int rows, int cols){
    for(int i = 0; i < rows; i++) {
      field.add(new ArrayList<>(cols));
      for(int j = 0; j < cols; j++) {
        field.get(i).add('.');
      }
    }
  }

  // TODO: implement the methods from the Grid-interface

  @Override
  public boolean isCellAlive(int row, int col){
    return false;  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
  }

  @Override
  public void setCellAlive(int row, int col){

  }

  @Override
  public void setCellDead(int row, int col){

  }

  @Override
  public void resize(int rows, int cols){

  }

  @Override
  public int getRows(){
    return field.size();
  }

  @Override
  public int getColumns(){
    return field.get(0).size();
  }

  @Override
  public Collection<Cell> getPopulation(){
    List<Cell> tempArray = new ArrayList<>(); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    tempArray.add(new Cell(10,10)); //!!!!!!!!!!!!!!!!!!!!!!!!!!!
    return tempArray; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  }

  @Override
  public void clear(){
    for(int i = 0; i < getRows(); i++) {
      for(int j = 0; j < getColumns(); j++) {
        field.get(i).set(j, '.');
      }
    }
    this.setGenerationToZero();
  }

  private void setGenerationToZero() {
    this.generation = 0;
  }

  @Override
  public void next(){

  }

  @Override
  public int getGenerations(){
    return generation;
  }

  @Override
  public String toString(){
    StringBuilder stringBuilder = new StringBuilder();
    for(int i = 0; i < getRows(); i++) {
      for(int j = 0; j < getColumns(); j++) {
        stringBuilder.append(field.get(i).get(j));
      }
      stringBuilder.append("\n");
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }

  // You may add further private and public methods as needed.
  // The methods provided in the interface are however sufficient.


}

