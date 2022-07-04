package enruta.sistole_gen;

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
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/** Esta clase crea las validaciones y los campos a mostrar**/
public class TomaDeLecturasArgentina extends TomaDeLecturasGenerica {
	
	
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
	
	
	Vector <TextView> textViews= new Vector<TextView>();
	MensajeEspecial mj_estaCortado;
	MensajeEspecial mj_sellos;
	MensajeEspecial mj_consumocero;
	MensajeEspecial mj_ubicacionVacia;
	MensajeEspecial mj_anomalia_seis;
	Hashtable <String, Integer> ht_calidades;
	
	
	
	
	
	public TomaDeLecturasArgentina(Context context) {
		super(context);
		long_registro=490;
		
		//Creamos los campos que serán de salida
		//globales.tlc.getListaDeCamposFormateado(new String[]{"secuencia", "lectura", "anomalia", "fecha", "hora"});
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
		
		globales.multiplesAnomalias=true;
		globales.convertirAnomalias=true;
		
		globales.longitudCodigoAnomalia=2;
		globales.longitudCodigoSubAnomalia=2;
		
		globales.rellenoAnomalia=".";
		globales.rellenarAnomalia=false;
		
		globales.repiteAnomalias=true;
		
		globales.remplazarDireccionPorCalles=true;
		
		globales.mostrarCuadriculatdl=true;
		
		globales.mostrarRowIdSecuencia=true;
		
		globales.dejarComoAusentes=true;
		
		globales.mensajeDeConfirmar=R.string.msj_lecturas_verifique_1;
		
		globales.mostrarNoRegistrados=false;
		globales.tipoDeValidacion=Globales.CONTRASEÑA;
		globales.mensajeContraseñaLecturista=R.string.str_login_msj_lecturista_contrasena_arg;
		globales.controlCalidadFotos=4;
		
		globales.sonidoCorrecta=Sonidos.NINGUNO;
		globales.sonidoIncorrecta=Sonidos.BEEP;
		globales.sonidoConfirmada=Sonidos.NINGUNO;
		
		globales.mostrarMacImpresora=false;
		globales.mostrarServidorGPRS=false;
		globales.mostrarFactorBaremo=false;
		globales.mostrarTamañoFoto=false;
		globales.mostrarMetodoDeTransmision=false;
		globales.mostrarIngresoFacilMAC=false;
		globales.mostrarCalidadFoto=false;
		globales.mostrarBorrarRuta=true;
		
		globales.defaultLote="09-M002";
		globales.defaultCPL="CPL025";
		globales.defaultTransmision="2";
		globales.defaultRutaDescarga="C:\\Apps\\SGL\\Lectura";
		
		globales.letraPais="A";
		
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
		
		globales.mostrarGrabarEnSD=true;
		
		
		
		globales.legacyCaptura=true;
		
		InicializarMatrizDeCompatibilidades();
		inicializaTablaDeCalidades();
	}

	/**
	 * Validacion de una lectura
	 * @param ls_lectAct
	 * @return Regresa el mensaje de error
	 */
	public String validaLectura(String ls_lectAct) {
		
//		if (1==1)
//			return "Soy Nicaragua";
		
		int esferas=0;
		
		if (globales.tll.getLecturaActual().numerodeesferasReal.equals(""))
			esferas=globales.tll.getLecturaActual().numerodeesferas;
		else
			esferas=Integer.parseInt(globales.tll.getLecturaActual().numerodeesferasReal);
		
		if (ls_lectAct.length() > esferas) {
			return NO_SOSPECHOSA + "|" + globales.getString(R.string.msj_validacion_esferas);
		}

		if (ls_lectAct.equals("")) {
			return NO_SOSPECHOSA +"|"+ globales.getString(R.string.msj_validacion_no_hay_lectura);
		}

		long ll_lectAct = Long.parseLong(ls_lectAct);
		
		//La anomalia H, como es cambio de medidor siempre va a aceptar la lectura
		if (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("H")){
			return "";
		}

		//is_lectAnt tiene la lectura anteriormente ingresada, si esta vacio, quiere decir que iniciamos con las validaciones.
		if (is_lectAnt.equals("")) {
			//Si el estado del suministro es cortado, debería aceptar la lectura anterior
			if (ls_lectAct.equals(String.valueOf(globales.tll.getLecturaActual().lecturaAnterior))&& (globales.tll.getLecturaActual().estadoDelSuministro.equals("1") || globales.tll.getLecturaActual().estadoDelSuministro.equals("2"))){
				is_lectAnt = ls_lectAct;
				return "";
			}else if (!ls_lectAct.equals(String.valueOf(globales.tll.getLecturaActual().lecturaAnterior))&& (globales.tll.getLecturaActual().estadoDelSuministro.equals("1") || globales.tll.getLecturaActual().estadoDelSuministro.equals("2"))){
				is_lectAnt = ls_lectAct;
				return /*TomaDeLecturas.FUERA_DE_RANGO;*/ SOSPECHOSA +"|"+ globales.getString(globales.mensajeDeConfirmar);
			}
			
			//Si consumo y baremo son 0 y no esta cortado debe confirmar
			if (getConsumo(ls_lectAct)==0 && globales.tll.getLecturaActual().baremo==0  && !(globales.tll.getLecturaActual().estadoDelSuministro.equals("1") || globales.tll.getLecturaActual().estadoDelSuministro.equals("2"))){
				is_lectAnt = ls_lectAct;
				return /*TomaDeLecturas.FUERA_DE_RANGO;*/ SOSPECHOSA +"|"+ globales.getString(globales.mensajeDeConfirmar);
			}
			//Hay que checar si las estimaciones son  mayores para pedir confirmacion.
			if (Integer.parseInt(globales.tll.getLecturaActual().is_estimaciones)>0){
				is_lectAnt = ls_lectAct;
				globales.ignorarContadorControlCalidad=true;
				return /*TomaDeLecturas.FUERA_DE_RANGO;*/ SOSPECHOSA +"|"+ globales.getString(globales.mensajeDeConfirmar);
			}
			if (globales.il_lect_max < ll_lectAct || globales.il_lect_min > ll_lectAct
					||  globales.tll.getLecturaActual().confirmarLectura() /*|| globales.bModificar*/) {
				is_lectAnt = ls_lectAct;
				boolean seEquivoco = false;
				
				if (globales.il_lect_max < ll_lectAct || globales.il_lect_min > ll_lectAct) {
					seEquivoco = true;
				}

				if (globales.tll.getLecturaActual().is_supervisionLectura.equals("1")) {
					globales.ignorarContadorControlCalidad=true;
					if (seEquivoco)
						globales.is_terminacion = "Y1";
					else
						globales.is_terminacion = "-S";
				}

				if (globales.tll.getLecturaActual().is_reclamacionLectura.equals("1")) {
					globales.ignorarContadorControlCalidad=true;
					if (seEquivoco)
						globales.is_terminacion = "X1";
					else
						globales.is_terminacion = "-R";
				}

				//sonidos.playSoundMedia(Sonidos.URGENT);

				return /*TomaDeLecturas.FUERA_DE_RANGO;*/ SOSPECHOSA +"|"+ globales.getString(globales.mensajeDeConfirmar);
			}
		}
		else {

			if (!is_lectAnt.equals(ls_lectAct)) {
				is_lectAnt = ls_lectAct;
				//Contador de lectura distinta
				globales.tll.getLecturaActual().intentos=String.valueOf(globales.tll.getLecturaActual().intentos)+1;
				//sonidos.playSoundMedia(Sonidos.URGENT);
				globales.ignorarContadorControlCalidad=true;
				return /*NO_*/SOSPECHOSA +"|"+ globales.getString(R.string.msj_lecturas_verifique);
			}

		}

//		if (is_lectAnt.equals("")) {
//			//sonidos.playSoundMedia(Sonidos.BEEP);
//		}
		//Borramos la anomalia 4 cuando campturemos la lectura
		globales.tll.getLecturaActual().deleteAnomalia("4");
		is_lectAnt = "";
		return "";
	}
	
	public String getNombreFoto(Globales globales, SQLiteDatabase db, long secuencial, String is_terminacion,   String ls_anomalia ){
		String ls_nombre="", ls_unicom;
		Cursor c;
		/**
		 * Este es el fotmato del nombre de la foto
		 * 
		 * Poliza a 8 posiciones, ultimos 4 digitos del encabezado (Itinerario), los 2 digitos antes de los 4 anteriores (Ruta), YYYYMMDDHHIISS 
		 * 
		 * la terminacion... -1 Regularmente
		 * Si es de anomalia ... La anomalia ingresada
		 * 
		 */
//Quiero su nis_rad
    	
    	c= db.rawQuery("Select poliza from ruta where cast(secuenciaReal as Integer) ="+secuencial, null);
    	c.moveToFirst();
    	
    	ls_nombre+=Main.rellenaString(c.getString(c.getColumnIndex("poliza")), "0", 7, true);

    	c.close();
		
		c= db.rawQuery("Select registro from encabezado", null);
		ls_nombre+="20";
    	
    	c.moveToFirst();
    	ls_unicom= new String (c.getBlob(c.getColumnIndex("registro")));
    	
    	ls_nombre+= ls_unicom.substring(ls_unicom.length()-4, ls_unicom.length())+ls_unicom.substring(ls_unicom.length()-6, ls_unicom.length()-4);
    	c.close();
    	
    	
    	
    	//ls_nombre=caseta+ "_"+ secuencial + "_" + Main.obtieneFecha()+".jpg";
    	
    	ls_nombre+=Main.obtieneFecha("ymdhis");
    	//Hay que preguntar por la terminacion
    	ls_nombre+= ls_anomalia.equals("")?is_terminacion:"_"+ls_anomalia +".JPG";
    	
    	return ls_nombre;
		
	}

	public  Vector<String> getInformacionDelMedidor(Lectura lectura) {
		
		
		Vector <String> datos= new Vector<String>();
		
		//Esta variable la usaremos para poder determinar si algun dato es vacio y mostrar solo lo necesario en la pantalla
		String comodin="";
		
		datos.add(lectura.getDireccion() + " #" +lectura.numeroDeEdificio.trim() + (!lectura.numeroDePortal.trim().equals("")?"-" +lectura.numeroDePortal.trim():""));
		if (!lectura.getColonia().equals("")){
			datos.add(lectura.getColonia());
		}
		if (!lectura.is_aviso.equals("")){
			datos.add(lectura.is_aviso.trim());
		}
		
		
		
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
		
		//Vamos a agregar los campos que se van llenando mientras se agregan anomalias
		String ls_anom=lectura.getAnomaliasCapturadas();
		//if (ls_anom.contains("B") || ls_anom.contains("H")){
		
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
		
		ls_comentarios="\n" + ls_comentarios
		+ globales.tll.getLecturaActual().getComentarios();
		if (!ls_comentarios.trim().equals(""));
			datos.add(ls_comentarios);
		
		if (!lectura.serieMedidorReal.trim().equals("")){
			datos.add("Num. Medidor Real: " + lectura.serieMedidorReal.trim());
		}
		
		if (!lectura.numerodeesferasReal.trim().equals("")){
			datos.add("Num. Esferas Real: " + lectura.numerodeesferasReal.trim());
		}
			
		//}
		
		
		//datos.add(lectura.getDireccion());
		
//		//Vamos a obtener cual es el tipo de consumo
//		int li_clave = Integer
//				.parseInt(lectura.is_tarifa.trim()
//						.substring(2));
//		String tipoConsumo="";
//
//		switch (li_clave) {
//		case 11:
//			tipoConsumo = "Cliente Doméstico";
//			break;
//		case 12:
//			tipoConsumo = "Cliente Comercial";
//			break;
//
//		default:
//			tipoConsumo = globales.tll.getLecturaActual().is_tarifa;
//			break;
//		}
//			
//			datos.add(tipoConsumo);
		
			return datos;
		
//		TextView tv_view;
//		LinearLayout ll_linear;
//		LayoutParams layout_params;
//		
//		ll_generico.removeAllViewsInLayout();
//		
//		
//		//La primera linea debe tener el acceso y el estado del suministro en la misma linea
//		//El acceso debe ser 70% y el estado un 30%
//		
//		ll_linear=new LinearLayout(context);
//		tv_view=new TextView(context);
//		
//		
//		ll_linear.setOrientation(LinearLayout.HORIZONTAL);
//		
//		layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		
//		tv_view.setText(lectura.getAcceso());
//		tv_view.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
//		layout_params.weight=0.70f;
//		ll_linear.addView(tv_view, layout_params);
//		
//		
//		//Ahora, el estado del suministro
//		tv_view=new TextView(context);
//		layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		
//		tv_view.setText(lectura.estadoDelSuministro);
//		tv_view.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
//		layout_params.weight=0.20f;
//		layout_params.gravity= Gravity.RIGHT;
//		ll_linear.addView(tv_view, layout_params);
//		
//		layout_params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//		ll_generico.addView(ll_linear, layout_params);
//
//
//		//Ahora el nombre del cliente
//		layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//		tv_view=new TextView(context);
//		tv_view.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
//		tv_view.setText(lectura.getNombreCliente());
//		ll_generico.addView(tv_view, layout_params);
//		
//		//Direccion
//		layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//		tv_view=new TextView(context);
//		tv_view.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
//		tv_view.setText(lectura.getDireccion());
//		ll_generico.addView(tv_view, layout_params);
//		
//		//Marca
//		layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//		tv_view=new TextView(context);
//		tv_view.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
//		tv_view.setText("HOLA");
//		ll_generico.addView(tv_view, layout_params);
//		
//		//Vamos a obtener cual es el tipo de consumo
//		int li_clave = Integer
//				.parseInt(lectura.is_tarifa.trim()
//						.substring(2));
//		String tipoConsumo="";
//
//		switch (li_clave) {
//		case 11:
//			tipoConsumo = "Cliente Doméstico";
//			break;
//		case 12:
//			tipoConsumo = "Cliente Comercial";
//			break;
//
//		default:
//			tipoConsumo = globales.tll.getLecturaActual().is_tarifa;
//			break;
//
//		}
//		
//		//Ahora el nombre del tipo de consumo
//		layout_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//		tv_view=new TextView(context);
//		tv_view.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
//		tv_view.setText(tipoConsumo);
//		ll_generico.addView(tv_view, layout_params);
		
				
	}

	@Override
	public MensajeEspecial getMensaje() {
		// TODO Auto-generated method stub
		//Ejemplo de la prueba
//		if (tipo%2==0)
//			return mj_activo;
//		else if (tipo%3==0)
//			return mj_sellos;
//		
		
		if (globales.tll.getLecturaActual().is_ubicacion.trim().equals("")){
			return mj_ubicacionVacia;
		}
		
		return null;
	}
	
	private void InicializarMatrizDeCompatibilidades(){
		
		anomaliasCompatibles= new Hashtable<String, String>();
		
		anomaliasCompatibles.put("A", "CEST13");
		anomaliasCompatibles.put("B", "CEFGIJKNSTVWY1234");
		anomaliasCompatibles.put("C", "ABEFGHIJKLNRSTVWYZ1234");
		anomaliasCompatibles.put("D", "");
		anomaliasCompatibles.put("E", "ABCFGHIJKLNRSTVWY1234");
		anomaliasCompatibles.put("F", "BCEGHIJKNSTVWY1234");
		anomaliasCompatibles.put("G", "BCEFHIJKNSTVWY12346");
		anomaliasCompatibles.put("H", "CEFGJKNSTVY13");
		anomaliasCompatibles.put("I", "BCEFGJKNSTVY1234");
		anomaliasCompatibles.put("J", "BCEFGHIKNOPQSTUVWXY12345");
		anomaliasCompatibles.put("K", "BCEFGHIJNSTVWY1234");
		
		anomaliasCompatibles.put("L", "CENSTVY3");
		anomaliasCompatibles.put("N", "BCEFGHIJKLRSTVWY1234");
		anomaliasCompatibles.put("R", "CENSTVY136");
		anomaliasCompatibles.put("S", "ABCEFGHIJKLNRTVWYZ1234");
		anomaliasCompatibles.put("T", "ABCEFGHIJKLNRSVWYZ1234");
		anomaliasCompatibles.put("V", "ABCEFGHIJKLNRSTWYZ1234");
		anomaliasCompatibles.put("W", "BCEFGJKNSTWY1234");
		anomaliasCompatibles.put("Y", "BCEFGHIJKLNRSTVWZ1234");
		anomaliasCompatibles.put("Z", "CSTVY36");
		anomaliasCompatibles.put("1", "ABCEFGHIJKNRSTVWY234");
		anomaliasCompatibles.put("2", "BCEFGIJKNSTVWY134");
		anomaliasCompatibles.put("3", "ABCEFGHIJKLNRSTVWYZ124");
		anomaliasCompatibles.put("4", "BCEFGIJKNSTVWY123");
		anomaliasCompatibles.put("5", "56");
		anomaliasCompatibles.put("6", "6RZ5");
		
	}

	@Override
	public boolean esAnomaliaCompatible(String anomaliaAInsertar,
			String anomaliasCapturadas) {
		int lastIndexOfStar=0;
		boolean esCompatible=true;
		
		String cadena=anomaliasCompatibles.get(anomaliaAInsertar);
		
		
		if (anomaliasCapturadas.endsWith("*")){
			//No se han ingresado anomalias
			return true;
		}
		
		lastIndexOfStar=anomaliasCapturadas.lastIndexOf("*")+1;
		
		
		for (int i=lastIndexOfStar; i<anomaliasCapturadas.length();i++){
			if(cadena.indexOf(anomaliasCapturadas.substring(i, i+1))<0){
				esCompatible= false;
				break;
			}
			
		}
		
		//Excepciones
		if (anomaliasCapturadas.contains("A")){
			if("RLS".contains(anomaliaAInsertar)){
				esCompatible= true;
			}
			
		}
		else if (anomaliasCapturadas.contains("R")){
			if("LS".contains(anomaliaAInsertar)){
				esCompatible= true;
			}
		}
		else if (anomaliasCapturadas.contains("Z")){
			if("ARLSD".contains(anomaliaAInsertar)){
				esCompatible= true;
			}
		}else if (anomaliasCapturadas.contains("5")){
			if("ARLSD".contains(anomaliaAInsertar)){
				esCompatible= true;
			}
		}
		
		return esCompatible;
	}

	@Override
	public ComentariosInputBehavior getAvisoMensajeInput(String anomalia) {
		ComentariosInputBehavior cib_config=null;
		int longitud_disponible=globales.tlc.getLongCampo("comentarios")-(globales.tll.getLecturaActual().getComentarios().length())+3;
		// TODO Auto-generated method stub
		if (anomalia.equals("2")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_esferas), InputType.TYPE_CLASS_NUMBER, 1, globales.tll.getLecturaActual().numerodeesferasReal.equals("")?String.valueOf(globales.tll.getLecturaActual().numerodeesferas):globales.tll.getLecturaActual().numerodeesferasReal);
			
		}
		
//		if (anomalia.equals("R")) {
//			cib_config = new ComentariosInputBehavior("", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, longitud_disponible,"");
//			
//		}
//		
//		if (anomalia.equals("S")) {
//			cib_config = new ComentariosInputBehavior("", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, longitud_disponible,"");
//			
//		}
		
		if (anomalia.equals("N")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_direccion), InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("comentarios"), String.valueOf(globales.tll.getLecturaActual().getDireccion()));
			
		}
		if (anomalia.equals("1")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_ubicacion), InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 2, "");
		}
		if (anomalia.equals("T")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_entrecalles), InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("aviso"),  globales.tll.getLecturaActual().is_aviso.trim());
			cib_config.obligatorio=false;
			
		}
		if (anomalia.equals("Y")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_nombre),InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("cliente"),  String.valueOf(globales.tll.getLecturaActual().getCliente()));
			cib_config.obligatorio=false;
		}
		if (anomalia.equals("C")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_medidor_antpos), InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("comentarios"), "");
		}
//		if (anomalia.equals("3")) {
//			csTablaAnomalias += "Escriba la SubAnomalia";
//		}
		if (anomalia.equals("E")) {
			
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_advertencia), InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("advertencias") ,  globales.tll.getLecturaActual().is_advertencias.trim());
			cib_config.obligatorio=false;
		}
		if (anomalia.equals("V")) {
			cib_config = new ComentariosInputBehavior(context.getString(R.string.lbl_comentarios_ingrese_act_comercial), InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,longitud_disponible , "");
		}
		return cib_config;
	}

	@Override
	public void RealizarModificacionesDeAnomalia(String anomalia, String comentarios) {
		// TODO Auto-generated method stub
		String queActualizar="";
		boolean actualizar=false;
		
		if(anomalia.equals("Y")){
			//Cambiamos Nombre del cliente
			queActualizar="cliente='"+comentarios+"'";
			actualizar=true;
			globales.tll.getLecturaActual().setCliente(comentarios);
		}
		else if (anomalia.equals("2")){
			//numEsferas
			queActualizar="numEsferasReal='"+comentarios+"'";
			actualizar=true;
			globales.tll.getLecturaActual().numerodeesferasReal=comentarios;
		}
//		else if (anomalia.equals("1")){
//			//numEsferas
//			queActualizar="ubicacion='"+comentarios+"'";
//			actualizar=false;
//			globales.tll.getLecturaActual().is_ubicacion=comentarios;
//		}
		else if (anomalia.equals("T")){
			//numEsferas
			queActualizar="aviso='"+comentarios+"'";
			actualizar=true;
			globales.tll.getLecturaActual().is_aviso=comentarios;
		}
		else if (anomalia.equals("E")){
			//numEsferas
			queActualizar="advertencias='"+comentarios+"'";
			actualizar=false;
			globales.tll.getLecturaActual().is_advertencias=comentarios;
		}
		
		if (actualizar){
			openDatabase();
			db.execSQL("Update ruta set "+ queActualizar +" where cast(secuenciaReal as integer)="+globales.tll.getLecturaActual().secuenciaReal);
			closeDatabase();
		}
		
	}
	
	@Override
	public void RealizarModificacionesDeAnomalia(String anomalia) {
		if(anomalia.startsWith("1")){
			globales.tll.getLecturaActual().is_ubicacion=anomalia.substring(1);
		}
	}

	@Override
	public void DeshacerModificacionesDeAnomalia(String anomalia) {
		// TODO Auto-generated method stub
		
		if (anomalia.equals("2")){
			RealizarModificacionesDeAnomalia(anomalia, "");
		}else if (anomalia.equals("E")){
			RealizarModificacionesDeAnomalia(anomalia, "");
		}
		else if (anomalia.equals("H")){
			globales.tll.getLecturaActual().serieMedidorReal="";
			globales.tll.getLecturaActual().numerodeesferasReal="";
		}
		else if (anomalia.equals("B")){
			globales.tll.getLecturaActual().serieMedidorReal="";
		}
	}

	@Override
	public MensajeEspecial mensajeDeConsumo(String ls_lectAct) {
		// TODO Auto-generated method stub
		//Hay que convertirla a entero
		
		//Vamos a realizar una prueba... no se los estados del suministro, asi que si es par es 0 y non 4
		String estadoDelSuministro= globales.tll.getLecturaActual().estadoDelSuministro;
		
		int li_lectAct= Integer.parseInt(ls_lectAct);
		if (globales.tll.getLecturaActual().lecturaAnterior!=li_lectAct &&( estadoDelSuministro.equals("1") ||  estadoDelSuministro.equals("2")) ){
			//Si es la misma o menor... quiere decir que no hubo un consumo
			return mj_estaCortado;
		}
		
		//Hay la otra causa de un mensaje de consumo.. cuando esta el estado de suministro igual a 4 (cortado) y el consumo es diferente de 0
		
		if (globales.tll.getLecturaActual().lecturaAnterior==li_lectAct && estadoDelSuministro.equals("0")){
			//Si es la misma o menor... quiere decir que no hubo un consumo
			return mj_consumocero;
		}
		return null;
	}

	@Override
	public void RespuestaMensajeSeleccionada(MensajeEspecial me, int respuesta) {
		// TODO Auto-generated method stub
		
		switch (me.respondeA){
		case PREGUNTAS_SIGUE_CORTADO:
			if (respuesta== MensajeEspecial.NO){
				//Borramos si hay una j
				globales.tll.getLecturaActual().deleteAnomalia("J");
				//Agregamos la anomalia J al vector de anomalias
				cambiosAnomaliaAntesDeGuardar(globales.is_lectura);
				globales.tll.getLecturaActual().setAnomalia("J");
				globales.is_presion=globales.tll.getLecturaActual().getAnomalia();
				globales.tll.getLecturaActual().is_estadoDelSuministroReal="0";
			}else{
				globales.tll.getLecturaActual().is_estadoDelSuministroReal="1";
			}
			break;
		case PREGUNTAS_CONSUMO_CERO:
			//Borramos la anomalia y la sub
			globales.tll.getLecturaActual().deleteAnomalia(me.regresaValor(respuesta).substring(0, 1));
			//Agregamos
			cambiosAnomaliaAntesDeGuardar(globales.is_lectura);
			globales.tll.getLecturaActual().setAnomalia(me.regresaValor(respuesta).substring(0, 1));
			globales.tll.getLecturaActual().setSubAnomalia(me.regresaValor(respuesta));
			
			globales.is_presion=globales.tll.getLecturaActual().getAnomalia();
			break;
		case PREGUNTAS_EN_EJECUCION:
			break;
			
		case PREGUNTAS_UBICACION_VACIA:
			globales.tll.getLecturaActual().is_ubicacion=me.regresaValor(respuesta).substring(1,2);
			globales.tll.getLecturaActual().deleteAnomalia(me.regresaValor(respuesta).substring(0,1));
			globales.tll.getLecturaActual().setAnomalia(me.regresaValor(respuesta).substring(0,1));
			globales.tll.getLecturaActual().setSubAnomalia(me.regresaValor(respuesta));
			break;
			
		case ANOMALIA_SEIS:
			globales.tll.getLecturaActual().deleteAnomalia("6");
//			globales.tll.getLecturaActual().deleteAnomalia("R");
//			globales.tll.getLecturaActual().deleteAnomalia("Z");
//			globales.tll.getLecturaActual().deleteAnomalia("5");
//			globales.tll.getLecturaActual().deleteAnomalia("G");
//			globales.tll.getLecturaActual().deleteAnomalia(me.regresaValor(respuesta));
			globales.tll.getLecturaActual().setAnomalia(me.regresaValor(respuesta));
			globales.tll.getLecturaActual().setAnomalia("6");
			break;
		}
		
	}

	@Override
	public ComentariosInputBehavior getCampoGenerico(int campo) {
		// TODO Auto-generated method stub
		
		ComentariosInputBehavior cib_config=null;
		
		switch(campo){
		case MEDIDOR_ANTERIOR:
			cib_config = new ComentariosInputBehavior("Medidor Anterior", InputType.TYPE_CLASS_NUMBER, globales.tlc.getLongCampo("serieMedidor"), "");
			cib_config.obligatorio=false;
			break;
		case MEDIDOR_POSTERIOR:
			cib_config = new ComentariosInputBehavior("Medidor Posterior", InputType.TYPE_CLASS_NUMBER, globales.tlc.getLongCampo("serieMedidor"), "");
			cib_config.obligatorio=false;
			break;
			
		case NUM_MEDIDOR:
			cib_config = new ComentariosInputBehavior("Número de Medidor", InputType.TYPE_CLASS_NUMBER, globales.tlc.getLongCampo("serieMedidor"), "");
			break;
			
		case NUM_ESFERAS:
			cib_config = new ComentariosInputBehavior("Número de Esferas", InputType.TYPE_CLASS_NUMBER, globales.tlc.getLongCampo("numEsferas"), "");
			break;
		case MARCA:
			cib_config = new ComentariosInputBehavior("Marca de Medidor", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, 3, "");
			break;
		case CALLE:
			cib_config = new ComentariosInputBehavior("Calle", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("direccion"), globales.tll.getLecturaActual().getDireccion());
			cib_config.obligatorio=false;
			break;
		case NUMERO:
			cib_config = new ComentariosInputBehavior("Número", InputType.TYPE_CLASS_NUMBER, globales.tlc.getLongCampo("numEdificio"), globales.tll.getLecturaActual().numeroDeEdificio.trim());
			cib_config.obligatorio=false;
			break;
		case PORTAL:
			cib_config = new ComentariosInputBehavior("Portal", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,globales.tlc.getLongCampo("numPortal"), globales.tll.getLecturaActual().numeroDePortal.trim());
			cib_config.obligatorio=false;
			break;
		case ESCALERA:
			cib_config = new ComentariosInputBehavior("Escalera", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("escalera"), globales.tll.getLecturaActual().is_escalera.trim());
			cib_config.obligatorio=false;
			break;
		case PISO:
			cib_config = new ComentariosInputBehavior("Piso", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("piso"), globales.tll.getLecturaActual().is_piso.trim());
			cib_config.obligatorio=false;
			break;
		case PUERTA:
			cib_config = new ComentariosInputBehavior("Puerta", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("puerta"), globales.tll.getLecturaActual().is_puerta.trim());
			cib_config.obligatorio=false;
			break;
		case COMPLEMENTO:
			cib_config = new ComentariosInputBehavior("Complemento", InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS, globales.tlc.getLongCampo("colonia"), globales.tll.getLecturaActual().getColonia());
			cib_config.obligatorio=false;
			break;
			
			
		}
		
		return cib_config;
	}

	@Override
	public int[] getCamposGenerico(String anomalia) {
		// TODO Auto-generated method stub
		int [] campos=null;
		if (anomalia.equals("C")) {
			campos= new int[2];
			campos[0]=MEDIDOR_ANTERIOR;
			campos[1]=MEDIDOR_POSTERIOR;
		} 
		else if (anomalia.equals("B") || anomalia.equals("H")) {
			campos= new int[3];
			campos[0]=NUM_MEDIDOR;
			campos[1]=NUM_ESFERAS;
			campos[2]=MARCA;
		} 
		else if (anomalia.equals("N")) {
			campos= new int[7];
			campos[0]=CALLE;
			campos[1]=NUMERO;
			campos[2]=PORTAL;
			campos[3]=ESCALERA;
			
			campos[4]=PISO;
			campos[5]=PUERTA;
			campos[6]=COMPLEMENTO;
		} 
		return campos;
	}

	@Override
	public void regresaDeCamposGenericos(Bundle bu_params, String anomalia) {
		// TODO Auto-generated method stub
		if (anomalia.equals("C")) {
			globales.tll.getLecturaActual().setComentarios("MA:" +bu_params.getString(String.valueOf(MEDIDOR_ANTERIOR)) +",MP:"+bu_params.getString(String.valueOf(MEDIDOR_POSTERIOR)));
		} 
		else if (anomalia.equals("B") || anomalia.equals("H")) {
			globales.tll.getLecturaActual().numerodeesferasReal=bu_params.getString(String.valueOf(NUM_ESFERAS));
			globales.tll.getLecturaActual().serieMedidorReal=bu_params.getString(String.valueOf(NUM_MEDIDOR));
//			 if (anomalia.equals("B")){
//				 globales.tll.getLecturaActual().setAnomalia("2");
//			 }
		} else if (anomalia.equals("N")) {
			globales.tll.getLecturaActual().setDireccion(bu_params.getString(String.valueOf(CALLE)));
			globales.tll.getLecturaActual().numeroDeEdificio=bu_params.getString(String.valueOf(NUMERO));
			globales.tll.getLecturaActual().numeroDePortal=bu_params.getString(String.valueOf(PORTAL));
			globales.tll.getLecturaActual().is_escalera=bu_params.getString(String.valueOf(ESCALERA));
			globales.tll.getLecturaActual().is_piso=bu_params.getString(String.valueOf(PISO));
			globales.tll.getLecturaActual().is_puerta=bu_params.getString(String.valueOf(PUERTA));
			globales.tll.getLecturaActual().setColonia(bu_params.getString(String.valueOf(COMPLEMENTO)));
			
		}if (anomalia.equals("34")) {
			globales.tll.getLecturaActual().setComentarios("34:" +bu_params.getString("input"));
		} 
		else if (anomalia.equals("ZT")) {
			globales.tll.getLecturaActual().setComentarios("ZT:" +bu_params.getString("input"));
		} 
		else if (anomalia.equals("R4")) {
			globales.tll.getLecturaActual().setComentarios("R4:" +bu_params.getString("input"));
		} 
		else if (anomalia.equals("S4")) {
			globales.tll.getLecturaActual().setComentarios("S4:" +bu_params.getString("input"));
		} 
		else if (anomalia.equals("V")) {
			globales.tll.getLecturaActual().setComentarios("AC:" +bu_params.getString("input"));
		} 
	}
	


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

	@Override
	public void creaTodosLosCampos() {
		// TODO Auto-generated method stub
			
			//globales.tlc.add(new Campo(0, "registro", 0, 490, Campo.D, ""));
			
			globales.tlc.add(new Campo(0, "sinUso1", 0, 8, Campo.D, " "));
			globales.tlc.add(new Campo(1, "secuencia", 8, 6, Campo.D, "0"));
			globales.tlc.add(new Campo(2, "comentarios", 14, 90, Campo.I, " "));
			//globales.tlc.add(new Campo(3, "situacionDelSuministro", 97, 1, Campo.D, " "));
			globales.tlc.add(new Campo(4, "subAnomalia", 104, 10, Campo.I, " "));
			globales.tlc.add(new Campo(5, "cliente", 114, 50, Campo.I, " "));
			globales.tlc.add(new Campo(6, "direccion", 164, 30, Campo.I, " "));
			globales.tlc.add(new Campo(7, "numEdificio", 194, 5, Campo.I, " "));
			globales.tlc.add(new Campo(8, "numPortal", 199, 3, Campo.I, " "));
			globales.tlc.add(new Campo(9, "aviso", 202, 40, Campo.I, " "));
			globales.tlc.add(new Campo(10, "lecturaAnterior", 242, 10, Campo.D, "0"));
			globales.tlc.add(new Campo(11, "baremo", 252, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(12, "ilr", 259, 10, Campo.D, "0"));
			globales.tlc.add(new Campo(13, "tipoLectura", 269, 1, Campo.I, " "));
			globales.tlc.add(new Campo(14, "lectura", 270, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(15, "consumo", 277, 7, Campo.D, "0"));
			globales.tlc.add(new Campo(16, "fecha", 284, 10, Campo.F, "Ymdhi"));
			globales.tlc.add(new Campo(17, "anomalia", 294, 30, Campo.I, " "));
			globales.tlc.add(new Campo(18, "divisionContrato", 324, 2, Campo.D, "0"));
			globales.tlc.add(new Campo(19, "sospechosa", 326, 2, Campo.D, " "));//Confirmadax
			globales.tlc.add(new Campo(20, "intentos", 328, 2, Campo.D, " "));//Distinta
			globales.tlc.add(new Campo(21, "marcaMedidor", 330, 0, Campo.I, " "));
			globales.tlc.add(new Campo(22, "tipoMedidor", 330, 0, Campo.I, " "));
			globales.tlc.add(new Campo(23, "serieMedidor", 330,8, Campo.D, " "));
			globales.tlc.add(new Campo(24, "colonia", 338, 49, Campo.I, " "));
			globales.tlc.add(new Campo(6, "sinUso2", 387, 1, Campo.I, " "));
			globales.tlc.add(new Campo(25, "poliza", 388, 7, Campo.D, " "));
			globales.tlc.add(new Campo(26, "numEsferas", 395, 1, Campo.D, " "));
			globales.tlc.add(new Campo(27, "consAnoAnt", 395, 0, Campo.D, "0"));
			globales.tlc.add(new Campo(28, "consBimAnt", 395, 0, Campo.D, "0"));
			globales.tlc.add(new Campo(29, "hora", 396, 6, Campo.F, "his"));
			globales.tlc.add(new Campo(30, "lecturista", 402, 4, Campo.D, "0"));
			globales.tlc.add(new Campo(31, "ordenDeLectura", 406, 4, Campo.D, "0"));
			globales.tlc.add(new Campo(32, "escalera", 410, 3, Campo.I, " "));
			globales.tlc.add(new Campo(33, "piso", 413, 2,  Campo.I, " "));
			globales.tlc.add(new Campo(34, "puerta", 415, 5, Campo.D, " "));
			globales.tlc.add(new Campo(35, "reclamacionLectura", 420, 1, Campo.D, " "));
			globales.tlc.add(new Campo(36, "supervisionLectura", 421, 1, Campo.D, " "));
			globales.tlc.add(new Campo(37, "reclamacion", 422, 1, Campo.D, " "));
			globales.tlc.add(new Campo(38, "supervision", 423, 1, Campo.D, " "));
			globales.tlc.add(new Campo(39, "advertencias", 424, 40, Campo.I, " "));
			globales.tlc.add(new Campo(40, "ubicacion", 464, 2, Campo.I, " "));
			globales.tlc.add(new Campo(41, "tarifa", 466, 2, Campo.I, " "));
			globales.tlc.add(new Campo(41, "estimaciones", 468, 1, Campo.I, " "));
			globales.tlc.add(new Campo(42, "estadoDelSuministro", 469, 1, Campo.D, " "));
			globales.tlc.add(new Campo(43, "numEsferasReal", 470, 1, Campo.D, " "));
			globales.tlc.add(new Campo(44, "fechaAviso", 471, 10, Campo.F, "Ymdhi"));
			globales.tlc.add(new Campo(45, "serieMedidorReal", 481, 8, Campo.D, " "));
			//add(new Campo(18, "rutaReal", 490, 4, Campo.D, ""));
			
			globales.tlc.add(new Campo(35, "estadoDelSuministroReal", 489, 1, Campo.D, " "));
			
	}

	@Override
	public long getLecturaMinima() {
		// TODO Auto-generated method stub
		long minima/*= 100-devolverConfiguracionInt("baremo")*/;
		/*minima= minima<0?0:minima;*/
		
		float baremo_tpl =  ((float)(100 - globales.baremo)) /100;
		if(baremo_tpl<0){
			baremo_tpl=0;
		}
		//ahora hay que multiplicarlo por el baremo que hay en el archivo
		minima=Math.round((globales.tll.getLecturaActual().lecturaAnterior) + globales.tll.getLecturaActual().baremo *baremo_tpl);
		
		if ((((globales.tll.getLecturaActual().lecturaAnterior) + globales.tll.getLecturaActual().baremo * baremo_tpl) - minima) > 0){
			minima++;
		}
		
		return minima;
	}

	@Override
	public long getLecturaMaxima() {
//		// TODO Auto-generated method stub
//		int maxima= 100+devolverConfiguracionInt("baremo");
//		
//		//ahora hay que multiplicarlo por el baremo que hay en el archivo
//		maxima=maxima*globales.tll.getLecturaActual().baremo;
		float baremo_tpl =  ((float)(100 + globales.baremo)) / 100;
		long maxima= (long)((globales.tll.getLecturaActual().lecturaAnterior) + globales.tll.getLecturaActual().baremo *baremo_tpl);
		return maxima;
	}

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
			return globales.tll.getLecturaActual().is_ubicacion.trim();
		}
		else if (ls_etiqueta.equals("campo3")){
			return globales.tll.getLecturaActual().estadoDelSuministro.trim();
		}
		else if (ls_etiqueta.equals("campo4")){
			return globales.tll.getLecturaActual().is_estimaciones.trim();
		}
		else{
			return "";
		}
	}

	@Override
	public FormatoDeEtiquetas getMensajedeRespuesta() {
		// TODO Auto-generated method stub
		if (!globales.tll.getLecturaActual().is_estimaciones.equals("0")){
			return new FormatoDeEtiquetas("Lectura Crítica", R.color.red);
		}
		else if ((globales.tll.getLecturaActual().estadoDelSuministro.equals("1") || globales.tll.getLecturaActual().estadoDelSuministro.equals("2"))){
			return new FormatoDeEtiquetas("Cortado", R.color.Orange);
		}
		return null;
	}

	@Override
	public String getMensajedeAdvertencia() {
		// TODO Auto-generated method stub
		return globales.tll.getLecturaActual().is_advertencias.trim();
	}

	@Override
	public void regresaDeBorrarLectura() {
		// TODO Auto-generated method stub
		globales.tll.getLecturaActual().deleteAnomalia("4");
		globales.tll.getLecturaActual().deleteAnomalia("I");
		globales.tll.getLecturaActual().deleteAnomalia("W");
		globales.tll.getLecturaActual().deleteAnomalia("J");
	}

	@Override
	public void cambiosAnomaliaAntesDeGuardar(String ls_lect_act) {
		// TODO Auto-generated method stub
		if (!ls_lect_act.equals("")){
			String ls_anomalia=globales.tll.getLecturaActual().getAnomaliasCapturadas();
			
			if ((ls_anomalia.contains("A") && globales.tll.getLecturaActual().containsSubAnomalia("AA") )||ls_anomalia.contains("R")||ls_anomalia.contains("Z")||ls_anomalia.contains("5")){
				globales.tll.getLecturaActual().setAnomalia("*");
				if (!globales.tll.getLecturaActual().getSubAnomaliaAMostrar().equals("")){
					globales.tll.getLecturaActual().setSubAnomalia("*");
				}
			}
		}
		
	}

	@Override
	public void anomaliasARepetir() {
		// TODO Auto-generated method stub
		String ls_anomalias= globales.tll.getLecturaActual().getAnomaliasAIngresadas();
		String ls_lectura =globales.is_lectura;
		boolean repetirHastaAnomalia =globales.anomaliaARepetir.equals("N") || globales.anomaliaARepetir.equals("T");
		if (ls_anomalias.endsWith("R")||
				(ls_anomalias.endsWith("Z") && (globales.tll.getLecturaActual().containsSubAnomalia("ZG") || globales.tll.getLecturaActual().containsSubAnomalia("ZL")  || globales.tll.getLecturaActual().containsSubAnomalia("ZM")))||ls_anomalias.endsWith("5")||ls_anomalias.endsWith("N")||ls_anomalias.endsWith("T")||ls_anomalias.endsWith("C")){
			globales.anomaliaARepetir= ls_anomalias.substring(ls_anomalias.length()-1);
			globales.lecturaARepetir=globales.tll.getLecturaActual();
		}else if (ls_anomalias.endsWith("AC")||ls_anomalias.endsWith("CA")){
			globales.anomaliaARepetir= ls_anomalias.substring(ls_anomalias.length()-2);
			globales.lecturaARepetir=globales.tll.getLecturaActual();
		}else if (ls_anomalias.endsWith("A")){
			globales.anomaliaARepetir= ls_anomalias.substring(ls_anomalias.length()-1);
			globales.lecturaARepetir=globales.tll.getLecturaActual();
		}else if (!ls_anomalias.endsWith(globales.anomaliaARepetir) &&
				((!ls_anomalias.equals("")/* && repetirHastaAnomalia*/) ||
						(!ls_lectura.equals("") && !repetirHastaAnomalia) )){
			globales.anomaliaARepetir="";
			//!(ls_anomalias.endsWith("N"))
		}
		else if (ls_anomalias.endsWith("Z") && !(globales.tll.getLecturaActual().containsSubAnomalia("ZG") || globales.tll.getLecturaActual().containsSubAnomalia("ZL")  || globales.tll.getLecturaActual().containsSubAnomalia("ZM"))){
			globales.anomaliaARepetir="";
		}
		
	}

	@Override
	public void subAnomaliasARepetir() {
		// TODO Auto-generated method stub
		String[] ls_subanomalias= globales.tll.getLecturaActual().getSubAnomaliaAMostrar().split(";");
		globales.subAnomaliaARepetir="";
		
		for (int i=0; i<globales.anomaliaARepetir.length();i++){
			for(int j=0 ; j<ls_subanomalias.length;j++){
							
							if (ls_subanomalias[j].startsWith(globales.anomaliaARepetir.substring(i, i+1)))
							{
								//Esa subAnomalia 
								if (!globales.subAnomaliaARepetir.equals("")){
									globales.subAnomaliaARepetir+=";";
								}
								globales.subAnomaliaARepetir+=ls_subanomalias[j];
								break;
							}
						}
		}
	}

	@Override
	public boolean avanzarDespuesDeAnomalia(String ls_anomalia, String ls_subAnom, boolean guardar) {
		// TODO Auto-generated method stub
		//String ls_anomalia= globales.tll.getLecturaActual().getAnomaliaAMostrar();
		//if ((ls_anomalia.endsWith("A") && ls_subAnom.startsWith("AA"))||ls_anomalia.endsWith("R")||ls_anomalia.endsWith("Z")||ls_anomalia.endsWith("5")){
		if (esSegundaVisita(ls_anomalia, ls_subAnom)){
			//Grabamos
			if (guardar)
				globales.tll.getLecturaActual().guardar(true, globales.tll.getSiguienteOrdenDeLectura());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean esSegundaVisita(String ls_anomalia, String ls_subAnom) {
		if ((ls_anomalia.endsWith("A")/* && ls_subAnom.endsWith("AA")*/)||ls_anomalia.endsWith("R")||ls_anomalia.endsWith("Z")||ls_anomalia.endsWith("5")){
			return true;
		}
		return false;
	}
	
	
	
//	public String getFiltroDeLecturas(int comoFiltrar){
//		switch(comoFiltrar){
//		case AUSENTES:
//			return " lectura=''  and (anomalia='' or anomalia like '%*' or anomalia like '%A' or anomalia like '%R' or anomalia like '%Z' or anomalia like '%5') " ;
//		case LEIDAS:
//			return " (lectura<>'' or (anomalia<>'' and anomalia not like '%*' and anomalia not like '%A' and anomalia not like '%R' and anomalia not like '%Z' and anomalia not like '%5' )) ";
//		}
//		return "";
//	}

	@Override
	public String getDescripcionDeBuscarMedidor(Lectura lectura,
			int tipoDeBusqueda, String textoBuscado) {
		String ls_preview="";
		
		switch (tipoDeBusqueda) {
		case BuscarMedidorTabsPagerAdapter.MEDIDOR:
			ls_preview = Lectura.marcarTexto(lectura.is_serieMedidor, textoBuscado, false);
			if (!lectura.getColonia().equals(""))
				ls_preview += "<br>" + lectura.getColonia();
			ls_preview += "<br>" +lectura.getDireccion() + " #" +lectura.numeroDeEdificio;
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
		// TODO Auto-generated method stub
		
		if (ls_anomalia.equals("A")){
			if (globales.tll.getLecturaActual().is_ubicacion.trim().equals("A")){
				return "Medidor accesible. Incidencia no permitida por el momento.";
			}
		}
		else if (ls_anomalia.equals("6")){
			if (!globales.tll.getLecturaActual().getAnomaliaAMostrar().contains("*")){
				return "La Anomalia 6 solamente se permite en segunda visita";
			}
		}
		return "";
	}

	@Override
	public String getPrefijoComentario(String ls_anomalia) {
		// TODO Auto-generated method stub
		if (ls_anomalia.equals("C")){
			return "MA";
		}
		else if (ls_anomalia.equals("R4")){
			return "R4";
		}else if (ls_anomalia.equals("V")){
			return "AC";
		}else if (ls_anomalia.equals("ZT")){
			return "ZT";
		}
		else if (ls_anomalia.equals("S4")){
			return "S4";
		}
		else if (ls_anomalia.equals("34")){
			return "34";
		}
		
		return "";
	}

	@Override
	public void repetirAnomalias() {
		// TODO Auto-generated method stub
		if (globales.anomaliaARepetir.equals("C")){
			globales.tll.getLecturaActual().setComentarios(globales.lecturaARepetir.getComentarioAnomalia(globales.anomaliaARepetir));
		}
		else if  (globales.anomaliaARepetir.equals("N")){
			globales.tll.getLecturaActual().setColonia(globales.lecturaARepetir.getColonia());
			globales.tll.getLecturaActual().is_puerta=globales.lecturaARepetir.is_puerta;
			globales.tll.getLecturaActual().is_escalera=globales.lecturaARepetir.is_escalera;
			globales.tll.getLecturaActual().is_piso=globales.lecturaARepetir.is_piso;
			globales.tll.getLecturaActual().numeroDePortal=globales.lecturaARepetir.numeroDePortal;
			globales.tll.getLecturaActual().numeroDeEdificio=globales.lecturaARepetir.numeroDeEdificio;
			globales.tll.getLecturaActual().setDireccion(globales.lecturaARepetir.getDireccion());
			
		}else if  (globales.anomaliaARepetir.equals("T")){
			globales.tll.getLecturaActual().is_aviso=globales.lecturaARepetir.is_aviso;
		}
		else if  (globales.anomaliaARepetir.equals("R")){
			globales.tll.getLecturaActual().setComentarios(globales.lecturaARepetir.getComentarioAnomalia(globales.anomaliaARepetir));
		}
	}
	
	@Override
	public void setConsumo(){
		long ll_consumo=0;
		String anomCapturadas=globales.tll.getLecturaActual().getAnomaliasCapturadas();
		
		
		
		if (globales.is_lectura.equals("")/* ||anomCapturadas.contains("H") */ ){
			globales.tll.getLecturaActual().is_consumo=Main.rellenaString("", " ", globales.tlc.getLongCampo("consumo"), true);
			return ;
		}
			
		
		float baremo_max =   ((float)(100 + globales.baremo)) / 100;
		float baremo_min =   ((float)(100 - globales.baremo)) / 100;
		
		if (baremo_min<0){
			baremo_min=0;
		}
		
		
		
		//Hay que checar que las anomalias capturadas sean las que se dejan como AUSENTES
		
		globales.tll.getLecturaActual().deleteAnomalia("W");
		globales.tll.getLecturaActual().deleteAnomalia("I");
		cambiosAnomaliaAntesDeGuardar(globales.is_lectura);
		
		ll_consumo= getConsumo(globales.is_lectura);
		
		if (anomCapturadas.contains("H") ){
			if (ll_consumo<0 )
				ll_consumo= (long) (Math.pow(10, globales.tll.getLecturaActual().numerodeesferas) +ll_consumo);
			globales.tll.getLecturaActual().is_consumo=String.valueOf(ll_consumo);
			return;
		}
		
		if (ll_consumo<0 ){
			ll_consumo= (long) (Math.pow(10, globales.tll.getLecturaActual().numerodeesferas) +ll_consumo);
			if(ll_consumo<= globales.tll.getLecturaActual().baremo *baremo_max){
				if (!anomCapturadas.contains("4")){
					globales.tll.getLecturaActual().setAnomalia("4");
				}
				globales.tll.getLecturaActual().setAnomalia("W");
			}
			else{
				if (!anomCapturadas.contains("4")){
					globales.tll.getLecturaActual().setAnomalia("4");
				}
				globales.ignorarContadorControlCalidad=true;
				globales.tll.getLecturaActual().setAnomalia("I");
			}
		}
		else if (ll_consumo>=0  && !((globales.tll.getLecturaActual().estadoDelSuministro.equals("1") || globales.tll.getLecturaActual().estadoDelSuministro.equals("2")) && ll_consumo==0)){
			//if(ll_consumo < globales.tll.getLecturaActual().baremo * baremo_min){
			if (this.getLecturaMinima()>Long.valueOf(globales.is_lectura)){
				if (!anomCapturadas.contains("4")){
					globales.tll.getLecturaActual().setAnomalia("4");
				}
			}
			//else if(ll_consumo > globales.tll.getLecturaActual().baremo * baremo_max){
		else if (this.getLecturaMaxima()<Long.valueOf(globales.is_lectura)){
				if (!anomCapturadas.contains("4")){
					globales.tll.getLecturaActual().setAnomalia("4");
				}
			}
		}
		
		if (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("J")){
			
			globales.tll.getLecturaActual().deleteAnomalia("J");
			globales.tll.getLecturaActual().setAnomalia("J");
			
		}
		
			
		
		globales.tll.getLecturaActual().is_consumo=String.valueOf(ll_consumo);
		
		
	}

	@Override
	public long getConsumo(String lectura) {
		// TODO Auto-generated method stub
		return  Long.parseLong(lectura) -globales.tll.getLecturaActual().lecturaAnterior ;
	}
	
	public void setTipoLectura(){
		if  (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("A") && globales.tll.getLecturaActual().getAnomaliaAMostrar().contains("*")){
			//Si las capturadas tiene A y en las anteriores tiene A, hay que tomarla como capturada
			
			String anomalias=globales.tll.getLecturaActual().getAnomaliaAMostrar();
			
			
			anomalias= anomalias.substring(0, anomalias.lastIndexOf("*"));
			
			anomalias= anomalias.substring(anomalias.lastIndexOf("*")+1);
			
			if (anomalias.contains("A")){
				globales.tll.getLecturaActual().is_tipoLectura="4";
				return;
			}
			
			
		}
		
		if (globales.tll.getLecturaActual().getLectura().trim().equals("") && 
				( (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("A") && globales.tll.getLecturaActual().containsSubAnomalia("AA") )
				|| globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("R") 
				|| globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("Z") 
				|| globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("5") ) 
				&& !globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("6")){
			globales.tll.getLecturaActual().is_tipoLectura="";
			return;
		}
		
		super.setTipoLectura();
		
	}

	@Override
	public String validaCamposGenericos(String anomalia, Bundle bu_params) {
		// TODO Auto-generated method stub
		
		
		
		if (anomalia.equals("C")) {
			
			if (bu_params.getString(String.valueOf(MEDIDOR_ANTERIOR)).length()==0 && bu_params.getString(String.valueOf(MEDIDOR_POSTERIOR)).length()==0){
				return "No se han ingresado datos";
			}
			int longitud_disponible=globales.tlc.getLongCampo("comentarios")-(globales.tll.getLecturaActual().getComentarios().length());
			String campoAInsertar="MA:" +bu_params.getString(String.valueOf(MEDIDOR_ANTERIOR)) +",MP:"+bu_params.getString(String.valueOf(MEDIDOR_POSTERIOR));
			
			if(longitud_disponible<campoAInsertar.length()){
				return "Se excede la el límite de caracteres para el campo comentarios";
			}
			
			
		} else if (anomalia.equals("2")) {
			if (bu_params.getString("input").equals("0")){
				return "El número de esferas no puede ser 0";
			}else if (Integer.parseInt(bu_params.getString("input"))>7){
				return "El número de esferas no puede ser mayor a 7";
			}
		}
		return "";
	}
	
	

	@Override
	public MensajeEspecial regresaDeAnomalias(String ls_anomalia) {
		// TODO Auto-generated method stub
		if (ls_anomalia.equals("6")){
			return mj_anomalia_seis;
		}else if (ls_anomalia.equals("B")){
			globales.tll.getLecturaActual().setAnomalia("2");
		}
		return null;
	}

	@Override
	public boolean puedoRepetirAnomalia() {
		// TODO Auto-generated method stub
		if (globales.anomaliaARepetir.contains("A") && globales.tll.getLecturaActual().is_ubicacion.trim().equals("A")){
			return false;
		}else if (globales.anomaliaARepetir.length()==1){
			
			if (!globales.tdlg.esAnomaliaCompatible(globales.anomaliaARepetir, globales.tll.getLecturaActual().getAnomaliaAMostrar())){
				
				return false;
			}
		}
		else if (globales.anomaliaARepetir.length()>1){
			for (int i=0; i<globales.anomaliaARepetir.length();i++){
				if (!globales.tdlg.esAnomaliaCompatible(globales.anomaliaARepetir.substring(i, i+1), globales.tll.getLecturaActual().getAnomaliaAMostrar())){
					return false;
				}
			}
			
		}
		return true;
	}

	@Override
	public String remplazaValorDeArchivo(int tipo, String ls_anomalia,  String valor){
		switch(tipo){
		case FOTOS:
//			if (ls_anomalia.startsWith("A")){
//				return "0";
//			}
			break;
		case MENSAJE:
			if (ls_anomalia.startsWith("1")){
				return "0";
			}else if (ls_anomalia.startsWith("3") && !ls_anomalia.equals("34")){
				return "0";
			}
			else if (ls_anomalia.startsWith("S")/*&& !ls_anomalia.equals("S4")*/){
				return "0";
			}
			else if ( ls_anomalia.equals("R4")){
				return "1";
			}else if (ls_anomalia.startsWith("Z") && !ls_anomalia.equals("ZT")){
				return "0";
			}
			break;
		}
		return valor;
	}
	
	@Override
	public void cambiosAnomalia(String anomalia){
		//Vamos a hacer cambios al regresar de la pantalla de anomalias, asteriscos and stuff
		//Estamos basandonos en un cuadro
		
		if ((globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("A") && (anomalia.equals("R") ||  anomalia.equals("L") || anomalia.equals("S")))
				|| (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("R") && (anomalia.equals("L") || anomalia.equals("S")))
				|| (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("Z") && (anomalia.equals("R") || anomalia.equals("L") || anomalia.equals("S") || anomalia.equals("D")|| anomalia.equals("A")))
				||(globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("5") && (anomalia.equals("R") || anomalia.equals("L") || anomalia.equals("S") || anomalia.equals("D")|| anomalia.equals("A")))){
			globales.tll.getLecturaActual().setAnomalia("*");
			if (!globales.tll.getLecturaActual().getSubAnomaliaAMostrar().equals("")){
				globales.tll.getLecturaActual().setSubAnomalia("*");
			}
		}
		
		
	}
	
	public void cambiosAlBorrarAnomalia(String anomaliaBorrada){
		if (anomaliaBorrada.equals("R") || anomaliaBorrada.equals("L") || anomaliaBorrada.equals("S") ||anomaliaBorrada.equals("Z") || anomaliaBorrada.equals("D") || anomaliaBorrada.equals("A")){
			if (globales.tll.getLecturaActual().getAnomaliaAMostrar().endsWith("*")){
				String ls_anomalias=globales.tll.getLecturaActual().getAnomaliaAMostrar().substring(0, globales.tll.getLecturaActual().getAnomaliaAMostrar().length()-1);
				String ls_anomaliasFinal=ls_anomalias;
				
				if (ls_anomalias.lastIndexOf("*")>=0){
					ls_anomalias=ls_anomalias.substring(ls_anomalias.lastIndexOf("*")+1);
				}
				
				if ((ls_anomalias.contains("A") && (anomaliaBorrada.equals("R") ||  anomaliaBorrada.equals("L") || anomaliaBorrada.equals("S")))
						|| (ls_anomalias.contains("R") && (anomaliaBorrada.equals("L") || anomaliaBorrada.equals("S")))
						|| (ls_anomalias.contains("Z") && (anomaliaBorrada.equals("R") || anomaliaBorrada.equals("L") || anomaliaBorrada.equals("S") || anomaliaBorrada.equals("D")|| anomaliaBorrada.equals("A")))
						||(ls_anomalias.contains("5") && (anomaliaBorrada.equals("R") || anomaliaBorrada.equals("L") || anomaliaBorrada.equals("S") || anomaliaBorrada.equals("D")|| anomaliaBorrada.equals("A")))){
					globales.tll.getLecturaActual().reiniciaAnomalias(ls_anomaliasFinal);
					if (globales.tll.getLecturaActual().is_subAnomaliaInterna.endsWith("*")){
						globales.tll.getLecturaActual().reiniciaSubAnomalias(globales.tll.getLecturaActual().is_subAnomaliaInterna.substring(0, globales.tll.getLecturaActual().is_subAnomaliaInterna.length()-1));
					}
				}
				
			}
		}
//		else if (anomaliaBorrada.equals("6")){
//			if (globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("G"))
//				globales.tll.getLecturaActual().deleteAnomalia("G");
//			else if(globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("R"))
//				globales.tll.getLecturaActual().deleteAnomalia("R");
//			else if(globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("Z"))
//				globales.tll.getLecturaActual().deleteAnomalia("Z");
//			else if(globales.tll.getLecturaActual().getAnomaliasCapturadas().contains("5"))
//				globales.tll.getLecturaActual().deleteAnomalia("5");
//		}
	}
	
	public void inicializaTablaDeCalidades(){
		ht_calidades= new Hashtable<String, Integer>();
		ht_calidades.put("AAA", 20);
		ht_calidades.put("AAU", 20);
		ht_calidades.put("AAX", 20);
		ht_calidades.put("AAP", 20);
		ht_calidades.put("B", 60);
		ht_calidades.put("FFS", 20);
		ht_calidades.put("FFC", 60);
		ht_calidades.put("G", 60);
		ht_calidades.put("H", 60);
		ht_calidades.put("L", 20);
		ht_calidades.put("N", 20);
		ht_calidades.put("RR1", 20);
		ht_calidades.put("RR2", 20);
		ht_calidades.put("RR3", 20);
		ht_calidades.put("RR4", 20);
		ht_calidades.put("SS1", 20);
		ht_calidades.put("SS2", 20);
		ht_calidades.put("SS3", 20);
		ht_calidades.put("SS4", 20);
		ht_calidades.put("ZZG", 20);
		ht_calidades.put("ZZR", 20);
		ht_calidades.put("ZZA", 20);
		ht_calidades.put("ZZL", 20);
		ht_calidades.put("ZZB", 20);
		ht_calidades.put("ZZP", 20);
		ht_calidades.put("ZZF", 20);
		ht_calidades.put("ZZM", 20);
		ht_calidades.put("ZZC", 20);
		ht_calidades.put("ZZT", 20);
		ht_calidades.put("11A", 20);
		ht_calidades.put("11B", 20);
		ht_calidades.put("11V", 20);
		ht_calidades.put("11R", 20);
		ht_calidades.put("2", 20);
		ht_calidades.put("332", 20);
		ht_calidades.put("44J", 20);
		ht_calidades.put("44O", 20);
		ht_calidades.put("44P", 20);
		ht_calidades.put("44Q", 60);
		ht_calidades.put("44U", 20);
		ht_calidades.put("44X", 20);
		

	}
	
	@Override
	public int cambiaCalidadSegunTabla(String Anomalia, String subAnomalia){
		String comodin =Anomalia.trim()+subAnomalia.trim();
		Integer calidad=ht_calidades.get(comodin);
		
		if (calidad==null){
			//calidad= 20;
			calidad= globales.calidadDeLaFoto;
		}
		
		return calidad.intValue();
	}
	
	public boolean continuarConLaFoto() {
		// TODO Auto-generated method stub
		
		if (globales.tll.getLecturaActual().estadoDelSuministro.equals("2")){
			return false;
		}
		return true;
	}

}
