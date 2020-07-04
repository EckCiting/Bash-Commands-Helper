import com.jcraft.jsch.Session;
import java.util.Scanner;

public class Main {
    protected static int CONNECT_TIMEOUT = 1000;
    protected static Remote remote = new Remote();
    protected static Session session;
    public static void main(String[] args) throws Exception {
        remote.setHost("your host");
        remote.setPassword("your password");
        remote.setPort(22);
        remote.setUser("user name");
        session = Util.getSession(remote);
        session.connect(CONNECT_TIMEOUT);
        if (session.isConnected()) {
            System.out.println("Host( " + remote.getHost()+" ) connected.");
        }
        //Util.selfInput(); // Note: it cannot enter other directories
        
		//Util.remoteExecute(false,session,Commands.changeJson("test.json",".keys.key1","haha"));
        
		Util.remoteExecute(true,session,Commands.startxxx); 
		// The first parameter indicates whether the command uses "sudo".
		// The therd parameter is a kind of shortcut. User can use a constant to indicate the desired command.
        session.disconnect();
    }

}
