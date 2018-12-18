/**
 * @author Tom
 * 2018.12.14
 * 用户名，密码，匿名的显示
 */
package bean;

public class User {
	private String name;
	private String password;
	private int id;
	
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //只显示开头一个字母 其余显示*
    public String getAnonymousName() {
    	if(name==null)
    		return null;
    	if(name.length()<=1)
    		return "*";
    	if(name.length()==2)
    		return name.substring(0, 1)+"*";
    	char[] cs=name.toCharArray();
    	for(int i=1;i<cs.length-1;i++) {
    		cs[i]='*';
    	}
    	return new String(cs);
    }
}
