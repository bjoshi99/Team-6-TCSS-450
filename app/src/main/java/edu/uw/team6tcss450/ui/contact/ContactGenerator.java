package edu.uw.team6tcss450.ui.contact;

import java.util.Arrays;
import java.util.List;

public class ContactGenerator {

    private static final Contact[] BLOGS;
    public static final int COUNT = 20;
    private static int lastContactId = 0;

    static {
        BLOGS = new Contact[COUNT];
        for (int i = 0; i < BLOGS.length; i++) {
        }
    }

    public static List<Contact> getBlogList() {
        return Arrays.asList(BLOGS);
    }

    public static Contact[] getBLOGS() {
        return Arrays.copyOf(BLOGS, BLOGS.length);
    }

    private ContactGenerator() { }


}
