package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Delete.and;
import static org.tinygroup.tinysqldsl.Delete.delete;

/**
 * Created by luoguo on 2015/3/11.
 */
public class TestDelete {
    public static void main(String[] args) {
        System.out.println(delete(CUSTOM).where(CUSTOM.NAME.eq("悠然")));
        System.out.println(delete(CUSTOM).where(
                and(CUSTOM.NAME.leftLike("A"), CUSTOM.AGE.between(20, 30))));
    }
}
