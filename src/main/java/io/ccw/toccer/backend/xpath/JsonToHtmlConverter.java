package io.ccw.toccer.backend.xpath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Converts an arbitrary JSON payload into a simple HTML page.
 *
 * <p>
 * The resulting HTML is parseable by TagSoup/Saxon and can be queried with the
 * same XPath expressions used for regular HTML pages:
 * </p>
 * <ul>
 * <li>{@code JSONObject} → a {@code <div class="json_object">} that wraps one
 * child div per key.</li>
 * <li>{@code JSONArray} → the elements rendered in sequence.</li>
 * <li>Scalars → their string value.</li>
 * </ul>
 *
 * <p>
 * The conversion is intentionally minimal — only {@code &amp;} is escaped and
 * the {@code <italic>}/{@code </italic>} marker used by some feeds is stripped.
 * Downstream {@link io.ccw.toccer.backend.toc.TocEntry} performs a triple
 * {@code unescapeHtml4} to restore the human-readable text.
 * </p>
 */
public final class JsonToHtmlConverter {

    private JsonToHtmlConverter() {
    }

    /**
     * Converts the given JSON document into an HTML page.
     *
     * @param strJsonData a JSON object payload
     * @return the equivalent HTML wrapped in a minimal {@code <html>} document
     * @throws IllegalArgumentException if the payload is not valid JSON
     */
    public static String getHtmlData(String strJsonData) {
        final StringBuilder builder = new StringBuilder();
        try {
            builder.append("<html><head><title>converted</title></head><body>");
            jsonToHtml(new JSONObject(strJsonData), builder);
            builder.append("</body></html>");
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON payload", e);
        }
        return builder.toString();
    }

    /**
     * Recursively converts a JSON value into HTML.
     *
     * @param obj the JSON value (object, array or scalar)
     * @param out target builder
     */
    private static void jsonToHtml(Object obj, StringBuilder out) {
        if (obj instanceof JSONObject) {
            final JSONObject jsonObject = (JSONObject) obj;
            final Object[] names = JSONObject.getNames(jsonObject);
            if (names != null && names.length > 0) {
                out.append("<div class=\"json_object\">");
                for (final Object name : names) {
                    final String key = (String) name;
                    out.append("<div class=\"").append(key).append("\">");
                    jsonToHtml(jsonObject.get(key), out);
                    out.append("</div>");
                }
                out.append("</div>");
            }
        } else if (obj instanceof JSONArray) {
            final JSONArray array = (JSONArray) obj;
            for (int i = 0; i < array.length(); i++) {
                jsonToHtml(array.get(i), out);
            }
        } else {
            // Escape '&' so the output is safe inside an HTML element. The
            // leading/trailing <italic> markers used by some feeds are removed
            // here because they were intended only as visual decoration.
            final String value = obj.toString()
                    .replaceAll("</?italic>", "")
                    .replaceAll("&", "&amp;");
            out.append(value);
        }
    }
}
