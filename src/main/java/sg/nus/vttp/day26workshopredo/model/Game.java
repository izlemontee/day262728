package sg.nus.vttp.day26workshopredo.model;


import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

//@Document
public class Game {

    @Id
    private ObjectId _id;
    private int gid;
    private String name;
    private int year;
    private int ranking;
    private int users_rated;
    private String url;
    private String image;

    public JsonObject toJson(){
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JOB.add("_id",_id.toString())
            .add("gid",gid)
            .add("name",name)
            .add("year",year)
            .add("ranking",ranking)
            .add("users_rated",users_rated)
            .add("url",url)
            .add("image",image);
        return JOB.build();
    }

    public JsonObject gidAndNameJson(){
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JOB.add("gid",gid)
            .add("name",name);
        return JOB.build();
    }

    public Document toDocWithoutId(){
        Document document = new Document();
        document.put("gid",gid);
        document.put("name",name);
        document.put("year",year);
        document.put("ranking",ranking);
        document.put("users_rated",users_rated);
        document.put("url",url);
        document.put("image",image);
        return document;

                
    }

    public ObjectId get_id() {
        return _id;
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    public int getGid() {
        return gid;
    }
    public void setGid(int gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getRanking() {
        return ranking;
    }
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    public int getUsers_rated() {
        return users_rated;
    }
    public void setUsers_rated(int users_rated) {
        this.users_rated = users_rated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    
}
