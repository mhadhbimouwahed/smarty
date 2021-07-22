package com.example.smarty;

public class Products {
    String ProductName;
    String ProductPrise;
    String ProductCategory;
    String ProductImage;
    String ProductManufacturer;
    String ProductDescription;
    String InStock;
    String PID;



    public Products(){

    }
    public Products(String ProductName, String ProductPrise, String ProductCategory, String ProductImage, String ProductManufacturer, String ProductDescription, String InStock){
        this.ProductName=ProductName;
        this.ProductPrise=ProductPrise;
        this.ProductCategory=ProductCategory;
        this.ProductImage=ProductImage;
        this.ProductManufacturer=ProductManufacturer;
        this.ProductDescription=ProductDescription;
        this.InStock=InStock;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductPrise() {
        return ProductPrise;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public String getProductManufacturer() {
        return ProductManufacturer;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public String getInStock() {
        return InStock;
    }

    public String getPID() {
        return PID;
    }


    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductPrise(String productPrise) {
        ProductPrise = productPrise;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public void setProductManufacturer(String productManufacturer) {
        ProductManufacturer = productManufacturer;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public void setInStock(String inStock) {
        InStock = inStock;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }
}
