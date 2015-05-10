package org.tinygroup.validate;

import org.tinygroup.validate.annotation.Field;
import org.tinygroup.validate.annotation.Size;
import org.tinygroup.validate.annotation.Validation;

@Validation(name="user")
public class UserBean {
    @Size(min=2,max=3)
    @Field(title="用户名")
    private String userName;

    @Size(min=4,max=5)
    @Field(title="密码")
    private String passwd;

    @Size(min=6,max=7)
    @Field(title="主键")
    private String obid;

    public String getObid() {
        return obid;
    }

    public void setObid(String obid) {
        this.obid = obid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
