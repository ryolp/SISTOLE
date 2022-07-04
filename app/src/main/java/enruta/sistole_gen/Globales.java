package enruta.sistole_gen;

import java.util.Vector;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.text.InputType;

public class Globales extends Application {
	final static int NICARAGUA=0;
	final static int COLOMBIA=1;
	final static int ELECTRICARIBE=2;
	final static int ARGENTINA=3;
	final static int BRASIL=4;
	final static int PANAMA=5;
	final static int COMAPA_TAMPICO=6;
	
	final static int USUARIO=0;
	final static int CONTRASEÑA=1;
	final static int AMBAS=2;
	final static int SIN_VALIDACION=3;
	
	final static int BANDERAS_CONF_COLOMBIA=1;
	final static int BANDERAS_INTENTOS_ECA=2;
	final static int BANDERAS_CONF_PANAMA=3;
	
	ThreadTransmitirWifi ttw_timer_a_apagar;
	
	/**
	 * No se genera cambio en las lecturas que se no se han leido
	 */
	final static int NINGUNO=0;
	/**
	 * Se realiza un procedimiento para cerrar las lecturas que no se han tomado
	 */
	final static int FORZADO=1;
	/**
	 * No puede entregar la ruta si alguna de las lecturas no se ha tomado
	 */
	final static int RUTA_COMPLETA=2;
	
	/**
	 * Transmite y no pregunta nada
	 */
	final static int SOLO_TRANSMITIR_PENDIENTES=3;
	
	final static String SECUENCIA_CORRECTA_SUPER="ABC";
	String secuenciaSuperUsuario="";
	
	boolean mostrarCodigoUsuario=false;
	
	boolean transmitirTodo=false;
	
	/**
	 * Cambia el pais actual
	 */
	public int ii_pais=this.COMAPA_TAMPICO;
	
	int modoDeCierreDeLecturas=FORZADO;
	
	int flash /* ah-ah! he is a miracle!*/= CamaraActivity.AUTO;
	
	//public boolean entrarComoSuperUsuario=true;
	
	public boolean reemplazarToastPorMensaje=false;
	
	//En algunos paises esta terminacion debe cambiar
	public String textoEsferas="Esferas";
	
	int tipoDeEntradaUsuarioLogin=InputType.TYPE_CLASS_NUMBER;
	
	public String letraPais="A";
	public int tipoDeValidacion=AMBAS;
	public int longCampoUsuario=10;
	public int longCampoContrasena=10;
	boolean mostrarNoRegistrados=true;
	int calidadDeLaFoto=50;
	/**Calidad que se remplaza segun otros parametros**/
	int calidadOverride=calidadDeLaFoto;
	boolean ignorarGeneracionCalidadOverride=false;
	/**Por cada cuantas lecturas malas toma una foto**/
	int controlCalidadFotos=1;
	boolean ignorarContadorControlCalidad=false;
	boolean ignorarfoto=false;
	
	/** Elementos que se pueden esconder de la pantalla de configuracion **/
	boolean mostrarMacBt=true;
	boolean mostrarMacImpresora=true;
	boolean mostrarServidorGPRS=true;
	boolean mostrarServidorWIFI=true;
	boolean mostrarFactorBaremo=true;
	boolean mostrarTamañoFoto=true;
	boolean mostrarMetodoDeTransmision=true;
	boolean mostrarIngresoFacilMAC=false;
	boolean mostrarIngresoFacilMAC_BT=true;
	boolean mostrarImpresion=false;
	boolean mostrarCalidadFoto=true;
	boolean mostrarUnicom=false;
	boolean mostrarRuta=false;
	boolean mostrarItinerario=false;
	boolean mostrarLote=true;
	boolean mostrarCPL=true;
	boolean mostrarSonido=true;
	
	boolean mostrarBorrarRuta=false;
	
	
	/**Elementos del menu que deben ser escondidos segun cada version**/
	boolean mostrarGrabarEnSD=false;
	
	boolean fuerzaEntrarComoSuperUsuarioAdmon=false;
	
	/** Informacion por default de la pantalla de configuracion **/
	String defaultLote="ACTIVOS";
	String defaultCPL="";
	String defaultTransmision="0";
	String defaultTamañoFoto="0";
	String defaultUnicom="";
	String defaultRuta="";
	String defaultItinerario="";
	String defaultServidorGPRS="http://www.espinosacarlos.com";
	String defaultServidorWIFI="http://www.espinosacarlos.com";
	String defaultServidorDeActualizacion="";
	String defaultRutaDescarga="C:\\Apps\\SGL\\Lectura";
	
	/** Default de los tamaños de fuente**/
	Double porcentaje_main=1.0,
			porcentaje_main2=1.0,
			porcentaje_hexateclado=1.0,
			porcentaje_teclado=1.0,
			porcentaje_lectura=1.0,
			porcentaje_info=1.0;
	
	/**
	 * Este baremo funciona para remplazar el baremo actual
	 */
	int baremo=75;
	
	int mensajeContraseñaLecturista=R.string.str_login_msj_lecturista;
	
	boolean sonidos=true;
	
	private String usuario=""; //Usuario actual

	/**Variables de toma de lecturas**/
	TodasLasLecturas tll; //Variable en donde estan todas las lecturas
	long il_ultimoSegReg=0; //Ultimo medidor guardado
	String idMedidorUltimaLectura="";
	String is_lectura, is_presion, is_caseta, is_terminacion;
	boolean bModificar=false, bcerrar=true;
	boolean moverPosicion=false;
	boolean bEstabaModificando=false;
	boolean capsModoCorreccion=false;
	boolean permiteDarVuelta=false;
	boolean sonLecturasConsecutivas=false;
	boolean estoyTomandoFotosConsecutivas=false;
	long il_lect_act=0, il_total, il_lect_max, il_lect_min;
	String is_nombre_Lect="";
	int ii_orden=TomaDeLecturasPadre.ASC;
	Vector <Lectura> lecturasConFotosPendientes=null;
	int ii_foto_cons_act=0;
	boolean estoyCapturando = false;
	boolean inputMandaCierre=false;
	boolean requiereLectura = false;
	boolean mostrarMensajeLecturaRepetida=false;
	//int il_lectDistinta = 0;
	
	
	/** 
	 * Indica que si se borra una lectura en modo de correccion, pueda mostrar el boton de grabar
	 */
	boolean dejarComoAusentes=false;
	String mensaje="";
	
	boolean modoCaptura = false;
	
	int ultimaPestanaAnomaliasUsada=PantallaAnomaliasTabsPagerAdapter.TODAS;
	

	
	boolean gpsEncendido=false;
	boolean requiereGPS=false;
	/**
	 * Indica si el  programa esta capacitado para tomar puntos gps
	 */
	boolean GPS=false;
	
	boolean fotoForzada = false; // Siempre tomará foto despues de una lectura
	boolean validar = true; // No se validará la lectura
	
	
	Location location;
	/**Fin de Variables de toma de lecturas**/
	
	TodosLosCampos tlc /*=new TodosLosCampos()*/;
	TomaDeLecturasGenerica tdlg;
	
	//TomaDeLecturasGenerica tdlGenerica= new TomaDeLecturasGenerica(this);	
	
	
	/** Estas son las variables de configuracion**/
	final static int USO=0;
	final static int ALFABETICAMENTE=1;
	
	static final int TRANSMION_NORMAL = 0;
	static final int TRANSMION_ELECTIRCARIBE = 1;
	int tipoDeRecepcion=TRANSMION_NORMAL;
	
	boolean filtrarAnomaliasConLectura=false;
	int anomaliasPorMostrar=12;
	int orden=USO;
	
	int logo=R.drawable.logo;
	
	boolean multiplesAnomalias=false;
	boolean convertirAnomalias=false;
	boolean respetarLongitudDeEntradaAnomalias=true;
	
	int longitudRealCodigoAnomalia=3;
	int longitudCodigoAnomalia=3;
	int longitudCodigoSubAnomalia=3;
	
	String rellenoAnomalia=".";
	boolean rellenarAnomalia=false;
	
	boolean repiteAnomalias=false;
	boolean remplazarDireccionPorCalles=false;
	String anomaliaARepetir="";
	String subAnomaliaARepetir="";
	Lectura lecturaARepetir=null;
	
	boolean mostrarCuadriculatdl=false;
	boolean mostrarRowIdSecuencia=false;
	
	int mensajeDeConfirmar=R.string.msj_lecturas_verifique;
	
	boolean tomaMultiplesFotos=true;
	
	/** Fin de variables de configuracion**/
	
	/** Aqui definimos los sonidos**/
	
	int sonidoCorrecta=Sonidos.BEEP;
	int sonidoIncorrecta=Sonidos.URGENT;
	int sonidoConfirmada=Sonidos.NINGUNO;
	
	/**
	 * Controla si el captura se comporta como en nokia
	 */
	boolean legacyCaptura=false;
	
	/** Configuraciones visuales de la pantalla de toma de lecturas**/
	boolean ver_celda0=true;
	boolean ver_celda1=true;
	boolean ver_celda2=true;
	boolean ver_celda3=true;
	boolean ver_celda4=true;
	
	/**
	 * Guarda la anomalia de instalacion y Medidor en diferentes variables
	 */
	boolean DiferirEntreAnomInstYMed=false;
	boolean ignorarTomaDeFoto=false;
	
	int minIntentos=0;
	int maxIntentos=0;
	
	int contadorIntentos=0;
	
	String ultimoBloqueCapturado="0500";
	String ultimoMedidorCapturado="";
	
	public String lote;
	
	boolean rellenarVaciaLectura=true;
	
	boolean puedoCancelarFotos=false;
	boolean mostrarOtraFoto=true;
	
	boolean esSuperUsuario = false;
	
	boolean bloquearBotonesLecturaObligatoria=false;
	
	//Tabs que aparecen en busqueda, no puedo modificar el orden o su lugar, asi que cuidado
	 int[] tabs ={ R.string.lbl_medidor,R.string.lbl_direccion, R.string.lbl_numero};
	 
	 int modoDeBanderasAGrabar=NINGUNO;
	 
	 /**
	  * Desencripta segun el metodo especificado
	  */
	public boolean desencriptarEntrada=false;
	
	boolean enviarLongitudDeCadenaAEnviar=true;

	boolean contraseñaUsuarioEncriptada=false;
	
	boolean preguntaSiTieneMedidor=false;
	
	boolean mostrarPausaActiva=false;
	boolean bloquearBorrarSiIntento=false;
	
	boolean validacionCon123=false;
	boolean dirigirAlUnicoResultado=false;
	boolean switchBuscarPorMover=false;
	
	/**
	 * Tiempo que pasara entre una pausa activa y otra en milisegundos
	 */
	double tiempoPausaActiva=1800000;
	double fechaEnMilisegundos=0;
	//El fix de GPS
	String fix="4";
	protected int Satellites;
	boolean ordenarAnomalias=false;
	public boolean elegirQueDescargar=false;
	public String textoNoRegistrados="No Registrados";
	int iconoNoRegistrados=R.drawable.ic_action_nuevo;

	boolean guardarSospechosa=true;
	boolean quitarPrimerCaracterNombreFoto=true;
	boolean mostrarBuscarDespuesDeCapturar=false;
	
	boolean habilitarPuntoDecimal=false;
	
	boolean tomarFotoCambioMedidor=false;
	
	boolean mostrarCambioMedidor=false;
	/**
	 * Para panama, el AN
	 */
	public String prefijoAnomalia="";
	
	
	String admonPass="9776";
	String separadorSalida="";
	
	String filtroGlobalBusqueda="";
	public String agrupacionGlobalBusqueda="";
	
	boolean preguntarSiSegundaVezDescargada=false;
	
	boolean envioAutomatico=false;
	
	boolean enviarSoloLoMarcado=false;
	
	/**
	 * De estar encendida, ignora los cambios hechos en la pantalla de configuracion y 
	 * sobreescribe con los default
	 */
	boolean sobreEscribirServidorConDefault=false;
	
	/**
	 * Se bloquea la CPL cuando no sea super usuario
	 */
	boolean bloquearCPLNoSuper=false;
	
	public String getUsuario(){
		
	    return usuario;
	  }
	  public void setUsuario(String s){
		  usuario = s;
		  
	  }
	  
	  
	  public String traducirAnomalia(){
		  String anomaliaTraducida;
		  if (convertirAnomalias)
				anomaliaTraducida="conv";
			else
				anomaliaTraducida="anomalia";
		  
		  return anomaliaTraducida;
	  }
	  
	  
	
	 
	  
}
