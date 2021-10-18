/**  
* <p>Title: Parametros.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 15 oct. 2021  
* @version 1.0  
*/  
package vista;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import modelo.IO;

/**
 * <p>Title: Parametros</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 15 oct. 2021
 * @version versión 1.0
 */
public class Parametros extends JPanel {
	/** serialVersionUID*/  
	private static final long serialVersionUID = -4920717884036584918L;
	private JTabbedPane paneles;	
	@SuppressWarnings("unused")
	private int numeroZonas;
	private String nombre;
	private int contadorPaneles;
	
	
	/**
	 * <p>Title: Parametros del proyecto</p>  
	 * <p>Description: Constructor de la clase que recibe los valores esenciales
	 * del proyecto, como por ejemplo número inicial de grupos, nombre del proyecto,
	 * etcétera. </p>  
	 * @param nombre Nombre del proyecto, los archivos que componente el proyecto estarán conformados por este nombre.
	 * @param numeroZonas Número de grupos de estudio del proyecto.
	 */
	public Parametros(String nombre, int numeroZonas) {
		this.contadorPaneles = 0;
		this.numeroZonas = numeroZonas;
		this.setNombre(nombre);
		setBackground(Color.YELLOW);
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(new Dimension(800, 600));
		paneles = new JTabbedPane(JTabbedPane.LEFT);
		add(paneles, BorderLayout.CENTER);
		configura();
	}
	
	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p> 
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame("Paneles de configuración");
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth();
	    int y = (int)m.getHeight()+15;
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setMaximumSize(new Dimension(2767, 2767));
		frame.setMinimumSize(new Dimension(800, 600));
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}
	
	private void configura() {
		//Creación de los paneles
		//Panel 1 debe ser de información y características del proyecto.
        JPanel panel1 = new Archivos(null,null);
        //Panel 2 de las características de la enfermedad.
        JPanel panel2 = new JPanel();
        //Panel 3 de las relaciones de las comunidades.
        JPanel panel3 = new TablaEditor(null, null);
        //Panel 4 de las características de cada zona.
        JPanel panel4 = new JPanel();
       
        
        //Añadir los paneles
        paneles.addTab("Proyecto", panel1);
        paneles.addTab("Enfermedad", panel2);
        paneles.addTab("Relaciones", panel3);
        paneles.addTab("Grupos", panel4);     
	    
	    iniciarPanel(panel1,false,"Propiedades del proyecto","Configuración de las propiedades del proyecto","/vista/imagenes/Iconos/proyecto_64px.png");
	    iniciarPanel(panel2,true,"Parámetros de la enfermedad","Definición de parámetros de la enfermedad","/vista/imagenes/Iconos/virus_64px.png");
	    iniciarPanel(panel3,false,"Matriz de relaciones","Definir la matriz de contactos (relaciones)","/vista/imagenes/Iconos/nodos_64px.png");
	    iniciarPanel(panel4,true,"Parámetros de zonas","Define los parámetros de los grupos (zonas)","/vista/imagenes/Iconos/parametros_64px.png");
        
	}
	
	
	private void iniciarPanel(JPanel panel, Boolean logo, String nombre, String tooltip, String icono) {
		//Configuración del borde.
		TitledBorder tb = BorderFactory.createTitledBorder(nombre);
		tb.setTitleFont(new Font(Font.DIALOG, Font.PLAIN, 26));
		tb.setTitleColor(Color.BLUE);
		panel.setBorder(tb);
		
		// Añadir el panel al JTabbedPane
		//paneles.addTab(nombre,panel);
		
        // Configurar Icono
		if(icono != null) paneles.setIconAt(contadorPaneles, IO.getIcon(icono,15,15));
		// Establece el tooltip
		if(tooltip != null) paneles.setToolTipTextAt(contadorPaneles, tooltip);
		// Establece el icono también como logo del panel.
		if(logo) {
			JLabel labelLogo = new JLabel("");
			labelLogo.setIcon(new ImageIcon(Archivos.class.getResource(icono)));
			labelLogo.setBounds(12, 12, 70, 75);
			panel.add(labelLogo);
		}
		
        // Los colores de fondo
        //paneles.setBackgroundAt(contadorPaneles, Color.yellow);

		contadorPaneles++;
	}
	

	/**
	 * Devuelve el nombre del proyecto
	 * @return Nombre del proyecto
	 */
	public String getNombre() {	return nombre;}

	/**
	 * Establece el nombre del proyecto
	 * @param nombre El nombre a establecer
	 */
	public void setNombre(String nombre) {this.nombre = nombre;	}
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: funciona para pruebas</p> 
	 * @param args argumentos
	 */
	public static void main(String[] args) {
		// Trabajo con un grupo inicial de 4.
		Parametros parametros = new Parametros("Proyecto de pruebas",4);
		parametros.abrirFrame();
	}

}
