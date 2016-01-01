package org.dspace.app.webui.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by root on 1/1/16.
 */
public class SoapHelper {

    public Document getRecordById(String id){
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL("http://doc.ssau.ru/ssau_biblioteka_test/ws/DspaceIntegration.1cws");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Authorization", "Basic d2Vic2VydmljZTp3ZWJzZXJ2aWNl");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);

        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(
                    connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Charset charset = Charset.forName("UTF-8");
        try {

            wr.write(new String("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:imc=\"http://imc.parus-s.ru\">\n" +
                    "   <soap:Header/>\n" +
                    "   <soap:Body>\n" +
                    "      <imc:GetRecordsInfo>\n" +
                    "         <imc:Codes>"+id+"</imc:Codes>\n" +
                    "         <imc:Separator>?</imc:Separator>\n" +
                    "         <imc:Type>?</imc:Type>\n" +
                    "      </imc:GetRecordsInfo>\n" +
                    "   </soap:Body>\n" +
                    "</soap:Envelope>").getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
        String line;
        String responseString = "";
        try {
            while((line = rd.readLine()) != null) {
                responseString+=line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DocumentBuilder db = null;

        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        InputSource is2 = new InputSource();
        is2.setCharacterStream(new StringReader(responseString));

        Document doc = null;
        try {
            doc = db.parse(is2);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

}
