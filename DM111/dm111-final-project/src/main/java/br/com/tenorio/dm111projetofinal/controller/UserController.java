package br.com.tenorio.dm111projetofinal.controller;

import br.com.tenorio.dm111projetofinal.exception.InvalidDataException;
import br.com.tenorio.dm111projetofinal.exception.UserNotFoundException;
import br.com.tenorio.dm111projetofinal.model.User;
import br.com.tenorio.dm111projetofinal.repository.UserRepository;
import br.com.tenorio.dm111projetofinal.util.CheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity saveUser(@RequestBody User user) {

        try {
            return new ResponseEntity<>(userRepository.saveOrUpdateUser(user), HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/byEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email, Authentication authentication) {

        if (isOperationAllowedByEmail(authentication, email)) {
            Optional<User> optUser = userRepository.getByEmail(email);

            if (optUser.isPresent()) {
                return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/byCpf")
    public ResponseEntity<User> getUserByCpf(@RequestParam String cpf, Authentication authentication) {

        try {
            if (isOperationAllowedByCpf(authentication, cpf)) {
                Optional<User> optUser = userRepository.getByCpf(cpf);

                if (optUser.isPresent()) {
                    return new ResponseEntity<>(optUser.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<User> getUsers() {

        return userRepository.getUsers();
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/byCpf")
    public ResponseEntity<User> deleteUser(@RequestParam String cpf, Authentication authentication) {

        try {
            if (isOperationAllowedByCpf(authentication, cpf)) {
                return new ResponseEntity<>(userRepository.deleteUser(cpf), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Verifica se é permitido que o usuário autenticado realize uma determinada operação.
     *
     * @param authentication objeto que contém informações do usuário autenticado.
     * @param email email do usuário a ser comparado.
     * @return <code>true</code> se o usuário é permitido, <code>false</code> caso contrário.
     */
    private boolean isOperationAllowedByEmail(Authentication authentication, String email) {

        boolean hasRoleAdmin = CheckRole.hasRoleAdmin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return hasRoleAdmin || userDetails.getUsername().equals(email);
    }

    /**
     * Verifica se é permitido que o usuário autenticado realize uma determinada operação.
     *
     * @param authentication objeto que contém informações do usuário autenticado.
     * @param cpf CPF do usuário a ser comparado.
     * @return <code>true</code> se o usuário é permitido, <code>false</code> caso contrário.
     *
     * @throws UserNotFoundException
     */
    private boolean isOperationAllowedByCpf(Authentication authentication, String cpf) throws UserNotFoundException {
        boolean hasRoleAdmin = CheckRole.hasRoleAdmin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> user = userRepository.getByEmail(userDetails.getUsername());

        if (user.isPresent()) {
            String userCpf = user.get().getCpf();
            return hasRoleAdmin || userCpf.equals(cpf);
        } else {
            throw new UserNotFoundException("Usuario nao encontrado");
        }
    }
}

