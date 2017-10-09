public class Config {
    private static Config configInst = new Config();

    private String LogPath = "";

    private String ConsumerKey = "";

    private String ConsumerSecret = "";

    private String AccessToken = "";

    private String AccessTokenSecret = "";

    private String Replies[] = new String[]{};

    public static Config getConfig() {
        return configInst;
    }

    public static void setConfig(Config config) {
        configInst = config;
    }

    public String getLogPath() {
        return LogPath;
    }

    public void setLogPath(String logPath) {
        LogPath = logPath;
    }

    public String getConsumerKey() {
        return ConsumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        ConsumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return ConsumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        ConsumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return AccessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        AccessTokenSecret = accessTokenSecret;
    }

    public String[] getReplies() {
        return Replies;
    }

    public void setReplies(String[] replies) {
        Replies = replies;
    }

    public String getTriggerPhrase() {
        return TriggerPhrase;
    }

    public void setTriggerPhrase(String triggerPhrase) {
        TriggerPhrase = triggerPhrase;
    }

    private String TriggerPhrase = "";

}
