package aleksey.sheyko.sgbp.rest;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "SGBP_User_Reg_Info_List");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("UserRegInfo")) {

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String tag = parser.getName();
                    // Starts by looking for the entry tag
                    if (tag.equals("SGBP_User_Reg_Info")) {
                        entries.add(readUser(parser));
                    } else {
                        return null;
                    }
                }

            } else {
                skip(parser);
            }
        }
        return entries;
    }

    public static class User {
        public final int id;
        public final String firstName;
        public final String lastName;
        public final int schoolId;
        public final boolean multipleGrade;
        public final boolean coupons;
        public final boolean notifications;
        public final boolean location;
        public final boolean is18;
        public final String email;

        private User(int id, String firstName, String lastName, int schoolId, boolean multipleGrade, boolean coupons,
                     boolean notifications, boolean location, boolean is18, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.schoolId = schoolId;
            this.multipleGrade = multipleGrade;
            this.coupons = coupons;
            this.notifications = notifications;
            this.location = location;
            this.is18 = is18;
            this.email = email;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private User readUser(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "SGBP_User_Reg_Info");
        int userId = -1;
        String firstName = null;
        String lastName = null;
        int schoolId = -1;
        boolean multipleGrade = false;
        boolean coupons = true;
        boolean notifications = true;
        boolean location = true;
        boolean is18 = true;
        String email = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals("User_Reg_Info_Id")) {
                userId = readUserId(parser);
            } else if (tag.equals("First_Name")) {
                firstName = readFirstName(parser);
            } else if (tag.equals("Last_Name")) {
                lastName = readLastName(parser);
            } else if (tag.equals("School_Id")) {
                schoolId = readSchoolId(parser);
            } else if (tag.equals("Has_Multiple_Child")) {
                multipleGrade = readGradeLevel(parser);
            } else if (tag.equals("Is_Coupon_Allowed")) {
                coupons = readCoupons(parser);
            } else if (tag.equals("Is_Notification_Allowed")) {
                notifications = readNotifications(parser);
            } else if (tag.equals("Is_Location_Service_Allowed")) {
                location = readLocation(parser);
            } else if (tag.equals("Is_User_Over_18_Year")) {
                is18 = readAge(parser);
            } else if (tag.equals("User_Email")) {
                email = readEmail(parser);
            } else {
                skip(parser);
            }
        }
        return new User(userId, firstName, lastName, schoolId, multipleGrade, coupons, notifications, location, is18, email);
    }

    // Processes id tags in the feed.
    private int readUserId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "User_Reg_Info_Id");
        int id = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "User_Reg_Info_Id");
        return id;
    }

    // Processes first name tags in the feed.
    private String readFirstName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "First_Name");
        String firstName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "First_Name");
        return firstName;
    }

    // Processes last name tags in the feed.
    private String readLastName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Last_Name");
        String lastName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Last_Name");
        return lastName;
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