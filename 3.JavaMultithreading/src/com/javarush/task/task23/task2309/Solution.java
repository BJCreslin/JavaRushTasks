package com.javarush.task.task23.task2309;

import com.javarush.task.task23.task2309.vo.*;

import java.util.List;

/* 
Анонимность иногда так приятна!
*/
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        print(solution.getUsers());
        print(solution.getLocations());
    }

    public List<User> getUsers() {

        AbstractDbSelectExecutor <User>  userU = new AbstractDbSelectExecutor<User>() {
            @Override
            public String getQuery() {
                return "User";
            }
        };
        return userU.execute();

    }
    public List<Location> getLocations() {
        AbstractDbSelectExecutor <Location>  userU = new AbstractDbSelectExecutor<Location>(){
            @Override
            public String getQuery() {
                return "Location";
            }
        };

        return userU.execute();
    }

    public List<Server> getServers() {
        AbstractDbSelectExecutor <Server>  userU = new AbstractDbSelectExecutor<Server>(){

            @Override
            public String getQuery() {
                return "Server";
            }
        };

        return userU.execute();
    }
    public List<Subject> getSubjects() {
        AbstractDbSelectExecutor <Subject>  userU = new AbstractDbSelectExecutor<Subject>(){

            @Override
            public String getQuery() {
                return "Subject";
            }
        };

        return userU.execute();
    }

    public List<Subscription> getSubscriptions() {
        AbstractDbSelectExecutor <Subscription>  userU = new AbstractDbSelectExecutor<Subscription>(){
            @Override
            public String getQuery() {
                return "Subscription";
            }};

        return userU.execute();
    }


    public static void print(List list) {
        String format = "Id=%d, name='%s', description=%s";
        for (Object obj : list) {
            NamedItem item = (NamedItem) obj;
            System.out.println(String.format(format, item.getId(), item.getName(), item.getDescription()));
        }
    }
}
