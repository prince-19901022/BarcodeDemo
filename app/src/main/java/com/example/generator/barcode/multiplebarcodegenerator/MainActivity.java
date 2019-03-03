package com.example.generator.barcode.multiplebarcodegenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText gs1PrefixEditText;
    private EditText manufacturerCodeEditText;
    private EditText firstProductCodeEditText;
    private EditText totalNumberOfBarcodesEditText;

    private ProgressBar loadingIndicator;
    private TextView loadingLabel;

    private RecyclerView barcodeRecyclerView;
    private BarcodeRecyclerViewAdapter adapter;

    private static  final int WIDTH = 800;
    private static  final int HEIGHT = 400;

    private static final int HRI_HEIGHT = 40;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gs1PrefixEditText = findViewById(R.id.et_gs1_prefix);
        manufacturerCodeEditText = findViewById(R.id.et_manufacturer_code);
        firstProductCodeEditText = findViewById(R.id.et_first_product_code);
        totalNumberOfBarcodesEditText = findViewById(R.id.et_total_barcodes);

        loadingIndicator = findViewById(R.id.pb_loading_indicator);
        loadingLabel = findViewById(R.id.tv_loading_label);
        barcodeRecyclerView = findViewById(R.id.rv_barcode_list);

        barcodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        barcodeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new BarcodeRecyclerViewAdapter(this);
        barcodeRecyclerView.setAdapter(adapter);
    }

    public void onBarcodeGenerationStarted(View view) {

        if(areDataValid()){

            try{
                int productCode = Integer.parseInt(firstProductCodeEditText.getText().toString());
                int n = Integer.parseInt(totalNumberOfBarcodesEditText.getText().toString());

                List<BarcodeImage> list = new ArrayList<>();
                askUserToWait();
                MultiFormatWriter formatWriter = new MultiFormatWriter();
                BarcodeEncoder encoder = new BarcodeEncoder();
                for(int i = 0; i < n; i++){

                    BarcodeImage bi = new BarcodeImage();

                    bi.setHri(gs1PrefixEditText.getText().toString() +
                    manufacturerCodeEditText.getText().toString() +
                    String.valueOf(productCode + i));

                    list.add(bi);
                }

                adapter.addItem(list);
                endWaiting();

            }catch (NumberFormatException ex){
                ex.printStackTrace();
                return;
            }
        }
    }

    private void askUserToWait(){
        loadingLabel.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void endWaiting(){
        loadingLabel.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
    }

    private boolean areDataValid(){
        return true;
    }

    private Bitmap getBitmapWith(String text){

        Bitmap hriBitmap = Bitmap.createBitmap(WIDTH, HRI_HEIGHT, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(hriBitmap);
        canvas.drawRGB(255,255,255);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);

        // draw text to the Canvas center
        Rect boundsText = new Rect();
        paint.getTextBounds(text,
                0, text.length(),
                boundsText);
        int x = (hriBitmap.getWidth() - boundsText.width()) / 2;
        int y = (hriBitmap.getHeight() + boundsText.height()) / 2;

        canvas.drawText(text, x,y,paint);

        return hriBitmap;
    }

    private Bitmap mergeBitmaps(Bitmap barcodeBitmap, Bitmap hriBitmap){

        Bitmap resultantBitmap = Bitmap.createBitmap(barcodeBitmap.getWidth(),
                barcodeBitmap.getHeight() + hriBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultantBitmap);
        canvas.drawBitmap(barcodeBitmap,0f,0f,null);
        canvas.drawBitmap(hriBitmap,0f,barcodeBitmap.getHeight(),null);

        return resultantBitmap;
    }

    private Bitmap createBarcode(MultiFormatWriter formatWriter, BarcodeEncoder encoder, String textToEncode, BarcodeFormat format){

        try {
            BitMatrix bitMat = formatWriter.encode(textToEncode,format,WIDTH,HEIGHT);
            return encoder.createBitmap(bitMat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO : Calculate checksum and continue to generate barcode with hri.
}
