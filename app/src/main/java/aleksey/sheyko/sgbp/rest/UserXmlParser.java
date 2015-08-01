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

    public User parse(InputStream in) throws XmlPullParserException, IOException {
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

    private User readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<User> userList = new ArrayList<>();

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
                        userList.add(readUser(parser));
                    } else {
                        return null;
                    }
                }

            } else {
                skip(parser);
            }
        }
        if (userList.size() > 0) {
            // user exists
            return userList.get(0);
        } else {
            // not found, need to register
            return null;
        }
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
            switch (tag) {
                case "User_Reg_Info_Id":
                    userId = readUserId(parser);
                    break;
                case "First_Name":
                    firstName = readFirstName(parser);
                    break;
                case "Last_Name":
                    lastName = readLastName(parser);
                    break;
                case "School_Id":
                    schoolId = readSchoolId(parser);
                    break;
                case "Has_Multiple_Child":
                    multipleGrade = readGradeLevel(parser);
                    break;
                case "Is_Coupon_Allowed":
                    coupons = readCoupons(parser);
                    break;
                case "Is_Notification_Allowed":
                    notifications = readNotifications(parser);
                    break;
                case "Is_Location_Service_Allowed":
                    location = readLocation(parser);
                    break;
                case "Is_User_Over_18_Year":
                    is18 = readAge(parser);
                    break;
                case "User_Email":
                    email = readEmail(parser);
                    break;
                default:
                    skip(parser);
                    break;
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

    // Processes school id tags in the feed.
    private int readSchoolId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "School_Id");
        int schoolId = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "School_Id");
        return schoolId;
    }

    // Processes grade level tags in the feed.
    private boolean readGradeLevel(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Has_Multiple_Child");
        boolean multipleGrade = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Has_Multiple_Child");
        return multipleGrade;
    }

    // Processes coupons tags in the feed.
    private boolean readCoupons(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Is_Coupon_Allowed");
        boolean coupons = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Is_Coupon_Allowed");
        return coupons;
    }

    // Processes notifications tags in the feed.
    private boolean readNotifications(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Is_Notification_Allowed");
        boolean notifications = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Is_Notification_Allowed");
        return notifications;
    }

    // Processes location tags in the feed.
    private boolean readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Is_Location_Service_Allowed");
        boolean location = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Is_Location_Service_Allowed");
        return location;
    }

    // Processes age tags in the feed.
    private boolean readAge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Is_User_Over_18_Year");
        boolean is18 = Boolean.parseBoolean(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "Is_User_Over_18_Year");
        return is18;
    }

    // Processes age tags in the feed.
    private String readEmail(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "User_Email");
        String email = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "User_Email");
        return email;
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