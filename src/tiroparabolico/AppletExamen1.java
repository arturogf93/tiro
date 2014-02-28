package tiroparabolico;

/**
 *
 * @author Oscar Abraham Rodriguez Quintanilla
 */
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppletExamen1 extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    public AppletExamen1() throws IOException {
        init();
        start();
    }

    private Bueno heroe;                //Objeto tipo Bueno
    private Malo bomba;
    private Graphics dbg;               //Objeto tipo Graphics
    private Image dbImage;              //Imagen para el doblebuffer  
    private long tiempoActual;          //Long para el tiempo del applet
    private boolean movimiento;         //Booleano si esta en movimient
    private boolean bombamueve;
    private boolean pausa;              //Booleando para pausa
    private int direccion;              //entero para la direccion
    private SoundClip chCacha;          //audio para el heroe
    private SoundClip chFalla;          //audio para las paredes
    private int vidas;
    private int contcaidas;
    private FileWriter file;
    private PrintWriter out;
    private boolean guarda;
    private boolean carga;
    private File archivo;
    private FileReader fr;
    private BufferedReader br;
    private int arr[];
    private int vx;
    private int vy;
    private boolean gameover;
    private Image im_over;
    private boolean sound;
    private Image informacion;
    private boolean info;
    /**
     * Metodo <I>init</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() throws IOException {
        this.setSize(1000, 650);
        info = false;
        sound = true;
        gameover = false;
        vidas = 5;
        addKeyListener(this);
        direccion = 0;                  //Se inicializa a 0 la direccion (no se mueve)
        setBackground(Color.BLACK);     //fondo negra
        movimiento = false;             // al principi esta quirto
        heroe = new Bueno(0, 0);
        bomba = new Malo(30, 330, 0, 0);
        heroe.setPosX((this.getWidth() * 6) / 8 - (new ImageIcon(heroe.getImagen())).getIconWidth() / 2);   //posicion x del Bueno
        heroe.setPosY(this.getHeight() - (new ImageIcon(heroe.getImagen())).getIconHeight() - 2);    //posicion y del Bueno
        addMouseListener(this);
        addMouseMotionListener(this);
        //URL eaURL = this.getClass().getResource("chocaHeroe.wav");
        chCacha = new SoundClip("Sounds/chocaHeroe.wav");
        //URL choURL = this.getClass().getResource("chocaPared.wav");
        chFalla = new SoundClip("Sounds/chocaPared.wav");
        file = new FileWriter("hola.txt");
        out = new PrintWriter(file);
        archivo = new File("hola.txt");
        fr = new FileReader(archivo);
        br = new BufferedReader(fr);
        guarda = false;
        carga = false;
        arr=new int[10]; 
        if ((int)(Math.random() * 2)==1){
            vx = (int) (Math.random() * 3) + 17; 
            vy = -( (int)(Math.random() * 4) + 15);
        }
        else{
            vx = (int) (Math.random() * 3) + 10; 
            vy = -( (int)(Math.random() * 4) + 20);
        }
        URL gURL = this.getClass().getResource("Images/Creditos.png");
        im_over = Toolkit.getDefaultToolkit().getImage(gURL);
        URL iURL = this.getClass().getResource("Images/info.png");
        informacion = Toolkit.getDefaultToolkit().getImage(iURL);
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
                try {
                    actualiza();
                } catch (IOException ex) {
                    Logger.getLogger(AppletExamen1.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    public void actualiza() throws IOException {
        //if(movimiento){
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

        //Guarda el tiempo actual
        tiempoActual += tiempoTranscurrido;



        //Actualiza la animación en base al tiempo transcurrido
        if (bombamueve && !info) {
            (bomba.getImagenes()).actualiza(tiempoActual);
            vy++;
            bomba.setPosX(bomba.getPosX()+vx);
            bomba.setPosY(bomba.getPosY()+vy);
        }
        
        if (movimiento) {//Si se mueve se actualiza
            (heroe.getImagenes()).actualiza(tiempoActual);

        }
        heroe.setPosX(heroe.getPosX() + direccion);

        //Auqie empieza a guardad datos en el archivo
        if (guarda && !info) {
            //try {
                file = new FileWriter("hola.txt");
                out = new PrintWriter(file);
                out.println(bomba.getPosX());
                out.println(bomba.getPosY());
                out.println(heroe.getPosX());
                out.println(vy);
                out.println(vx);
                out.println(vidas);
                out.println(contcaidas);
                out.println(bomba.getScore());
                file.close();
            guarda=false;
        }
        //Aqui termina de guardar datos
        
        //Aqui carga los datos del archivo
        if (carga) {
                // Se abre del archivo 
                archivo = new File("hola.txt");
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                // Lectura del archivo
                String linea;
                int cont=0;
                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);
                    int foo = Integer.parseInt(linea);
                    arr[cont]=foo;
                    cont++;
                } 
              bomba.setPosX(arr[0]);
              bomba.setPosY(arr[1]);
              heroe.setPosX(arr[2]);
              vy=arr[3];
              vx=arr[4];
              vidas=arr[5];
              contcaidas=arr[6];
              bomba.setScore(arr[7]);
              bombamueve=true;
        }
        carga = false;
        if(heroe.intersecta(bomba)){
            bomba.setPosX(30);
            bomba.setPosY(330);
            bombamueve=false;
            if ((int)(Math.random() * 2)==1){
                vx = (int) (Math.random() * 3) + 17; 
                vy = -( (int)(Math.random() * 4) + 15);
            }
            else{
                vx = (int) (Math.random() * 3) + 10; 
                vy = -( (int)(Math.random() * 4) + 20);
            }
            if (sound){
                chCacha.play();
            }
            bomba.setScore(bomba.getScore()+2);
        }
        
        //cuando la bomba sale por abajo
        if(bomba.getPosY()>this.getHeight()) {
           
            bomba.setPosX(30);
            bomba.setPosY(330);
            bombamueve=false;
            if ((int)(Math.random() * 2)==1){
                vx = (int) (Math.random() * 3) + 17; 
                vy = -( (int)(Math.random() * 4) + 15);
            }
            else{
                vx = (int) (Math.random() * 3) + 10; 
                vy = -( (int)(Math.random() * 4) + 20);
            }
            contcaidas++;
             if (sound){
                chFalla.play();
            }
            if (contcaidas >=3){
                vidas--;
                contcaidas=0;
            } 
        }
        if (vidas <=0){
            gameover = true;
        }

    }

    /**
     * Metodo usado para checar las colisiones del objeto elefante y raton con
     * las orillas del <code>Applet</code>.
     */
    public void checaColision() {
        heroe.colision(this.getWidth(), this.getHeight());        //Checa colision del heroe con el applet
        if (direccion !=0) {
            movimiento = true;
        } else {
            movimiento = false;
        }

        //bomba.colision(this.getHeight(), this.getHeight());
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
        // Presiono izq
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            movimiento = true;
            direccion = -(vidas*2);
        } //Presiono der
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movimiento = true;
            direccion = vidas*2;
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
        if (e.getKeyCode() == KeyEvent.VK_G) {  //dejo de presionar la tecla de arriba
            if (!guarda) {
                guarda = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {  //dejo de presionar la tecla de arriba
            if (!carga) {
                carga = true;
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_S) {  //dejo de presionar la tecla de arriba
            if (!sound) {
                sound = true;
            }
            else{
                sound = false;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_I) {  //dejo de presionar la tecla de arriba
            if (!info) {
                info = true;
            }
            else{
                info = false;
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            direccion = 0;
        } //Presiono der
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direccion = 0;
        }
        // Presiono izq
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
        if (heroe != null && bomba != null) {
            if (gameover){
                g.drawImage(im_over, 0, 30, this);
            }
            else{
                if (info){
                    g.drawImage(informacion, 0, 0, this);
                }
                else{
                    //Dibuja la imagen en la posicion actualizada
                    g.drawImage(heroe.getImagen(), heroe.getPosX(), heroe.getPosY(), this);
                    g.drawImage(bomba.getImagen(), bomba.getPosX(), bomba.getPosY(), this);

                    if (pausa) {
                        g.setColor(Color.WHITE);
                        g.drawString("" + heroe.getPAUSADO(), heroe.getPosX() - heroe.getWidth() / 7, heroe.getPosY() + (heroe.getHeight() / 2));
                    }
                    g.setColor(Color.WHITE);
                g.drawString("Puntaje: " + bomba.getScore(), 20, 50);
                g.drawString("Vidas    : " + vidas, 20, 65);                
                }                
            }
        } else {
            //Da un mensaje mientras se carga el dibujo	
            g.drawString("No se cargo la imagen..", 20, 20);
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (bomba.dentro(x, y)) { // se da clic dentro del heroe
            bombamueve = true;
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
