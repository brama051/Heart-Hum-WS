package brama.com.entities;

/**
 * Created by vedra on 09.08.2015..
 */
public class File {
    int id;
    String AWSURL;
    int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAWSURL() {
        return AWSURL;
    }

    public void setAWSURL(String AWSURL) {
        this.AWSURL = AWSURL;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
