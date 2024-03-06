package com.wanyun.select.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 */
@Data
public class City1 implements Serializable {
    private String LocationId;
    private float Latitude;
    private float Longitude;
}
