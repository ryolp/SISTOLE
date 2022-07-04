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
public class TomaDeLecturasPanama extends TomaDeLecturasGenerica {
	
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
	public final static int NIC=15;
	public final static int SELLO_RET_NUMERO=16;
	public final static int SELLO_RET_ESTADO=17;
	public final static int SELLO_RET_COLOR=18;
	public final static int SELLO_RET_MODELO=19;
	public final static int SELLO_INST_NUMERO=20;
	public final static int SELLO_INST_COLOR=21;
	public final static int SELLO_INST_MODELO=22;
	public final static int CODIGO_OBSERVACION=23;
	public final static int OBSERVACION=24;
	public final static int DATOS_CAMPANA=25;
	public final static int DATOS_OBSERVACIONES=26;
	
	//archivos a transmitir
	public final static int ARCHIVO_MENSAJES=2;
	public final static int ARCHIVO_G=3, ARCHIVO_I=4, ARCHIVO_R=5, ARCHIVO_M=6, ARCHIVO_N=7;
	
	Vector <TextView> textViews= new Vector<TextView>();
	MensajeEspecial mj_estaCortado;
	MensajeEspecial mj_sellos;
	MensajeEspecial mj_consumocero;
	MensajeEspecial mj_ubicacionVacia;
	MensajeEspecial mj_anomalia_seis;
	Hashtable <String, Integer> ht_calidades;
	
// CE, REVISAR
	public TomaDeLecturasPanama(Context context) {
		super(context);
		long_registro=386;
		globales.admonPass="2002";
		
		procesosAlEntrar();
		
		String [] campos={"unicom", "ruta", "itinerario", "ciclo", "sinUso1", "serieMedidor", "marcaMedidor", "tarifa", 
				"lectura", "anomalia", "hora", "fecha",  "intentos","sospechosa", "sospechosa",
				"selloRetNumero", "selloInstNumero", "selloRetEstado", "selloRetColor", "selloRetModelo",
				"selloInstColor", "selloRetModelo","codigoObservacion", "comentarios", "datosCampana"};
		String separadorSalida="";
		//Creamos los campos que serán de salida
		globales.tlc.getListaDeCamposFormateado(campos, "\t");
		
		globales.textoEsferas="Ruedas";
		globales.textoNoRegistrados="Nuevo Medidor";
		
		mj_estaCortado= new MensajeEspecial("¿Sigue Cortado?", PREGUNTAS_SIGUE_CORTADO);
		mj_estaCortado.cancelable=false;
		Vector <Respuesta> respuesta= new Vector <Respuesta> () ;
		respuesta.add(new Respuesta("0", "Con Sellos"));
		respuesta.add(new Respuesta("1", "Sin Sellos"));
		respuesta.add(new Respuesta("2", "Reconectado"));
		
		mj_sellos=new MensajeEspecial("Retirar Sellos", getCamposGenerico("sellos"), RETIRAR_SELLOS);
		
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
		
		globales.reemplazarToastPorMensaje=false;
		
		
		globales.logo=R.drawable.logo;		// CE, Hay que cambiar este LOGOTIPO
		
		globales.multiplesAnomalias=false;
		globales.convertirAnomalias=false;
		
		globales.longitudCodigoAnomalia=3;
		globales.longitudCodigoSubAnomalia=3;
		
		globales.rellenoAnomalia=".";
		globales.rellenarAnomalia=false;
		
		globales.repiteAnomalias=false;
		
		globales.remplazarDireccionPorCalles=false;
		
		globales.mostrarCuadriculatdl=true;
		
		globales.mostrarRowIdSecuencia=true;
		
		globales.dejarComoAusentes=true;
		
		globales.mensajeDeConfirmar=R.string.msj_lecturas_verifique_1;
		
		globales.mostrarNoRegistrados=true;
		globales.tipoDeValidacion=Globales.USUARIO;
		globales.mensajeContraseñaLecturista=R.string.str_login_msj_lecturista_contrasena_pana;
		globales.controlCalidadFotos=0;
		
		globales.mostrarMacImpresora=false;
		globales.mostrarServidorGPRS=false;
		globales.mostrarServidorWIFI=false;
		globales.mostrarFactorBaremo=false;
		globales.mostrarTamañoFoto=false;
		globales.mostrarMetodoDeTransmision=false;
		globales.mostrarIngresoFacilMAC=false;
		globales.mostrarUnicom=false;
		globales.mostrarRuta=false;
		globales.mostrarItinerario=false;
		globales.mostrarLote=false;
		globales.mostrarCPL=true;
		globales.mostrarSonido=false;

		globales.defaultTransmision="2";
		globales.defaultRutaDescarga="C:\\ALL_VerSCL\\Datos";
		globales.defaultUnicom="1120";								// CE, Hay que ver si tenemos el campo CPL
		globales.defaultRuta="03";
		globales.defaultItinerario="4480";
		globales.defaultServidorGPRS="http://10.240.142.194/1120";
		globales.defaultServidorWIFI="http://10.240.225.11/1120";
		globales.defaultServidorDeActualizacion="";
		
		globales.letraPais="P";
		
		globales.mostrarCodigoUsuario=true;
		
		globales.tomaMultiplesFotos=true;
		
		globales.porcentaje_main=1.0;
		globales.porcentaje_main2=1.0;
		globales.porcentaje_hexateclado=.74998;
		globales.porcentaje_teclado=.6410;
		globales.porcentaje_lectura=1.0;
		globales.porcentaje_info=1.1;
		
		globales.calidadDeLaFoto=20;
		
		globales.modoDeCierreDeLecturas=Globales.FORZADO;
		
		globales.mostrarGrabarEnSD=false;
		
		globales.mostrarCalidadFoto=true;
		
		globales.legacyCaptura=true;
		
		//Escondemos los elementos de la cuadricula que no nos interesan
		globales.ver_celda0=true;
		globales.ver_celda1=true;
		globales.ver_celda2=false;
		globales.ver_celda3=false;
		globales.ver_celda4=false;
		
		globales.DiferirEntreAnomInstYMed=false;
		
		globales.maxIntentos=3;
		
		globales.rellenarVaciaLectura=false;
		
		globales.puedoCancelarFotos=false;
		globales.mostrarOtraFoto=false;
		
		globales.tabs = new int [1] ;
		
		globales.tabs[0]= R.string.lbl_medidor;
		
		globales.longCampoUsuario=20;
		globales.longCampoContrasena=4;
		
		globales.fuerzaEntrarComoSuperUsuarioAdmon=true;
		
		globales.desencriptarEntrada=false;
		
		globales.tipoDeRecepcion=Globales.TRANSMION_NORMAL;
		
		globales.enviarLongitudDeCadenaAEnviar=false;
		
		globales.contraseñaUsuarioEncriptada=false;
		
		globales.preguntaSiTieneMedidor=false;
		
		globales.ultimoBloqueCapturado="0000";
		
		globales.mostrarPausaActiva=false;
		globales.tiempoPausaActiva=7200000;
		globales.fechaEnMilisegundos=0;
		
		globales.GPS=false;
		globales.bloquearBorrarSiIntento=true;
		
		globales.validacionCon123=false;
		
		globales.ordenarAnomalias=true;
		
		globales.guardarSospechosa=false;
		
		globales.elegirQueDescargar=false;
		globales.modoDeBanderasAGrabar=Globales.BANDERAS_CONF_PANAMA;
		
		globales.prefijoAnomalia="AN";
		globales.mostrarBuscarDespuesDeCapturar=true;
		
		
		globales.filtrarAnomaliasConLectura=false;
		
		globales.habilitarPuntoDecimal=false;
		
		globales.filtroGlobalBusqueda=/*" tarifa='CO011'"*/ " serieMedidor ";
		
		globales.tipoDeEntradaUsuarioLogin=InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
		
		globales.dirigirAlUnicoResultado=true;
		globales.switchBuscarPorMover=true;

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
		
		//Priemro sacamos los intentos
		
		int intento=1;
		
		if (ls_lectAct.length()==0) {
			return NO_SOSPECHOSA + "|Ingrese una lectura";
		}
		
		if (ls_lectAct.contains(".")){
			ls_lectAct=ls_lectAct.substring(0, ls_lectAct.indexOf("."))+ Main.rellenaString(ls_lectAct.substring( ls_lectAct.indexOf(".")+1), "0", 3, false);
		}
		
		long ll_lectAct = Long.parseLong(ls_lectAct);

		//Es valida
		if ((globales.il_lect_max >= ll_lectAct && globales.il_lect_min <= ll_lectAct && is_lectAnt.equals("")
				&&  !globales.tll.getLecturaActual().confirmarLectura() ) || (globales.tll.getLecturaActual().is_aviso.startsWith("Dem"))) {
			is_lectAnt = "";
			return "";
		}
		
		//Aqui ya se equivoco... ignoraremos la toma de la foto en reactiva
		if(globales.tll.getLecturaActual().is_aviso.startsWith("Rea")) {
			globales.ignorarTomaDeFoto=true;
		}
		
		
		if (!is_lectAnt.equals(ls_lectAct)) {
			if (!is_lectAnt.equals("") || globales.bModificar){
				globales.tll.getLecturaActual().intentos=String.valueOf(Lectura.toInteger(globales.tll.getLecturaActual().intentos) + 1 );
				globales.tll.getLecturaActual().distinta="1";
			}
			is_lectAnt = ls_lectAct;
			//Contador de lectura distinta
			
			globales.ignorarContadorControlCalidad=true;			
			globales.contadorIntentos++;
			
			if (globales.contadorIntentos<globales.maxIntentos){
				return /*NO_*/SOSPECHOSA +"|"+ globales.getString(R.string.msj_lecturas_verifique);
			}
		}else{
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
		 * NumMedidor a 10 posiciones,  
		 * fecha	  a YYYYMMDD
		 * hora		  a HHMMSS
		 */
    	
    	ls_nombre = Main.rellenaString(globales.tll.getLecturaActual().is_serieMedidor, "0", globales.tlc.getLongCampo("serieMedidor"), true) + "-";
    	ls_nombre+=Main.obtieneFecha("ymd");
    	ls_nombre+=Main.obtieneFecha("his");
    	ls_nombre+=".JPG";
    	return ls_nombre;
	}

// CE, REVISAR
	public  Vector<String> getInformacionDelMedidor(Lectura lectura) {
		Vector <String> datos= new Vector<String>();
		
		//Esta variable la usaremos para poder determinar si algun dato es vacio y mostrar solo lo necesario en la pantalla
		String comodin="";
		
		datos.add(lectura.is_sectorCorto);
		datos.add(lectura.getDireccion());
		datos.add(lectura.getColonia());
		datos.add((!lectura.numeroDeEdificio.trim().equals("")?lectura.numeroDeEdificio.trim():""));
		
		datos.add(lectura.getNombreCliente().trim());
		
		//Vamos a agregar los campos que se van llenando mientras se agregan anomalias
		String ls_anom=lectura.getAnomaliasCapturadas();
		String ls_comentarios="";
		if (!globales.tll.getLecturaActual().getAnomaliaAMostrar().equals("")) {
			// Tiene una anomalia
			ls_comentarios =context.getString(R.string.str_anomalia)+": "  + globales.is_presion;
			if (!globales.tll.getLecturaActual().getSubAnomaliaAMostrar().equals("")) {
				// Tiene una subanomalia
				ls_comentarios += /*", " */ "\n"+ "Observaciones"+": " 
						+ globales.tll.getLecturaActual().getSubAnomaliaAMostrar();
			}
			ls_comentarios += "\n";
		}
//		if (globales.tll.getLecturaActual().getComentarios().length()>18)
			ls_comentarios+=/*"\n" + ls_comentarios + */globales.tll.getLecturaActual().getComentarios()/*.substring(18)*/;
		if (!ls_comentarios.trim().equals(""));
			datos.add(ls_comentarios);
		return datos;
	}

	@Override
	public MensajeEspecial getMensaje() {
//		if (globales.tll.getLecturaActual().selloRetNumero.equals("") && globales.tll.getLecturaActual().is_aviso.startsWith("Dem"))
//			return mj_sellos;
		
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
			openDatabase();
			Cursor c= db.rawQuery("Select poliza from noregistrados", null);
			c.moveToFirst();
			if (c.getCount()>0){
				direccion= c.getString(c.getColumnIndex("poliza"));
				direccion=direccion.substring(16, 30);
				direccion=direccion.trim();
			}
			closeDatabase();
			cib_config = new ComentariosInputBehavior("Calle", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 30, direccion);
		break;
		case PUERTA:
			cib_config = new ComentariosInputBehavior("Número Exterior", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 6, "");
		break;
		case NUM_MEDIDOR:
			cib_config = new ComentariosInputBehavior("Número de Serie:", InputType.TYPE_CLASS_NUMBER, 10, "");
		break;
		case NUM_ESFERAS:
			cib_config = new ComentariosInputBehavior("Número de Ruedas:", InputType.TYPE_CLASS_NUMBER, 1,"");
		break;
		case LECTURA:
			cib_config = new ComentariosInputBehavior("Lectura:", InputType.TYPE_CLASS_NUMBER, 6, "");
		break;
		case MARCA:
			cib_config = new ComentariosInputBehavior("Marca Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 3, "");
		break;
		case TIPO:
			cib_config = new ComentariosInputBehavior("Tipo de Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 10,"");
		break;
		case SELLO_RET_NUMERO:
			cib_config = new ComentariosInputBehavior("Número de Sello Retirado", InputType.TYPE_CLASS_NUMBER, 10,"");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case SELLO_RET_ESTADO:
			cib_config = new ComentariosInputBehavior("Estado (NUevo,ENcontrata,INstalado,REtirado,DAñado,VIolado,CLandestino,EXtraviado,ILegible)", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2,"RE");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case SELLO_RET_COLOR:
			cib_config = new ComentariosInputBehavior("Color (AZul,BLanco,AMarillo,ROjo,GRis)", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2,"AZ");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case SELLO_RET_MODELO:
			cib_config = new ComentariosInputBehavior("Modelo (ROtocil,CAndado,PLomo)", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2,"CA");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case SELLO_INST_NUMERO:
			cib_config = new ComentariosInputBehavior("Número de Sello Instalado", InputType.TYPE_CLASS_NUMBER, 10,"");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case SELLO_INST_COLOR:
			cib_config = new ComentariosInputBehavior("Color Instalado (AZul,BLanco,AMarillo,ROjo,GRis)", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2,"AZ");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case SELLO_INST_MODELO:
			cib_config = new ComentariosInputBehavior("Modelo Instalado (ROtocil,CAndado,PLomo)", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2,"CA");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case CODIGO_OBSERVACION:
			cib_config = new ComentariosInputBehavior("Código Observación", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 5,"");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case OBSERVACION:
			cib_config = new ComentariosInputBehavior("Observación", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 25,"");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case DATOS_CAMPANA:
			cib_config = new ComentariosInputBehavior("Datos de Campaña", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 40,"");
			cib_config.obligatorio=false;	// Puede estar en blanco
		break;
		case OBSERVACIONES:
			cib_config = new ComentariosInputBehavior("Observaciones:", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 30, "");
			cib_config.obligatorio=false;
		break;
		case NIC:
			cib_config = new ComentariosInputBehavior("NIC", InputType.TYPE_CLASS_NUMBER, 7, "");
			cib_config.obligatorio=false;
		break;
		}
		return cib_config;
	}

	@Override
	public int[] getCamposGenerico(String anomalia) {
		int [] campos=null;
		if (anomalia.equals("noregistrados")) {
			campos= new int[4];
			campos[0]=NUM_MEDIDOR ;
			campos[1]=NUM_ESFERAS;
			campos[2]=LECTURA;
			campos[3]=OBSERVACIONES;
		}else if (anomalia.equals("observacion")) {
			campos= new int[2];
			campos[0]=CODIGO_OBSERVACION;
			campos[1]=OBSERVACION;
		}else if (anomalia.equals("campana")) {
			campos= new int[1];
			campos[0]=DATOS_CAMPANA;
		}else if (anomalia.equals("sellos")) {
			campos= new int[7];
			campos[0]=SELLO_RET_NUMERO;
			campos[1]=SELLO_INST_NUMERO;
			campos[2]=SELLO_RET_ESTADO;
			campos[3]=SELLO_RET_COLOR;
			campos[4]=SELLO_RET_MODELO;
			campos[5]=SELLO_INST_COLOR;
			campos[6]=SELLO_INST_MODELO;
		}
//		else if (anomalia.equals("002") || anomalia.equals("003") || anomalia.equals("004") || 
//				anomalia.equals("101") || anomalia.equals("200") || anomalia.equals("205") || anomalia.equals("208") ) {
//			campos= new int[1];
//			campos[0]=OBSERVACIONES;
//		} 		
		return campos;
	}

	@Override
	public void regresaDeCamposGenericos(Bundle bu_params, String anomalia) {
		String cadena=/*globales.lote*/"";
		if (anomalia.equals("noregistrados")) {
			
	    	Cursor c;
	    	openDatabase();
	    	c= db.rawQuery("Select unicom||'\t'||ruta||'\t'||itinerario||'\t'||ciclo||'\t' as Encabezado from Ruta where cast(secuenciaReal as Integer) ="+globales.il_lect_act, null);
	    	c.moveToFirst();
	    	cadena = c.getString(c.getColumnIndex("Encabezado"));
	    	c.close();
	    	
	    	

	    	c= db.rawQuery("Select count(*)+1 as canti from NoRegistrados", null);
	    	c.moveToFirst();
	    	cadena += Main.rellenaString(String.valueOf(c.getLong(c.getColumnIndex("canti"))), " ", 4, true) + "\t";
	    	c.close();
	    	
//	    	cadena +=globales.il_ultimoSegReg +'\t';

			cadena += Main.rellenaString(bu_params.getString(String.valueOf(NUM_MEDIDOR)), " ",10, false) + "\t";
			cadena += Main.rellenaString(bu_params.getString(String.valueOf(NUM_ESFERAS)), "0", 1, true) + "\t";
			cadena += Main.rellenaString(bu_params.getString(String.valueOf(LECTURA)), "0", 8, true) + "\t";
			cadena += Main.rellenaString(/*globales.tll.getLecturaActual().sinUso1*/ String.valueOf(globales.il_ultimoSegReg), " ", 4, true) + "\t";
			cadena += Main.rellenaString(bu_params.getString(String.valueOf(OBSERVACIONES)), " ", 25, false) ;
			
			//Guardamos en la bd
			db.execSQL("insert into noRegistrados values('"+ cadena+"')");
			closeDatabase();
		}else if (anomalia.equals("sellos")) {
			//Hay que poner el sello de instalacion
			String retirado=bu_params.getString(String.valueOf(SELLO_RET_NUMERO)).trim().equals("")?"0":bu_params.getString(String.valueOf(SELLO_RET_NUMERO)).trim();
			String instalado=bu_params.getString(String.valueOf(SELLO_INST_NUMERO)).trim().equals("")?"0":bu_params.getString(String.valueOf(SELLO_INST_NUMERO)).trim();
			globales.tll.getLecturaActual().selloRetNumero=retirado;
			openDatabase();
			db.execSQL("Update ruta set selloRetNumero='"+retirado+"', " +
					"selloRetEstado='"+ ObtenerCodigo(bu_params.getString(String.valueOf(SELLO_RET_ESTADO)), 3)+"', " +
					"selloRetColor='"+ObtenerCodigo(bu_params.getString(String.valueOf(SELLO_RET_COLOR)), 1)+"', "+
					"selloRetModelo='"+ObtenerCodigo(bu_params.getString(String.valueOf(SELLO_RET_MODELO)), 2)+"', "+
					"selloInstNumero='"+instalado+"', "+
					"selloInstColor='"+ObtenerCodigo(bu_params.getString(String.valueOf(SELLO_INST_COLOR)), 1)+"', "+
					"selloInstModelo='"+ObtenerCodigo(bu_params.getString(String.valueOf(SELLO_INST_MODELO)), 2)+"'" +
							" where secuenciaReal=" +globales.il_lect_act);
			closeDatabase();
		}else if (anomalia.equals("observacion")) {
		}else if (anomalia.equals("campana")) {
		}else{
			globales.tll.getLecturaActual().ls_codigoObservacion="OB032";
			globales.tll.getLecturaActual().setComentarios(bu_params.getString("input"));
		}
	}

// CE, REVISAR
	@Override
	public ContentValues getCamposBDAdicionales() {
		// TODO Auto-generated method stub
		ContentValues cv_params = new ContentValues();
		
//		cv_params.put("intentos", 0);
//		cv_params.put("sospechosa", "0");
////		cv_params.put("nisRad", 0);
//		cv_params.put("dondeEsta", "");
//		cv_params.put("anomInst", "");
//		cv_params.put("tipoLectura", "");
//		cv_params.put("comentarios", "");
//		cv_params.put("hora", "");
//		cv_params.put("fecha", "");
//		cv_params.put("lectura", "");
//		cv_params.put("anomalia", "");
////		cv_params.put("sectorCorto", "");
////		cv_params.put("sectorLargo", "");
////		cv_params.put("comoLlegar2", "");
////		cv_params.put("comoLlegar1", "");
//		cv_params.put("intento1", "");
//		cv_params.put("intento2", "");
//		cv_params.put("intento3", "");
//		cv_params.put("intento4", "");
//		cv_params.put("intento5", "");
//		cv_params.put("intento6", "");
//		cv_params.put("intento7", "");
		return cv_params;
	}

// CE, REVISAR
	@Override
	public void creaTodosLosCampos() {
//		globales.tlc.add(new Campo(21, "anomaliaDeInstalacion", 11, 1, Campo.D, "0"));//Distinta		// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***
//		globales.tlc.add(new Campo(27, "nisRad", 16, 10, Campo.D, " "));
//		globales.tlc.add(new Campo(27, "poliza", 16, 10, Campo.D, " "));
//		globales.tlc.add(new Campo(5, "comoLlegar1", 93, 23, Campo.I, " "));
//		globales.tlc.add(new Campo(5, "comoLlegar2", 116, 50, Campo.I, " "));
//		globales.tlc.add(new Campo(8,"sectorCorto", 307, 3, Campo.D, "0"));	
//		globales.tlc.add(new Campo(8,"sectorLargo", 310, 20, Campo.D, "0"));
//		globales.tlc.add(new Campo(31, "hora", 400, 6, Campo.F, "his", false));
//		globales.tlc.add(new Campo(8, "baremo", 487, 3, Campo.D, "0", false));	
//		globales.tlc.add(new Campo(9, "advertencias", 119, 0, Campo.I, " "));
//		globales.tlc.add(new Campo(10, "ilr", 121, 0, Campo.D, "0"));
//		globales.tlc.add(new Campo(11, "ism", 128, 0, Campo.D, "0"));
//		globales.tlc.add(new Campo(12, "saldoEnMetros", 129, 0, Campo.D, "0"));
//		globales.tlc.add(new Campo(15, "consumo", 144, 0, Campo.D, "0"));
//		globales.tlc.add(new Campo(18, "divisionContrato", 159, 0, Campo.D, "0"));
//		globales.tlc.add(new Campo(23, "tipoMedidor", 390, 1, Campo.I, " "));
//		globales.tlc.add(new Campo(27, "indicadorGPS", 349, 1, Campo.D, " "));
//		globales.tlc.add(new Campo(0, "sinUso1", 0, 16, Campo.I, " "));

// CE, Los siguientes campos vienen del Servidor.class
		globales.tlc.add(new Campo(0, "tabulador", 4, 1, Campo.D, " "));
		globales.tlc.add(new Campo(1, "unicom", 0, 4, Campo.D, " "));
		globales.tlc.add(new Campo(2, "ruta", 5, 2, Campo.D, " "));
		globales.tlc.add(new Campo(3, "itinerario", 8, 4, Campo.D, " "));
		globales.tlc.add(new Campo(4, "ciclo", 13, 2, Campo.D, " "));

		globales.tlc.add(new Campo(5, "serieMedidor", 16, 10, Campo.D, " "));
		globales.tlc.add(new Campo(6, "marcaMedidor", 27, 5, Campo.I, " "));
		globales.tlc.add(new Campo(7, "sinUso1", 33, 4, Campo.D, "0"));
		globales.tlc.add(new Campo(8, "cliente", 38, 25, Campo.I, " "));
		globales.tlc.add(new Campo(8, "sectorcorto", 64, 20, Campo.I, " "));	// barrio
		globales.tlc.add(new Campo(8, "sectorlargo", 85, 20, Campo.I, " "));	// calle
		globales.tlc.add(new Campo(11, "colonia", 106, 20, Campo.I, " "));
		//falta numPuerta(127,5)
		globales.tlc.add(new Campo(13, "numPortal", 133, 6, Campo.I, " "));
		globales.tlc.add(new Campo(14, "direccion", 140, 60, Campo.I, " "));
		//falta numPuerta(201,20)
		globales.tlc.add(new Campo(16, "numEdificio", 222, 20, Campo.I, " "));
		globales.tlc.add(new Campo(17, "numEsferas", 243, 1, Campo.D, " "));
		globales.tlc.add(new Campo(18, "estadoDelSuministro", 245, 1, Campo.D, " "));
		//falta TipoDeServicio(247,5)
		//falta CodigoDeTarifa(253,5)
		globales.tlc.add(new Campo(21, "tarifa", 259, 5, Campo.I, " "));
		globales.tlc.add(new Campo(22, "lecturaAnterior", 265, 8, Campo.D, "0"));
		globales.tlc.add(new Campo(23, "consBimAnt", 265, 8, Campo.D, "0"));
		globales.tlc.add(new Campo(24, "consAnoAnt", 274, 8, Campo.D, "0"));
		globales.tlc.add(new Campo(25, "reclamacion", 283, 1, Campo.D, " "));
		globales.tlc.add(new Campo(26, "reclamacionLectura", 283, 1, Campo.D, " "));
		globales.tlc.add(new Campo(27, "supervision", 283, 1, Campo.D, " "));
		globales.tlc.add(new Campo(28, "supervisionLectura", 283, 1, Campo.D, " "));
		globales.tlc.add(new Campo(29, "aviso", 285, 48, Campo.I, " "));
		//falta dos puntos que no sabes que son(333,2)
		globales.tlc.add(new Campo(29, "tipoMedidor", 336, 50, Campo.I, " "));			// DescripcionDeLaMarcaDelAparato
		
// CE Los siguientes campos los debe llenar el SISTOLE
		globales.tlc.add(new Campo(31, "tipoLectura", 401, 1, Campo.I, " ", false));
		globales.tlc.add(new Campo(32, "lectura", 402, 8, Campo.D, "0", false));
		//faltan (410,2)
		globales.tlc.add(new Campo(34, "ordenDeLectura", 412, 4, Campo.D, "0", false));
		globales.tlc.add(new Campo(35, "anomalia", 416, 5, Campo.I, " ", false));
		globales.tlc.add(new Campo(36, "fecha", 421, 8, Campo.F, "ymd", false));
		globales.tlc.add(new Campo(36, "hora", 421, 6, Campo.F, "his", false));
		globales.tlc.add(new Campo(37, "sospechosa", 435, 2, Campo.D, "0", false));//Confirmadax
		globales.tlc.add(new Campo(38, "intentos", 437, 2, Campo.D, "0", false));//Distinta		
		globales.tlc.add(new Campo(39, "sinUso2", 439, 2, Campo.I, " ", false));

		//falta (441,2)
		//falta (443,1)
		//falta (444,6)
		
		globales.tlc.add(new Campo(43, "selloRetNumero", 450, 10, Campo.I, " ", false));
		globales.tlc.add(new Campo(44, "selloInstNumero", 460, 10, Campo.I, " ", false));
		globales.tlc.add(new Campo(45, "selloRetEstado", 465, 5, Campo.I, " ", false));
		globales.tlc.add(new Campo(46, "selloRetColor", 470, 5, Campo.I, " ", false));
		globales.tlc.add(new Campo(47, "selloRetModelo", 475, 5, Campo.I, " ", false));
		globales.tlc.add(new Campo(48, "selloInstColor", 480, 5, Campo.I, " ", false));
		globales.tlc.add(new Campo(49, "selloInstModelo", 485, 5, Campo.I, " ", false));

		globales.tlc.add(new Campo(50, "codigoObservacion", 490, 5, Campo.I, " ", false));
		globales.tlc.add(new Campo(51, "comentarios", 495, 25, Campo.I, " ", false));
		globales.tlc.add(new Campo(52, "datosCampana", 520, 40, Campo.I, " ", false));
		
// CE; Los siguientes campos no se usan en Panama
//		globales.tlc.add(new Campo(46, "comentarios", 525, 10, Campo.I, " ", false));
//
//		globales.tlc.add(new Campo(34, "escalera", 265, 0, Campo.I, " "));
//		globales.tlc.add(new Campo(35, "piso", 268, 0,  Campo.I, " "));
//		globales.tlc.add(new Campo(36, "puerta", 270, 0, Campo.D, " "));
//		
//		globales.tlc.add(new Campo(41, "numEsferasReal", 278, 0, Campo.D, " "));
//		globales.tlc.add(new Campo(42, "fechaAviso", 278, 0, Campo.F, "dmy"));
//		globales.tlc.add(new Campo(43, "serieMedidorReal", 278, 0, Campo.D, " "));
//		globales.tlc.add(new Campo(44, "ubicacion", 278, 0, Campo.I, " "));
//		globales.tlc.add(new Campo(45, "estimaciones", 278, 0, Campo.I, " "));
//		
//		globales.tlc.add(new Campo(47, "subAnomalia", 278, 0, Campo.I, " "));
//		globales.tlc.add(new Campo(48, "estadoDelSuministroReal", 278, 0, Campo.D, " "));
//		
//		globales.tlc.add(new Campo(42, "intento1", 278, 8, Campo.D, " ", false));
//		globales.tlc.add(new Campo(43, "intento2", 278, 8, Campo.D, " " ,false));
//		globales.tlc.add(new Campo(44, "intento3", 278, 8, Campo.D, " " ,false));
//		globales.tlc.add(new Campo(45, "intento4", 278, 8, Campo.D, " " ,false));
//		globales.tlc.add(new Campo(47, "intento5", 278, 8, Campo.D, " " ,false));
//		globales.tlc.add(new Campo(48, "intento6", 278, 8, Campo.D, " " ,false));
//		globales.tlc.add(new Campo(49, "intento7", 278, 8, Campo.D, " " ,false));
//		
//		globales.tlc.add(new Campo(42, "fechaReintento1", 278, 14, Campo.F, "hisdmy",false));
//		globales.tlc.add(new Campo(43, "fechaReintento2", 278, 14, Campo.F, "hisdmy",false));
//		globales.tlc.add(new Campo(44, "fechaReintento3", 278, 14, Campo.F, "hisdmy",false));
//		globales.tlc.add(new Campo(45, "fechaReintento4", 278, 14, Campo.F, "hisdmy",false));
//		globales.tlc.add(new Campo(47, "fechaReintento5", 278, 14, Campo.F, "hisdmy",false));
//		globales.tlc.add(new Campo(48, "fechaReintento6", 278, 14, Campo.F, "hisdmy",false));
//		globales.tlc.add(new Campo(49, "fechaReintento7", 278, 14, Campo.F, "hisdmy",false));
//		
//		globales.tlc.add(new Campo(48, "latitud", 278, 20, Campo.I, " ",false));
//		globales.tlc.add(new Campo(49, "longitud", 278, 20, Campo.I, " ",false));
//		globales.tlc.add(new Campo(49, "satelites", 278, 2, Campo.I, "0",false));
//		globales.tlc.add(new Campo(49, "fix", 278, 1, Campo.I, " ", false));
}

// CE, REVISAR
	@Override
	public long getLecturaMinima() {
		return globales.tll.getLecturaActual().consBimAnt ; 
	}

// CE, REVISAR
	@Override
	public long getLecturaMaxima() {
		return globales.tll.getLecturaActual().consAnoAnt;
	}

// CE, Es para llenar la cuadricula
	@Override
	public String obtenerContenidoDeEtiqueta(String ls_etiqueta) {
		// TODO Auto-generated method stub
		if (ls_etiqueta.equals("campo0")){
			return String.valueOf(globales.tll.getLecturaActual().sinUso1);
		}
		else if (ls_etiqueta.equals("campo1")){
			return globales.tll.getLecturaActual().is_tipoMedidor.trim();
		}
		else{
			return "";
		}
	}
	

	public String obtenerTituloDeEtiqueta(String ls_etiqueta) {
		if (ls_etiqueta.equals("campo0")){
			return "Secuencia";
		}
		else if (ls_etiqueta.equals("campo1")){
			return "Marca";
		}
		else{
			return super.obtenerTituloDeEtiqueta(ls_etiqueta);
		}
	}

// CE, REVISAR
	@Override
	public FormatoDeEtiquetas getMensajedeRespuesta() {
		// TODO Auto-generated method stub
		if (globales.tll.getLecturaActual().is_aviso.startsWith("Act")) {
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_aviso.trim(), R.color.DarkGreen);
		}else if (globales.tll.getLecturaActual().is_aviso.startsWith("Dem")) {
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_aviso.trim(), R.color.Red);
		}else if(globales.tll.getLecturaActual().is_aviso.startsWith("Rea")) {
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_aviso.trim(), R.color.blue);
		}else{
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_aviso.trim(), R.color.Orange);
		}
	}

	@Override
	public String getMensajedeAdvertencia() {
		// TODO Auto-generated method stub
		return "";
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
				ls_preview += "<br>" + lectura.is_sectorCorto + "<br>" + lectura.getColonia();
			ls_preview += "<br>" +lectura.getDireccion();
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
	}

	@Override
	public long getConsumo(String lectura) {
		return  0;
	}
	
	public void setTipoLectura(){
		super.setTipoLectura();
	}

	@Override
	public String validaCamposGenericos(String anomalia, Bundle bu_params) {
		if ( anomalia.equals("sellos")){
			if (bu_params.getString(String.valueOf(SELLO_RET_NUMERO)).trim().equals("") && 
					bu_params.getString(String.valueOf(SELLO_INST_NUMERO)).equals("")){
				return "Los campos de número de sello retirado y número de sello instalado no puden estar vacios, llene alguno de ellos.";
			}	
		}
		
		return "";
	}
	
	@Override
	public MensajeEspecial regresaDeAnomalias(String ls_anomalia, boolean esAnomalia) {
		if (esAnomalia){
			return null;
		}
		openDatabase();
		
		Cursor c;
		c= db.rawQuery("Select * from anomalia where substr(desc, 1, 3)='"+ls_anomalia+"'", null);
		
		if (c.getCount()==0){
			c.close();
			closeDatabase();
			return null;
		}
		c.moveToFirst();
		
		if (c.getString(c.getColumnIndex("subanomalia")).equals("S")){
			String desc= c.getString(c.getColumnIndex("desc"));
//			desc=Main.rellenaString(desc, " ", 18, false);
//			globales.tll.getLecturaActual().setComentarios("OB"+desc.substring(0,3));
			globales.tll.getLecturaActual().ls_codigoObservacion="OB"+desc.substring(0,3);
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
//		Integer calidad = 20;
		Integer calidad=globales.calidadDeLaFoto;
		return calidad.intValue();
	}
	
	public boolean continuarConLaFoto() {
		return true;
	}
	
	public void AgregarAnomaliasManualmente(SQLiteDatabase db) {
	}
	
	public void accionesDespuesDeCargarArchivo(SQLiteDatabase db){
//		db.execSQL("update ruta set ='', anomalia='' , anomaliaDeInstalacion=''");
		db.execSQL("Update anomalia" +
				" set "+
				"  foto=(select a2.foto from anomalia a2 where a2.subanomalia='T' and a2.anomalia=anomalia.anomalia),  " +
				" ausente=(select a2.ausente from anomalia a2 where a2.subanomalia='T' and a2.anomalia=anomalia.anomalia)," +
				"  lectura=(select a2.lectura from anomalia a2 where a2.subanomalia='T' and a2.anomalia=anomalia.anomalia)" +
				" where subanomalia='S'" );
		
		db.execSQL("Update anomalia" +
				" set "+
				"  mens='0' where substr(desc, 1, 3)<>'032' and subanomalia<>'T'" );
		
		db.execSQL("Update anomalia" +
				" set "+
				"  mens='1' where anomalia in (select a2.anomalia from anomalia a2 where substr(a2.desc, 1, 3)='032')" +
				" and substr(desc, 1, 3)<>'032' " );
		
		db.execSQL("Update anomalia" +
				" set "+
				"  activa='I' where anomalia='777'" );
		
		db.execSQL("Update anomalia" +
				" set foto "+
				"  ='1' where substr(desc, 1, 3)='033'" );
		
		
		db.execSQL("delete from anomalia where substr(desc, 1, 3)='032'");
	}
	
	public void agregarRegistroRaro(SQLiteDatabase db, String registro){
	}
	
	public boolean esUnRegistroRaro(String registro){
		return false;
	}
	
	public int [] getArchivosATransmitir(int metodoDeTransmision){
		return new int[0];
	}
	
	public String getNombreArchvio(int tipo){
		
		String ls_archivo="";
		TransmitionObject to= new TransmitionObject();
		
		switch(tipo){
		case SALIDA:
//			TransmitionObject to= new TransmitionObject();
			getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
			
			to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"D";
			
			return to.ls_categoria;
		case NO_REGISTRADOS:
//			TransmitionObject to= new TransmitionObject();
			getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
			
			to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"N";
			
			return to.ls_categoria;
		}
		
		return super.getNombreArchvio(tipo);
	}
	
public Cursor getContenidoDelArchivo(SQLiteDatabase db, int tipo){
		
		String ls_select="";
		
		Cursor c=null;
		
		switch(tipo){
		case SALIDA:
			//Primero el encabezado
			c=db.rawQuery("Select * from encabezado", null);
			c.moveToFirst();
			String encabezado=new String(c.getBlob(c.getColumnIndex("registro")));
			ls_select=Main.rellenaString("", "0", 4, false);
//			ls_select+=encabezado.substring(202, 212); //Unicom ruta itinerario y ciclo
			
			if (encabezado.substring(200, 212).trim().equals("")){
				String unicom=encabezado.substring(200, 204).trim();
		    	String ruta=encabezado.substring(204, 206).trim();
		    	String itinerario=encabezado.substring(206, 210).trim();
		    	
		    	if (unicom.equals("")){
		    		try{
		    			c=db.rawQuery("Select value from config where key='unicom'", null);
		        		c.moveToFirst();
		        		unicom=c.getString(c.getColumnIndex("value"));
		    		}
		    		catch(Throwable e){
		    			
		    		}
		    	}
			
				
		    	if (ruta.equals("")){
		    		try{
		    			c=db.rawQuery("Select value from config where key='ruta'", null);
		        		c.moveToFirst();
		        		ruta=c.getString(c.getColumnIndex("value"));
		    		}
		    		catch(Throwable e){
		    		}
		    			
		    		
		    	}	
		    		if (itinerario.equals("")){
		    			try{
		    				c=db.rawQuery("Select value from config where key='itinerario'", null);
		    	    		c.moveToFirst();
		    	    		itinerario=c.getString(c.getColumnIndex("value"));
		    			}
		    			catch(Throwable e){
		    				
		    			}
		    		}
		    		ls_select+=Main.rellenaString(unicom, " ", 4, true);
		    		ls_select+=Main.rellenaString(ruta, " ", 2, true);
		    		ls_select+=Main.rellenaString(itinerario, " ", 4, true);
		    		ls_select+=Main.rellenaString(String.valueOf(Integer.parseInt(Main.obtieneFecha("m"))), " ", 2, true);//Unicom ruta itinerario y ciclo
			}
			else{
				ls_select+=encabezado.substring(200, 212); //Unicom ruta itinerario y ciclo
			}
			
			
			c.close();
			String valor;
			c=db.rawQuery("Select count(*) canti from ruta where anomalia<>'099'", null);
			 c.moveToFirst();
			 
			 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
			 ls_select+=Main.rellenaString(valor, " ", 4, false); //Con lectura
			
			c.close();

//			ls_select+=" 0";
			ls_select+= Main.obtieneFecha("ymd");

			
			
			c= db.rawQuery("Select count(*) canti from Ruta", null);
	    	c.moveToFirst();
	    	 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
	    	 ls_select+=Main.rellenaString(valor, " ", 4, false);
	    	  //Lecturista
	    	 
	    	 c.close();
			
			
	    	 c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='0' and trim(upper(serieMedidor))='CONDIR'", null);
			 c.moveToFirst();
			 
			 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
			 ls_select+=Main.rellenaString(valor, " ", 4, false);
			 //directos
			
			c.close();
			
			c=db.rawQuery("Select count(*) canti from NoRegistrados", null);
			 c.moveToFirst();
			 
			 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
			 ls_select+= Main.rellenaString(valor, " ", 4, false);
			 //Nuevos
			
			c.close();
			
			
			ls_select+=Main.rellenaString("0", " ",4, false);
			
			 c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='4'", null);
			 c.moveToFirst();
			 
			 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
			 ls_select+=Main.rellenaString(valor, " ", 4, false); //ausentes
			
			c.close();
			
			 c=db.rawQuery("Select count(*) canti from ruta where trim(comentarios)<>''", null);
	    	 c.moveToFirst();
	    	 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
	    	 ls_select+= Main.rellenaString(valor, " ", 4, false); //ausentes
	    	 c.close();
	    	 
	    	 ls_select+=" ";
			
			//Ya tenemos el encabezado, ahora el query normal
	    	 ls_select= " select '" + ls_select + "' TextoSalida  union ";
	    	 ls_select+= "Select ";
	    	 ls_select+=globales.tlc.getCampoObjeto("sinUso1").campoSQLFormateado();
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("serieMedidor").campoSQLFormateado();
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("tipoMedidor").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("sectorCorto").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("lectura").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("anomalia").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("hora").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("fecha").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("sospechosa").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("poliza").campoSQLFormateado() ;
	    	 ls_select+=" || " + globales.tlc.getCampoObjeto("nisrad").campoSQLFormateado() ;
	    	 
	    	 ls_select+=" TextoSalida from ruta union ";
	    	 ls_select+="select 'RFIN' TextoSalida";
	    	 c=db.rawQuery(ls_select, null);
			break;
		}		 
		return c;
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
    	long ll_sinLect;
    	long ll_noRegistrados;
    	long ll_conDir;
    	
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
    	 ll_sinLect=c.getLong(c.getColumnIndex("canti"));
    	 c.close();
    	 
    	 c=db.rawQuery("Select count(*) canti from ruta where trim(anomalia)<>''", null);
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
    	 
    	 resumen.add(new EstructuraResumen( "Lecturas en la Ruta", String.valueOf(ll_total)));
    	 resumen.add(new EstructuraResumen( context.getString(R.string.msj_main_fotos_tomadas), String.valueOf(ll_fotos)));
    	 resumen.add(new EstructuraResumen("", ""));
    	 porcentaje=  (((float)ll_restantes*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen( "Lecturas Pendientes",String.valueOf(ll_restantes),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_tomadas*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Registros con Lectura", String.valueOf(ll_tomadas),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_sinLect*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Registros sin Lectura",String.valueOf(ll_sinLect), formatter.format(porcentaje) +"%"));
    	 
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
//    	 resumen.add(new EstructuraResumen("Mensajes", String.valueOf(ll_mensajes)));
    	 if (globales.mostrarNoRegistrados)
    		 resumen.add(new EstructuraResumen("Nuevos Medidores", String.valueOf(ll_noRegistrados)));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 return resumen;
	}
	
	public void activacionDesactivacionOpciones(boolean esSuperUsuario){
			if (esSuperUsuario){
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
	
	public void EncriptarDesencriptar(byte[] medidor){
		byte comodin0 = 2;
  		byte comodin1 = 3;
	  	byte comodin2 = 4;

  		int X = 0;
  		int rep = 0;
		byte byteLetra;

		for (int i=0; i<medidor.length-2; i++){
    			byteLetra = medidor[i];
    			if      (byteLetra == 10) 	medidor[i] = 10;
    			else if (byteLetra == 9)  	medidor[i] = 9;
    			else if (byteLetra == 94) 	medidor[i] = 94;
    			else if (X == 0) 		medidor[i] = (byte)(byteLetra ^ comodin0);
    			else if (X == 1) 		medidor[i] = (byte)(byteLetra ^ comodin1);
    			else if (X == 2) 		medidor[i] = (byte)(byteLetra ^ comodin2);
    			
    			X = X + (byte) 1;
    			if ((X == 2) && (rep == 0)) {
      				X = 1;
      				rep = 1;
    			}else{ 
				if ((X == 2) && (rep > 0)) rep = 0;
    			}
    			if (X == 3) X = 0;
  		}
	}
		
	public String getEstructuras( TransmitionObject to, int tipo, int tipoTransmision){
		openDatabase();
		String resultado=getEstructuras(db, to, tipo, tipoTransmision);
		closeDatabase();
		return resultado;
	}
	
	public String getEstructuras(SQLiteDatabase db, TransmitionObject to, int tipo, int tipoTransmision){
		String ls_subcarpeta, ls_ruta, ls_itinerario, ls_unicom;
		//openDatabase();
	       
	       //Tomamos el servidor desde la pantalla de configuracion
	       Cursor c = null;
			 
//			 if (tipoTransmision==TransmisionesPadre.BLUETOOTH){
				 c=db.rawQuery("Select value from config where key='mac_bluetooth'", null);
				 c.moveToFirst();
				 if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_macBluetooth) , context.getString(R.string.str_configuracion),context.getString(R.string.info_macBluetooth));
//			 }

			 to.ls_servidor=c.getString(c.getColumnIndex("value")); 
			   c.close();
			   //Ahora vamos a ver que archivo es el que vamos a recibir... para nicaragua es el clp + la extension
			   //Lo vamos a guardar en categoria, Asi le llamamos a los archivos desde "SuperLibretaDeDirecciones" 2013 (c) ;)
			   
			   
			   c=db.rawQuery("Select value from config where key='cpl'", null);
			   
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return  String.format(context.getString(R.string.msj_config_no_disponible), "CPL" , context.getString(R.string.str_configuracion),"CPL");
				  
			   //ls_categoria="";
			   ls_itinerario=c.getString(c.getColumnIndex("value")) ;
			   c.close();
			   
			   //Por ultimo la ruta de descarga... Como es un servidor web, hay que quitarle el C:\... mejor empezamos desde lo que sigue. De cualquier manera, deberá tener el siguiente formato
			   //Ruta de descarga.subtr(3) + Entrada  + \ + lote 
			   c=db.rawQuery("Select value from config where key='ruta_descarga'", null);
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_rutaDescarga) , context.getString(R.string.str_configuracion),context.getString(R.string.info_rutaDescarga));
			   
			   to.ls_carpeta=c.getString(c.getColumnIndex("value")) ;
			   
			   
			   c.close();
			   
			   if (tipoTransmision==TransmisionesPadre.WIFI || tipoTransmision==TransmisionesPadre.GPRS){
//				   if (to.ls_carpeta.indexOf(":") >= 0) {
//						to.ls_carpeta = to.ls_carpeta.substring(to.ls_carpeta.indexOf(":") + 2);
//					}
				   to.ls_carpeta="";
				   
//				   if (!to.ls_servidor.endsWith("\\") && !to.ls_servidor.equals("") )
//					   to.ls_servidor+="\\";
				   
			   }
			   
			
			   
			   if (!to.ls_carpeta.endsWith("\\") && !to.ls_carpeta.equals("") )
				   to.ls_carpeta+="\\";
			   
			   
			   to.ls_carpeta+=tipo==TransmisionesPadre.TRANSMISION?"":   ls_itinerario+"\\DN";
			   
//			   c=db.rawQuery("Select value from config where key='lote'", null);
//			   c.moveToFirst();
//			   if (!validaCampoDeConfig(c ))
//					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_lote) , context.getString(R.string.str_configuracion),context.getString(R.string.info_lote));
//			   
//			   ls_subcarpeta=  c.getString(c.getColumnIndex("value"));
//			   
//			   ls_carpeta+="\\" + ls_subcarpeta ;
//			   
//			   c.close();

			   to.ls_categoria="      ";
	       
	      // closeDatabase();
	       return "";
	}
	
	
	public String encabezadoAEnviar(String ls_carpeta, String ls_categoria){
		return globales.letraPais + ls_carpeta +"\\";
	}
	
	
	public byte[] encabezadoAMandar(SQLiteDatabase db){
		TransmitionObject to= new TransmitionObject();
		getEstructuras(db, to, TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
		
		Cursor c=db.rawQuery("Select value from config where key='ruta_descarga'", null);
		
		c.moveToFirst();
		
		String ruta=c.getString(c.getColumnIndex("value")) +"\\";
				
		c.close();
		
		c=db.rawQuery("Select value from config where key='cpl'", null);
		
		c.moveToFirst();
		
		ruta += c.getString(c.getColumnIndex("value")) + "\\UP\\";
				
		c.close();
		
		c=db.rawQuery("Select registro from encabezado", null);
		
		c.moveToFirst();
		byte [] bytesAEnviar=c.getBlob(c.getColumnIndex("registro"));
		c.close();
		
		bytesAEnviar[0]='.';
		bytesAEnviar[2]='D';
		
		for (int i=0; i<ruta.length();i++)bytesAEnviar[i+20]=ruta.getBytes()[i];

		//Con lectura
		String valor;
		c=db.rawQuery("Select count(*) canti from ruta", null);
		c.moveToFirst();
		 
		valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
		valor= Main.rellenaString(valor, " ", 4, false);
		c.close();

		for (int i=0; i<4;i++)bytesAEnviar[i+227]=valor.getBytes()[i]; //Con lectura
		
		bytesAEnviar[230]='\t';
		bytesAEnviar[239]='\t';
		bytesAEnviar[240]='1';

		valor= Main.obtieneFecha("ymd");
		for (int i=0; i<8;i++)bytesAEnviar[i+231]=valor.getBytes()[i]; //Fecha
			return bytesAEnviar;
	}
	
	public void noRegistradosinMedidor(){
		Bundle bu_params= new  Bundle();
		
		bu_params.putString(String.valueOf(NUM_MEDIDOR), "CONDIR");
		bu_params.putString(String.valueOf(LECTURA), "0");
		
		regresaDeCamposGenericos(bu_params, "noregistrados");
	}

	public void accionesAntesDeGrabarLectura(){
			globales.ultimoBloqueCapturado=globales.tll.getLecturaActual().sinUso1;
			globales.ultimoMedidorCapturado=globales.tll.getLecturaActual().is_serieMedidor;
	}
	
	public void accionesDespuesDeGrabarLectura(){
		
		if (!globales.tll.getLecturaActual().getAnomaliaAMostrar().equals("")){
			boolean esAusente=globales.tll.getLecturaActual().requiereLectura()==Anomalia.LECTURA_AUSENTE;
			String tipoLectura= "";
			String is_fecha=Main.obtieneFecha(globales.tlc.getRellenoCampo("fecha"));
			String is_hora=Main.obtieneFecha(globales.tlc.getRellenoCampo("hora"));
			
			String ls_anomalia=globales.tll.getLecturaActual().getAnomaliaAMostrar();
			
			String ls_query="Update ruta set ";
			ls_query+=" anomalia='AN"+ls_anomalia +"'";
			ls_query+=", fecha='"+is_fecha+"'" ;
			ls_query+=", hora='"+is_hora +"'";
			ls_query+=", codigoObservacion='"+ (globales.tll.getLecturaActual().ls_codigoObservacion.equals("")?globales.tll.getLecturaActual().ls_codigoObservacion:
				"OB"+globales.tll.getLecturaActual().ls_codigoObservacion)+"'";
			ls_query+=", comentarios='"+ globales.tll.getLecturaActual().getComentarios()+"'";
			ls_query+=", subAnomalia='"+ globales.tll.getLecturaActual().getSubAnomaliaAMostrar()+"'";
			openDatabase();
			if (esAusente){
				ls_query+=", tipoLectura='4'" ;
				ls_query+=", lectura=''";
			}
			else{
				
			}
			
			ls_query+=" where trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
					" and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
			
			db.execSQL(ls_query);
			
			if (!esAusente){
				ls_query= "Update ruta set tipoLectura='' where trim(lectura)=''";
				ls_query+=" and  trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
						" and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
				db.execSQL(ls_query);
			}
//			else{
//				//Posiblemente borraron la anomalia
//				ls_query= "Update ruta set anomalia=''";
//				
//				ls_query+=" and  trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
//						" and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
//				
//				ls_query= "Update ruta set tipoLectura='' where trim(lectura)=''";
//				ls_query+=" and  trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
//						" and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
//				
//				ls_query= "Update ruta set tipoLectura='' where trim(lectura)=''";
//				ls_query+=" and  trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
//						" and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
//				db.execSQL(ls_query);
//			}
			
			closeDatabase();
		}
		else{
			openDatabase();
			String ls_query="Update ruta set ";
			ls_query+=" anomalia=''";
			ls_query+=", codigoObservacion=''";
			ls_query+=", comentarios='"+ globales.tll.getLecturaActual().getComentarios()+"'";
			ls_query+=", subAnomalia='"+ globales.tll.getLecturaActual().getSubAnomaliaAMostrar()+"'";
			
			
			ls_query+=" where trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
					" and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
			
			db.execSQL(ls_query);
			
			//Ahora hay que borrar la fecha y hora y el tipo de lectura en las que se les haya asignado
			
			ls_query="Update ruta set";
			
			ls_query+=" fecha=''" ;
			ls_query+=", hora=''";
			ls_query+=", tipoLectura=''";
			
			ls_query+=" where trim(serieMedidor)='"+globales.tll.getLecturaActual().is_serieMedidor.trim()+"'" +
					" and tipoLectura='4' and sinUso1<>'"+globales.tll.getLecturaActual().sinUso1+"'";
			
			db.execSQL(ls_query);
			
			closeDatabase();
		}
}
		
	public Vector <EstructuraResumen> getPrincipal(SQLiteDatabase db){
		String unicom="";
		String ruta="";
		String itinerario="";
		String ciclo="";
		String cpl="";
		String mac_bt="";
		String mac_impr="";
		
		Vector <EstructuraResumen>resumen= new Vector<EstructuraResumen>();
//    	resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	Cursor c;
		try{
			c=db.rawQuery("Select unicom, ruta, itinerario, ciclo from ruta limit 1 ", null);
    		c.moveToFirst();
    		unicom=c.getString(c.getColumnIndex("unicom"));
    		ruta=c.getString(c.getColumnIndex("ruta"));
    		itinerario=c.getString(c.getColumnIndex("itinerario"));
    		ciclo=c.getString(c.getColumnIndex("ciclo"));
    		
    		
		}
		catch(Throwable e){
			
		}
		
		try{
			c=db.rawQuery("Select value from config where key='cpl'", null);
    		c.moveToFirst();
    		cpl=c.getString(c.getColumnIndex("value"));
		}
		catch(Throwable e){
			
		}
		
		try{
			c=db.rawQuery("Select value from config where key='mac_bluetooth'", null);
    		c.moveToFirst();
    		mac_bt=c.getString(c.getColumnIndex("value"));
		}catch(Throwable e){
			
		}
    	
		
		resumen.add(new EstructuraResumen(unicom, "Unicom"));
		resumen.add(new EstructuraResumen(ruta, "Ruta"));
		resumen.add(new EstructuraResumen(itinerario, "Itinerario"));
		resumen.add(new EstructuraResumen(ciclo, "Ciclo"));
		resumen.add(new EstructuraResumen(cpl, "CPL"));
		resumen.add(new EstructuraResumen(mac_bt, "MAC"));
		
		resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
		
    	 return resumen;
//		String ls_resumen;
//		Cursor c;
//			try{
//				c=db.rawQuery("Select value from config where key='unicom'", null);
//	    		c.moveToFirst();
//	    		unicom=c.getString(c.getColumnIndex("value"));
//			}
//			catch(Throwable e){
//				
//			}
//			
//			try{
//				c=db.rawQuery("Select value from config where key='ruta'", null);
//	    		c.moveToFirst();
//	    		ruta=c.getString(c.getColumnIndex("value"));
//			}
//			catch(Throwable e){
//				
//			}
//			
//			try{
//				c=db.rawQuery("Select value from config where key='itinerario'", null);
//	    		c.moveToFirst();
//	    		itinerario=c.getString(c.getColumnIndex("value"));
//			}
//			catch(Throwable e){
//				
//			}
//	    		
//	    		try{
//	    			c=db.rawQuery("Select value from config where key='mac_bluetooth'", null);
//	        		c.moveToFirst();
//	        		mac_bt=c.getString(c.getColumnIndex("value"));
//	    		}catch(Throwable e){
//	    			
//	    		}
//	    		
//	    		
//	    		try{
//	    			c=db.rawQuery("Select value from config where key='mac_impresora'", null);
//	        		c.moveToFirst();
//	        		mac_impr=c.getString(c.getColumnIndex("value"));
//	    		}catch(Throwable e){
//	    			
//	    		}
//	    		
//	    		
//			
//			
//			
//	    	//ll_restantes = ll_total-ll_tomadas ;
//			Vector <EstructuraResumen>resumen= new Vector<EstructuraResumen>();
//			
//			resumen.add(new EstructuraResumen(unicom, "Unicom"));
//			resumen.add(new EstructuraResumen(ruta,"Ruta"));
//			resumen.add(new EstructuraResumen(itinerario,"Itinerario"));
//	    	
////	    	ls_resumen="CPL: " + cpl +"\n" +
////	    			"Lote: " +  lote +"\n";
//	    	
//	    	if (!mac_bt.equals("") && !mac_bt.equals(".")){
//	    		   
////	    			ls_resumen+="MAC BT: \n"+ mac_bt +"\n";
//	    		resumen.add(new EstructuraResumen(mac_bt, /*getString(R.string.info_macBluetooth)*/"MAC"));
//	    	}
//	    	
//	    	if (!mac_impr.equals("") && !mac_impr.equals(".")){
//	    		   
////				ls_resumen+="\nMAC Impr: \n"+ mac_impr;
//	    		resumen.add(new EstructuraResumen( mac_impr, context.getString(R.string.info_macImpresora)));
//		}
//	    	
//	    	if (!globales.getUsuario().equals("")){
//
//	    			resumen.add(new EstructuraResumen( globales.getUsuario()+"-" + globales.is_nombre_Lect, "Lect."));
//	    	}
//	    	
//			
//	    	resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
//	    	 
//	    	 return resumen;
			
		}
	
	Cursor lineasAEscribir(SQLiteDatabase db){
		return getContenidoDelArchivo(db, SALIDA);
	}
	
	private String ObtenerCodigo(String strTexto, int nTipo){
		String lect;
		lect = strTexto;
		if (nTipo == 1){
			while (lect.length() < 5) lect = lect + " ";//color
			if (lect.trim().length() > 1) {
				if (lect.substring(0,2).equalsIgnoreCase("AZ")) lect = "CL001"; else
				if (lect.substring(0,2).equalsIgnoreCase("BL")) lect = "CL002"; else
				if (lect.substring(0,2).equalsIgnoreCase("AM")) lect = "CL003"; else 
				if (lect.substring(0,2).equalsIgnoreCase("RO")) lect = "CL004"; else
				if (lect.substring(0,2).equalsIgnoreCase("GR")) lect = "CL005"; else lect = "CL999";
			}
		}else if (nTipo == 2){
			while (lect.length() < 5) lect = lect + " ";//modelo
			if (lect.trim().length() > 1) {
				if (lect.substring(0,2).equalsIgnoreCase("RO")) lect = "ML001"; else
				if (lect.substring(0,2).equalsIgnoreCase("CA")) lect = "ML002"; else
				if (lect.substring(0,2).equalsIgnoreCase("PL")) lect = "ML003"; else lect = "ML999";
			}
		}else {
			while (lect.length() < 5) lect = lect + " ";//estado
			if (lect.trim().length() > 1) {
				if (lect.substring(0,2).equalsIgnoreCase("NU")) lect = "ES101"; else
				if (lect.substring(0,2).equalsIgnoreCase("EN")) lect = "ES102"; else
				if (lect.substring(0,2).equalsIgnoreCase("IN")) lect = "ES103"; else
				if (lect.substring(0,2).equalsIgnoreCase("RE")) lect = "ES104"; else
				if (lect.substring(0,2).equalsIgnoreCase("DA")) lect = "ES105"; else
				if (lect.substring(0,2).equalsIgnoreCase("VI")) lect = "ES106"; else
				if (lect.substring(0,2).equalsIgnoreCase("CL")) lect = "ES107"; else
				if (lect.substring(0,2).equalsIgnoreCase("EX")) lect = "ES108"; else
				if (lect.substring(0,2).equalsIgnoreCase("IL")) lect = "ES109"; else lect = "ES999";
			}
		}
		return lect;
	}
	
	public boolean mostrarVentanaDeSellos(){
		return globales.tll.getLecturaActual().selloRetNumero.equals("") && globales.tll.getLecturaActual().is_aviso.startsWith("Dem");
	}
	
	public boolean tomarFotoModificar() {
		// TODO Auto-generated method stub
		if(globales.tll.getLecturaActual().is_aviso.startsWith("Rea")|| globales.tll.getLecturaActual().is_aviso.startsWith("Dem")) {
			return false;
		}
			return true;
	}

}
