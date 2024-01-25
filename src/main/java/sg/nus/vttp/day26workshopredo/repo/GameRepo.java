package sg.nus.vttp.day26workshopredo.repo;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

@Repository
public class GameRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Document> getGames(int skip, int limit){
        //  db.game.find(

        // ).
        // skip(5).
        // limit(10)
        Query query = new Query();
        query.skip(skip).limit(limit);
        List<Document> result = mongoTemplate.find(query,Document.class,"game");
        return result;
    }

    public List<Document> getGamesRanked(int skip, int limit){
        //db.game.find().sort({"ranking":-1})
        Query query = new Query();
        query.with(Sort.by(Direction.ASC,"ranking"));
        List<Document> result = mongoTemplate.find(query,Document.class,"game");
        return result;
    }

    public Optional<Document> getGameByGid(int gid){
        // db.game.find(
        // {
        //     gid:342
        // }
        // )

        Query query = Query.query(Criteria.where("gid").is(gid));
        List<Document> result = mongoTemplate.find(query, Document.class,"game");
        if(result.size() == 0){
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    public boolean gameExists(int gid){
        // db.game.find(
        // {
        //     gid:342
        // }
        // )
        Query query = Query.query(Criteria.where("gid").is(gid));
        List<Document> result = mongoTemplate.find(query, Document.class,"game");
        return(result.size()>0);
    }

    public String getGameName(int gid){
        //db.game.find({gid:342},{"name":1})
        Query query = Query.query(Criteria.where("gid").is(gid));
        query.fields().include("name");
        List<Document> result = mongoTemplate.find(query,Document.class,"game");
        Document d = result.get(0);
        return d.getString("name");
    }

    public Document addReview(Document document){
        Document insertedDoc = mongoTemplate.insert(document, "reviews");
        return insertedDoc;
    }

    public boolean reviewExists(int review_id){
        Query query = Query.query(Criteria.where("_id").is(review_id));
        List<Document> result = mongoTemplate.find(query, Document.class,"reviews");
        return (result.size()>0);
    }

    public Document getReview(int review_id){
        Query query = Query.query(Criteria.where("_id").is(review_id));
        List<Document> result = mongoTemplate.find(query, Document.class,"reviews");
        return result.get(0);
    }

    public long updateReview(int review_id, Document reviewDoc){
        // db.reviews.update(
        //     {
        //         "_id": 53
        //     },
        //     {
        //         $set: { comment: "this game is mid", rating: 4} ,
        //         $push:{edited:{"comment":"this comment"}}
        //     }
        //     ,
        //     {$upsert:true}
        // ); 
        Query query = Query.query(Criteria.where("_id").is(review_id));
        Update update = new Update().set("comment","this game is alright").
                        set("rating",reviewDoc.get("rating"))
                        .set("posted",reviewDoc.get("posted")).push("edited", reviewDoc.get("edit"));
        UpdateResult result = mongoTemplate.updateMulti(query, update, "reviews");
        return result.getModifiedCount();
    }


    
}
