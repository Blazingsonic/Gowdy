package com.example.sonic.gowdy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sonic.gowdy.ui.DetailActivity;
import com.example.sonic.gowdy.Kneipe;
import com.example.sonic.gowdy.R;

import java.util.List;

/**
 * Created by sonic on 06.05.15.
 */
public class Kneipenadapter extends RecyclerView.Adapter<Kneipenadapter.ViewHolder> {

    private List<Kneipe> mKneipen;
    private Context mContext;

    public Kneipenadapter(List<Kneipe> kneipen, Context context) {
        mKneipen = kneipen;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mName;
        public TextView mAdresse;
        public TextView mTyp;
        public TextView mBewertung;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.nameLabel);
            mAdresse = (TextView) itemView.findViewById(R.id.adresseLabel);
            mTyp = (TextView) itemView.findViewById(R.id.typLabel);
            mBewertung = (TextView) itemView.findViewById(R.id.bewertungValue);
            mListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            mListener.onPotato(itemView, this.getAdapterPosition());
        }

        public static interface IMyViewHolderClicks {
            public void onPotato(View caller, int position);
        }
    }

    @Override
    public int getItemCount() {
        return mKneipen.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Kneipe kneipe = mKneipen.get(position);

        // Do something

        viewHolder.mName.setText(mKneipen.get(position).getName());
        viewHolder.mAdresse.setText(mKneipen.get(position).getAdresse());
        viewHolder.mTyp.setText(mKneipen.get(position).getTyp());
        viewHolder.mBewertung.setText(mKneipen.get(position).getBewertung());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kneipen_list_item, viewGroup, false);

        ViewHolder holder = new ViewHolder(view, new Kneipenadapter.ViewHolder.IMyViewHolderClicks() {
            public void onPotato(View caller, int position) {

                Log.i("Kneipenadapter", String.format("value=%d", position));
                //Toast.makeText(mContext, String.format("value=%d", position), Toast.LENGTH_SHORT).show();

                // Start new intent
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Name", mKneipen.get(position).getName());
                intent.putExtra("Adresse", mKneipen.get(position).getAdresse());
                intent.putExtra("Typ", mKneipen.get(position).getTyp());
                intent.putExtra("Bewertung", mKneipen.get(position).getBewertung());
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    public void setKneipen(List<Kneipe> kneipen) {
        this.mKneipen = kneipen;
    }

    /*public void refill(List<Kneipe> kneipen) {
        // refill
    }*/
}
