package entity;

import javax.persistence.*;

@Entity
@Table(name = "postleitzahlen", schema = "fluessiggascrawler", catalog = "")
public class PostleitzahlenEntity {
    private int id;
    private String plz;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PLZ")
    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostleitzahlenEntity that = (PostleitzahlenEntity) o;

        if (id != that.id) return false;
        if (plz != null ? !plz.equals(that.plz) : that.plz != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (plz != null ? plz.hashCode() : 0);
        return result;
    }
}
