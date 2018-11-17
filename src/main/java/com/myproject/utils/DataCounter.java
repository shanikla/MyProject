package com.myproject.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.stream.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

public class DataCounter {

    private final Logger log = LoggerFactory.getLogger(DataCounter.class);


    private static DataCounter instance = new DataCounter();
    private final ObjectMapper mapper = new ObjectMapper();
    private final HashMap<String, Long> eventTypeCounter = new HashMap<>();
    private final HashMap<String, Long> eventWordCounter = new HashMap<>();


    public static synchronized DataCounter getInstance() {
        if (instance == null) {
            instance = new DataCounter();
        }
        return instance;
    }

    private DataCounter() {
    }

    /**
     * handle an incoming event
     * parse json and handle counting of words and event types.
     *
     * @param eventString String. event string
     */
    public void handleEvent(final String eventString) {
        try {
            Event event = mapper.readValue(eventString, Event.class);
            countEvents(event);
        } catch (IOException e) {
            // couldn't parse the event - ignore
            log.debug("could not parse json string", e);
        }
    }

    private void countEvents(final Event event) {
        long countEventType = eventTypeCounter.getOrDefault(event.getEventType(), 0L);
        eventTypeCounter.put(event.getEventType(), ++countEventType);
        for (String word : event.getData().split(" ")) {
            long countWord = eventWordCounter.getOrDefault(word, 0L);
            eventWordCounter.put(word, ++countWord);
        }
    }

    /**
     * create json response for event type count
     *
     * @return JsonResponse.
     */
    public JsonResponse getEventTypeCount() {
        return getCountersByMap(eventTypeCounter);
    }

    /**
     * create json response for event words count
     *
     * @return JsonResponse.
     */
    public JsonResponse getEventWordCount() {
        return getCountersByMap(eventWordCounter);
    }

    private JsonResponse getCountersByMap(HashMap<String, Long> map) {
        JsonResponse jsonResponse = new JsonResponse();
        for (String eventType : map.keySet()) {
            jsonResponse.addCount(eventType, map.get(eventType));
        }
        return jsonResponse;
    }
}
