/**  
* Lista de imágenes usadas en el proyecto.
* El objetivo es aglutinar en esa clase las direcciones a todas las imgágenes
* e iconos usadas en la aplicación para su interfaz gráfica. De manera que
* sea más sencillo su mantenimiento pero sobretodo, permita generar SKINs para la aplicación.  
* @author Silverio Manuel Rosales Santana
* @date 20 sept 2022  
* @version 1.0  
*/  
package modelo;

/**
 * ImagesList
 * @author Silverio Manuel Rosales Santana
 * @date 20 sept 2022
 * @version versión 1.0
 */
public class ImagesList {
	private final static String img = "/vista/imagenes/";
	private final static String icon = "/vista/imagenes/Iconos/";
	
	/**
	 * En orden de permitir flexibilidad, no unificar los parámetros de fondos de pantalla
	 * ni aquellos que doten de independencia a una parte de la vista. 
	 **/
	
	
	/* Vista Principal */
	/** P_BCKGND Imagen de fondo de la pantalla principal */  
	public final static String P_BCKGND = img + "agua_800px.png";
	/** P_LOGO Icono de la aplicación cuando está minimizado */
	public final static String P_LOGO = img + "LogoUNED.jpg";
	
	/* Iconos de idiomas */
	/** FLAG_ES Bandera de España.*/  
	public final static String FLAG_ES = icon + "spain.png";
	/** FLAG_UK Bandera de Gran Betraña.*/  
	public final static String FLAG_EN = icon + "united-kingdom.png";
	/** FLAG_FR Bandera de Francia.*/  
	public final static String FLAG_FR = icon + "france.png";
	/** FLAG_DE Bandera de Alemania.*/  
	public final static String FLAG_DE = icon + "germany.png";
	/** FLAG_UR Bandera de Ucrania.*/  
	public final static String FLAG_UR = icon + "ukraine.png";

	/* Grafichas Chart*/
	/** BCKGND_CHART Imagen de fondo de la gráficas de línea.*/  
	public final static String BCKGND_CHART = img + "degradado.png";

	/* Vista Zonas */
	/** NO_IMG Imagen sustita de una imagen ausente o no cargada.*/  
	public final static String NO_IMG = icon + "sinImg_256px.png";

	/* Vista About */
	/** BCKGND_ABOUT Imagen de fondo de la ventana ABOUT*/  
	public final static String BCKGND_ABOUT = img + "Fondo_About.png";
	/** ABOUT_IMG_1 Imagen mostrada en la cabecera ABOUT.*/  
	public final static String ABOUT_IMG_1 = img + "Charlestoon.gif";
	
	/* Vista Pizarra */
	/** APPLY Icono de una mano sobre un botón para simbolizar la acción de aplicar*/  
	public final static String APPLY = icon + "aplicando_64px.png";
	
	/* Vista del reproductor Player */
	/** BCKGND_PLAYER Imagen de fondo del reproductor.*/  
	public final static String BCKGND_PLAYER = img + "degradado.png";
	
	/* Vista Archivos */
	/** BCKGND_FILES Imagen de fondo del módulo archivos*/  
	public final static String BCKGND_FILES = img + "lineasAzules.png";
	/** FILES_ICON_DEF Icono representativo dentro del módulo de archivos de la definición de enfermedad.*/  
	public final static String FILES_ICON_DEF = icon + "adn_64px.png";
	/** FILES_ICON_MAP Icono representativo dentro del módulo de archivos de los grupos de población.*/  
	public final static String FILES_ICON_MAP = icon + "spain_128px.png";

	/* Vista Parámetros Proyecto */
	/** BCKGND_PARAMETERS Imagen de fondo para la vista de parámetros de la enfermedad.*/  
	public final static String BCKGND_PARAMETERS = img + "agua_800px.png";

	/* Vista Editor de Tablas */
	/** BCKGND_PLAYER Imagen de fondo del editor de tablas.*/  
	public final static String BCKGND_TABLE_EDITOR = img + "degradado.png";
	/** TE_NEW_TABLE Icono de tabla nueva.*/  
	public final static String TE_NEW_TABLE = icon + "nuevaTabla_64px.png";
	/** TE_NEW_ROW Icono de nueva fila.*/  
	public final static String TE_NEW_ROW = icon + "nuevaFila_64px.png";
	/** TE_NEW_COL Icono de nueva columna.*/  
	public final static String TE_NEW_COL = icon + "nuevaColumna_64px.png";
	/** TE_DEL_ROW Icono de eliminar filas.*/  
	public final static String TE_DEL_ROW = icon + "eliminarFila_64px.png";
	/** TE_DEL_COL Icono de eliminar columnas.*/  
	public final static String TE_DEL_COL = icon + "eliminarCol_64px.png";
	/** TE_DEL_TABLE Icono de eliminar una tabla.*/  
	public final static String TE_DEL_TABLE = icon + "borrarTabla_64px.png";

	/* Vista de parámetros de la enfermedad */
	/** BCKGND_DEF Fondo de la vista definición de enfermedad.*/  
	public final static String BCKGND_DEF = img + "blueWind.png";
	/** DEF_ICON Icono de representación de la vista SIR.*/  
	public final static String DEF_ICON = icon + "motor_512px.png";
	/** ICON_PTE Icono de representación de PTE.*/  
	public final static String ICON_PTE = icon + "probabilidad_64px.png";
	/** ICON_DME Icono representación de DME.*/  
	public final static String ICON_DME = icon + "duracion_64px.png";
	/** ICON_DMI Icono representación de DMI.*/  
	public final static String ICON_DMI = icon + "duracionMedia_64px.png";
	/** ICON_IT Icono de tiempo inicial.*/  
	public final static String ICON_IT = icon + "startTime_64px.png";
	/** ICON_FT Icono de tiempo final.*/  
	public final static String ICON_FT = icon + "stopTime_64px.png";
	/** ICON_IP Icono de inmunidad permanente.*/  
	public final static String ICON_IP = icon + "inmunidad_64px.png";
	
	/* De propósito General */
	/** CLIPBOARD Icono representativo de un checklist.*/  
	public final static String CLIPBOARD = icon + "portapapeles_64px.png";
	/** FOLDER Icono representativo de abrir archivo.*/  
	public final static String FOLDER = icon + "carpeta_64px.png";
	/** DISK_1 Icono para la acción de guardar los datos.*/  
	public final static String DISK_1 = icon + "disquete_64px.png";
	/** DISK_2 Icono representativo de guardar los cambios.*/  
	public final static String DISK_2 = icon + "guardarCambios_64px.png";
	/** EXIT Icono de salida.*/  
	public final static String EXIT = icon + "salir_64px.png";
	/** NODES Representación de las relaciones */  
	public final static String NODES = icon + "nodos_64px.png";
	/** MAP Icono representativo del mapa*/  
	public final static String MAP = icon + "region_64px.png";
	/** PAL Icono de la paleta de colores*/  
	public final static String PAL = icon + "circulo-de-color_64px.png";
	/** PLAYER Icono del reproductor */  
	public final static String PLAYER = icon + "animar_128px.png";
	/** TABLE Icono del editor de tablas*/  
	public final static String TABLE = icon + "hoja-de-calculo_64px.png";
	/** PIZARRA Icono del editor gráfico para las zonas (Pizarra)*/  
	public final static String PIZARRA = icon + "editorGrafico_128px.png";
	/** BOOKS Icono de varios libros apoyados para representar documentación*/  
	public final static String BOOKS = icon + "archivo_64px.png";
	/** OK Icono de validación OK */  
	public final static String OK = icon + "ok_64px.png";
	/** CLEAN Icono para limpiar la pantalla */  
	public final static String CLEAN = icon + "limpiar_64px.png";
	/** EDIT Icono de editar una tabla.*/  
	public final static String EDIT_TABLE = icon + "editarTabla_64px.png";
	/** DELETE Icono de borrar los datos contenidos.*/  
	public final static String DELETE = icon + "borrar_64px.png";
	/** MANDATORY Icono de opción obligatoria. Icono de aviso.*/  
	public final static String MANDATORY = icon + "obligatorio2_64px.png";
	/** PRINTER Icono de representación del dispositivo de impresión*/  
	public final static String PRINTER = icon + "impresora_64px.png";

}
