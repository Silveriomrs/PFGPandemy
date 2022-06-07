/**  
* <p>Title: OperationsType.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 7 dic. 2021  
* @version 1.0  
*/  
package modelo;

/**
 * <p>Title: OperationsType</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 7 dic. 2021
 * @version versión 1.0
 */
public enum OperationsType {
	/** Operación de actualizar datos o vista.*/ 
	UPDATE("Actualizar"),
	/** Operación de abrir un componente o un fichero*/
	OPEN("Abrir"),
	/** Operación de eliminar un dato o fichero*/
	DELETE("Borrar"),
	/** Operación de editar un dato o fichero*/
	EDIT("Editar"),
	/** Archivo de proyecto que contiene el módelo PRJ*/  
	EXECUTE("Ejecutar");

    private final String display;
    private OperationsType(String s) { display = s; }

    @Override
    public String toString() { return display; }

}
