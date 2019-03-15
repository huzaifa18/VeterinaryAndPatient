package betaar.pk.Models;

public class RequestModelVet {

    String visit_id;
    String user_id_sender;
    String user_id_receiver;
    String immediate_visit;
    String date_of_visit;
    String time_of_visit;
    String user_address;
    String user_lat;
    String user_lng;
    String reason_of_visit;
    String no_of_animals;
    String speciality_id;
    String protocol;
    String visit_started_at;
    String visit_ended_at;
    String created_at;
    String name;
    String phone;
    String category;
    String subcategory;

    public RequestModelVet(String visit_id, String user_id_sender, String user_id_receiver, String immediate_visit, String date_of_visit, String time_of_visit, String user_address, String user_lat, String user_lng, String reason_of_visit, String no_of_animals, String speciality_id, String protocol, String visit_started_at, String visit_ended_at, String created_at, String name, String phone, String category, String subcategory) {
        this.visit_id = visit_id;
        this.user_id_sender = user_id_sender;
        this.user_id_receiver = user_id_receiver;
        this.immediate_visit = immediate_visit;
        this.date_of_visit = date_of_visit;
        this.time_of_visit = time_of_visit;
        this.user_address = user_address;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.reason_of_visit = reason_of_visit;
        this.no_of_animals = no_of_animals;
        this.speciality_id = speciality_id;
        this.protocol = protocol;
        this.visit_started_at = visit_started_at;
        this.visit_ended_at = visit_ended_at;
        this.created_at = created_at;
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(String visit_id) {
        this.visit_id = visit_id;
    }

    public String getUser_id_sender() {
        return user_id_sender;
    }

    public void setUser_id_sender(String user_id_sender) {
        this.user_id_sender = user_id_sender;
    }

    public String getUser_id_receiver() {
        return user_id_receiver;
    }

    public void setUser_id_receiver(String user_id_receiver) {
        this.user_id_receiver = user_id_receiver;
    }

    public String getImmediate_visit() {
        return immediate_visit;
    }

    public void setImmediate_visit(String immediate_visit) {
        this.immediate_visit = immediate_visit;
    }

    public String getDate_of_visit() {
        return date_of_visit;
    }

    public void setDate_of_visit(String date_of_visit) {
        this.date_of_visit = date_of_visit;
    }

    public String getTime_of_visit() {
        return time_of_visit;
    }

    public void setTime_of_visit(String time_of_visit) {
        this.time_of_visit = time_of_visit;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(String user_lat) {
        this.user_lat = user_lat;
    }

    public String getUser_lng() {
        return user_lng;
    }

    public void setUser_lng(String user_lng) {
        this.user_lng = user_lng;
    }

    public String getReason_of_visit() {
        return reason_of_visit;
    }

    public void setReason_of_visit(String reason_of_visit) {
        this.reason_of_visit = reason_of_visit;
    }

    public String getNo_of_animals() {
        return no_of_animals;
    }

    public void setNo_of_animals(String no_of_animals) {
        this.no_of_animals = no_of_animals;
    }

    public String getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(String speciality_id) {
        this.speciality_id = speciality_id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVisit_started_at() {
        return visit_started_at;
    }

    public void setVisit_started_at(String visit_started_at) {
        this.visit_started_at = visit_started_at;
    }

    public String getVisit_ended_at() {
        return visit_ended_at;
    }

    public void setVisit_ended_at(String visit_ended_at) {
        this.visit_ended_at = visit_ended_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
