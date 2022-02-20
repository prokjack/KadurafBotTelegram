package com.j2ck.dict;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

public class HebrewParser {
    private final Set<Binyan> binyansSet;

    HebrewParser() {
        binyansSet = Arrays.stream(Binyan.values()).collect(Collectors.toSet());
    }

    List<Result> parse(String root) throws IOException {
        if (root.length() != 3) {
            System.out.println("Root can be only from 3 letters for now");
            return null;
        }

        String url = "https://www.pealim.com/ru/dict/?pos=verb&num-radicals=all&rf=" + URLEncoder.encode(root.charAt(2) + "", "UTF-8") + "&r2=" + URLEncoder.encode(root.charAt(1) + "", "UTF-8") + "&r1=" + URLEncoder.encode(root.charAt(0) + "", "UTF-8");
        Document doc = Jsoup.connect(url).get();
        Elements trElementsTable = doc.getElementsByClass("table table-hover dict-table-t").select("tr");

        List<Result> results = new ArrayList<>();

        for (int i = 1; i < trElementsTable.size(); i++) {
            Element element = trElementsTable.get(i);
            String href = element.select("a").first().attributes().get("href");
            String binyanUrl = "https://www.pealim.com" + href;
            Document docInt = Jsoup.connect(binyanUrl).get();
            Result result = new Result();
            result.setTranslation(docInt.getElementsByClass("lead").first().text());
            String text = docInt.getElementsByClass("container").get(1).select("b").first().text();

            result.setBinyan(text);
            Element element1 = docInt.select("table").get(0).select("tbody").get(0);
            result.setInfinitiv(Optional.ofNullable(element1.getElementById("INF-L")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPresent1(Optional.ofNullable(element1.getElementById("AP-ms")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPresent2(Optional.ofNullable(element1.getElementById("AP-fs")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPast1(Optional.ofNullable(element1.getElementById("PERF-1s")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPast2(Optional.ofNullable(element1.getElementById("PERF-3ms")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setFuture1(Optional.ofNullable(element1.getElementById("IMPF-1s")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setFuture2(Optional.ofNullable(element1.getElementById("IMPF-3ms")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            results.add(result);
        }
        return results;
    }
}
