package jsf;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JiHongwei
 */
//上传文件时设置权限的选项
public class Authority implements Serializable{

    public Authority() {
        position = null;
        isSelected = false;
    }

    public Authority(String p, Boolean b) {
        position = p;
        isSelected = b;
    }

    private String position;
    private Boolean isSelected;

    public String getPosition() {
        return position;
    }

    public void setPosition(String s) {
        position = s;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean b) {
        isSelected = b;
    }

}
