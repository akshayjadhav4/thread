package com.clone.threadclone.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Helpers {
    private Helpers() {
        throw new UnsupportedOperationException("Helpers class");
    }

    public static List<String> getAllHashtags(String input) {
        String regex = "#\\w+"; // Match hashtags starting with '#'
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        Set<String> uniqueHashtags = new HashSet<>(); // Use Set to filter duplicates
        while (matcher.find()) {
            uniqueHashtags.add(matcher.group());
        }
        return new ArrayList<>(uniqueHashtags); // Convert Set back to List
    }

}
