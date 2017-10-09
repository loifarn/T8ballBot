import com.google.gson.Gson;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.*;

public class Logic extends TimerTask {
    private Twitter twitter;
    private TwitterFactory tf;
    private ConfigurationBuilder cb;

    private ArrayList<String> replyLog = new ArrayList<>();
    private List<String> eightBall;
    private boolean doIhaveNewTweet;

    public Logic() {
        initialize();
        Config config = Config.getConfig();
        eightBall = Arrays.asList(config.getReplies());
        cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(config.getConsumerKey());
        cb.setOAuthConsumerSecret(config.getConsumerSecret());
        cb.setOAuthAccessToken(config.getAccessToken());
        cb.setOAuthAccessTokenSecret(config.getAccessTokenSecret());
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        logRead(config.getLogPath(), replyLog);
        askTheBall(twitter);
    }

    @Override
    public void run() {
        new Logic().askTheBall(twitter);
    }

    private boolean initialize() {
        Gson gson = new Gson();
        if (!new File("config.json").exists()) {
            if (FileManager.getInstance().writeToFile("config.json", gson.toJson(Config.getConfig()))) {
                System.out.println("Please fill out the config file that was generated and restart the bot");
                return false;
            }
        } else {
            String config = FileManager.getInstance().readFromFile("config.json");
            if (config != null) {
                Config.setConfig(gson.fromJson(config, Config.class));
                return true;
            }
        }
        return false;
    }

    private void logRead(String logFile, ArrayList storage) {
        String line = null;
        int placeInStorage = 0;
        try {
            FileReader fr = new FileReader(logFile);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                storage.add(placeInStorage, line);
                placeInStorage++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File '" + logFile + "' not found");
        } catch (IOException e) {
            System.out.println("Error reading file '" + logFile + "'");
            e.printStackTrace();
        }

    }

    private void logWrite(ArrayList storage) {
        String fileName = Config.getConfig().getLogPath();
        try {
            System.out.println("Attempting to write to log...");
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < storage.size(); i++) {
                bw.write("" + storage.get(i));
                bw.newLine();
            }
            bw.close();
            System.out.println("Write Successful.");
        } catch (IOException e) {
            System.out.println("Error writing to log '" + fileName + "'");
            e.printStackTrace();
        }
    }

    private void askTheBall(Twitter tw) {
        Random r = new Random();
        Query q = new Query(Config.getConfig().getTriggerPhrase());

        try {
            QueryResult result = tw.search(q.resultType(Query.ResultType.recent));
            for (Status s : result.getTweets()) {
                if (!replyLog.contains(Long.toString(s.getId()))) {
                    doIhaveNewTweet = true;
                    System.out.println("New tweet: " + s.getText() + " from: " + s.getUser().getScreenName());
                    System.out.println("Replied to tweet: '" + s.getId() + " from user: @" + s.getUser().getScreenName());
                    reply(s.getId(), "@" + s.getUser().getScreenName() + " : " + eightBall.get(r.nextInt(19)), tw);
                    replyLog.add(Long.toString(s.getId()));
                }
            }
            if (doIhaveNewTweet) {
                logWrite(replyLog);
            }
            doIhaveNewTweet = false;
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

    private static void reply(long inReplyToStatusId, String text, Twitter tw) throws TwitterException {
        StatusUpdate statusUpdate = new StatusUpdate(text);
        statusUpdate.setInReplyToStatusId(inReplyToStatusId);
        tw.updateStatus(statusUpdate);
    }
}
