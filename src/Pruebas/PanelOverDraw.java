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
import java.awt.Image;
 
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
 

	class PanelOverDraw extends JPanel {
		/** serialVersionUID*/  
		private static final long serialVersionUID = -4339205143748112115L;
		private Graphics2D pint;
		private BufferedImage img = null;
		int ancho, alto;
		
		/**
		 * <p>Title: Constructor de la clase</p>  
		 * <p>Description: Tiene la configuración del fondo y dimensiones de la
		 * pizarra..</p>
		 * @param ancho ancho del panel.
		 * @param alto alto del panel.
		 */
		public PanelOverDraw(int ancho, int alto) {
			this.setPreferredSize(new Dimension(ancho,alto));
			this.setSize(ancho,alto);
			this.addMouseListener(new SelectPointListener());
			//Carga imagen
			File imageFile = new File("/vista/imagenes/mapa-mudo-CCAA.jpg"); 					// guarda la imagen en un archivo
			try {img = ImageIO.read(getClass().getResourceAsStream(imageFile.toString())); }	// la carga en una BufferedReader
			catch (IOException e) {	e.printStackTrace();}
			img = resize(img);
			//Configuración PANEL			
			this.ancho = img.getWidth();
			this.alto = img.getHeight();
			// creamos una instancia graphics desde la imagen para pintar sobre ella
			pint = img.createGraphics();
	        pint.dispose();
		}
 
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(img,0,0,ancho,alto,null); 											// dibuja la imagen al iniciar la aplicacion
			g.dispose();
		}
		
		private void refrescar() {
			this.updateUI();
		}

		public BufferedImage resize(BufferedImage img) { 
			this.ancho = getWidth();
			this.alto = getHeight();
			Image tmp = img.getScaledInstance(ancho,alto, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
		    return dimg;
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
    private class SelectPointListener extends MouseAdapter{
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
            pint.drawLine(posX, posY -5, posX, posY +5);						// Línea vertical
            //Unión gráfica de dos puntos consecutivos.
            if(!primero) {
            	pint.drawLine(													// Dibujado de la línea entre punto anterior y actual.
            			(int)a.getX(),
            			(int)a.getY(),
            			posX,posY);
            }
            a = p;																// Establece el punto actual como el anterior para la siguiente línea.   
            System.out.println("P(" + posX + "," + posY + ")");
            primero = false;
            pint.dispose();
            refrescar();
        }
    }
    
	public static void main(String... args) {
		JFrame frame = new JFrame("Dibujo sobre imagen");
		int ancho = 800;
		int alto = 600;
		//Configuración FRAME
		frame.add(new PanelOverDraw(ancho - 1,alto -25));
		frame.setSize(ancho,alto);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
}