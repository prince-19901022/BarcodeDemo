package com.example.generator.barcode.multiplebarcodegenerator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BarcodeRecyclerViewAdapter extends RecyclerView.Adapter<BarcodeRecyclerViewAdapter.BarcodeViewHolder> {

    private Context context;
    private List<BarcodeImage> barcodeImageList;

    public BarcodeRecyclerViewAdapter(Context context) {
        this.context = context;
        barcodeImageList = new ArrayList<>();
    }

    public void addItem(List<BarcodeImage> barcodeImages){
        this.barcodeImageList = barcodeImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new BarcodeViewHolder(LayoutInflater.from(context).
                inflate(R.layout.row_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeViewHolder barcodeViewHolder, int i) {

        barcodeViewHolder.setHri(barcodeImageList.get(i).getHri());
    }

    @Override
    public int getItemCount() {
        return barcodeImageList.size();
    }

    class BarcodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hri;

        public BarcodeViewHolder(@NonNull View itemView) {
            super(itemView);

            hri = itemView.findViewById(R.id.tv_hri);
            itemView.setOnClickListener(this);
        }

        public void setHri(String hriText){
            hri.setText(hriText);
        }

        @Override
        public void onClick(View v) {
            Log.d("www.d.com", "Row : "+getAdapterPosition());
        }
    }
}
