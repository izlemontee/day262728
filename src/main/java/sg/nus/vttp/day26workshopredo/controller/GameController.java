package sg.nus.vttp.day26workshopredo.controller;

import java.io.StringReader;
import java.util.Date;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.nus.vttp.day26workshopredo.services.GameService;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public ResponseEntity<String> getGames(@RequestParam(name="offset", required = false) String offsetString,
                                            @RequestParam(name="limit", required=false) String limitString){
        int offset = 0;
        int limit = 25;
        if(offsetString != null){
            offset = Integer.parseInt(offsetString);
        }
        if(limitString != null){
            limit = Integer.parseInt(limitString);
        }
        JsonObject jsonObject = gameService.getGames(offset, limit);
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                                            .body(jsonObject.toString());
        return response;
    }

    @GetMapping(path = "/games/rank")
    public ResponseEntity<String> getGamesRanked(@RequestParam(name="offset", required = false) String offsetString,
            @RequestParam(name="limit", required=false) String limitString){
        int offset = 0;
        int limit = 25;
        if(offsetString != null){
        offset = Integer.parseInt(offsetString);
        }
        if(limitString != null){
        limit = Integer.parseInt(limitString);
        }
        JsonObject jsonObject = gameService.getGamesRanked(offset, limit);
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
            .body(jsonObject.toString());
        return response;
    }

    @GetMapping(path = "/game/{gid}")
    public ResponseEntity<String> getGameById(@PathVariable(name = "gid") String gidString){
        int gid = Integer.parseInt(gidString);
        JsonObject jsonObject = gameService.getGameById(gid);
        if(jsonObject == null){
            ResponseEntity<String> response = ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("Not found.");
            return response;
        }
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
        return response;
    }

    @PostMapping(path = "/review")
    public ResponseEntity<String> addNewReview(@RequestBody MultiValueMap<String,Object> mvm){
        String name = mvm.getFirst("name").toString();
        Integer gid = Integer.parseInt(mvm.getFirst("gid").toString());
        String comment = mvm.getFirst("comment").toString();
        int rating = Integer.parseInt(mvm.getFirst("rating").toString());
        Document insertedDoc = gameService.addNewReview(name, gid, comment, rating);
        if(insertedDoc == null){
            ResponseEntity<String> response = ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("Not found.");
            return response;
        }
        ResponseEntity<String> response = ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(insertedDoc.toString());
        return response;

    }

    @PutMapping(path = "/review/{review_id}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "review_id")String idString,
                                                @RequestBody String body){
        StringReader stringReader = new StringReader(body);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonObject jsonObject = jsonReader.readObject();
        String comment = jsonObject.getString("comment");
        Integer rating = jsonObject.getInt("rating");
        String posted = (new Date()).toString();
        int review_id = Integer.parseInt(idString);
        Optional<Long> updateCount = gameService.updateReview(review_id, comment, rating, posted);
        if(updateCount.get()>0){
            ResponseEntity<String> response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("ok");
            return response;
        }
        ResponseEntity<String> response = ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("not found");

        return response;
    }

    
}
