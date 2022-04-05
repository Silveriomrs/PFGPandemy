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
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import controlador.ControladorModulos;
import modelo.DCVS;
import modelo.FondoPanel;
import modelo.Labels;
import modelo.TypesFiles;

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
	/** serialVersionUID*/  
	private static final long serialVersionUID = 1L;
	private ControladorModulos cm;
	
	private JFrame frame;
	private JButton btnPlayPause;
	private Timer timer;
	private final FondoPanel fondo = new FondoPanel("/vista/imagenes/degradado.png");
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
	private DCVS historico;
	private boolean activo;														//Establece si el reproducctor esta activo o pausado.
	private Date d1,d2;

	/**
	 * <p>Title: </p>  
	 * <p>Description: Reproductor de secuencias en línea de tiempo </p>  
	 * @param cm Controlador de módulos, necesario para integrarse con el resto de la aplicación.
	 * @param mapa Vista del mapa donde se hará la representación gráfica secuenciada.
	 */
	public Player(ControladorModulos cm, Mapa mapa) {
		super();
		this.cm = cm;
		this.mapa = mapa;
		activo = false;
		//Border: configuración de estilo
		setOpaque(false);
		setBackground(Color.LIGHT_GRAY);
		tb = BorderFactory.createTitledBorder("Reproductor");
		tb.setTitleColor(Color.BLUE);
		setBorder(tb);
		//Controles: creación.
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
		//Frame
		frame = new JFrame();
		configuracion();
        repaint();
	}
	
	/**
	 * <p>Title: iniciarFrame</p>  
	 * <p>Description: Inicia las propiedades del marco. </p>
	 */
	private void iniciarFrame() {
		frame.setContentPane(fondo);
		frame.setTitle("Reproductor");											//Establecimiento del título.
		frame.setSize(344,310);													//Establecimiento de las dimensiones.
		frame.setResizable(false); 												//Dimesiones fijas.
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);					//Comportamiento al cerrar el frame.
		frame.setAlwaysOnTop(false);		
	}
	
	/**
	 * <p>Title: clear</p>  
	 * <p>Description: Elimina los datos temporales internos que pudieran estar
	 *  etablecidos.</p>
	 *  Función requerida antes de la carga de un nuevo proyecto. 
	 */
	public void clear() {
		contador = 0;
		ultima = 0;	
		historico = null;
		activo = false;
		d1  = null;
		d2 = null;
		frame.setVisible(false);
	}

	/**
	 * <p>Title: configuracion</p>  
	 * <p>Description: Agrupación del código a fin de mejorar la legibilidad 
	 * del código del constructor. </p>
	 */
	private void configuracion() {
		//Timer
		contador = 0;
		timer =  new Timer(1000, new Temporizador()); 
		
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
		iniciarFrame();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		mapa.updateUI();
	}
	
	@Override
	public void setVisible(boolean ver) {frame.setVisible(ver);}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Obtención del botón pulsado	
	}
	
	 /** <p>Title: setPosicion</p>  
	 * <p>Description: Establece la posición para el frame</p> 
	 * @param xPos Posición X relativa a la pantalla.
	 * @param yPos Posición Y relativa a la pantalla.
	 */
	public void setPosicion(int xPos, int yPos) {frame.setLocation(xPos,yPos);}
	
	private void setIFT() {
		String sIT = (String) cm.getModule(TypesFiles.DEF).getDataFromRowLabel(Labels.IT);
		String sFT = (String) cm.getModule(TypesFiles.DEF).getDataFromRowLabel(Labels.FT);
		contador = Integer.parseInt(sIT);
		ultima = Integer.parseInt(sFT);
	}
	
	private void setDates() {
		//Establecer fechas iniciales y final.
		//Obtener la fecha inicial de la etiqueta del histórico
		
		//Sino existe obtener la de creación del proyecto.
		
		//Obtener la fecha final búscando la etiqueta en el histórico.
		
		//Sino está buscar la última fecha de columna
		
		//Sino es válida, sumar a la fecha inicial los slots de tiempo.
		d1 = stringToDate(historico.getColumnName(1));							//Obtener primera fecha.
        d2 = new Date();
		Calendar c = Calendar.getInstance();
        c.setTime(d2);
        c.add(Calendar.DATE, ultima);
        d2 = c.getTime();
//		Date d2 = stringToDate(historico.getColumnName(ultima));				//Obtener segunda fecha.
	}
	
	/**
	 * <p>Title: play</p>  
	 * <p>Description: Establece la Reprodución del historico con el mapa de las zonas y 
	 * la leyenda cargadas. </p> 
	 */
	public void setPlay() {
		//Activar visualizar mapa y leyenda.
		this.historico = cm.getModule(TypesFiles.HST);
		//Leer tiempos iniciales y finales.
		setIFT();
		//Con los tiempos iniciales se configura la barra de progreso.
		progressBar.setMinimum(contador);
		progressBar.setMaximum(ultima);
		//Borrado de todas las gráficas dibujadas previamente.
		cm.getZonas().forEach((k,v)->{
			v.getGrafica().reset();
		});
		
		//Configuración del rango de fechas.
		setDates();
		activo = true;															//Activación temporal del player para evitar activación erronéa del búscador.
		dateChooser.setSelectableDateRange(d1, d2);								//Establece rango de fechas.
		dateChooser.setDate(d1);												//Establece la fecha de comienzo.
		activo = !activo;														//Vuelta al estado desactivado del reproductor.
	}
	
	/**
	 * <p>Title: play</p>  
	 * <p>Description: Reproduce los datos almacenados en una línea de datos</p> 
	 * @param pos Número de columna a leer de la entrada de datos.
	 */
	private void play(int pos) {
//		SimpleDateFormat formato = new SimpleDateFormat("hh:mm");				//Formato de la fecha.
		//Actualizar fecha
//		String f = (String) historico.getValueAt(pos, 0);						//Obtención de la fecha almacenada en la posición 0 de la línea.
//		Date d = stringToDate(f);	
//		frmtdtxtfldHoraMinuto.setText(formato.format(d));
//		dateChooser.setDate(d);													//Establecimiento de la fecha leida para mostrar en el dateChooser.
		int rows = cm.getModule(TypesFiles.HST).getRowCount();
//		System.out.println("Player > play > Pos: " + pos);
		for(int row = 0; row < rows; row++) {
			int id = getID("" + historico.getValueAt(row, 0));					//Obtener ID columna
			double nivel = getValor("" + historico.getValueAt(row, pos + 1));	//Obtener valor
			String serie = ("" + historico.getValueAt(row, 0)).split(" ")[0];
			mapa.addZonaNivel(id, serie,pos, nivel);							//Otorgar nivel al mapa/zona
			this.updateUI();
		}
	
		progressBar.setValue(pos);												//Actualización del la barra de progreso.
		
		//En caso de cierre del reproductor, pausar la reproducción y dejarlo listo para reanudar.
		if(!frame.isVisible()) {
			btnPlayPause.setText("Reproducir");									//Estado siguiente: Parado.
			timer.stop();
		}
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
				int resultado = 0;
				if(dAux != null) resultado = dAux.compareTo(d);
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
		 Date date = new Date();
		 try { date = formato.parse(fecha);	}									//Conversión tipo de datos.
		 catch (ParseException ex) {
			System.out.println("Player > stringToDate > Valor de fecha incorrecto: " + fecha );
		 }
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
		String txt = s.split(" ")[1];
		id = Integer.parseInt(txt.substring(1));
		return id;
	}
	
	/**
	 * <p>Title: getValor</p>  
	 * <p>Description: Convierte una cadena de texto, la cual contiene un valor
	 * númerico en un valor de tipo Double. </p>
	 * El número representado debe usar como separador decimales la coma ','. 
	 * @param v Valor a convertir de formato texto a double.
	 * @return el valor de la conversión. 0 en otro caso.
	 */
	private double getValor(String v) {
		double vd = 0.0;
		//Cambio de símbolo decimal y conversion a Double.
		vd = Double.parseDouble(v.replace(",", "."));
		return vd;
	}
	
	/**
	 * <p>Title: getPanel</p>  
	 * <p>Description: Devuelve el JPanel que contiene el reproductor,
	 * de esta manera puede facilitarse su inclusión dentro otros Frames o JPanels
	 * </p> 
	 * @return JPanel contendor de este reproductor y su configuración.
	 */
	public JPanel getPanel() {return this;}
	
	/**
	 * <p>Title: BtnPlay</p>  
	 * <p>Description: Clase controladora de los estados de la reproducción del 
	 *  player.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 6 dic. 2021
	 * @version versión 1.0
	 */
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
					setPlay();
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
//			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");	//Formato de la fecha.
			if(!activo && arg0.getPropertyName().equals("date") ) {
				Date f = dateChooser.getDate();									//Obtención del valor establecido.				
				linea = getLineaDate(f);
				
				String s = (String) historico.getValueAt(linea, 0);				//Obtención de la fecha almacenadala línea obtenida.
				Date d = stringToDate(s);
				progressBar.setValue(linea);									//Actualización del la barra de progreso.
				contador = linea;												//Actualizar contador
				play(contador);
				if(d != null && d.compareTo(f) < 0) {dateChooser.setDate(d);	}				//Caso tener la fecha, actualiza con la más cercana inferior..	
			}
		}
	}
	
	/**
	 * <p>Title: SliderListener</p>  
	 * <p>Description: Clase controladora de la barra de deslizamiento del
	 *  reproductor.</p> 
	 *  Su función principal es incrementar o reducir el tiempo de reproducción.
	 * @author Silverio Manuel Rosales Santana
	 * @date 6 dic. 2021
	 * @version versión 1.2
	 */
	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int valor = slider.getValue();										//Obtención del valor establecido.
			lblX.setText("x " + valor + " mSec/día");							//Mostrar valor de la escala en el slider.			
			timer.setDelay(valor);												//Establece el timer con el nuevo valor.
		}
	}
	
	/**
	 * <p>Title: Temporizador</p>  
	 * <p>Description: Temporizador encargado de realizar una reproducción a cada
	 *  intervalo de tiempo.</p>
	 *  Controla además cuando se debe parar la reproducción o termina su tarea
	 *   en función de los parámetros iniciales de tiempo inicial y tiempo final.
	 * @author Silverio Manuel Rosales Santana
	 * @date 6 dic. 2021
	 * @version versión 1.0
	 */
	private class Temporizador implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {  
			//Si es la última línea -> parar reproducción.
			if(contador < ultima) {
				play(contador);
				contador++;
			}else {		
				timer.stop();
				btnPlayPause.setText("Repetir");
				activo = !activo;
				dateChooser.getCalendarButton().setEnabled(!activo);				//Activa boton del dateChooser cuando la reproducción no está activa.
			}
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
