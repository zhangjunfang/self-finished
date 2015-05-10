package org.tinygroup.tinysqldsl;

import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.base.Table;

/**
 * Created by luoguo on 2015/3/11.
 */
public class NewTable extends Table {
    public static final NewTable NEWTABLE = new NewTable();
    public final Column ID = new Column(this, "id");
    public final Column NAME = new Column(this, "name");
    public final Column AGE = new Column(this, "age");

    private NewTable() {
        super("newtable");
    }
}
