package com.example.generator.barcode.multiplebarcodegenerator;

import android.graphics.Bitmap;

public class BarcodeImage {

    private Bitmap barcodeImage;
    private String hri;

    public Bitmap getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(Bitmap barcodeImage) {
        this.barcodeImage = barcodeImage;
    }

    public String getHri() {
        return hri;
    }

    public void setHri(String hri) {
        this.hri = hri;
    }
}
