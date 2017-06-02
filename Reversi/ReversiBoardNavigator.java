import java.util.ArrayList;


public class ReversiBoardNavigator{


  private static final int NONE = 0;
  private static final int BLACK = -1;
  private static final int WHITE = 1;


  public static int[] updatePosition(int m, int[] position){
    int update[] = new int[2];
    switch(m){
      case 1: // up
      update[0] = position[0] - 1;
      update[1] = position[1];
      break;
      case 2: // upper right
      update[0] = position[0] - 1;
      update[1] = position[1] + 1;
      break;
      case 3: // right
      update[0] = position[0];
      update[1] = position[1] + 1;
      break;
      case 4: // lower right
      update[0] = position[0] + 1;
      update[1] = position[1] + 1;
      break;
      case 5: // down
      update[0] = position[0] + 1;
      update[1] = position[1];
      break;
      case 6: // lower left
      update[0] = position[0] + 1;
      update[1] = position[1] - 1;
      break;
      case 7: // left
      update[0] = position[0];
      update[1] = position[1] - 1;
      break;
      case 8: // upper left
      update[0] = position[0] - 1;
      update[1] = position[1] - 1;
      break;
    }

    return update;
  }

  public static boolean addressIsOnMap(int[] position){
    if(position[0] >= 0 && position[0] < 8){
      if(position[1] >= 0 && position[1] < 8){
        return true;
      }
    }
    return false;
  }

  public static ArrayList<int[]> checkPossibleMoves(int[][] gameState, boolean isWhiteTurn){
      int availableMoves = 0;
      int[] currentPosition = new int[2];
      ArrayList<int[]> arr = new ArrayList<int[]>();

      for(int i = 0; i < 8; i++){
          for(int j = 0; j < 8; j++){
              currentPosition[0] = i;
              currentPosition[1] = j;
              int oponent = 0;
              int you = 0;
              if(isWhiteTurn && gameState[i][j] == WHITE){
                  you = WHITE;
                  oponent = BLACK;
              }
              else if(!isWhiteTurn && gameState[i][j] == BLACK){
                  you = BLACK;
                  oponent = WHITE;
              }

              if(gameState[i][j] == you){
                  for(int m = 1; m <= 8; m++){
                      currentPosition[0] = i;
                      currentPosition[1] = j;
                      boolean passedOponent = false;
                      while(true){
                          currentPosition = updatePosition(m, currentPosition);
                          if(addressIsOnMap(currentPosition)){
                              if(gameState[currentPosition[0]][currentPosition[1]] == oponent){
                                  passedOponent = true;
                                  continue;
                              }
                              else if(gameState[currentPosition[0]][currentPosition[1]] == NONE && passedOponent){
                                  int[] possible = new int[2];
                                  possible[0] = currentPosition[0];
                                  possible[1] = currentPosition[1];
                                  arr.add(possible);
                                  availableMoves++;
                                  break;
                              }
                              else{
                                  break;
                              }
                          }
                          else{
                              break;
                          }

                      }
                  }
              }
          }
      }
        return arr;
  }


}
