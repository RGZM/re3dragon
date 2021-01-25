package link.archaeology.re3dragon.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import link.archaeology.re3dragon.conf.DragonItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mainzed.re3dragon.exceptions.ResourceNotAvailableException;
import org.mainzed.re3dragon.exceptions.RetcatException;
import org.mainzed.re3dragon.log.Logging;

public class IconClass {

    public static JSONObject info(String url) throws IOException, ResourceNotAvailableException, ParseException, RetcatException {
        try {
            // query sparql endpoint
            String sparqlendpoint = "https://api.data.netwerkdigitaalerfgoed.nl/datasets/rkd/iconclass/services/iconclass/sparql";
            String sparql = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ";
            sparql += "SELECT * { ";
            sparql += "<" + url.replace("(", "%2528").replace(")", "%2529") + "> skos:prefLabel ?preflabel. ";
            sparql += "FILTER(LANGMATCHES(LANG(?preflabel), \"en\"))";
            sparql += "<" + url.replace("(", "%2528").replace(")", "%2529") + "> skos:prefLabel ?label. ";
            sparql += "OPTIONAL{ <" + url.replace("(", "%2528").replace(")", "%2529") + "> skos:broader ?broader. ?broader skos:prefLabel ?bLabel. FILTER(LANGMATCHES(LANG(?bLabel), \"en\")) }";
            sparql += "OPTIONAL{ <" + url.replace("(", "%2528").replace(")", "%2529") + "> skos:narrower ?narrower. ?narrower skos:prefLabel ?nLabel. FILTER(LANGMATCHES(LANG(?nLabel), \"en\")) }";
            sparql += " }";
            URL obj = new URL(sparqlendpoint);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/sparql-results+json");
            String urlParameters = "query=" + sparql;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // parse SPARQL results json
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.toString());
            JSONObject resultsObject = (JSONObject) jsonObject.get("results");
            JSONArray bindingsArray = (JSONArray) resultsObject.get("bindings");
            // create object
            DragonItem DRAGON = new DragonItem(url);
            if (bindingsArray.size() > 0) {
                // set preflabel
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject label = (JSONObject) tmpElement.get("preflabel");
                    String labelValue = (String) label.get("value");
                    String labelLang = (String) label.get("xml:lang");
                    DRAGON.setLabelLang(labelValue, labelLang);
                }
                // set prefdesc
                for (Object element : bindingsArray) {
                }
                // set other labels
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject label = (JSONObject) tmpElement.get("label");
                    String labelValue = (String) label.get("value");
                    String labelLang = (String) label.get("xml:lang");
                    DRAGON.setLabel(labelValue, labelLang);
                }
                // set other descriptions
                for (Object element : bindingsArray) {
                }
                // set broader terms
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject broader = (JSONObject) tmpElement.get("broader");
                    if (broader != null) {
                        String broaderValue = (String) broader.get("value");
                        JSONObject label = (JSONObject) tmpElement.get("bLabel");
                        String labelValue = (String) label.get("value");
                        DRAGON.setBroaderTerms(broaderValue, labelValue);
                    }
                }
                // set narrower terms
                for (Object element : bindingsArray) {
                    JSONObject tmpElement = (JSONObject) element;
                    JSONObject narrower = (JSONObject) tmpElement.get("narrower");
                    String narrowerValue = (String) narrower.get("value");
                    JSONObject label = (JSONObject) tmpElement.get("nLabel");
                    String labelValue = (String) label.get("value");
                    DRAGON.setNarrowerTerms(narrowerValue, labelValue);
                }
                // set additional information from triplestore
                DRAGON.setLairInfo("MWGDYW5S");
            } else {
                throw new ResourceNotAvailableException(url);
            }
            return DRAGON.getDragonItem();
        } catch (Exception e) {
            JSONParser parser = new JSONParser();
            JSONObject error = (JSONObject) parser.parse(Logging.getMessageJSON(e, "link.archaeology.re3dragon.action.IconClass"));
            return error;
        }
    }

}
