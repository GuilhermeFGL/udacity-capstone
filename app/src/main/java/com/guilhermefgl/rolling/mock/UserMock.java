package com.guilhermefgl.rolling.mock;

import com.guilhermefgl.rolling.model.User;

import java.util.ArrayList;

public class UserMock {

    public static User getLogeedUser() {
        return new User() {{
           setUserId(0L);
           setUserName("Woody Stevens");
           setUserEmail("woody_stevens@mail.com");
           setUserAvatarUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTA5viZbUCznFNlxewF53CkhPAzOd3HEdQY5-xyouFaCEHvwqTX");
        }};
    }

    public static ArrayList<User> getTripListUser() {
        return new ArrayList<User>() {{
           add(new User() {{
               setUserId(0L);
               setUserName("Woody Stevens");
               setUserEmail("woody_stevens@mail.com");
               setUserAvatarUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTA5viZbUCznFNlxewF53CkhPAzOd3HEdQY5-xyouFaCEHvwqTX");
           }});
            add(new User() {{
                setUserId(1L);
                setUserName("Doug Madsen");
                setUserEmail("doug_madsen@mail.com");
                setUserAvatarUrl("https://cbsnews2.cbsistatic.com/hub/i/r/2012/01/05/290e43d9-a645-11e2-a3f0-029118418759/resize/620x465/e0a85aac80bc6bea16eada21a7771e70/timallen_ss2.jpg");
            }});
            add(new User() {{
                setUserId(2L);
                setUserName("Bobby Davis");
                setUserEmail("bobby_davis@mail.com");
                setUserAvatarUrl("https://4.bp.blogspot.com/-h32ahK3U3uU/WPLu2Xajz_I/AAAAAAAAFuU/0ZqBG6KeUCEGMPF__8-3oyaau96B4VBVwCLcB/s640/AP_MartinLawrence_080306_main.jpg");
            }});
            add(new User() {{
                setUserId(3L);
                setUserName("Dudley Frank");
                setUserEmail("dudley_frank@mail.com");
                setUserAvatarUrl("https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTE5NTU2MzE2MzAwNjc0NTcx/william-h.jpg");
            }});
        }};
    }
}
