package io.ccw.toccer.backend.xpath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonToHtmlConverter {

	private JsonToHtmlConverter() {
	}

	public static String getHtmlData(String strJsonData) {
		final StringBuilder builder = new StringBuilder();
		builder.append("<html><head><title>converted</title></head><body>");
		builder.append(jsonToHtml(new JSONObject(strJsonData)));
		builder.append("</body></html>");
		return builder.toString();
	}

	private static String jsonToHtml(Object obj) {
		StringBuilder html = new StringBuilder();

		try {
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				String[] keys = JSONObject.getNames(jsonObject);

				html.append("<div class=\"json_object\">");

				if (keys != null && keys.length > 0) {
					for (String key : keys) {
						// print the key and open a DIV
						html.append("<div class=\"").append(key).append("\">");

						Object val = jsonObject.get(key);
						// recursive call
						html.append(jsonToHtml(val));
						// close the div
						html.append("</div>");
					}
				}

				html.append("</div>");

			} else if (obj instanceof JSONArray) {
				JSONArray array = (JSONArray) obj;
				for (int i = 0; i < array.length(); i++) {
					// recursive call
					html.append(jsonToHtml(array.get(i)));
				}
			} else {
				// print the value
				html.append(obj.toString().replaceAll("&", "&amp;").replaceAll("</italic>", "").replaceAll("<italic>", ""));
			}
		} catch (JSONException e) {
			return e.getLocalizedMessage();
		}

		return html.toString();
	}

}
