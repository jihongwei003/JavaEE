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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jsf.util.JsfUtil;

/**
 *
 * @author JiHongwei
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private String usrName = "1";

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    private String password = "1";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PersistenceContext(unitName = "GCGJAppPU") //注意这里的名字，需要与persistence中的配置一致
    private EntityManager em;

    public String login() {

        try {
            //使用query访问数据库
            Query query = em.createNamedQuery("Users.findByUserName");
            query.setParameter("userName", usrName);
            Users user = (Users) query.getSingleResult();
            
            if (user.getPassword().equals(password)) {
                //将登录的用户放入context中，供其它网页使用
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().put("loginUser", user);
                
                return "/homePage.xhtml";
            } else {
                
                JsfUtil.addErrorMessage("用户名或密码错误！");
                
                return "/login.xhtml";
            }
        } catch (NoResultException e) {
            JsfUtil.addErrorMessage("用户名不存在！");
            return "/login.xhtml"; //初始数据库为空时会出错！
        }
    }
}
