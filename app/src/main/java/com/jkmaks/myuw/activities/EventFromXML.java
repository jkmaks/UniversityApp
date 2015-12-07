
package com.jkmaks.myuw.activities;

import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Max on 10/9/2014.
 */
public class EventFromXML {

    protected String get(String address) {
        StringBuilder builder = new StringBuilder();
        builder.append("<rss xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x-microsoft=\"http://schemas.microsoft.com/x-microsoft\" xmlns:x-trumba=\"http://schemas.trumba.com/rss/x-trumba\" version=\"2.0\">\n" +
                "<channel>\n" +
                "<title>UW Tacoma Campus Events</title>\n" +
                "<link>http://www.tacoma.uw.edu/home/calendar</link>\n" +
                "<description/>\n" +
                "<language>en-us</language>\n" +
                "<lastBuildDate>06 Dec 2015 08:52:56 GMT</lastBuildDate>\n" +
                "<image>\n" +
                "<title>Calendar Feed powered by Trumba</title>\n" +
                "<url>https://www.trumba.com/images/trumba_logo_sm.gif</url>\n" +
                "<link>http://www.tacoma.uw.edu/home/calendar</link>\n" +
                "</image>\n" +
                "<item>\n" +
                "<title>Holiday Gift Drive- UWT Gives</title>\n" +
                "<description>\n" +
                "Ongoing through Monday, Dec. 14, 2015 <br/><br/><img src=\"https://www.trumba.com/i/DgDZ2i9r0orzgXG90ps0aYBm.jpg\" width=\"100\" height=\"18\" /><br/><br/>Starting November 2nd, we will be collecting gifts for UW Tacoma families who might not otherwise be able to celebrate the season.&#160; For information on how you can give or receive, please contact:<br /> Katie at <a href=\"mailto:altbrk2@uw.edu\" target=\"_blank\">altbrk2@uw.edu</a> <br/><br/><b>Campus room</b>:&nbsp;KEY 212 Drop Off (and other locations) <br/><b>Event types</b>:&nbsp;Student Activities <br/><b>Event sponsors</b>:&nbsp;<a href=\"http://www.tacoma.uw.edu/CSL\" target=\"_blank\">Center for Service and Leadership</a> <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116275105\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116275105\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/11/09 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116275105</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>Winter 2016 Registration Period II</title>\n" +
                "<description>\n" +
                "Ongoing through Sunday, Jan. 3, 2016 <br/><br/><b>Event types</b>:&nbsp;Academics <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116059475\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116059475\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/11/23 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116059475</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>CHEM I SUPPORT (TESC 141)</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 10&nbsp;&ndash;&nbsp;11&nbsp;a.m.&nbsp;PST <br/><br/><img src=\"https://www.trumba.com/i/DgBDdjPD6gbHGXMNcxey3-5w.png\" width=\"100\" height=\"41\" /><br/><br/>This workshop supports students enrolled in TESC 141.&#160; Please come prepared with questions. <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.244596,-122.438017&amp;z=18\" target=\"_blank\">UW Tacoma Snoqualmie (SNO)</a> <br/><b>Campus room</b>:&nbsp;239 <br/><b>Event types</b>:&nbsp;Academics, Workshops <br/><b>Event sponsors</b>:&nbsp;Teaching and Learning Center <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116316737\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116316737\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116316737</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>Social Media Research Group</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 10:30&nbsp;a.m.&nbsp;&ndash;&nbsp;12:30&nbsp;p.m.&nbsp;PST <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.244352,-122.438679&amp;z=18\" target=\"_blank\">UW Tacoma Tioga Library Building (TLB)</a> <br/><b>Campus room</b>:&nbsp;307B (Atrium) <br/><b>Event types</b>:&nbsp;Lectures/Seminars <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d110390695\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/110390695\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/110390695</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>Peer-Mentoring Assistant Professors</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 12:15&nbsp;&ndash;&nbsp;1:45&nbsp;p.m.&nbsp;PST <br/><br/>Peer-Mentoring Assistant Professors WCG 322: 12:15PM to 1:45PM <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.245944,-122.437368&amp;z=18\" target=\"_blank\">UW Tacoma West Coast Grocery (WCG)</a> <br/><b>Campus room</b>:&nbsp;WCG 322 <br/><b>Event types</b>:&nbsp;Meetings <br/><b>Event sponsors</b>:&nbsp;Turan Kayaoglu <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116455627\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116455627\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116455627</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>\n" +
                "It Takes a Village: Data Support from Citizen Science| Environmental Science Seminar\n" +
                "</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 12:25&nbsp;&ndash;&nbsp;1:25&nbsp;p.m.&nbsp;PST <br/><br/><img src=\"https://www.trumba.com/i/DgBui4sLOVJsvJa4T7M69FZB.jpg\" width=\"100\" height=\"133\" /><br/><br/>Lecturer Julie Masura presents<em> It Takes a Village: Data Support from Citizen Science</em> <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.245545,-122.438398&amp;z=18\" target=\"_blank\">UW Tacoma Science Building (SCI)</a> <br/><b>Campus room</b>:&nbsp;309 <br/><b>Event types</b>:&nbsp;Academics, Lectures/Seminars <br/><b>Event sponsors</b>:&nbsp;<a href=\"http://www.tacoma.uw.edu/node/38810/\" target=\"_blank\">Science and Mathematics (SAM)</a> division of the <a href=\"http://www.tacoma.uw.edu/node/38805/\" target=\"_blank\">School of IAS</a>. <br/><b>Twitter</b>:&nbsp;#uwt_sias #JulieMasura <br/><b>More info</b>:&nbsp;<a href=\"http://www.tacoma.uw.edu/sites/default/files/sections/SciencesandMathematics%28SAM%29/151009_SAM_EnvSci_SeminarsFall.jpg\" target=\"_blank\" title=\"http://www.tacoma.uw.edu/sites/default/files/sections/SciencesandMathematics%28SAM%29/151009_SAM_EnvSci_SeminarsFall.jpg\">www.tacoma.uw.edu&#8230;</a> <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116759704\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116759704\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116759704</guid>\n" +
                "<x-trumba:weblink>\n" +
                "http://www.tacoma.uw.edu/sites/default/files/sections/SciencesandMathematics%28SAM%29/151009_SAM_EnvSci_SeminarsFall.jpg\n" +
                "</x-trumba:weblink>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>Sources, Citations...& Cocoa</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 1&nbsp;&ndash;&nbsp;3&nbsp;p.m.&nbsp;PST <br/><br/><img src=\"https://www.trumba.com/i/DgCPfqV3gBtseC0nz0UrzddH.jpg\" width=\"100\" height=\"100\" /><br/><br/>Need help finding sources or formatting citations as you wrap up your autumn quarter projects? Join us for these special drop-in hours!<br /> <br /> SOURCES, CITATIONS...&amp; COCOA<br /> * Thursday 12/3 &#8211; 11-1pm &#8211; SNO 136<br /> * Monday 12/7 &#8211; 1-3pm &#8211; SNO 136<br /> <br /> Librarians and staff from the TLC will be present to answer questions. Cocoa, coffee, and tea will be on hand to keep minds warm and spirits high.<br /> <br /> If you can&#39;t make these times, or if you need more in-depth help:<br /> - Contact a librarian: <a href=\"http://www.tacoma.uw.edu/library/ask-librarian\" target=\"_blank\" title=\"http://www.tacoma.uw.edu/library/ask-librarian\">www.tacoma.uw.edu&#8230;</a><br /> - Or contact the Teaching &amp; Learning Center: <a href=\"https://www.tacoma.uw.edu/teaching-learning-center\" target=\"_blank\" title=\"https://www.tacoma.uw.edu/teaching-learning-center\">www.tacoma.uw.edu&#8230;</a> <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.244596,-122.438017&amp;z=18\" target=\"_blank\">UW Tacoma Snoqualmie (SNO)</a> <br/><b>Campus room</b>:&nbsp;SNO 136 <br/><b>Event types</b>:&nbsp;Workshops <br/><b>Event sponsors</b>:&nbsp;UW Tacoma Library <br/><b>Facebook</b>:&nbsp;<a href=\"https://www.facebook.com/events/974895069240738/\" target=\"_blank\" title=\"https://www.facebook.com/events/974895069240738/\">www.facebook.com&#8230;</a> <br/><b>More info</b>:&nbsp;<a href=\"https://www.facebook.com/events/974895069240738/\" target=\"_blank\" title=\"https://www.facebook.com/events/974895069240738/\">www.facebook.com&#8230;</a> <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d117205979\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/117205979\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/117205979</guid>\n" +
                "<x-trumba:weblink>https://www.facebook.com/events/974895069240738/</x-trumba:weblink>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>Faculty Only Writing Group</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 2&nbsp;&ndash;&nbsp;4&nbsp;p.m.&nbsp;PST <br/><br/>All faculty are welcome to join us in the Faculty Resource Center (Walsh Gardner 208), starting October 5th for the SIAS on-campus faculty writing groups. These groups have helped us set aside time for doing research and creative work. We&#39;ll chat briefly about goals at the 1st session and then get to writing. Please feel free to bring laptops and other materials. You&#39;re welcome to bring your lunch, etc.<br /> <br /> Here are the times -<br /> <br /> Mondays from 2-4pm with Ellen<br /> Tuesdays from 12-2pm with Ed<br /> Fridays from 9-11am with Emma<br /> <br /> If you have questions, send us an e-mail.<br /> <br /> Ed Chamberlain <a href=\"mailto:ec10@uw.edu\" target=\"_blank\">ec10@uw.edu</a><br /> Ellen Moore <a href=\"mailto:melle@uw.edu\" target=\"_blank\">melle@uw.edu</a><br /> Emma Rose <a href=\"mailto:ejrose@uw.edu\" target=\"_blank\">ejrose@uw.edu</a> <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.244671,-122.437035&amp;z=18\" target=\"_blank\">UW Tacoma Walsh Gardner (WG)</a> <br/><b>Campus room</b>:&nbsp;208 <br/><b>Event types</b>:&nbsp;Academics <br/><b>Event sponsors</b>:&nbsp;The School of Interdisciplinary Arts and Sciences faculty. <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116340617\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116340617\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116340617</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>TBUS 301 Support Workshop</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 3&nbsp;&ndash;&nbsp;4&nbsp;p.m.&nbsp;PST <br/><br/><img src=\"https://www.trumba.com/i/DgBDdjPD6gbHGXMNcxey3-5w.png\" width=\"100\" height=\"41\" /><br/><br/>This workshop supports students enrolled in TBUS 301. Please come prepared with questions. <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.244596,-122.438017&amp;z=18\" target=\"_blank\">UW Tacoma Snoqualmie (SNO)</a> <br/><b>Campus room</b>:&nbsp;239 <br/><b>Event types</b>:&nbsp;Academics, Workshops <br/><b>Event sponsors</b>:&nbsp;Teaching and Learning Center <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116316846\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116316846\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116316846</guid>\n" +
                "</item>\n" +
                "<item>\n" +
                "<title>BSN Application Workshop and Information Session</title>\n" +
                "<description>\n" +
                "Monday, Dec. 7, 2015, 4:30&nbsp;&ndash;&nbsp;6&nbsp;p.m.&nbsp;PST <br/><br/>The Nursing Program at the University of Washington Tacoma offers a Bachelor of Science in Nursing (RN to BSN). The courses support students in developing professionally. The program goals, in part, stress communication, critical thinking, cultural sensitivity, enhanced patient care, research and scholarship, health promotion and education, and adapting to changes in the healthcare setting.<br /> <br /> Come to our Application Workshop and Information Session to learn more!<br /> <br /> RSVP via email <a href=\"mailto:tnursing@uw.edu\" target=\"_blank\">tnursing@uw.edu</a> or call 253.692.4470 <br/><br/><b>Campus location</b>:&nbsp;<a href=\"http://maps.google.com/maps?q=47.245766,-122.437325&amp;z=18\" target=\"_blank\">UW Tacoma Birmingham Hay and Seed (BHS)</a> <br/><b>Campus room</b>:&nbsp;104 <br/><b>Event types</b>:&nbsp;Information Sessions, Workshops <br/><br/>\n" +
                "</description>\n" +
                "<link>\n" +
                "http://www.tacoma.uw.edu/home/calendar?trumbaEmbed=view%3devent%26eventid%3d116225573\n" +
                "</link>\n" +
                "<x-trumba:ealink>\n" +
                "https://eventactions.com/eventactions/tac_campus#/actions/116225573\n" +
                "</x-trumba:ealink>\n" +
                "<category>2015/12/07 (Mon)</category>\n" +
                "<guid isPermaLink=\"false\">http://uid.trumba.com/event/116225573</guid>\n" +
                "</item>\n" +
                "<item>");
        //Log.d("Builder is ", builder.toString());
        return builder.toString();
    }

}

