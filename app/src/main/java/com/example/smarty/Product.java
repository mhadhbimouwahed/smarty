package com.example.smarty;

import java.util.Map;

public class Product {
    String productID;
    String productName;
    String productPrise;
    String productDescription;
    String productCategory;
    String productImage;
    String productManufacturer;
    String inStock;

    public Product(Object productName, Object productPrise, Object productDescription, Object productCategory, Object productImage, Object productManufacturer, Object inStock) {
        this.productName = (String) productName;
        this.productPrise = (String) productPrise;
        this.productDescription = (String) productDescription;
        this.productCategory = (String) productCategory;
        this.productImage = (String) productImage;
        this.productManufacturer = (String) productManufacturer;
        this.inStock = (String) inStock;
    }
    public Product(){

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

    public String getProductImage() {
        return productImage;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public String getInStock() {
        return inStock;
    }
}
