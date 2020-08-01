package com.example.picarprojectfinal;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "PicarTable")

public class PicarDO {
    private String _devID;
    private String _time;
    private Float _distance;

    @DynamoDBHashKey(attributeName = "dev_id")
    @DynamoDBAttribute(attributeName = "dev_id")
    public String get_devID() {
        return _devID;
    }

    public void set_devID(final String _devID) {
        this._devID = _devID;
    }

    @DynamoDBRangeKey (attributeName = "time")
    @DynamoDBAttribute(attributeName = "time")
    public String get_time() {
        return _time;
    }

    public void set_time(final String _time) {
        this._time= _time;
    }

    @DynamoDBIndexHashKey(attributeName = "distance")
    @DynamoDBAttribute(attributeName = "distance")
    public Float getDistance() {
        return _distance;
    }

    public void setDistance(final Float _distance) {
        this._distance = _distance;
    }

}
