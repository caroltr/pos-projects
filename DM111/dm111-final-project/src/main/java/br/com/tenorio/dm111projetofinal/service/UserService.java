package br.com.tenorio.dm111projetofinal.service;

import br.com.tenorio.dm111projetofinal.model.User;
import br.com.tenorio.dm111projetofinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    private static final Logger log = Logger.getLogger("UserService");

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optUser = userRepository.getByEmail(email);

        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            log.info("Usuario " + email + " nao encontrado.");
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
