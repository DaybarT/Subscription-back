package com.project.daybart.demo.Subscription;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.project.daybart.demo.Users.Users;
import com.project.daybart.demo.Users.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SubscriptionService {

    public final SubscriptionRepository subsRepo;
    public final UsersRepository usersRepo;

    public ResponseEntity<String> setSubscriptionByUser_Id(Users users) {
        try {
            Optional<Subscription> optionalSub = subsRepo.findByUser_Id(users.getId());
            if (optionalSub.isPresent()) {
                Subscription sub = optionalSub.get();
                sub.setPayed(true);
                sub.setDate(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
                UUID uuid = UUID.randomUUID();
                String randomString = uuid.toString().substring(0, 10);
                sub.setSerial(randomString);

                return new ResponseEntity<>("Suscripcion creada con exito", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Suscripcion fallida", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
