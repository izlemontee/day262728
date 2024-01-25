package sg.nus.vttp.day26workshopredo.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;

public class Review {
    @Id
    private int _id;
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    private String name;
    private int rating;
    private String comment;
    private int gid;

    public Document toDocument(){
        Document document = new Document();
        document.put("_id",_id);
        document.put("user",name);
        document.put("rating",rating);
        document.put("comment",comment);
        document.put("gid",gid);
        return document;

    }

    public Review fromDocument(Document document){
        Review review = new Review();
        review.setName(document.getString("name"));
        review.setRating(document.getInteger("rating"));
        review.setComment(document.getString("comment"));
        review.setGid(document.getInteger("gid"));
        review.set_id(document.getInteger("_id"));
        return review;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getGid() {
        return gid;
    }
    public void setGid(int gid) {
        this.gid = gid;
    }
    
}
