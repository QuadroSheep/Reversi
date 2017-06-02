import java.util.ArrayList;

public class ReversiAI{
  private static final long serialVersionUID = 1L;

  private final int NONE = 0;
  private final int BLACK = -1;
  private final int WHITE = 1;

  private int ALPHA = -1000000;
  private int BETA = 1000000;
  private int blackScore = 0;
  private int whiteScore = 0;

  public int[] goAI(int gameState[][], int blackScore, int whiteScore){
    Node root = new Node(gameState, blackScore, whiteScore);
    ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
    int bestMove[] = new int[2];
    float bestScore;
    float alpha = -1000000;
    float beta = 1000000;
    boolean isFirst = true;
    float temp;
    float score = 0;
    float goal = -1000000;
    possibleMoves = ReversiBoardNavigator.checkPossibleMoves(root.getGameState(), true);
    for(int possibleMove[] : possibleMoves){
      this.blackScore = blackScore;
      this.whiteScore = whiteScore + 1;
      int tempGameState[][] = new int[8][8];
      int tempo[][] = root.getGameState();
      for(int a = 0; a < 8; a++){
        for(int b = 0; b < 8; b++){
          tempGameState[a][b] = tempo[a][b];
        }
      }

      tempGameState[possibleMove[0]][possibleMove[1]] = WHITE;

      Node node = new Node(evaluate(possibleMove[0], possibleMove[1],tempGameState, true), blackScore, whiteScore);
      score = makeGameTree(node, false, 1, this.blackScore, this.whiteScore, alpha, beta);
      if(score > goal){
        goal = score;
        bestMove[0] = possibleMove[0];
        bestMove[1] = possibleMove[1];
      }

    }
    return bestMove;
  }

  public float makeGameTree(Node root, Boolean turn, int depth, int blackScore, int whiteScore, float alpha, float beta){
    ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
    float goal = 0;
    float score;
    float temp = 0;
    boolean isFirst = true;

    if(turn){
      goal = -1000000;
    }else{
      goal = 1000000;
    }

    if(blackScore+whiteScore == 64){
      if(whiteScore > blackScore){
        return 100;
      }else if(blackScore < whiteScore){
        return -100;
      }else{
        return 0;
      }
    }

    if(depth == 3){
      return gameStateTotalHeuristic(root.getGameState());
    }
    possibleMoves = ReversiBoardNavigator.checkPossibleMoves(root.getGameState(), turn);

    for(int possibleMove[] : possibleMoves){
      this.blackScore = blackScore;
      this.whiteScore = whiteScore;
      int tempGameState[][] = new int[8][8];
      int tempo[][] = root.getGameState();
      for(int a = 0; a < 8; a++){
        for(int b = 0; b < 8; b++){
          tempGameState[a][b] = tempo[a][b];
        }
      }

      if(turn){
        tempGameState[possibleMove[0]][possibleMove[1]] = WHITE;
        this.whiteScore++;
      }else{
        tempGameState[possibleMove[0]][possibleMove[1]] = BLACK;
        this.blackScore++;
      }

        Node node = new Node(evaluate(possibleMove[0], possibleMove[1],tempGameState, turn) , blackScore, whiteScore);
        root.setChild(node);
        score = makeGameTree(node, !turn, depth+1, this.blackScore, this.whiteScore, alpha, beta);
        if(turn){
          if(score > goal){
            goal = score;
            if(goal > beta){
              return goal;
            }else{
              alpha = goal;
            }
          }
        }else{
          if(score < goal){
            goal = score;
            if(goal < alpha){
              return goal;
            }else{
              beta = goal;
            }
          }
        }
    }

    if(possibleMoves.size() == 0){
      return makeGameTree(root, !turn, depth + 1, blackScore, whiteScore, alpha, beta);
    }
    return goal; // return minimax score
  }

  public int[][] evaluate(int i, int j, int[][] gameState, boolean isWhiteTurn){
    String temp[] = new String[8];
    int[] currentPosition = new int[2];
    int count = 0;
    int ctr = 0;
    boolean flippable = false;
    String[] toFlip = new String[64];
    currentPosition[0] = i;
    currentPosition[1] = j;
    int[][] newState = gameState;

    for(int a = 1; a <= 8; a++){
      currentPosition[0] = i;
      currentPosition[1] = j;
      count = 0;
      flippable = false;
      temp = new String[8];
      while(true){
        currentPosition = ReversiBoardNavigator.updatePosition(a, currentPosition);
        if(ReversiBoardNavigator.addressIsOnMap(currentPosition)){
          if(isWhiteTurn && newState[currentPosition[0]][currentPosition[1]] == BLACK){
            temp[count++] = "" + currentPosition[0] + currentPosition[1];
          }else if(!isWhiteTurn && newState[currentPosition[0]][currentPosition[1]] == WHITE){
            temp[count++] = "" + currentPosition[0] + currentPosition[1];
          }else if(!isWhiteTurn && newState[currentPosition[0]][currentPosition[1]] == BLACK){
            flippable = true;
            break;
          }else if(isWhiteTurn && newState[currentPosition[0]][currentPosition[1]] == WHITE){
            flippable = true;
            break;
          }else{
            break;
          }
        }else{
          break;
        }
      }
      if(flippable){
        for(int b = 0; b < count; b++){
          toFlip[ctr++] = temp[b];
        }
      }
    }

/********************************NO CHANGES EXCEPT content -> newState and symbols changed to final Values*******************************************/
    for(int a = 0; a < ctr; a++){
      if(isWhiteTurn){
        newState[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")] = WHITE;
        whiteScore++;
        blackScore--;
      }else if(!isWhiteTurn){
        newState[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")] = BLACK;
        blackScore++;
        whiteScore--;
      }
    }

    return newState;
  }

  public float coinParityHeuristic(int[][] gameState){
      float totalScore = (evaluateScore(gameState, true) + evaluateScore(gameState, false));
      float heuristic = (100 * (evaluateScore(gameState, true) - (evaluateScore(gameState, false)/totalScore)));
      return heuristic;
  }

  public float actualMobilityHeuristic(int[][] gameState){
    int whiteMobility = ReversiBoardNavigator.checkPossibleMoves(gameState, true).size();
    int blackMobility = ReversiBoardNavigator.checkPossibleMoves(gameState, false).size();
    float heuristic = 0;
    if(whiteMobility + blackMobility != 0){
        heuristic = 100 * ((whiteMobility - blackMobility)/(whiteMobility + blackMobility));
    }
    return heuristic;
  }

  public float potentialMobilityHeuristic(int[][] gameState){
    int whitePotentialMobilityValue = potentialMobility(gameState, true);
    int blackPotentialMobilityValue = potentialMobility(gameState, false);
    float heuristic = 0;
    if(whitePotentialMobilityValue + whitePotentialMobilityValue != 0){
      heuristic = 100 * ((whitePotentialMobilityValue - blackPotentialMobilityValue)/(whitePotentialMobilityValue + blackPotentialMobilityValue));
    }
    return heuristic;
  }

  public float cornersHeuristic(int[][] gameState){
    int whiteCornerValue = playerCornerValue(gameState, true);
    int blackCornerValue = playerCornerValue(gameState, false);
    float heuristic = 0;
    if(whiteCornerValue + blackCornerValue != 0){
        heuristic = 100 * ((whiteCornerValue - blackCornerValue)/(whiteCornerValue + blackCornerValue));
    }
    return heuristic;
  }

  public float stabilityHeuristic(int[][] gameState){ //Still under development,. makure kasi hin guti T.T
    return 0.0f;
  }

  public float gameStateTotalHeuristic(int[][] gameState){
    float coinHeuristic = 25 * coinParityHeuristic(gameState);
    float actualMobilityHeuristic = 5 * actualMobilityHeuristic(gameState);
    float potentialMobilityHeuristic = 5 * potentialMobilityHeuristic(gameState);
    float cornerHeuristic = 30 * cornersHeuristic(gameState);
    float total = coinHeuristic + actualMobilityHeuristic + potentialMobilityHeuristic + cornerHeuristic;

    return total;
  }


  private final int POTENTIALCORNERWEIGHT = 20;
  private final int CORNERCAPTUREDWEIGHT = 30;
  private int[][] cornerIndexes = {{0,0},{0,7},{7,0},{7,7}};

  public int playerCornerValue(int[][] state, boolean isWhiteTurn){
    int type, score = 0;
    if(isWhiteTurn){
        type = WHITE;
    }
    else{
        type = BLACK;
    }

    for(int i = 0; i < 4; i++){
      if(state[cornerIndexes[i][0]][cornerIndexes[i][1]] == type){
        score += CORNERCAPTUREDWEIGHT;
      }

      ArrayList<int[]> posMoves = ReversiBoardNavigator.checkPossibleMoves(state, isWhiteTurn);
      for(int[] index: posMoves){
        if(cornerIndexes[i][0] == index[0] && cornerIndexes[i][1] == index[1]){
          score += POTENTIALCORNERWEIGHT;
        }
      }
    }
    return score;
  }

  public int evaluateScore(int[][] state, boolean turn){
    int type, score = 0;
    if(turn){
      type = WHITE;
    }else{
      type = BLACK;
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if(state[i][j] == type){
          score++;
        }
      }
    }
    return score;
  }

  public int potentialMobility(int[][] state, boolean turn){
    int type, score = 0;
    if(turn){
      type = BLACK;
    }else{
      type = WHITE;
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if(state[i][j] == type){
          int[] pos = new int[2];
          for(int k = 1; k <= 8; k++){
            pos[0] = i;
            pos[1] = j;
            pos = ReversiBoardNavigator.updatePosition(k, pos);
            if(ReversiBoardNavigator.addressIsOnMap(pos) && state[pos[0]][pos[1]] == NONE){
              score++;
            }
          }
        }
      }
    }
    return score;
  }
}
