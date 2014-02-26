package tiroparabolico;

/**
 *
 * @author Oscar Abraham Rodriguez Quintanilla
 */
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Toolkit;


public class AppletExamen1 extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener {
   
    public AppletExamen1(){
            init();
            start();
        }
    
    private Bueno heroe;                //Objeto tipo Bueno
    private Graphics dbg;               //Objeto tipo Graphics
    private Image dbImage;              //Imagen para el doblebuffer    
    private long tiempoActual;          //Long para el tiempo del applet
    private boolean movimiento;         //Booleano si esta en movimiento
    private LinkedList<Malo> malos;     //LinkedList  de objetos Malo
    private boolean pausa;              //Booleando para pausa
    private int direccion;              //entero para la direccion
    private int cantMalos;              // entero cantidad de malos
    private boolean clic;               //Booleano para saber si se dio clic en el bueno
    private int tiempo;                 // entero para contar el tiempo que pasa
    private boolean desaparece;         // booleano para escribir desaparece    
    private SoundClip chHeroe;          //audio para el heroe
    private SoundClip chPared;          //audio para las paredes

    /**
     * Metodo <I>init</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {
        this.setSize(1000, 650);
        addKeyListener(this);
        direccion = 0;                  //Se inicializa a 0 la direccion (no se mueve)
        setBackground(Color.RED);     //fondo negra
        malos = new LinkedList();
        movimiento = false;             // al principi esta quirto
        heroe = new Bueno(0, 0);
        heroe.setPosX(this.getWidth() / 2 - (new ImageIcon(heroe.getImagen())).getIconWidth() / 2);   //posicion x del Bueno
        heroe.setPosY(this.getHeight() / 2 - (new ImageIcon(heroe.getImagen())).getIconHeight() / 2);    //posicion y del Bueno
        cantMalos = (int) (((Math.random() * 3) + 1) * 2) + 10;
        for (int x = 0; x < cantMalos / 2; x++) {    //murcielagos de arriba
            int posrX = (int) (20 + (Math.random() * (getWidth() - 80)));    //posision x random
            int posrY = (int) (Math.random() * (getHeight() / 4)) * (-1);    //posision y random
            int velocidad = (int) ((Math.random() * 3 + 1) + 3);                   //velocidad random
            Malo bat = new Malo(posrX, posrY, 0, velocidad);                 //nuevo objeto tipo Malo
            malos.add(bat);                                            //se agrega el objeto a la linked list
        }

        for (int x = cantMalos / 2; x < cantMalos; x++) {   //murcielagos de abajo
            int posrX = (int) (20 + (Math.random() * (getWidth() - 80)));    //posision x random
            int posrY = (int) ((Math.random() * (getHeight() / 4)) + this.getHeight());    //posision y random
            int velocidad = (int) ((Math.random() * 3 + 1) + 3);                   //velocidad random
            Malo bat = new Malo(posrX, posrY, 0, velocidad);                 //nuevo objeto tipo Malo
            malos.add(bat);                                            //se agrega el objeto a la linked list
        }
        addMouseListener(this);
        addMouseMotionListener(this);
        //URL eaURL = this.getClass().getResource("chocaHeroe.wav");
        chHeroe = new SoundClip("Sounds/chocaHeroe.wav");
        //URL choURL = this.getClass().getResource("chocaPared.wav");
        chPared = new SoundClip ("Sounds/chocaPared.wav");
    }

    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se
     * incrementa la posicion en x o y dependiendo de la direccion, finalmente
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     *
     */
    public void run() {
        while (true) {
            if (!pausa) {
                actualiza();
                checaColision();
            }
            repaint();    // Se actualiza el <code>Applet</code> repintando el contenido.
            try {
                // El thread se duerme.
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }
        }
    }

    /**
     * Metodo usado para actualizar la posicion de objetos elefante y raton.
     *
     */
    public void actualiza() {
        //if(movimiento){
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;

        //Actualiza la animación en base al tiempo transcurrido
        if (movimiento) {//Si se mueve se actualiza
            (heroe.getImagenes()).actualiza(tiempoActual);
        }
        if (!clic) {
            if (direccion == 1) {
                heroe.setPosX(heroe.getPosX() - 2);
            } else if (direccion == 2) {
                heroe.setPosX(heroe.getPosX() + 2);
            } else if (direccion == 3) {
                heroe.setPosY(heroe.getPosY() - 2);
            } else if (direccion == 4) {
                heroe.setPosY(heroe.getPosY() + 2);
            }
        }
        for (int x = 0; x < malos.size(); x++) {
            (((Malo) (malos.get(x))).getImagenes()).actualiza(tiempoActual);
        }
        //}

        for (int x = 0; x < malos.size() / 2; x++) { //murcielagos de arriba
            ((Malo) malos.get(x)).setPosY(((Malo) malos.get(x)).getPosY() + ((Malo) malos.get(x)).getVelocidad());
            //((Malo) malos.get(x)).setVelocidad(velocidad);
        }

        for (int x = malos.size() / 2; x < malos.size(); x++) {  // murcielagos de abajo
            ((Malo) malos.get(x)).setPosY(((Malo) malos.get(x)).getPosY() - ((Malo) malos.get(x)).getVelocidad());
            //((Malo) malos.get(x)).setVelocidad(velocidad);
        }

    }

    /**
     * Metodo usado para checar las colisiones del objeto elefante y raton con
     * las orillas del <code>Applet</code>.
     */
    public void checaColision() {
        heroe.colision(this.getWidth(), this.getHeight());        //Checa colision del heroe con el applet
        for (int x = 0; x < malos.size(); x++) {                //for para hacer lo mismo con cada objeto tipo MAlo
            Malo bat = (Malo) malos.get(x);
            {
                if (x < (malos.size() / 2)) {       //murcielagos de arriba
                    if (bat.getPosY() + bat.getHeight() > this.getHeight()) {
                        int pX = (int) (20 + (Math.random() * (this.getWidth() - 80)));
                        bat.setPosX(pX);  //posision x nueva
                        int pY = (int) (Math.random() * (this.getHeight() / 4)) * (-1);
                        bat.setPosY(pY);//posision y nueva
                        int vel = (int) ((Math.random() * 3 + 1) + 3);
                        bat.setVelocidad(vel); // velocidad nueva
                        chPared.play();
                    }
                } else if (x >= (malos.size() / 2)) {
                    if (bat.getPosY() < 0) {
                        int pX = (int) (20 + (Math.random() * (this.getWidth() - 80)));
                        bat.setPosX(pX);  //posision x nueva
                        int pY = (int) ((Math.random() * (this.getHeight() / 4)) + (this.getHeight()));
                        bat.setPosY(pY);//posision y nueva
                        int vel = (int) ((Math.random() * 3 + 1) + 3);
                        bat.setVelocidad(vel); // velocidad nueva
                        chPared.play();     // si choca con la pared se emite sonido
                    }
                }
            }
            if (bat.intersecta(heroe)) {
                chHeroe.play(); //si choca emite sonido
                ((Malo) malos.get(x)).setScore(((Malo) malos.get(x)).getScore() + 1); //se suma uno al score
                desaparece = true;
                tiempo = 10;
                if (x < malos.size() / 2) {
                    int pX = (int) (20 + (Math.random() * (this.getWidth() - 80)));
                    bat.setPosX(pX);  //posision x nueva
                    int pY = (int) (Math.random() * (this.getHeight() / 4)) * (-1);
                    bat.setPosY(pY);//posision y nueva
                    int vel = (int) (Math.random() * 3 + 1);
                    bat.setVelocidad(vel); // velocidad nueva
                } else {
                    int pX = (int) (20 + (Math.random() * (this.getWidth() - 80)));
                    bat.setPosX(pX);  //posision x nueva
                    int pY = (int) (Math.random() * (this.getHeight() / 4)) + (this.getHeight());
                    bat.setPosY(pY);//posision y nueva
                    int vel = (int) (Math.random() * 3 + 3) * (1);
                    bat.setVelocidad(vel); // velocidad nueva
                }
            }
        }
    }

    /**
     * Metodo <I>update</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint(Graphics g) {
        // Inicializan el DoubleBuffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la
     * tecla.
     *
     * @param e es el <code>evento</code> generado al presionar las teclas.
     */
    public void keyPressed(KeyEvent e) {
        // Presiono A
        if (e.getKeyCode() == KeyEvent.VK_A) {
            direccion = 1;
            movimiento = true;
            clic = false;
        } //Presiono B
        else if (e.getKeyCode() == KeyEvent.VK_D) {
            direccion = 2;
            movimiento = true;
            clic = false;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {//Presiono W
            direccion = 3;
            movimiento = true;
            clic = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {//Presiono S
            direccion = 4;
            movimiento = true;
            clic = false;
        }
    }

    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que
     * no es de accion.
     *
     * @param e es el <code>evento</code> que se genera en al presionar las
     * teclas.
     */
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla
     * presionada.
     *
     * @param e es el <code>evento</code> que se genera en al soltar las teclas.
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {  //dejo de presionar la tecla de arriba
            if (pausa) {
                pausa = false;
            } else {
                pausa = true;
            }
        }

    }

    /**
     * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>, heredado
     * de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint1(Graphics g) {
        if (heroe != null && malos != null) {
            //Dibuja la imagen en la posicion actualizada
            g.drawImage(heroe.getImagen(), heroe.getPosX(), heroe.getPosY(), this);
            for (int x = 0; x < malos.size(); x++) {        //Dibuja todos los objetos de tipo Malo
                g.drawImage(((Malo) malos.get(x)).getImagen(), ((Malo) malos.get(x)).getPosX(), ((Malo) malos.get(x)).getPosY(), this);
            }
            if (pausa) {
                g.setColor(Color.WHITE);
                g.drawString("" + heroe.getPAUSADO(), heroe.getPosX() - heroe.getWidth() / 7, heroe.getPosY() + (heroe.getHeight() / 2));
            }
            if (desaparece) {
                g.setColor(Color.WHITE);
                g.drawString("" + heroe.getDESAPARECE(), heroe.getPosX() - heroe.getWidth() / 5, heroe.getPosY() + (heroe.getHeight() / 2));
                tiempo--;
                if (tiempo < 0) {
                    desaparece = false;
                }
            }
            g.setColor(Color.WHITE);
            g.drawString("Puntaje: "+ ((Malo) (malos.get(0))).getScore(), 60, 60);
        } else {
            //Da un mensaje mientras se carga el dibujo	
            g.drawString("No se cargo la imagen..", 20, 20);
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (heroe.dentro(x, y)) { // se da clic dentro del heroe
            if (clic) {
                clic = false;
                movimiento = true;
            } else {
                clic = true;
                movimiento = false;
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }
    
}
