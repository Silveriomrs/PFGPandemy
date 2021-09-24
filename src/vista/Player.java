/**  
* <p>Title: Player.java</p>  
* <p>Description: Reproductor de la simulación creada.
* Lee un registro y lo reproduce a la velocidad indicada con una barra de tiempos
* y aquellos otros controles necesarios para una correcta visualización y análisis
* del registro de modo visual.</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 3 ago. 2021  
* @version 1.0  
*/  
package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import modelo.DCVS;
import modelo.FondoPanel;

import java.awt.Dimension;
import java.awt.Font;

/**
 * <p>Title: Player</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 3 ago. 2021
 * @version versión
 */
public class Player extends JPanel implements ActionListener{
	private JFrame frame;
	private JButton btnPlayPause;
	private Timer timer;
	private final FondoPanel fondo = new FondoPanel("/vista/imagenes/histograma2.png");
	private TitledBorder tb;
	private JSlider slider;
	private JProgressBar progressBar;
	private JLabel lblBarraDeProgresin, lblEscalaDeTiempos, lblFecha, lblHora, lblX;
	private JFormattedTextField frmtdtxtfldHoraMinuto;
	private JDateChooser dateChooser;
	private GroupLayout groupLayout;
	private int contador;
	private int ultima;
	private Mapa mapa;
	private Leyenda leyenda;
	private DCVS historico;
	private boolean activo;														//Establece si el reproducctor esta activo o pausado.
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = 1L;


	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param width Ancho del panel de reproductor.
	 * @param height Alto del panel de reproductor.
	 * @param editable Indica cuando las propiedades de visualización como
	 * dimensiones u otras son editables o no lo son, true si lo son,
	 * false en otro caso.
	 */
	public Player(int width, int height, boolean editable) {
		super();
		activo = false;
		//Border: configuración de estilo
		setOpaque(false);
		setBackground(Color.LIGHT_GRAY);
		tb = BorderFactory.createTitledBorder("Reproductor");
		tb.setTitleColor(Color.BLUE);
		setBorder(tb);
		//Controles: creación.
		frame = new JFrame();
		btnPlayPause = new JButton("Reproducir");
		slider = new JSlider(0,2000,1000);
		slider.setBackground(new Color(255, 228, 225));
		progressBar = new JProgressBar();
		dateChooser = new JDateChooser();
		frmtdtxtfldHoraMinuto = new JFormattedTextField();
		//Etiquetas: Creación.
		lblBarraDeProgresin = new JLabel("Barra de progresión");	
		lblEscalaDeTiempos = new JLabel("Escala de tiempos:");
		lblX = new JLabel("x 50 mSec/día");
		lblFecha = new JLabel("Fecha:");
		lblHora = new JLabel("Hora:");
		frame.setContentPane(fondo);
		
		configuracion();
        repaint();
	}

	/**
	 * <p>Title: configuracion</p>  
	 * <p>Description: Agrupación del código a fin de mejorar la legibilidad 
	 * del código del constructor. </p>
	 */
	private void configuracion() {
		//Timer
		contador = 0;
		timer =  new Timer (1000, new Temporizador()); 
		
		btnPlayPause.addMouseListener(new BtnPlay());
		progressBar.setStringPainted(true);
		slider.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.BLACK));
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(250);
		slider.setMajorTickSpacing(500);
		slider.setPaintLabels(true);
		slider.setFont(new Font("Liberation Sans", Font.ITALIC, 10));
		slider.setPreferredSize(new Dimension(200, 14));
		slider.addChangeListener(new SliderListener());
		//Desactivar la edición de fechas.
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setEditable(false);
		dateChooser.addPropertyChangeListener(new DateChooserListener());
		

		//Establecimiento de los textos tooltips
		btnPlayPause.setToolTipText("Reproduce o Pausa la animación.");
		dateChooser.setToolTipText("Fecha representada");
		dateChooser.getCalendarButton().setToolTipText("Introducción de fecha concreta");
		slider.setToolTipText("Escala de tiempo en horas/segundo");	
		progressBar.setToolTipText("Porcentaje de progreso de la reproducción del registro.");
		lblFecha.setToolTipText("Ir a una fecha concreta");		
		lblHora.setToolTipText("Hora de la representación");
		frmtdtxtfldHoraMinuto.setToolTipText("Saltar a una hora concreta");
		frmtdtxtfldHoraMinuto.setText("Hora Minuto");	
		
		//Establecimiento de los Layouts
		setLO();
		
		frame.setTitle("Reproductor");											//Establecimiento del título.
		frame.setSize(344,310);													//Establecimiento de las dimensiones.
		frame.setResizable(false); 												//Dimesiones fijas.
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				//Comportamiento al cerrar el frame.
		frame.setAlwaysOnTop(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	@Override
	public void setVisible(boolean ver) {frame.setVisible(ver);}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Obtención del botón pulsado	
	}
	
	/**
	 * <p>Title: play</p>  
	 * <p>Description: Establece la Reprodución del historico con el mapa de las zonas y 
	 * la leyenda cargadas. </p> 
	 * @param mapa Mapa con las zonas implicadas.
	 * @param leyenda Leyenda representación de los grados de contagio.
	 * @param historico Datos historicos a reproductir.
	 */
	public void setPlay(Mapa mapa, Leyenda leyenda, DCVS historico) {
		//Activar visualizar mapa y leyenda.
		this.mapa = mapa;
		this.leyenda = leyenda;
		this.historico = historico;
		this.mapa.setVisible(true);
		this.leyenda.setVisible(true);
		ultima = this.historico.getRowCount() -1;
		progressBar.setMinimum(0);
		progressBar.setMaximum(ultima);
		//Configuración del rango de fechas.
		Date d1 = stringToDate((String) historico.getValueAt(0, 0));			//Obtener primera fecha.
		Date d2 = stringToDate((String) historico.getValueAt(ultima, 0));		//Obtener segunda fecha.
		activo = true;															//Activación temporal del player para evitar activación erronéa del búscador.
		dateChooser.setSelectableDateRange(d1, d2);								//Establece rango de fechas.
		dateChooser.setDate(d1);												//Establece la fecha de comienzo.
		activo = !activo;														//Vuelta al estado desactivado del reproductor.
	}
	
	/**
	 * <p>Title: play</p>  
	 * <p>Description: Reproduce los datos almacenados en una línea de datos</p> 
	 * @param linea Número de línea a leer de la entrada de datos.
	 */
	private void play(int linea) {
		String fila[] = historico.getFila(linea);								//Obtener fila.
		int columnas = historico.getColumnCount();
		//Actualizar fecha
		String f = (String) historico.getValueAt(linea, 0);						//Obtención de la fecha almacenada en la posición 0 de la línea.
		Date d = stringToDate(f);		
		frmtdtxtfldHoraMinuto.setText(d.getHours() + ":" + d.getMinutes());		//Establece la hora de la línea leída en el cuadro de texto.
		dateChooser.setDate(d);													//Establecimiento de la fecha leida para mostrar en el dateChooser.
		
		for(int j = 1; j < columnas; j++) {		
			int id = getID(historico.getColumnName(j));							//Obtener ID columna
			int nivel = getValor(fila[j]);										//Obtener valor									
			mapa.addZonaNivel(id, d, nivel);									//Otorgar nivel al mapa/zona
			this.updateUI();
		}
		//Si es la última línea -> parar reproducción.
		if(linea == ultima) {
			timer.stop();
			btnPlayPause.setText("Repetir");
			activo = !activo;
			dateChooser.getCalendarButton().setEnabled(!activo);				//Activa boton del dateChooser cuando la reproducción no está activa.
		}
		
		progressBar.setValue(linea);											//Actualización del la barra de progreso.
		//System.out.println("Linea: " + linea + "/" + ultima);
		
	}
	
	/**
	 * <p>Title: getLineaDate</p>  
	 * <p>Description: Realiza una búsqueda de una fecha concreta</p>
	 * Realiza la búsqueda utilizando el algoritmo  
	 * @param d Date contenedora de la fecha seleccionada.
	 * @return número de línea del historico de fechas que contiene dicha fecha, 
	 * la fecha inferior más cercana en otro caso.
	 */
	private int getLineaDate(Date d) {		
		int linea = 0;															//Línea candidata
		int inferior = 0;														//Límete inferior de búsqueda
		int superior = this.ultima;												//Límite superior de búsqueda
		int aux = superior/2;													//Punto medio de búsqueda.
		Date dAux;
		boolean encontrado = false;
		if(d != null) {
			while(!encontrado) {
				String f = (String) historico.getValueAt(aux, 0);				//Obtención de la fecha almacenada en la posición 0 de la línea.
				dAux = stringToDate(f);
				int resultado = dAux.compareTo(d);
				if(resultado < 0) {												//Si Aux es anterior a la fecha búscada
					inferior = linea = aux;										//Ajuste de margenes inferiores de la búsqueda.
					aux += (superior -aux)/2;									//Nuevo punto de búsqueda.
					if(aux == inferior) {encontrado = true;}					//Caso de no encontrado pero se tiene la mayor aproximación inferior. 
				}else if(resultado > 0) {										//Caso de ser posterior.
					superior = aux;												//Auste margen superior.
					aux = superior/2;
				}else if (resultado == 0){										//Caso fecha búscada.
					encontrado = true;
					linea = aux;
				}
			}		
		}
		return linea;
	}
	
	/**
	 * <p>Title: stringToDate</p>  
	 * <p>Description: Convierte una cadena de texto que contiene una fecha
	 * en un objeto Date</p> 
	 * @param fecha Grupo fecha/hora en formato: "dd/MM/yyyy hh:mm"
	 * @return Date con los valores leidos almacenados.
	 */
	@SuppressWarnings("finally")
	private Date stringToDate(String fecha){
		 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");	//Formato de la fecha.
		 Date date = null;
		 try { date = formato.parse(fecha);	}									//Conversión tipo de datos.
		 catch (ParseException ex) { ex.printStackTrace(); }
		 finally {return date;} 
	}

	/**
	 * <p>Title: getID</p>  
	 * <p>Description: Devuelve el valor entero almacenado como texto.</p> 
	 * @param s Cadena de texto que representa al número entero.
	 * @return valor del número entero. -1 en otro caso.
	 */
	private int getID(String s) {
		int id = -1;
		if(s != null) id = Integer.parseInt(s.split(" ")[0]);	
		return id;
	}
	
	/**
	 * <p>Title: getValor</p>  
	 * <p>Description: Convierte una cadena de texto, la cual contiene un valor
	 * númerico en un entero. </p>
	 * El número representado debe usar como separador decimales la coma ','. 
	 * @param v Valor a convertir de formato texto a int.
	 * @return el valor de la conversión. 0 en otro caso.
	 */
	private int getValor(String v) {
		int nivel = 0;
		Double vd = Double.parseDouble(v.replace(",", "."));					//Cambio de símbolo decimal y conversion a Double.
		nivel = vd.intValue()/10;												//Conversión a int y reducción a nivel.
		return nivel;
	}
	
	/**
	 * <p>Title: getFrame</p>  
	 * <p>Description: Devuelve el JPanel que contiene el reproductor,
	 * de esta manera puede facilitarse su inclusión dentro otros Frames o JPanels
	 * </p> 
	 * @return JPanel contendor de este reproductor y su configuración.
	 */
	public JPanel getFrame() {return this;}
	
	private class BtnPlay extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			String estado = btnPlayPause.getText();
			switch(estado){
				case ("Reproducir"):											//Estado pausado, muestra 'Reproducir'
					btnPlayPause.setText("Pausar");								//Estado siguiente: reproducir.
					timer.start();
					break;
				case ("Repetir"):												//Estado parado en final muestra y etiqueta 'Repetir'.
					btnPlayPause.setText("Pausar");								//Estado siguiente: reproducir.				
					contador = 0;
					timer.restart();
					break;
				default:														//Estado pausado/parado, muestra 'Reproducir'
					btnPlayPause.setText("Reproducir");							//Estado siguiente: Parado.
					timer.stop();						
			}		
			activo = !activo;
			dateChooser.getCalendarButton().setEnabled(!activo);				//Activa boton del dateChooser cuando la reproducción no está activa.
			
		}
	}
	
	private class DateChooserListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			int linea = contador;
			if(!activo && arg0.getPropertyName().equals("date") ) {
				Date f = dateChooser.getDate();									//Obtención del valor establecido.				
				linea = getLineaDate(f);
				
				String s = (String) historico.getValueAt(linea, 0);				//Obtención de la fecha almacenadala línea obtenida.
				Date d = stringToDate(s);
				
				System.out.println("Fecha búscada  : " + f.getDay() + "/" + f.getMonth() + "/" + f.getYear());		
				System.out.println("Fecha propuesta: " + d.getDay() + "/" + d.getMonth() + "/" + d.getYear());
				System.out.println("Línea calculada: " + linea);
	
				progressBar.setValue(linea);									//Actualización del la barra de progreso.
				contador = linea;												//Actualizar contador
				play(contador);
				if(d.compareTo(f) < 0) {dateChooser.setDate(d);	}				//Caso tener la fecha, actualiza con la más cercana inferior..	
			}
		}
	}
	
	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int valor = slider.getValue();									//Obtención del valor establecido.
			lblX.setText("x " + valor + " mSec/día");							//Mostrar valor de la escala en el slider.			
			timer.setDelay(valor);												//Establece el timer con el nuevo valor.
		}
	}
	
	private class Temporizador implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
	        contador++;
	        play(contador);
	    }
	}
	
	/**
	 * <p>Title: setLO</p>  
	 * <p>Description: Establece los LayOuts por defecto del reproductor.</p>
	 * Principalmente su función es aliviar de código innecesario las funciones 
	 * principales.
	 */
	private void setLO() {
		groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEscalaDeTiempos)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblX))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
								.addComponent(lblBarraDeProgresin)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(2)
									.addComponent(lblHora)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(frmtdtxtfldHoraMinuto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(57)))
							.addGap(14))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblFecha)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
						.addComponent(slider, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(60)
					.addComponent(btnPlayPause, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(78, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBarraDeProgresin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEscalaDeTiempos)
						.addComponent(lblX))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblFecha)
						.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(frmtdtxtfldHoraMinuto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHora))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnPlayPause)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		//Layout de la imagen de fondo.
		GroupLayout gl_fondo = new GroupLayout(fondo);
		gl_fondo.setHorizontalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addComponent(this, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		);
		gl_fondo.setVerticalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addComponent(this, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
		);
	}
}
