package betaar.pk.Models;

public class JobInfo {

    String id;
    String title;
    String required_experience;
    String qualification;
    String user_id;
    String category_for_job_id;
    String min_salary;
    String max_salary;
    String description;

    public JobInfo(String id, String title, String required_experience, String qualification, String user_id, String category_for_job_id, String min_salary, String max_salary, String description) {
        this.id = id;
        this.title = title;
        this.required_experience = required_experience;
        this.qualification = qualification;
        this.user_id = user_id;
        this.category_for_job_id = category_for_job_id;
        this.min_salary = min_salary;
        this.max_salary = max_salary;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequired_experience() {
        return required_experience;
    }

    public void setRequired_experience(String required_experience) {
        this.required_experience = required_experience;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_for_job_id() {
        return category_for_job_id;
    }

    public void setCategory_for_job_id(String category_for_job_id) {
        this.category_for_job_id = category_for_job_id;
    }

    public String getMin_salary() {
        return min_salary;
    }

    public void setMin_salary(String min_salary) {
        this.min_salary = min_salary;
    }

    public String getMax_salary() {
        return max_salary;
    }

    public void setMax_salary(String max_salary) {
        this.max_salary = max_salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
