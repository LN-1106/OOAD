import Frontend.GUI.MainFrame;
import Frontend.GUI.LogIn.LoginFrame;
import Frontend.Compoent.Theme;

public class App { 
    public static void main(String[] args) {
        Theme.setup(false);
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}