package aleksey.sheyko.sgbp.rest;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import aleksey.sheyko.sgbp.model.Coupon;

public class CouponsXmlParser {
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
        parser.require(XmlPullParser.START_TAG, ns, "SGBP_Coupon_InfoRespose");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("CouponList")) {

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    Coupon.deleteAll(Coupon.class);

                    String tag = parser.getName();
                    // Starts by looking for the entry tag
                    if (tag.equals("CouponCodeDetails")) {
                        readCoupon(parser).save();
                    }
                }

            } else {
                skip(parser);
            }
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Coupon readCoupon(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "CouponCodeDetails");
        int storeid = -1;
        String storeName = null;
        String code = null;
        String desc = null;
        String expireDate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals("Store_Id")) {
                storeid = readStoreId(parser);
            } else if (tag.equals("Store_Name")) {
                storeName = readStoreName(parser);
            } else if (tag.equals("Coupon_Code")) {
                code = readCouponCode(parser);
            } else if (tag.equals("Coupon_Code_Desc")) {
                desc = readCodeDesk(parser);
            } else if (tag.equals("End_Date")) {
                expireDate = readEndDate(parser);
            } else {
                skip(parser);
            }
        }
        return new Coupon(storeid, storeName, code, desc, expireDate);
    }

    // Processes store id tags in the feed.
    private int readStoreId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Id");
        int storeId = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Store_Id");
        return storeId;
    }

    // Processes store name tags in the feed.
    private String readStoreName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Store_Name");
        String storeName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Store_Name");
        return storeName;
    }

    // Processes coupon code tags in the feed.
    private String readCouponCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Coupon_Code");
        String code = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Coupon_Code");
        return code;
    }

    // Processes coupon desc tags in the feed.
    private String readCodeDesk(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Coupon_Code_Desc");
        String desc = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Coupon_Code_Desc");
        return desc;
    }

    // Processes exn date desc tags in the feed.
    private String readEndDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "End_Date");
        String expireDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "End_Date");
        return expireDate;
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