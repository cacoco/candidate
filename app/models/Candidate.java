package models;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.Type;
import play.db.jpa.*;

@Entity
@Table(name="candidate")
public class Candidate extends Model {

    public String name;
    public String email;
    @Lob @Type(type = "org.hibernate.type.TextType")
    public String about;
    public String urls;

    public Date modifiedon;

    public Candidate(String name, String email, String about, String urls) {
        this.urls = urls;
        this.about = about;
        this.email = email;
        this.name = name;
        this.modifiedon = new Date();
    }
}
