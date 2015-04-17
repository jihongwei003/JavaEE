package jsf;

import entity.Position;
import entity.Users;
import jsf.util.JsfUtil;
import session.UsersFacade;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import session.PositionFacade;

@ManagedBean(name = "userRegistrationController")
@SessionScoped
public class UserRegistration implements Serializable {

    private Users current;
    //private DataModel items = null;
    @EJB
    private session.UsersFacade usersFacade;

    @EJB
    private session.PositionFacade positionFacade;

    @PersistenceContext(unitName = "GCGJAppPU") //注意这里的名字，需要与persistence中的配置一致
    private EntityManager em;

    private PositionFacade getPositionFacade() {
        return positionFacade;
    }

    //从多个职务中选择一个
    public SelectItem[] getItemsAvailableSelectOne() {
        int size = getPositionFacade().findAll().size() + 1;
        SelectItem[] item = new SelectItem[size];
        int i = 0;

        item[0] = new SelectItem("", "---");
        i++;

        for (Object x : getPositionFacade().findAll()) {
            Position pos = (Position) x;
            item[i++] = new SelectItem(pos, pos.getName());//参数（value, label）
        }
        return item;
    }

    //private int selectedItemIndex;
    public UserRegistration() {
    }

    public Users getSelected() {
//        if (current == null) {
//            current = new Users();
//            //selectedItemIndex = -1;
//        }
        return current;
    }

    public void setSelected(Users usr) {
        this.current = usr;
    }

    private UsersFacade getFacade() {
        return usersFacade;
    }

//    public String prepareCreate() {
//        current = new Users();
//        selectedItemIndex = -1;
//        return "Create";
//    }
    //在网页载入的时候调用
    public String prepareCreate() {
        current = new Users();
        //selectedItemIndex = -1;
        return "login";
    }

    public String create() {

        if (current.getUserName().length() < 1 || current.getPassword().length() < 1) {
            JsfUtil.addErrorMessage("用户名或密码为空！");
            return "login";
        }

        if (current.getPosition() == null) {
            JsfUtil.addErrorMessage("职务不能为空！");
            return "login";
        }

        try {
            current.setUserID(0);

            //需要先进行查重
            //使用query访问数据库
            Query query = em.createNamedQuery("Users.findByUserName");
            query.setParameter("userName", current.getUserName());
            Users user = (Users) query.getSingleResult();

            JsfUtil.addErrorMessage("用户名已注册！");

            return "login"; //发现重复立即返回

        } catch (NoResultException e) {
            //持久化
            getFacade().create(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersCreated"));

            return "login";

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    @FacesConverter(forClass = Users.class)
    public static class UsersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserRegistration controller = (UserRegistration) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersController");
            return controller.usersFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Users) {
                Users o = (Users) object;
                return getStringKey(o.getUserID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Users.class.getName());
            }
        }

    }

}
