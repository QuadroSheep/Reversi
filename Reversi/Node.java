public class Node{
  private Node child[] = new Node[60];
  private int score;
  private int count = 0;
  private int gameState[][];
  private int blackScore, whiteScore;

  public Node(int gameState[][], int blackScore, int whiteScore){
    this.gameState = gameState;
    this.blackScore = blackScore;
    this.whiteScore = whiteScore;
  }

  public void setChild(Node insertChild){
    child[count++] = insertChild;
  }

  public void setScore(int score){
    this.score = score;
  }

  public int getNumberOfChildren(){
    return count;
  }

  public Node getChild(int index){
    return child[index];
  }

  public int[][] getGameState(){
    return gameState;
  }
}
