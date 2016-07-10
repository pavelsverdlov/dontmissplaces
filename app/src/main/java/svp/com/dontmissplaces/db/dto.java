package svp.com.dontmissplaces.db;

import java.io.Serializable;

public class dto implements Serializable {
    public long id;

    public dto(long id) {
        this.id = id;
    }

    public boolean isStored() {
        return id != -1;
    }
}
