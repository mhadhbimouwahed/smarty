package com.example.smarty;

import android.net.Uri;

public class Product {
    String productID;
    String productName;
    String productPrise;
    String productDescription;
    String productCategory;
    String productImage;
    String productManufacturer;
    String inStock;
    private boolean expanded;

    public Product(Object productID,Object productName, Object productPrise, Object productDescription, Object productCategory, Object productImage, Object productManufacturer, Object inStock) {
        this.productID=(String) productID;
        this.productName = (String) productName;
        this.productPrise = (String) productPrise;
        this.productDescription = (String) productDescription;
        this.productCategory = (String) productCategory;
        this.productImage = (String) productImage;
        this.productManufacturer = (String) productManufacturer;
        this.inStock = (String) inStock;
        this.expanded=false;
    }
    public Product(Object productName, Object productPrise, Object productDescription, Object productCategory, Object productImage, Object productManufacturer, Object inStock) {

        this.productName = (String) productName;
        this.productPrise = (String) productPrise;
        this.productDescription = (String) productDescription;
        this.productCategory = (String) productCategory;
        this.productImage = (String) productImage;
        this.productManufacturer = (String) productManufacturer;
        this.inStock = (String) inStock;
        this.expanded=false;
    }
    public Product(){

    }

    public boolean isExpanded(){
        return expanded;
    }

    public void setIsExpanded(boolean expanded){
        this.expanded=expanded;
    }




    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrise() {
        return productPrise;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public Uri getProductImage() {
        return Uri.parse(productImage);
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public String getInStock() {
        return inStock;
    }
}
