package com.project.daybart.demo.Users;
import com.project.daybart.demo.Subscription.Subscription;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.project.daybart.demo.Subscription.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    public final UsersRepository usersRepo;
    public final SubscriptionRepository subsRepo;

    // Crear usuarios, filtramos por email y usuario para evitar crear duplicados.
    public Optional<Users> createUser(Users users) {
        Optional<Users> existingEmail = usersRepo.findByEmail(users.getEmail());

        if (existingEmail.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }
        Optional<Users> existingUsername = usersRepo.findByUsername(users.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        else {
            String hashedPassword = BCrypt.hashpw(users.getPassword(), BCrypt.gensalt());
            users.setPassword(hashedPassword);
            users.setRole("trial");
            Users savedUser = usersRepo.save(users);

            Subscription subscription = new Subscription();
            subscription.setUser(savedUser); // Asignar el usuario a la suscripción
            subsRepo.save(subscription); // Guardar la suscripción en la base de datos

            return Optional.of(users);
        }
    }

    // Eliminar usuarios, cogeremos el id del token.
    public void deleteUser(Users users) {
        usersRepo.deleteById(users.getId());
    }

    // Herramienta de super usuario, buscar por usuario.
    public Optional<Users> findByUsername(String username) {
        return usersRepo.findByUsername(username);
    }

    // actualizar por id, lo cogeremos del token.
    public Optional<Users> updateUserByID(Users updatedUser) {
        Optional<Users> userOptional = usersRepo.findById(updatedUser.getId());

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setName(updatedUser.getName().isEmpty() ? user.getName() : updatedUser.getName());
            user.setLastName(updatedUser.getLastName().isEmpty() ? user.getLastName() : updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail().isEmpty() ? user.getEmail() : updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword().isEmpty() ? user.getPassword() : updatedUser.getPassword());

            usersRepo.save(user); // Guardar los cambios en el repositorio
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

}
