package tiroparabolico;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abraham
 */
import java.awt.Image;
import java.awt.Toolkit;

public class Malo extends Base {

    private int cont;   //entero de contador
    private int velocidad;  //entero de velocidad
    private static int score;

    public Malo(int posX, int posY, int cont, int velocidad) {
        super(posX, posY);
        this.velocidad = velocidad;
        this.cont = cont;
        Image bat1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/Bat1.png"));
        Image bat2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/Bat2.png"));
        Image bat3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/Bat3.png"));
        (this.getImagenes()).sumaCuadro(bat1, 70);
        (this.getImagenes()).sumaCuadro(bat2, 70);
        (this.getImagenes()).sumaCuadro(bat3, 70);
    }
    //@param  es para conocer cual contador se pondra de tipo <code>int</code>

    public void setCont(int cont) {          //Metodo para asignar el contador
        this.cont = cont;
    }

    //@return    regresa el contador de tipo <code>TipoM</code>
    public int getCont() {                   //Metodo para obtener el contador
        return cont;
    }

    //@param  es para saber la velocidad que se asignara de tipo <code>Tipo1</code>
    public void setVelocidad(int velocidad) {  //Metodo para asignar la velocidad
        this.velocidad = velocidad;
    }

    //@return    regresa la velocidad de tipo <code>TipoM</code>
    public int getVelocidad() {                 //Metodo para obtener la velocidad
        return velocidad;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }
}
