/**  
* <p>Title: Grafica.java</p>  
* <p>Description: Representa gráficamente la progresión una cadena de valores.</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 18 ago. 2021  
* @version 1.0  
*/  
package vista;

import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * <p>Title: Grafica</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 18 ago. 2021
 * @version versión
 */
public class Grafica extends JPanel {
	ArrayList<Integer> historico;
	private int escala;

	/** serialVersionUID*/  
	private static final long serialVersionUID = 5076984446921820625L;

	/**
	 * Create the panel.
	 * @param escala Escala de representación.
	 */
	public Grafica(int escala) {
		this.setEscala(escala);
		historico = new ArrayList<Integer>();

	}
	
	/**
	 * <p>Title: addNivel</p>  
	 * <p>Description: Añade un nuevo nivel de manera secuencia.</p> 
	 * @param n Nivel a añadir.
	 */
	public void addNivel(int n) { historico.add(n);	}
	
	/**
	 * <p>Title: getJPanel</p>  
	 * <p>Description: Devuelve el Jpanel configurado para una represetanción
	 * adecuada.</p> 
	 * @return Panel con la representación gráfica.
	 */
	public JPanel getJPanel() {	return this; }
	
	
	/**
	 * Genera el poligono que representación de los datos almacenados..
	 * @return poligono creado a partir de los puntos dados.
	 */
	public Polygon getPoligono() {
		int size = historico.size();
		int[] polX = new int[size];
		int[] polY = new int[size];
		//Lectura y almacenamiento de coordenadas.
		for(int i=0; i<size;i++) {												
			polY[i] = historico.get(i)/escala;									//Establece nivel
			polX[i] = polX[i-1] + escala;										//Establece separación X respecto a valor anterior.
		}	
		return new Polygon(polX, polY, size);
	}

	/**
	 * @return El/la escala
	 */
	public int getEscala() {return escala;	}

	/**
	 * @param escala El/la escala a establecer
	 */
	public void setEscala(int escala) {	this.escala = escala;	}

}
