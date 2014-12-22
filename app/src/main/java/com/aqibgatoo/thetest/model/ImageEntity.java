package com.aqibgatoo.thetest.model;

import com.google.api.client.util.Key;
import com.kinvey.java.LinkedResources.LinkedGenericJson;
import com.kinvey.java.model.KinveyMetaData;

/**
 * Created by Aqib on 12/19/2014.
 */
public class ImageEntity extends LinkedGenericJson {
    public ImageEntity() {
        putFile("image");
    }

    @Key("_id")
    private String id;
    @Key
    private String name;
    @Key("_kmd")
    private KinveyMetaData kinveyMetaData;
    @Key("_acl")
    private KinveyMetaData.AccessControlList accessControlList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KinveyMetaData getKinveyMetaData() {
        return kinveyMetaData;
    }

    public void setKinveyMetaData(KinveyMetaData kinveyMetaData) {
        this.kinveyMetaData = kinveyMetaData;
    }

    public KinveyMetaData.AccessControlList getAccessControlList() {
        return accessControlList;
    }

    public void setAccessControlList(KinveyMetaData.AccessControlList accessControlList) {
        this.accessControlList = accessControlList;
    }


    @Override
    public String toString() {
        return "ImageEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", kinveyMetaData=" + kinveyMetaData +
                ", accessControlList=" + accessControlList +
                '}';
    }
}
