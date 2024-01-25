package sg.nus.vttp.day26workshopredo.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.nus.vttp.day26workshopredo.model.Edit;
import sg.nus.vttp.day26workshopredo.model.Game;
import sg.nus.vttp.day26workshopredo.model.Review;
import sg.nus.vttp.day26workshopredo.repo.GameRepo;

@Service
public class GameService {
    
    @Autowired
    private GameRepo gameRepo;

    public JsonObject getGames(int skip, int limit){
        List<Document> docs = gameRepo.getGames(skip, limit);
        List<JsonObject> games = new ArrayList<>();
        for(Document d:docs){
            ObjectId _id = d.getObjectId("_id");
            int gid = d.getInteger("gid");
            String name = d.getString("name");
            int year = d.getInteger("year");
            int ranking = d.getInteger("ranking");
            int users_rated = d.getInteger("users_rated");
            String url = d.getString("url");
            String image = d.getString("image");
            Game game = new Game();
            game.setGid(gid);
            game.setImage(image);
            game.setName(name);
            game.setRanking(ranking);
            game.setUrl(url);
            game.setUsers_rated(users_rated);
            game.setYear(year);
            game.set_id(_id);
            games.add(game.gidAndNameJson());
        }
        int total = games.size();
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JsonArrayBuilder JAB = Json.createArrayBuilder();
        for(JsonObject JSO: games){
            JAB.add(JSO);
        }
        JOB.add("games",JAB.build())
            .add("offset",skip)
            .add("limit",limit)
            .add("total",total)
            .add("timestamp",new Date().toString());
        return JOB.build();
    }

    public JsonObject getGamesRanked(int skip, int limit){
        List<Document> docs = gameRepo.getGamesRanked(skip, limit);
        List<JsonObject> games = new ArrayList<>();
        for(Document d:docs){
            ObjectId _id = d.getObjectId("_id");
            int gid = d.getInteger("gid");
            String name = d.getString("name");
            int year = d.getInteger("year");
            int ranking = d.getInteger("ranking");
            int users_rated = d.getInteger("users_rated");
            String url = d.getString("url");
            String image = d.getString("image");
            Game game = new Game();
            game.setGid(gid);
            game.setImage(image);
            game.setName(name);
            game.setRanking(ranking);
            game.setUrl(url);
            game.setUsers_rated(users_rated);
            game.setYear(year);
            game.set_id(_id);
            games.add(game.gidAndNameJson());
        }
        int total = games.size();
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JsonArrayBuilder JAB = Json.createArrayBuilder();
        for(JsonObject JSO: games){
            JAB.add(JSO);
        }
        JOB.add("games",JAB.build())
            .add("offset",skip)
            .add("limit",limit)
            .add("total",total)
            .add("timestamp",new Date().toString());
        return JOB.build();
    }

    public JsonObject getGameById(int gidSearch){
        Optional<Document> doc = gameRepo.getGameByGid(gidSearch);
        if(doc.isEmpty()){
            return null;
        }
        Document d = doc.get();
        ObjectId _id = d.getObjectId("_id");
        int gid = d.getInteger("gid");
        String name = d.getString("name");
        int year = d.getInteger("year");
        int ranking = d.getInteger("ranking");
        int users_rated = d.getInteger("users_rated");
        String url = d.getString("url");
        String image = d.getString("image");
        Game game = new Game();
        game.setGid(gid);
        game.setImage(image);
        game.setName(name);
        game.setRanking(ranking);
        game.setUrl(url);
        game.setUsers_rated(users_rated);
        game.setYear(year);
        game.set_id(_id);
        return game.toJson();

    }

    public Document addNewReview(String name, int gid, String comment, int rating){
        Review review = new Review();
        review.set_id(gid);
        review.setComment(comment);
        review.setGid(gid);
        review.setName(name);
        review.setRating(rating);
        if(gameRepo.gameExists(gid)){
            Document document = review.toDocument();
            document.put("posted",new Date().toString());
            document.put("name",gameRepo.getGameName(gid));
            Document insertedDoc = gameRepo.addReview(document);
            return insertedDoc;
        }
        return null;
    }
    
    public Optional<Long> updateReview(int review_id, String editComment, Integer editRating, String editPosted){
        if(gameRepo.reviewExists(review_id)){
            Document document = gameRepo.getReview(review_id);
            List<Document> edited = new ArrayList<>();
            if(document.get("edited") != null){
                edited = (List<Document>)document.get("edited");

            }
            String comment = document.getString("comment");
            int rating = document.getInteger("rating");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            String posted = document.getString("posted");
            // make the edit
            Edit edit = new Edit();
            edit.setComment(comment);
            edit.setPosted(posted);
            edit.setRating(rating);
            edited.add(edit.toDocument());

            // make the review and update
            Review review = new Review();
            review = review.fromDocument(document);
            review.setComment(editComment);
            review.setRating(editRating);

            // convert review to doc
            Document reviewDoc = review.toDocument();
            reviewDoc.put("posted",editPosted);
            reviewDoc.put("edited",edited);
            reviewDoc.put("edit",edit.toDocument());
            long updateCount = gameRepo.updateReview(review_id, reviewDoc);
            return Optional.of(updateCount);
        }
        return Optional.empty();

    }
}
