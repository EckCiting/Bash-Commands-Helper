public class Commands {
	// You can define constants to represent the commands you often use
    protected static String stopxxx = "systemctl stop xxx";
    protected static String startxxx = "systemctl start xxx";
    protected static String restartxxx = "systemctl restart xxx";
    protected static String xxxStatus = "systemctl status xxx";
    protected static String changeJson(String filePath, String key, String newVal) {
        return "jq '" + key + " = $newVal' --arg newVal " + newVal + "' " + filePath + " > " + filePath + ".tmp && cp " + filePath + ".tmp " + filePath;
    }
	// key example: .outerKey.innerKey for nested keys
}