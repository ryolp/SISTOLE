package enruta.sistole_gen;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TodosLosCampos {
	//Vector <Campo> campos = new Vector<Campo>();
	
	String is_tabla="Ruta";

// Si esta vacio, en descargar la informacion al servidor se regresa en el mismo orden
// Si no, podemos especificar los campos que queremos usar en la salida
//	String is_CamposDeSalida = "secuencia || direccion || cliente || lectura || fecha || lectura";
	String is_camposDeSalida = "";
	
	Hashtable<String, Campo> campos= new Hashtable();
	ContentValues cv_params= new ContentValues();
	
	Vector <String> camposEnOrden= new Vector<String>();
	
	
	TodosLosCampos(){
		
	}
	
	TodosLosCampos(ContentValues cv_params){
		this.cv_params= cv_params;
	}
	
	public void add(Campo campo){
		campos.put(campo.getNombre().toUpperCase(), campo);
		if (campo.esDeEntrada)
			camposEnOrden.add(campo.getNombre().toUpperCase());
	}
	
	public void byteToBD(SQLiteDatabase db, byte[] bytes, int secuenciaReal){
		ContentValues cv_params = new ContentValues(this.cv_params);
		Enumeration <Campo> e;
		e=campos.elements();
		
		while (e.hasMoreElements())	{
		//for (int i=0; i<campos.size() ;i++){
			Campo campo=e.nextElement();
			
			if(!campo.esDeEntrada){
				continue;
			}
			cv_params.put(campo.getNombre(), campo.recortarByte(bytes));
		}
		cv_params.put("secuenciaReal", secuenciaReal );
		db.insert(is_tabla, null, cv_params);
	}
	
	public void byteToBD(SQLiteDatabase db, String bytes, int secuenciaReal){
		ContentValues cv_params = new ContentValues(this.cv_params);
		Enumeration <Campo> e;
		e=campos.elements();
		while (e.hasMoreElements())	{
		//for (int i=0; i<campos.size() ;i++){
			
			Campo campo=e.nextElement();
			if(!campo.esDeEntrada){
				continue;
			}
			cv_params.put(campo.getNombre(), campo.recortarByte(bytes));
		}
		cv_params.put("secuenciaReal", secuenciaReal );
		db.insert(is_tabla, null, cv_params);
	}
	
	public String getCampo(String nombre){
		return campos.get(nombre).getNombre();
	}
	
	
	public Campo getCampoObjeto(String nombre){
		return campos.get(nombre.toUpperCase());
	}
	
	public int getLongCampo(String nombre){
		return campos.get(nombre.toUpperCase()).getLong();
	}
	
	public int getPosCampo(String nombre){
		return campos.get(nombre.toUpperCase()).getPos();
	}
	
	public String getRellenoCampo(String nombre){
		return campos.get(nombre.toUpperCase()).getRelleno();
	}
	
	public void getListaDeCamposFormateado(){
		getListaDeCamposFormateado(new String[0] );
	}
	
	
	public void getListaDeCamposFormateado(String[] lsa_campos){
		getListaDeCamposFormateado(lsa_campos, "");
	}
	public void getListaDeCamposFormateado(String[] lsa_campos, String separador){
		
		is_camposDeSalida="";
		String[] arreglo;
		
		
		
		if (lsa_campos.length>0){
			arreglo=lsa_campos;
		}else{
			//Debemos enviarlos en el orden
//			for (String ls_campo:camposEnOrden){
//				if (!is_camposDeSalida.equals(""))
//					is_camposDeSalida +="||";
//				
//				is_camposDeSalida=campos.get(ls_campo).campoSQLFormateado();
//			}
		
			arreglo= new String[camposEnOrden.size()];
			
			for (int i=0; i<camposEnOrden.size();i++){
				arreglo[i]= camposEnOrden.get(i);
			}
		}
		for (String ls_campo:arreglo){
			if (!is_camposDeSalida.equals("")){
				is_camposDeSalida +="||";
				if (!separador.equals("")){
					is_camposDeSalida +=" '"+separador+"'|| ";
				}
			}
				
			
			is_camposDeSalida+=campos.get(ls_campo.toUpperCase()).campoSQLFormateado();
		}
			
		
	}
	
	
	
	public static String campoSQLFormateado(String is_nombre, int ii_longitud, String is_relleno, int ii_alineacion){
		String campo="";
		int li_longitud=0;
		switch(ii_alineacion){
		case Campo.D:
			li_longitud=ii_longitud;
			campo =" substr('"+Campo.rellenaString("", is_relleno, ii_longitud, true)+ "'|| " +is_nombre+", -"+li_longitud+", "+li_longitud+") " /*+ is_nombre*/;
			break;
		
		case Campo.I:
			li_longitud=ii_longitud+1;
			campo =" substr( " +is_nombre+"||'"+Campo.rellenaString("", is_relleno, ii_longitud, true)+"', "+li_longitud+", -"+li_longitud+ ") "/* + is_nombre*/;
			break;
			
		case Campo.F:
			li_longitud=ii_longitud+1;
			campo =" substr( " +is_nombre+"||'"+Campo.rellenaString("", " ", ii_longitud, true)+"', "+(li_longitud)+", -"+(li_longitud) +") "/* + is_nombre*/;
			break;
		}
		
		return campo;
	}
	
	

}
