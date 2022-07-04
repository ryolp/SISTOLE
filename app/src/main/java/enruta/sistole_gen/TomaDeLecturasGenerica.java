package enruta.sistole_gen;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Esta clase crea las validaciones y los campos a mostrar**/
public abstract class TomaDeLecturasGenerica {
	Globales globales;
	String is_lectAnt = "";
	Context context;
	
	DBHelper dbHelper;
	SQLiteDatabase db;
	
	static final int PREGUNTAS_CONSUMO_CERO=0;
	static final int PREGUNTAS_SIGUE_CORTADO=1;
	static final int PREGUNTAS_EN_EJECUCION=2;
	static final int PREGUNTAS_UBICACION_VACIA=3;
	static final int ANOMALIA_SEIS=4;
	static final int TIENE_MEDIDOR=5;
	static final int RETIRAR_SELLOS=6;
	
	static final int NO_SOSPECHOSA=0;
	static final int SOSPECHOSA=1;
	static final int SOSPECHOSA_MENSAJE=2;
	
	static final int AUSENTES=0;
	static final int LEIDAS=1;
	
	static final int ENTRADA=0;
	static final int SALIDA=1;
	static final int NO_REGISTRADOS=8;
	
	/**Variables que tienen que ver con sobrescribir**/
	static final int FOTOS=0;
	static final int MENSAJE=1;
	
	boolean guardarSospechosa=true;
	
	
	
	/**
	 * Longitud del registro
	 */
	int long_registro=500;
	
	int respuestaBusquedaCalles=R.string.msj_buscar_cant_calles_encontrados;
	
	MensajeEspecial mj_tiene_medidor;
	
	/** Arreglo en tipo de hash (para facilitar la busqueda) que contiene
	 * Key = Anomalia a ser ingresada
	 * Value = String de valores
	 */
	Hashtable <String, String> anomaliasCompatibles;
	public String ultimoBloqueCapturadoDefault="";
	
	TomaDeLecturasGenerica(Context context){
		this.context=context;
		globales= ((Globales) context.getApplicationContext());
		globales.tlc =new TodosLosCampos(getCamposBDAdicionales());
		
		mj_tiene_medidor=new MensajeEspecial("Tiene Medidor", TIENE_MEDIDOR);
		//mj_tiene_medidor.cancelable=false;
		creaTodosLosCampos();
	}
	
	
	
	/**
	 * Validacion de una lectura
	 * @param ls_lectAct
	 * @return Regresa el mensaje de error
	 */
	public abstract String validaLectura(String ls_lectAct);

	/**
	 * Nombra una foto
	 * @param globales Objeto en donde se encuentran todas las variables globales
	 * @param db Objeto que contiene la base de datos
	 * @param secuencial Secuencial de lectura
	 * @param is_terminacion Terminacion de la foto (En caso de tener)
	 * @return
	 */
	public abstract String getNombreFoto(Globales globales, SQLiteDatabase db, long secuencial, String is_terminacion ,  String ls_anomalia);
	
	public abstract  Vector<String> getInformacionDelMedidor( Lectura lectura);
	
	public abstract  MensajeEspecial getMensaje();
	
	/**
	 * Verifica si la anomalia ingresada es compatible con las anomalias que se ingresaron anteriormente
	 * @param anomaliaAInsertar Anomalia por Ingresar
	 * @param anomaliasCapturadas Anomalias Ingresadas anteriormente
	 * @return Regresa un objeto boleano indicando "verdadero" si es compatible o "falso" si no es compatible
	 */
	public abstract  boolean esAnomaliaCompatible(String anomaliaAInsertar, String anomaliasCapturadas);
	
	/**
	 * Devuelve el aviso que debe indicar la pantalla de input despues de insertar una anomalia
	 * @param anomalia Anomalia seleccionada
	 * @return El aviso con el que cuenta la anomalia, de no tener, se devuelve null
	 */
	public abstract ComentariosInputBehavior getAvisoMensajeInput(String anomalia);
	
	/**Realiza los cambios necesarios en la bd segun la anomalia seleccionada
	 * 
	 * @param Anomalia Anomalia seleccionada
	 */
	public abstract void RealizarModificacionesDeAnomalia(String anomalia, String comentarios);
	
	/**Deshace los cambios necesarios en la bd segun la anomalia seleccionada
	 * 
	 * @param Anomalia Anomalia borrada
	 */
	public abstract void DeshacerModificacionesDeAnomalia(String anomalia);
	
	/**
	 * Considera si se deberá mostrar un mensaje cuando se ingresa un consumo
	 * @param ls_lectAct lectura ingresada
	 * @return El mensaje que se deberá mostrar o de no haber mensaje, devolverá nulo
	 */
	public abstract MensajeEspecial mensajeDeConsumo(String ls_lectAct);
	
	/**
	 * Maneja lo que debe de hacer cierto pais con la seleccion de un mensaje
	 * @param me Mensaje especial que se mostró
	 * @param respuesta la respuesta de seleccionada
	 */
	public abstract void RespuestaMensajeSeleccionada(MensajeEspecial me, int respuesta);
	
	/**
	 * Genera un campo apartir de una clave
	 * @param campo Clave del campo a agregar
	 * @return Regresa el campo indicado, de no tener un campo para esa clave se regresa null
	 */
	public abstract ComentariosInputBehavior getCampoGenerico(int campo);
	
	/**
	 * Regresa los campos que tendrá la pantalla generica segun la anomalia dada
	 * @param anomalia Anomalia que pudiera tener campos genericos
	 * @return Los campos genericos de la anomalia, en caso de no tener regresa null
	 */
	public abstract int[] getCamposGenerico(String anomalia);
	/**
	 * Regresa los campos que tendrá la pantalla generica segun la anomalia dada
	 * @param anomalia
	 * @param subAnomalia
	 * @return
	 */
	public  int[] getCamposGenerico(String anomalia, String subAnomalia){
			return getCamposGenerico( anomalia);
	}

	
	/**
	 * Realiza las operaciones necesarias con los campos genericos presentados en pantalla
	 * @param bu_params Parametros regresados por la pantalla de input generico
	 */
	public abstract void regresaDeCamposGenericos(Bundle bu_params, String anomalia);
	
	/**
	 * Inicializa campos que no se encuentran en el archivo
	 */
	public abstract ContentValues getCamposBDAdicionales();
	
	/**
	 * Genera todos los campos necesarios para cada pais
	 */
	public abstract void creaTodosLosCampos();
	
	/**
	 * Al dar el nombre de la etiqueta, devuelve el valor que deberá tener. Esto se puede usar para partes del diseño que estan fijas y dificiles de recrear
	 * con programacion.
	 * 
	 * @param ls_etiqueta Nombre de la etique
	 */
	public abstract String obtenerContenidoDeEtiqueta(String ls_etiqueta);
	
	/**
	 * Regresa el formato de la etiqueta a insertar
	 * @return Regresa el formato a aplicar, si regresa nulo, no hay mensaje que mostrar
	 */
	public abstract FormatoDeEtiquetas getMensajedeRespuesta();
	
	/**
	 * Regresa el mensaje a mostrar en el espacio de Advertencias
	 * @return Mensaje a mostrar
	 */
	public abstract String getMensajedeAdvertencia();
	
	/**
	 * Realiza acciones posteriores despues de haber borrado la lectura
	 */
	public abstract void regresaDeBorrarLectura();
	
	/**
	 * Realiza los cambios que se le tienen que hacer a una anomalia dependiendo del pais
	 * @param ls_lect_act lectura que se tomó
	 */
	public abstract void cambiosAnomaliaAntesDeGuardar(String ls_lect_act);
	
	/**
	 * Establece las anomalias a repetir, si esta vacio, no hay nada que repetir
	 */
	public abstract void anomaliasARepetir();
	
	/**
	 *Establece las subanomalias a repetir, si esta vacio, no hay nada que repetir
	 */
	public abstract void subAnomaliasARepetir();
	
	/**
	 * Realiza las acciones propias del pais para repetir su anomalia
	 */
	public abstract void repetirAnomalias();
	
	
	/**
	 * Indica si es o no una anomalia de segunda visita
	 */
	public abstract boolean esSegundaVisita(String ls_anomalia , String ls_subAnom);
	
	/**
	 * Indica si debe moverse despues de haber capturado la anomalia seleccionada
	 */
	public abstract boolean avanzarDespuesDeAnomalia(String ls_anomalia , String ls_subAnom, boolean guardar);
	
	/**
	 * Indica si la anomalia debe insertarse o no
	 * @param ls_anomalia anomalia a insertarse
	 * @return devuelve el mensaje de error si no pasa la validacion. Si el mensaje esta vacio, quiere decir que pasó la validación
	 */
	public  abstract String validaAnomalia(String ls_anomalia);
	
	/**
	 * Regresa el prefijo con el que debe empezar un comentario, esta funcion solo funciona cuando se puede agregar una anomalia con multiples comentarios
	 * @param ls_anomalia anomalia a insertar
	 * @return el prefijo que deberá estar al principio del comentario
	 */
	public abstract String getPrefijoComentario(String ls_anomalia);
	
	/**
	 * Segun la lectura obtiene el consumo
	 * @param lectura
	 * @return
	 */
	public abstract long getConsumo(String lectura);
	
	/**
	 *  @param anomalia Anomalia que se esta insertando
	 * @param comentario 
	 * @return
	 */
	public abstract String validaCamposGenericos(String anomalia, Bundle comentario);
	
	/**
	 * Regresa el filtro segun el parametro indicado
	 * @param comoFiltrar Indique LEIDAS (1) para obtener todos los medidores con lectura y AUSENTES(0) para obtener todos
	 * los medidores sin lectura
	 * @return Regresa la cadena de filtrado
	 */
	public String getFiltroDeLecturas(int comoFiltrar){
		switch(comoFiltrar){
		case AUSENTES:
			//return " lectura=''  and anomalia='' " ;
			return " trim(tipoLectura)='' ";
		case LEIDAS:
			//return " (lectura<>'' or anomalia<>'') ";
			return " trim(tipoLectura)<>'' ";
		}
		
		return "";
	}
	
	/**
	 * Indica si debe moverse despues de haber capturado la anomalia seleccionada
	 */
	public abstract String getDescripcionDeBuscarMedidor(Lectura lectura, int tipoDeBusqueda, String textoBuscado);
	
	/**
	 * Realiza los cambios necesarios en la bd segun la anomalia seleccionada
	 * @param Anomalia Anomalia seleccionada
	 */
		public abstract void RealizarModificacionesDeAnomalia(String anomalia);
		
		/**
		 * Establece el consumo de la lectura actual
		 */
		public abstract void setConsumo();
		
		
		/**
		 * Hace los procesos necesarios despues de regresar de anomalias
		 * @param ls_anomalia Anomalia ingresada
		 * @param esAnomalia indica si la anomalia ingresada es anomalia o no
		 * @return De tenerlo, regresa un mensaje especial, de lo contrario, regresa null.
		 */
		public  MensajeEspecial regresaDeAnomalias(String ls_anomalia, boolean esAnomalia){
			return null;
		}
		
		/**
		 * Hace los procesos necesarios despues de regresar de anomalias
		 * @param ls_anomalia Anomalia ingresada
		 * @return De tenerlo, regresa un mensaje especial, de lo contrario, regresa null.
		 */
		public MensajeEspecial regresaDeAnomalias(String ls_anomalia){
			
			return regresaDeAnomalias( ls_anomalia, true);
		}

		/**
		 * Verifica si la anomalia a repetir se puede repetir con la lectura actual
		 * @return
		 */
		public  boolean puedoRepetirAnomalia(){
			return false;
		}
		
		/**
		 * Segun su configuracion, sobreescribe lo que exista en los registros de anomalias con lo que querramos modificar
		 * @param tipo Tipo de remplazo, fotos o mensajes
		 * @param anomalia Anomalia o sub anomalia correspondiente
		 * @param valor valor a remplazar
		 * @return Valor remplazado, o si no hay, el mismo valor
		 */
		public String remplazaValorDeArchivo(int tipo, String anomalia, String valor){
			return valor;
		}
		
		
		/**
		 * Establece los cambios necesarios en la anomalia antes de ser insertada
		 * @param anomalia Anomalia a ser insertada
		 */
		public abstract void cambiosAnomalia(String anomalia);
		
		/**
		 * Hace los cambios necesarios despues de borrar una anomalia
		 * @param anomaliaBorrada
		 */
		public  void cambiosAlBorrarAnomalia(String anomaliaBorrada){
			
		}
	
	public abstract long getLecturaMinima();
	public abstract long getLecturaMaxima(); 
	
	protected void openDatabase() {
		//dbHelper = new DBHelper(context);

		dbHelper= DBHelper.getInstance(context);
		db = dbHelper.getReadableDatabase();
	}

	protected void closeDatabase() {
		db.close();
		//dbHelper.close();
	}
	

	
	public String devolverConfiguracion(String llave){
		openDatabase();
		String valor="";
		Cursor c = db.query("config", null, "key='"+llave+"'", null, null, null,
				null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			valor= c.getString(c.getColumnIndex("value"));

		}

		c.close();
		
		closeDatabase();
		
		return valor;
	}
	
	public int devolverConfiguracionInt(String llave){
		int valor=0;
		try{
			valor = Integer.parseInt(devolverConfiguracion(llave));
		}
		catch(Throwable e){
			
		}
		
		return valor;
		
	}
	
	/**
	 * Devuelve el nombre del archivo segun el tipo
	 * @param tipo Tipo de archivo
	 * @return
	 */
	public String getNombreArchvio(int tipo){
		
		//Por default es el numero de CPL
				String ls_extension="TPL";
				String ls_archivo="";
				
		switch(tipo){
		case ENTRADA:
		case SALIDA:
			openDatabase();
			
			Cursor c= db.rawQuery("select value from config where key='cpl'", null);
			
			if (c.getCount()==0){
				c.close();
				closeDatabase();
					return"";
				}
			c.moveToFirst();
			if (c.getString(c.getColumnIndex("value")).trim().equals("")){
				c.close();
				closeDatabase();
					return "";
			}
			ls_archivo=c.getString(c.getColumnIndex("value"))+"."+ls_extension;
			c.close();
			closeDatabase();
			
			break;
		}
		
		
		return ls_archivo;
		
	}


	public void setTipoLectura(){
		if (!globales.tll.getLecturaActual().getLectura().trim().equals("")) {
			globales.tll.getLecturaActual().is_tipoLectura="0";
		}else  if (globales.tll.getLecturaActual().getLectura().trim().equals("") &&  !globales.tll.getLecturaActual().getAnomaliasCapturadas().equals("")){
			globales.tll.getLecturaActual().is_tipoLectura="4";
		}else{
			globales.tll.getLecturaActual().is_tipoLectura="";
		}
	}



	public int cambiaCalidadSegunTabla(String Anomalia, String subAnomalia) {
		// TODO Auto-generated method stub
		return globales.calidadDeLaFoto;
	}



	/**
	 * Indica si se debe serguir con la toma de una foto o no
	 * @return
	 */
	public boolean continuarConLaFoto() {
		// TODO Auto-generated method stub
		return true;
	}

	
	/**
	 *
	 * Aqui agrega las anomalias en código
	 * @param db Base de datos en la que agregamos las anomalias
	 */
	public void AgregarAnomaliasManualmente(SQLiteDatabase db) {
		
	}
	
	  /**
	   * Acciones necesarias despeus de realizar la carga del itinerario
	   */
	  public void accionesDespuesDeCargarArchivo(SQLiteDatabase db){
			
		}
		
	  /**
	   * Registros que por compatibilidad no concuerdan con los estandares definidos en siguientes versiones del CPL
	   * @param registro
	   */
		public void agregarRegistroRaro(SQLiteDatabase db, String registro){
			
		}
		
		/**
		 * Valida si es un registro raro o no
		 * @param registro
		 */
		public boolean esUnRegistroRaro(String registro){
			return false;
		}
	
		public String obtenerTituloDeEtiqueta(String ls_etiqueta) {
			if (ls_etiqueta.equals("campo0")){
				return "Póliza";
			}
			else if (ls_etiqueta.equals("campo1")){
				return "Tar.";
			}
			else if (ls_etiqueta.equals("campo2")){
				return "Ub.";
			}
			else if (ls_etiqueta.equals("campo3")){
				return "Edo.";
			}
			else if (ls_etiqueta.equals("campo4")){
				return "Est.";
			}
			else{
				return "";
			}
		}
		
		
		/**
		 * Regresa una lista de enteros con los archivos a transmitir
		 * @return Regresa la lista
		 */
		public int [] getArchivosATransmitir(){
			return getArchivosATransmitir(0);//Cualquier metodo, no importa
		}
		
		/**
		 * Regresa una lista de enteros con los archivos a transmitir segun el metodo
		 * @return Regresa la lista
		 */
		public int [] getArchivosATransmitir(int metodoDeTransmision){
			return new int[0];
		}
		
		
		/**
		 * Regresa un cursor con los datos que deben escribirse
		 * @param db Base de datos en la cual se extrae la info
		 * @param tipo archivo a transmitir
		 * @return Regresa el cursor, si es nulo, no hay contenido
		 */
		public Cursor getContenidoDelArchivo(SQLiteDatabase db, int tipo){
			return null;
		}


		/**
		 * Regresa los mensajes a mostrar durante la carga de un archivo generico
		 * @param tipo tipo archivo a transmitir
		 * @return Mensaje a mostrar, si se regresa vacio, es que no hay nada que mostrar
		 */
	public String regresaMensajeDeTransmision(int tipo){

		return "";
	}
	
	/**
	 * Regresa el tipo de opcion a ingresar en el servidor segun el archivo especificado
	 * @param tipo
	 * @return
	 */
	public int opcionAElegir(int tipo){
		return 0;
	}
	
	/**
	 * Regresa el resumen que se muestra en la pantalla principal
	 * @param db
	 * @return
	 */
public Vector <EstructuraResumen> getResumen(SQLiteDatabase db){
		
		Cursor c;
		  long ll_total;
		long ll_tomadas;
    	long ll_fotos;
    	long ll_restantes;
    	long ll_conAnom;
    	long ll_noRegistrados;
    	String ls_archivo;
    	
    	Vector <EstructuraResumen>resumen= new Vector<EstructuraResumen>();
		
		c= db.rawQuery("Select count(*) canti from Ruta", null);
    	c.moveToFirst();
    	ll_total=c.getLong(c.getColumnIndex("canti"));
    	
		try{
			c=db.rawQuery("Select value from config where key='cpl'", null);
    		c.moveToFirst();
   		 	ls_archivo=c.getString(c.getColumnIndex("value"));
		}
		catch(Throwable e){
			ls_archivo="";
		}
		
		c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='0'", null);
		 c.moveToFirst();
		 
		ll_tomadas=c.getLong(c.getColumnIndex("canti"));
		 c=db.rawQuery("Select count(*) canti from fotos", null);
		 c.moveToFirst();
    	ll_fotos=c.getLong(c.getColumnIndex("canti"));
    	c.close();
    	
    	 c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='4'", null);
    	 c.moveToFirst();
    	 ll_conAnom=c.getLong(c.getColumnIndex("canti"));
    	 c.close();
    	 
    	 c=db.rawQuery("Select count(*) canti from ruta where trim(tipoLectura)=''", null);
    	 c.moveToFirst();
    	 ll_restantes=c.getLong(c.getColumnIndex("canti"));
    	 c.close();
    	 
    	 c=db.rawQuery("Select count(*) canti from NoRegistrados", null);
    	 c.moveToFirst();
    	 ll_noRegistrados=c.getLong(c.getColumnIndex("canti"));
    	 c.close();
    	
    	//ll_restantes = ll_total-ll_tomadas ;
    	
//    	ls_resumen="Total de Lecturas " + ll_total +"\n" +
//    			"Medidores con Lectura " +  + ll_tomadas +"\n" +
//    			"Medidores con Anomalias "+  ll_conAnom +"\n" +
//    			"Lecturas Restantes "+  ll_restantes +"\n\n" +
//    			
//    			"Fotos Tomadas "+ ll_fotos +"\n\n" +
//    			
//    			"No Registrados "+ ll_noRegistrados;
//    	
//    	tv_resumen.setText(ls_resumen);
    	 
    	 float porcentaje=0;
    	 DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
    	 otherSymbols.setDecimalSeparator('.');
    	 otherSymbols.setGroupingSeparator(','); 
    	 DecimalFormat formatter = new DecimalFormat("##0.00", otherSymbols);
    	 
    	 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_total_lecturas), String.valueOf(ll_total)));
    	 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_fotos_tomadas), String.valueOf(ll_fotos)));
    	 resumen.add(new EstructuraResumen("", ""));
    	 porcentaje=  (((float)ll_restantes*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_lecturas_restantes),String.valueOf(ll_restantes),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_tomadas*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_medidores_con_lectura), String.valueOf(ll_tomadas),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_conAnom*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen(  context.getString(R.string.msj_main_medidores_con_anomalias),String.valueOf(ll_conAnom), formatter.format(porcentaje) +"%"));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	 if (globales.mostrarNoRegistrados)
    		 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_no_registrados), String.valueOf(ll_noRegistrados)));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	 return resumen;
		
	}


/**
 * Activa o desactiva opciones dependiendo del modo superusuario
 * @param esSuperUsuario
 */
	public void activacionDesactivacionOpciones(boolean esSuperUsuario){
		
	}
	
	
	/**
	 * Actividades que se hacen cuando entras
	 */
	public void procesosAlEntrar(){
		
	}
	
	/**
	 * Esta funcion regresa una busqueda generica
	 * @param tipo Tipo de filtro, Se encuentran estaticamente en esta clase y pueden ser las siguientes:
	 * 0 - No leidas
	 * 1 - Leidas
	 * 2 - Todas las lecturas
	 * @param filtro condicion a filtrar
	 * 
	 */
	public Vector <String> getBusquedaGenerica(int tipo, String filtro){
		openDatabase();
		Vector <String> lvs_vector = new Vector <String>();
		Cursor c;
		
		String select="min(cast(secuenciaReal as Integer))"+"||'*'||direccion  || '<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || CASE  WHEN cast(numEdificio as Integer)%2=0 THEN ' PARES' ELSE ' NONES' END || ' ('|| count(*) ||')' nombre";
		
		if (!filtro.trim().equals("")){
			filtro=" " + (tipo==TodasLasLecturas.TODAS_LAS_LECTURAS?" where ": " and ")+" upper(direccion) like '%"+filtro.toUpperCase()+"%' ";
		}
		if (tipo== TodasLasLecturas.LEIDA){
			 c=db.rawQuery("Select "+select+" from ruta where  " + globales.tdlg.getFiltroDeLecturas(TomaDeLecturasGenerica.LEIDAS) 			 		
						 +filtro +" group by direccion, cast(numEdificio as Integer)%2 order by cast(secuenciaReal as Integer)  asc", null);
		 }
		 else if (tipo== TodasLasLecturas.SIN_LEER) {
			 c=db.rawQuery("Select  "+select+"  from ruta where  " + globales.tdlg.getFiltroDeLecturas(TomaDeLecturasGenerica.AUSENTES) 			 		
						 +filtro + " group by direccion, cast(numEdificio as Integer)%2 order by cast(secuenciaReal as Integer)  asc", null);
			 
		 }
		 else{
			 c=db.rawQuery("Select "+select+"  from ruta " +filtro
		 +" group by direccion, cast(numEdificio as Integer)%2 order by cast(secuenciaReal as Integer)  asc", null);
		 }
		
		c.moveToFirst();
		
		for (int i=0; i<c.getCount();i++){
			lvs_vector.add(c.getString(c.getColumnIndex("nombre")));
			c.moveToNext();
		}
		
		c.close();
		closeDatabase();
		
		return lvs_vector;
	}
	
	public int EncriptarDesencriptarAlterno(byte[] medidor, int nContBytesClaveEncriptado)throws Throwable{
		return 0;
	}
	
	public static String Mid(String cad,int a, int b){		
		cad = cad.substring(a-1,a + b -1);		
		return cad;	
	}
	
	void EncriptarDesencriptarConParametros(byte[] medidor, int inicio, int longitud){
		
	}
	public void EncriptarDesencriptar(byte[] medidor){
		
	}
	
	public String getEstructuras( TransmitionObject to,  int tipo, int tipoTransmision){
		String ls_subcarpeta;
		openDatabase();
	       
	       //Tomamos el servidor desde la pantalla de configuracion
		   Cursor c;
			 
			 if (tipoTransmision==TransmisionesPadre.BLUETOOTH){
				 c=db.rawQuery("Select value from config where key='mac_bluetooth'", null);
				 c.moveToFirst();
				 if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_macBluetooth) , context.getString(R.string.str_configuracion),context.getString(R.string.info_macBluetooth));
				  
				
			 }
			 else{
				 c=db.rawQuery("Select value from config where key='server_gprs'", null);
				 c.moveToFirst();
				 if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_servidorGPRS) , context.getString(R.string.str_configuracion),context.getString(R.string.info_servidorGPRS));
				  
			 }
			 
			 to.ls_servidor=c.getString(c.getColumnIndex("value")); 
			  
			   
			   c.close();
			   //Ahora vamos a ver que archivo es el que vamos a recibir... para nicaragua es el clp + la extension
			   //Lo vamos a guardar en categoria, Asi le llamamos a los archivos desde "SuperLibretaDeDirecciones" 2013 (c) ;)
			   
			   c=db.rawQuery("Select value from config where key='cpl'", null);
			   
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return  String.format(context.getString(R.string.msj_config_no_disponible), context.getString(R.string.info_CPL) , context.getString(R.string.str_configuracion),context.getString(R.string.info_CPL));
				  
			  to.ls_categoria=c.getString(c.getColumnIndex("value")) +".TPL";
			   
			   c.close();
			   
			   //Por ultimo la ruta de descarga... Como es un servidor web, hay que quitarle el C:\... mejor empezamos desde lo que sigue. De cualquier manera, deberá tener el siguiente formato
			   //Ruta de descarga.subtr(3) + Entrada  + \ + lote 
			   c=db.rawQuery("Select value from config where key='ruta_descarga'", null);
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_rutaDescarga) , context.getString(R.string.str_configuracion),context.getString(R.string.info_rutaDescarga));
			   
			   to.ls_carpeta=c.getString(c.getColumnIndex("value")) ;
			   
			   
			   c.close();
			   
			   if (to.ls_carpeta.endsWith("\\"))
				   to.ls_carpeta= to.ls_carpeta.substring(0, to.ls_carpeta.length() -1);
			   
			   
			   to.ls_carpeta+=tipo==TransmisionesPadre.TRANSMISION?"Entrada":"Salida";
			   
			   c=db.rawQuery("Select value from config where key='lote'", null);
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c ))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_lote) , context.getString(R.string.str_configuracion),context.getString(R.string.info_lote));
			   
			   ls_subcarpeta=  c.getString(c.getColumnIndex("value"));
			   
			   to.ls_carpeta+="\\" + ls_subcarpeta ;
			   
			   c.close();

	       
	       closeDatabase();
	       return "";
	}
	
	public boolean validaCampoDeConfig(Cursor c){
		String ls_valor;
		 if (c.getCount()==0){
			   
			   //muere(true, "No se ha configurado algún servidor.\nConfigure el servidor en la pantalla 'Configuración' que se en cuentra en el menu de la 'Categoria" +
			   //		" Principal'.");
		   
		   /*db.execSQL("insert into config(key, value) values ('servidor', 'http://www.espinosacarlos.com')");
		   c=db.rawQuery("Select value from config where key='servidor'", null);
		   c.moveToFirst();
			  // return;
		   ls_servidor=c.getString(c.getColumnIndex("value"));*/
		   
		   return false;
			   
		   
	   }else{
		   ls_valor=c.getString(c.getColumnIndex("value"));
		   if (ls_valor.equals("") || ls_valor.equals(".") ){
			   
			   return false;
		   }
		   
	   }
		 
		 return true;
	}
	
	public String encabezadoAEnviar(String ls_carpeta, String ls_categoria){
		return globales.letraPais + ls_carpeta +"\\" + ls_categoria+Main.obtieneFecha("d/m/y  h:i:s");
	}
	
	public byte[] encabezadoAMandar(SQLiteDatabase db){
		Cursor c=db.rawQuery("Select registro from encabezado", null);
		
		c.moveToFirst();
		byte [] bytesAEnviar=c.getBlob(c.getColumnIndex("registro"));
		c.close();
		
		return bytesAEnviar;
	}
	
	public void noRegistradosinMedidor(){
		
	}
	
	/**
	 * Acciones que se hacen antes de capturar la lectura
	 */
	public void accionesAntesDeGrabarLectura(){
		if (globales.tll.getLecturaActual().sinUso1.trim().length()>=11 )
			globales.tll.getLecturaActual().globales.ultimoBloqueCapturado=globales.tll.getLecturaActual().sinUso1.substring(6, 10);
	}
	
	/**
	 * Acciones que se hacen despues de capturar la lectura
	 */
	public void accionesDespuesDeGrabarLectura(){
		
	}
	
	/**
	 * Regresa el usuario guardado / el ultimo ingresado segun el pais
	 * @return
	 */
	public String getUsuarioGuardado(){
		return"";
	}
	
public Vector <EstructuraResumen> getPrincipal(SQLiteDatabase db){
	

	String lote="";
	String cpl="";
	String mac_bt="";
	String mac_impr="";
	
	
	String ls_resumen;
	
	Cursor c;

		try{
			c=db.rawQuery("Select value from config where key='cpl'", null);
    		c.moveToFirst();
    		cpl=c.getString(c.getColumnIndex("value"));
		}
		catch(Throwable e){
			
		}
		
		try{
			c=db.rawQuery("Select value from config where key='lote'", null);
    		c.moveToFirst();
    		lote=c.getString(c.getColumnIndex("value"));
		}
		catch(Throwable e){
			
		}
    		
    		try{
    			c=db.rawQuery("Select value from config where key='mac_bluetooth'", null);
        		c.moveToFirst();
        		mac_bt=c.getString(c.getColumnIndex("value"));
    		}catch(Throwable e){
    			
    		}
    		
    		
    		try{
    			c=db.rawQuery("Select value from config where key='mac_impresora'", null);
        		c.moveToFirst();
        		mac_impr=c.getString(c.getColumnIndex("value"));
    		}catch(Throwable e){
    			
    		}
    		
    		
		
		
		
    	//ll_restantes = ll_total-ll_tomadas ;
		Vector <EstructuraResumen>resumen= new Vector<EstructuraResumen>();
		
		resumen= globales.tdlg.getResumen(db);
		
		resumen.add(new EstructuraResumen(cpl, context.getString(R.string.info_CPL)));
		resumen.add(new EstructuraResumen(lote,context.getString(R.string.info_lote)));
    	
//    	ls_resumen="CPL: " + cpl +"\n" +
//    			"Lote: " +  lote +"\n";
    	
    	if (!mac_bt.equals("") && !mac_bt.equals(".")){
    		   
//    			ls_resumen+="MAC BT: \n"+ mac_bt +"\n";
    		resumen.add(new EstructuraResumen(mac_bt, /*getString(R.string.info_macBluetooth)*/"MAC"));
    	}
    	
    	if (!mac_impr.equals("") && !mac_impr.equals(".")){
    		   
//			ls_resumen+="\nMAC Impr: \n"+ mac_impr;
    		resumen.add(new EstructuraResumen( mac_impr, context.getString(R.string.info_macImpresora)));
	}
    	
    	if (globales.getUsuario().equals("")){
    		
//    		if (!ma_papa.globales.mostrarCodigoUsuario)
//    			resumen.add(new EstructuraResumen( is_nombre_Lect, "Lect."));
//    		else
    			resumen.add(new EstructuraResumen( globales.getUsuario(), "Lect."));
    	}
    	
		
		
    	 
    	 return resumen;
		
	}

Cursor lineasAEscribir(SQLiteDatabase db){
	Cursor c = db.rawQuery("select " + globales.tlc.is_camposDeSalida + " as TextoSalida, secuenciaReal from Ruta ", null);
	return c;
}

public boolean mostrarVentanaDeSellos(){
	return false;
}



public boolean tomarFotoModificar() {
	// TODO Auto-generated method stub
	return true;
}

public String subirDirectorio(String ls_carpeta, int cuantos) {
	String ls_discoDuro = "";

	if (ls_carpeta.endsWith("\\"))
		ls_carpeta = ls_carpeta.substring(0, ls_carpeta.length());

	if (ls_carpeta.indexOf(":") >= 0) {
		ls_discoDuro = ls_carpeta.substring(0, ls_carpeta.indexOf(":") + 1);
		ls_carpeta = ls_carpeta.substring(ls_carpeta.indexOf(":") + 2);
	}

	for (int i = 0; i < cuantos; i++) {
		if (ls_carpeta.lastIndexOf("\\") >= 0)
			ls_carpeta = ls_carpeta.substring(0,
					ls_carpeta.lastIndexOf("\\"));
		else{
			ls_carpeta ="";
		}
	}

	return ls_discoDuro + ls_carpeta;

}


public String carpetaDeFotos( String ls_carpeta){
	ls_carpeta=subirDirectorio(ls_carpeta, 2);
	ls_carpeta="/fotos";
	return ls_carpeta;
}

}
