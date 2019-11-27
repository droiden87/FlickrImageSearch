package com.scalefocus.flickr.network;

import android.util.Log;

import com.scalefocus.flickr.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlickrJsonParser {
    private static final String TAG = FlickrJsonParser.class.getSimpleName();

    private static final String FLICKR_PHOTOS = "photos";
    private static final String FLICKR_IMAGE_LIST = "photo";

    private static final String FLICKR_STAT = "stat";
    private static final String FLICKR_STAT_FAIL = "fail";
    private static final String FLICKR_ERROR_MESSAGE = "message";

    private static final String FLICKR_OWNER = "owner";
    private static final String FLICKR_SECRET = "secret";
    private static final String FLICKR_ID = "id";
    private static final String FLICKR_FARM = "farm";
    private static final String FLICKR_SERVER_ID = "server";
    private static final String FLICKR_TITLE = "title";

    /**
     * Parse the result of flickr.photos.search request and return an array of photos
     *
     * @param photoJsonStr Result from the flickr.photos.search request
     * @return An array of photos or null if there is an error
     * @throws JSONException JSON parsing exception
     */
    public static Photo[] getPhotosFromJson(String photoJsonStr) throws JSONException {
        Log.v(TAG, photoJsonStr);
        JSONObject photoJson = new JSONObject(photoJsonStr);

        /* Is there an error ?*/
        if (photoJson.has(FLICKR_STAT)) {
            String status = photoJson.getString(FLICKR_STAT);
            if (status.equals(FLICKR_STAT_FAIL)) {
                if (photoJson.has(FLICKR_ERROR_MESSAGE)) {
                    String errorMessage = photoJson.getString(FLICKR_ERROR_MESSAGE);
                    Log.e(TAG, "Failed Request: " + errorMessage);
                } else {
                    Log.e(TAG, "Failed Request: Unknown Reason");
                }
                return null;
            }
        }

        JSONArray photoArray =
                photoJson.getJSONObject(FLICKR_PHOTOS).getJSONArray(FLICKR_IMAGE_LIST);

        Photo[] result = new Photo[photoArray.length()];

        for (int i = 0; i < photoArray.length(); i++) {
            String owner, secret, id, farm, serverId, title;
            JSONObject photo = photoArray.getJSONObject(i);
            owner = photo.getString(FLICKR_OWNER);
            secret = photo.getString(FLICKR_SECRET);
            id = photo.getString(FLICKR_ID);
            farm = photo.getString(FLICKR_FARM);
            serverId = photo.getString(FLICKR_SERVER_ID);
            title = photo.getString(FLICKR_TITLE);
            result[i] = new Photo(owner, secret, id, farm, serverId, title);
        }
        return result;
    }

}
