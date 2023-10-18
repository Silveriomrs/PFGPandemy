/**  
* Concentra las etiquetas relacionadas con operaciones. Su objetivo es homogenizar
*  las llamadas a dichas operaciones desde diferentes partes de la aplicación, así
*   como permitir desacoplar las accionos del idioma.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 oct. 2023  
* @version 1.2  
*/  
package modelo;

/** 
 * Esta clase enumerada contiene todas las operaciones que pueda usar la aplicación y cada una de sus partes.
 *  Las acciones tienen caracter general evitando redundancias, aunque en aquellas acciones donde sea necesario
 *  especificar el receptor o el disparador de la acción, tendrá creada una entrada específica (ej. los idiomas).
 * @author Silverio Manuel Rosales Santana
 * @date 17 oct. 2023
 * @version versión 2.0
 */
public enum OperationsType {
	//TODO: Añadir comentarios JavaDOC a las nuevas entradas.
	/** Acción de aplicar un cambio*/
	APPLY,
	/** Acción de notificación de cambios*/
	CHANGES,
	/** Acción de cerrar una ventana*/
	CLOSE,
	/** Eliminar un dato o fichero*/
	DELETE,
	/** Editar un dato o fichero*/
	EDIT,
	/** Editar gráfico*/
	EDIT_GRAPHIC,
	/** Editar Paleta de colores / Escala de valores*/
	EDIT_PAL,
	/** Ejecutar una acción*/  
	EXECUTE,
	/** Acción de salir*/
	EXIT,
	HELP_ABOUT,
	HELP_TABLES,
	HELP_USER_GUIDE,
	/** Importar tabla Vensim tipo A*/
	IMPORT_A,
	/** Importar tabla Vensim tipo B*/
	IMPORT_B,
	/** Selección idioma Español*/
	LANG_ES,
	/** Selección idioma Inglés*/
	LANG_EN,
	/** Selección idioma Francés*/
	LANG_FR,
	/** Selección idioma Alemán*/
	LANG_DE,
	/** Selección idioma Ucraniano*/
	LANG_UR,
	/** Nuevo elemento (Módulo u otro)*/
	NEW,
	/** Operación de abrir un componente o un fichero*/
	OPEN,
	/** Acción de reproducir en el player*/
	PLAY,
	/** Acción de pausar la reproducción en el player*/
	PAUSE,
	/** Acción de repetir la reproducción del player*/
	REPLAY,
	/** Guardar un fichero*/
	SAVE,
	/** Guardar como un fichero en donde se especifique*/
	SAVE_AS,
	/** Operación de actualizar datos o vista.*/ 
	UPDATE,
	VIEW_PRJ,
	VIEW_DEF,
	VIEW_GRP,
	VIEW_MAP,
	VIEW_PAL,
	VIEW_REL,
	/** Etiqueta final a eliminar cuando este todo hecho*/
	END;
    

}
