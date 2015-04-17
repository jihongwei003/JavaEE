/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import entity.Files;
import entity.Users;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import jsf.util.JsfUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.FilesFacade;

/**
 *
 * @author JiHongwei
 */
@ManagedBean(name = "homePageController")
@SessionScoped
public class HomePageController implements Serializable {

    @EJB
    private session.FilesFacade ejbFacade;
    private List<Files> items = null;
    private Files selected;

    //根据checkboxlist中相应的选择显示文件
    private List<Authority> checkBoxList = new LinkedList<Authority>();

    public List getCheckBoxList() {
        return checkBoxList;
    }

    public HomePageController() {
        //默认显示所有类型的文件（根据上传人的职务将文件分类）
        checkBoxList.add(new Authority("教务管理员", true));
        checkBoxList.add(new Authority("财务管理员", true));
        checkBoxList.add(new Authority("党务管理员", true));
        checkBoxList.add(new Authority("资产管理员", true));
        checkBoxList.add(new Authority("人事管理员", true));
        checkBoxList.add(new Authority("学生管理员", true));
    }

    public Files getSelected() {
        return selected;
    }

    public void setSelected(Files selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FilesFacade getFacade() {
        return ejbFacade;
    }

    public Files prepareCreate() {
        selected = new Files();
        initializeEmbeddableKey();
        return selected;
    }

    //显示所有文件
    public List<Files> getItems() {
        // if (items == null) {
        items = getFacade().findAll();
        // }
        return items;
    }

    private List<Files> itemsAccordOption = new LinkedList<Files>();

    //根据选择显示文件
    public List<Files> getItemsAccordOption() {
        getItems();
        updateDataList();
        return itemsAccordOption;
    }

    //更新后台数据（这个方法效率很低，时间原因就先将就着用）
    private void updateDataList() {
        //显示之前先初始化
        itemsAccordOption.clear();

        try {
            //循环检测checkboxList
            for (Authority x : checkBoxList) {
                //当前checkbox是否选中
                if (x.getIsSelected()) {
                    //单独取出某一类文件
                    for (Files f : items) { 
                        if (f.getOwnerObj().getPosition().getName().equals(x.getPosition())) {
                            itemsAccordOption.add(f);
                        }
                    }
                }//end of if
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage("当前列表为空2！");
        }
    }
    
    //ajax的listener需要调用一个方法才能刷新页面
    public void refreshPage(){
        
    }

    //页面消息提示
    public void addMessage() {
        if (itemsAccordOption.isEmpty()) {
            JsfUtil.addErrorMessage("当前列表为空1！");
        }
    }

    private Users getLoginUser() {
        //从context中取出当前登录用户
        FacesContext context = FacesContext.getCurrentInstance();
        Users user = (Users) context.getExternalContext().getSessionMap().get("loginUser");
        return user;
    }

    //根据权限挑战到上传文件页
    public String uploadFileAccordAuthority() {
        Users user = getLoginUser();

        if (user.getPosition().getPosID() == 8 || user.getPosition().getPosID() == 1) {//8代表教师
            JsfUtil.addErrorMessage("当前用户没有上传文件的权限！");
            return "/homePage.xhtml";
        }
        return "/createFile.xhtml";
    }

    //下载文件
    public StreamedContent getDownLoadFile() {
        try {
            String fileType = selected.getFile().substring(selected.getFile().lastIndexOf(".") + 1); // 提取文件的扩展名
            return download(selected.getFile(), "new." + fileType);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("请选择要下载的文件.........！");
            return null;
        }
    }

    private DefaultStreamedContent download(String srcPath, String newName) throws FileNotFoundException {
        File file = new File(srcPath);
        //boolean f = file.exists();

        FileInputStream fis = new FileInputStream(file);
        DefaultStreamedContent dsc = new DefaultStreamedContent(fis, null, newName);

        return dsc;
    }

    // bytearray to object
    private java.lang.Object byteToObject(byte[] bytes) {
        java.lang.Object obj = new java.lang.Object();
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    //根据权限下载文件
    public StreamedContent getDownLoadFileAccordAuthority() {
        try {
            Users user = getLoginUser();

            //避免重复下载
            boolean flag = false;

            //上传的人和领导可以下载
            if (selected.getOwnerObj().getUserID() == user.getUserID()
                    || user.getPosition().getName().equals("领导")) {
                flag = true;
                //return getDownLoadFile();
            }

            //权限的解析
            byte[] b = selected.getAuthority();
            List<Authority> authoList = (List<Authority>) byteToObject(b);

            //当前用户是否在权限中            
            for (Authority x : authoList) {
                if (x.getPosition().equals(user.getPosition().getName())) {
                    if (x.getIsSelected()) {
                        flag = true;
                        //return getDownLoadFile();
                    }
                }
            }

            if (flag) {
                JsfUtil.addSuccessMessage("开始下载文件！");
                return getDownLoadFile();
            } else {
                JsfUtil.addErrorMessage("当前用户没有下载该文件的权限！");
                return null;
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage("请选择要下载的文件！");
            return null;
        }

    }

    //删除文件
    public void deleteFile() {
        deleteCourse(selected.getFile());
        remove(selected);
        selected = null;
        JsfUtil.addSuccessMessage("成功删除文件！");
    }

    //删除服务器上的文件
    private void deleteCourse(String filePath) {
        File f = new File(filePath);
        //boolean a = f.exists();

        f.delete();
    }

    //删除数据库中的条目
    private void remove(Files file) {
        getFacade().remove(file);
    }

    //根据权限删除文件
    public void removeAccordAuthority() {
        Users user = getLoginUser();
        try {
            //避免重复删除
            boolean flag = false;

            //上传的人和领导可以删除文件
            if (selected.getOwnerObj().getUserID() == user.getUserID()
                    || user.getPosition().getName().equals("领导")) {
                flag = true;
            }

//            //权限的解析
//            byte[] b = selected.getAuthority();
//            List<Authority> authoList = (List<Authority>) byteToObject(b);
//
//            //当前用户是否在权限中            
//            for (Authority x : authoList) {
//                if (x.getPosition().equals(user.getPosition().getName())) {
//                    if (x.getIsSelected()) {
//                        flag = true;
//                    }
//                }
//            }

            if (flag) {
                deleteFile();
            } else {
                JsfUtil.addErrorMessage("当前用户没有删除该文件的权限！");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("请选择要删除的文件！");
        }

    }

}
