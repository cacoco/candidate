package controllers;

import notifiers.Notifier;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import play.*;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void apply() {
        // Don't serve GET requests or if the content-type is NOT json
        if ("GET".equalsIgnoreCase(request.get().method) ||
                !"application/json".equalsIgnoreCase(request.get().contentType)) {
            response.status = Http.StatusCode.BAD_REQUEST;
            return;
        }
        Logger.debug(String.format("Request method: [%s]", request.get().method));
        Logger.debug(String.format("Content type: [%s]", request.get().contentType));

        final BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) { sb.append(line); }

            // These are all required
            final JSONObject json = new JSONObject(sb.toString());
            final String name = json.getString("name");
            final String email = json.getString("email");
            final String about = json.getString("about");
            final JSONArray array = json.getJSONArray("urls");
            String[] urls = new String[0];
            if (array != null) {
                urls = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    urls[i] = array.getString(i);
                }
            }

            boolean previouslySubmitted = false;
            Logger.info(String.format("Looking for candidate previously submitted with email: [%s]", email));
            Candidate candidate = Candidate.find("byEmail", email).first();
            if (candidate == null) {
                Logger.info(String.format("No candidate found for email: [%s], creating new candidate.", email));
                candidate = new Candidate(name, email, about, Arrays.toString(urls));
            } else {
                Logger.info(String.format("Found previously submitted candidate with email: [%s], updating record.", email));
                previouslySubmitted = true;
                // only update name, about and urls
                candidate.name = name;
                candidate.about = about;
                candidate.urls = Arrays.toString(urls);
                candidate.modifiedon = new Date();
            }
            // save
            candidate = candidate.save();
            Logger.debug("Saved candidate.");
            try {
                Notifier.candidate(candidate, previouslySubmitted);
            } catch (Exception e) {
                Logger.info(String.format("Unable to email about new candidate submittal: %s", e.getMessage()));
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            response.status = Http.StatusCode.BAD_REQUEST;
            return;
        }

        response.status = Http.StatusCode.OK;
    }
}