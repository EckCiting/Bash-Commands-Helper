import com.jcraft.jsch.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Util {
    public static Session getSession(Remote remote) throws JSchException {
        JSch jSch = new JSch();
        if (Files.exists(Paths.get(remote.getIdentity()))) {
            jSch.addIdentity(remote.getIdentity(), remote.getPassphrase());
        }
        Session session = jSch.getSession(remote.getUser(), remote.getHost(),remote.getPort());
        session.setPassword(remote.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }
    public static List<String> remoteExecute(boolean sudo, Session session, String command) throws JSchException {
        //log.debug(">> {}", command);
        List<String> resultLines = new ArrayList<>();
        ChannelExec channel = null;
        try{
            channel = (ChannelExec) session.openChannel("exec");
            if(sudo)
                channel.setCommand("sudo -S -p '' "+command);
            InputStream input = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            channel.connect(Main.CONNECT_TIMEOUT);
            if(sudo) {
                out.write((Main.remote.getPassword() + "\n").getBytes());
                out.flush();
            }
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine = null;
                while((inputLine = inputReader.readLine()) != null) {
                    //log.debug("   {}", inputLine);
                    resultLines.add(inputLine);
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        //log.error("JSch inputStream close error:", e);
                        System.out.println("JSch inputStream close error:" + e);
                    }
                }
            }
        } catch (IOException e) {
            //log.error("IOcxecption:", e);
            System.out.println("IOcxecption:" + e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    //log.error("JSch channel disconnect error:", e);
                    System.out.println("JSch channel disconnect error:" + e);
                }
            }
        }
        return resultLines;
    }
    // selfInput is not tested
    public static void selfInput() throws JSchException {
            Scanner scanner = new Scanner(System.in);
            String kbinput ="";
            boolean sudo = false;
            while(!kbinput.equals("break")){
                if(scanner.hasNextLine()){
                    kbinput = scanner.nextLine();
                }
                if(kbinput.contains("sudo"))
                    sudo = true;
                System.out.println(remoteExecute(sudo,Main.session,kbinput));
            }
            scanner.close();
        }
}
