import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TTT_MethodTesterUpdated
{
    public static void main(String[]args) //main method
    {
        JFrame window = new JFrame("Tic Tac Toe by Aarnav Noble");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        window.getContentPane().add(new TTT_Game_Updated());
        window.setBounds(500,500,750,750);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}