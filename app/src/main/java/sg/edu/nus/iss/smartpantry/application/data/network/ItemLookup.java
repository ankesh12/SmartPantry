/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package sg.edu.nus.iss.smartpantry.application.data.network;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import sg.edu.nus.iss.smartpantry.R;
import sg.edu.nus.iss.smartpantry.application.util.XMLUtil;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class ItemLookup {
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
   private String AWS_ACCESS_KEY_ID,AWS_SECRET_KEY,ASSOCIATE_TAG,ENDPOINT;

    public ItemLookup(Context context){
        try {
            XMLUtil xmlUtil = new XMLUtil();
            AWS_ACCESS_KEY_ID =xmlUtil.getElementText("PublicKey",context.getResources().openRawResource(R.raw.configuration));
            AWS_SECRET_KEY = xmlUtil.getElementText("SecretKey", context.getResources().openRawResource(R.raw.configuration));
            ASSOCIATE_TAG= xmlUtil.getElementText("AssociateTag", context.getResources().openRawResource(R.raw.configuration));
            ENDPOINT = xmlUtil.getElementText("EndPoint", context.getResources().openRawResource(R.raw.configuration));System.out.println("End: " + ENDPOINT);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
//    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
    private static String ITEM_ID = "";

    public ArrayList<String> GetProductDetails(String barcode) {
        /*
         * Set up the signed requests helper 
         */
        ItemLookup.ITEM_ID = barcode;
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String requestUrl = null;
        String title = null;

        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemLookup");
        params.put("ItemId", ITEM_ID);
        params.put("ResponseGroup", "Large");
        params.put("IdType","EAN");
        params.put("SearchIndex","All");
        params.put("AssociateTag","smartqtpcom-20");

        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");

        return fetchTitle(requestUrl);
    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static ArrayList<String> fetchTitle(String requestUrl) {
        ArrayList<String> details = new ArrayList<String>();
        String title = null;
        try {
            URL url = new URL(requestUrl);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Title");
            NodeList nodeList2 = doc.getElementsByTagName("MediumImage");
            details.add(nodeList.item(0).getTextContent());
            details.add(nodeList2.item(0).getFirstChild().getTextContent());
            return details;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
