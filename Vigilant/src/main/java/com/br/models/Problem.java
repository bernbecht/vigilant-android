package com.br.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Berhell on 28/07/14.
 */
public class Problem implements Serializable {

    private String objectId;
    private String problemProtocol;
    private String description;
    private String address;
    private LatLng coordinate;
    private Date createdAt;
    private boolean inappropriate;


    public Problem(){

    }
}
