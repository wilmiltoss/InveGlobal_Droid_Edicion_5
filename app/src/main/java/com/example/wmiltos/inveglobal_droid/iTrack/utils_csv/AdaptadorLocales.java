package com.example.wmiltos.inveglobal_droid.iTrack.utils_csv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.wmiltos.inveglobal_droid.iTrack.tablasCSV_Objetos.Locales;

import java.util.List;

public class AdaptadorLocales extends RecyclerView.Adapter<AdaptadorLocales.LocalesViewHolder> {

    Context context;
    List<Locales> ListaLocales;

    public AdaptadorLocales(Context context, List<Locales> ListaLocales) {
        this.context = context;
        this.ListaLocales = ListaLocales;
    }

    @NonNull
    @Override
    public LocalesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalesViewHolder localesViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LocalesViewHolder extends RecyclerView.ViewHolder {
        public LocalesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
