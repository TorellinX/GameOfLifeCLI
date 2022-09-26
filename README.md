# GameOfLife CLI

John Horton Conway’s Game of Life is no ordinary game, but a mathematical system of two-dimensionally arranged cellular automata. It is the best known example of cellular automata. Conway first published the game in Scientific American 223 in October 1970: http://ddi.cs.uni-potsdam.de/John Horton Conway’s Game of Life is no ordinary game, but a mathematical system of two-dimensionally arranged cellular automata. It is the best known example of cellular automata. Conway first published the game in Scientific American 223 in October 1970: http://ddi.cs.uni-potsdam.de/HyFISCH/Produzieren/lis_projekt/proj_gamelife/ConwayScientificAmerican.htmHyFISCH/Produzieren/lis_projekt/proj_gamelife/ConwayScientificAmerican.htm

## Game of Life Rules

The game board is divided into rows and columns. On each square is a cell, which can be either alive or dead. Each cell has 8 neighbors with which it interacts. After each time step (‘mutation’), the state of the cell changes as follows:

- [ ] Living cells with less than two neighbors die of loneliness in the following generation.
- [ ] Living cells with more than three neighbors die of overpopulation in the subsequent generation.
- [ ] Living cells with exactly two or three living neighbors remain alive in the subsequent generation.
- [ ] Dead cells with exactly three living neighbors are reborn in the subsequent generation.

In the beginning, the playing field is populated with an initial population. Afterwards, the rules mentioned above are applied simultaneously to all cells for each step in time. This results in complex patterns that remain either stable, oscillate, grow or shrink depending on their shape. Some even wander across the playing field, so-called spaceships.

Theoretically, the playing field is infinitely large. However, since memory is limited, a limited playing field is assumed right from the start. Cells outside the playing field are considered dead.

## Common initial population

- [ ] Block (stable)
- [ ] Boat (stable)
- [ ] Blinker (two-phase oscillator)
- [ ] Toad (two-phase oscillator)
- [ ] Glider (spaceship)
- [ ] Spaceship (spaceship)
- [ ] Pulsar (three-phase oscillator)

Other known structures are listed in the Life Lexicon.

## Command-Line Interface

The Game of Life is controlled via a shell. The prompt shall be “gol> “. The following commands need to be supported:

- [ ] NEW x y
Starts a new game with a field size of x columns and y rows.

- [ ] ALIVE i j
Sets the cell in i-th column and j-th row alive.

- [ ] DEAD i j
Sets the cell in i-th column and j-th row dead.

- [ ] GENERATE
Calculates the next generation according to the rules of the game. The (consecutive) number of the generation must be displayed.

- [ ] PRINT
Prints the game field row by row. Living cells are printed with ‘X’ and dead ones with ‘.’.

- [ ] CLEAR
Kills all living cells and resets the generation counter to 0.

- [ ] RESIZE x y
Resizes the game field to x columns and y rows. Living cells whose coordinates are on the new playfield remain alive. The generation counter is not reset.

- [ ] SHAPE s
Loads a predefined initial population with name s. At least the above mentioned common populations must be present and referenceable with their above mentioned name. A CLEAR is automatically performed prior to this. The initial populations are centered on the playfield. If the population does not fit on the playfield an error message is printed.

- [ ] HELP
Prints a meaningful help text.

- [ ] QUIT
Quits the program.

The first letter is sufficient to recognize the command. Commands and shapes are not case sensitive. If the user command is invalid, the program must display a helpful error message in the format Error! <Message>:

For unknown commands, the following error message is displayed: Error! Invalid command. For all invalid arguments not mentioned in the commands above (for example too few arguments), the following error message is displayed: Error! Invalid arguments: EXPLANATION where ‘EXPLANATION’ is a placeholder for an explanation of the issue. Otherwise, if none of the above fits, then the following error message is sufficient: Error! EXPLANATION , where again the `EXPLANATION` is a placeholder for a description that explains the underlying issue.

## Implementation Tips
### Files To Submit
The name of your main file must be GameOfLifeMain.java. This also contains the main method, which can be executed from the command line.

In addition, a text file Tests.txt is also required, that again contains a description of your test cases. The test cases should show that you have tested your program extensively.

### Interface Definition
The use of the Grid.java interface for the model is mandatory! The shell must communicate with the model only by means of this interface. The logic regarding the shell and game must be strictly separated.
```

 public interface Grid {

  boolean isCellAlive(int col, int row); // get the status of a cell

  void setCellAlive(int col, int row);   // set a cell alive

  void setCellDead(int col, int row);    // put a cell into its dead state

  void resize(int cols, int rows); // resize grid

  int getColumns();                      // x-dimension
  
  int getRows();                         // y-dimension
 
  Collection<Cell> getPopulation();      // get all living cells

  void clear();                          // kill all cells

  void next();                           // compute next generation

  int getGenerations();                  // get number of generations

  String toString();                     // get string representation

}
```
The class GridTest.java contains JUnit tests (JUnit 5), which can be used to check whether your implementation of the Grid.java interface fulfills the requirements. When submitting, make sure that you also submit this class.

### Centering the Shape
Assume a Shape with dimensions (ws, hs) and a game field with dimensions (wf, hf). You can center the shape by calculating the offset from the shapes top left corner to the top left corner of the playfield:
'''
offset_x = floor( wf - ws ) / 2
offset_y = floor( hf - hs ) / 2
'''
 

Example: Given a 7 x 7 game field and a a shape with the form of a block, both offset_x and offset_y would calculate to 2. The block would therefore start at the coordinates (2, 2).

 

### Further Information
Resizing the playfield should not delete the current living population. Cells remain in the place where they were before. Cells that fall out of the playfield after resizing are removed.

In addition, always make sure that the state of your playfield is consistent.

## Examples
```
gol> NEW 10 10
gol> ALIVE 1 0
gol> ALIVE 2 1
gol> ALIVE 0 2
gol> ALIVE 1 2
gol> ALIVE 2 2
gol> PRINT
.X........
..X.......
XXX.......
..........
..........
..........
..........
..........
..........
..........
gol> GENERATE
Generation: 1
gol> GENERATE
Generation: 2
gol> PRINT
..........
..X.......
X.X.......
.XX.......
..........
..........
..........
..........
..........
..........
gol> DEAD 2 3
gol> GENERATE
Generation: 3
gol> PRINT
..........
.X........
..X.......
.X........
..........
..........
..........
..........
..........
..........
gol> RESIZE 15 15
gol> SHAPE Pulsar
gol> PRINT
...............
...XX.....XX...
....XX...XX....
.X..X.X.X.X..X.
.XXX.XX.XX.XXX.
..X.X.X.X.X.X..
...XXX...XXX...
...............
...XXX...XXX...
..X.X.X.X.X.X..
.XXX.XX.XX.XXX.
.X..X.X.X.X..X.
....XX...XX....
...XX.....XX...
...............
gol> GENERATE
Generation: 1
gol> GENERATE
Generation: 2
gol> PRINT
....X.....X....
....X.....X....
....XX...XX....
...............
XXX..XX.XX..XXX
..X.X.X.X.X.X..
....XX...XX....
...............
....XX...XX....
..X.X.X.X.X.X..
XXX..XX.XX..XXX
...............
....XX...XX....
....X.....X....
....X.....X....
gol> QUIT
```

## Implementation Tips
- [ ] Build the program incrementally. Start with the easiest commands to implement: Commands ‘HELP’ and ‘QUIT’

Next continue with the minimum requirements of the project: Commands ‘NEW’, ‘ALIVE’, ‘DEAD’, ‘CLEAR’ and ‘PRINT’.

Once you have tested that these commands work reliably and when you are happy with your code, continue with the commands that build on that: ‘GENERATE’ and ‘RESIZE’.

Finally, implement the command ‘SHAPE s’ (and any other missing commands).

- [ ] Do not implement for speed, but for understandability! Make the code easy to understand and work reliably. Introduce classes to keep your data in an understandable way and try to mimic the game’s world with your classes.

- [ ] Only care about performance bottlenecks as a last step. Regarding speed, we only care about asymptotic performance issues, i.e., unfitting data structures and nested loops that could be avoided.

## General Requirements
- [ ] You can modify the behavior of any method of the template, and add public methods if necessary.

- [ ] BUT: Do not modify any public method signatures or interfaces. You are not allowed to add method parameters to existing public methods.

- [ ] The output and return values of implemented methods must exactly match the output described here. Otherwise, tests will fail.

- [ ] Documentation of the source code is part of the exercise and will be graded. The Google Java Style Guide must be fulfilled.

Setup in Intellij can be done as follows:
Download the google_checks.xml to a location of your choice and import it next in Intellij. This is done by opening the settings (Menu → Settings), and then navigating to Editor → Code Style → Java. Finally, click on the gearwheel and afterwards on import Scheme → CheckSytle Configuration. There, add the downloaded xml file from before and apply the changes.
The asymptotic run-time performance of the implementation is part of the exercise and will be graded.


## Helpful Resources

Stream is an advanced data structure similar to lists. It can be consumed only once and has lots of methods for transformation and filtering. Often uses method references and other advanced topics. You can turn any Stream into a List-object like this:

```
Stream<Integer> numberStream = // .. snip ..;
List<Integer> numberList = numberStream.collect(Collectors.toList());
```


