package com.arise.chat.util;

import java.util.List;

public class WelcomeMessageFactory {

    private static final List<String> messages = List.of(
            "Welcome to the server %s, hope you brought snacks",
            "Uh oh... %s has arrived!! Act cool, everyone",
            "Someone get the confetti, %s is here!",
            "Is it a bird? Is it a plane? No!! It's %s",
            "Look what the cat dragged in! it's %s",
            "Welcome to the chat dojo, %s! Show us your best typing skills"

    );

    public static String getMessage(int index){
        return messages.get(index);
    }
}
