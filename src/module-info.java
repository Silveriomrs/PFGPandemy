
/**
 * @author Silverio Manuel Rosales Santana
 * @date 02042021
 * @version 1.2
 *
 */
module pfgPandemy {
	exports controlador;
	exports modelo;
	exports vista;
	
	requires transitive java.desktop;
	requires com.opencsv;
	requires org.jfree.jfreechart;
	requires jcalendar;
	requires AbsoluteLayout.RELEASE190;				//Formato gráfico de algunos layouts.
	requires org.apache.commons.lang3;				//Correcta lectura de ficheros desde disco.
}