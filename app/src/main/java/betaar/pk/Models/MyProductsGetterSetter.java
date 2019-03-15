package betaar.pk.Models;

import java.util.ArrayList;

/**
 * Created by User-10 on 16-Apr-18.
 */

public class MyProductsGetterSetter {

    private String id;
    private String name;
    private String category_id;
    private String sub_category_for_product_id;
    private String internal_sub_category_for_product_id;
    private String quantity;
    private String price;
    private String unit;
    private String description;
    private String categoryName;
    private String subCategoryName;
    private String internalSubCategoryName;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private ArrayList<String> images;
    private String postedById;
    private String postedByName;
    private String postedByPhone;

    public MyProductsGetterSetter(String id, String name, String category_id, String sub_category_for_product_id, String internal_sub_category_for_product_id, String quantity, String price, String unit, String description, String categoryName, String subCategoryName, String internalSubCategoryName, String image1, ArrayList<String> images, String postedById, String postedByName, String postedByPhone) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.sub_category_for_product_id = sub_category_for_product_id;
        this.internal_sub_category_for_product_id = internal_sub_category_for_product_id;
        this.quantity = quantity;
        this.price = price;
        this.unit = unit;
        this.description = description;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.internalSubCategoryName = internalSubCategoryName;
        this.image1 = image1;
        this.images = images;
        this.postedById = postedById;
        this.postedByName = postedByName;
        this.postedByPhone = postedByPhone;
    }

    public MyProductsGetterSetter(String id, String name, String category_id, String sub_category_for_product_id, String internal_sub_category_for_product_id, String price, String unit, String description, String categoryName, String subCategoryName, String internalSubCategoryName, String image1, String image2, String image3, String image4) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.sub_category_for_product_id = sub_category_for_product_id;
        this.internal_sub_category_for_product_id = internal_sub_category_for_product_id;
        this.price = price;
        this.unit = unit;
        this.description = description;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.internalSubCategoryName = internalSubCategoryName;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSub_category_for_product_id() {
        return sub_category_for_product_id;
    }

    public void setSub_category_for_product_id(String sub_category_for_product_id) {
        this.sub_category_for_product_id = sub_category_for_product_id;
    }

    public String getInternal_sub_category_for_product_id() {
        return internal_sub_category_for_product_id;
    }

    public void setInternal_sub_category_for_product_id(String internal_sub_category_for_product_id) {
        this.internal_sub_category_for_product_id = internal_sub_category_for_product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getInternalSubCategoryName() {
        return internalSubCategoryName;
    }

    public void setInternalSubCategoryName(String internalSubCategoryName) {
        this.internalSubCategoryName = internalSubCategoryName;
    }

    public String getPostedById() {
        return postedById;
    }

    public void setPostedById(String postedById) {
        this.postedById = postedById;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public String getPostedByPhone() {
        return postedByPhone;
    }

    public void setPostedByPhone(String postedByPhone) {
        this.postedByPhone = postedByPhone;
    }
}
