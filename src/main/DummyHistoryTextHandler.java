package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.List;

public class DummyHistoryTextHandler extends NgordnetQueryHandler {
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int start = q.start();
        int end = q.end();

        String response = "You entered the following info into the browser:\n";
        response += "Words: " + q.words() + "\n";
        response += "Start Year: " + q.start() + "\n";
        response += "End Year: " + q.end() + "\n";
        return response;
    }
}
