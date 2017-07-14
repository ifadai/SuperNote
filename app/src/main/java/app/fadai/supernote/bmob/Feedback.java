package app.fadai.supernote.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by miaoyongyong on 2017/2/20.
 */

public class Feedback extends BmobObject{
    private String contact=new String();
    private String content=new String();
    private int sdk;
    private String version;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getSdk() {
        return sdk;
    }

    public void setSdk(int sdk) {
        this.sdk = sdk;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
