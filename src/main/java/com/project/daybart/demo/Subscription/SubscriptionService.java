package com.project.daybart.demo.Subscription;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.project.daybart.demo.Users.Users;
import com.project.daybart.demo.Users.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SubscriptionService {

    public final SubscriptionRepository subsRepo;
    public final UsersRepository usersRepo;

    public String setSubscriptionByUser_Id(Users users) {
        Optional<Subscription> optionalSub = subsRepo.findByUser_Id(users.getId());
        if (optionalSub.isPresent()) {
            Subscription sub = optionalSub.get();
            sub.setPayed(true);
            sub.setDate(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
            UUID uuid = UUID.randomUUID();
            String randomString = uuid.toString().substring(0, 10);
            sub.setSerial(randomString);

            return "The subscription was success";
        } else {
            return "The subscription was unsuccessful";
        }
    }

}
