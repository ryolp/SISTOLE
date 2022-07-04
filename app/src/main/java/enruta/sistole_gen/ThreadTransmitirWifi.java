package enruta.sistole_gen;

import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class ThreadTransmitirWifi extends TimerTask {
	final static int TRANSMITIR_FOTOS = 1;
	final static int TRANSMITIR_LECTURAS = 2;
	final static int TRANSMITIR_FOTOS_Y_LECTURAS = 3;
	
	/**
	 * Muestra el mensaje que tiene el progreso de un poceso
	 */
	final static int MENSAJE_PROGRESO=0;
	/**
	 * Muestra el mensaje que resulto del proceso de transmision
	 */
	final static int RESULTADO_TRANSMISION=1;

	int cantidad;
	Serializacion serial;
	Globales globales;
	boolean puedoCerrar = true;
	trasmisionDatos dt = null;
	boolean transmitirLecturas = true;
	boolean mostrarMensajes = false;
	boolean transmiteFotos = true;
	String ls_carpeta;
	String ls_servidor;
	DBHelper dbHelper;
	SQLiteDatabase db;
	Handler handler;
	
	boolean prioridadFotos=false;
	boolean esUnServicio=false;
	boolean activa=true;
	
	int tiempoDeActivacion=120000;
	
	Main activity;
	boolean finalizarUnaTarea=false;
	
	
	Timer tarea;

	ThreadTransmitirWifi(Serializacion serial, Globales globales,
			String carpeta, String servidor/* , int tipoTransmision */) {
		tarea=new Timer();
		this.serial = serial;
		this.globales = globales;
		this.ls_carpeta = carpeta;
		this.ls_servidor = servidor;
		prioridadFotos=true;
		esUnServicio=true;
		// switch(tipoTransmision){
		// case TRANSMITIR_FOTOS:
		// transmitirFotos=true;
		// break;
		// case TRANSMITIR_LECTURAS:
		// transmitirLecturas=true;
		// break;
		// case TRANSMITIR_FOTOS_Y_LECTURAS:
		// transmitirLecturas=true;
		// transmitirFotos=true;
		// break;
		// }
	}

	ThreadTransmitirWifi(Serializacion serial, Globales globales,
			trasmisionDatos dt) {
		tarea=new Timer();
		this.serial = serial;
		this.globales = globales;
		this.dt = dt;
		prioridadFotos=false;
		mostrarMensajes = true;
		
		ls_carpeta=dt.ls_carpeta;
		ls_servidor=dt.ls_servidor;
	}

	public void run() {
		if(!activa){
			return;
		}
		
		boolean envioExistoso=true;

		// TODO Auto-generated method stub
		serial = new Serializacion(Serializacion.WIFI);
		String ls_cadena = "";
		byte[] lby_registro, lby_cadenaEnBytes;
		String ls_nombre_final;
		cancelarNotificacion(RESULTADO_TRANSMISION);
		
		mostrarNotificacion(MENSAJE_PROGRESO,R.drawable.ic_action_exportar,  "Iniciando el envio", "");

		switch (globales.modoDeCierreDeLecturas) {
		case Globales.FORZADO:
			puedoCerrar = false;
			if (mostrarMensajes) {
				dt.mostrarMensaje(dt.PROGRESO,
						dt.getString(R.string.msj_trans_forzando));
				// Abrimos el arreglo de todas las lecturas y forzamos
				dt.tll.forzarLecturas();
				dt.cancelar = false;
			}

			break;
		}
		puedoCerrar = true;
		Cursor c = null;
		try {
			if (transmitirLecturas) {
			
				do{
					trasnmitirLecturas();
					
					mostrarNotificacion(MENSAJE_PROGRESO,R.drawable.ic_action_exportar, "Enviando Lecturas", "");
					c = globales.tdlg.lineasAEscribir(db);

					cantidad = c.getCount();
					c.close();
					//mostrarNotificacion("Se encontraron mas lecturas.... espera");
				}while(prioridadFotos && cantidad>0  && activa);
				

			}
			
			mostrarNotificacion(MENSAJE_PROGRESO,R.drawable.ic_action_exportar,"Enviando Fotos", "");

			// Aqui enviamos los no registrados

			if (transmiteFotos) {

				if (mostrarMensajes) {
					dt.mostrarMensaje(dt.PROGRESO,
							dt.getString(R.string.msj_trans_generando_fotos));
					dt.mostrarMensaje(dt.MENSAJE,
							dt.getString(R.string.str_espere));
				}

				openDatabase();

				String ls_query = "select nombre, foto , rowid from fotos ";
				if (globales.enviarSoloLoMarcado)
					ls_query += " where envio= 1;";

				c = db.rawQuery(ls_query, null);

				cantidad = c.getCount();

				if (mostrarMensajes) {
					dt.mHandler.post(new Runnable() {
						public void run() {
							dt.pb_progress.setMax(cantidad);
						}
					});
				}

				// closeDatabase();

				// String ls_capertaFotos = subirDirectorio(ls_carpeta, 2)
				// + "/fotos/" + ls_subCarpeta + "/"
				// + Main.obtieneFecha("ymd");

				// String ls_capertaFotos = subirDirectorio(ls_carpeta, 2)
				// + "/fotos";

				String ls_capertaFotos = globales.tdlg
						.carpetaDeFotos(ls_carpeta);

				c.moveToFirst();

				for (int i = 0; i < c.getCount(); i++) {
					if (mostrarMensajes) {
						dt.stop();
					}

					serial.open(ls_servidor, ls_capertaFotos, "",
							Serializacion.ESCRITURA, 0, 0);

					String nombreFoto = c.getString(c.getColumnIndex("nombre"));
					if (globales.quitarPrimerCaracterNombreFoto) {
						nombreFoto = nombreFoto.substring(1);
					}
					// ls_cadena=generaCadenaAEnviar(c);
					serial.write(nombreFoto,
							c.getBlob(c.getColumnIndex("foto")));

					String bufferLenght;
					int porcentaje = ((i + 1) * 100) / c.getCount();
					bufferLenght = String.valueOf(c.getCount());
					serial.close();
					openDatabase();

					String whereClause = "rowid=?";
					String[] whereArgs = { c.getString(c
							.getColumnIndex("rowid")) };
					ContentValues cv_datos = new ContentValues(1);

					// if (!transmitirTodo) {
					cv_datos.put("envio", TomaDeLecturas.ENVIADA);

					int j = db
							.update("fotos", cv_datos, whereClause, whereArgs);
					// }
					// closeDatabase();
					// Marcar como enviada
					c.moveToNext();
					if (mostrarMensajes) {
						dt.mostrarMensaje(
								dt.MENSAJE,
								(i + 1) + " " + dt.getString(R.string.de) + " "
										+ bufferLenght + " "
										+ dt.getString(R.string.str_fotos)
										+ ".\n" + String.valueOf(porcentaje)
										+ "%");
						dt.mostrarMensaje(dt.BARRA, String.valueOf(1));
					}else{
						mostrarNotificacion(MENSAJE_PROGRESO,R.drawable.ic_action_exportar,"Enviando Fotos",(i + 1) + " " + globales.getString(R.string.de) + " "
								+ bufferLenght + " "
								+ globales.getString(R.string.str_fotos)
								+ ".\n" + String.valueOf(porcentaje)
								+ "%");
					}

				}
				
			}

//			// Actualizamos las ordenes, ya que ya se enviaron
//			if (globales.enviarSoloLoMarcado)
//				db.execSQL("Update fotos set envio=0");

			if (mostrarMensajes) {
				dt.mostrarMensaje(dt.MENSAJE, dt.getString(R.string.str_espere));
				// serial.close();
				dt.yaAcabo = true;
			}
			// mostrarMensaje(PROGRESO, "Mandando datos al servidor");

			if (transmitirLecturas) {

				marcarComoDescargada();
			}
			if(mostrarMensajes){
				dt.muere(true, String.format(dt.getString(R.string.msj_trans_correcta),
						dt.getString(R.string.str_exportado)));
			}
			
			c.close();
			cancelarNotificacion(MENSAJE_PROGRESO);
			mostrarNotificacion(RESULTADO_TRANSMISION,R.drawable.ic_stat_hecho, "SISTOLE", "Las lecturas han sido envidas");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			
			envioExistoso=false;
			cancelarNotificacion(MENSAJE_PROGRESO);
			mostrarNotificacion(RESULTADO_TRANSMISION,R.drawable.ic_stat_error, "Error de Envio", "No hay internet/Servidor no disponible");
			e.printStackTrace();
			if(mostrarMensajes){
				dt.muere(true,
						String.format(dt.getString(R.string.msj_trans_error),
								dt.getString(R.string.str_exportar_lowercase))
								+ e.getMessage());
			}
			
		}catch (Throwable e) {
			// TODO Auto-generated catch block
			envioExistoso=false;
			cancelarNotificacion(MENSAJE_PROGRESO);
			mostrarNotificacion(RESULTADO_TRANSMISION,R.drawable.ic_stat_error, "Error de Envio", e.getClass().getName()+" Msg: " +e.getMessage());
			e.printStackTrace();
			if(mostrarMensajes){
				dt.muere(true,
						String.format(dt.getString(R.string.msj_trans_error),
								dt.getString(R.string.str_exportar_lowercase))
								+ e.getMessage());
			}
			
		} finally {
			closeDatabase();
			
			if (activity!=null){
				activity.mHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.actualizaTabs();
					}
					
				});
				
			}
			
			final boolean envioExistoso2=envioExistoso;
			if (finalizarUnaTarea){
				activity.mHandler.post(new Runnable(){

					@Override
					public void run() {
						activity.regresoDeEnviar(envioExistoso2);
						finalizarUnaTarea=false;
					}
				
			});
				
			}

			// dialog.cancel();
			if (esUnServicio && activa){
				detieneTarea();
				iniciaTarea(tiempoDeActivacion);
			}
			 
		}

	}

	public void openDatabase() {
		dbHelper = new DBHelper(globales);

		db = dbHelper.getReadableDatabase();
	}

	public void closeDatabase() {
		if (db.isOpen()) {
			db.close();
			dbHelper.close();
		}

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

	protected void marcarComoDescargada() {
		openDatabase();

		db.execSQL("update encabezado set descargada=1");

		closeDatabase();

	}
	
	protected void trasnmitirLecturas() throws Throwable{
		if (mostrarMensajes) {
			dt.mostrarMensaje(dt.PROGRESO,
					dt.getString(R.string.str_espere));
		}

		openDatabase();

		String[] ls_params = { String
				.valueOf(TomaDeLecturas.NO_ENVIADA) };

		// if (globales.tlc.is_camposDeSalida.equals(""))
		// c = db.rawQuery("select * from Ruta ", null);
		// else
		// c = db.rawQuery("select " + globales.tlc.is_camposDeSalida +
		// " as TextoSalida from Ruta ", null);
		Cursor c = null;
		byte[] lby_registro, lby_cadenaEnBytes;
		String ls_nombre_final;
		
		c = globales.tdlg.lineasAEscribir(db);

		cantidad = c.getCount();
		if (mostrarMensajes) {
			dt.mHandler.post(new Runnable() {
				public void run() {
					dt.pb_progress.setMax(cantidad);
				}
			});

			dt.mostrarMensaje(dt.PROGRESO,
					dt.getString(R.string.msj_trans_generando));
			dt.mostrarMensaje(dt.MENSAJE,
					dt.getString(R.string.str_espere));
		}

		if (!globales.enviarSoloLoMarcado|| globales.transmitirTodo)
			borrarArchivo(ls_carpeta
					+ "/"
					+ globales.tdlg
							.getNombreArchvio(TomaDeLecturasGenerica.SALIDA));

		serial.open(ls_servidor, ls_carpeta, globales.tdlg
				.getNombreArchvio(TomaDeLecturasGenerica.SALIDA),
				Serializacion.ESCRITURA, 0, 0);
		for (int i = 0; i < cantidad; i++) {
			if (mostrarMensajes)
				dt.stop();
			c.moveToPosition(i);

			// ls_cadena=generaCadenaAEnviar(c);
			// lby_cadenaEnBytes=ls_cadena.getBytes();

			// Ya tenemos los datos a enviar (que emocion!) asi que
			// hay que agregarlos a la cadena final

			lby_registro = c.getString(c.getColumnIndex("TextoSalida"))
					.getBytes();

			// for (int j=0; j<lby_cadenaEnBytes.length;j++)
			// lby_registro[j+resources.getInteger(R.integer.POS_DATOS_TIPO_LECTURA)]=lby_cadenaEnBytes[j];

			String ls_cadenaAEnviar = "";
			// if (globales.tlc.is_CamposDeSalida.equals("")) {
			// ls_cadenaAEnviar = new String(c.getBlob(c
			// .getColumnIndex("registro")));
			// if (ls_cadenaAEnviar.length() > globales.tlc
			// .getLongCampo("registro"))
			// ;
			// ls_cadenaAEnviar = ls_cadenaAEnviar.substring(0,
			// globales.tdlg.long_registro);
			// }else{
			ls_cadenaAEnviar = new String(c.getString(c
					.getColumnIndex("TextoSalida")));
			// }
			// Escribimos los bytes en el archivo
			serial.write(ls_cadenaAEnviar + "\r\n");

			String bufferLenght;
			int porcentaje = ((i + 1) * 100) / c.getCount();
			bufferLenght = String.valueOf(c.getCount());

			/*
			 * openDatabase();
			 * 
			 * String whereClause="secuencial=?"; String[] whereArgs=
			 * {String.valueOf(c.getLong(c.getColumnIndex("secuencial"
			 * )))}; ContentValues cv_datos=new ContentValues(1);
			 * 
			 * cv_datos.put("envio",TomaDeLecturas.ENVIADA);
			 * 
			 * int j=db.update("lecturas", cv_datos, whereClause,
			 * whereArgs);
			 * 
			 * closeDatabase();
			 */
			// Marcar como enviada
			if (mostrarMensajes) {
				dt.mostrarMensaje(
						dt.MENSAJE,
						(i + 1) + " " + dt.getString(R.string.de) + " "
								+ bufferLenght + " "
								+ dt.getString(R.string.registros)
								+ ".\n" + String.valueOf(porcentaje)
								+ "%");
				dt.mostrarMensaje(dt.BARRA, String.valueOf(1));
			}else{
				mostrarNotificacion(MENSAJE_PROGRESO,R.drawable.ic_action_exportar, "Enviando Lecturas", (i + 1) + " " + globales.getString(R.string.de) + " "
						+ bufferLenght + " "
						+ globales.getString(R.string.registros)
						+ ".\n" + String.valueOf(porcentaje)
						+ "%");
			}
			

			

			// Actualizamos las ordenes, ya que ya se enviaron
			if (globales.enviarSoloLoMarcado){
				serial.close();
				db.execSQL("Update ruta set envio=0 where secuenciaReal="+ c.getInt(c
						.getColumnIndex("secuenciaReal")));
			}
				

		}
		c.close();
		
			serial.close();
		if (mostrarMensajes) {
			dt.mostrarMensaje(
					dt.PROGRESO,
					dt.getString(R.string.msj_trans_generando_no_registrados));
			dt.mostrarMensaje(dt.MENSAJE,
					dt.getString(R.string.str_espere));
			dt.mostrarMensaje(dt.BARRA, String.valueOf(0));
		}

		String ls_query = "select poliza, rowid from NoRegistrados ";

		if (globales.enviarSoloLoMarcado)
			ls_query += "where  envio= 1;";

		c = db.rawQuery(ls_query, null);

		ls_nombre_final = globales.tdlg
				.getNombreArchvio(TomaDeLecturasGenerica.NO_REGISTRADOS);
		if (!globales.enviarSoloLoMarcado)
			borrarArchivo(ls_carpeta + "/" + ls_nombre_final);

		cantidad = c.getCount();

		serial.open(ls_servidor, ls_carpeta, ls_nombre_final,
				Serializacion.ESCRITURA, 0, 0);

		if (mostrarMensajes) {
			dt.mHandler.post(new Runnable() {
				public void run() {
					dt.pb_progress.setMax(cantidad);
				}
			});
		}

		for (int i = 0; i < cantidad; i++) {
			if (mostrarMensajes) {
				dt.stop();
			}

			c.moveToPosition(i);

			// ls_cadena=generaCadenaAEnviar(c);
			// lby_cadenaEnBytes=ls_cadena.getBytes();

			// Ya tenemos los datos a enviar (que emocion!) asi que
			// hay que agregarlos a la cadena final

			lby_registro = c.getBlob(c.getColumnIndex("poliza"));

			// for (int j=0; j<lby_cadenaEnBytes.length;j++)
			// lby_registro[j+resources.getInteger(R.integer.POS_DATOS_TIPO_LECTURA)]=lby_cadenaEnBytes[j];

			// Escribimos los bytes en el archivo
			serial.write(new String(lby_registro) + "\r\n");

			String bufferLenght;
			int porcentaje = ((i + 1) * 100) / c.getCount();
			bufferLenght = String.valueOf(c.getCount());

			/*
			 * openDatabase();
			 * 
			 * String whereClause="secuencial=?"; String[] whereArgs=
			 * {String.valueOf(c.getLong(c.getColumnIndex("secuencial"
			 * )))}; ContentValues cv_datos=new ContentValues(1);
			 * 
			 * cv_datos.put("envio",TomaDeLecturas.ENVIADA);
			 * 
			 * int j=db.update("lecturas", cv_datos, whereClause,
			 * whereArgs);
			 * 
			 * closeDatabase();
			 */
			
			
			// Actualizamos las ordenes, ya que ya se enviaron
			if (globales.enviarSoloLoMarcado){
				serial.close();
				db.execSQL("Update NoRegistrados set envio=0 where rowid="+c.getInt(c.getColumnIndex("rowid")));
			}
				
			
			if (mostrarMensajes) {
				dt.mostrarMensaje(
						dt.MENSAJE,
						(i + 1) + " " + dt.getString(R.string.de) + " "
								+ bufferLenght + " "
								+ dt.getString(R.string.registros)
								+ ".\n" + String.valueOf(porcentaje)
								+ "%");
				dt.mostrarMensaje(dt.BARRA, String.valueOf(1));
			}
			else{
				mostrarNotificacion(MENSAJE_PROGRESO,R.drawable.ic_action_exportar, "Enviando No Registrados", (i + 1) + " " + globales.getString(R.string.de) + " "
						+ bufferLenght + " "
						+ globales.getString(R.string.registros)
						+ ".\n" + String.valueOf(porcentaje)
						+ "%");
			}
			// Marcar como enviada

		}

					
				

		c.close();

		// Actualizamos las ordenes, ya que ya se enviaron
			serial.close();
//			db.execSQL("Update NoRegistrados set envio=0");
			

		for (int archivo : globales.tdlg
				.getArchivosATransmitir(TransmisionesPadre.WIFI)) {
			// Aqui enviamos los no registrados

			if (mostrarMensajes) {
				dt.mostrarMensaje(dt.PROGRESO, globales.tdlg
						.regresaMensajeDeTransmision(archivo));
				dt.mostrarMensaje(dt.MENSAJE,
						dt.getString(R.string.str_espere));
				dt.mostrarMensaje(dt.BARRA, String.valueOf(0));
			}

			c = globales.tdlg.getContenidoDelArchivo(db, archivo);

			ls_nombre_final = globales.tdlg.getNombreArchvio(archivo);
			borrarArchivo(ls_carpeta + "/" + ls_nombre_final);

			cantidad = c.getCount();

			serial.open(ls_servidor, ls_carpeta, ls_nombre_final,
					Serializacion.ESCRITURA, 0, 0);

			if (mostrarMensajes) {
				dt.mHandler.post(new Runnable() {
					public void run() {
						dt.pb_progress.setMax(cantidad);
					}
				});
			}

			for (int i = 0; i < cantidad; i++) {
				if (mostrarMensajes) {
					dt.stop();
				}

				c.moveToPosition(i);

				// ls_cadena=generaCadenaAEnviar(c);
				// lby_cadenaEnBytes=ls_cadena.getBytes();

				// Ya tenemos los datos a enviar (que emocion!) asi que
				// hay que agregarlos a la cadena final

				lby_registro = c.getBlob(c.getColumnIndex("texto"));

				// for (int j=0; j<lby_cadenaEnBytes.length;j++)
				// lby_registro[j+resources.getInteger(R.integer.POS_DATOS_TIPO_LECTURA)]=lby_cadenaEnBytes[j];

				// Escribimos los bytes en el archivo
				serial.write(new String(lby_registro) + "\r\n");

				String bufferLenght;
				int porcentaje = ((i + 1) * 100) / c.getCount();
				bufferLenght = String.valueOf(c.getCount());

				/*
				 * openDatabase();
				 * 
				 * String whereClause="secuencial=?"; String[]
				 * whereArgs=
				 * {String.valueOf(c.getLong(c.getColumnIndex(
				 * "secuencial" )))}; ContentValues cv_datos=new
				 * ContentValues(1);
				 * 
				 * cv_datos.put("envio",TomaDeLecturas.ENVIADA);
				 * 
				 * int j=db.update("lecturas", cv_datos, whereClause,
				 * whereArgs);
				 * 
				 * closeDatabase();
				 */
				// Marcar como enviada
				if (mostrarMensajes) {
					dt.mostrarMensaje(
							dt.MENSAJE,
							(i + 1) + " " + dt.getString(R.string.de)
									+ " " + bufferLenght + " "
									+ dt.getString(R.string.registros)
									+ ".\n"
									+ String.valueOf(porcentaje) + "%");
					dt.mostrarMensaje(dt.BARRA, String.valueOf(1));
				}

			}
			serial.close();

			c.close();

		}
		
		

		if (mostrarMensajes) {
			dt.mostrarMensaje(dt.BARRA, String.valueOf(0));
		}
	}
	
	public void iniciaTarea(int milisegundos){
		activa=true;
		
		if (globales.ttw_timer_a_apagar!=null){
			globales.ttw_timer_a_apagar.activa=false;
		}
		
		globales.ttw_timer_a_apagar=new ThreadTransmitirWifi(serial, globales,
				ls_carpeta, ls_servidor);
		globales.ttw_timer_a_apagar.activa=true;
		globales.ttw_timer_a_apagar.activity=activity;
		globales.ttw_timer_a_apagar.finalizarUnaTarea=finalizarUnaTarea;
		tarea.schedule(globales.ttw_timer_a_apagar,milisegundos);
		
	}
	
	public void enviaYFinaliza(Main actividad){
		finalizarUnaTarea=true;
		this.activity=actividad;
		
		iniciaTarea(0);
		
	}
	
	public void detieneTarea(){
		if (globales.ttw_timer_a_apagar!=null){
			globales.ttw_timer_a_apagar.activa=false;
		}
		
		if (tarea==null){
			return;
		}
		try {
			tarea.cancel();
		} catch (Throwable e) {

		}

		
		tarea.purge();
		globales.ttw_timer_a_apagar=null;
		tarea= new Timer();
	}
	
	public void mostrarNotificacion(int id,int icono, String titulo, String mensaje){
		if (mostrarMensajes){
			return;
		}
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(globales)
		        .setSmallIcon(icono)
		        .setContentTitle(titulo)
		        .setContentText(mensaje);
		// Creates an explicit intent for an Activity in your app
//		Intent resultIntent = new Intent(globales, ResultActivity.class);
//
//		// The stack builder object will contain an artificial back stack for the
//		// started Activity.
//		// This ensures that navigating backward from the Activity leads out of
//		// your application to the Home screen.
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(globales);
//		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(ResultActivity.class);
//		// Adds the Intent that starts the Activity to the top of the stack
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//		        stackBuilder.getPendingIntent(
//		            0,
//		            PendingIntent.FLAG_UPDATE_CURRENT
//		        );
//		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) globales.getSystemService(Context.NOTIFICATION_SERVICE);
//		// mId allows you to update the notification later on.
		mNotificationManager.notify(id, mBuilder.build());
		
		
	}
	
	void cancelarNotificacion(int id){
		if (mostrarMensajes){
			return;
		}
		NotificationManager mNotificationManager =
			    (NotificationManager) globales.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(id);
	}

}
