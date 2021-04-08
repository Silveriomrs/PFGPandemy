
package controlador;


/**
 * Almacena los distintos tipos de ayuda que puede mostrarse en funcion
 * el caso al que se haga referencia, ya sea por parametros o por mensajes de error.
 * 
 * @author (Silverio Manuel Rosales Santana) 
 * @version (2017.10.15)
 */
public class Ayuda
{
    /**
     * Constructor de la clase Ayuda
     */
    public Ayuda()
    {
        //Inicializacion de variables
    }

    /**
     * Metodo que muestra mensajes de ayuda en funcion del codigo de entrada.
     * 
     * @param ay ,código de la ayuda. 
     */
    public void imprimirAyuda(int ay)
    {
        switch (ay){
                case 0:System.out.println("Error, no hay suficiente memoria para procesar el algoritmo con esta cantidad de datos"); break;     
                case 1:System.out.println("Error, parametros incorrectos, [-h] para ayuda"); break;
                case 2:System.out.println("Error, el archivo de salida ya existe"); break;
                case 3:System.out.println("Error, S no esta en la periferia"); break;
                case 4:System.out.println("\nFORMATO VALIDO DE ARCHIVO:\n========================== \nNo estan permitidos espacios\n"
                                        + "Datos de entrada introducidos en una sola columna");
                       System.out.println("Un solo caracter 'R'");
                       System.out.println("Un solo caracter 'S', ademas debe estar en la periferia de la matriz");
                       System.out.println("Al menos 1 obstaculo, caracter 'O'");
                       System.out.println("No se admiten minusculas");
                       System.out.println("La cantidad de nodos debe coincidir con las dimensiones NxM especificadas");
                       System.out.println("*Todos los valores deben ser numeros tipo ENTERO (INT) mayores de cero");
                       System.out.println("*Ningun valor debe superar " + Integer.MAX_VALUE);
                       System.out.println("**********************************************************************\n"); break;        
                case 5:System.out.println("Error, debe haber al menos un obstaculo en los datos del fichero,");
                       System.out.println("\n un solo nodo de inicio R y un solo nodo final S que ademas debe ser periferico."); break;
                case 6:System.out.println("Error, fichero no encontrado"); break;
                case 7:System.out.println("\ndjisktra v2 (C) 2017 Silverio Manuel Rosales Santana\n");
                       System.out.println("SINTAXIS: djisktra [-t] [-h] [fichero_entrada] [fichero_salida]");
                       System.out.println("Opciones: ");
                       System.out.println("-t : Muestra la traza del algoritmo. Si es mostrada por consola introduce un retardo\n" + 
                                          "     entre cada paso. Ademas imprime (unicamente) por pantalla los\n" + 
                                          "     tiempo de lectura, ejecucion del algoritmo y de grabacion del resultado cuando corresponda.");
                       System.out.println("-h : Muestra informacion de ayuda, la ayuda no admite otro parametro.");
                       System.out.println("fichero_entrada  : Nombre del fichero de entrada");
                       System.out.println("fichero_salida   : Nombre del fichero de salida");break;
                case 8:System.out.println("Error al tratar de cerrar el fichero."); break;
                case 9:System.out.println("Todos los valores deben ser numeros tipo ENTERO (INT) mayores de cero.");
                       System.out.println("Ningun valor debe superar " + Integer.MAX_VALUE); break;
                case 10:System.out.println("Los unicos caracteres validos son O, S y R, no se admiten espacios."); break;
                case 11:System.out.println("Los datos de la cabecera solo pueden contener valores INT."); break;
                case 12:System.out.println("\nError, datos incorrectos dentro del archivo de entrada"); break;
        }
    }
    
}
