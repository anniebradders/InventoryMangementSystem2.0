package sample;


import javafx.beans.property.SimpleStringProperty;

public class Request {

      private static int request_id;
      private String type;
      private String issue;
      private int employee_id;
      private String location;
      private String status;
      private String severity;

      public Request(int id, String type, String issue, int employee_id, String location, String status, String severity){
          Request.request_id = id;
          this.type = type;
          this.issue = issue;
          this.employee_id = employee_id;
          this.location = location;
          this.status = status;
          this.severity = severity;
      }

    public String getStatus() { return status; }

    public String getLocation() { return location; }

    public int getID(){ return request_id; }

    public static void setID(int id){
        Request.request_id = id;
    }

    public int getEmployee_id() { return employee_id; }

    public String getType() {
        return type;
    }

    public String getIssue() {
       return issue;
   }

    public String setIssue(String issue) {
        this.issue = issue;
        return issue;
    }

    public String getSeverity() {
        return severity;
    }
}