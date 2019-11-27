package com.scalefocus.flickr.model;

public class Owner {
    private String realname;
    private String nsid;
    private String iconserver;
    private int iconfarm;

    public Owner(String realname, String nsid, String iconserver, int iconfarm) {
        this.realname = realname;
        this.nsid = nsid;
        this.iconserver = iconserver;
        this.iconfarm = iconfarm;
    }

    public String getRealname() {

        if (realname == null || realname.equals("")) {
            return "Unknown name";
        }
        return realname;
    }

    public String getProfilePictureURL() {
        return "http://farm" + String.valueOf(iconfarm) + ".staticflickr.com/" +
                iconserver + "/buddyicons/" + nsid + ".jpg";
    }
}
