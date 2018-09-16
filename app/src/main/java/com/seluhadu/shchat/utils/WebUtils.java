package com.seluhadu.shchat.utils;import android.os.AsyncTask;import android.util.Patterns;import com.seluhadu.shchat.models.UrlPreview;import org.jsoup.Jsoup;import org.jsoup.nodes.Document;import org.jsoup.nodes.Element;import org.jsoup.select.Elements;import java.io.IOException;import java.util.ArrayList;import java.util.Hashtable;import java.util.List;import java.util.regex.Pattern;public class WebUtils {    private WebUtils() {    }    public static List<String> extractUrls(String url) {        List<String> result = new ArrayList<>();        String[] words = url.split("\\S+");        Pattern pattern = Patterns.WEB_URL;        for (String word : words) {            if (pattern.matcher(word).find()) {                if (!word.toLowerCase().contains("http://") && !word.toLowerCase().contains("https://")) {                    word = "http://" + word;                }                result.add(word);            }        }        return result;    }    public static abstract class UrlPreviewAsyncTask extends AsyncTask<String, Void, UrlPreview> {        @Override        protected abstract void onPostExecute(UrlPreview urlPreview);        @Override        protected UrlPreview doInBackground(String... strings) {            Hashtable<String, String> result = new Hashtable<>();            String url = strings[0];            Document document = null;            try {                document = Jsoup.connect(url).followRedirects(true).timeout(10 * 1000).get();                Elements ogTags = document.select("meta[property^=og:]");                for (int f = 0; f < ogTags.size(); f++) {                    Element tag = ogTags.get(f);                    String text = tag.attr("property");                    if ("og:image".equals(text)) {                        result.put("image", tag.attr("content"));                    } else if ("og:url".equals(text)) {                        result.put("url", tag.attr("content"));                    } else if ("og:description".equals(text)) {                        result.put("description", tag.attr("content"));                    } else if ("og:site_name".equals(text)) {                        result.put("name", tag.attr("content"));                    } else if ("og:title".equals(text)) {                        result.put("title", tag.attr("content"));                    }                }                ogTags = document.select("meta[property^=twitter:]");                for (int t = 0; t < ogTags.size(); t++) {                    Element tag = ogTags.get(t);                    String text = tag.attr("property");                    if ("twitter:url".equals(text)) {                        if (!result.containsKey("url")) {                            result.put("url", tag.attr("content"));                        }                    } else if ("twitter:image".equals(text)) {                        if (!result.containsKey("image")) {                            result.put("image", tag.attr("content"));                        }                    } else if ("twitter:description".equals(text)) {                        if (!result.containsKey("description")) {                            result.put("description", tag.attr("content"));                        }                    } else if ("twitter:title".equals(text)) {                        if (!result.containsKey("title")) {                            result.put("title", tag.attr("content"));                        }                    } else if ("twitter:site".equals(text)) {                        if (!result.containsKey("name")) {                            result.put("name", tag.attr("content"));                        }                    }                }                if (!result.containsKey("name") && result.containsKey("title")) {                    result.put("name", result.get("title"));                }                if (!result.containsKey("url")) {                    result.put("url", url);                }                if (result.get("image") != null && result.get("image").startsWith("//")) {                    result.put("image", "http:" + result.get("image"));                }                if (result.get("url") != null && result.get("url").startsWith("//")) {                    result.put("url", "http:" + result.get("url"));                }                if (result.keySet().size() == 5) {                    return new UrlPreview(                            result.get("url"),                            result.get("name"),                            result.get("title"),                            result.get("description"),                            result.get("imageUrl"));                }            } catch (IOException e) {                e.printStackTrace();            }            return null;        }    }}