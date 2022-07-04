package enruta.sistole_gen;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import androidx.appcompat.app.AppCompatActivity;

public class CPL extends AppCompatActivity {
	
	public final static int NINGUNO=0;
	public final static int ADMINISTRADOR=1;
	public final static int LECTURISTA=2;
	public final static int SUPERUSUARIO=3;
	
	public final static int ENTRADA=1;
	public final static int LOGIN=2;
	public final static int MAIN=3;
	
	public final static int CAMBIAR_USUARIO=1;
	
	public int ii_perfil=NINGUNO;
	public int ii_pantallaActual=NINGUNO;
	
	
	boolean esSuperUsuario=false;

	String is_nombre_Lect="";
	
	TextView tv_msj_login, tv_usuario, tv_contrasena;
	EditText et_usuario, et_contrasena ;
	
	DBHelper dbHelper;
	SQLiteDatabase db;
	
	
	String superUsuarioPass="9776";
	
	String usuario="";
	
	Globales globales;
	ImageView iv_logo;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cpl);
		ii_pantallaActual=ENTRADA;
		
		 globales = ((Globales)getApplicationContext());
		 
		 
		
		 iv_logo= (ImageView) findViewById(R.id.iv_logo);
		 TextView tv_version= (TextView) findViewById(R.id.tv_version_lbl);
			
			try {
				tv_version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode+ ", "+ getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			estableceVariablesDePaises();
			
	}
	
	


	
	/**
	  * Aqui se van a cargar las variables que correspondan a cada pais
	  */
	private void estableceVariablesDePaises() {
		// TODO Auto-generated method stub
		
		 switch(globales.ii_pais){
		 case Globales.ARGENTINA:
			 globales.tdlg= new TomaDeLecturasArgentina(this);
			 break;
		 case Globales.COLOMBIA:
			 globales.tdlg= new TomaDeLecturasColombia(this);
			 break;
			 
		 case Globales.ELECTRICARIBE:
			 globales.tdlg= new TomaDeLecturasElectricaribe(this);
			 break;
		 case Globales.PANAMA:
			 globales.tdlg= new TomaDeLecturasPanama(this);
			 break;
		 case Globales.COMAPA_TAMPICO:
			 globales.tdlg= new TomaDeLecturasComapaTampico(this);
			 break;
		 }
		 
		 iv_logo.setImageResource(globales.logo);
		 
	}






	public void entrarAdministrador(View v){
		ii_perfil=ADMINISTRADOR;
		
		setContentView(R.layout.p_login);
		ii_pantallaActual=LOGIN;
		getObjetosLogin();
		et_contrasena.setFilters( new InputFilter[] { new InputFilter.LengthFilter(globales.longCampoContrasena) } );
		tv_msj_login.setText(R.string.str_login_msj_admon);
		tv_usuario.setVisibility(View.GONE);
		et_usuario.setVisibility(View.GONE);
		globales.secuenciaSuperUsuario="A";
		et_contrasena.requestFocus();
		mostrarTeclado();
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
	}
	
	public void entrarLecturista(View v){
		ii_perfil=LECTURISTA;
		ii_pantallaActual=LOGIN;
		setContentView(R.layout.p_login);
		getObjetosLogin();
		globales.secuenciaSuperUsuario+="C";
		
		
		et_usuario.setFilters( new InputFilter[] { new InputFilter.LengthFilter(globales.longCampoUsuario) } );
		et_contrasena.setFilters( new InputFilter[] { new InputFilter.LengthFilter(globales.longCampoContrasena) } );
		//Hay que adaptar según el tipo de validacion
		switch(globales.tipoDeValidacion){
		
		case Globales.AMBAS:
			
			String ls_usuarioGuardado=globales.tdlg.getUsuarioGuardado();
			
			if (ls_usuarioGuardado.trim().length()==0 ){
				et_usuario.requestFocus();
				
			}
			else{
				et_usuario.setText(ls_usuarioGuardado);
				et_contrasena.requestFocus();
			}
			
			
			
			break;
		
		case Globales.USUARIO:
			et_usuario.requestFocus();
			
			
			
			et_contrasena.setVisibility(View.GONE);
			tv_contrasena.setVisibility(View.GONE);
			
			tv_usuario.setVisibility(View.VISIBLE);
			et_usuario.setVisibility(View.VISIBLE);
			
			break;
			
		case Globales.CONTRASEÑA:
		case Globales.SIN_VALIDACION:
			tv_usuario.setVisibility(View.VISIBLE);
			et_usuario.setVisibility(View.GONE);
			
			et_contrasena.setVisibility(View.VISIBLE);
			tv_contrasena.setVisibility(View.GONE);
			
			et_contrasena.requestFocus();
			break;
		}
		
//		if(globales.tipoDeValidacion==Globales.CONTRASEÑA)
//			tv_msj_login.setText(R.string.str_login_msj_lecturista_contrasena);
//		else
			tv_msj_login.setText(globales.mensajeContraseñaLecturista);
		
		mostrarTeclado();
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		et_usuario.setInputType(globales.tipoDeEntradaUsuarioLogin);
	}
	
	
	public void entrar(View v){
		boolean validar=false;
		switch(ii_perfil){
			case ADMINISTRADOR:
				esconderTeclado();
				validar=validarAdministrador();
				break;
			case LECTURISTA:
				esconderTeclado();
				validar=validarLecturista();
				
				break;
		}
		
		if (validar){
			//Aqui abrimos la actividad
			
			//Hay que empezar a restingir las cosas que cada uno puede hacer
			
			Intent intent =new Intent (this, Main.class);
			intent.putExtra("rol", ii_perfil);
			intent.putExtra("esSuperUsuario", esSuperUsuario);
			intent.putExtra("nombre", is_nombre_Lect);
			
		    
			startActivityForResult(intent, MAIN);
		}
		else{
			
			switch(ii_perfil){
			case ADMINISTRADOR:
				Toast.makeText(this, getString(R.string.msj_cpl_verifique_contrasena) , Toast.LENGTH_LONG).show();
				globales.secuenciaSuperUsuario+="B";
				break;
			case LECTURISTA:
				if(globales.tipoDeValidacion==Globales.CONTRASEÑA)
					Toast.makeText(this, getString(R.string.msj_cpl_verifique_contrasena) , Toast.LENGTH_LONG).show();
				else if (globales.tipoDeValidacion==Globales.USUARIO)
					Toast.makeText(this,getString(R.string.msj_cpl_verifique_usuario), Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this,getString(R.string.msj_cpl_verifique_usuario_contrasena) , Toast.LENGTH_LONG).show();
				break;
		}
			et_usuario.setText("");
			et_contrasena.setText("");
			
		}
		
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bundle bu_params = null;
		if (data != null) {
			bu_params = data.getExtras();
		}
		switch (requestCode) {
			case MAIN:
				if (resultCode == Activity.RESULT_CANCELED) {
					finish(); //Cancelo con el back
				} else if (resultCode == Activity.RESULT_OK) {
					//Cuando cambia de usuario...
					if (bu_params.getInt("opcion") == CAMBIAR_USUARIO) {
						cambiarUsuario();
					}
				}


		}
	}
	
	
	public boolean validarLecturista(){
		boolean esValido=false;
		
		esSuperUsuario=(et_contrasena.getText().toString().equals(superUsuarioPass)||et_usuario.getText().toString().equals(superUsuarioPass)) && globales.secuenciaSuperUsuario.equals(Globales.SECUENCIA_CORRECTA_SUPER);
		
		//Hay que buscar que la combinacion usuario y contraseña sean correctos
		if (esSuperUsuario){
			esValido=true;
			is_nombre_Lect="Super Usuario";
			globales.setUsuario("9776");
		}
		else{
			openDatabase();
			Cursor c;
			
			String ls_contraseña=et_contrasena.getText().toString().trim();
					
			if (globales.contraseñaUsuarioEncriptada){
				byte[] medidor= Main.rellenaString(ls_contraseña, " ", globales.longCampoContrasena, false).getBytes();
				
				globales.tdlg.EncriptarDesencriptarConParametros(medidor, 0, globales.longCampoContrasena)	;
					
				ls_contraseña= new String (medidor);
			}
			
			switch(globales.tipoDeValidacion){
			case Globales.CONTRASEÑA:
				c= db.rawQuery("Select * from usuarios where trim (contrasena)='" +et_contrasena.getText().toString().trim()+"'" , null) ;
				break;
			case Globales.USUARIO:
				c= db.rawQuery("Select * from usuarios where lower(trim(usuario))='" +et_usuario.getText().toString().trim().toLowerCase() +"' " , null) ;
				break;
			case Globales.SIN_VALIDACION:
				globales.setUsuario(et_contrasena.getText().toString().trim());
				closeDatabase();
				return true;
			default:
				if (globales.validacionCon123){
					c= db.rawQuery("Select * from usuarios where trim(usuario)='" +et_usuario.getText().toString().trim() +"' "
							 , null) ;
				}else{
					c= db.rawQuery("Select * from usuarios where trim(usuario)='" +et_usuario.getText().toString().trim() +"' "+
							" and trim (contrasena)='" +ls_contraseña+"'" , null) ;
				}
				
				break;
			}
			
			
			
			if (c.getCount()>0){
				esValido= true;
				c.moveToFirst();
				if (globales.validacionCon123){
					esValido=et_contrasena.getText().toString().trim().equals("123");
				}
				
				if (globales.tipoDeValidacion==Globales.CONTRASEÑA){
					globales.setUsuario(et_contrasena.getText().toString().trim());
				}
				else{
					globales.setUsuario(et_usuario.getText().toString().trim());
				}
				globales.controlCalidadFotos=c.getInt(c.getColumnIndex("fotosControlCalidad"));
				globales.baremo=Lectura.toInteger(c.getString(c.getColumnIndex("baremo")));
				is_nombre_Lect=c.getString(c.getColumnIndex("nombre"));
				
			}
				
			
			c.close();
//			c= db.rawQuery("Select * from usuarios ", null) ;
//			c.moveToFirst();
//			String usuario= c.getString(0);
//			String contraseña=c.getString(1);
//			
//			c.moveToNext();
//			usuario= c.getString(0);
//			contraseña=c.getString(1);

			closeDatabase();
		}

		
		return esValido;
	}
	
    private void openDatabase(){
    	dbHelper= new DBHelper(this);
		
        db = dbHelper.getReadableDatabase();
    }
	
	 private void closeDatabase(){
	    	db.close();
	        dbHelper.close();
	        
	    }
	
	public boolean validarAdministrador(){
		openDatabase();
		//Buscamos si existe la palabra administrador en los ususatios
		Cursor c;
		c= db.rawQuery("Select * from usuarios where rol in ('2', '3') " , null) ;
		
		
		
		if (c.getCount()>0 ){
			//Existe un administrador, usaremos su contraseña para entrar al sistema
			c.close();
			c= db.rawQuery("Select * from usuarios where rol in ('2', '3') and trim (contrasena)='" +et_contrasena.getText().toString().trim()+"'" , null) ;
			if (c.getCount()>0)
			{
				c.close();
				esSuperUsuario=(et_contrasena.getText().toString().equals(superUsuarioPass));
				return true;
			}
			else if ( !globales.fuerzaEntrarComoSuperUsuarioAdmon){
				
				c.close();
				return false;
			}
		}
		c.close();
		closeDatabase();
		esSuperUsuario=(et_contrasena.getText().toString().equals(superUsuarioPass));
		globales.esSuperUsuario=esSuperUsuario;
//		return true; //Entra con todos
		return this.et_contrasena.getText().toString().equals(globales.admonPass) || this.et_contrasena.getText().toString().equals(superUsuarioPass);
	}
	
	
	public void cambiarUsuario(){
		setContentView(R.layout.cpl);
		esSuperUsuario=false;
		ii_pantallaActual=ENTRADA;
		ii_perfil=NINGUNO;
		globales.setUsuario("");
		TextView tv_version= (TextView) findViewById(R.id.tv_version_lbl);
		
		try {
			tv_version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode+ ", "+ getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		iv_logo= (ImageView) findViewById(R.id.iv_logo);
		iv_logo.setImageResource(globales.logo);
		globales.anomaliaARepetir="";
		globales.subAnomaliaARepetir="";
		
		globales.tdlg.procesosAlEntrar();
	}
	
	public void getObjetosLogin(){
		tv_msj_login= (TextView) findViewById(R.id.tv_msj_login);
		et_usuario= (EditText) findViewById(R.id.et_usuario);
		et_contrasena= (EditText) findViewById(R.id.et_contrasena);
		tv_usuario= (TextView) findViewById(R.id.tv_usuario);
		tv_contrasena=(TextView) findViewById(R.id.tv_contrasena);
		
		OnEditorActionListener oeal=new OnEditorActionListener() {

			

			@Override
			public boolean onEditorAction(TextView arg0, int arg1,
					KeyEvent arg2) {
				// TODO Auto-generated method stub
				entrar(arg0);
				return false;
			}
	       };
	       
		if (globales.tipoDeValidacion== Globales.USUARIO){
			et_usuario.setOnEditorActionListener(oeal);
			et_contrasena.setOnEditorActionListener(oeal);
		}
		else{
			et_contrasena.setOnEditorActionListener(oeal);
		}
		
//et_contrasena.setOnEditorActionListener(new OnEditorActionListener() {
//
//			
//
//			@Override
//			public boolean onEditorAction(TextView arg0, int arg1,
//					KeyEvent arg2) {
//				// TODO Auto-generated method stub
//				entrar(arg0);
//				return false;
//			}
//	       });
	}
	
	public void salir(){
		finish();
	}
	
	public void onBackPressed() {
		switch (ii_pantallaActual){
		case ENTRADA:
			salir();
			break;
		case LOGIN:
			cambiarUsuario();
			break;
		
		}
		
	}
	
		
	
	
	public void esconderTeclado(){
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_usuario.getWindowToken(), 0);
	}
	
	public void mostrarTeclado(){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		  imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}

}
