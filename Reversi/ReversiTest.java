import javax.swing.JFrame;


public class ReversiTest {
  public static void main(String[] args) {
    ReversiFrame reversiFrame = new ReversiFrame();
    reversiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    reversiFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    reversiFrame.setUndecorated(true);
    reversiFrame.setVisible(true);
    reversiFrame.setLocationRelativeTo(null);
  }
}
