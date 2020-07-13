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
            else
                channel.setCommand(command);
            InputStream input = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            channel.connect(Main.CONNECT_TIMEOUT);
            if(sudo) {
                // Did not consider the case if the password is empty
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

    public static void remoteInteractShell(Session session) throws JSchException {
        try {
            Channel channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setInputStream(new FilterInputStream(System.in) {
                public int read(byte[] b, int off, int len) throws IOException {
                    return in.read(b, off, (len > 1024 ? 1024 : len));
                }
            });
            channel.setOutputStream(System.out);
            channel.connect(3000);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static void remoteExecuteShell(Session session) throws JSchException {
        try {
            Channel channel = session.openChannel("shell");
            String cmd = "xxx"; // your own command
            cmd+=" \n";
            InputStream in = new ByteArrayInputStream(cmd.getBytes("UTF-8"));
            channel.setInputStream(in);

            channel.setOutputStream(System.out);
            channel.connect(3000);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
