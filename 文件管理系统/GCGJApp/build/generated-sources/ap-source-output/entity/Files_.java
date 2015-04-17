package entity;

import entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-04-16T22:09:14")
@StaticMetamodel(Files.class)
public class Files_ { 

    public static volatile SingularAttribute<Files, Users> owner;
    public static volatile SingularAttribute<Files, String> fileName;
    public static volatile SingularAttribute<Files, String> file;
    public static volatile SingularAttribute<Files, byte[]> authority;
    public static volatile SingularAttribute<Files, Integer> fileID;

}