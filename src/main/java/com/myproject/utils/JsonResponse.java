package com.myproject.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JsonResponse {

    private List<Pair> counts = new ArrayList<>();

    public JsonResponse() {
    }

    public JsonResponse(List<Pair> counts) {
        this.counts = counts;
    }

    public List<Pair> getCounts() {
        return counts;
    }

    public void addCount(final String name, final Long count) {
        counts.add(new Pair(name, count));
    }

    private static class Pair {

        private String key;
        private Long count;

        public Pair() {
        }

        Pair(final String key, final Long count) {
            this.key = key;
            this.count = count;
        }

        public String getKey() {
            return key;
        }

        public Long getCount() {
            return count;
        }

    }

    @Override
    public String toString() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }
}
