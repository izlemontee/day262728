package sg.nus.vttp.day26workshopredo.model;

import org.bson.Document;

public class Edit {
    
    private String comment;
    private int rating;
    private String posted;
    
    public Document toDocument(){
        Document document = new Document();
        document.put("comment",comment);
        document.put("rating",rating);
        document.put("posting",posted);
        return document;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getPosted() {
        return posted;
    }
    public void setPosted(String posted) {
        this.posted = posted;
    }
    
}
