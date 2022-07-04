package enruta.sistole_gen;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;

public class Resumen extends Fragment {
	
	View rootView;
	
	DBHelper dbHelper;
	SQLiteDatabase db;
	TextView tv_resumen;
	Main ma_papa;
		 
	   @Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	          Bundle savedInstanceState) {
	 
	       rootView = inflater.inflate(R.layout.entrada, container, false);
	       ma_papa=(Main) getActivity();
	       actualizaResumen();
	       return rootView;
	   }
	   
	   public void actualizaResumen(){
		   long ll_total;
	    	
	    	
	    	String ls_resumen;
	    	final GridView gv_resumen= (GridView) rootView.findViewById(R.id.gv_resumen);
	    	tv_resumen= (TextView) rootView.findViewById(R.id.tv_resumen);
	    	Vector <EstructuraResumen>resumen= null;
	    	
	    	Cursor c;
	    	openDatabase();
	    	c= db.rawQuery("Select count(*) canti from Ruta", null);
	    	c.moveToFirst();
	    	ll_total=c.getLong(c.getColumnIndex("canti"));
	    	if (ll_total>0){
	    		
	    		resumen= ma_papa.globales.tdlg.getResumen(db);
	    		
	        	 final ResumenGridAdapter adapter = new ResumenGridAdapter(getActivity(), resumen, ma_papa.infoFontSize * ma_papa.porcentaje2);
	        	 
//	        	 gv_resumen.invalidateViews();
//	        	 adapter.notifyDataSetChanged();
	        	 
	        	 gv_resumen.setAdapter(adapter);
	        	 
			    	
		        	tv_resumen.setVisibility(View.GONE);
		        	gv_resumen.setVisibility(View.VISIBLE);
		        	
//		        	gv_resumen.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//		    			@Override
//		    			public void onGlobalLayout() {
//				        	ViewGroup.LayoutParams layoutParams = gv_resumen.getLayoutParams();
//				        	layoutParams.height =(int) adapter.height; //this is in pixels
//				        	gv_resumen.setLayoutParams(layoutParams);
//		    			   
//		    			 }
//		    			});
		        	
//		        	ViewGroup.LayoutParams layoutParams = gv_resumen.getLayoutParams();
//		        	layoutParams.height =(int) adapter.height; //this is in pixels
//		        	gv_resumen.setLayoutParams(layoutParams);
		        	
		        	
	    	} else{
//	    		tv_resumen.setText("No hay itinerarios cargados" );
	    		tv_resumen.setVisibility(View.VISIBLE);
	        	gv_resumen.setVisibility(View.GONE);
	    	}
	    	
	    	
	    	closeDatabase();
	    	
	    	
	    }
	    
	    private void openDatabase(){
	    	dbHelper= new DBHelper(getActivity());
			
	        db = dbHelper.getReadableDatabase();
	    }
		
		 private void closeDatabase(){
		    	db.close();
		        dbHelper.close();
		        
		    }
	
}
