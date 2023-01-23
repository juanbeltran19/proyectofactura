
package com.factura.dbutil;

import java.net.InetAddress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Properties;
import javax.swing.JOptionPane;


/**
 * Esta clase permite conectar a una base de datos Mysql, acces y ademas ejecutar sentencias DML
 */


final public class ConectarBD {
 
  //** Declaracion de variables
  private Connection conexion;
  
  private Statement sentencia;
   
  PreparedStatement stm;
  
  private boolean generarAuditorias = false;
  
  private String nombreTerminal;
  
  private String nombreUsuario;  
  
  private String fechaServidor;
  
  /** 
   *Constructor general que se conecta a la base de datos dependiendo de los parametros
   *
   *@param servidorNombre Nombre del servidor o direccion IP
   *@param nombreBD  nombre de la base de datos
   *@param usuario Usuario autorizado
   *@param  password
   *
   */
  
  public  ConectarBD(String servidorNombre,String nombreBD,String usuario,String password) throws Exception {
  	
  	 configurarConexion(servidorNombre,nombreBD,usuario,password);
  	
  }
  
   
  /** 
   *Constructor general que se conecta a la base de datos dependiendo de los parametros tomados a partir de un archivo
   *
   */
  
  public ConectarBD(Properties prop) throws Exception{
  	
      configurarConexion((String)prop.get("HostName"),(String)prop.get("BaseDatos"),(String)prop.get("UserName"),(String)prop.get("Password"));
    
   }
  
  
  public ConectarBD(String Alias_ODBC){
	
		try {
	
			//** Se carga el driver JDBC-ODBC
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	
		}catch (Exception e) {
	
			System.out.println("No se pudo cargar el puente JDBC-ODBC");
	
	    }
	    
	    try {
	
	    	//** Se establece conexion con la base de datos
	    	conexion=DriverManager.getConnection("jdbc:odbc:"+Alias_ODBC,"","");
	
	    	//** Se relaciona el objeto Statement con la base de datos
	    	sentencia=conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	
	    }catch (Exception e) {
	
	    	System.out.println("Error"+e);
	
	    }

}
	
  
  public void configurarConexion(String servidorNombre,String nombreBD,String usuario,String password) throws Exception{
  	 
  	 //** Se carga el driver para conectarse a la base de datos
  	 
  	 try {
  	 
  	 	Class.forName("com.mysql.jdbc.Driver").newInstance();
  	 
  	 }catch  (Exception e) {
  	 	System.out.println("Error"+e);
  	 }
  	 
  	    
    // Se conecta a la base de datos
    // Se crea un URL hacia la maquina y la base de datos
 	String url= "jdbc:mysql://" + servidorNombre + "/" + nombreBD; 
    
    //se crea la conexion a la base de datos
    conexion=DriverManager.getConnection(url,usuario,password);
    
    //conexion.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    
    sentencia=conexion.createStatement();//createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    
    generarAuditorias = true;
    
    setFechaServidor(); 
  }
  
  //Otros metodos
  
  
  
  /** 
   *Permite ejecutar una sentencia SQL de tipo DML
   *
   *@param sentenciaSQL Sentencia SQL que pueder ser un Insert, Update, Delete
   *
   */
  
   public void ejecutaSentencia(String sentenciaSQL) throws Exception {
     
      // Se ejecutan las sentencias DML que llegan como parametro
  	  sentencia.execute(sentenciaSQL);
  	  
		//Se valida de que se halla configurado las auditorias y se general las auditorias
  	 if (generarAuditorias) {
  	 
  	 	String tipoSentencia;
  	 	String tabla ="";
  	 	
  	 	sentenciaSQL = sentenciaSQL.replace('\'',' ');
  	 	
  	   	switch (sentenciaSQL.charAt(0)) {
  	 	  
  	 	 case 'I': 
  	 	    tipoSentencia	= "INSERCION";
  	 	    tabla = sentenciaSQL.substring(12,sentenciaSQL.indexOf("Values")-1).toUpperCase();
  	 	 break;
  	 	 
  	 	 case 'D':   
  	 	    tipoSentencia	= "ELIMINACION";
  	 	    tabla = sentenciaSQL.substring(11,sentenciaSQL.indexOf("Where")-1).toUpperCase();
  	 	 break;
  	 	 
  	 	 default :
  	 	    tipoSentencia	= "ACTUALIZACION";
  	 	    tabla = sentenciaSQL.substring(7,sentenciaSQL.lastIndexOf("Set")-1).toUpperCase();
  	 	 break;
  	 	}  
  	 	
  	 	if (nombreTerminal == null)
  	 	    nombreTerminal = getObtenerNombreMaquina();
  	 	    
  	 	StringBuffer sentenciaSQLAuditoria = 	new StringBuffer("Insert Into AuditoriasTransacciones values(null,'"+nombreUsuario+"','"+fechaServidor+"','"+nombreTerminal+"','"+
  	 	                                                          tabla+"','"+ tipoSentencia + "','"+sentenciaSQL+"')");  

                
  	     //Se inserta la sentecia	                                
  	    sentencia.execute(sentenciaSQLAuditoria.toString());
  	    
  	    }
  	  	  
  }
  
  /** 
   *Permite buscar un/unos registro(s) en la base de datos
   *
   *@param sentenciaSQL Sentencia SQL para buscar un registro
   *@return ResultSet Registro encontrado
   *
   */

  public  ResultSet buscarRegistro (String sentenciaSQL) throws Exception {
  	
  	
  	// Se hace la busqueda del registro en la B.D
  	ResultSet resultado = sentencia.executeQuery(sentenciaSQL);
  	
  	return resultado;
  
  }	
  
  
  public Connection getConexion() {
  	
  	 return conexion;
  }
  
  public Statement getSentencia(){
  	
  	 return sentencia;
  	
  }
  
  public void commit() throws Exception {
  	
  	 conexion.commit();
  }
  
  
  
  public void rollback() {
  	
  	 try {
  	 	
  	 	conexion.rollback();
  	 	
  	 } catch (Exception e) {
  	 	
  	 	System.out.println("Error "+ e);
  	 }
  }
  
  
  
   /**
	 * Guarda registro(s) en la Base de datos
	 *
	 * @param nombreTabla Nombre de la tabla a actualizar
	 * @param campos  Array de los nombres de los valores (datos) a almecenar en un tabla en la BD
	 * @param conMySQL Conexion de la Base de datos
	 * @param escribir true si desea mostrar mensajes desde el metodo; false en caso contrario
     *
	 */
 	
  	 final public boolean guardar(String nombreTabla,String[] campos,boolean escribir) throws  Exception { 
  	  
  	    boolean resultado = false;
  	
  	    String sql = generarSentenciaInsert(nombreTabla,campos);
  	  
  	    System.out.println(sql);
  	    
  	  	// Se inserta el registro en la base de datos
		ejecutaSentencia(sql);
		     
		// Se muestra el mensaje de insecin de exitosa
	    if (escribir)
	        escribir=true; 
	      //Mensaje("Registro Insertado","Informaci�n ",JOptionPane.INFORMATION_MESSAGE);                           
		
		resultado = true;
		
       
       
       return resultado; 	  
  	
  	}  
  
  
  	/**
	 * Guarda registro(s) en la Base de datos
	 *
	 * @param nombreTabla Nombre de la tabla a actualizar
	 * @param array  Array de los nombres de los valores (datos) a almecenar en un tabla en la BD
	 * @param conMySQL Conexion de la Base de datos
	 * @param escribir true si desea mostrar mensajes desde el metodo; false en caso contrario
     *
	 */
 	 final public boolean guardar(String nombreTabla,String[][] array,int numColumnas,boolean escribir) throws  Exception{ 
  	     
  	   boolean resultado = true;
  	
  	   for (int i=0; i<array.length;i++) {
  	  	 
  	  	   String campos[] = new String[numColumnas];
  	  	    
  	  	   // Se sacan las filas que se desean insertar
  	  	   for (int j=0;j<numColumnas;j++)
  	  	    
  	  	     campos[j] = array[i][j];
  	  	      
  	  	     // Se crea la expresion para insertar
  	  	     String sql = generarSentenciaInsert(nombreTabla,campos);
  	  	     
  	  	     System.out.println(sql);
  	         
  	    	 // Se inserta el registro en la base de datos
	         ejecutaSentencia(sql);

  	   }
		    // Se muestra el mensaje de inserci�n de exitosa
		    if (escribir)
		    	escribir = true; 
		       //Mensaje("Registro Insertado","Informaci�n ",JOptionPane.INFORMATION_MESSAGE);                           
		
		     return resultado;
	 
  	}  
  
  	/**
	 * Genera una sentencia DML de insercion de datos a partir de un array
	 *
	 * @param nombreTabla Nombre de la tabla a actualizar
	 * @param campos  Array de los nombres de los valores (datos) a almecenar en un tabla en la BD
	 * return String senetencia de insercion de datos
	 *
	 */
 	
  	final public String generarSentenciaInsert(String nombreTabla,String[] campos) {
  		 
  		 
	  	  String sql = "Insert Into "+nombreTabla+" Values(";
	  	  
	  	  // Se especifican los campos que llegan como parametro
	  	  for (int i=0;i<campos.length;i++) {
	  	    
	  	    sql = sql+campos[i];
	  	  
	  	     if (i < (campos.length-1))
	  	       sql += ",";
	  	     else 
	  	       sql += ")";
	  	     
	  	  }	 
	  	  
	  	  return sql;
	  	  
  	}  
  
  	/**
	 * Muestra una casilla de mensajes con el un titulo, mensaje y tipo de mensaje
	 *
	 * @param mensaje Mensaje a mostrar
	 * @param titulo  Titulo del mensaje
	 * @param tipo  Tipo de mensaje: 0 error, 1 Informacion o confirmacion, 2 Precaucion
	 *
	 */
 	 
    final public void Mensaje(String mensaje,String titulo, int tipo) {
    	
    	JOptionPane.showMessageDialog(null,mensaje,titulo,tipo);
    	
    }  
  	
  /** 
   *Permite validar si un registro se encuentra en la  base de datos
   *
   *@param nombreTabla Nombre de la tabla
   *@return boolean true: Se encontro false: No encontrado
   *
   */
   public boolean validarRegistro (String nombreTabla,String condicion)  {
  	
  	  boolean resultadoBoolean = false;
  	  
  	  try {
  	        String sentenciaSQL = "Select 'x' "+ 
  	                               "From "+nombreTabla+" "+condicion;
  	              
  	       	// Se hace la busqueda del registro en la B.D
		  	ResultSet resultado = sentencia.executeQuery(sentenciaSQL);
		  	
		  	if (resultado!=null)
		  	 
		  	  
		  	  if (resultado.next())
		        
		         resultadoBoolean = true;
		      
		       
	  } catch (Exception e) {}	
	  
	  
	  
	   return resultadoBoolean;       
   }  	
  	
  	
  /** 
   *Permite configurar el nombre de usuario para las auditorias
   *
   *@param nombreUsuario Nombre de usuario 
   *
   */

  public  void setNombreUsuario (String nombreUsuario){
  	
      this.nombreUsuario = nombreUsuario; 
  }
  
  /** 
   *Devuelve la direccion IP de una maquina
   *
   *@return String Direccion IP
   *
   */
  public static String getObtenerNombreMaquina() {
  	 
  	 String resultado = null;
  	 
  	 try {
  	 
  	    InetAddress addr = InetAddress.getLocalHost();
  	   
  	    resultado =  addr.getHostName();
  	 
  	 } catch (Exception e)  {
  	    
  	    System.out.println("Error " +e)	;
  	 	
  	 } 
  	 
  	 return resultado;
  }

  /** 
   *Devuelve la fecha del servidor
   *
   *@return String resultado
   *
   */
  public void setFechaServidor() {
  	 
  	 String sql = "Select now() as fecha";
  	 
  	 try {
			ResultSet result=buscarRegistro(sql);
			if(result.next()){
				fechaServidor = result.getString("fecha").toString();
				System.out.println(fechaServidor);
			}  	    
  	 
  	 } catch (Exception e)  {
  	    
  	    System.out.println("Error " +e)	;
  	 	
  	 } 

  } 
         	
  	
}


