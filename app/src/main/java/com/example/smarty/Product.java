package com.example.smarty;

public class Product {
    public String ProductName;
    public String ProductPrise;
    public String ProductPhotoURL;
    public String ProductManufacturer;
    public String ProductDescription;
    public String InStock;

    public Product(){

    }
    public Product(String ProductName,String ProductPrise,String ProductPhotoURL,String ProductManufacturer,String ProductDescription,String InStock){
        this.ProductName=ProductName;
        this.ProductPrise=ProductPrise;
        this.ProductPhotoURL=ProductPhotoURL;
        this.ProductManufacturer=ProductManufacturer;
        this.ProductDescription=ProductDescription;
        this.InStock=InStock;
    }
    public Product(String ProductName,String ProductPrise){
        this.ProductName=ProductName;
        this.ProductPrise=ProductPrise;
    }

}
