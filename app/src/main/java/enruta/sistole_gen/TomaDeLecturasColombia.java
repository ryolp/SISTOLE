package enruta.sistole_gen;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.Vector;

import enruta.sistole_gen.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/** Esta clase crea las validaciones y los campos a mostrar**/
public class TomaDeLecturasColombia extends TomaDeLecturasGenerica {
	
	public final static int MEDIDOR_ANTERIOR=0;
	public final static int MEDIDOR_POSTERIOR=1;
	public final static int NUM_ESFERAS=2;
	public final static int NUM_MEDIDOR=3;
	public final static int MARCA=4;
	public final static int CALLE=5;
	public final static int NUMERO=6;
	public final static int PORTAL=7;
	public final static int ESCALERA=8;
	public final static int PISO=9;
	public final static int PUERTA=10;
	public final static int COMPLEMENTO=11;
	public final static int LECTURA=12;
	public final static int TIPO=13;
	public final static int OBSERVACIONES=14;
	
	
	//archivos a transmitir
	public final static int ARCHIVO_MENSAJES=2;
	
	Vector <TextView> textViews= new Vector<TextView>();
	MensajeEspecial mj_estaCortado;
	MensajeEspecial mj_sellos;
	MensajeEspecial mj_consumocero;
	MensajeEspecial mj_ubicacionVacia;
	MensajeEspecial mj_anomalia_seis;
	Hashtable <String, Integer> ht_calidades;
	
// CE, REVISAR
	public TomaDeLecturasColombia(Context context) {
		super(context);
		long_registro=279;
		
		procesosAlEntrar();
		
		
		//Creamos los campos que serán de salida
		globales.tlc.getListaDeCamposFormateado();
		
		
		
		mj_estaCortado= new MensajeEspecial("¿Sigue Cortado?", PREGUNTAS_SIGUE_CORTADO);
		mj_estaCortado.cancelable=false;
		Vector <Respuesta> respuesta= new Vector <Respuesta> () ;
		respuesta.add(new Respuesta("0", "Con Sellos"));
		respuesta.add(new Respuesta("1", "Sin Sellos"));
		respuesta.add(new Respuesta("2", "Reconectado"));
		
		mj_sellos=new MensajeEspecial("¿Tiene Sellos?", respuesta, PREGUNTAS_EN_EJECUCION);
		
		respuesta= new Vector <Respuesta> () ;
		respuesta.add(new Respuesta("4J", "J-Sin Servicio"));
		respuesta.add(new Respuesta("4O", "O-No usan gas"));
		respuesta.add(new Respuesta("4P", "P-Refaccion"));
		respuesta.add(new Respuesta("4Q", "Q-Bien Tomado"));
		respuesta.add(new Respuesta("4U", "U-Desocupado"));
		respuesta.add(new Respuesta("4X", "X-Cerrado Vacaciones"));
		mj_consumocero=new MensajeEspecial("Consumo Cero. Seleccione una de las opciones", respuesta, PREGUNTAS_CONSUMO_CERO);
		mj_consumocero.cancelable=false;
		
		respuesta= new Vector <Respuesta> () ;
		respuesta.add(new Respuesta("1A", "A - Accesible"));
		respuesta.add(new Respuesta("1B", "B - Batería"));
		respuesta.add(new Respuesta("1V", "V - Vivienda"));
		respuesta.add(new Respuesta("1R", "R - Accesible tras reja"));
		mj_ubicacionVacia=new MensajeEspecial("Capt. Ubicación", respuesta, PREGUNTAS_UBICACION_VACIA);
		mj_ubicacionVacia.cancelable=true;
		
		respuesta= new Vector <Respuesta> () ;
		respuesta.add(new Respuesta("G", "G - Vidrio empañado u opaco"));
		respuesta.add(new Respuesta("R", "R - Puerta trabada"));
		respuesta.add(new Respuesta("Z", "Z - Acceso Bloqueado"));
		respuesta.add(new Respuesta("5", "5 - No puede acceder por barrios carenciados"));
		mj_anomalia_seis=new MensajeEspecial("Seleccione", respuesta, ANOMALIA_SEIS);
		mj_anomalia_seis.cancelable=false;
		
		globales.logo=R.drawable.logo;
		
		globales.multiplesAnomalias=false;
		globales.convertirAnomalias=false;
		
		globales.longitudCodigoAnomalia=2;
		globales.longitudCodigoSubAnomalia=3;
		
		globales.rellenoAnomalia=".";
		globales.rellenarAnomalia=false;
		
		globales.repiteAnomalias=false;
		
		globales.remplazarDireccionPorCalles=true;
		
		globales.mostrarCuadriculatdl=true;
		
		globales.mostrarRowIdSecuencia=true;
		
		globales.dejarComoAusentes=true;
		
		globales.mensajeDeConfirmar=R.string.msj_lecturas_verifique_1;
		
		globales.mostrarNoRegistrados=true;
		globales.tipoDeValidacion=Globales.SIN_VALIDACION;
		globales.mensajeContraseñaLecturista=R.string.str_login_msj_lecturista_contrasena_arg;
		globales.controlCalidadFotos=0;
		
		globales.sonidoCorrecta=Sonidos.NINGUNO;
		globales.sonidoIncorrecta=Sonidos.BEEP;
		globales.sonidoConfirmada=Sonidos.NINGUNO;
		
		globales.mostrarMacImpresora=false;
		globales.mostrarServidorGPRS=true;
		globales.mostrarFactorBaremo=false;
		globales.mostrarTamañoFoto=false;
		globales.mostrarMetodoDeTransmision=true;
		globales.mostrarIngresoFacilMAC=false;
		
		globales.defaultLote="0202575";
		globales.defaultCPL="GNM001";
		globales.defaultTransmision="2";
		globales.defaultRutaDescarga="C:\\Apps\\SGL\\Lectura";
		
		globales.letraPais="C";
		
		globales.mostrarCodigoUsuario=true;
		
		globales.tomaMultiplesFotos=false;
		
		globales.porcentaje_main=1.0;
		globales.porcentaje_main2=1.0;
		globales.porcentaje_hexateclado=.74998;
		globales.porcentaje_teclado=.6410;
		globales.porcentaje_lectura=1.0;
		globales.porcentaje_info=1.1;
		
		globales.calidadDeLaFoto=20;
		
		globales.modoDeCierreDeLecturas=Globales.NINGUNO;
		
		globales.mostrarGrabarEnSD=false;
		
		globales.mostrarCalidadFoto=false;
		
		globales.legacyCaptura=true;
		
		//Escondemos los elementos de la cuadricula que no nos interesan
		globales.ver_celda0=true;
		globales.ver_celda1=true;
		globales.ver_celda2=true;
		globales.ver_celda3=true;
		globales.ver_celda4=false;
		
		globales.DiferirEntreAnomInstYMed=true;
		
		globales.maxIntentos=3;
		
		globales.rellenarVaciaLectura=false;
		
		globales.puedoCancelarFotos=true;
		globales.mostrarOtraFoto=false;
		
		globales.tabs =new int [2] ;
		
		globales.tabs[0]= R.string.lbl_medidor;
		globales.tabs[1]=  R.string.lbl_edificios;
		
		globales.longCampoContrasena=4;
		
		globales.fuerzaEntrarComoSuperUsuarioAdmon=true;
		
		globales.bloquearBotonesLecturaObligatoria=true;
		
		respuestaBusquedaCalles=R.string.msj_buscar_cant_edificios_encontrados;
		
		 globales.modoDeBanderasAGrabar=Globales.BANDERAS_CONF_COLOMBIA;
		 
		 ultimoBloqueCapturadoDefault="0500";
		
		InicializarMatrizDeCompatibilidades();
		inicializaTablaDeCalidades();
	}

	/**
	 * Validacion de una lectura
	 * @param ls_lectAct
	 * @return Regresa el mensaje de error
	 */
// CE, REVISAR
	public String validaLectura(String ls_lectAct) {
		
		globales.ignorarTomaDeFoto=false;

		
		if (ls_lectAct.length() > globales.tll.getLecturaActual().numerodeesferas) {
			return NO_SOSPECHOSA + "|" + globales.getString(R.string.msj_validacion_esferas);
		}

		if (ls_lectAct.equals("")) {
			return NO_SOSPECHOSA +"|"+ globales.getString(R.string.msj_validacion_no_hay_lectura);
		}

		long ll_lectAct = Long.parseLong(ls_lectAct);
		
		long ll_consumo = getConsumo(ls_lectAct);
		
		
		
		if (ll_consumo < 0 ){
			ll_consumo = (long) (Math.pow(10, globales.tll.getLecturaActual().numerodeesferas) + ll_consumo);
		}
		
		if (globales.il_lect_max >= ll_lectAct && globales.il_lect_min <= ll_lectAct
				&&  !globales.tll.getLecturaActual().confirmarLectura() && ll_consumo<=globales.tll.getLecturaActual().baremo /*|| globales.bModificar*/) {
			
//			//Dentro del rango pero... sale del baremo
//			if (ll_consumo>globales.tll.getLecturaActual().baremo){
//				is_lectAnt = ls_lectAct;
//				globales.contadorIntentos++;
//				return /*TomaDeLecturas.FUERA_DE_RANGO;*/ SOSPECHOSA +"|"+ globales.getString(globales.mensajeDeConfirmar);
//			}
			
			globales.ignorarTomaDeFoto=true;
			
			if (globales.bModificar){
				globales.tll.getLecturaActual().distinta="1";
				globales.tll.getLecturaActual().confirmada="1";
			}
			else if(is_lectAnt.equals("")){
				globales.tll.getLecturaActual().distinta="0";
				globales.tll.getLecturaActual().confirmada="0";
			}
			else if(!is_lectAnt.equals("")){
				globales.tll.getLecturaActual().distinta="1";
			}
			is_lectAnt = "";
			return "";
		}
		
//		if (is_lectAnt.equals("")) {
//			is_lectAnt = ls_lectAct;
//			globales.contadorIntentos++;
//			return /*TomaDeLecturas.FUERA_DE_RANGO;*/ SOSPECHOSA +"|"+ globales.getString(globales.mensajeDeConfirmar);
//		}
		
		
		if (!is_lectAnt.equals(ls_lectAct)) {
			
			
			if (!is_lectAnt.equals("") || globales.bModificar){
				globales.tll.getLecturaActual().intentos=String.valueOf(Lectura.toInteger(globales.tll.getLecturaActual().intentos) +1 );		// CE, REVISAR Aqui tenemos que aceptar al tercer intento
				globales.tll.getLecturaActual().distinta="1";
			}
			is_lectAnt = ls_lectAct;
			//Contador de lectura distinta
			
			//sonidos.playSoundMedia(Sonidos.URGENT);																	// CE, REVISAR Aqui debemos ver si activamos los sonidos
			globales.ignorarContadorControlCalidad=true;
			
			globales.contadorIntentos++;
			
			if (globales.contadorIntentos<globales.maxIntentos){
				return /*NO_*/SOSPECHOSA +"|"+ globales.getString(R.string.msj_lecturas_verifique);
			}
			
			
		}else{
			globales.tll.getLecturaActual().confirmada="1";
		}


//		if (is_lectAnt.equals("")) {
//			//sonidos.playSoundMedia(Sonidos.BEEP);
//		}
		if (globales.bModificar){
			globales.tll.getLecturaActual().distinta="1";
			globales.tll.getLecturaActual().confirmada="1";
		}

		globales.ignorarContadorControlCalidad=true;
		is_lectAnt = "";
		return "";
	}
	
// CE, REVISAR
	public String getNombreFoto(Globales globales, SQLiteDatabase db, long secuencial, String is_terminacion, String ls_anomalia ){
		String ls_nombre="", ls_unicom;
		Cursor c;
		
		/**
		 * Este es el fotmato del nombre de la foto
		 * 
		 * Poliza a 8 posiciones,  
		 * 
		 * la terminacion... -1 Regularmente
		 * Si es de anomalia ... La anomalia ingresada
		 * 
		 */
    	
    	c= db.rawQuery("Select poliza from ruta where cast(secuenciaReal as Integer) ="+secuencial, null);
    	c.moveToFirst();
    	
    	ls_nombre+=Main.rellenaString(c.getString(c.getColumnIndex("poliza")), "0", 8, true);

    	c.close();
		
    	
    	
    	//Hay que preguntar por la terminacion
    	ls_nombre+=(!ls_anomalia.equals("")?"-A":"") +".JPG";
    	
    	return ls_nombre;
	}

// CE, REVISAR
	public  Vector<String> getInformacionDelMedidor(Lectura lectura) {
		Vector <String> datos= new Vector<String>();
		
		//Esta variable la usaremos para poder determinar si algun dato es vacio y mostrar solo lo necesario en la pantalla
		String comodin="";
		
		datos.add(lectura.getDireccion() + " #" +lectura.numeroDeEdificio.trim() + (!lectura.numeroDePortal.trim().equals("")?"-" +lectura.numeroDePortal.trim():""));

		//Escalera
		if (!lectura.is_escalera.trim().equals("")){
			comodin="Esc: "+ lectura.is_escalera.trim();
		}
		
		//Piso 
		if (!lectura.is_piso.trim().equals("")){
			if (!comodin.equals(""))
				comodin+=" ";
			comodin+="Piso: "+ lectura.is_piso.trim();
		}
		
		// Puerta
		if (!lectura.is_puerta.trim().equals("")){
			if (!comodin.equals(""))
				comodin+=" ";
			comodin+="Puerta: "+ lectura.is_puerta.trim();
		}
		
		if (!comodin.equals("")){
			datos.add(comodin);
		}
		
		datos.add(lectura.getNombreCliente().trim());

		datos.add("Marca de Med. : " + globales.tll.getLecturaActual().getMarcaDeMedidorAMostrar());

//		datos.add("Anom. de Med. : " + globales.tll.getLecturaActual().getAnomaliaAMostrar());
//		datos.add("Anom. de Inst.: " + globales.tll.getLecturaActual().getAnomaliaDeInstalacionAMostrar());
		
		
		openDatabase();
		
		Cursor c=db.rawQuery("Select max(cast(secuencia as int)) contEdificio from ruta where  sinUso1='"+globales.tll.getLecturaActual().sinUso1+"'", null); 
		c.moveToFirst();
		int contEdificio= c.getInt(c.getColumnIndex("contEdificio"));
		closeDatabase();

		datos.add("Punto de Suministro: " + globales.tll.getLecturaActual().secuencia +" de " +contEdificio);
		datos.add("Medidor En la Ruta : " + (globales.mostrarRowIdSecuencia?globales.tll.getLecturaActual().secuenciaReal:globales.il_lect_act) + " de " + globales.il_total);

		//Vamos a agregar los campos que se van llenando mientras se agregan anomalias
		String ls_anom=lectura.getAnomaliasCapturadas();
		String ls_comentarios="";
//		if (!globales.tll.getLecturaActual().getAnomaliaAMostrar().equals("")) {
//			// Tiene una anomalia
//			ls_comentarios =context.getString(R.string.str_anomalia)+": "  + globales.is_presion;
//			if (!globales.tll.getLecturaActual().getSubAnomaliaAMostrar().equals("")) {
//				// Tiene una subanomalia
//				ls_comentarios += /*", " */ "\n"+ context.getString(R.string.str_subanomalia)+": " 
//						+ globales.tll.getLecturaActual().getSubAnomaliaAMostrar();
//			}
//			ls_comentarios += "\n";
//		}
		
		if (globales.tll.getLecturaActual().getComentarios().length()>18)
			ls_comentarios=/*"\n" + ls_comentarios + */globales.tll.getLecturaActual().getComentarios().substring(18);
		
		
		if (!ls_comentarios.trim().equals(""));
			datos.add(ls_comentarios);
			
			c.close();
		
		return datos;
	}

	@Override
	public MensajeEspecial getMensaje() {
		return null;
	}
	
// CE, REVISAR   **** LAS ANOMALIAS DE MEDIDOR SON COMPATIBLES CON LAS ANOMALIAS DE INSTALACION Y VICEVERSA ****
	private void InicializarMatrizDeCompatibilidades(){
		anomaliasCompatibles= new Hashtable<String, String>();
		anomaliasCompatibles.put("M", "I");		// CE, Habria que ver como escribir esto de manera generica
		anomaliasCompatibles.put("I", "M");		// CE, Habria que ver como escribir esto de manera generica
	}

// CE, REVISAR
	@Override
	public boolean esAnomaliaCompatible(String anomaliaAInsertar, String anomaliasCapturadas) {
		int lastIndexOfStar = 0;
		boolean esCompatible = true;
		
//		String cadena=anomaliasCompatibles.get(anomaliaAInsertar);
//		
//		for (int i=lastIndexOfStar; i<anomaliasCapturadas.length();i++){
//			if(cadena.indexOf(anomaliasCapturadas.substring(i, i+1))<0){
//				esCompatible= false;
//				break;
//			}
//		}
		return esCompatible;
	}

	@Override
	public ComentariosInputBehavior getAvisoMensajeInput(String anomalia) {
		ComentariosInputBehavior cib_config=null;
		return cib_config;
	}

	@Override
	public void RealizarModificacionesDeAnomalia(String anomalia, String comentarios) {
	}
	
	@Override
	public void RealizarModificacionesDeAnomalia(String anomalia) {
	}

	@Override
	public void DeshacerModificacionesDeAnomalia(String anomalia) {
	}

	@Override
	public MensajeEspecial mensajeDeConsumo(String ls_lectAct) {
		return null;
	}

	@Override
	public void RespuestaMensajeSeleccionada(MensajeEspecial me, int respuesta) {
	}

	@Override
	public ComentariosInputBehavior getCampoGenerico(int campo) {
		ComentariosInputBehavior cib_config=null;
		
		switch(campo){
		case CALLE:
			String direccion="";
//			openDatabase();
//			Cursor c= db.rawQuery("Select poliza from noregistrados", null);
//			c.moveToFirst();
//			if (c.getCount()>0){
//				direccion= c.getString(c.getColumnIndex("poliza"));
//				direccion=direccion.substring(16, 30);
//				direccion=direccion.trim();
//			}
//			
//			closeDatabase();
			cib_config = new ComentariosInputBehavior("Calle", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 30, direccion);
		break;
		case PUERTA:
			cib_config = new ComentariosInputBehavior("Número Exterior", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 6, "");
		break;
		case NUM_MEDIDOR:
			cib_config = new ComentariosInputBehavior("Número de Serie", InputType.TYPE_CLASS_NUMBER, 9, "");
		break;
		case NUM_ESFERAS:
			cib_config = new ComentariosInputBehavior("Número de Esferas", InputType.TYPE_CLASS_NUMBER, 1,"");
		break;
		case LECTURA:
			cib_config = new ComentariosInputBehavior("Lectura", InputType.TYPE_CLASS_NUMBER, 7, "");
		break;
		case MARCA:
			cib_config = new ComentariosInputBehavior("Marca Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 10, "");
		break;
		case TIPO:
			cib_config = new ComentariosInputBehavior("Tipo de Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 10,"");
			break;
		case OBSERVACIONES:
			cib_config = new ComentariosInputBehavior("Observaciones", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 22, "");
			cib_config.obligatorio=false;
		break;
		
		}
		
		return cib_config;
	}

	@Override
	public int[] getCamposGenerico(String anomalia) {
		int [] campos=null;
		
		
		if (anomalia.equals("noregistrados")) {
			campos= new int[8];
			campos[0]=NUM_MEDIDOR ;
			campos[1]=MARCA ;
			campos[2]=TIPO;
			campos[3]=NUM_ESFERAS;
			campos[4]=LECTURA;
			campos[5]=CALLE;
			campos[6]=PUERTA;
			campos[7]=OBSERVACIONES;
		} 
		
		return campos;
	}

	@Override
	public void regresaDeCamposGenericos(Bundle bu_params, String anomalia) {
		
		if (anomalia.equals("noregistrados")) {
			openDatabase();
			Cursor c= db.rawQuery("Select registro from encabezado", null);
			c.moveToFirst();
			String lote =new String(c.getBlob(c.getColumnIndex("registro")));
			lote=lote.substring(23, 30);
			String cadena=lote;
			c.close();
			cadena +=globales.ultimoBloqueCapturado;
			cadena +="..++";
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(CALLE)), " ", 30, false) ;
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(PUERTA)), " ", 6, false) ;
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(NUM_MEDIDOR)), " ", 9, false) ;
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(NUM_ESFERAS)), " ", 1, false) ;
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(LECTURA)), "0", 7, true) ;
			cadena +=Main.rellenaString( bu_params.getString(String.valueOf(MARCA)), " ", 10, false);
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(TIPO)), " ", 10, false) ;
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(OBSERVACIONES)), " ", 22, false) ;
			cadena +=Main.obtieneFecha("dmy");
			cadena +=" ";
			
			
			//Guardamos en la bd
			
			db.execSQL("insert into noRegistrados values('"+ cadena+"')");
			closeDatabase();
			
		} 
		else{
			openDatabase();
			
			Cursor c= db.rawQuery("Select desc from Anomalia where  trim(anomalia)='" + anomalia.substring(0, 2)+"'", null);
			
			c.moveToFirst();
			
			String desc= c.getString(c.getColumnIndex("desc"));
			
			desc=Main.rellenaString(desc, " ", 18, false);
			c.close();
			closeDatabase();
			
			globales.tll.getLecturaActual().setComentarios(desc+bu_params.getString("input"));
		}
		
	}

// CE, REVISAR
	@Override
	public ContentValues getCamposBDAdicionales() {
		// TODO Auto-generated method stub
		ContentValues cv_params = new ContentValues();
		cv_params.put("intento1", "");
		cv_params.put("intento2", "");
		cv_params.put("intento3", "");
		cv_params.put("intento4", "");
		cv_params.put("intento5", "");
		cv_params.put("intento6", "");
		cv_params.put("intentos", 0);
		cv_params.put("sospechosa", "0");
		cv_params.put("nisRad", 0);
		cv_params.put("dondeEsta", "");
		cv_params.put("anomInst", "");
		cv_params.put("sectorCorto", "");
		cv_params.put("sectorLargo", "");
		cv_params.put("comoLlegar2", "");
		cv_params.put("comoLlegar1", "");
		return cv_params;
	}

// CE, REVISAR
	@Override
	public void creaTodosLosCampos() {
			globales.tlc.add(new Campo(0, "sinUso1", 0, 14, Campo.I, " "));
			globales.tlc.add(new Campo(1, "secuencia", 14, 4, Campo.D, "0"));
			globales.tlc.add(new Campo(2, "cliente", 18, 35, Campo.I, " "));
			globales.tlc.add(new Campo(3, "direccion", 53, 27, Campo.I, " "));
			globales.tlc.add(new Campo(4, "numEdificio", 80, 4, Campo.I, " "));
			globales.tlc.add(new Campo(5, "numPortal", 84, 3, Campo.I, " "));
			globales.tlc.add(new Campo(6, "aviso", 87, 18, Campo.I, " "));
			globales.tlc.add(new Campo(7, "lecturaAnterior", 105, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(8, "baremo", 112, 7, Campo.D, "0"));					// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***
			globales.tlc.add(new Campo(9, "advertencias", 119, 2, Campo.I, " "));
			globales.tlc.add(new Campo(10, "ilr", 121, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(11, "ism", 128, 1, Campo.D, "0"));
			globales.tlc.add(new Campo(12, "saldoEnMetros", 129, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(13, "tipoLectura", 136, 1, Campo.I, " "));
			globales.tlc.add(new Campo(14, "lectura", 137, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(15, "consumo", 144, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(16, "fecha", 151, 6, Campo.F, "dhi"));
			globales.tlc.add(new Campo(17, "anomalia", 157, 2, Campo.I, "0"));
			globales.tlc.add(new Campo(18, "divisionContrato", 159, 2, Campo.D, "0"));
			globales.tlc.add(new Campo(19, "sospechosa", 161, 1, Campo.D, " "));//Confirmadax
			globales.tlc.add(new Campo(20, "intentos", 162, 1, Campo.D, " "));//Distinta		// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***
			globales.tlc.add(new Campo(21, "anomaliaDeInstalacion", 163, 2, Campo.D, "0"));//Distinta		// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***
			globales.tlc.add(new Campo(22, "marcaMedidor", 165, 2, Campo.I, " "));
			globales.tlc.add(new Campo(23, "tipoMedidor", 167, 13, Campo.I, " "));
			globales.tlc.add(new Campo(24, "serieMedidor", 180, 10, Campo.D, " "));
			globales.tlc.add(new Campo(25, "colonia", 190, 25, Campo.I, " "));
			globales.tlc.add(new Campo(26, "tarifa", 215, 2, Campo.I, " "));
			globales.tlc.add(new Campo(26, "sinUso3", 217, 5, Campo.I, " "));
			globales.tlc.add(new Campo(27, "poliza", 222, 8, Campo.D, " "));
			globales.tlc.add(new Campo(28, "numEsferas", 230, 1, Campo.D, " "));
			globales.tlc.add(new Campo(29, "consAnoAnt", 231, 10, Campo.D, "0"));				// CE, REVISAR, *** ESTE CAMPO NO SE USABA EN ARGENITNA, AQUI ES IMPORTANTE ***
			globales.tlc.add(new Campo(30, "consBimAnt", 241, 10, Campo.D, "0"));				// CE, REVISAR, *** ESTE CAMPO NO SE USABA EN ARGENITNA, AQUI ES IMPORTANTE ***
			globales.tlc.add(new Campo(31, "hora", 251, 6, Campo.F, "his"));
			globales.tlc.add(new Campo(32, "lecturista", 257, 4, Campo.I, " "));
			globales.tlc.add(new Campo(33, "ordenDeLectura", 261, 4, Campo.D, "0"));
			globales.tlc.add(new Campo(34, "escalera", 265, 3, Campo.I, " "));
			globales.tlc.add(new Campo(35, "piso", 268, 2,  Campo.I, " "));
			globales.tlc.add(new Campo(36, "puerta", 270, 5, Campo.D, " "));
			globales.tlc.add(new Campo(37, "reclamacionLectura", 275, 1, Campo.D, " "));
			globales.tlc.add(new Campo(38, "supervisionLectura", 276, 1, Campo.D, " "));
			globales.tlc.add(new Campo(39, "reclamacion", 277, 1, Campo.D, " "));
			globales.tlc.add(new Campo(40, "supervision", 278, 1, Campo.D, " "));
			
			globales.tlc.add(new Campo(41, "numEsferasReal", 278, 0, Campo.D, " "));
			globales.tlc.add(new Campo(42, "fechaAviso", 278, 0, Campo.F, "dmy"));
			globales.tlc.add(new Campo(43, "serieMedidorReal", 278, 0, Campo.D, " "));
			globales.tlc.add(new Campo(44, "ubicacion", 278, 0, Campo.I, " "));
			globales.tlc.add(new Campo(45, "estimaciones", 278, 0, Campo.I, " "));
			globales.tlc.add(new Campo(46, "comentarios", 278, 0, Campo.I, " "));
			globales.tlc.add(new Campo(47, "subAnomalia", 278, 0, Campo.I, " "));
			globales.tlc.add(new Campo(48, "estadoDelSuministroReal", 278, 0, Campo.D, " "));
			globales.tlc.add(new Campo(49, "estadoDelSuministro", 278, 0, Campo.D, " "));
			
	}

// CE, REVISAR
	@Override
	public long getLecturaMinima() {
		return globales.tll.getLecturaActual().ilr + globales.tll.getLecturaActual().consAnoAnt; 
	}

// CE, REVISAR
	@Override
	public long getLecturaMaxima() {
		return globales.tll.getLecturaActual().ilr + globales.tll.getLecturaActual().consBimAnt;
	}

// CE, REVISAR
	@Override
	public String obtenerContenidoDeEtiqueta(String ls_etiqueta) {
		// TODO Auto-generated method stub
		if (ls_etiqueta.equals("campo0")){
			return globales.tll.getLecturaActual().poliza.trim();
		}
		else if (ls_etiqueta.equals("campo1")){
			return globales.tll.getLecturaActual().is_tarifa.trim();
		}
		else if (ls_etiqueta.equals("campo2")){
			return globales.tll.getLecturaActual().getAnomalia();
		}
		else if (ls_etiqueta.equals("campo3")){
			return globales.tll.getLecturaActual().is_anomaliaDeInstalacion;
		}
		else if (ls_etiqueta.equals("campo4")){
			return globales.tll.getLecturaActual().getSubAnomaliaAMostrar();
		}
		else{
			return "";
		}
	}
	

	public String obtenerTituloDeEtiqueta(String ls_etiqueta) {
		if (ls_etiqueta.equals("campo2")){
			return "A.deM.";
		}
		else if (ls_etiqueta.equals("campo3")){
			return "A.deI.";
		}
		else if (ls_etiqueta.equals("campo4")){
			return "Sub.";
		}
		else{
			return super.obtenerTituloDeEtiqueta(ls_etiqueta);
		}
	}

// CE, REVISAR
	@Override
	public FormatoDeEtiquetas getMensajedeRespuesta() {
		// TODO Auto-generated method stub
		if (!(globales.tll.getLecturaActual().is_tarifa.equals("C1") || globales.tll.getLecturaActual().is_tarifa.equals("CE")))
			return new FormatoDeEtiquetas("Cliente Doméstico", R.color.green);
		else
			return new FormatoDeEtiquetas("Cliente Comercial", R.color.blue);
	}

	@Override
	public String getMensajedeAdvertencia() {
		// TODO Auto-generated method stub
		if (globales.tll.getLecturaActual().is_advertencias.equals("00") && globales.tll.getLecturaActual().is_advertenciasTipoAdicionales.trim().equals(""))
				return "";
		else if (globales.tll.getLecturaActual().is_advertenciasTipoAdicionales.trim().equals(""))
			return globales.tll.getLecturaActual().is_advertencias.trim();
		else{
			return globales.tll.getLecturaActual().is_advertenciasTipoAdicionales.trim();
		}
	}

	@Override
	public void regresaDeBorrarLectura() {
	}

// CE, REVISAR
	@Override
	public void cambiosAnomaliaAntesDeGuardar(String ls_lect_act) {
		// TODO Auto-generated method stub
	}

	@Override
	public void anomaliasARepetir() {
	}

	@Override
	public void subAnomaliasARepetir() {
	}

	@Override
	public boolean avanzarDespuesDeAnomalia(String ls_anomalia, String ls_subAnom, boolean guardar) {
//		if (esSegundaVisita(ls_anomalia, ls_subAnom)){
//			//Grabamos
//			if (guardar)
//				globales.tll.getLecturaActual().guardar(true, globales.tll.getSiguienteOrdenDeLectura());
//			return true;
//		}
		return false;
	}
	
	@Override
	public boolean esSegundaVisita(String ls_anomalia, String ls_subAnom) {
		return false;
	}
	
// CE, REVISAR
	@Override
	public String getDescripcionDeBuscarMedidor(Lectura lectura,
			int tipoDeBusqueda, String textoBuscado) {
		String ls_preview="";
		
		switch (tipoDeBusqueda) {
		case BuscarMedidorTabsPagerAdapter.MEDIDOR:
			ls_preview = Lectura.marcarTexto(lectura.is_serieMedidor, textoBuscado, false);
			if (!lectura.getColonia().equals(""))
				ls_preview += "<br>" + lectura.getColonia();
			ls_preview += "<br>" +lectura.getDireccion() + " #" +lectura.numeroDeEdificio +" " +lectura.numeroDePortal;
			ls_preview += "<br>" +lectura.is_marcaMedidor + " " +lectura.is_tipoMedidor;
			break;

		case BuscarMedidorTabsPagerAdapter.DIRECCION:
			ls_preview =  lectura.is_serieMedidor;
			if (!lectura.getColonia().equals(""))
				ls_preview += "<br>"
						+ Lectura.marcarTexto(lectura.getColonia(), textoBuscado, true);
			ls_preview += "<br>" + lectura.getDireccion() + " #" +lectura.numeroDeEdificio;
			break;

		case BuscarMedidorTabsPagerAdapter.NUMERO:
			ls_preview =  lectura.is_serieMedidor;
			if (!lectura.getColonia().equals(""))
				ls_preview += "<br>" + lectura.getColonia();
			ls_preview += "<br>" + Lectura.marcarTexto(lectura.getDireccion() + " #" +lectura.numeroDeEdificio, textoBuscado, true);
			break;
			
		case BuscarMedidorTabsPagerAdapter.CALLES:
			ls_preview = Lectura.marcarTexto(lectura.getDireccion(), textoBuscado, true);
			break;
		}
		
		return ls_preview;
	}

	@Override
	public String validaAnomalia(String ls_anomalia) {
		return "";
	}

	@Override
	public String getPrefijoComentario(String ls_anomalia) {
		return "";
	}

	@Override
	public void repetirAnomalias() {
	}
	
// CE, REVISAR
	@Override
	public void setConsumo(){
		long ll_consumo=0;
		String anomCapturadas=globales.tll.getLecturaActual().getAnomaliasCapturadas();
		
		if (globales.is_lectura.equals("")){
			globales.tll.getLecturaActual().is_consumo=Main.rellenaString("", " ", globales.tlc.getLongCampo("consumo"), true);
			return ;
		}
		
		//Hay que checar que las anomalias capturadas sean las que se dejan como AUSENTES
		
		ll_consumo = getConsumo(globales.is_lectura);
		
		if (ll_consumo < 0 ){
			ll_consumo = (long) (Math.pow(10, globales.tll.getLecturaActual().numerodeesferas) + ll_consumo);
		}
//		if (globales.tll.getLecturaActual().is_ism == 2){															// CE REVISAR Pondre en cometarios pues hay que revisar tipos de datos
//			ll_consumo = (long) (ll_consumo + globales.tll.getLecturaActual().is_saldoEnMetros);
//		}
//		if (globales.tll.getLecturaActual().is_ism == 1) {
//			if (globales.tll.getLecturaActual().is_saldoEnMetros < ll_consumo)){
//				ll_consumo = (long) (ll_consumo - globales.tll.getLecturaActual().is_saldoEnMetros);
//			}else if (globales.tll.getLecturaActual().ilr < globales.tll.getLecturaActual().lecturaAnterior) {
//				if (globales.tll.getLecturaActual().is_saldoEnMetros < ll_consumo)
//					ll_consumo = (long) (ll_consumo - globales.tll.getLecturaActual().is_saldoEnMetros);
//				else
//					ll_consumo = 0;
//			}
//		}
		
		if (ll_consumo<=20 && ll_consumo<=globales.tll.getLecturaActual().baremo && !globales.tll.getLecturaActual().confirmarLectura()){
			globales.ignorarTomaDeFoto=true;
		}
		
		globales.tll.getLecturaActual().is_consumo=String.valueOf(ll_consumo);
	}

	@Override
	public long getConsumo(String lectura) {
//		return  Long.parseLong(lectura) - globales.tll.getLecturaActual().lecturaAnterior ;
		return  Long.parseLong(lectura) - globales.tll.getLecturaActual().ilr ;					// CE, REVISAR Cambiamos la LecturaAnterior por ILR
	}
	
	public void setTipoLectura(){
		super.setTipoLectura();
	}

	@Override
	public String validaCamposGenericos(String anomalia, Bundle bu_params) {
		return "";
	}
	
	@Override
	public MensajeEspecial regresaDeAnomalias(String ls_anomalia) {
		openDatabase();
		
		Cursor c;
		Bundle bu_params= new Bundle();
		c= db.rawQuery("Select * from anomalia where substr(desc, 1, 3)='"+ls_anomalia+"'", null);
		
		if (c.getCount()==0){
			c.close();
			closeDatabase();
			return null;
		}
		c.moveToFirst();
		
		if (c.getString(c.getColumnIndex("subanomalia")).equals("S")){

			
			
			
			String desc= c.getString(c.getColumnIndex("desc"));
			
			
			
			
			//globales.tll.getLecturaActual().setComentarios(desc+ls_anomalia);
			bu_params.putString("input", desc);
			c.close();
			closeDatabase();
			regresaDeCamposGenericos(bu_params, ls_anomalia.substring(0, 2));
			return null;
		}
		
		
		c.close();
		closeDatabase();
		
		return null;
	}

	@Override
	public boolean puedoRepetirAnomalia() {
		return false;
	}

	@Override
	public String remplazaValorDeArchivo(int tipo, String ls_anomalia,  String valor){
		return valor;
	}
	
	@Override
	public void cambiosAnomalia(String anomalia){
	}
	
	public void cambiosAlBorrarAnomalia(String anomaliaBorrada){
	}
	
	public void inicializaTablaDeCalidades(){
	}
	
	@Override
	public int cambiaCalidadSegunTabla(String Anomalia, String subAnomalia){
		Integer calidad = 20;
		return calidad.intValue();
	}
	
	public boolean continuarConLaFoto() {
		return true;
	}
	
	public void AgregarAnomaliasManualmente(SQLiteDatabase db) {
//		final TomaDeLecturasColombia context = this;
//		SQLiteDatabase db;
		try {
			//openDatabase();
			//db.beginTransaction();
			//new Anomalia(context, "#CMA00 0110.00NADA                                                     ", db);
			new Anomalia(context, "#CMA02 1110T00LECTURA REAL CON INCIDENCIA                              ", db);
			new Anomalia(context, "#CMA02 1100S00021 - MDR ENCERRADO CON LECTURA                          ", db);
			new Anomalia(context, "#CMA02 1100S00022 - MDR EN LOTE BALDIO                                 ", db);
			new Anomalia(context, "#CMA02 1100S00023 - MDR PRESENTE Y CON DEFECTO                         ", db);
			new Anomalia(context, "#CMA05 1110.00SELLOS ROTOS                                             ", db);
			new Anomalia(context, "#CMA07 1014.00POSIBLE FRAUDE                                           ", db);
			new Anomalia(context, "#CMA11 1110.00OBRAS REMODELACION                                       ", db);
			new Anomalia(context, "#CMA12 1110.00ROTO CRISTAL                                             ", db);
			new Anomalia(context, "#CMA14 0110.00PREDIO DESHABITADO                                       ", db);
			new Anomalia(context, "#CMA15 0110.00FUGA                                                     ", db);
			new Anomalia(context, "#CMA16 1110.00ESFERAS ILEGIBLES                                        ", db);
			new Anomalia(context, "#CMA20 0110T00ORD. LECT INCOR                                          ", db);
			new Anomalia(context, "#CMA20 0100S00201 - FUERA DE DISTRITO                                  ", db);
			new Anomalia(context, "#CMA20 0100S00202 - FUERA DE SECTOR                                    ", db);
			new Anomalia(context, "#CMA20 0100S00203 - FUERA DE SECUENCIA                                 ", db);
			new Anomalia(context, "#CMA21 0110.00DIRECCION INCORRECTA                                     ", db);
			new Anomalia(context, "#CMA28 0014.00DIRECCION ILOCALIZADA                                    ", db);
			new Anomalia(context, "#CMA31 1014.00PREDIO DEMOLIDO                                          ", db);
			new Anomalia(context, "#CMA35 0110.00RUIDOS                                                   ", db);
			new Anomalia(context, "#CMA37 1014T00CONTADOR LEVANTADO                                       ", db);
			new Anomalia(context, "#CMA37 1004S00371 - ROBADO                                             ", db);
			new Anomalia(context, "#CMA37 1004S00372 - LEV. CLIENTE                                       ", db);
			new Anomalia(context, "#CMA37 1004S00373 - LEV. EMPRESA                                       ", db);
			new Anomalia(context, "#CMA38 1014.00CONTADOR ILOCALIZABLE                                    ", db);
			new Anomalia(context, "#CMA41 1014T00CONTA. ENCERRADO                                         ", db);
			new Anomalia(context, "#CMA41 1004S00411 - ANTEJARDIN                                         ", db);
			new Anomalia(context, "#CMA41 1004S00412 - GARAJE                                             ", db);
			new Anomalia(context, "#CMA41 1004S00413 - NICHO                                              ", db);
			new Anomalia(context, "#CMA43 1110.00NUM CONTADOR ERR                                         ", db);
			new Anomalia(context, "#CMA44 1014.00MEDIDOR TAPADO                                           ", db);
			new Anomalia(context, "#CMA48 1110.00CONTADOR INVERTIDO O NO MARCA                            ", db);
			new Anomalia(context, "#CMA51 1110T00ID COMERCIAL                                             ", db);
			new Anomalia(context, "#CMA51 1100S00511 - HOTELES CON SERV. GAS NATURAL                      ", db);
			new Anomalia(context, "#CMA51 1100S00512 - CENTROS DE SALUD CON SERV. GN.                     ", db);
			new Anomalia(context, "#CMA51 1100S00513 - ESTABLEC. ALIMENTICIOS CON GN                      ", db);
			new Anomalia(context, "#CMA51 1100S00514 - INST. EDUCATIVAS CON SERV. GN                      ", db);
			new Anomalia(context, "#CMA51 1100S00515 - SUPERMERCADOS CON SERV. GN                         ", db);
			new Anomalia(context, "#CMA54 0014.00ADMON. NO PERMITE INGRESO                                ", db);
			new Anomalia(context, "#CMA61 1110T00MEDIDOR DE DIFICIL ACCESO                                ", db);
			new Anomalia(context, "#CMA61 1100S00611 - ALTO, NO PROVEEN HERRAMIENTAS                      ", db);
			new Anomalia(context, "#CMA61 1100S00612 - PELIGROSO                                          ", db);
			new Anomalia(context, "#CMA61 1100S00613 - DIFICIL ACCESO                                     ", db);
			new Anomalia(context, "#CIA70 1110.00CAMPANA LECTURA Y FOTO                                   ", db);
			new Anomalia(context, "#CIA71 1014.00CAMPANA LECTURA Y FOTO AUSENTE                           ", db);
			new Anomalia(context, "#CIA72 1010.00CAMPANA FOTO                                             ", db);
			new Anomalia(context, "#CIA73 1014.00CAMPANA FOTO Y AUSENTE                                   ", db);
			new Anomalia(context, "#CIA74 0110.00CAMPANA LECTURA                                          ", db);
			new Anomalia(context, "#CIA75 0014.00CAMPANA LECTURA Y AUSENTE                                ", db);
			new Anomalia(context, "#CMA97 1014.00ODOMETRO AVERIADO                                        ", db);
			new Anomalia(context, "#CMA99 1014.00MEDIDOR SIN INSTALAR                                     ", db);
			
			
		} catch (Throwable e) {
			
		} finally {
			try {
				//db.endTransaction();
			} catch (Throwable e) {
				
			}
			//closeDatabase();
		}
		
	}
	
	
	public void accionesDespuesDeCargarArchivo(SQLiteDatabase db){
		db.execSQL("update ruta set lectura='', anomalia='' , anomaliaDeInstalacion=''");
		
		
	}
	
	public void agregarRegistroRaro(SQLiteDatabase db, String registro){
		if (registro.startsWith("A")){//Es un comentario
			//Cortamos el comentario
			String comentario=registro.substring(10);
			String poliza=registro.substring(1, 9);
			
			//openDatabase();
			db.execSQL("update ruta set advertenciasTipoAdicionales='"+comentario+"' where poliza='" +poliza+"'");
//			db.execSQL("update ruta set advertencias='"+comentario+"' where poliza='" +poliza+"'");
			//closeDatabase();
		}
	}
	
	public boolean esUnRegistroRaro(String registro){
		if (registro.startsWith("A")){
			return true;
		}
		return false;
	}
	
	
	public int [] getArchivosATransmitir(){
		int archivos []={ARCHIVO_MENSAJES};
		return archivos;
	}
	
	public String getNombreArchvio(int tipo){
		
		String ls_archivo="";
		
		switch(tipo){
		case ARCHIVO_MENSAJES:
			
			ls_archivo=globales.lote+".MEN";
			return ls_archivo;
		}
		
		return super.getNombreArchvio(tipo);
	}
	
public Cursor getContenidoDelArchivo(SQLiteDatabase db, int tipo){
		
		String ls_select="";
		
		Cursor c=null;
		
		switch(tipo){
		case ARCHIVO_MENSAJES:
			c= db.rawQuery("Select registro from encabezado", null);
			c.moveToFirst();
			String lote =new String(c.getBlob(c.getColumnIndex("registro")));
			
			lote=lote.substring(23, 30);
			String cadena=lote;
			
			ls_select="'C'"+" || " + globales.tlc.getCampoObjeto("direccion").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("numEdificio").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("divisionContrato").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("poliza").campoSQLFormateado() ;
			ls_select+=" || " + " substr(poliza,  8, 1)"  ;
//			ls_select+=" || " + globales.tlc.getCampoObjeto("sinUso1").campoSQLFormateado() ;
			ls_select+=" || " + " sinUso1 "  ;
			ls_select+=" || '" + lote ;
			ls_select+="' || " + " substr(sinUso1,  6, 5)"  ;
			ls_select+=" || " + TodosLosCampos.campoSQLFormateado("comentarios", 50, " ", Campo.I);
			
			ls_select+=" || " + " fechaAviso texto"  ;
			
			ls_select="Select " + ls_select + " from ruta where trim(comentarios)<>''";
			
			c.close();
			
			c=db.rawQuery(ls_select, null);
			
			break;
		}
		 
		return c;
	}


public String regresaMensajeDeTransmision(int tipo){
	switch(tipo){
	case ARCHIVO_MENSAJES:
		return "Transmitiendo Mensajes";

	}
	
	return "";
}


	public int opcionAElegir(int tipo){
		
		
		switch(tipo){
		case ARCHIVO_MENSAJES:
			return trasmisionDatosBt.OPCION_CPL_A_PC_MENSAJES;
	
		}
		
		return 0;
		
	}
	
	
	public Vector <EstructuraResumen> getResumen(SQLiteDatabase db){
		
		Cursor c;
		  long ll_total;
		long ll_tomadas;
    	long ll_fotos;
    	long ll_restantes;
    	long ll_conAnom;
    	long ll_noRegistrados;
    	
    	long ll_mensajes;
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
    	 
    	 c=db.rawQuery("Select count(*) canti from ruta where trim(comentarios)<>''", null);
    	 c.moveToFirst();
    	 ll_mensajes=c.getLong(c.getColumnIndex("canti"));
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
    	 resumen.add(new EstructuraResumen("Lecturas Realizadas", String.valueOf(ll_tomadas),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_conAnom*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Lecturas Ausentes",String.valueOf(ll_conAnom), formatter.format(porcentaje) +"%"));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	 
    	 resumen.add(new EstructuraResumen("Mensajes", String.valueOf(ll_mensajes)));
    	 if (globales.mostrarNoRegistrados)
    		 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_no_registrados), String.valueOf(ll_noRegistrados)));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	 return resumen;
		
	}
	
	public void activacionDesactivacionOpciones(boolean esSuperUsuario){
			if (esSuperUsuario){
				globales.mostrarMetodoDeTransmision=true;
				globales.mostrarServidorGPRS=true;
				globales.mostrarBorrarRuta=true;
			}
			else{
				globales.mostrarMetodoDeTransmision=false;
				globales.mostrarServidorGPRS=false;
				globales.mostrarBorrarRuta=false;
			}
		}

	
	public void procesosAlEntrar(){
openDatabase();
		
		Cursor c= db.rawQuery("Select * from usuarios", null);
		
		if (c.getCount()==0)
			new Usuario(context, "!                    administrador       2002                ADMIN                                                                                                                                                                                                                                                                                                                                                                                                                                  304075", db);
		
		c.close();
		closeDatabase();
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
		
		String select="min(cast(secuenciaReal as Integer))"+"||'*'||direccion  ||  ' #' || numEdificio || ' ' || numPortal || ' ('|| count(*) ||')' nombre";
		
		if (!filtro.trim().equals("")){
			filtro=" " + (tipo==TodasLasLecturas.TODAS_LAS_LECTURAS?" where ": " and ")+" upper(direccion  || ' ' || numEdificio || ' ' || numPortal ) like '%"+filtro.toUpperCase()+"%' ";
		}
		if (tipo== TodasLasLecturas.LEIDA){
			 c=db.rawQuery("Select "+select+" from ruta where  " + globales.tdlg.getFiltroDeLecturas(TomaDeLecturasGenerica.LEIDAS) 			 		
						 +filtro +" group by sinUso1  order by cast(secuenciaReal as Integer)  asc", null);
		 }
		 else if (tipo== TodasLasLecturas.SIN_LEER) {
			 c=db.rawQuery("Select  "+select+"  from ruta where  " + globales.tdlg.getFiltroDeLecturas(TomaDeLecturasGenerica.AUSENTES) 			 		
						 +filtro + " group by sinUso1 order by cast(secuenciaReal as Integer)  asc", null);
			 
		 }
		 else{
			 c=db.rawQuery("Select "+select+"  from ruta " +filtro
		 +" group by sinUso1 order by cast(secuenciaReal as Integer)  asc", null);
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

}
