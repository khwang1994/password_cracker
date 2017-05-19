public class User{
	String salt;
	String ePassword;
	String ptPassword; // plaintext password
	String name;
	boolean cracked;

	public User(String salt, String ep, String name){
		this.salt = salt;
		ePassword = ep;
		this.name = name;
		cracked = false;
	}

	public void setCracked(boolean x){
		cracked = x;
	}
	
	public void setPW(String pw){
		ptPassword = pw;
	}

}
