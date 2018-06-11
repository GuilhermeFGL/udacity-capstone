package com.guilhermefgl.rolling.mock;

import com.guilhermefgl.rolling.model.User;

import java.util.ArrayList;

public class UserMock {

    public static User getLogeedUser() {
        return new User() {{
           setUserId(0L);
           setUserName("Guilherme");
           setUserEmail("guilherme_fgl@hotmail.com");
           setUserAvatarUrl("");
        }};
    }

    public static ArrayList<User> getTripListUser() {
        return new ArrayList<User>() {{
           add(new User() {{
               setUserId(0L);
               setUserName("Guilherme");
               setUserEmail("guilherme_fgl@hotmail.com");
               setUserAvatarUrl("");
           }});
            add(new User() {{
                setUserId(0L);
                setUserName("Priscilla");
                setUserEmail("priscilla@mail.com");
                setUserAvatarUrl("");
            }});
            add(new User() {{
                setUserId(0L);
                setUserName("Fulano");
                setUserEmail("fulano@mail.com");
                setUserAvatarUrl("");
            }});
            add(new User() {{
                setUserId(0L);
                setUserName("Ciclano");
                setUserEmail("ciclano@mail.com");
                setUserAvatarUrl("");
            }});
        }};
    }
}
