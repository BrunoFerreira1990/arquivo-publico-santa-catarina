package com.example.apesc.util;

public class CommonUtils {

    private CommonUtils() {
        // Construtor privado para esconder o implícito público padrão
    }

    public static String toTitleCase(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        
        String[] words = text.trim().split("\\s+");
        StringBuilder formatted = new StringBuilder();
        
        for (String word : words) {
            formatted.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                formatted.append(word.substring(1).toLowerCase());
            }
            formatted.append(" ");
        }
        
        return formatted.toString().trim();
    }

    public static String toUpperCaseSafe(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        return text.trim().toUpperCase();
    }
}
