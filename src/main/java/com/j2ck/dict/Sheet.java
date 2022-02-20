package com.j2ck.dict;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sheet {
    private final String APPLICATION_NAME = "Google Sheets API Dict";
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final GoogleCredentials credentials;

    public Sheet(GoogleCredentials credentials) {
        this.credentials = credentials;
    }

    void write(String rootWord, List<Result> parseResult) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1CM8m8RDm5VA1nPM_c8dEINYr0Mxx3GKBzM-V6AmQj_w";

        final String range = "verbs!A1:AV";

        credentials.refresh();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new GoogleCredential().setAccessToken(credentials.getAccessToken().getTokenValue()))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> currentValues = response.getValues();
        if (isAlreadyExist(parseResult, currentValues)) {
            System.out.println("Word already exist");
            return;
        }

        int sizeOfExistingTable = currentValues.size();
        final String rangeSet = "verbs!A" + (sizeOfExistingTable + 1) + ":BV";

        List<List<Object>> values = getValuesFromParseResult(rootWord, parseResult);
        ValueRange body = new ValueRange()
                .setValues(values);
        UpdateValuesResponse result =
                service.spreadsheets().values().update(spreadsheetId, rangeSet, body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
    }

    private boolean isAlreadyExist(List<Result> parseResult, List<List<Object>> currentValues) {
        String wordToCheck = parseResult.get(0).getInfinitiv();
        return currentValues.stream()
                .anyMatch(l -> l.stream().filter(o -> o instanceof String)
                        .anyMatch(obj -> Objects.equals(wordToCheck, obj.toString())));
    }

    private List<List<Object>> getValuesFromParseResult(String rootWord, List<Result> parseResult) {

        Map<Binyan, Result> collect = parseResult.stream().filter(result -> Objects.nonNull(result.getBinyan()))
                .filter(distinctByKey((r) -> Binyan.fromString(r.getBinyan())))
                .collect(Collectors.toMap(r -> Binyan.fromString(r.getBinyan()), r -> r));

        ArrayList<Object> strings = new ArrayList<>();
        strings.add(Optional.ofNullable(rootWord).orElse(""));
        strings = addBinyanToResultList(Binyan.PAAL, collect, strings);
        strings = addBinyanToResultList(Binyan.PIEL, collect, strings);
        strings = addBinyanToResultList(Binyan.PUAL, collect, strings);
        strings = addBinyanToResultList(Binyan.HITPAEL, collect, strings);
        strings = addBinyanToResultList(Binyan.NIFAL, collect, strings);
        strings = addBinyanToResultList(Binyan.HIFIL, collect, strings);
        strings = addBinyanToResultList(Binyan.HUFAL, collect, strings);


        return Arrays.asList(strings);
        // Additional rows ...
    }

    private ArrayList<Object> addBinyanToResultList(Binyan binyan, Map<Binyan, Result> collect, ArrayList<Object> strings) {
        if (collect.keySet().contains(binyan)) {
            Result result = collect.get(binyan);
            strings.add(Optional.ofNullable(result.getPresent2()).orElse(""));
            strings.add(Optional.ofNullable(result.getPresent1()).orElse(""));
            strings.add(Optional.ofNullable(result.getPast2()).orElse(""));
            strings.add(Optional.ofNullable(result.getPast1()).orElse(""));
            strings.add(Optional.ofNullable(result.getFuture2()).orElse(""));
            strings.add(Optional.ofNullable(result.getFuture1()).orElse(""));
            strings.add(Optional.ofNullable(result.getInfinitiv()).orElse(""));
            strings.add(Optional.ofNullable(result.getTranslation()).orElse(""));
        } else {
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
        }

        return strings;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}