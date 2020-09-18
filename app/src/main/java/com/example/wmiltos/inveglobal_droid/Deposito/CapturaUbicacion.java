package com.example.wmiltos.inveglobal_droid.Deposito;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.PruebasBD.ScannerActivity;
import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.ConfiguracionSoporteActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.LecturasActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CapturaUbicacion extends AppCompatActivity {

    EditText codigoSoporte, codigoManipulacion;
    private ZXingScannerView vistaEscaner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_ubicacion);
        variables();

    }

    private void variables() {

        codigoSoporte = findViewById(R.id.et_codigoSoporte);
        codigoManipulacion = findViewById(R.id.et_codigoManipulacion);

    }

    public void aceptarlo(View view) {

        envioMensaje_a_Ubicacion();
    }

    public void envioMensaje_a_Ubicacion (){
        if (codigoSoporte.getText().toString().isEmpty() || codigoManipulacion.getText().toString().isEmpty()){//si viene vacio el editext del scanner ubicacion
            dialogoBtnCerrar();//cierra
        }else {
            Intent miIntent = null;
            String msjcodigoSoporte = codigoSoporte.getText().toString();
            String msjcodigoManipulacion = codigoManipulacion.getText().toString();

            miIntent = new Intent(CapturaUbicacion.this, UbicacionDeposito.class);
            Bundle miBundle = new Bundle();
            miBundle.putString("msjcodigoSoporte", codigoSoporte.getText().toString());
            miBundle.putString("msjcodigoManipulacion", codigoManipulacion.getText().toString());


            miIntent.putExtras(miBundle);
            startActivity(miIntent);//abre la sgte ventana
            CapturaUbicacion.this.finish();//finaliza la ventana anterior

        }
    }

    public void dialogoBtnCerrar(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(CapturaUbicacion.this);
        dialogo.setMessage("El campo Ubicación o la Unidad de Manipulación esta vacio.!").setTitle("Ubicación")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CapturaUbicacion.this.finish();//finaliza la ventana

            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }



    public void capturaCodigo(){
        vistaEscaner = new ZXingScannerView(this);
        vistaEscaner.setResultHandler(new escanearAqui());
        setContentView(vistaEscaner);
        vistaEscaner.startCamera();
    }

    //BOTON SCANNER -- llama a la camara
    class escanearAqui implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result rawResult) {
            String dato = rawResult.getText();
            setContentView(R.layout.activity_lecturas);
            vistaEscaner.stopCamera();
            codigoSoporte =  findViewById(R.id.et_scanningLectura);
            codigoSoporte.setText(dato);//muestra en la tx

            //tvScanning = (TextView) findViewById(R.id.tv_scanning);
           // tvScanning.setText(dato);

            //llama nuevamente en esta subClase los metodos iniciales
          //  navigationView();
            variables();
            //recepcionDatosUbicacion();
            //validacionCampoScanning();
        }
    }


    public void scanner(View view) {
        capturaCodigo();

    }



}
