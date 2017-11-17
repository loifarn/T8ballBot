# T8ballBot

T8ballBot is a simplistic magic 8ball bot that utilizes twitter4j and the twitter API.
If tweeted to with "@yourTwitterAcount #8ball "Your question"" it will answer with a prediction.

## How to use

Tweet @bot with triggerphrase "#8ball 'your question'" and it will answer with a prediction


## Configuration

Setup / Fill in the follwing fields in the config.json file
```
  "LogPath": "/path/to/log.txt",
  "ConsumerKey": "",
  "ConsumerSecret": "",
  "AccessToken": "",
  "AccessTokenSecret": "",
```

## Logging
The program heavily relies on keeping a log of all the tweets it has answered (due to a weakness in the twitter API on time filtering). So make sure to always have the logfile available and regularly back it up!
