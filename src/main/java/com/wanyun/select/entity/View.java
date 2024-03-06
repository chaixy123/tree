package com.wanyun.select.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @version 1.0
 */
@Component
@Data
public class View implements Serializable {
    private String variable;
    private String refTime;
    private String lo2;
    private String lo1;
    private String dx;
    private String dy;
    private String nx;
    private String ny;
    private String parameterNumberName;
    private String  parameterCategory;
    private String parameterCategoryName;
    private String parameterNumber;
    private String la1;
    private String la2;
    private String parameterUnit;
    private ArrayList<Float> data;
    private String fileName;

}
