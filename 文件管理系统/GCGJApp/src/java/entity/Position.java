/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JiHongwei
 */
@Entity
@Table(name = "position")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p"),
    @NamedQuery(name = "Position.findByPosID", query = "SELECT p FROM Position p WHERE p.posID = :posID"),
    @NamedQuery(name = "Position.findByName", query = "SELECT p FROM Position p WHERE p.name = :name")})
public class Position implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PosID")
    private Integer posID;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "position")
    private Collection<Users> usersCollection;

    public Position() {
    }

    public Position(Integer posID) {
        this.posID = posID;
    }

    public Integer getPosID() {
        return posID;
    }

    public void setPosID(Integer posID) {
        this.posID = posID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (posID != null ? posID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.posID == null && other.posID != null) || (this.posID != null && !this.posID.equals(other.posID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Position[ posID=" + posID + " ]";
    }
    
}
