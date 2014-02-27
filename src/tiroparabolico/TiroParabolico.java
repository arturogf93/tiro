/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiroparabolico;

import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Abraham
 */
public class TiroParabolico{

    
    public TiroParabolico(){

    }
    
    public static void main(String[] args) throws IOException {
        AppletExamen1 variable;
        variable = new AppletExamen1();
        variable.setVisible(true);
        variable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
