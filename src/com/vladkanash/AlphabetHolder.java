package com.vladkanash;

import java.util.stream.Collectors;

/**
 * Created by vladkanash on 21.12.16.
 */
class AlphabetHolder {

    private final String alphabet;
    private final int size;
    private final int step;

    AlphabetHolder(final String charSet) {
        if (charSet == null ||
            charSet.isEmpty() ||
            charSet.equals(Constants.DEFAULT_ALPHABET)) {

            this.alphabet = Constants.DEFAULT_ALPHABET;
        } else if (charSet.length() == 1) {
            this.alphabet = createAlphabet(charSet + " ");
        } else {
            this.alphabet = createAlphabet(charSet);
        }
        this.size = alphabet.length();
        this.step = 255 / size;
    }

    AlphabetHolder() {
        this.alphabet = Constants.DEFAULT_ALPHABET;
        this.size = alphabet.length();
        this.step = 255 / size;
    }

    private String createAlphabet(String charSet) {
        return Constants.DEFAULT_ALPHABET.chars()
                .filter(e -> charSet.indexOf(e) != -1)
                .mapToObj(e -> Character.toString((char) e))
                .collect(Collectors.joining());
    }

    String getAlphabet() {
        return alphabet;
    }

    int getSize() {
        return size;
    }

    int getStep() {
        return step;
    }
}
