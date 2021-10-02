/**
 * 
 */
package modelo;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Esta subclase establece una imagen de fondo al panel.
 * @author Silverio Manuel Rosales Santana
 * @date 10/05/2021
 * @version 1.0
 */
public class FondoPanel extends JPanel{
	/**
	 * Serialización de la clase.
	 */
	private static final long serialVersionUID = -6096941937803410903L;
	private Image imagen;
	private String ruta;
	
	/**
	 * Carga una imagen especificada en la ruta pasada por parámetro.
	 * @param nombreImagen ruta y nombre de la imagen a cargar.
	 */
	public FondoPanel(String nombreImagen) {
		ruta = nombreImagen;
	}
	
	@Override
	public void paint(Graphics g) {
		if(ruta != null) {
			imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
			g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
			setOpaque(false);
			super.paint(g);
		}
	}
	
	/**
	 * <p>Title: getFondo</p>  
	 * <p>Description: Devuelve el JPanel con su imagen de fondo.</p> 
	 * @return La imagen incrustada en el panel.
	 */
	public JPanel getFondo() {return this;}
}
