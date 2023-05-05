package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription = new Subscription();
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        int totalAmountPaid = 0;
        if(subscription.getSubscriptionType()==SubscriptionType.BASIC){
            totalAmountPaid = subscription.getNoOfScreensSubscribed()*200 + 500;
        }
        else if(subscription.getSubscriptionType()==SubscriptionType.PRO){
            totalAmountPaid = subscription.getNoOfScreensSubscribed()*250 + 800;
        }
        else{
            totalAmountPaid = subscription.getNoOfScreensSubscribed()*350 + 1000;
        }
        subscription.setTotalAmountPaid(totalAmountPaid);
        subscription.setUser(user);
        user.setSubscription(subscription);
        userRepository.save(user);
        return totalAmountPaid;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();
        if(subscription.getSubscriptionType()==SubscriptionType.ELITE)
            throw new Exception("Already the best Subscription");

        int price = 0;
        if(subscription.getSubscriptionType()==SubscriptionType.BASIC){
            price = subscription.getNoOfScreensSubscribed()*250 + 800 - subscription.getTotalAmountPaid();
            subscription.setSubscriptionType(SubscriptionType.PRO);
        }
        else{
                price = subscription.getNoOfScreensSubscribed()*350 + 1000 - subscription.getTotalAmountPaid();
                subscription.setSubscriptionType(SubscriptionType.ELITE);
        }
        user.setSubscription(subscription);
        userRepository.save(user);
        return price;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int revenue = 0;
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        for(Subscription subscription: subscriptionList){
            revenue += subscription.getTotalAmountPaid();
        }
        return revenue;
    }

}
