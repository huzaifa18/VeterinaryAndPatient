package betaar.pk.Models;

import java.io.Serializable;

public class MedData implements Serializable {

    String id;
    String name;
    String group_id;
    String unit_price;
    String unit;

    public MedData(String id, String name, String group_id, String unit_price, String unit) {
        this.id = id;
        this.name = name;
        this.group_id = group_id;
        this.unit_price = unit_price;
        this.unit = unit;
    }

    public MedData() {

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

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
