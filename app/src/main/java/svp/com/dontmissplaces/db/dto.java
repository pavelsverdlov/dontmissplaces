package svp.com.dontmissplaces.db;

import java.io.Serializable;

public class dto implements Serializable {
    public final long id;

    public dto(long id) {
        this.id = id;
    }
}
