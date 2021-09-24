/**  
* <p>Title: Test.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 31 ago. 2021  
* @version 1.0  
*/  
package Pruebas;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
@SuppressWarnings({ "serial", "javadoc" })
public class Test extends JFrame {
	private Graphics2D pint;
	private BufferedImage img = null;
	
	public static void main(String... args) {new Test();}
 
	public Test() {
		//Configuración FRAME
		super("Dibujo sobre imagen");
		this.add(new PanelOverDraw());
		this.setSize(484,519);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
 
	public class PanelOverDraw extends JPanel {
  
		
		
		/**
		 * <p>Title: Constructor de la clase</p>  
		 * <p>Description: Tiene la configuración del fondo y demás.</p>
		 */
		public PanelOverDraw() {
			//Carga imagen
			File imageFile = new File("/vista/imagenes/mapa-mudo-CCAA.jpg"); 					// guarda la imagen en un archivo
			try {img = ImageIO.read(getClass().getResourceAsStream(imageFile.toString())); }	// la carga en una BufferedReader
			catch (IOException e) {	e.printStackTrace();}
			//Configuración PANEL
			this.setPreferredSize(new Dimension(484,409));
			addMouseListener(new SelectPointListener());
 
			// creamos una instancia graphics desde la imagen para pintar sobre ella
			pint = img.createGraphics();
			//Se crea el poligono a dibujar con sus atributos.
	        pint.setColor(Color.GREEN);
	        pint.fillRect(200,200,100,100);
	        pint.dispose();
		}
 
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(img,0,0,null); // dibuja la imagen al iniciar la aplicacion
		}
 
	}
	
	   /**
     * <p>Title: SelectPointListener</p>  
     * <p>Description: Realiza un seguimiento de los puntos que son seleccionados para
     * conformar el Poligono posteriormente</p>
     * Cada punto seleccionado se marca con una cruz y un punto en su centro, a
     * medida que se van añadiendo más puntos se van creando más líneas hasta 
     * finalizar la operación.  
     * @author Silverio Manuel Rosales Santana
     * @date 23 sept. 2021
     * @version versión
     */
    class SelectPointListener extends MouseAdapter{
    	private Point a;														// Punto inicial y final de una recta a dibujar.
    	boolean primero = true;
    	
    	/**
    	 * Dibuja las marcas de referencia de cada punto, las líneas de unión
    	 * cuando procede y añade cada punto nuevo a la lista correspondiente de
    	 * la clase padre.
    	 * @param e Evento del ratón.
    	 */
    	@Override
		public void mouseClicked(MouseEvent e) {
    		//Comprobación de pulsación de borrado de pizarra.
            Point p =  e.getPoint();      
            int posX = e.getX();
            int posY = e.getY();
            pint = img.createGraphics();
            //Dibuja una cruz en las coordenadas (X,Y)
            pint.setColor(Color.RED);
            pint.fillOval(posX -2,posY -2,4,4);									// Punto central
            pint.drawLine(posX -5,posY, posX +5, posY);							// Línea horizontal
            pint.drawLine(posX, posY -5, posX, posY +5);							// Línea vertical
            //Unión gráfica de dos puntos consecutivos.
            if(!primero) {
            	pint.drawLine(														// Dibujado de la línea entre punto anterior y actual.
            			(int)a.getX(),
            			(int)a.getY(),
            			posX,posY);
            }
            a = p;																// Establece el punto actual como el anterior para la siguiente línea.   
            System.out.println(primero + " " + "(" + posX + "," + posY + ")");
            primero = false;
            pint.dispose();
        }
    }
}