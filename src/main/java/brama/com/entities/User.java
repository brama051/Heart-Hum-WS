package brama.com.entities;

/**
 * Created by vedra on 09.08.2015..
 */
public class User {
    int id;
    String email;
    int sendResults;

    public User(){

    }

    public User(int id, String email, int sendResults){
        this.id=id;
        this.email=email;
        this.sendResults=sendResults;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSendResults() {
        return sendResults;
    }

    public void setSendResults(int sendResults) {
        this.sendResults = sendResults;
    }

    @Override
    public String toString(){
        return String.format("User[id=%d, email='%s', sendResults=%d]", id, email,sendResults);
    }
}
