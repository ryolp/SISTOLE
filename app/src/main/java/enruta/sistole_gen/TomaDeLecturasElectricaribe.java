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
public class TomaDeLecturasElectricaribe extends TomaDeLecturasGenerica {
	
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
	public TomaDeLecturasElectricaribe(Context context) {
		super(context);
		long_registro=355;
		
		procesosAlEntrar();
		
		String [] campos={"sinUso1", "serieMedidor", "tipoMedidor", "sectorCorto", "lectura", "anomalia", "hora", "fecha", "sospechosa", "poliza", "nisrad", "comentarios",
				"intento1","intento2" ,"intento3","intento4","intento5","intento6","intento7" 
				,"fechaReintento1","fechaReintento2","fechaReintento3","fechaReintento4","fechaReintento5","fechaReintento6","fechaReintento7",
				"latitud", "longitud", "fix", "hora"};
		
		//Creamos los campos que serán de salida
		globales.tlc.getListaDeCamposFormateado(campos);
		
		globales.textoEsferas="Ruedas";
		globales.textoNoRegistrados="Nuevos Suministros";
		
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
		
		globales.reemplazarToastPorMensaje=false;
		
		
		globales.logo=R.drawable.logo_electricaribe;
		
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
		globales.tipoDeValidacion=Globales.AMBAS;
		globales.mensajeContraseñaLecturista=R.string.str_login_msj_lecturista;
		globales.controlCalidadFotos=0;
		
		globales.sonidoCorrecta=Sonidos.NINGUNO;
		globales.sonidoIncorrecta=Sonidos.BEEP;
		globales.sonidoConfirmada=Sonidos.NINGUNO;
		
		globales.mostrarMacImpresora=false;
		globales.mostrarServidorGPRS=true;
		globales.mostrarFactorBaremo=false;
		globales.mostrarTamañoFoto=true;
		globales.mostrarMetodoDeTransmision=true;
		globales.mostrarIngresoFacilMAC=false;
		globales.mostrarUnicom=true;
		globales.mostrarRuta=true;
		globales.mostrarItinerario=true;
		globales.mostrarLote=false;
		globales.mostrarCPL=false;

		globales.defaultTransmision="0";
		globales.defaultRutaDescarga="C:\\Aploclec\\Datos";
		globales.defaultUnicom="1120";
		globales.defaultRuta="03";
		globales.defaultItinerario="4480";
		globales.defaultServidorGPRS="http://10.240.142.194/1120";
		globales.defaultServidorWIFI="http://10.240.225.11/1120";
		globales.defaultServidorDeActualizacion="";
		
		globales.letraPais="E";
		
		globales.mostrarCodigoUsuario=true;
		
		globales.tomaMultiplesFotos=true;
		
		globales.porcentaje_main=1.0;
		globales.porcentaje_main2=1.0;
		globales.porcentaje_hexateclado=.74998;
		globales.porcentaje_teclado=.6410;
		globales.porcentaje_lectura=1.0;
		globales.porcentaje_info=1.1;
		
		globales.calidadDeLaFoto=20;
		
		globales.modoDeCierreDeLecturas=Globales.RUTA_COMPLETA;
		
		globales.mostrarGrabarEnSD=false;
		
		globales.mostrarCalidadFoto=false;
		
		globales.legacyCaptura=true;
		
		//Escondemos los elementos de la cuadricula que no nos interesan
		globales.ver_celda0=true;
		globales.ver_celda1=true;
		globales.ver_celda2=false;
		globales.ver_celda3=false;
		globales.ver_celda4=false;
		
		globales.DiferirEntreAnomInstYMed=true;
		
		globales.maxIntentos=6;
		
		globales.rellenarVaciaLectura=false;
		
		globales.puedoCancelarFotos=false;
		globales.mostrarOtraFoto=true;
		
		globales.tabs =new int [2] ;
		
		globales.tabs[0]= R.string.lbl_medidor;
		globales.tabs[1]= R.string.lbl_calles;
		
		globales.longCampoUsuario=8;
		globales.longCampoContrasena=10;
		
		globales.fuerzaEntrarComoSuperUsuarioAdmon=true;
		
		globales.desencriptarEntrada=false;
		
		globales.tipoDeRecepcion=Globales.TRANSMION_ELECTIRCARIBE;
		
		globales.enviarLongitudDeCadenaAEnviar=false;
		
		globales.contraseñaUsuarioEncriptada=true;
		
		globales.preguntaSiTieneMedidor=true;
		
		globales.ultimoBloqueCapturado="0000";
		
		globales.mostrarPausaActiva=true;
		globales.tiempoPausaActiva=7200000;
		globales.fechaEnMilisegundos=0;
		
		globales.GPS=true;
		globales.bloquearBorrarSiIntento=true;
		
		globales.validacionCon123=true;
		
		globales.ordenarAnomalias=true;
		
		globales.guardarSospechosa=false;
		
		globales.elegirQueDescargar=true;
		globales.modoDeBanderasAGrabar=Globales.BANDERAS_INTENTOS_ECA;
		globales.iconoNoRegistrados=R.drawable.ic_nuevos_medidores;
		globales.quitarPrimerCaracterNombreFoto=false;
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
		
		long ll_lectAct = Long.parseLong(ls_lectAct);
		
		if (ls_lectAct.length()!= globales.tll.getLecturaActual().numerodeesferas) {
			return NO_SOSPECHOSA + "|" + globales.getString(R.string.msj_validacion_esferas);
		}
		
		if (globales.tll.getLecturaActual().intento1.equals("")){
			intento=1;
			globales.tll.getLecturaActual().intento1=ls_lectAct;
		}else if (globales.tll.getLecturaActual().intento2.equals("")){
			intento=2;
			globales.tll.getLecturaActual().intento2=ls_lectAct;
		}else if (globales.tll.getLecturaActual().intento3.equals("")){
			intento=3;
			globales.tll.getLecturaActual().intento3=ls_lectAct;
		}else if (globales.tll.getLecturaActual().intento4.equals("")){
			intento=4;
			globales.tll.getLecturaActual().intento4=ls_lectAct;
		}else if (globales.tll.getLecturaActual().intento5.equals("")){
			intento=5;
			globales.tll.getLecturaActual().intento5=ls_lectAct;
		}else if (globales.tll.getLecturaActual().intento6.equals("")){
			intento=6;
			globales.tll.getLecturaActual().intento6=ls_lectAct;
		}
		else{
			return NO_SOSPECHOSA +"|Ha superado la cantidad de intentos, ya no se puede ingresar lectura";
		}
		
		
		openDatabase();
		
		String csTextoInsertar;
		csTextoInsertar = Main.rellenaString(ls_lectAct.trim(), "0", 8, true);
//		csTextoInsertar = "12345678";
		
		db.execSQL("update ruta set intento"+(intento)+"='"+csTextoInsertar+"', fechaReintento"+(intento)+
				"='"+Main.obtieneFecha("hisymd")+"', sospechosa='"+intento+"' where secuenciaReal="+globales.tll.getLecturaActual().secuenciaReal);
		
		Cursor c=db.rawQuery("Select intento1, intento2, intento3, intento4, intento5, intento6 from ruta where secuenciaReal="+globales.tll.getLecturaActual().secuenciaReal, null);
		c.moveToFirst();
		
		closeDatabase();
		
		
		if ((ll_lectAct -11)<globales.tll.getLecturaActual().lecturaAnterior && ( ll_lectAct + 11)>globales.tll.getLecturaActual().lecturaAnterior && is_lectAnt.equals("") ){
			if (globales.contadorIntentos<2 && intento<6 ){
				is_lectAnt = ls_lectAct;
				globales.contadorIntentos=0;
				globales.is_terminacion="FZ";
				return /*NO_*/SOSPECHOSA_MENSAJE +"|Lectura Repetida. Verifique Anomalías";
			}
		}
		
		//Es valida
		if (globales.il_lect_max >= ll_lectAct && globales.il_lect_min <= ll_lectAct && is_lectAnt.equals("")
			/*	&&  !globales.tll.getLecturaActual().confirmarLectura()  /*|| globales.bModificar*/) {
			if (globales.tll.getLecturaActual().is_reclamacionLectura.equals("1")){
				globales.fotoForzada=true;
			}
			is_lectAnt = "";
			return "";
		}
		
		globales.is_terminacion="FZ";
		if (!is_lectAnt.equals(ls_lectAct)) {
			if (!is_lectAnt.equals("") || globales.bModificar){
				globales.tll.getLecturaActual().intentos=String.valueOf(Lectura.toInteger(globales.tll.getLecturaActual().intentos) + 1 );
				globales.tll.getLecturaActual().distinta="1";
			}
			is_lectAnt = ls_lectAct;
			//Contador de lectura distinta
			
			//sonidos.playSoundMedia(Sonidos.URGENT);																	// CE, REVISAR Aqui debemos ver si activamos los sonidos
			globales.ignorarContadorControlCalidad=true;
			
			globales.contadorIntentos=0;
			
			if ( intento<6){
				return /*NO_*/SOSPECHOSA +"|"+ globales.getString(R.string.msj_lecturas_verifique);
			}
			globales.fotoForzada=true;
		}else{
			globales.contadorIntentos++;
			if (globales.contadorIntentos<2 && intento<6 ){
				return /*NO_*/SOSPECHOSA +"|"+ globales.getString(R.string.msj_lecturas_verifique);
			}
			globales.fotoForzada=true;
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
		 * unicom del encabezado
		 * ruta del encabezado
		 * itinerario del encabezado
		 * fecha
		 * hora
		 * terminacion
		 */
    	
    	c= db.rawQuery("Select poliza from ruta where cast(secuenciaReal as Integer) ="+secuencial, null);
    	c.moveToFirst();
    	
    	ls_nombre+=Main.rellenaString(c.getString(c.getColumnIndex("poliza")), "0", 7, true);
    	ls_nombre+="_";
    	

    	c.close();
    	
    	//Obtenemos el encabezado
    	
    	c= db.rawQuery("Select registro from encabezado", null);
    	c.moveToFirst();
		
    	String encabezado=new String(c.getBlob(c.getColumnIndex("registro")));
    	
    	
    	c.close();
    	
    	if (is_terminacion.equals("-1")){
    		is_terminacion="LB";
    	}
    	
    	if (!ls_anomalia.trim().equals("")){
    		is_terminacion="AN";
    	}
    	
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
    	    	
    	ls_nombre+=Main.rellenaString(unicom, "0", 4, true);
    	ls_nombre+="_";
    	ls_nombre+=Main.rellenaString(ruta, "0", 2, true);
    	ls_nombre+="_";
    	ls_nombre+=Main.rellenaString(itinerario, "0", 4, true);
    	ls_nombre+="_";
    	ls_nombre+=Main.obtieneFecha("ymd");
    	ls_nombre+="_";
    	ls_nombre+=Main.obtieneFecha("his");
    	//Hay que preguntar por la terminacion
    	ls_nombre+=/*(!ls_anomalia.equals("")?"-A":"") +*/is_terminacion+".JPG";
    	
    	return ls_nombre;
	}

// CE, REVISAR
	public  Vector<String> getInformacionDelMedidor(Lectura lectura) {
		Vector <String> datos= new Vector<String>();
		
		//Esta variable la usaremos para poder determinar si algun dato es vacio y mostrar solo lo necesario en la pantalla
		String comodin="";
		
		datos.add(lectura.getDireccion() + " #" +lectura.numeroDePortal.trim() + (!lectura.numeroDeEdificio.trim().equals("")?"-" +lectura.numeroDeEdificio.trim():""));
		
		datos.add(lectura.getColonia());

//		datos.add("NIC: " + lectura.nis_rad);
		
		datos.add(lectura.getNombreCliente().trim());
		datos.add(lectura.is_comollegar1);
		datos.add(lectura.is_comoLlegar2);
		
		datos.add(lectura.is_tarifa);
		
//		datos.add(lectura.is_sectorLargo);

//		datos.add("Marca de Med. : " + globales.tll.getLecturaActual().getMarcaDeMedidorAMostrar());

//		datos.add("Anom. de Med. : " + globales.tll.getLecturaActual().getAnomaliaAMostrar());
//		datos.add("Anom. de Inst.: " + globales.tll.getLecturaActual().getAnomaliaDeInstalacionAMostrar());
		
		
//		openDatabase();
//		
//		Cursor c=db.rawQuery("Select max(cast(secuencia as int)) contEdificio from ruta where  sinUso1='"+globales.tll.getLecturaActual().sinUso1+"'", null); 
//		c.moveToFirst();
//		int contEdificio= c.getInt(c.getColumnIndex("contEdificio"));
//		closeDatabase();
//
//		datos.add("Punto de Suministro: " + globales.tll.getLecturaActual().secuencia +" de " +contEdificio);
//		datos.add("Medidor En la Ruta : " + (globales.mostrarRowIdSecuencia?globales.tll.getLecturaActual().secuenciaReal:globales.il_lect_act) + " de " + globales.il_total);

		//Vamos a agregar los campos que se van llenando mientras se agregan anomalias
		String ls_anom=lectura.getAnomaliasCapturadas();
		String ls_comentarios="";
		if (!globales.tll.getLecturaActual().getAnomaliaAMostrar().equals("")) {
			// Tiene una anomalia
			ls_comentarios =context.getString(R.string.str_anomalia)+": "  + globales.is_presion;
			if (!globales.tll.getLecturaActual().getSubAnomaliaAMostrar().equals("")) {
				// Tiene una subanomalia
				ls_comentarios += /*", " */ "\n"+ context.getString(R.string.str_subanomalia)+": " 
						+ globales.tll.getLecturaActual().getSubAnomaliaAMostrar();
			}
			ls_comentarios += "\n";
		}
		
		if (globales.tll.getLecturaActual().getComentarios().length()>18)
			ls_comentarios+=/*"\n" + ls_comentarios + */globales.tll.getLecturaActual().getComentarios().substring(18);
		
		
		if (!ls_comentarios.trim().equals(""));
			datos.add(ls_comentarios);
			
//			c.close();
		
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
			cib_config = new ComentariosInputBehavior("Número de Medidor", InputType.TYPE_CLASS_NUMBER, 10, "");
		break;
		case NUM_ESFERAS:
			cib_config = new ComentariosInputBehavior("Número de Esferas", InputType.TYPE_CLASS_NUMBER, 1,"");
		break;
		case LECTURA:
			cib_config = new ComentariosInputBehavior("Lectura", InputType.TYPE_CLASS_NUMBER, 8, "");
		break;
		case MARCA:
			cib_config = new ComentariosInputBehavior("Marca Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 3, "");
		break;
		case TIPO:
			cib_config = new ComentariosInputBehavior("Tipo de Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 10,"");
			break;
		case OBSERVACIONES:
			cib_config = new ComentariosInputBehavior("Observaciones", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 22, "");
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
			campos= new int[2];
			campos[0]=NUM_MEDIDOR ;
			campos[1]=LECTURA;
//			campos[5]=CALLE;
//			campos[6]=PUERTA;
//			campos[7]=OBSERVACIONES;
		} 
//		else
//			if (anomalia.equals("001") ) {
//				campos= new int[2];
//				campos[0]=NUM_MEDIDOR ;
//				campos[1]=NIC;
////				campos[5]=CALLE;
////				campos[6]=PUERTA;
////				campos[7]=OBSERVACIONES;
//			}
//				else if (anomalia.equals("066") || anomalia.equals("010")) {
//					campos= new int[3];
//					campos[0]=NUM_MEDIDOR ;
//					campos[1]=NUM_ESFERAS;
//					campos[2]=MARCA;
//				
//		}
		
		return campos;
	}

	@Override
	public void regresaDeCamposGenericos(Bundle bu_params, String anomalia) {
		String cadena=/*globales.lote*/"";
		if (anomalia.equals("noregistrados")) {
			
			cadena +=globales.ultimoBloqueCapturado;
			cadena +=Main.rellenaString(globales.ultimoMedidorCapturado, " ", 10, false);
			cadena +=globales.tll.getLecturaActual().sinUso1;
			cadena +=Main.rellenaString(globales.tll.getLecturaActual().is_serieMedidor, " ", 10, false);
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(NUM_MEDIDOR)), " ",10, false) ;
			cadena +=Main.rellenaString(bu_params.getString(String.valueOf(LECTURA)), "0", 8, true) ;
			
			
			//Guardamos en la bd
			openDatabase();
			db.execSQL("insert into noRegistrados values('"+ cadena+"')");
			closeDatabase();
			
		} 
		else
			if (anomalia.equals("001")) {
				cadena="01 M " +Main.rellenaString(bu_params.getString(String.valueOf(NUM_MEDIDOR)), " ",10, false)+
						" N " + Main.rellenaString(bu_params.getString(String.valueOf(NIC)), " ",7, false);
				globales.tll.getLecturaActual().setComentarios(cadena);
			}
		else if (anomalia.equals("095")|| anomalia.equals("010")) {
					cadena=Main.rellenaString(bu_params.getString(String.valueOf(NUM_MEDIDOR)), " ",10, false)+ " "+
							Main.rellenaString(bu_params.getString(String.valueOf(NUM_ESFERAS)), " ",1, false)+ " "+
							Main.rellenaString(bu_params.getString(String.valueOf(MARCA)), " ",20, false);
							
					globales.tll.getLecturaActual().setComentarios(cadena);
				}
		else if (anomalia.equals("088")) {
			cadena=Main.rellenaString(bu_params.getString(String.valueOf(NUM_ESFERAS)), " ",1, false);
			globales.tll.getLecturaActual().setComentarios(cadena);
		}
		else{
			globales.tll.getLecturaActual().setComentarios(bu_params.getString("input"));
		}
		
	}

// CE, REVISAR
	@Override
	public ContentValues getCamposBDAdicionales() {
		// TODO Auto-generated method stub
		ContentValues cv_params = new ContentValues();
		
		cv_params.put("intentos", 0);
		cv_params.put("sospechosa", "0");
//		cv_params.put("nisRad", 0);
		cv_params.put("dondeEsta", "");
		cv_params.put("anomInst", "");
		cv_params.put("tipoLectura", "");
		cv_params.put("comentarios", "");
		cv_params.put("hora", "");
		cv_params.put("fecha", "");
		cv_params.put("lectura", "");
		cv_params.put("anomalia", "");
//		cv_params.put("sectorCorto", "");
//		cv_params.put("sectorLargo", "");
//		cv_params.put("comoLlegar2", "");
//		cv_params.put("comoLlegar1", "");
		cv_params.put("intento1", "");
		cv_params.put("intento2", "");
		cv_params.put("intento3", "");
		cv_params.put("intento4", "");
		cv_params.put("intento5", "");
		cv_params.put("intento6", "");
		cv_params.put("intento7", "");
		return cv_params;
	}

// CE, REVISAR
	@Override
	public void creaTodosLosCampos() {
		globales.tlc.add(new Campo(0, "sinUso1", 0, 4, Campo.I, " "));
		globales.tlc.add(new Campo(2, "cliente", 4, 25, Campo.I, " "));
		globales.tlc.add(new Campo(25, "colonia", 29, 25, Campo.I, " "));
		//globales.tlc.add(new Campo(1, "secuencia", 33, 4, Campo.D, "0"));
		globales.tlc.add(new Campo(3, "direccion", 54, 25, Campo.I, " "));
		globales.tlc.add(new Campo(4, "numEdificio", 79, 4, Campo.I, " "));
		globales.tlc.add(new Campo(5, "numPortal", 83, 10, Campo.I, " "));
		globales.tlc.add(new Campo(5, "comoLlegar1", 93, 23, Campo.I, " "));
		globales.tlc.add(new Campo(5, "comoLlegar2", 116, 50, Campo.I, " "));
		globales.tlc.add(new Campo(6, "aviso", 176, 50, Campo.I, " "));
		globales.tlc.add(new Campo(24, "serieMedidor", 226, 10, Campo.D, " "));
		globales.tlc.add(new Campo(23, "tipoMedidor", 236, 3, Campo.I, " "));
		globales.tlc.add(new Campo(22, "marcaMedidor", 239, 20, Campo.I, " "));
		globales.tlc.add(new Campo(7, "lecturaAnterior", 259, 8, Campo.D, "0"));
		globales.tlc.add(new Campo(30, "consBimAnt", 267, 8, Campo.D, "0"));
		globales.tlc.add(new Campo(29, "consAnoAnt", 275, 8, Campo.D, "0"));
		globales.tlc.add(new Campo(28, "numEsferas", 283, 1, Campo.D, " "));
		//falta campo(284,3)
		globales.tlc.add(new Campo(26, "tarifa", 287, 20, Campo.I, " "));
		globales.tlc.add(new Campo(8,"sectorCorto", 307, 3, Campo.D, "0"));	
		globales.tlc.add(new Campo(8,"sectorLargo", 310, 20, Campo.D, "0"));
		//falta campo(330,12)
		globales.tlc.add(new Campo(27, "nisRad", 342, 7, Campo.D, " "));
		globales.tlc.add(new Campo(27, "poliza", 335, 7, Campo.D, " "));
		globales.tlc.add(new Campo(27, "indicadorGPS", 349, 1, Campo.D, " "));
		//faltan campos(349,10)
		globales.tlc.add(new Campo(39, "reclamacionLectura", 352, 1, Campo.D, " "));
		globales.tlc.add(new Campo(39, "supervision", 353, 1, Campo.D, " "));
		globales.tlc.add(new Campo(40, "supervisionLectura", 354, 1, Campo.D, " "));
		
		globales.tlc.add(new Campo(13, "tipoLectura", 360, 1, Campo.I, " ", false));
		globales.tlc.add(new Campo(14, "lectura", 361, 8, Campo.D, "0", false));
		globales.tlc.add(new Campo(16, "fecha", 369, 8, Campo.F, "ymd", false));
		globales.tlc.add(new Campo(17, "anomalia", 383, 3, Campo.I, "0", false));
		// noe sta el campo(385,1)
		globales.tlc.add(new Campo(19, "sospechosa", 386, 2, Campo.D, "0", false));//Confirmadax
		globales.tlc.add(new Campo(20, "intentos", 388, 2, Campo.D, " ", false));//Distinta		
		globales.tlc.add(new Campo(31, "hora", 400, 6, Campo.F, "his", false));
		//falta (406,4)
		globales.tlc.add(new Campo(32, "lecturista", 410, 8, Campo.I, " ", false));
		//falta (418,2)
		globales.tlc.add(new Campo(33, "ordenDeLectura", 420, 4, Campo.D, "0", false));
		// falta (424,6)
//		globales.tlc.add(new Campo(37, "reclamacionLectura", 430, 1, Campo.D, " ", false));
//		globales.tlc.add(new Campo(38, "supervisionLectura", 431, 1, Campo.D, " ", false));
		
		//faltan (434,53)
		globales.tlc.add(new Campo(46, "comentarios", 440, 25, Campo.I, " ", false));
		globales.tlc.add(new Campo(8, "baremo", 487, 3, Campo.D, "0", false));	

		
						// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***
		globales.tlc.add(new Campo(9, "advertencias", 119, 0, Campo.I, " "));
		globales.tlc.add(new Campo(10, "ilr", 121, 0, Campo.D, "0"));
		globales.tlc.add(new Campo(11, "ism", 128, 0, Campo.D, "0"));
		globales.tlc.add(new Campo(12, "saldoEnMetros", 129, 0, Campo.D, "0"));
		
		
		globales.tlc.add(new Campo(15, "consumo", 144, 0, Campo.D, "0"));
	
		
		globales.tlc.add(new Campo(18, "divisionContrato", 159, 0, Campo.D, "0"));
		// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***
		globales.tlc.add(new Campo(21, "anomaliaDeInstalacion", 163, 0, Campo.D, "0"));//Distinta		// CE, REVISAR, *** HAY UN CAMPO QUE NO TENIA ARGENTINA ***

		globales.tlc.add(new Campo(26, "sinUso3", 217, 0, Campo.I, " "));
		
		
					// CE, REVISAR, *** ESTE CAMPO NO SE USABA EN ARGENITNA, AQUI ES IMPORTANTE ***
						// CE, REVISAR, *** ESTE CAMPO NO SE USABA EN ARGENITNA, AQUI ES IMPORTANTE ***
		
		
		globales.tlc.add(new Campo(34, "escalera", 265, 0, Campo.I, " "));
		globales.tlc.add(new Campo(35, "piso", 268, 0,  Campo.I, " "));
		globales.tlc.add(new Campo(36, "puerta", 270, 0, Campo.D, " "));
		

		globales.tlc.add(new Campo(41, "numEsferasReal", 278, 0, Campo.D, " "));
		globales.tlc.add(new Campo(42, "fechaAviso", 278, 0, Campo.F, "dmy"));
		globales.tlc.add(new Campo(43, "serieMedidorReal", 278, 0, Campo.D, " "));
		globales.tlc.add(new Campo(44, "ubicacion", 278, 0, Campo.I, " "));
		globales.tlc.add(new Campo(45, "estimaciones", 278, 0, Campo.I, " "));
		
		globales.tlc.add(new Campo(47, "subAnomalia", 278, 0, Campo.I, " "));
		globales.tlc.add(new Campo(48, "estadoDelSuministroReal", 278, 0, Campo.D, " "));
		globales.tlc.add(new Campo(49, "estadoDelSuministro", 278, 0, Campo.D, " "));
		
		
		
		globales.tlc.add(new Campo(42, "intento1", 278, 8, Campo.D, " ", false));
		globales.tlc.add(new Campo(43, "intento2", 278, 8, Campo.D, " " ,false));
		globales.tlc.add(new Campo(44, "intento3", 278, 8, Campo.D, " " ,false));
		globales.tlc.add(new Campo(45, "intento4", 278, 8, Campo.D, " " ,false));
		globales.tlc.add(new Campo(47, "intento5", 278, 8, Campo.D, " " ,false));
		globales.tlc.add(new Campo(48, "intento6", 278, 8, Campo.D, " " ,false));
		globales.tlc.add(new Campo(49, "intento7", 278, 8, Campo.D, " " ,false));
		
		globales.tlc.add(new Campo(42, "fechaReintento1", 278, 14, Campo.F, "hisdmy",false));
		globales.tlc.add(new Campo(43, "fechaReintento2", 278, 14, Campo.F, "hisdmy",false));
		globales.tlc.add(new Campo(44, "fechaReintento3", 278, 14, Campo.F, "hisdmy",false));
		globales.tlc.add(new Campo(45, "fechaReintento4", 278, 14, Campo.F, "hisdmy",false));
		globales.tlc.add(new Campo(47, "fechaReintento5", 278, 14, Campo.F, "hisdmy",false));
		globales.tlc.add(new Campo(48, "fechaReintento6", 278, 14, Campo.F, "hisdmy",false));
		globales.tlc.add(new Campo(49, "fechaReintento7", 278, 14, Campo.F, "hisdmy",false));
		
		globales.tlc.add(new Campo(48, "latitud", 278, 20, Campo.I, " ",false));
		globales.tlc.add(new Campo(49, "longitud", 278, 20, Campo.I, " ",false));
		globales.tlc.add(new Campo(49, "satelites", 278, 2, Campo.I, "0",false));
		globales.tlc.add(new Campo(49, "fix", 278, 1, Campo.I, " ", false));
		
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

// CE, REVISAR
	@Override
	public String obtenerContenidoDeEtiqueta(String ls_etiqueta) {
		// TODO Auto-generated method stub
		if (ls_etiqueta.equals("campo0")){
			return String.valueOf(globales.tll.getLecturaActual().nis_rad);
		}
		else if (ls_etiqueta.equals("campo1")){
			return globales.tll.getLecturaActual().is_marcaMedidor;
		}
//		else if (ls_etiqueta.equals("campo2")){
//			return globales.tll.getLecturaActual().getAnomalia();
//		}
//		else if (ls_etiqueta.equals("campo3")){
//			return globales.tll.getLecturaActual().is_anomaliaDeInstalacion;
//		}
//		else if (ls_etiqueta.equals("campo4")){
//			return globales.tll.getLecturaActual().getSubAnomaliaAMostrar();
//		}
		else{
			return "";
		}
	}
	

	public String obtenerTituloDeEtiqueta(String ls_etiqueta) {
		if (ls_etiqueta.equals("campo0")){
			return "NIC";
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
		if (globales.tll.getLecturaActual().is_sectorLargo.startsWith("ACT") )
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_sectorLargo, R.color.DarkGreen);
		else if (globales.tll.getLecturaActual().is_sectorLargo.startsWith("DEM")) {
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_sectorLargo, R.color.Red);
		}else if(globales.tll.getLecturaActual().is_sectorLargo.startsWith("REA")) {
			return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_sectorLargo, R.color.blue);
		}else{
				return new FormatoDeEtiquetas(globales.tll.getLecturaActual().is_sectorLargo, R.color.DarkOliveGreen);
			
		}
//			return null;
//		else
//			return new FormatoDeEtiquetas("Cliente Comercial", R.color.blue);
	}

	@Override
	public String getMensajedeAdvertencia() {
		// TODO Auto-generated method stub
		if (globales.tll.getLecturaActual().is_advertencias.equals("00"))
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
//		long ll_consumo=0;
//		String anomCapturadas=globales.tll.getLecturaActual().getAnomaliasCapturadas();
//		
//		if (globales.is_lectura.equals("")){
//			globales.tll.getLecturaActual().is_consumo=Main.rellenaString("", " ", globales.tlc.getLongCampo("consumo"), true);
//			return ;
//		}
//		
//		//Hay que checar que las anomalias capturadas sean las que se dejan como AUSENTES
//		
//		ll_consumo = getConsumo(globales.is_lectura);
//		
//		if (ll_consumo < 0 ){
//			ll_consumo = (long) (Math.pow(10, globales.tll.getLecturaActual().numerodeesferas) + ll_consumo);
//		}
////		if (globales.tll.getLecturaActual().is_ism == 2){															// CE REVISAR Pondre en cometarios pues hay que revisar tipos de datos
////			ll_consumo = (long) (ll_consumo + globales.tll.getLecturaActual().is_saldoEnMetros);
////		}
////		if (globales.tll.getLecturaActual().is_ism == 1) {
////			if (globales.tll.getLecturaActual().is_saldoEnMetros < ll_consumo)){
////				ll_consumo = (long) (ll_consumo - globales.tll.getLecturaActual().is_saldoEnMetros);
////			}else if (globales.tll.getLecturaActual().ilr < globales.tll.getLecturaActual().lecturaAnterior) {
////				if (globales.tll.getLecturaActual().is_saldoEnMetros < ll_consumo)
////					ll_consumo = (long) (ll_consumo - globales.tll.getLecturaActual().is_saldoEnMetros);
////				else
////					ll_consumo = 0;
////			}
////		}
//		
//		if (ll_consumo<20){
//			globales.ignorarTomaDeFoto=true;
//		}
//		
//		globales.tll.getLecturaActual().is_consumo=String.valueOf(ll_consumo);
	}

	@Override
	public long getConsumo(String lectura) {
//		return  Long.parseLong(lectura) - globales.tll.getLecturaActual().lecturaAnterior ;
		return  0;					// CE, REVISAR Cambiamos la LecturaAnterior por ILR
	}
	
	public void setTipoLectura(){
		super.setTipoLectura();
	}

	@Override
	public String validaCamposGenericos(String anomalia, Bundle bu_params) {
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
			
			desc=Main.rellenaString(desc, " ", 18, false);
			
			
			globales.tll.getLecturaActual().setComentarios(desc);
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
	
	
	public int [] getArchivosATransmitir(int metodoDeTransmision){
		if (metodoDeTransmision!=TransmisionesPadre.BLUETOOTH){
			int archivos []={/*ARCHIVO_G,*/ ARCHIVO_I, ARCHIVO_R, ARCHIVO_M};
			return archivos;
		}else{
			return new int[0];
		}
		
	}
	
	public String getNombreArchvio(int tipo){
		
		String ls_archivo="";
		TransmitionObject to= new TransmitionObject();
		
		switch(tipo){
		case ARCHIVO_G:
			
			ls_archivo=getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
			to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"G";
			return to.ls_categoria;
		case ARCHIVO_I:
					
					ls_archivo=getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
					to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"I";
					return to.ls_categoria;
		case ARCHIVO_R:
			
			ls_archivo=getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
			to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"R";
			return to.ls_categoria;
		case ARCHIVO_M:
			
			ls_archivo=getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
			to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"M";
			return to.ls_categoria;
//		case ARCHIVO_N:
//			
//			ls_archivo=getEstructuras(  to,  TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
//			to.ls_categoria=to.ls_categoria.substring(0, to.ls_categoria.length()-1)+"N";
//			return ls_archivo;
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
	    	 
	    	 ls_select+= getUsuarioGuardado(db);
	    	 
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
		case ARCHIVO_G:
			
			
			ls_select=globales.tlc.getCampoObjeto("nisrad").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("latitud").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("longitud").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("poliza").campoSQLFormateado() ;
			
			ls_select+=" || if (trim(satelites||fix)='', '0.000'," ;
			
			ls_select+=  globales.tlc.getCampoObjeto("satelites").campoSQLFormateado()  ;
//			ls_select+=" || " + globales.tlc.getCampoObjeto("sinUso1").campoSQLFormateado() ;
			ls_select+=" || '.' || " + globales.tlc.getCampoObjeto("fix").campoSQLFormateado()  ;
			ls_select+=" || ' ') ";
//			ls_select+=" || '" + globales.lote ;
//			ls_select+="' || " + " substr(sinUso1,  6, 5)"  ;
//			ls_select+=" || " + TodosLosCampos.campoSQLFormateado("comentarios", 50, " ", Campo.I);
//			
			ls_select+=" texto"  ;
			
			ls_select="Select cast(sinUso1 as int) sinUso1, " + ls_select + " from ruta where indicadorGPS=1 union select 9999 as sinUso1,  'RFIN' texto order by sinUso1";
			
			c=db.rawQuery(ls_select, null);
			
			break;
		case ARCHIVO_I:
			ls_select=globales.tlc.getCampoObjeto("sinUso1").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("comentarios").campoSQLFormateado() ;
			
//			
			ls_select+=" texto"  ;
			
			ls_select="Select cast(sinUso1 as int) sinUso1," + ls_select + " from ruta where trim(comentarios)<>'' union  select 9999 as sinUso1,'RFIN' texto order by sinUso1";
			
			c=db.rawQuery(ls_select, null);
			
			break;
		case ARCHIVO_R:
			String ls_tmp="";
			String ls_campo=" texto ";
			ls_select=globales.tlc.getCampoObjeto("sinUso1").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("serieMedidor").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("tipoMedidor").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("sectorCorto").campoSQLFormateado() ;
			
			for (int i=1; i<=6; i++){
				ls_tmp+="select cast(sinUso1 as int) sinUso1, " +ls_select + " || " + globales.tlc.getCampoObjeto("intento"+i).campoSQLFormateado() ;
				ls_tmp+=" || " + globales.tlc.getCampoObjeto("fechaReintento"+i).campoSQLFormateado() ;
				ls_tmp+=" || " + i + " ";
				
				ls_tmp+=ls_campo  ;
				
				ls_tmp+= " from ruta where trim(intento"+i+")<>'' ";	
				
//				if (i!=6){
					ls_tmp+=" union ";
//				}
			}
			
			ls_tmp += " Select 9999 sinUso1, 'RFIN' texto ";
			
			ls_tmp += " order by sinUso1";
			c=db.rawQuery(ls_tmp, null);
			
			break;
			
		case ARCHIVO_M:
			
			ls_select=globales.tlc.getCampoObjeto("sinUso1").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("serieMedidor").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("colonia").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("direccion").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("numEdificio").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("numPortal").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("comoLlegar1").campoSQLFormateado() ;
			ls_select+=" || " + globales.tlc.getCampoObjeto("numEsferas").campoSQLFormateado() ;
			ls_select+=" || '   '" ;

			ls_select= "select cast(sinUso1 as int)  sinUso1," + ls_select;
			ls_select += " as texto from ruta where tipoLectura='4' union Select 9999  sinUso1,'RFIN' texto  order by sinUso1";
			c=db.rawQuery(ls_select, null);
			
			break;
		}
		 
		return c;
	}


public String regresaMensajeDeTransmision(int tipo){
	switch(tipo){
	case ARCHIVO_G:
		return "Transmitiendo Puntos GPS";
	case ARCHIVO_I:
		return "Transmitiendo Comentarios";
	case ARCHIVO_M:
		return "Transmitiendo Modificaciones";
	case ARCHIVO_R:
		return "Transmitiendo Intentos";

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
    	 
    	 c=db.rawQuery("Select count(*) canti from ruta where trim(comentarios)<>''", null);
    	 c.moveToFirst();
    	 ll_mensajes=c.getLong(c.getColumnIndex("canti"));
    	 c.close();
    	 
    	 c=db.rawQuery("Select count(*) canti from NoRegistrados", null);
    	 c.moveToFirst();
    	 ll_noRegistrados=c.getLong(c.getColumnIndex("canti"));
    	 c.close();
    	 
    	 c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='0' and upper(serieMedidor)='CONDIR'", null);
    	 c.moveToFirst();
    	 ll_conDir=c.getLong(c.getColumnIndex("canti"));
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
    	 resumen.add(new EstructuraResumen( "Suminis. Pendientes",String.valueOf(ll_restantes),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_tomadas*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Suminis. con Lectura", String.valueOf(ll_tomadas),  formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_sinLect*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Suminis. sin Lectura",String.valueOf(ll_sinLect), formatter.format(porcentaje) +"%"));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 porcentaje=  (((float)ll_conDir*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Suminis. Directos Leidos", String.valueOf(ll_conDir), formatter.format(porcentaje) +"%"));
    	 porcentaje=  (((float)ll_conAnom*100) /(float)ll_total);
    	 resumen.add(new EstructuraResumen("Suminis. con Anomalia",String.valueOf(ll_conAnom), formatter.format(porcentaje) +"%"));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	 
//    	 resumen.add(new EstructuraResumen("Mensajes", String.valueOf(ll_mensajes)));
    	 if (globales.mostrarNoRegistrados)
    		 resumen.add(new EstructuraResumen("Nuevos Suministros", String.valueOf(ll_noRegistrados)));
    	 
    	 resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
    	 
    	 return resumen;
		
	}
	
	public void activacionDesactivacionOpciones(boolean esSuperUsuario){
			
				globales.mostrarMetodoDeTransmision=true;
				globales.mostrarServidorGPRS=true;
				globales.mostrarBorrarRuta=true;
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
	
	public int EncriptarDesencriptarAlterno(byte[] medidor, int nContBytesClaveEncriptado) throws Throwable{
		String Codigo, CAR;	
		int i = 0;	
		char codigo[],car[];		
		String  strClaveEncriptadoElectricaribe = new String("AJ1LXMNP2MDDGX8Y4NLQ5XAAC6WQJ7ZAY8NQ0Z");	
		for(i = 1; i <= medidor.length; i++){
			//CAR = new String(medidor, i - 1, 1);;
			CAR = new String(medidor, i - 1, 1, "ISO-8859-1");;
			nContBytesClaveEncriptado++;
			car = CAR.toCharArray();			
			Codigo = Mid(strClaveEncriptadoElectricaribe, ((nContBytesClaveEncriptado - 1) % strClaveEncriptadoElectricaribe.length()) + 1, 1);
			codigo= Codigo.toCharArray();		
			if (((int)codigo[0] ^ (int)car[0])>255){
				int a=1;
				a++;
			}
			medidor[i-1] = (byte)((int)codigo[0] ^ (int)car[0]); 
		}        
		return nContBytesClaveEncriptado;
	}
	

	
	void EncriptarDesencriptarConParametros(byte[] medidor, int inicio, int longitud){
		byte comodin0 = 2;
  		byte comodin1 = 3;
	  	byte comodin2 = 4;

  		int X = 0;
  		int rep = 0;
		byte byteLetra;
		for (int i=0; i<longitud; i++){
    			byteLetra = medidor[i+inicio];
    			if      (byteLetra == 10) 	medidor[i+inicio] = 10;
    			else if (byteLetra == 9)  	medidor[i+inicio] = 9;
    			else if (byteLetra == 94) 	medidor[i+inicio] = 94;
    			else if (X == 0) 		medidor[i+inicio] = (byte)(byteLetra ^ comodin0);
    			else if (X == 1) 		medidor[i+inicio] = (byte)(byteLetra ^ comodin1);
    			else if (X == 2) 		medidor[i+inicio] = (byte)(byteLetra ^ comodin2);
    			
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
	       Cursor c;
			 
			 if (tipoTransmision==TransmisionesPadre.BLUETOOTH){
				 c=db.rawQuery("Select value from config where key='mac_bluetooth'", null);
				 c.moveToFirst();
				 if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),context.getString(R.string.info_macBluetooth) , context.getString(R.string.str_configuracion),context.getString(R.string.info_macBluetooth));
				  
				
			 }
			 else if (tipoTransmision==TransmisionesPadre.WIFI){
				 c=db.rawQuery("Select value from config where key='server_wifi'", null);
				 c.moveToFirst();
				 if (!validaCampoDeConfig(c))
					  return String.format(context.getString(R.string.msj_config_no_disponible),"Servidor WIFI" , context.getString(R.string.str_configuracion),"Servidor WIFI");
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
			   
			   c=db.rawQuery("Select value from config where key='unicom'", null);
			   
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return  String.format(context.getString(R.string.msj_config_no_disponible), "unicom" , context.getString(R.string.str_configuracion),"unicom");
				  
			   //ls_categoria="";
			   ls_unicom=c.getString(c.getColumnIndex("value")) ;
			   
			   c.close();
			   
			   c=db.rawQuery("Select value from config where key='ruta'", null);
			   
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return  String.format(context.getString(R.string.msj_config_no_disponible), "ruta" , context.getString(R.string.str_configuracion),"ruta");
				  
			   //ls_categoria="";
			   ls_ruta=c.getString(c.getColumnIndex("value")) ;
			   
			   c.close();
			   
			   c=db.rawQuery("Select value from config where key='itinerario'", null);
			   
			   c.moveToFirst();
			   if (!validaCampoDeConfig(c))
					  return  String.format(context.getString(R.string.msj_config_no_disponible), "itinerario" , context.getString(R.string.str_configuracion),"itinerario");
				  
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
			   
			   
			   to.ls_carpeta+=tipo==TransmisionesPadre.TRANSMISION?"":ls_unicom+"-"+ls_ruta+"-"+ls_itinerario+"\\out";
			   
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

			   to.ls_categoria=ls_unicom+ls_itinerario+"."+ls_ruta+"C";
	       
	      // closeDatabase();
	       return "";
	}
	
	
	public String encabezadoAEnviar(String ls_carpeta, String ls_categoria){
		return globales.letraPais + ls_carpeta +"\\" + ls_categoria+Main.obtieneFecha("d/m/y  h:i:s")+"7.5";
	}
	
	
	public byte[] encabezadoAMandar(SQLiteDatabase db){
		TransmitionObject to= new TransmitionObject();
		getEstructuras(db, to, TransmisionesPadre.TRANSMISION,TransmisionesPadre.WIFI);
		
		Cursor c=db.rawQuery("Select value from config where key='ruta_descarga'", null);
		
		c.moveToFirst();
		
		String ruta=c.getString(c.getColumnIndex("value")) +"\\"+ to.ls_categoria.substring(0,to.ls_categoria.length()-1 );
				
		c.close();
		
		c=db.rawQuery("Select registro from encabezado", null);
		
		c.moveToFirst();
		byte [] bytesAEnviar=c.getBlob(c.getColumnIndex("registro"));
		c.close();
		
		String encabezado=new String(bytesAEnviar);
		if (encabezado.substring(200, 212).trim().equals("")){
			String unicom=encabezado.substring(200, 204).trim();
	    	String rutas=encabezado.substring(204, 206).trim();
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
		
			
	    	if (rutas.equals("")){
	    		try{
	    			c=db.rawQuery("Select value from config where key='ruta'", null);
	        		c.moveToFirst();
	        		rutas=c.getString(c.getColumnIndex("value"));
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
	    		String ls_select="";
	    		ls_select=Main.rellenaString(unicom, " ", 4, true);
	    		ls_select+=Main.rellenaString(rutas, " ", 2, true);
	    		ls_select+=Main.rellenaString(itinerario, " ", 4, true);
	    		
	    		for (int i=0; i<ls_select.length();i++)bytesAEnviar[i+200]=ls_select.getBytes()[i];
		}
		
		String ciclo=Main.rellenaString(String.valueOf(Integer.parseInt(Main.obtieneFecha("m"))), " ", 2, true);
		
		for (int i=0; i<ciclo.length();i++)bytesAEnviar[i+210]=ciclo.getBytes()[i];
		

		bytesAEnviar[0]='/';
		
		for (int i=0; i<ruta.length();i++)bytesAEnviar[i+20]=ruta.getBytes()[i];
		
		//Con lectura
		String valor;
		c=db.rawQuery("Select count(*) canti from ruta where anomalia<>'099'", null);
		 c.moveToFirst();
		 
		 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
		 valor= Main.rellenaString(valor, " ", 4, false);
		for (int i=0; i<4;i++)bytesAEnviar[i+264]=valor.getBytes()[i]; //Con lectura
		
		c.close();

		 valor= Main.obtieneFecha("ymd");
		for (int i=0; i<8;i++)bytesAEnviar[i+231]=valor.getBytes()[i]; //Fecha
		
		
		valor = getUsuarioGuardado(db);
		for (int i=0; i<8;i++)bytesAEnviar[i+212]=valor.getBytes()[i]; //Lecturista
		
		
		c= db.rawQuery("Select count(*) canti from Ruta", null);
    	c.moveToFirst();
    	 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
    	 valor=Main.rellenaString(valor, " ", 4, false);
    	 for (int i=0; i<4;i++)bytesAEnviar[i+244]=valor.getBytes()[i]; //Lecturista
    	 
    	 c.close();
		
		
    	 c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='0' and upper(serieMedidor)='CONDIR'", null);
		 c.moveToFirst();
		 
		 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
		 valor= Main.rellenaString(valor, " ", 4, false);
		for (int i=0; i<4;i++)bytesAEnviar[i+248]=valor.getBytes()[i]; //directos
		
		c.close();
		
		c=db.rawQuery("Select count(*) canti from NoRegistrados", null);
		 c.moveToFirst();
		 
		 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
		 valor= Main.rellenaString(valor, " ", 4, false);
		for (int i=0; i<4;i++)bytesAEnviar[i+260]=valor.getBytes()[i]; //Nuevos
		
		c.close();
		
		 c=db.rawQuery("Select count(*) canti from ruta where tipoLectura='4'", null);
		 c.moveToFirst();
		 
		 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
		 valor= Main.rellenaString(valor, " ", 4, false);
		for (int i=0; i<4;i++)bytesAEnviar[i+252]=valor.getBytes()[i]; //ausentes
		
		c.close();
		
		 c=db.rawQuery("Select count(*) canti from ruta where trim(comentarios)<>''", null);
    	 c.moveToFirst();
    	 valor=String.valueOf(c.getLong(c.getColumnIndex("canti")));
    	 valor= Main.rellenaString(valor, " ", 4, false);
    	 for (int i=0; i<4;i++)bytesAEnviar[i+256]=valor.getBytes()[i]; //ausentes
    	 c.close();
    	 
    	 
		
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
	
	
	public String getUsuarioGuardado(){
		openDatabase();
		
		String ls_lecturista=getUsuarioGuardado(db);
		closeDatabase();
		
		return ls_lecturista;
	}
	
	public String getUsuarioGuardado(SQLiteDatabase db){
		String ls_lecturista="";
		//openDatabase();
		
		Cursor c= db.rawQuery("Select * from ruta  where trim(lecturista)<>'' order by cast(fecha as int) desc, cast (hora as int) desc limit 1", null);
		
		c.moveToFirst();
		
		if (c.getCount()>0){
			ls_lecturista=c.getString(c.getColumnIndex("lecturista"));
			
		}
		
		c.close();
		
		if (ls_lecturista.equals("")){
			 c= db.rawQuery("Select registro from encabezado", null);
			 
			c.moveToFirst();
			 
			 if (c.getCount()>0){
				 String ls_encabezado=new String(c.getBlob(c.getColumnIndex("registro")));
					ls_lecturista=ls_encabezado.substring(214, 222).trim();
				}
			c.close();
		}
		
		//closeDatabase();
		
		return Main.rellenaString(ls_lecturista, "0", 8, true);
	}
	
	public Vector <EstructuraResumen> getPrincipal(SQLiteDatabase db){
		

		String unicom="";
		String ruta="";
		String itinerario="";
		String mac_bt="";
		String mac_impr="";
		
		
		String ls_resumen;
		
		Cursor c;

			try{
				c=db.rawQuery("Select value from config where key='unicom'", null);
	    		c.moveToFirst();
	    		unicom=c.getString(c.getColumnIndex("value"));
			}
			catch(Throwable e){
				
			}
			
			try{
				c=db.rawQuery("Select value from config where key='ruta'", null);
	    		c.moveToFirst();
	    		ruta=c.getString(c.getColumnIndex("value"));
			}
			catch(Throwable e){
				
			}
			
			try{
				c=db.rawQuery("Select value from config where key='itinerario'", null);
	    		c.moveToFirst();
	    		itinerario=c.getString(c.getColumnIndex("value"));
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
			
			resumen.add(new EstructuraResumen(unicom, "Unicom"));
			resumen.add(new EstructuraResumen(ruta,"Ruta"));
			resumen.add(new EstructuraResumen(itinerario,"Itinerario"));
	    	
//	    	ls_resumen="CPL: " + cpl +"\n" +
//	    			"Lote: " +  lote +"\n";
	    	
	    	if (!mac_bt.equals("") && !mac_bt.equals(".")){
	    		   
//	    			ls_resumen+="MAC BT: \n"+ mac_bt +"\n";
	    		resumen.add(new EstructuraResumen(mac_bt, /*getString(R.string.info_macBluetooth)*/"MAC"));
	    	}
	    	
	    	if (!mac_impr.equals("") && !mac_impr.equals(".")){
	    		   
//				ls_resumen+="\nMAC Impr: \n"+ mac_impr;
	    		resumen.add(new EstructuraResumen( mac_impr, context.getString(R.string.info_macImpresora)));
		}
	    	
	    	if (!globales.getUsuario().equals("")){

	    			resumen.add(new EstructuraResumen( globales.getUsuario()+"-" + globales.is_nombre_Lect, "Lect."));
	    	}
	    	
			
	    	resumen.add(new EstructuraResumen("", "")); //Agregamos una linea mas
	    	 
	    	 return resumen;
			
		}
	
	Cursor lineasAEscribir(SQLiteDatabase db){
		return getContenidoDelArchivo(db, SALIDA);
	}
	
	public  int[] getCamposGenerico(String anomalia, String subAnomalia){
		int [] campos=null;
		
			if (anomalia.equals("001") && subAnomalia.equals("001") ) {
				campos= new int[2];
				campos[0]=NUM_MEDIDOR ;
				campos[1]=NIC;
			}
				else if ((anomalia.equals("066") && subAnomalia.equals("095")) || anomalia.equals("010")) {
					campos= new int[3];
					campos[0]=NUM_MEDIDOR ;
					campos[1]=NUM_ESFERAS;
					campos[2]=MARCA;				
		}
				else if ((anomalia.equals("088") )) {
					campos= new int[1];
					campos[0]=NUM_ESFERAS;			
		}
		
		return campos;
	}

}
