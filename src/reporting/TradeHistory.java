package reporting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TradeHistory {
	public static String getTradeHistory(String jsonFile){
		String s="";
		JSONObject json=null;
		try {
			s = readFile(jsonFile);
			json = readJsonString(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonToHtml(json);
	}
	
	public static JSONObject readJsonString(String jsonString){
		JSONObject json= null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	public static String jsonToHtml(JSONObject json){
		StringBuilder  html = new StringBuilder();
		// add html header first
		JSONArray trans=null;
		try {
			trans = json.getJSONArray("transactions");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		HashMap<String, StringBuilder> map = parseJson(trans);
		String[] sts = {"EMA", "TMA", "EMA", "LWMA"};
		for(String st: sts){
			html.append("<table>");
			
			html.append("<tr>");
			html.append("<th> Time </th>");
			html.append("<th> Type </th>");
			html.append("<th> Price </th>");
			html.append("<th> Manager </th>");
			html.append("<th> Strategy </th>");
			html.append("</tr>\n");
		
			html.append(map.get(st));
			
			html.append("</table>\n");
		}
		return html.toString();
		
		
	}
	
	public static HashMap<String, StringBuilder> parseJson(JSONArray trans){
		HashMap<String, StringBuilder> map = new HashMap<String, StringBuilder>();
		map.put("EMA", new StringBuilder());
		map.put("LWMA", new StringBuilder());
		map.put("SMA", new StringBuilder());
		map.put("TMA", new StringBuilder());
		
		JSONObject trade=null;
		String strategy=null;
		for(int i=0; i<trans.length(); i++){
			try {
				trade = trans.getJSONObject(i);
				strategy = trade.getString("strategy");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
			((StringBuilder) map.get(strategy)).append(jsonObjectToTable(trade));
		}
		
		return map;
		
	}
	
	public static String jsonObjectToTable(JSONObject json){
		StringBuilder  sb = new StringBuilder();
		sb.append("<tr>\n");
		try {
			sb.append("<td>"+ json.getString("time") +"</td>");
			sb.append("<td>"+ json.getString("type") +"</td>");
			sb.append("<td>"+ json.getDouble("price") +"</td>");
			sb.append("<td>"+ json.getString("manager") +"</td>");
			sb.append("<td>"+ json.getString("strategy") +"</td>");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("</tr>\n");
		
		return sb.toString();
	}
	
	private static String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
}
