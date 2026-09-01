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

    public static String digitsOnly(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\\D", "");
    }

    // Remove espacos em branco no inicio/fim, preservando null (nao confundir com
    // "obrigatorio" — quem decide isso e a validacao de cada campo).
    public static String trim(String text) {
        return text == null ? null : text.trim();
    }

    // Capitaliza somente o primeiro caractere, preservando o resto do texto como
    // foi digitado (diferente de toTitleCase, que capitaliza cada palavra e forca
    // o restante para minusculo).
    public static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
