package brama.com.entities;

/**
 * Created by vedra on 10.08.2015..
 */
public class FileUploadResponse {
    String path;
    String url;
    String email;

    public FileUploadResponse(String path, String url, String email){
        this.path = path;
        this.url = url;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return "FileUploadResponse [path=" + path + ", url=" + url + ", email=" + email + "]";
    }
}
