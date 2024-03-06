package com.wanyun.select.utils;

/**
 * @author 李帅明
 * @version 1.0
 * @date 2022/3/14 9:46
 */
public class VariableList {
    public static String getVariable(String variable ){

        switch (variable){
            case "temp" :
             variable="TMP_GDS3_HTGL";
             break;
            case "u_wind" :
                variable="U_GRD_GDS3_HTGL";
                break;
            case "v_wind" :
                variable="V_GRD_GDS3_HTGL";
                break;
            default:
                variable="文件内没有该变量" ;
        }
        return variable;
    }
}
