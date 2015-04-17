/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import entity.Users;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author JiHongwei
 */
@ManagedBean(name = "templateController")
@SessionScoped
public class TemplateController  implements Serializable{
    
    private Users user;
    
    public TemplateController(){
        //从context中取出当前登录用户 //不能在这里获取，构造方法只调用一回
//        FacesContext context = FacesContext.getCurrentInstance();
//        user = (Users) context.getExternalContext().getSessionMap().get("loginUser");
    }
    
    private Users getLoginUser(){
        //从context中取出当前登录用户
        FacesContext context = FacesContext.getCurrentInstance();
        user = (Users) context.getExternalContext().getSessionMap().get("loginUser");
        return user;
    }
    
    public String getUserName(){
        user = getLoginUser();
        return user.getUserName();
    }
    
    public String getUserPosition(){
        user = getLoginUser();
        return user.getPosition().getName();
    }
    
    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("loginUser", user);
        
        return "/login.xhtml";
    }
}
