package br.com.tenorio.dm111projetofinal.controller;

import br.com.tenorio.dm111projetofinal.exception.InvalidDataException;
import br.com.tenorio.dm111projetofinal.exception.UserNotFoundException;
import br.com.tenorio.dm111projetofinal.model.ProductOfInterest;
import br.com.tenorio.dm111projetofinal.model.User;
import br.com.tenorio.dm111projetofinal.repository.ProductOfInterestRepository;
import br.com.tenorio.dm111projetofinal.repository.UserRepository;
import br.com.tenorio.dm111projetofinal.util.CheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/productsOfInterest")
public class ProductsOfInterestController {

    private static final Logger log = Logger.getLogger("ProductsOfInterestController");

    @Autowired
    private ProductOfInterestRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping
    public ResponseEntity resgisterProductOfInteres(@RequestBody ProductOfInterest product,
                                                                       Authentication authentication) {

        try {

            if(isOperationAllowed(authentication, product.getUserCpf())) {
                return new ResponseEntity<>(productRepository.saveProduct(product), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping(path = "/byCpf")
    public ResponseEntity getProducts(@RequestParam String cpf,
                                      Authentication authentication) {

        try {
            if (isOperationAllowed(authentication, cpf)) {
                return new ResponseEntity<>(productRepository.getProductsByUser(cpf), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping(path = "/byCpf")
    public ResponseEntity deleteProduct(@RequestParam String cpf,
                                                           @RequestParam long productId,
                                                           Authentication authentication) {

        try {
            if(isOperationAllowed(authentication, cpf)) {
                return new ResponseEntity<>(productRepository.deleteProduct(cpf, productId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity updateProductPrice(@RequestParam long productId,
                                                                      @RequestParam double newPrice) {
        try {
            productRepository.updateProductPrice(productId, newPrice);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
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
    private boolean isOperationAllowed(Authentication authentication, String cpf) throws UserNotFoundException {
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
