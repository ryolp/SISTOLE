package enruta.sistole_gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class trasmisionDatos extends TransmisionesPadre {
	boolean quedanMas = true;
	int metodoDeTransmisionUsado=TransmisionesPadre.WIFI;
	Serializacion serial=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enproceso);
		String[] ls_params;
		
		
		
		setTitle("");
		globales = ((Globales) getApplicationContext());

		tll = new TodasLasLecturas(this, false);
		resources = this.getResources();
		bu_params = getIntent().getExtras();
		metodoDeTransmisionUsado=bu_params.getInt("metodo");

		try {
//			transmiteFotos = bu_params.getBoolean("transmiteFotos");
		} catch (Throwable e) {
		}

//		openDatabase();
//
//		// Tomamos el servidor desde la pantalla de configuracion
//		Cursor c = db.rawQuery(
//				"Select value from config where key='server_gprs'", null);
//		c.moveToFirst();
//
//		if (!validaCampoDeConfig(c, String.format(
//				getString(R.string.msj_config_no_disponible),
//				getString(R.string.info_servidorGPRS),
//				getString(R.string.str_configuracion),
//				getString(R.string.info_servidorGPRS))))
//			return;
//
//		ls_servidor = c.getString(c.getColumnIndex("value"));
//
//		c.close();
//		// Ahora vamos a ver que archivo es el que vamos a recibir... para
//		// nicaragua es el clp + la extension
//		// Lo vamos a guardar en categoria, Asi le llamamos a los archivos desde
//		// "SuperLibretaDeDirecciones" 2013 (c) ;)
//
//		c = db.rawQuery("Select value from config where key='cpl'", null);
//
//		c.moveToFirst();
//		if (!validaCampoDeConfig(c, String.format(
//				getString(R.string.msj_config_no_disponible),
//				getString(R.string.info_CPL),
//				getString(R.string.str_configuracion),
//				getString(R.string.info_CPL))))
//			return;
//
//		ls_categoria = c.getString(c.getColumnIndex("value")) + "."
//				+ ls_extension;
//
//		c.close();
//
//		// Por ultimo la ruta de descarga... Como es un servidor web, hay que
//		// quitarle el C:\... mejor empezamos desde lo que sigue. De cualquier
//		// manera, deberá tener el siguiente formato
//		// Ruta de descarga.subtr(3) + Entrada + \ + lote
//		c = db.rawQuery("Select value from config where key='ruta_descarga'",
//				null);
//		c.moveToFirst();
//		if (!validaCampoDeConfig(c, String.format(
//				getString(R.string.msj_config_no_disponible),
//				getString(R.string.info_rutaDescarga),
//				getString(R.string.str_configuracion),
//				getString(R.string.info_rutaDescarga))))
//			return;
//
//		ls_carpeta = c.getString(c.getColumnIndex("value"));
//
//		if (ls_carpeta.indexOf(":") >= 0) {
//			ls_carpeta = ls_carpeta.substring(ls_carpeta.indexOf(":") + 2);
//		}
//
//		c.close();
//
//		if (ls_carpeta.endsWith("\\"))
//			ls_carpeta = ls_carpeta.substring(0, ls_carpeta.length() - 1);
//
//		ls_carpeta += bu_params.getInt("tipo") == TRANSMISION ? "Entrada"
//				: "Salida";
//
//		c = db.rawQuery("Select value from config where key='lote'", null);
//		c.moveToFirst();
//		if (!validaCampoDeConfig(c, String.format(
//				getString(R.string.msj_config_no_disponible),
//				getString(R.string.info_lote),
//				getString(R.string.str_configuracion),
//				getString(R.string.info_lote))))
//			return;
//
//		ls_subCarpeta = c.getString(c.getColumnIndex("value"));
//
//		ls_carpeta += "\\" + ls_subCarpeta;
//
//		c.close();
//
//		closeDatabase();
		
		 TransmitionObject to= new TransmitionObject();
	       
	       if(!validaCampoDeConfig(globales.tdlg.getEstructuras( to, bu_params.getInt("tipo"),bu_params.getInt("metodo")))){
	    	   return;
	       }
	       
	       ls_servidor= to.ls_servidor;
	       ls_carpeta=to.ls_carpeta;
	       ls_categoria= to.ls_categoria;

		tv_progreso = (TextView) findViewById(R.id.ep_tv_progreso);
		tv_indicador = (TextView) findViewById(R.id.ep_tv_indicador);
		pb_progress = (ProgressBar) findViewById(R.id.ep_gauge);

		mHandler = new Handler();

		seleccion();

	}

	public void transmitir() {
//		final trasmisionDatos context = this;
//		hilo = new Thread() {
//			int cantidad;
//
//			public void run() {
//
//				// TODO Auto-generated method stub
//				serial = new Serializacion(Serializacion.WIFI);
//				String ls_cadena = "";
//				byte[] lby_registro, lby_cadenaEnBytes;
//				String ls_nombre_final;
//
//				switch (globales.modoDeCierreDeLecturas){
//				case Globales.FORZADO:
//					puedoCerrar = false;
//					mostrarMensaje(PROGRESO, getString(R.string.msj_trans_forzando));
//					// Abrimos el arreglo de todas las lecturas y forzamos
//					tll.forzarLecturas();
//					cancelar = false;
//					break;
//				}
//				puedoCerrar = true;
//				Cursor c = null;
//				try {
//				if (transmitirLecturas){
//					mostrarMensaje(PROGRESO, getString(R.string.str_espere));
//					
//					
//						openDatabase();
//
//						String[] ls_params = { String
//								.valueOf(TomaDeLecturas.NO_ENVIADA) };
//
////						if (globales.tlc.is_camposDeSalida.equals(""))
////							c = db.rawQuery("select * from Ruta ", null);
////						else
////							c = db.rawQuery("select " + globales.tlc.is_camposDeSalida + " as TextoSalida from Ruta ", null);
//						c=globales.tdlg.lineasAEscribir( db);
//
//						cantidad = c.getCount();
//						mHandler.post(new Runnable() {
//							public void run() {
//								pb_progress.setMax(cantidad);
//							}
//						});
//
//						mostrarMensaje(PROGRESO,
//								getString(R.string.msj_trans_generando));
//						mostrarMensaje(MENSAJE, getString(R.string.str_espere));
//
//						if (!globales.enviarSoloLoMarcado)
//							borrarArchivo(ls_carpeta + "/" + globales.tdlg.getNombreArchvio(TomaDeLecturasGenerica.SALIDA));
//
//						serial.open(ls_servidor, ls_carpeta, globales.tdlg.getNombreArchvio(TomaDeLecturasGenerica.SALIDA),
//								Serializacion.ESCRITURA, 0, 0);
//						for (int i = 0; i < cantidad; i++) {
//							context.stop();
//							c.moveToPosition(i);
//
//							// ls_cadena=generaCadenaAEnviar(c);
//							// lby_cadenaEnBytes=ls_cadena.getBytes();
//
//							// Ya tenemos los datos a enviar (que emocion!) asi que
//							// hay que agregarlos a la cadena final
//
//							lby_registro = c.getString(c.getColumnIndex("TextoSalida")).getBytes();
//
//							// for (int j=0; j<lby_cadenaEnBytes.length;j++)
//							// lby_registro[j+resources.getInteger(R.integer.POS_DATOS_TIPO_LECTURA)]=lby_cadenaEnBytes[j];
//
//							String ls_cadenaAEnviar = "";
////							if (globales.tlc.is_CamposDeSalida.equals("")) {
////								ls_cadenaAEnviar = new String(c.getBlob(c
////										.getColumnIndex("registro")));
////								if (ls_cadenaAEnviar.length() > globales.tlc
////										.getLongCampo("registro"))
////									;
////								ls_cadenaAEnviar = ls_cadenaAEnviar.substring(0,
////										globales.tdlg.long_registro);
////							}else{
//								ls_cadenaAEnviar = new String(c.getString(c
//										.getColumnIndex("TextoSalida")));
////							}
//							// Escribimos los bytes en el archivo
//							serial.write(ls_cadenaAEnviar + "\r\n");
//
//							String bufferLenght;
//							int porcentaje = ((i + 1) * 100) / c.getCount();
//							bufferLenght = String.valueOf(c.getCount());
//
//							/*
//							 * openDatabase();
//							 * 
//							 * String whereClause="secuencial=?"; String[]
//							 * whereArgs=
//							 * {String.valueOf(c.getLong(c.getColumnIndex("secuencial"
//							 * )))}; ContentValues cv_datos=new ContentValues(1);
//							 * 
//							 * cv_datos.put("envio",TomaDeLecturas.ENVIADA);
//							 * 
//							 * int j=db.update("lecturas", cv_datos, whereClause,
//							 * whereArgs);
//							 * 
//							 * closeDatabase();
//							 */
//							// Marcar como enviada
//							mostrarMensaje(MENSAJE, (i + 1) + " " + getString(R.string.de)
//									+ " " + bufferLenght + " " + getString(R.string.registros)
//									+ ".\n" + String.valueOf(porcentaje) + "%");
//							mostrarMensaje(BARRA, String.valueOf(1));
//
//						}
//						serial.close();
//
//						c.close();
//						
//						//Actualizamos las ordenes, ya que ya se enviaron
//						if (globales.enviarSoloLoMarcado)
//							db.execSQL("Update ruta set envio=0");
//
//						mostrarMensaje(
//								PROGRESO,
//								getString(R.string.msj_trans_generando_no_registrados));
//						mostrarMensaje(MENSAJE, getString(R.string.str_espere));
//						mostrarMensaje(BARRA, String.valueOf(0));
//
//						String ls_query = "select * from NoRegistrados ";
//						
//						if (globales.enviarSoloLoMarcado)
//							ls_query+= "where  envio= 1;";
//						
//						c = db.rawQuery(ls_query, null);
//
//						ls_nombre_final =globales.tdlg.getNombreArchvio(TomaDeLecturasGenerica.NO_REGISTRADOS) ;
//						borrarArchivo(ls_carpeta + "/" + ls_nombre_final);
//
//						cantidad = c.getCount();
//
//						serial.open(ls_servidor, ls_carpeta, ls_nombre_final,
//								Serializacion.ESCRITURA, 0, 0);
//
//						mHandler.post(new Runnable() {
//							public void run() {
//								pb_progress.setMax(cantidad);
//							}
//						});
//						for (int i = 0; i < cantidad; i++) {
//							context.stop();
//							c.moveToPosition(i);
//
//							// ls_cadena=generaCadenaAEnviar(c);
//							// lby_cadenaEnBytes=ls_cadena.getBytes();
//
//							// Ya tenemos los datos a enviar (que emocion!) asi que
//							// hay que agregarlos a la cadena final
//
//							lby_registro = c.getBlob(c.getColumnIndex("poliza"));
//
//							// for (int j=0; j<lby_cadenaEnBytes.length;j++)
//							// lby_registro[j+resources.getInteger(R.integer.POS_DATOS_TIPO_LECTURA)]=lby_cadenaEnBytes[j];
//
//							// Escribimos los bytes en el archivo
//							serial.write(new String(lby_registro) + "\r\n");
//
//							String bufferLenght;
//							int porcentaje = ((i + 1) * 100) / c.getCount();
//							bufferLenght = String.valueOf(c.getCount());
//
//							/*
//							 * openDatabase();
//							 * 
//							 * String whereClause="secuencial=?"; String[]
//							 * whereArgs=
//							 * {String.valueOf(c.getLong(c.getColumnIndex("secuencial"
//							 * )))}; ContentValues cv_datos=new ContentValues(1);
//							 * 
//							 * cv_datos.put("envio",TomaDeLecturas.ENVIADA);
//							 * 
//							 * int j=db.update("lecturas", cv_datos, whereClause,
//							 * whereArgs);
//							 * 
//							 * closeDatabase();
//							 */
//							// Marcar como enviada
//							mostrarMensaje(MENSAJE, (i + 1) + " "
//									+ getString(R.string.de) + " " + bufferLenght
//									+ " " + getString(R.string.registros) + ".\n"
//									+ String.valueOf(porcentaje) + "%");
//							mostrarMensaje(BARRA, String.valueOf(1));
//
//						}
//						serial.close();
//
//						c.close();
//						
//						//Actualizamos las ordenes, ya que ya se enviaron
//						if (globales.enviarSoloLoMarcado)
//							db.execSQL("Update NoRegistrados set envio=0");
//						
//						
//						for (int archivo: globales.tdlg.getArchivosATransmitir(metodoDeTransmisionUsado)){
//			   				//Aqui enviamos los no registrados
//							
//							
//							mostrarMensaje(PROGRESO,  globales.tdlg.regresaMensajeDeTransmision(archivo));
//			   				mostrarMensaje(MENSAJE, getString(R.string.str_espere));
//			   				mostrarMensaje(BARRA,String.valueOf(0));
//
//							 c=  globales.tdlg.getContenidoDelArchivo(db, archivo);
//
//							ls_nombre_final =globales.tdlg.getNombreArchvio(archivo);
//							borrarArchivo(ls_carpeta + "/" + ls_nombre_final);
//							
//							
//
//							cantidad = c.getCount();
//
//							serial.open(ls_servidor, ls_carpeta, ls_nombre_final,
//									Serializacion.ESCRITURA, 0, 0);
//
//							mHandler.post(new Runnable() {
//								public void run() {
//									pb_progress.setMax(cantidad);
//								}
//							});
//							for (int i = 0; i < cantidad; i++) {
//								context.stop();
//								c.moveToPosition(i);
//
//								// ls_cadena=generaCadenaAEnviar(c);
//								// lby_cadenaEnBytes=ls_cadena.getBytes();
//
//								// Ya tenemos los datos a enviar (que emocion!) asi que
//								// hay que agregarlos a la cadena final
//
//								lby_registro = c.getBlob(c.getColumnIndex("texto"));
//
//								// for (int j=0; j<lby_cadenaEnBytes.length;j++)
//								// lby_registro[j+resources.getInteger(R.integer.POS_DATOS_TIPO_LECTURA)]=lby_cadenaEnBytes[j];
//
//								// Escribimos los bytes en el archivo
//								serial.write(new String(lby_registro) + "\r\n");
//
//								String bufferLenght;
//								int porcentaje = ((i + 1) * 100) / c.getCount();
//								bufferLenght = String.valueOf(c.getCount());
//
//								/*
//								 * openDatabase();
//								 * 
//								 * String whereClause="secuencial=?"; String[]
//								 * whereArgs=
//								 * {String.valueOf(c.getLong(c.getColumnIndex("secuencial"
//								 * )))}; ContentValues cv_datos=new ContentValues(1);
//								 * 
//								 * cv_datos.put("envio",TomaDeLecturas.ENVIADA);
//								 * 
//								 * int j=db.update("lecturas", cv_datos, whereClause,
//								 * whereArgs);
//								 * 
//								 * closeDatabase();
//								 */
//								// Marcar como enviada
//								mostrarMensaje(MENSAJE, (i + 1) + " "
//										+ getString(R.string.de) + " " + bufferLenght
//										+ " " + getString(R.string.registros) + ".\n"
//										+ String.valueOf(porcentaje) + "%");
//								mostrarMensaje(BARRA, String.valueOf(1));
//
//							}
//							serial.close();
//
//							c.close();
//					   			
//			   				}
//
//						mostrarMensaje(BARRA, String.valueOf(0));
//
//
//				}
//
//									// Aqui enviamos los no registrados
//					
//					if (transmiteFotos) {
//
//						mostrarMensaje(PROGRESO,
//								getString(R.string.msj_trans_generando_fotos));
//						mostrarMensaje(MENSAJE, getString(R.string.str_espere));
//
//						openDatabase();
//						
//						String ls_query="select nombre, foto , rowid from fotos ";
//						if (globales.enviarSoloLoMarcado)
//							ls_query+= " where envio= 1;";
//						
//						c = db.rawQuery(
//								ls_query, null);
//
//						
//						
//						cantidad = c.getCount();
//
//						mHandler.post(new Runnable() {
//							public void run() {
//								pb_progress.setMax(cantidad);
//							}
//						});
//						// closeDatabase();
//
////						String ls_capertaFotos = subirDirectorio(ls_carpeta, 2)
////								+ "/fotos/" + ls_subCarpeta + "/"
////								+ Main.obtieneFecha("ymd");
//						
////						String ls_capertaFotos = subirDirectorio(ls_carpeta, 2)
////								+ "/fotos";
//						
//						String ls_capertaFotos=globales.tdlg.carpetaDeFotos(ls_carpeta);
//
//						c.moveToFirst();
//
//						for (int i = 0; i < c.getCount(); i++) {
//							context.stop();
//
//							serial.open(ls_servidor, ls_capertaFotos, "",
//									Serializacion.ESCRITURA, 0, 0);
//
//							String nombreFoto=c.getString(c.getColumnIndex("nombre"));
//							if (globales.quitarPrimerCaracterNombreFoto){
//								nombreFoto=nombreFoto.substring(1);
//							}
//							// ls_cadena=generaCadenaAEnviar(c);
//							serial.write(nombreFoto,
//									c.getBlob(c.getColumnIndex("foto")));
//
//							String bufferLenght;
//							int porcentaje = ((i + 1) * 100) / c.getCount();
//							bufferLenght = String.valueOf(c.getCount());
//							serial.close();
//							openDatabase();
//
//							String whereClause = "rowid=?";
//							String[] whereArgs = { c.getString(c
//									.getColumnIndex("rowid")) };
//							ContentValues cv_datos = new ContentValues(1);
//
//							if (!transmitirTodo) {
//								cv_datos.put("envio", TomaDeLecturas.ENVIADA);
//
//								int j = db.update("fotos", cv_datos,
//										whereClause, whereArgs);
//							}
//							// closeDatabase();
//							// Marcar como enviada
//							c.moveToNext();
//							mostrarMensaje(MENSAJE, (i + 1) + " "
//									+ getString(R.string.de) + " "
//									+ bufferLenght + " "
//									+ getString(R.string.str_fotos) + ".\n"
//									+ String.valueOf(porcentaje) + "%");
//							mostrarMensaje(BARRA, String.valueOf(1));
//						}
//					}
//					
//					//Actualizamos las ordenes, ya que ya se enviaron
//					if (globales.enviarSoloLoMarcado)
//						db.execSQL("Update fotos set envio=0");
//
//					// mostrarMensaje(PROGRESO, "Mandando datos al servidor");
//					mostrarMensaje(MENSAJE, getString(R.string.str_espere));
//					// serial.close();
//					yaAcabo = true;
//					if (transmitirLecturas){
//
//						marcarComoDescargada();
//					}
//					muere(true, String.format(
//							getString(R.string.msj_trans_correcta),
//							getString(R.string.str_exportado)));
//					c.close();
//				} catch (Throwable e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					muere(true,
//							String.format(getString(R.string.msj_trans_error),
//									getString(R.string.str_exportar_lowercase))
//									+ e.getMessage());
//				} finally {
//					closeDatabase();
//
//					// dialog.cancel();
//				}
//
//			}
//
//		};
//
//		hilo.start();
		
		Timer timer= new Timer();

		timer.schedule(new ThreadTransmitirWifi(serial, globales, this), 0);
	}

	public void recepcion() {
		final trasmisionDatos context = this;
		puedoCerrar = false;
		hilo = new Thread() {
			int cantidad;

			public void run() {
				int secuenciaReal=0;
				
				boolean recibiOrdenes=false;

				// TODO Auto-generated method stub
				serial = new Serializacion(Serializacion.WIFI);
				String ls_cadena = "";
				byte[] lby_cadena;
				String[] lineas = null;
				String[] ls_cambios;

				String mPhoneNumber;

				/*
				 * TelephonyManager tMgr
				 * =(TelephonyManager)context.getSystemService
				 * (Context.TELEPHONY_SERVICE); String mPhoneNumber =
				 * tMgr.getLine1Number();
				 */
				// ProgressDialog dialog = ProgressDialog.show(context,
				// "Exportar", "Se esta exportando el archivo, espere", true);

				mostrarMensaje(MENSAJE,
						getString(R.string.msj_trans_recibiendo));
				mostrarMensaje(PROGRESO, getString(R.string.str_espere));
				int i = 0;

				try {
					
					puedoCerrar = true;

					openDatabase();

					/*
					 * Cursor parm=
					 * db.rawQuery("select value from params where key='telefono'"
					 * , null);
					 * 
					 * parm.moveToFirst();
					 * 
					 * mPhoneNumber=
					 * parm.getString(parm.getColumnIndex("value"));
					 */

					// borrarArchivo( "uploads/"+ mPhoneNumber+".txt");

					// Cursor c=
					// db.rawQuery("select id , nombre , padre , telefono, tipo, avance, total, color, orden, pordefecto, horaInicio, horaFin, porcion, unidad  from direcciones ",
					// null);

					// cantidad=c.getCount();

					/*
					 * mHandler.post(new Runnable() { public void run() {
					 * pb_progress.setMax(cantidad); } });
					 */
					// mostrarMensaje(PROGRESO, "Generando datos a importar");
					// mostrarMensaje(MENSAJE, "Espere...");
					
					switch(globales.tipoDeRecepcion){
					
					case Globales.TRANSMION_NORMAL:
						serial.open(ls_servidor, ls_carpeta, globales.tdlg.getNombreArchvio(TomaDeLecturasGenerica.ENTRADA),
								Serializacion.LECTURA, 0, 0);
						context.stop();
						// lby_cadena= new
						// byte[context.getResources().getInteger(R.integer.LONG_DATOS_MEDIDOR)];

						vLecturas = new Vector<String>();

						if (serial.longitudDelArchivo == 0) {
							// no se encontro el archivo
							serial.close();
							muere(false,
									getString(R.string.msj_trans_file_not_found));
							return;
						}

						// Obtenemos el archivo recibido completo
						lby_cadena = new byte[serial.longitudDelArchivo];
						serial.read(lby_cadena);
						ls_cadena = new String(lby_cadena);

						// Hacemos split con el salto de linea
						lineas = ls_cadena.split("\\n");
						break;
					
					case Globales.TRANSMION_ELECTIRCARIBE:
						
						lineas = preparaArchivoPorCargar();
					}
					

					tope(Integer.parseInt(String.valueOf(lineas.length)));

					// db.execSQL("delete from Lecturas ");

					/*
					 * db.execSQL("delete from ruta ");
					 * db.execSQL("delete from fotos ");
					 * db.execSQL("delete from Anomalia ");
					 * db.execSQL("delete from encabezado ");
					 * db.execSQL("delete from NoRegistrados ");
					 */
					borrarRuta(db);
					// serial.close();
					
					recibiOrdenes=lineas.length>0;

					db.beginTransaction();
					for (String ls_linea : lineas) {
						context.stop();
						
						// Comprobamos que las lineas son las que esperamos
						if (ls_linea.length() == 0) {
							// no se encontro el archivo
							serial.close();
							db.execSQL("delete from Lecturas ");
							closeDatabase();
							// db.setTransactionSuccessful();
							// db.endTransaction();
							algunError = true;
							// muere(false,
							// "No se encontro algun archivo exportado.");
							muere(false,
									getString(R.string.msj_trans_file_not_found));
						}

						if (ls_linea.toUpperCase().startsWith("<HTML>")) {
							// Error general
							serial.close();
							// db.execSQL("delete from Lecturas ");
							db.endTransaction();
							closeDatabase();
							// db.setTransactionSuccessful();

							algunError = true;
							muere(false,
									getString(R.string.msj_trans_connection_problem));

						}

						if (ls_linea.length() != globales.tdlg.long_registro) {
							// Error general
							serial.close();
							// db.setTransactionSuccessful();
							db.endTransaction();
							// db.execSQL("delete from Lecturas ");
							closeDatabase();
							algunError = true;
							muere(false,
									getString(R.string.msj_trans_file_doesnt_match));

						}

						// Agregamos mientras verificamos...
						// vLecturas.add(ls_cadena);
						// db.execSQL("Insert into lecturas(registro) values ('"+ls_linea+"')");
						if (i != 0 && !ls_linea.startsWith("#")
								&& !ls_linea.startsWith("!") && !globales.tdlg.esUnRegistroRaro(ls_linea)) {
							secuenciaReal++;
							globales.tlc.byteToBD(db,
									ls_linea.getBytes("ISO-8859-1"), secuenciaReal);// Esta
																		// clase
																		// ahora
																		// guarda
							// new Lectura(context,
							// ls_linea.getBytes("ISO-8859-1"), db);
						} else if (ls_linea.startsWith("#")) {// Esto indica que
																// es una
																// anomalia
							new Anomalia(context,
									ls_linea.getBytes("ISO-8859-1"), db);
						} else if (ls_linea.startsWith("!")) { // un usuario
							new Usuario(context,
									ls_linea.getBytes("ISO-8859-1"), db);
						}
						else if (globales.tdlg.esUnRegistroRaro(ls_linea) && i != 0){
		   					globales.tdlg.agregarRegistroRaro(db, ls_linea);
		   				}else if (i == 0) {
							// la primera
							ContentValues cv = new ContentValues();
							cv.put("registro", ls_linea.getBytes());

							db.insert("encabezado", null, cv);
						}

						int porcentaje = (i * 100) / lineas.length;
						mostrarMensaje(MENSAJE, (i + 1) + " "
								+ getString(R.string.de) + " " + lineas.length
								+ " " + getString(R.string.registros) + "\n"
								+ String.valueOf(porcentaje) + "%");
						mostrarMensaje(BARRA, String.valueOf(1));
						i++;

					}

					// Una vez verificado que todos los registros fueron
					// recibidos ahora si tenemos la seguridad de borrar

					// Una vez que borramos insertamos cada uno de los registros
					// recibidos

					// for(String ls_lectura:vLecturas)
					
					//Si acaso se requiere agregamos las anomalias manualmente
	   				globales.tdlg.AgregarAnomaliasManualmente(db); 
	   				
	   				globales.tdlg.accionesDespuesDeCargarArchivo(db);
	   				
	   				if(recibiOrdenes && !algunError){
						//Si hay un error al recibir o no recibí, mover el archivo no tiene sentido
//						enviarBackup(ls_carpeta, globales.tdlg.getNombreArchvio(TomaDeLecturasGenerica.ENTRADA));

						//Reordenamos la secuenciaReal
//						asignarSecuenciasReales();
						
					}

					if (!algunError)
						db.setTransactionSuccessful();

					mostrarMensaje(MENSAJE, getString(R.string.str_espere));
					serial.close();
					yaAcabo = true;

					muere(true, String.format(
							getString(R.string.msj_trans_correcta),
							getString(R.string.str_importado)));
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// db.endTransaction();
					// db.execSQL("delete from lecturas ");
					muere(true,
							String.format(getString(R.string.msj_trans_error),
									getString(R.string.str_importar_lowercase))
									+ i + " " + e.getMessage());
				} finally {
					try {
						db.endTransaction();
					} catch (Throwable e) {

					}

					closeDatabase();

					// dialog.cancel();
				}

			}

		};

		hilo.start();

	}

	public String remplazaNulls(String ls_cadena) {
		ls_cadena = (ls_cadena == null ? "" : ls_cadena);

		return (ls_cadena.trim().equals("") ? "" : ls_cadena);
	}

	private void borrarArchivo(String ls_ruta) throws Throwable {
		// HCG 20/07/2012 Manda los datos del wifi antes de cerrar la conexion
		String ruta, cadenaAEnviar;

		Hashtable params = new Hashtable();
		// params.put("cadena",cadenaAEnviar);
		params.put("ruta", ls_ruta);

		try {
			HttpMultipartRequest http = new HttpMultipartRequest(ls_servidor
					+ "/deleteFile.php", params, "upload_field", "",
					"text/plain", new String("").getBytes());
			byte[] response = http.send();
			// new String (response); Esta es la respuesta del servidor

			if (!new String(response).trim().equals("0")) {
				throw new Throwable(new String(response));
			}

			// Enviamos las fotos que tenemos pendientes
			// enviaFotosWifi();

		} catch (Throwable e) {
			throw e;
		}

	}

	void mostrarMensaje(final int tipo, final String mensaje) {
		// Esta funcion manda un request para que se cambie algun elemento en
		// patanlla
		mHandler.post(new Runnable() {
			public void run() {
				switch (tipo) {
				case MENSAJE:
					setMensaje(mensaje);
					break;
				case PROGRESO:
					setProgreso(mensaje);
					break;

				case BARRA:
					avanzaProgreso(Integer.parseInt(mensaje));
					break;
				case TOPE:
					tope(Integer.parseInt(mensaje));
					break;

				}
			}
		});
	}

	public void setMensaje(String texto) {
		tv_indicador.setText(texto);
	}

	public void setProgreso(String texto) {
		tv_progreso.setText(texto);
	}

	public void avanzaProgreso(int avance) {
		if (pb_progress.isIndeterminate())
			pb_progress.setIndeterminate(false);

		if (avance > 0)
			pb_progress.incrementProgressBy(avance);
		else {
			pb_progress.setProgress(0);
			pb_progress.setIndeterminate(true);

		}
	}

	public void tope(int avance) {
		if (pb_progress.isIndeterminate())
			pb_progress.setIndeterminate(false);
		pb_progress.setMax(avance);
	}

	public void setAcabado() {
		yaAcabo = true;
	}

	private String generaCadenaAEnviar(Cursor c) {
		String ls_cadena = "";
		String ls_lectura;
		c.moveToFirst();
		String ls_tmpSubAnom = "";

		ls_lectura = c.getString(c.getColumnIndex("lectura"));

		ls_cadena = ls_lectura.length() == 0 ? "4" : "0"; // Indicador de tipo
															// de lectura
		ls_cadena += Main.rellenaString(ls_lectura, "0",
				globales.tdlg.long_registro, true);
		ls_cadena += Main.rellenaString(ls_lectura, "0",
				globales.tdlg.long_registro, true);
		ls_cadena += c.getString(c.getColumnIndex("fecha"));
		ls_cadena += c.getString(c.getColumnIndex("hora"));

		ls_cadena += Main.rellenaString(
				c.getString(c.getColumnIndex("anomalia")), " ",
				globales.tdlg.long_registro, true);
		// Esto no se bien de que se trata, asi que de momento dejaremos
		// ceros...
		ls_cadena += Main.rellenaString("", "0",
				globales.tdlg.long_registro, true);
		ls_cadena += Main.rellenaString("", "0",
				globales.tdlg.long_registro, true);
		ls_cadena += Main.rellenaString("", "0",
				globales.tdlg.long_registro, true);

		return ls_cadena;
	}

	public void preguntaArchivo() {
		AlertDialog alert;

		LayoutInflater inflater = this.getLayoutInflater();

		String ls_archivo;

		final View view = inflater.inflate(R.layout.lote_a_cargar, null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final trasmisionDatos slda = this;
		final String[] selectionArgs = { "archivo" };
		builder.setView(view);

		final EditText et_archivocarga = (EditText) view
				.findViewById(R.id.et_archivocarga);

		openDatabase();

		Cursor c = db.rawQuery("Select value from config where key=?",
				selectionArgs);

		if (c.getCount() > 0) {
			c.moveToFirst();
			ls_archivo = c.getString(c.getColumnIndex("value"));
			if (ls_archivo.indexOf(".") > 0) {
				et_archivocarga.setText(ls_archivo.substring(0,
						ls_archivo.indexOf(".")));
			} else {
				et_archivocarga.setText(ls_archivo);
			}

		}
		/*
		 * else{ et_archivocarga.setText("cpl001"); }
		 */

		closeDatabase();

		builder.setCancelable(false)
				.setPositiveButton(R.string.continuar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ls_categoria = et_archivocarga.getText()
										.toString().trim()
										+ "." + ls_extension;
								if (ls_categoria.length() == 0)
									mensajeVacioLote();
								else {
									openDatabase();

									Cursor c = db
											.rawQuery(
													"Select value from config where key=?",
													selectionArgs);

									if (c.getCount() > 0)
										db.execSQL("update config set value='"
												+ ls_categoria
												+ "' where key='archivo'");
									else
										db.execSQL("insert into config(key, value) values('archivo', '"
												+ ls_categoria + "')");

									closeDatabase();
									recepcion();
								}

								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancelar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								mostrarAlerta = false;
								muere(true, "");

							}
						});

		builder.show();
		esconderTeclado(et_archivocarga);

	}

	// Elimina todo lo que necesite para que sea numero
	public long quitarCaracteres(String ls_cadena) {
		String ls_numero = "", ls_caracter;

		for (int i = 0; i < ls_cadena.length(); i++) {
			ls_caracter = ls_cadena.substring(i, i + 1);
			if (esNumero(ls_caracter)) {
				ls_numero += ls_caracter;
			}

		}

		return Long.parseLong(ls_numero);

	}

	public boolean esNumero(String ls_cadena) {
		try {
			Integer.parseInt(ls_cadena);
			return true;
		} catch (Throwable e) {
			return false;

		}
	}

	public String quitaComillas(String ls_candena) {
		return ls_candena.replace("\"", "");
	}

	public void mensajeVacioLote() {
		final trasmisionDatos slda = this;
		AlertDialog.Builder message = new AlertDialog.Builder(slda);
		message.setMessage(R.string.str_emptyField)
				.setCancelable(false)
				.setPositiveButton(R.string.aceptar,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								preguntaArchivo();
							}

						});
		AlertDialog alerta = message.create();
		alerta.show();
	}

	public void cancelar() {

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
		}

		return ls_discoDuro + ls_carpeta;

	}
	
	public String[] preparaArchivoPorCargar() throws Throwable{
		int LONG_DATOS_PARAMETR_PANAMA=  55;
//		Serializacion serial= new Serializacion(Serializacion.WIFI);
		boolean quedaMas=true, errorEnLaLectura=false, bEsEncabezado=true;
		int leidos=0, offset=0;
		byte[] medidor = null, medidorSalida;
//		String medidor;
		int nContBytesClaveEncriptadoEntrada = 0, totalBytes = 0;
		Vector vMedidores= new Vector(), vObservaciones= new Vector(); //SI! asi sin tipo de dato, porque viene del nokia
		int long_registro=globales.tdlg.long_registro+2;
		String [] ls_lineas;
//		 getArchivoAlterno();
		try{
			
			
	    	//Archivo .C
			//midlet.log.log("Empezamos a cargar el archivo");
			mostrarMensaje(MENSAJE, "Cargando Ruta");
			mostrarMensaje(PROGRESO, getString(R.string.str_espere));
			//EnProceso ep = new EnProceso(midlet, midlet.forma,"Cargando Ruta","Espere", 0);
			//midlet.log.log(ls_servidor + "/" + ls_carpeta + "/" + NombreArchivoPorRecibir());
//	    	serial.open(ls_servidor, ls_carpeta, ls_categoria, Serializacion.LECTURA, 0, 0);
	    	//totalDeMedidoresRecibidos=serial.longitudDelArchivo/midlet.LONG_DATOS_MEDIDOR ;
	    	
	    	mostrarMensaje(MENSAJE, "Procesando archivo de Carga ......");
			mostrarMensaje(PROGRESO, getString(R.string.str_espere));
			//Indefinite
	    	
//			medidor = new byte[serial.getStringSize()];
//			
//			serial.read(medidor);
//			
//			globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoEntrada);
			
			
			ls_lineas =getArchivoAlterno(ls_carpeta, ls_categoria);
	    	
	    	quedanMas 	 = true;
			errorEnLaLectura = false;
			leidos		 = 0;
			//while (quedanMas && !errorEnLaLectura){
			for(int j=0; j<ls_lineas.length;j++){
				offset = 0;
				
//				medidor = new byte[long_registro];
				//leidos=serial.read(medidor);
				medidor = ls_lineas[j].getBytes();
			
				//if (leidos == -1 || errorEnLaLectura ) quedanMas = false;
//				else{

					if (bEsEncabezado) {
						bEsEncabezado = false;
						medidorSalida  	= new byte[long_registro];
						for(int i=0; i<long_registro; i++) medidorSalida[i] = ' ';
						medidorSalida[2] = 'L';
						medidorSalida[long_registro-1] = 10;
						medidorSalida[long_registro-2] = 13;
						int nLongCopiarEnEncabezado = 26;

//						nContBytesClaveEncriptadoEntrada = globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoEntrada);

						if (new String(medidor).startsWith("RFIN")) {
							break;
						}
						for(int i=0; i<nLongCopiarEnEncabezado; i++) medidorSalida[i+200] = medidor[i+4];
						for(int i=0; i<nLongCopiarEnEncabezado; i++) medidorSalida[i+200] = medidor[i+200];
						vMedidores.addElement(medidorSalida);
					}
					else{
						
//						nContBytesClaveEncriptadoEntrada = globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoEntrada);
//						if(vMedidores.size()==656){
//							int a=1;
//							a++;
						
						if (new String(medidor).startsWith("RFIN")) {
							break;
						}
						
						vMedidores.addElement(medidor);
					}
					//totalMedidoresLeidos ++;
					
					}
				
				
				
			
//			}
			
//			serial.close();
			
		}catch(Throwable e)
			{
			//midlet.log.log("Ocurrio un error al cargar archivo de Carga "+ e);
			throw new Throwable("No se recibio el archivo de carga C");
//				return ;
			}
		//midlet.log.log("Archivo C. RECIBIDO");
			String strFoto		= "";
			String strClave 	= "";
			String strDescripcion 	= "";
			String strCompatible 	= "";
			String strPrefijo 	= "";

			String strAnomalia 	= "";
			String strObservacion	= "";
			String strObservacion1	= "";
			String strObservacion2	= "";
			String strSubAnomalias  = ".";
			String strRequiereMensaje = "";
			String strRequiereMedidor = "";
	    	

				int LONG_DATOS_ANOMALIAS_PANAMA = 35;
				int LONG_DATOS_OBSERVACIONES_PANAMA = 17;
				String archivo= ls_categoria, strNombreDelArchivoFinal;

				
				//strNombreDelArchivoFinal = archivo.substring(0, archivo.length()-8) + "Comunes\\USUARIOS.DAT";
				int nContBytesClaveEncriptadoUsuarios = 0;
				try{
					ls_lineas =getArchivoAlterno( "Comunes", "USUARIOS.DAT");
//					serial.open(ls_servidor, "Comunes", "USUARIOS.DAT", Serializacion.LECTURA, 0, 0);
				}catch(Throwable e){
					throw new Throwable ("No se encontró el archivo USUARIOS.DAT");
					//error=true;
//					return;
				}
				//System.out.println("Cargando Usuarios en Memoria ......");
				quedanMas 	 = true;
				errorEnLaLectura = false;
				leidos		 = 0;
				medidorSalida  	= new byte[long_registro];
				for(int j=0; j<ls_lineas.length;j++){
					offset = 0;
					//medidor = /* new byte[LONG_DATOS_PARAMETR_PANAMA]*/;
					medidorSalida  	= new byte[long_registro];
					for(int i=0; i<long_registro; i++) medidorSalida[i] = ' ';
//					medidorSalida[long_registro-1] = 10;
//					medidorSalida[long_registro-2] = 13;
					
						try{
							medidor=ls_lineas[j].getBytes();
							totalBytes += leidos;
							offset += leidos;
						}catch(Throwable e){
							throw new Throwable ("Archivo USUARIOS.DAT. NO RECIBIDO");
//							errorEnLaLectura = true;
//							error=true;
//							return ;
						}
					
//					nContBytesClaveEncriptadoUsuarios = globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoUsuarios);
					if (leidos == -1 || errorEnLaLectura) quedanMas = false;
					else{
						medidorSalida[0] = '!';
						for(int i=0; i<8;  i++) medidorSalida[i+21]  = medidor[i];

	// CE, La contraseña del lecturista esta encriptada, vamos a desencriptarla
						globales.tdlg.EncriptarDesencriptarConParametros(medidor, 38, 10);
						for(int i=0; i<10; i++) medidorSalida[i+41]  = medidor[i+38];

						for(int i=0; i<30; i++) medidorSalida[i+61]  = medidor[i+8];
						for(int i=0; i<1;  i++) medidorSalida[i+long_registro-8] = medidor[i+52];
						vMedidores.addElement(medidorSalida);
					}
				}
				
				//throw new Throwable ("Archivo USUARIOS.DAT. RECIBIDO");
//				try{
//					serial.close();
//				}catch(Throwable e){
//					throw new Throwable ("Error al cerrar DIS: " + e);
////					error=true;
////					return ;
//				}

				byte[] medidorObservaciones;		
				vObservaciones = new Vector();
				String strObservacionTipo;
				//strNombreDelArchivoFinal = archivo.substring(0, archivo.length()-29) + "Comunes\\ANOMALIA.DAT";
				int nContBytesClaveEncriptadoAnomalia = 0;
				try{
					ls_lineas =getArchivoAlterno( "Comunes", "ANOMALIA.DAT");
//					serial.open(ls_servidor, "Comunes", "ANOMALIA.DAT", Serializacion.LECTURA, 0, 0);
				}catch(Throwable e){
					throw new Throwable ("Archivo ANOMALIA.DAT no encontrado");
//					error=true;
//					return ;
				}

//				EnProceso ep = new EnProceso(midlet, midlet.forma,"Cargando Observaciones en Memoria ......","Espere", 0);
				
				mostrarMensaje(MENSAJE, "Cargando Observaciones en Memoria ......");
				mostrarMensaje(PROGRESO, getString(R.string.str_espere));
				
				quedanMas 	 = true;
				errorEnLaLectura = false;
				leidos		 = 0;
				for(int j=0; j<ls_lineas.length;j++){
					offset = 0;
//					medidor 	= new byte[LONG_DATOS_OBSERVACIONES_PANAMA];
					
						try{
							//leidos = serial.read(medidor);
							medidor= ls_lineas[j].getBytes();
							totalBytes += leidos;
							offset += leidos;
						}catch(Throwable e){
							errorEnLaLectura = true;
//							error=true;
//							return ;
						}
//					nContBytesClaveEncriptadoAnomalia = globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoAnomalia);
					if (leidos == -1 || errorEnLaLectura) quedanMas = false;
					else{
						vObservaciones.addElement(medidor);
					}
				}
				try{
//					serial.close();
				}catch(Throwable e){
					throw new Throwable ("Archivo ANOMALIA.DAT. NO RECIBIDO");
//					midlet.log.log("Error al cerrar DIS: " + e);		
//					error=true;
//					return ;
				}
				
//				midlet.log.log("Archivo ANOMALIA.DAT. RECIBIDO");

				//strNombreDelArchivoFinal = archivo.substring(0, archivo.length()-29) + "Comunes\\TABCOD.DAT";
				int nContBytesClaveEncriptadoTabCod = 0;
				try{
//					serial.open(ls_servidor, "Comunes", "TABCOD.DAT", Serializacion.LECTURA, 0, 0);
					ls_lineas =getArchivoAlterno( "Comunes", "TABCOD.DAT");
				}catch(Throwable e){
					throw new Throwable ("Archivo TABCOD.DAT no encontrado");
//					error=true;
//					return ;
				}
				
				mostrarMensaje(MENSAJE, "Cargando Anomalias en Memoria ......");
				mostrarMensaje(PROGRESO, getString(R.string.str_espere));
//				 ep = new EnProceso(midlet, midlet.forma,"Cargando Anomalias en Memoria ......","Espere", 0);
				quedanMas 	 = true;
				errorEnLaLectura = false;
				leidos		 = 0;
				for(int j=0; j<ls_lineas.length;j++){
					offset = 0;
					medidor 	= new byte[LONG_DATOS_ANOMALIAS_PANAMA];
					medidorSalida  	= new byte[long_registro];
					for(int i=0; i<long_registro; i++) medidorSalida[i] = ' ';
					medidorSalida[long_registro-1] = 10;
					medidorSalida[long_registro-2] = 13;
					
						try{
//							leidos = serial.read(medidor);
							medidor= ls_lineas[j].getBytes();
							totalBytes += leidos;
							offset += leidos;
						}catch(Throwable e){
							errorEnLaLectura = true;
//							error=true;
//							return ;
						}
					
//					nContBytesClaveEncriptadoTabCod = globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoTabCod);
					if (leidos == -1 || errorEnLaLectura) quedanMas = false;
					else{
							strClave 	= new String(medidor, 0, 3);
							strDescripcion 	= new String(medidor, 3, 25);
							strCompatible 	= new String(medidor, 32, 1);
							if (strCompatible.equals("1") || strCompatible.equals("2")) 	
								strCompatible = "0";
							else	strCompatible = "4";

							strFoto = "0";
							strSubAnomalias = ".";
							Enumeration o = vObservaciones.elements();
							boolean bPrimeraVez = true;
							strRequiereMensaje = "0";
							strRequiereMedidor = "0";
							while (o.hasMoreElements()){
								medidorObservaciones = (byte[]) o.nextElement();
								strAnomalia 	= new String(medidorObservaciones, 0,  3);
								if (strClave.equals(strAnomalia.trim())) {
									strObservacionTipo	= new String(medidorObservaciones, 3, 1);
									strFoto			= new String(medidorObservaciones, 4, 1);
									if (strFoto.trim().equals("")) 	strFoto = "0";
									else				strFoto = "1";
									if (strObservacionTipo.equals("1")){
										strSubAnomalias    = ".";
										strRequiereMensaje = "1";
									}else if (strObservacionTipo.equals("2")){
										strSubAnomalias    = "T";
										strRequiereMensaje = "1";
									}else if (strObservacionTipo.equals("3")){
										strSubAnomalias    = ".";
										strRequiereMedidor = "1";
									}
									if (strCompatible.equals("0"))
										medidorObservaciones[3] = '0';
									else
										medidorObservaciones[3] = '4';
								}
							}
							if (strClave.equals("066")) {
//								strRequiereMedidor = "1";
								strRequiereMensaje = "1";
							}
							strPrefijo = "#EMA" + strClave + strFoto + "0" + strRequiereMensaje + strCompatible + strSubAnomalias + strRequiereMedidor + "0" + strDescripcion;
							byte[] lectEnBytes = strPrefijo.getBytes();
							for (int i=0; i<strPrefijo.length(); i++) medidorSalida[i] = lectEnBytes[i];

							vMedidores.addElement(medidorSalida);
							//totalMedidoresLeidos ++;
					}
				}
				try{
//					serial.close();
				}catch(Throwable e){
					System.out.println("Error al cerrar DIS: " + e);	
					throw new Throwable ("Archivo TABACOD.DAT. NO RECIBIDO");
//					error=true;
//					return ;
				}
//				midlet.log.log("Archivo TABACOD.DAT. RECIBIDO");

				LONG_DATOS_ANOMALIAS_PANAMA = 40;
				//strNombreDelArchivoFinal = archivo.substring(0, archivo.length()-29) + "Comunes\\ANOMALIACOD.DAT";
				int nContBytesClaveEncriptadoAnomaliaCod = 0;
				try{
//					serial.open(ls_servidor,"Comunes", "ANOMALIACOD.DAT", Serializacion.LECTURA, 0, 0);
					ls_lineas =getArchivoAlterno( "Comunes", "ANOMALIACOD.DAT");
				}catch(Throwable e){
					throw new Throwable ("Archivo ANOMALIACOD.DAT no encontrado");
//					error=true;
//					return ;
				}
				
				mostrarMensaje(MENSAJE, "Cargando SubAnomalias en Memoria ......");
				mostrarMensaje(PROGRESO, getString(R.string.str_espere));
				
				quedanMas 	 = true;
				errorEnLaLectura = false;
				leidos		 = 0;
				int nCOntadorSubAnom = 0;
				for(int j=0; j<ls_lineas.length;j++){
					offset = 0;
					medidor 	= new byte[LONG_DATOS_ANOMALIAS_PANAMA];
					medidorSalida  	= new byte[long_registro];
					for(int i=0; i<long_registro; i++) medidorSalida[i] = ' ';
					medidorSalida[long_registro-1] = 10;
					medidorSalida[long_registro-2] = 13;
					
						try{
//							leidos = serial.read(medidor);
							medidor= ls_lineas[j].getBytes();
							totalBytes += leidos;
							offset += leidos;
						}catch(Throwable e){
							errorEnLaLectura = true;
						}
					
					//nContBytesClaveEncriptadoAnomaliaCod = globales.tdlg.EncriptarDesencriptarAlterno(medidor,nContBytesClaveEncriptadoAnomaliaCod);
					nCOntadorSubAnom++;
					if (leidos == -1 || errorEnLaLectura) quedanMas = false;
					else{
							strClave	= new String(medidor, 0, 3);
							strDescripcion 	= new String(medidor, 3, 25);
							Enumeration o = vObservaciones.elements();
							boolean bPrimeraVez = true;
							while (o.hasMoreElements()){
								medidorObservaciones = (byte[]) o.nextElement();
								strAnomalia 		= new String(medidorObservaciones, 0, 3);
								strObservacion1		= new String(medidorObservaciones, 6, 3);
								strObservacion2		= new String(medidorObservaciones,10, 3);
								int nObservacion1   	= cambiaAEntero(strObservacion1.trim());
								int nObservacion2   	= cambiaAEntero(strObservacion2.trim());
								strFoto			= new String(medidorObservaciones, 4, 1);
								if (strFoto.trim().equals("")) 	strFoto = "0";
								else				strFoto = "1";
								if ((nCOntadorSubAnom >= nObservacion1) && (nCOntadorSubAnom <= nObservacion2)) {
									if (!bPrimeraVez)	medidorSalida  	= new byte[long_registro];
									else			bPrimeraVez = false;
									for(int i=0; i<long_registro; i++) medidorSalida[i] = ' ';
									medidorSalida[long_registro-1] = 10;
									medidorSalida[long_registro-2] = 13;
		
									strCompatible = new String(medidorObservaciones, 3, 1);
									strPrefijo = "#EMA" + strAnomalia + strFoto + "00" + strCompatible + "S00" + strClave + " - " + strDescripcion;
									byte[] lectEnBytes = strPrefijo.getBytes();
									for (int i=0; i<strPrefijo.length(); i++) medidorSalida[i] = lectEnBytes[i];

									vMedidores.addElement(medidorSalida);
									//totalMedidoresLeidos ++;
								}
							}
					}
				}
				try{
//					serial.close();
				}catch(Throwable e){
					throw new Throwable ("Archivo ANOMALIACOD.DAT. NO RECIBIDO");
//					midlet.log.log("Error al cerrar DIS: " + e);			
//					error=true;
//					return ;
				}
//				midlet.log.log("Archivo ANOMALIACOD.DAT. RECIBIDO");
			
		
				String [] lineas = new String[vMedidores.size()];
				for (int i=0; i<vMedidores.size();i++){ 
					String linea=new String ((byte[])vMedidores.get(i));
					lineas[i]=linea.substring(0, globales.tdlg.long_registro );
					
				}
				
				return lineas;
		
	}
	
	public int cambiaAEntero(String str){
		int valor;
		try{
			valor = Integer.parseInt(str);
		}catch(NumberFormatException e){
			valor = 0;	
		}
		return valor;
		}
	
	
	public String[] getArchivoAlterno(String ls_carpeta, String ls_archivo) throws Throwable{
		
			cambiarExtension(ls_carpeta, ls_archivo, "txt"); 
			DefaultHttpClient  httpclient = new DefaultHttpClient();
			String ls_ruta=ls_servidor;
			ls_ruta+= !ls_ruta.endsWith("/")?"/":"";
			ls_ruta+=ls_carpeta;
			ls_ruta+= !ls_ruta.endsWith("/")?"/":"";
			ls_ruta+= ls_archivo.substring(0, ls_archivo.length()-3)+"txt";
			
			ls_ruta=cambiaCaracter(ls_ruta, "\\","/");
			
			HttpGet httppost = new HttpGet(ls_ruta);
			HttpResponse response = httpclient.execute(httppost);
			        HttpEntity ht = response.getEntity();

			        BufferedHttpEntity buf = new BufferedHttpEntity(ht);

			        InputStream is = buf.getContent();
		      		
//			        cambiarExtension(ls_carpeta, ls_archivo.substring(0, 9)+"txt",ls_archivo.substring(ls_archivo.length()-3, ls_archivo.length())); 
		      		byte [] toBytes= getBytesFromInputStream(is);
		      		globales.tdlg.EncriptarDesencriptarAlterno(toBytes, 0);
		      		
		      		String pool=new String(toBytes);
		      		if (pool.startsWith("<!DOCTYPE")){
		      			
		      			throw new Throwable("No se encontró el archivo");
		      			
		      		}
		      		
		      		String[] lineas=pool.split("\\n");
		      		
		      		return lineas;
		      		
		      		
		}
	
	
	public static byte[] getBytesFromInputStream(InputStream inStream)
			 throws Throwable {

			  // Get the size of the file
			 long streamLength = inStream.available();

			  if (streamLength > Integer.MAX_VALUE) {
			 // File is too large
			 }

			  // Create the byte array to hold the data
			 byte[] bytes = new byte[(int) streamLength];

			  // Read in the bytes
			 int offset = 0;
			 int numRead = 0;
			 while (offset < bytes.length
			  && (numRead = inStream.read(bytes,
			  offset, bytes.length - offset)) >= 0) {
			  offset += numRead;
			 }

			  // Ensure all the bytes have been read in
			 if (offset < bytes.length) {
			 throw new Throwable("Could not completely read file ");
			 }

			  // Close the input stream and return bytes
			 inStream.close();
			 return bytes;
			 }
	
	private void cambiarExtension(String is_carpeta, String is_archivo, String extension) throws Throwable{
		//HCG 16/07/2012 Esta funcion es la encargada de abrir la conexion con el WIFI o cualquier tipo de conexion a internet.
		//Recibe:
			//		ls_urlConArchivo: ubicacion y nombre del archivo
		
		
		//Cambiaremos la forma de leer los archivos por wifi, ahora, recibiremos la pagina web. Hay que estar atentos porque hay dos posibilidades
		//-Que devuelva la pagina web
		//-Que devuelva los valores realmente solicitados
		Hashtable params = new Hashtable();
		HttpMultipartRequest http ;
		params.put("extension", extension);
		String ruta="";
		if (is_carpeta.equals(""))
			ruta=is_archivo;
		else
			ruta=is_carpeta+ "\\" +is_archivo;
		//params.put("cadena",cadenaAEnviar);
		params.put("ruta", ruta);
		params.put("carpeta", is_carpeta);
		params.put("archivo", is_archivo);
		
		
		try {
			http = new HttpMultipartRequest(ls_servidor + "/cambiarExtension.php", params, "upload_field","", "text/plain", new String("").getBytes());
			//byte[] response=http.send();
			//new String (response); Esta es la respuesta del servidor
			
			byte [] bytesAEnviar=http.send();
			String s= new String(bytesAEnviar);

			
			
			}
		catch(FileNotFoundException e2){
		
		}
			

	}
	
static String cambiaCaracter(String ls_cadena, String ls_caracter, String ls_caracter2){
		
		while (ls_cadena.indexOf(ls_caracter)!=-1){
			ls_cadena=ls_cadena.substring(0, ls_cadena.indexOf(ls_caracter)) +ls_caracter2+ ls_cadena.substring(ls_cadena.indexOf(ls_caracter)+1);
		}
		
		return ls_cadena;
		
	}
	

public void enviarBackup(String ls_ruta, String ls_file){
	//Como es muy importante que la fecha siempre sea la actual,  agregaremos una validacion para que ellos esten al tanto que la fecha sea la mas actual
	Hashtable params = new Hashtable();
	boolean esCorrecta=true;
	byte[] response;
	params.put("ruta", ls_ruta);
	params.put("archivo", ls_file);
	params.put("backup", getNombreArchivoBackup(ls_file));
	
	
	
	HttpMultipartRequest http ;
	try {
		http = new HttpMultipartRequest(ls_servidor + "/createBackup.php", params, "upload_field","", "text/plain", new String("").getBytes());
		response=http.send();
		new String(response);
		
		}catch(Throwable e){
			
		}
}

public String getNombreArchivoBackup(String ls_nombre){
	int li_pos;
	li_pos=ls_nombre.indexOf(".");
	
	ls_nombre= ls_nombre.substring(0, li_pos)+ "_"+Main.obtieneFecha("ymdhis")+ls_nombre.substring(li_pos);
	return ls_nombre;
}

public void onBackPressed(){
	super.onBackPressed();
    if (puedoCerrar){
    	this.muere(false, "La operacion ha sido cancelada");
    	try{
    		serial.close();
    	}catch(Throwable e){
    		
    	}
 	    
    }
    
    cancelar=true;
   
}


}
