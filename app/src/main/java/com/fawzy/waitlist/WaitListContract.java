package com.fawzy.waitlist;

import android.provider.BaseColumns;

public class WaitListContract {

      // 3shan ya3mli id lwhdo w y3ml increment automatically
    public static final class WaitListEntry implements BaseColumns {
        public static final String TABLE_NAME = "waitlist";
        public static final String COULUMN_GUEST_NAME = "guestName" ;
        public static final String COULUMN_PARTY_SIZE = "partysize" ;
        public static final String COULUMN_TIMESTAP = "timestap" ;
    }


}
