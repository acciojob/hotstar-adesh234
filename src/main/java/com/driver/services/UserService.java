package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User user1 = new User();
        user1.setAge(user.getAge());
        user1.setName(user.getName());
        user1.setMobNo(user.getMobNo());

        userRepository.save(user1);
        return user1.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        List<WebSeries> seriesList = webSeriesRepository.findAll();
        Integer answer = 0;
        User user = userRepository.findById(userId).get();
        for(WebSeries webSeries: seriesList){
            if(webSeries.getAgeLimit()>user.getAge())
                answer++;
        }

        return answer;
    }
}