import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReversiFrame extends JFrame{
  private static final long serialVersionUID = 1L;
  private int whiteScore = 2;
  private int blackScore = 2;

  private JPanel mainPanel;

  private JPanel gridPanel;
  private JPanel[][] box = new JPanel[8][8];

  private JLabel borderLabel = new JLabel(new ImageIcon("images/border.png"));
  private JLabel playerVsPlayer = new JLabel(new ImageIcon("images/playervsplayer.jpg"));
  private JLabel playerVsComputer = new JLabel(new ImageIcon("images/playervscomputer.jpg"));
  private JLabel instructions = new JLabel(new ImageIcon("images/instructions.png"));
  private JLabel credits = new JLabel(new ImageIcon("images/credits.png"));
  private JLabel gameOver;
  private JLabel blackScoreLabel;
  private JLabel whiteScoreLabel;

  ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/unmuteMusic.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
  ImageIcon imageIcon1 = new ImageIcon(new ImageIcon("images/unmuteSound.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
  ImageIcon imageIcon2 = new ImageIcon(new ImageIcon("images/muteMusic.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
  ImageIcon imageIcon3 = new ImageIcon(new ImageIcon("images/muteSound.png").getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

  private JLabel musicLabel = new JLabel(imageIcon);
  private JLabel soundLabel = new JLabel(imageIcon1);

  private boolean isMute;
  private boolean isFXMute;

  private JLabel resetLabel = new JLabel("RESET");
  private JLabel instrucLabel = new JLabel("INSTRUCTIONS");
  private JLabel creditsLabel = new JLabel("CREDITS");
  private JLabel quitLabel = new JLabel("QUIT");

  private JButton twoPlayerButton;

  private Handler handler = new Handler();

  private boolean isWhiteTurn = false;
  private boolean isGamePlaying = false;
  private boolean AIPlaying = false;

  private String move = "";
  private String toFlip [] = new String[64];
  private String[][] content = new String[8][8];

  private SoundClip backgroundClip = new SoundClip("audio/Day_in_the_sun.wav", 0);
  private SoundClip clickClip = new SoundClip("audio/button-09.wav", 1);

  private JPanel whitePanel;
  private JPanel blackPanel;

  public ReversiFrame(){
    backgroundClip.start();
    mainPanel = new BackgroundPanel();
    mainPanel.setBackground(new Color(0,0,0));
    mainPanel.setLayout(null);
    add(mainPanel);

    playerVsPlayer.setBounds(147,232, 111,144);
    mainPanel.add(playerVsPlayer);

    playerVsComputer.setBounds(353,232, 111,144);
    mainPanel.add(playerVsComputer);

    resetLabel.setBounds(268, 513, 200, 50);
    resetLabel.setForeground(new Color(20,20,20));
    resetLabel.setFont(new Font("Impact", Font.PLAIN, 30));
    mainPanel.add(resetLabel);

    instrucLabel.setBounds(220, 563, 200, 50);
    instrucLabel.setForeground(new Color(20,20,20));
    instrucLabel.setFont(new Font("Impact", Font.PLAIN, 30));
    mainPanel.add(instrucLabel);

    creditsLabel.setBounds(257, 623, 200, 50);
    creditsLabel.setForeground(new Color(20,20,20));
    creditsLabel.setFont(new Font("Impact", Font.PLAIN, 30));
    mainPanel.add(creditsLabel);

    quitLabel.setBounds(277, 678, 200, 50);
    quitLabel.setForeground(new Color(20,20,20));
    quitLabel.setFont(new Font("Impact", Font.PLAIN, 30));
    mainPanel.add(quitLabel);

    musicLabel.setBounds(10, 550, 70, 70);
    mainPanel.add(musicLabel);

    soundLabel.setBounds(10, 630, 70, 70);
    mainPanel.add(soundLabel);

    resetLabel.addMouseListener(handler);
    instrucLabel.addMouseListener(handler);
    creditsLabel.addMouseListener(handler);
    quitLabel.addMouseListener(handler);
    playerVsPlayer.addMouseListener(handler);
    playerVsComputer.addMouseListener(handler);
    musicLabel.addMouseListener(handler);
    soundLabel.addMouseListener(handler);

    borderLabel.setBounds(622,37,694,695);
    mainPanel.add(borderLabel);

    gridPanel = new JPanel(new GridLayout(8,8, 5,5));
    gridPanel.setBackground(new Color(0,0,0));

    gridPanel.setBounds(630, 45, 680, 680);
    mainPanel.add(gridPanel);

    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        box[i][j] = new JPanel();
        box[i][j].addMouseListener(handler);
        box[i][j].setBackground(new Color(3, 139, 71));

        content[i][j] = "null";
        gridPanel.add(box[i][j]);
      }
    }

    box[3][3].add(new JLabel(new ImageIcon("images/white.png")));
    content[3][3] = "white";
    box[3][4].add(new JLabel(new ImageIcon("images/black.png")));
    content[3][4] = "black";
    box[4][3].add(new JLabel(new ImageIcon("images/black.png")));
    content[4][3] = "black";
    box[4][4].add(new JLabel(new ImageIcon("images/white.png")));
    content[4][4] = "white";

    gridPanel.repaint();
    gridPanel.revalidate();
    mainPanel.repaint();
    mainPanel.revalidate();
  }

  private int[] currentPosition = new int[2];

  public void checkPossibleMoves(){
    int availableMoves = 0;
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        currentPosition[0] = i;
        currentPosition[1] = j;
        String oponent = "";
        String you = "";
        if(isWhiteTurn && content[i][j].equals("white")){
          you = "white";
          oponent = "black";
        }else if(!isWhiteTurn && content[i][j].equals("black")){
          you = "black";
          oponent = "white";
        }

        if(content[i][j].equals(you)){
          for(int m = 1; m <= 8; m++){
            currentPosition[0] = i;
            currentPosition[1] = j;
            boolean passedOponent = false;
            while(true){
              currentPosition = ReversiBoardNavigator.updatePosition(m, currentPosition);
              if(ReversiBoardNavigator.addressIsOnMap(currentPosition)){
                if(content[currentPosition[0]][currentPosition[1]].equals(oponent)){
                  passedOponent = true;
                  continue;
                }else if(content[currentPosition[0]][currentPosition[1]].equals("null") && passedOponent){
                  content[currentPosition[0]][currentPosition[1]]  = "g";
                  box[currentPosition[0]][currentPosition[1]].removeAll();
                  if(isWhiteTurn == true){
                    box[currentPosition[0]][currentPosition[1]].add(new JLabel(new ImageIcon("images/whiteTrans.png")));
                    availableMoves++;
                  }else{
                    box[currentPosition[0]][currentPosition[1]].add(new JLabel(new ImageIcon("images/blackTrans.png")));
                    availableMoves++;
                  }
                  break;
                }else{
                  break;
                }
              }else{
                break;
              }
            }
          }
        }
      }
    }
    gridPanel.repaint();
    gridPanel.revalidate();
    mainPanel.repaint();
    mainPanel.revalidate();

    if(availableMoves == 0){
      String type;
      if(isWhiteTurn){
        type = "WHITE";
      }
      else{
        type = "BLACK";
      }
      JOptionPane.showMessageDialog(null, " No Available Moves for " +type+" piece");

      if(noMoreMoves()){
        endOfGame();
      }
      else{
        if(isWhiteTurn){
          isWhiteTurn = false;
          checkPossibleMoves();
        }else{
          isWhiteTurn = true;
          if(isGamePlaying && AIPlaying == false){
            checkPossibleMoves();
          }else if(isGamePlaying && AIPlaying == true){
            AImove(content);
            if(isGamePlaying){
              checkPossibleMoves();
            }
          }
        }
      }
    }
  }

  public boolean noMoreMoves(){
    if(ReversiBoardNavigator.checkPossibleMoves(convertToGameState(content), true).size() == 0 && ReversiBoardNavigator.checkPossibleMoves(convertToGameState(content), false).size() == 0 ){
      return true;
    }
    else{
      return false;
    }
  }

  public void resetGs(){
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if(content[i][j].equals("g")){
          content[i][j] = "null";
          box[i][j].removeAll();
        }
      }
    }
  }

  public synchronized void AImove(String[][] gameState){
    ReversiAI AI = new ReversiAI();
    int[] bestMove = AI.goAI(convertToGameState(gameState), blackScore, whiteScore);
    move = ""+bestMove[0]+bestMove[1];

    box[bestMove[0]][bestMove[1]].removeAll();
    box[bestMove[0]][bestMove[1]].add(new JLabel(new ImageIcon("images/white.png")));
    content[bestMove[0]][bestMove[1]] = "white";
    whiteScore++;
    flip(move);
    isWhiteTurn = false;

    if(whiteScore + blackScore == 64){
      isGamePlaying = false;
      endOfGame();
    }
  }

  public void flip(String move){
    String temp[] = new String[8];
    int count = 0;
    int ctr = 0;
    boolean flippable = false;
    currentPosition[0] = Integer.parseInt(move.charAt(0) + "");
    currentPosition[1] = Integer.parseInt(move.charAt(1) + "");

    for(int a = 1; a <= 8; a++){
      currentPosition[0] = Integer.parseInt(move.charAt(0) + "");
      currentPosition[1] = Integer.parseInt(move.charAt(1) + "");
      count = 0;
      flippable = false;
      temp = new String[8];
      while(true){
        currentPosition = ReversiBoardNavigator.updatePosition(a, currentPosition);
        if(ReversiBoardNavigator.addressIsOnMap(currentPosition)){
          if(isWhiteTurn && content[currentPosition[0]][currentPosition[1]] == "black"){
            temp[count++] = "" + currentPosition[0] + currentPosition[1];
          }else if(!isWhiteTurn && content[currentPosition[0]][currentPosition[1]] == "white"){
            temp[count++] = "" + currentPosition[0] + currentPosition[1];
          }else if(!isWhiteTurn && content[currentPosition[0]][currentPosition[1]] == "black"){
            flippable = true;
            break;
          }else if(isWhiteTurn && content[currentPosition[0]][currentPosition[1]] == "white"){
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

    for(int a = 0; a < ctr; a++){
      if(isWhiteTurn){
        content[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")] = "white";
        box[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")].removeAll();
        box[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")].add(new JLabel(new ImageIcon("images/white.png")));
        whiteScore++;
        blackScore--;
      }else if(!isWhiteTurn){
        content[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")] = "black";
        box[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")].removeAll();
        box[Integer.parseInt(toFlip[a].charAt(0) + "")][Integer.parseInt(toFlip[a].charAt(1) + "")].add(new JLabel(new ImageIcon("images/black.png")));
        blackScore++;
        whiteScore--;
      }
    }
  }

  public void resetAll(){
    mainPanel.remove(whitePanel);
    mainPanel.remove(blackPanel);
    gridPanel.setVisible(true);

    if(gameOver != null)
    mainPanel.remove(gameOver);

    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        box[i][j].removeAll();
        content[i][j] = "null";
      }
    }
    AIPlaying = false;
    isWhiteTurn = false;
    isGamePlaying = false;
    whiteScore = 2;
    blackScore = 2;

    box[3][3].add(new JLabel(new ImageIcon("images/white.png")));
    content[3][3] = "white";
    box[3][4].add(new JLabel(new ImageIcon("images/black.png")));
    content[3][4] = "black";
    box[4][3].add(new JLabel(new ImageIcon("images/black.png")));
    content[4][3] = "black";
    box[4][4].add(new JLabel(new ImageIcon("images/white.png")));
    content[4][4] = "white";

    playerVsPlayer.setIcon(new ImageIcon("images/playervsplayer.jpg"));
    playerVsComputer.setIcon(new ImageIcon("images/playervscomputer.jpg"));
    playerVsPlayer.addMouseListener(handler);
    playerVsComputer.addMouseListener(handler);

    gridPanel.repaint();
    gridPanel.revalidate();
    mainPanel.repaint();
    mainPanel.revalidate();
  }

  public int[][] convertToGameState(String[][] gameContent){
    int[][] gState = new int[8][8];

    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if(gameContent[i][j].equals("null")){
          gState[i][j] = 0;
        }else if(gameContent[i][j].equals("black")){
          gState[i][j] = -1;
        }else if(gameContent[i][j].equals("white")){
          gState[i][j] = 1;
        }
      }
    }
    return gState;
  }

  public void appearScore(){
    playerVsPlayer.setIcon(null);
    playerVsComputer.setIcon(null);
    playerVsPlayer.removeMouseListener(handler);
    playerVsComputer.removeMouseListener(handler);

    whitePanel = new JPanel();
    whitePanel.setBackground(new Color(235,235,235));
    whitePanel.setBounds(147,232,111,144);
    mainPanel.add(whitePanel);

    whiteScoreLabel = new JLabel("2", SwingConstants.CENTER);
    whiteScoreLabel.setFont(new Font("Impact", Font.PLAIN, 105));
    whitePanel.add(whiteScoreLabel);

    blackPanel = new JPanel();
    blackPanel.setBounds(353,232, 111,144);
    blackPanel.setBackground(new Color(54,54,54));
    mainPanel.add(blackPanel);

    blackScoreLabel = new JLabel("2", SwingConstants.CENTER);
    blackScoreLabel.setForeground(new Color(235,235,235));
    blackScoreLabel.setFont(new Font("Impact", Font.PLAIN, 105));
    blackPanel.add(blackScoreLabel);
  }

  private class Handler extends MouseAdapter {
    public void mouseEntered(MouseEvent event){
      if(event.getSource() == resetLabel)
        resetLabel.setForeground(new Color(200,200,200));
      if(event.getSource() == instrucLabel){
        instrucLabel.setForeground(new Color(200,200,200));
        gridPanel.setVisible(false);
        instructions.setBounds(630, 45, 680, 680);
        mainPanel.add(instructions);
      }
      if(event.getSource() == creditsLabel){
        creditsLabel.setForeground(new Color(200,200,200));
        credits.setBounds(630, 45, 680, 680);
        gridPanel.setVisible(false);
        mainPanel.add(credits);
      }
      if(event.getSource() == quitLabel)
        quitLabel.setForeground(new Color(200,200,200));
    }

    public void mouseExited(MouseEvent event){
      if(event.getSource() == resetLabel)
        resetLabel.setForeground(new Color(20,20,20));
      if(event.getSource() == instrucLabel){
        instrucLabel.setForeground(new Color(20,20,20));
        gridPanel.setVisible(true);
        mainPanel.remove(instructions);
      }
      if(event.getSource() == creditsLabel){
        creditsLabel.setForeground(new Color(20,20,20));
        gridPanel.setVisible(true);
        mainPanel.remove(credits);
      }
      if(event.getSource() == quitLabel)
        quitLabel.setForeground(new Color(20,20,20));
    }

    public void mouseClicked(MouseEvent event){
      for(int i = 0; i < 8; i++){
        for(int j = 0; j < 8; j++){
          if(event.getSource() == box[i][j]){
            if(content[i][j].equals("g") && isWhiteTurn == false){
              if(!isFXMute){
                clickClip.start();
              }

              box[i][j].removeAll();
              box[i][j].add(new JLabel(new ImageIcon("images/black.png")));
              content[i][j] = "black";
              move  = ""+i+j;
              blackScore++;
              flip(move);
              isWhiteTurn = true;
              resetGs();

              if(whiteScore + blackScore == 64){
                isGamePlaying = false;
                endOfGame();
              }

              if(isGamePlaying && AIPlaying == false){
                checkPossibleMoves();
              }else if(isGamePlaying && AIPlaying == true){
                if(ReversiBoardNavigator.checkPossibleMoves(convertToGameState(content), true).size() != 0){
                  AImove(content);
                }

                if(isGamePlaying){
                  checkPossibleMoves();
                }
              }
            }else if(content[i][j].equals("g") && isWhiteTurn == true && !AIPlaying){
              if(!isFXMute){
                clickClip.start();
              }
              box[i][j].removeAll();
              box[i][j].add(new JLabel(new ImageIcon("images/white.png")));
              content[i][j] = "white";
              move  = ""+i+j;
              whiteScore++;
              flip(move);
              isWhiteTurn = false;
              resetGs();

              if(whiteScore + blackScore == 64){
                isGamePlaying = false;
                endOfGame();
              }

              if(isGamePlaying)
              checkPossibleMoves();
            }

            whiteScoreLabel.setText("" + whiteScore);
            blackScoreLabel.setText("" + blackScore);

            gridPanel.repaint();
            gridPanel.revalidate();
            mainPanel.repaint();
            mainPanel.revalidate();
          }
        }
      }

      if(event.getSource() == playerVsPlayer){
        appearScore();
        checkPossibleMoves();
        isGamePlaying = true;
      }

      if(event.getSource() == playerVsComputer){
        AIPlaying = true;
        appearScore();
        checkPossibleMoves();
        isGamePlaying = true;
      }

      if(event.getSource() == resetLabel){
        if(isGamePlaying || whiteScore + blackScore == 64)
        resetAll();
      }
      if(event.getSource() == quitLabel){
        System.exit(0);
      }
      if(event.getSource() == musicLabel){
        if(isMute){
          musicLabel.setIcon(imageIcon);
          backgroundClip.resume();
          isMute = false;
        }else{
          musicLabel.setIcon(imageIcon2);
          backgroundClip.pause();
          isMute = true;
        }
        mainPanel.repaint();
        mainPanel.revalidate();
      }
      if(event.getSource() == soundLabel){
        if(isFXMute){
          soundLabel.setIcon(imageIcon1);
          isFXMute = false;
        }else{
          soundLabel.setIcon(imageIcon3);
          isFXMute = true;
        }
        mainPanel.repaint();
        mainPanel.revalidate();
      }
    }
  }

  public void endOfGame(){
    if(blackScore > whiteScore){
      if(AIPlaying){
        gameOver = new JLabel(new ImageIcon("images/playerwins.png"));
      }else{
        gameOver = new JLabel(new ImageIcon("images/player1wins.png"));
      }
    }else if(whiteScore > blackScore){
      if(AIPlaying){
        gameOver = new JLabel(new ImageIcon("images/computerwins.png"));
      }else{
        gameOver = new JLabel(new ImageIcon("images/player2wins.png"));
      }
    }else{
      gameOver = new JLabel(new ImageIcon("images/draw.png"));
    }
    gameOver.setBounds(630, 45, 680, 680);
    gridPanel.setVisible(false);
    mainPanel.add(gameOver);
  }

/*****************************************************Minimax Algo****************************************************************************************/

  private class BackgroundPanel extends JPanel{
    private static final long serialVersionUID = 1L;
    public void paintComponent(Graphics page){
      super.paintComponent(page);
      page.drawImage((new ImageIcon("images/background2.jpg")).getImage(), 0, 0, null);
    }
  }
}
