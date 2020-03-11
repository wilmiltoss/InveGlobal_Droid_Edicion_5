package com.example.wmiltos.inveglobal_droid.iTrack.utils_csv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.iTrack.CapturasActivity;
import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Product;

import java.util.ArrayList;

public class AdaptadorLista extends BaseAdapter {

    private Context context;
    private ArrayList<Product> entradas;

    public AdaptadorLista(Context context, ArrayList<Product> entradas) {
        this.context = context;
        this.entradas = entradas;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int position) { return entradas.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // inflate the layout.
        final ViewHolder mHolder;
        //selecciona el items de la columna
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mylistado, parent, false);
            mHolder = new ViewHolder();
            mHolder.txColumnId= convertView.findViewById(R.id.text2);
            mHolder.txColumnCodigo= convertView.findViewById(R.id.text2);
            mHolder.txColumnDescrip= convertView.findViewById(R.id.columna_descripcion);
            mHolder.txColumnCantidad= convertView.findViewById(R.id.columna_cantidad);
            convertView.setTag(mHolder);

        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }

        //muestra el texto en la lista
        Product item = entradas.get(position);
        mHolder.txColumnId.setText(item.getId());//en que columna con txTexto2 y que texto del array en getTexto1
        mHolder.txColumnCodigo.setText(item.getCodigo_producto());
        mHolder.txColumnDescrip.setText(item.getDescripcion());
        mHolder.txColumnCantidad.setText(item.getCantidad());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHolder.txColumnId.setText("Usted salió de aquí!");

                Intent intent = new Intent(context, CapturasActivity.class);
                intent.putExtra("texto1", entradas.get(position).getId());
                intent.putExtra("texto2", entradas.get(position).getCodigo_producto());
                intent.putExtra("texto3", entradas.get(position).getDescripcion());
                intent.putExtra("texto4", entradas.get(position).getCantidad());
                context.startActivity(intent);

                notifyDataSetChanged();
            }
        });

        convertView.setTag(mHolder);
        return convertView;
        //return convertView;
    }

    private class ViewHolder {
        private TextView txColumnId;
        private TextView txColumnCodigo;
        private TextView txColumnDescrip;
        private TextView txColumnCantidad;
    }
}
