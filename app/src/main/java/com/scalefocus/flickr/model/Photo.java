package com.scalefocus.flickr.model;

import android.util.Log;

public class Photo {
    private static final String TAG = Photo.class.getSimpleName();
    private String secret = "";
    private String id = "";
    private String farm = "";
    private String serverId = "";
    private String title = "";
    private String country = "";
    private String locality = "";
    private long postedDate = 0;
    private Owner owner = null;

    public Photo() {
    }

    public Photo(String owner, String secret, String id, String farm,
                 String serverId, String title) {
        this.secret = secret;
        this.id = id;
        this.farm = farm;
        this.serverId = serverId;
        this.title = title;
    }

    public String getLocation() {
        String location;
        if (country.equals("")) {
            location = "Unknown Location";
        } else {
            location = country + ", " + locality;
        }
        return location;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSecret() {
        return secret;
    }

    public String getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public long getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(long postedDate) {
        this.postedDate = postedDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailURL() {
        String thumbURL = getURL("q");
        Log.v(TAG, "Thumbnail URL: " + thumbURL);
        return thumbURL;
    }

    public String getPhotoURL() {
        String imageURL = getURL("h");
        Log.v(TAG, "Image URL: " + imageURL);
        return imageURL;
    }

    private String getURL(String size) {
        return "https://farm" + farm + ".staticflickr.com/" +
                serverId + "/" + id + "_" + secret + "_" + size + ".jpg";
    }
}
