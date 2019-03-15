package betaar.pk.Models;

/**
 * Created by User-10 on 16-Apr-18.
 */

public class FarmSolutionData {


    private String title;
    private String sample1;
    private String sample2;
    private String sample3;

    public FarmSolutionData(String title) {
        this.title = title;
    }

    public FarmSolutionData(String title, String sample1, String sample2, String sample3){

        this.title = title;
        this.sample1 = sample1;
        this.sample2 = sample2;
        this.sample3 = sample3;



    }

    public String getTitle() {
        return title;
    }

    public String getSample1() {
        return sample1;
    }

    public String getSample2() {
        return sample2;
    }

    public String getSample3() {
        return sample3;
    }






}
