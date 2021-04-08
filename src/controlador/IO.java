/**
 * lectura de un fichero, linea a linea.
 * Contiene control de excepciones y el tratamiendo de las mismas cuando es posible.
 * Comprueba que el archivo de entrada tiene un formato correcto y en caso de realizar 
 * la operacion correctamente, almacena el resultado en un arreglo, donde los Obstaculos
 * tendran el valor -2, el nodo inicial el valor 0, el nodo final valor -1.
 * 
 * @author (Silverio Manuel Rosales Santana) 
 * @version (2017.10.15.0212)
 */
package controlador;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.BufferedReader;
import java.io.FileReader;



/**
 * Clase de Entrada y salida de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 
 * @version 1.0
 *
 */
public class IO
{   
    private int contadorO;
    private int contadorR;
    private int contadorS;
    private int filas, columnas, cuenta;
    private boolean ejecucionCorrecta;
    private int lectura[];
    private Ayuda ayuda;
    
    /**
     * Constructor de la clase IO
     */
    public IO()
    {
        contadorO = 0;
        contadorR = 0;
        contadorS = 0;
        filas = 0;
        columnas = 0;
        cuenta = 0;
        ejecucionCorrecta = true;
        ayuda = new Ayuda();
    }
    
    /**
     * Realiza la lectura, da formato a la lectura en forma de Matriz NxM (matriz de adyacencia) y devuelve la matriz de adyacencia con los datos.
     * Si la lectura no ha sido ejecutada correctametne sale al sistema con codigo de error -1
     * @param nombre del archivo a leer.
     * @return lectura Matriz en formato Filas X Columnas.
     */
    public int[] leerDatos(String nombre)
    {
        if(existeArchivo(nombre)){lecturaDisco(nombre);}
        else{
            ayuda.imprimirAyuda(6); //Error de fichero no encontrado
            System.exit(0); //Si no existe el archivo sale al sistema.
        } 
        //Si la operacion de lectura no ha sido correcta, no se termina
        if(!ejecucionCorrecta){System.exit(-1);}
        return lectura;
    }
    
    /**
     * Comprueba la posición pasada como parametro V, está en una posición de la periferia de la Matriz.
     * @param posicion posición como valor int.
     * @return TRUE si pertenece a la periferia, FALSE en caso contrario
     */
    private boolean esPeriferia(int posicion){
        boolean periferia = false;
        
        if((posicion < columnas) || (posicion%columnas == 0) || (((posicion-1)%columnas) == 0) || (posicion > (filas-1)*columnas) )
        {periferia = true;}
        else{periferia = false;}
        
        return periferia;
    }
    
    /**
     * Metodo que lee un archivo del disco. En caso de que no exista lanza un mensaje y el error de 
     * excepcion correspondiente. El metodo llevara el control de los parametros minimos y maximos permitidos.
     * Unicamente una 'R', unicamente una 'S' y en la periferia, al menos un caracter 'O'. Todo caracter en mayuscula.
     * No admite numeros negativos. Almacena el resultado en el campo lectura.
     * @param nombre del archivo.
     */
    private void lecturaDisco(String nombre)
    {
        BufferedReader br = null;
        FileReader fr = null;
        cuenta = 0; //almacena el numero de datos leidos totales;
                
        try{  
          fr = new FileReader(nombre);
          br = new BufferedReader( fr );
          String linea = null;
          
          //Leemos los dos primeros datos fuera del bucle principal (cabecera), ahorrando comprobaciones innecesarias
          //durante el resto del proceso
          for(int i = 0; i< 2; i++)
          {
              //
              if((linea = br.readLine()) != null){
                  int dato = tratarRSO(linea);
                  
                  //Si digito es negativo o igual a cero lanza error.
                  if(dato <= 0){
                      ayuda.imprimirAyuda(11);
                      throw new Exception("Error, valor de digito"); 
                  }
                  
                  //Tratamiento de los datos de la cabecera
                  if(cuenta == 0){
                       //almacena primer digito correspondiente a las filas.
                       filas = dato; 
                  }else{
                      //Almacena dato numerico correspondiente a columnas y crea el vector con la capacidad para almacenar el resto.
                      columnas = dato; //almacena el segundo digito correspondiente a las columnas.
                      int capacidad = filas*columnas +2;
                      lectura = new int [capacidad]; //se crea el arreglo que almacera todos datos
                      lectura[0] = filas;
                      lectura[1] = columnas;
                  }
                  
              }else{throw new Exception("Formato de archivo invalido");} //En caso de no numeros INT
              cuenta ++; //incrementa el contador
          }
                    
          //Cuerpo principal de lectura. Datos propios de la matriz
          while ((linea = br.readLine()) != null){ //lee mientras contenga un dato que no sea nulo.           
            int dato = tratarRSO(linea);			//Tratamiento del dato leido     
            if(dato == -9){throw new Exception("Error, caracter no valido");}	//En caso de error.            
            lectura[cuenta] = dato;					//Almacena el dato        
            cuenta ++; 								//incrementa el contador
          }

          /**Comprobacion de los parametros validos a traves de sus contadores **/

          if((contadorS != 1) || (contadorR != 1) || (contadorO <1) || (cuenta != (lectura.length))){
              throw new Exception("Formato de archivo invalido");}   
              
        } catch(FileNotFoundException e){
             ayuda.imprimirAyuda(6); //Error de fichero no encontrado
             System.exit(0); //Como no hay nada que cerrar, se sale directamente y evitamos problemas de tratar de cerrar en Finally
        } catch(OutOfMemoryError e){
            ayuda.imprimirAyuda(0); //Error de insuficiente memoria.
            ejecucionCorrecta = false;
        } catch(Exception e2){
            ayuda.imprimirAyuda(12);
            ayuda.imprimirAyuda(4); //Error de datos de entrada incorrectos.
            ejecucionCorrecta = false;
        } finally { //SIEMPRE SE EJECUTA, HAYA EXCEPCION O NO
            // Independientemente del estado de las operaciones, cerramos el fichero.
            try{br.close();}catch( Exception ex ){ayuda.imprimirAyuda(8);}
        } 
    }
    
    /**
     * Metodo para almacenar en un archivo la solucion del algoritmo.
     * @param nombreArchivo , nombre del archivo donde se almacenara la solucion.
     * @param datos de la solucion a almacenar.
     */
    public void grabarDatos(String nombreArchivo, String datos)
    { 
        FileWriter archivo = null; //especifica el archivo en que se va a escribir
        PrintWriter pw = null;
        
        try{
            //Comprobacion de que el archivo existe o no.
            if(!existeArchivo(nombreArchivo)){
              //Crear el archivo.
              archivo = new FileWriter(nombreArchivo);
              pw = new PrintWriter(archivo);
              //Se procede a escribir en el archivo
              pw.println(datos);                
            }else{ayuda.imprimirAyuda(2);} 
          } catch (IOException e){e.printStackTrace();
          } finally {
              try{
                  //aseguramos cerrar el archivo
                  if (null != archivo){archivo.close();};
              }catch (Exception e2){e2.printStackTrace();
              }
          }
    }
    
    /**
     * Tratamiento especial para los valores de nodos R, S y Obstaculos.
     * Ademas en el caso de S comprueba que sea periferico, en caso de que no lo sea, activara el flag correspondiente a FALSE,
     * para su tratamiento posterior al final de la lectura del archivo.
     * @param cad Nodo a tratar.
     * @return resultado El valor correspondiente, en caso de error devuelve -9.
     */
    private int tratarRSO(String cad)
    {
        int resultado;
        
        switch(cad){
            case ("O"): //lector de Objetos
                contadorO++;
                resultado = -2;
                break;
            case ("R"): //Lector de R.         
                contadorR++;
                resultado = 0;
                break;          
            case ("S"): //Lector de S.                
                contadorS++;
                resultado = -1;
                //Control de S para saber si esta en la periferia. En caso de no serlo resultado sera FALSE
                if(!esPeriferia(cuenta-1)){  //Se restan dos posiciones correspondientes a la cabecera
                    resultado = -9;
                    ayuda.imprimirAyuda(3); //S no esta en periferia.
                }
                break;
            default:
                try{
                    int num = Integer.parseInt(cad);
                    resultado = num;                
                    //si digito es negativo o cero lanza error.
                   if(num <= 0){
                       ayuda.imprimirAyuda(9); //Numero no valido
                       resultado = -9;
                    }       
                    
                } catch(Exception e2){
                     ayuda.imprimirAyuda(10); //De saltar la excepcion es por un caracter no reconocido.
                     resultado = -9;
                }
        }        
        return resultado;
    }
    
    /**
     * Comprobacion de que el nombre de archivo no existe actualmente en la ruta especificada o directorio actual.
     * @return TRUE si existe el archivo con dicho nombre, FALSE en caso contrario.
     */
    private boolean existeArchivo(String nombreArchivo)
    {        
        boolean exist = false;
        try{
            File f= new File(nombreArchivo);
            if(f.exists()){
                exist = true;
            }
        }catch(Exception e2){e2.printStackTrace();}
        
        return exist;
    }
}