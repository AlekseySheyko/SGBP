package aleksey.sheyko.sgbp.rest;

import android.util.Xml;

import org.apache.commons.lang3.text.WordUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import aleksey.sheyko.sgbp.app.helpers.Constants;
import aleksey.sheyko.sgbp.model.Store;

public class StoresXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public void parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFeed(parser);
        } finally {
            in.close();
        }
    }

    private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "SGBP_Store_Info_List");
        Store.deleteAll(Store.class);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("StoreInfo")) {

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String tag = parser.getName();
                    // Starts by looking for the entry tag
                    if (tag.equals("SGBP_Store_Info")) {
                        readStore(parser).save();
//                        String tempName = readStore(parser).getName();
//                        List<Store> existingStores = Store.find(Store.class, "name = ?", tempName);
//                        if (existingStores.size() == 0) {
//                            readStore(parser).save();
//                        }
                    }
                }

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Store readStore(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "SGBP_Store_Info");
        int id = -1;
        String name = null;
        String address = null;
        String phone = null;
        String latitude = null;
        String longitude = null;
        String category = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals("Store_Id")) {
                id = readId(parser);
            } else if (tag.equals("Store_Name")) {
                name = readName(parser);
            } else if (tag.equals("Store_Address_Line1")) {
                address = readAddress(parser);
            } else if (tag.equals("Store_Phone")) {
                phone = readPhone(parser);
            } else if (tag.equals("Store_Address_Latitude")) {
                latitude = readLatitude(parser);
            } else if (tag.equals("Store_Address_Longitude")) {
                longitude = readLongitude(parser);
            } else if (tag.equals("Is_Store_Location_Physical")) {
                boolean isMobile = readIsMobile(parser);
                if (isMobile) {
                    category = Constants.CATEGORY_MOBILE;
                }
            } else if (tag.equals("Store_Group_Name")) {
                category = WordUtils.capitalizeFully(
                        readText(parser));
            } else {
                skip(parser);
            }
        }
        return new Store(id, name, address, phone, latitude, longitude, category);
    }

    // Processes id tags in the feed.
    private int readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Id");
        int id = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Store_Id");
        return id;
    }

    // Processes name tags in the feed.
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Name");
        String name = readText(parser);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        parser.require(XmlPullParser.END_TAG, ns, "Store_Name");
        return name;
    }

    // Processes address tags in the feed.
    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Address_Line1");
        String address = readText(parser);
        address = address.substring(0, 1).toUpperCase() + address.substring(1);
        parser.require(XmlPullParser.END_TAG, ns, "Store_Address_Line1");
        return address;
    }

    // Processes phone tags in the feed.
    private String readPhone(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Phone");
        String phone = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Store_Phone");
        return phone;
    }

    // Processes latitude tags in the feed.
    private String readLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Address_Latitude");
        String lat = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Store_Address_Latitude");
        return lat;
    }

    // Processes longitude tags in the feed.
    private String readLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Address_Longitude");
        String lng = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Store_Address_Longitude");
        return lng;
    }

    // Processes mobile tags in the feed.
    private boolean readIsMobile(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Is_Store_Location_Physical");
        boolean isMobile = !Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Is_Store_Location_Physical");
        return isMobile;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}