package entity;

import entity.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-04-16T22:09:14")
@StaticMetamodel(Position.class)
public class Position_ { 

    public static volatile SingularAttribute<Position, Integer> posID;
    public static volatile SingularAttribute<Position, String> name;
    public static volatile CollectionAttribute<Position, Users> usersCollection;

}