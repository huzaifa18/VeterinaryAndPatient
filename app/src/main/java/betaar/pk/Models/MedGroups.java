package betaar.pk.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class MedGroups extends MedData implements Serializable {

    String id;
    String name;
    ArrayList<MedData> medData;

    public MedGroups(String id, String name, ArrayList<MedData> medData) {
        super();
        this.id = id;
        this.name = name;
        this.medData = medData;
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

    public ArrayList<MedData> getMedData() {
        return medData;
    }

    public void setMedData(ArrayList<MedData> medData) {
        this.medData = medData;
    }
}
