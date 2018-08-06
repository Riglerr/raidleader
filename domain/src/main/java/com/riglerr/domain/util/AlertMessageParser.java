package com.riglerr.domain.util;

import com.riglerr.domain.entities.Alert;
import com.riglerr.domain.entities.MessageContext;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlertMessageParser {
    public static Alert parse(MessageContext context) throws Exception {
        return parseMessage(context);
    }

    private static Alert parseMessage(MessageContext context) throws Exception {
        var text = context.getText();
        var argsKvP = splitAndGatherArguments(text);
        verifyArguments(argsKvP);
        LocalTime time = getTime(argsKvP);
        long interval = getInterval(argsKvP);
        String message = getMessage(argsKvP);
        return new Alert(time, interval, message, context.getGuidId(), context.getChannelId());
    }

    private static void verifyArguments(HashMap<String, List<String>> argsKvP) throws Exception {
        for (String key: argsKvP.keySet()
             ) {
            if (!key.equals("-t") && !key.equals("-i") && !key.equals("-m")) {
                throw new Exception(String.format("Invalid Option '%s'", key));
            }
        }
    }

    // Todo: Enable quoted arguments / swap with cli-library
    private static HashMap<String, List<String>> splitAndGatherArguments(String text) {
        HashMap<String, List<String>> optionsKvP = new HashMap<>();
        var args = text.split(" ");
        String lastOption = null ;
        var quotesFlag = false;
        for (String arg : args) {
            if (isValidOption(quotesFlag, arg) && !optionsKvP.containsKey(arg)) {
                optionsKvP.put(arg, new ArrayList<>());
                lastOption = arg;
            } else if (lastOption != null) {
                var list = optionsKvP.get(lastOption);
                optionsKvP.get(lastOption).add(arg);
            }
        }
        return optionsKvP;
    }

    private static boolean isValidOption(boolean quotesFlag, String arg) {
        return arg.startsWith("-") && !quotesFlag;
    }

    private static LocalTime getTime(HashMap<String, List<String>> argsKvP) throws Exception {
        if (argsKvP.containsKey("-t")) {
            List<String> argParams = argsKvP.get("-t");
            if (argParams.isEmpty()) {
                throw new Exception("Time is null");
            } else if (argParams.size() > 1) {
                throw new Exception("Time has too many arguments.");
            }
            return LocalTime.parse(argsKvP.get("-t").get(0));
        }
        throw new Exception("Time not specified.");
    }

    private static long getInterval(HashMap<String, List<String>> argsKvP) throws Exception {
        if (argsKvP.containsKey("-i")) {
            List<String> argParamList = argsKvP.get("-i");
            if (argParamList.isEmpty()) {
                return 0;
            } else if (argParamList.size() > 1) {
                throw new Exception("Too many arguments for interval.");
            }
            return Duration.parse(argsKvP.get("-i").get(0)).getSeconds();
        } else {
            return 0;
        }
    }

    private static String getMessage(HashMap<String, List<String>> argsKvP) throws Exception {
        if (argsKvP.containsKey("-m")) {
            List<String> argParamList = argsKvP.get("-m");
            var sb = new StringBuilder();
            if (argParamList.size() == 0) {
                throw new Exception("Message value null or empty.");
            }
            for (var i = 0; i < argParamList.size(); i++) {
                sb.append(argParamList.get(i));
                if (i < argParamList.size() - 1) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        } else {
            throw new Exception("Message argument not found.");
        }
    }
}
