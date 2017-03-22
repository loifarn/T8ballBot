import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.*;

public class Logic extends TimerTask{
    private Twitter twitter;
    private TwitterFactory tf;
    private ConfigurationBuilder cb;

    private String list[] = {
            "It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it",
            "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again",
            "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again",
            "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful",
    };
    private ArrayList<String> replyLog = new ArrayList<>();
    private List<String> eightBall = Arrays.asList(list);
    private String triggerPhrase = "@TOracleBot #8ball";
    private String logPath;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    private boolean doIhaveNewTweet;

    public Logic() {
        initialize();
        
        cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        logRead(logPath, replyLog);
        askTheBall(twitter);
    }

    @Override
    public void run() {
        new Logic().askTheBall(twitter);
    }
    private void initialize(){
        String line;
        int counter = 0;
        try {
            FileReader fr = new FileReader("config.txt");
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                if (counter == 1) {
                    logPath = line.substring(10);
                }
                if (counter == 2) {
                    consumerKey = line.substring(14);
                }
                if (counter == 3) {
                    consumerSecret = line.substring(17);
                }
                if (counter == 4) {
                    accessToken = line.substring(14);
                }
                if (counter == 5) {
                    accessTokenSecret = line.substring(20);
                }
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String fileName = logPath;
        try {
            System.out.println("Attempting to write to log...");
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i=0; i<storage.size(); i++) {
                bw.write(""+storage.get(i));
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
        Query q = new Query(triggerPhrase);

        try {
            QueryResult result = tw.search(q.resultType(Query.ResultType.recent));
            for(Status s : result.getTweets()) {
                if (!replyLog.contains(Long.toString(s.getId()))){
                    doIhaveNewTweet = true;
                    System.out.println("New tweet: "+s.getText()+" from: "+s.getUser().getScreenName());
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
