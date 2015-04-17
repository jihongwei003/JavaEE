/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JiHongwei
 */
@Entity
@Table(name = "files")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Files.findAll", query = "SELECT f FROM Files f"),
    @NamedQuery(name = "Files.findByFileID", query = "SELECT f FROM Files f WHERE f.fileID = :fileID"),
    @NamedQuery(name = "Files.findByFile", query = "SELECT f FROM Files f WHERE f.file = :file"),
    @NamedQuery(name = "Files.findByFileName", query = "SELECT f FROM Files f WHERE f.fileName = :fileName")})
public class Files implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fileID")
    private Integer fileID;
    @Size(max = 45)
    @Column(name = "file")
    private String file;
    @Lob
    @Column(name = "authority")
    private byte[] authority;
    @Size(max = 45)
    @Column(name = "fileName")
    private String fileName;
    @JoinColumn(name = "owner", referencedColumnName = "userID")
    @ManyToOne
    private Users owner;

    public Files() {
    }

    public Files(Integer fileID) {
        this.fileID = fileID;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public byte[] getAuthority() {
        return authority;
    }

    public void setAuthority(byte[] authority) {
        this.authority = authority;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Users getOwnerObj() {
        return owner;
    }
    
    //在列表中显示名字而不是ID
    public String getOwner() {
        return owner.getUserName();
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileID != null ? fileID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Files)) {
            return false;
        }
        Files other = (Files) object;
        if ((this.fileID == null && other.fileID != null) || (this.fileID != null && !this.fileID.equals(other.fileID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Files[ fileID=" + fileID + " ]";
    }
    
}
