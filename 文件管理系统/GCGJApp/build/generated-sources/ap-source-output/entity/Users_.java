package entity;

import entity.Files;
import entity.Position;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-04-16T22:09:14")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile CollectionAttribute<Users, Files> filesCollection;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, Position> position;
    public static volatile SingularAttribute<Users, String> userName;
    public static volatile SingularAttribute<Users, Integer> userID;

}