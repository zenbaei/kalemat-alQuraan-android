package org.zenbaei.kalematAlQuraan.utils;

/**
 * Created by zenbaei on 4/3/17.
 */

public class ArabicUtils {

    private static final String[] searchList = {"\u0610", "\u0611", "\u0612", "\u0613",
            "\u0614", "\u0615", "\u0616", "\u0617", "\u0618", "\u0619", "\u061A", "\u06D6", "\u06D7",
            "\u06D8", "\u06D9", "\u06DA", "\u06DB", "\u06DC", "\u06DD", "\u06DE", "\u06DF", "\u06E0",
            "\u06E1", "\u06E2", "\u06E3", "\u06E4", "\u06E5", "\u06E6", "\u06E7", "\u06E8", "\u06E9",
            "\u06EA", "\u06EB", "\u06EC", "\u06ED", "\u0640", "\u064B", "\u064C", "\u064D", "\u064E",
            "\u064F", "\u0650", "\u0651", "\u0652", "\u0653", "\u0654", "\u0655", "\u0656", "\u0657",
            "\u0658", "\u0659", "\u065A", "\u065B", "\u065C", "\u065D", "\u065E", "\u065F", "\u0670"};


    private static final String EMPTY_STRING = "";

    public static String normalize(String input) {

        for (final String s : searchList) {
            if (input.contains(s)) {
                input = input.replace(s, EMPTY_STRING);
            }
        }

        return input;
    }
}
