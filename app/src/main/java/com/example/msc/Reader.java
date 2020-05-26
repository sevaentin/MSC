package com.example.msc;

import android.os.AsyncTask;
import android.os.Build;
import android.security.NetworkSecurityPolicy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.annotation.RequiresApi;

public class Reader extends AsyncTask<String[], Void, Model> {

    Model model = new Model();
    private ReadCardActivity activity;

    public Reader(ReadCardActivity activity, Object o) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected Model doInBackground(String[]... params) {
        //NetworkSecurityPolicy.isCleartextTrafficPermitted();
        URL url = null;
        try {
            url = new URL("http://tasks.ingeneo.co.il:/test/abc.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = documentBuilder.parse(new InputSource(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        model = new Model();

        NodeList nodlist = document.getElementsByTagName("user");
        Element element = (Element) nodlist.item(0);
        NodeList id = element.getElementsByTagName("id");
        NodeList ln = element.getElementsByTagName("ln");
        NodeList sn = element.getElementsByTagName("fn");
        NodeList s = element.getElementsByTagName("s");

        Element element1 = (Element) id.item(0);
        model.setId(element1.getTextContent());

        element1 = (Element) ln.item(0);
        model.setLastName(element1.getTextContent());

       //ln = element.getElementsByTagName("sn");
        element1 = (Element) sn.item(0);
        model.setFirstName(element1.getTextContent());

        //ln = element.getElementsByTagName("s");
        element1 = (Element) s.item(0);
        model.setStatus(element1.getTextContent());

        return model;
    }

    @Override
    protected void onPostExecute(Model model) {
        activity.callBackData(model);
    }

    public Model GetModel(){
        return model;
    }
}
