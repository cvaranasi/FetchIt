package com.cv.fetchit.api;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("/fetchit")
public class FetchIt {
	private static final String JSON_URL = "https://raw.githubusercontent.com/dconnolly/chromecast-backgrounds/"
			+ "master/backgrounds.json";
	static Logger log = Logger.getLogger(FetchIt.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/complete")
	public String fetchProducts() throws JSONException, IOException, ParseException {

		String bgJson = IOUtils.toString(new URL(JSON_URL).openStream());
		JSONArray jsonArr = (JSONArray) JSONValue.parseWithException(bgJson);
		return jsonArr.toJSONString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/author/{author}")
	public String fetchProjectsByAuthor(@PathParam("author") String author)
			throws JSONException, IOException, ParseException {
		JSONArray retVal = new JSONArray();
		String bgJson = IOUtils.toString(new URL(JSON_URL).openStream());
		JSONArray jsonArr = (JSONArray) new JSONParser().parse(bgJson);
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArr.get(i);
			String auth = (String) jsonObject.get("author");
			if (auth != null && auth.contains(author)) {
				retVal.add(jsonObject);
			}
		}

		return retVal.toJSONString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/image/{image}")
	public String fetchProjectsByImageName(@PathParam("image") String image)
			throws JSONException, IOException, ParseException {
		JSONArray retVal = new JSONArray();
		String bgJson = IOUtils.toString(new URL(JSON_URL).openStream());
		JSONArray jsonArr = (JSONArray) new JSONParser().parse(bgJson);
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArr.get(i);
			String url = (String) jsonObject.get("url");
			if (url != null && url.contains(image)) {
				retVal.add(jsonObject);
			}
		}

		return retVal.toJSONString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allAuthors")
	public String fetchAllAuthors() throws JSONException, IOException, ParseException {
		JSONArray retVal = new JSONArray();
		int authCnt = 1;
		HashSet<String> authors = new HashSet<String>();

		String bgJson = IOUtils.toString(new URL(JSON_URL).openStream());
		JSONArray jsonArr = (JSONArray) new JSONParser().parse(bgJson);
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArr.get(i);
			String auth = (String) jsonObject.get("author");
			if (auth != null && !authors.contains(auth)) {
				authors.add(auth);
				JSONObject authObj = new JSONObject();
				authObj.put("author" + authCnt++, auth);
				retVal.add(authObj);
			}
		}

		return retVal.toJSONString();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allImages")
	public String fetchAllImages() throws JSONException, IOException, ParseException {
		JSONArray retVal = new JSONArray();
		int urlCnt = 1;
		HashSet<String> urls = new HashSet<String>();

		String bgJson = IOUtils.toString(new URL(JSON_URL).openStream());
		JSONArray jsonArr = (JSONArray) new JSONParser().parse(bgJson);
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArr.get(i);
			String url = (String) jsonObject.get("url");
			if (url != null && !urls.contains(url)) {
				urls.add(url);
				JSONObject urlObj = new JSONObject();
				urlObj.put("url" + urlCnt++, url);
				retVal.add(urlObj);
			}
		}

		return retVal.toJSONString();
	}
}
