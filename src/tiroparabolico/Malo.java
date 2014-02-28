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
        Image bomb1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb1.png"));
        Image bomb2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb2.png"));
        Image bomb3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb3.png"));
        Image bomb4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb4.png"));
        Image bomb5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb5.png"));
        Image bomb6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb6.png"));
        Image bomb7 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb7.png"));
        Image bomb8 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Images/bomb8.png"));
        (this.getImagenes()).sumaCuadro(bomb1, 70);
        (this.getImagenes()).sumaCuadro(bomb2, 70);
        (this.getImagenes()).sumaCuadro(bomb3, 70);
        (this.getImagenes()).sumaCuadro(bomb4, 70);
        (this.getImagenes()).sumaCuadro(bomb5, 70);
        (this.getImagenes()).sumaCuadro(bomb6, 70);
        (this.getImagenes()).sumaCuadro(bomb7, 70);
        (this.getImagenes()).sumaCuadro(bomb8, 70);
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
    
        public void colision(int width, int height) {       //Metodo para saber si colisiono con el applet
        if (this.getPosX() + this.getWidth() >= height) {
            this.setPosX(30);
            this.setPosY(230);
        }
    }
}
