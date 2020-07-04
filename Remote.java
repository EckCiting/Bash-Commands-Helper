public class Remote {

    private String user = "root";
    private String host = "127.0.0.1";
    private int port = 22;
    private String password = "";
    private String identity = "~/.ssh/id_rsa";
    private String passphrase = "";
    protected void setUser(String user){
        this.user = user;
    }
    protected void setHost(String host){
        this.host = host;
    }
    protected void setPort(int port){
        this.port = port;
    }
    protected void setPassword(String password){
        this.password = password;
    }

    protected String getHost(){
        return host;
    }
    protected String getUser(){
        return user;
    }
    protected int getPort(){
        return port;
    }
    protected String getPassword(){
        return password;
    }
    protected String getIdentity() {
        return identity;
    }
    protected String getPassphrase() {
        return passphrase;
    }

}
