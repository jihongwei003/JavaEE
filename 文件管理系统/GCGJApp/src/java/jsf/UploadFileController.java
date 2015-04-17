package jsf;

import entity.Files;
import entity.Users;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import jsf.util.JsfUtil;
import session.FilesFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "uploadFileController")
@SessionScoped
public class UploadFileController implements Serializable {

    @EJB
    private session.FilesFacade filesFacade;

    public UploadFileController() {
        checkBoxList.add(new Authority("教务管理员", true));
        checkBoxList.add(new Authority("财务管理员", true));
        checkBoxList.add(new Authority("党务管理员", true));
        checkBoxList.add(new Authority("资产管理员", true));
        checkBoxList.add(new Authority("人事管理员", true));
        checkBoxList.add(new Authority("学生管理员", true));
        checkBoxList.add(new Authority("教师", true));
    }

    private Files selected;
    private String file; //文件路径
    private UploadedFile fileUpload = null;

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile applicationUpload) {
        this.fileUpload = applicationUpload;
    }

    public Object getLoginUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get("loginUser");
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Files getSelected() {
        return selected;
    }

    public void setSelected(Files selected) {
        this.selected = selected;
    }

    private FilesFacade getFacade() {
        return filesFacade;
    }

    public Files prepareCreate() {
        selected = new Files();

        return selected;
    }

    //上传文件时设置权限的选项
    private List<Authority> checkBoxList = new LinkedList<Authority>();

    public List getCheckBoxList() {
        return checkBoxList;
    }

    // object to bytearray  
    private byte[] objectToByte(java.lang.Object obj) {
        byte[] bytes = new byte[1024];
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return (bytes);
    }

//    // bytearray to object
//    private java.lang.Object byteToObject(byte[] bytes) {
//        java.lang.Object obj = new java.lang.Object();
//        try {      
//            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
//            ObjectInputStream oi = new ObjectInputStream(bi);
//            obj = oi.readObject();
//            bi.close();
//            oi.close();
//        } catch (Exception e) {
//            System.out.println("translation" + e.getMessage());
//            e.printStackTrace();
//        }
//        return obj;
//    }
    //上传文件
    public String submit() {
        try {
            //combiAuthority();

            //一开始selected没有new出来
            selected.setFileID(1);

            selected.setAuthority(objectToByte(checkBoxList));
//            //测试用
//            byte[] b = objectToByte(checkBoxList);
//            List<Authority> list = (List<Authority>)byteToObject(b);
            selected.setFileName(fileUpload.getFileName());

            //将文件传到服务器
            if (0 != fileUpload.getFileName().length()) {
                String fileUploadName = convertFileName(fileUpload);
                uploadFile(fileUpload, fileUploadName);

                //服务器上文件的路径
                selected.setFile(FILE_PATH + fileUploadName);

                Users user = (Users) getLoginUser();
                selected.setOwner(user);

                //持久化
                getFacade().create(selected);

                JsfUtil.addSuccessMessage("上传文件成功！");
            } else {
                JsfUtil.addErrorMessage("上传文件不能为空！");
            }
            return "/homePage.xhtml";

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    //这里的文件名是数据库中保存的文件名（时间）
    private void uploadFile(UploadedFile file, String fileName) throws IOException {
        if (file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

            try (InputStream in = file.getInputstream()) {
                setFileStream(in, (int) file.getSize(), fileName);
            }
        }
    }

    final String FILE_PATH = "F:/";

    private void setFileStream(InputStream in, int size, String fileName) throws FileNotFoundException, IOException {
        byte[] buf = new byte[size];
        in.read(buf);

        File destFile = new File(FILE_PATH, fileName);
        try (FileOutputStream downloadToDisk = new FileOutputStream(destFile)) {
            downloadToDisk.write(buf);
        }
    }

    //文件名转化为服务器上唯一的名字
    private String convertFileName(UploadedFile file) {
        if (file != null) {
            String fileType = getFileType(file);

            Random r = new Random();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // 上传操作是的日期和时间、精确到秒

            String randomGeneratedFileName = dateFormat.format(new java.util.Date()) + r.nextInt(10) + r.nextInt(10) + r.nextInt(10) + "." + fileType;

            return randomGeneratedFileName;
        } else {
            return null;
        }
    }

    //获取文件后缀名
    private String getFileType(UploadedFile file) {
        String originalFileName = file.getFileName();
        String fileType = originalFileName.substring(file.getFileName().lastIndexOf(".") + 1); // 提取文件的扩展名

        return fileType;
    }

}
