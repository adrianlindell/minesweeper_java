// Adrian Lindell, 10/26/2021

import java.util.Scanner;

class Main {
  public static boolean isLoser;
  public static boolean[][] bombGrid;
  public static boolean[][] flagGrid;
  public static boolean[][] gridRevealed;
  public static char[][] gameGrid;
  public static int numRows;
  public static int numCols;

  static void setBombs(int numBombs, int ignoreRow, int ignoreCol) {
    // ignoreRow, ignoreCol is the first click location

    int randRow, randCol;
    while (numBombs > 0) {
      // generate random row and col
      randRow = (int) (numRows * Math.random());
      randCol = (int) (numCols * Math.random());

      // if there is a bomb at randRow, randCol or it is the first click location then regenerate random numbers
      while (bombGrid[randRow][randCol] == true || (randRow == ignoreRow && randCol == ignoreCol)) {
        randRow = (int) (numRows * Math.random());
        randCol = (int) (numCols * Math.random());
      }

      // place bomb
      bombGrid[randRow][randCol] = true;

      // increment neighbors
      for (int offsetX = -1; offsetX < 2; offsetX++) {
        if (randRow + offsetX >= 0 && randRow + offsetX < numRows) {
          for (int offsetY = -1; offsetY < 2; offsetY++) {
            if (randCol + offsetY >= 0 && randCol + offsetY < numCols) {
              // increment neighbors
              gameGrid[randRow + offsetX][randCol + offsetY]++;
            }
          }
        }
      }

      numBombs--;
    }
  }

  static void printGrid() {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        if (bombGrid[i][j]) {
          System.out.print("B ");
        } else {
          System.out.print("- ");
        }
      }
      System.out.println();
    }
  }

  static void printToUser() {
    for (int j = 0; j <= numCols; j++) {
      System.out.print(j + " ");
      if (j == 0) {
        System.out.print(" ");
      }
    }
    System.out.println();
    for (int i = 0; i < numRows; i++) {
      System.out.print((i + 1) + " ");
      if (i + 1 != 10) {
        System.out.print(" ");
      }
      for (int j = 0; j < numCols; j++) {
        if (gridRevealed[i][j]) {
          System.out.print(gameGrid[i][j]);
          System.out.print(" ");
        } else if (flagGrid[i][j]) {
          System.out.print("F ");
        } else if (isLoser && bombGrid[i][j]) {
          System.out.print("B ");
        } else {
          System.out.print("- ");
        }
      }
      System.out.println();
    }
  }

  static void printSpace() {
    for (int i = 0; i < 5; i++) {
      System.out.println();
    }
  }

  static void reveal(int row, int col) {
    // base case 0: row,col is bomb
    if (bombGrid[row][col]) {
      isLoser = true;
      return;
    }
    // base case 1: row,col is already revealed
    if (gridRevealed[row][col]) {
      return;
    }
    gridRevealed[row][col] = true;
    
    // base case 2: row,col is a number not '0'
    if (gameGrid[row][col] > '0' && gameGrid[row][col] <= '9') {
      return;
    }

    if (gameGrid[row][col] == '0') {
      // recursive case: reveal neighbors
      for (int offsetX = -1; offsetX < 2; offsetX++) {
        if (row + offsetX >= 0 && row + offsetX < numRows) {
          for (int offsetY = -1; offsetY < 2; offsetY++) {
            if (col + offsetY >= 0 && col + offsetY < numCols) {
              reveal(row + offsetX, col + offsetY);
            }
          }
        }
      }
    }

  }

  static boolean isWinner() {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        if (!bombGrid[i][j] && !gridRevealed[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int numBombs = 15;
    numRows = 10;
    numCols = 10;
    isLoser = false;
    int action = 0;

    bombGrid = new boolean[numRows][numCols];
    gameGrid = new char[numRows][numCols];
    gridRevealed = new boolean[numRows][numCols];
    flagGrid = new boolean[numRows][numCols];

    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        bombGrid[i][j] = false;
      }
    }

    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        flagGrid[i][j] = false;
      }
    }

    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        gridRevealed[i][j] = false;
      }
    }

    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        gameGrid[i][j] = '0';
      }
    }

    // user input
    System.out.print("Please input a row: ");
    int row = in.nextInt();
    row--;
    while (row < 0 || row >= numRows) {
      System.out.println("Invalid row.");
      System.out.print("Please input a row: ");
      row = in.nextInt();
      row--;
    }
    System.out.print("Please input a column: ");
    int col = in.nextInt();
    col--;
    while (col < 0 || col >= numCols) {
      System.out.println("Invalid column.");
      System.out.print("Please input a column: ");
      col = in.nextInt();
      col--;
    }

    // set up grid with ignoreRow and ignoreCol
    setBombs(numBombs, row, col);

    // reveal row,col
    reveal(row,col);
    printToUser();
    printSpace();

    while (!isLoser && !isWinner()) {
      System.out.print("Please input a row: ");
      row = in.nextInt();
      row--;
      while (row < 0 || row >= numRows) {
        System.out.println("Invalid row.");
        System.out.print("Please input a row: ");
        row = in.nextInt();
        row--;
      }
      System.out.print("Please input a column: ");
      col = in.nextInt();
      col--;
      while (col < 0 || col >= numCols) {
        System.out.println("Invalid column.");
        System.out.print("Please input a column: ");
        col = in.nextInt();
        col--;
      }


      System.out.print("Select an action (1: reveal square, 2: place flag): "); // possibly reselect square
      action = in.nextInt();

      while (action != 1 && action != 2) {
        System.out.println("Invalid action.");

        System.out.print("Select an action (1: reveal square, 2: place flag): ");
        action = in.nextInt();
      }
      if (action == 1) {
        // reveal row,col
        reveal(row, col);
      } else if (action == 2) {
        // place flag at row,col
        flagGrid[row][col] = true;
      } else {
        System.out.println("Uh oh, something went wrong. Please play again!");
      }

      printSpace();
      printToUser();
    }

    if (isLoser) {
      System.out.println("You lost :(");
    }
    else if (isWinner()) {
      System.out.println("You win :)");
    }
    else {
      System.out.println("Uh oh, something went wrong. Please play again!");
    }
  }
}
