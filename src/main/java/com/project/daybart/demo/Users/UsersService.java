package com.project.daybart.demo.Users;
import com.project.daybart.demo.Subscription.Subscription;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.project.daybart.demo.Subscription.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    public final UsersRepository usersRepo;
    public final SubscriptionRepository subsRepo;

    // Crear usuarios, filtramos por email y usuario para evitar crear duplicados.
    public ResponseEntity<String> createUser(Users users) {
        try {
            Optional<Users> existingEmail = usersRepo.findByEmail(users.getEmail());

            if (existingEmail.isPresent()) {
                return new ResponseEntity<>("El correo electrónico ya está en uso", HttpStatus.BAD_REQUEST);
            }
            Optional<Users> existingUsername = usersRepo.findByUsername(users.getUsername());
            if (existingUsername.isPresent()) {
                return new ResponseEntity<>("El nombre de usuario ya está en uso", HttpStatus.BAD_REQUEST);
            } else {
                String hashedPassword = BCrypt.hashpw(users.getPassword(), BCrypt.gensalt());
                users.setPassword(hashedPassword);
                users.setRole("trial");
                Users savedUser = usersRepo.save(users);

                Subscription subscription = new Subscription();
                subscription.setUser(savedUser); // Asignar el usuario a la suscripción
                subsRepo.save(subscription); // Guardar la suscripción en la base de datos

                return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar usuarios, cogeremos el id del token.
    public ResponseEntity<String> deleteUser(Users users) {
        try {
            usersRepo.deleteById(users.getId());
            return new ResponseEntity<>("Usuario eliminado",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Herramienta de super usuario, buscar por usuario.
    public ResponseEntity<Users> findByUsername(String username) {
        try {
            Optional<Users> findUser = usersRepo.findByUsername(username);

            if (findUser.isPresent()) {
                return new ResponseEntity<>(findUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // actualizar por id, lo cogeremos del token.
    public ResponseEntity<String> updateUserByID(Users updatedUser) {
        try {
            Optional<Users> userOptional = usersRepo.findById(updatedUser.getId());

            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                user.setName(updatedUser.getName().isEmpty() ? user.getName() : updatedUser.getName());
                user.setLastName(updatedUser.getLastName().isEmpty() ? user.getLastName() : updatedUser.getLastName());
                user.setEmail(updatedUser.getEmail().isEmpty() ? user.getEmail() : updatedUser.getEmail());
                user.setPassword(updatedUser.getPassword().isEmpty() ? user.getPassword() : updatedUser.getPassword());

                usersRepo.save(user); // Guardar los cambios en el repositorio
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
