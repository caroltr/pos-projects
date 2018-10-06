package br.com.tenorio.dm111projetofinal.controller;

import br.com.tenorio.dm111projetofinal.exception.GCMRegistrationException;
import br.com.tenorio.dm111projetofinal.exception.InvalidDataException;
import br.com.tenorio.dm111projetofinal.model.Order;
import br.com.tenorio.dm111projetofinal.model.User;
import br.com.tenorio.dm111projetofinal.repository.UserRepository;
import br.com.tenorio.dm111projetofinal.util.GCMSendMessage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/message")
public class MessageController {

    private static final Logger log = Logger.getLogger("MessageController");

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "/sendOrder")
    public ResponseEntity<String> sendOrder(@RequestBody Order order) {

        Optional<User> optUser = userRepository.getByCpf(order.getUserCpf());

        if (optUser.isPresent()) {

            User user = optUser.get();
            try {
                sendMessage(order, user);
            } catch (IOException e) {
                log.severe("Falha ao enviar mensagem");
                return new ResponseEntity<>("Falha ao enviar a mensagem", HttpStatus.PRECONDITION_FAILED);
            } catch (GCMRegistrationException e) {
                log.severe("Falha ao enviar mensagem ao usuario " + user.getEmail() + ": " + e.getMessage());
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (InvalidDataException e) {
                log.severe("Falha ao enviar mensagemao usuario " + user.getEmail() + ": " + e.getMessage());
                return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
            }

            log.info("Mensagem enviada ao usuario " + user.getEmail());
            return new ResponseEntity<>("Mensagem enviada ao usuario " + optUser.get().getEmail(), HttpStatus.OK);

        } else {
            log.severe("Usuário de CPF " + order.getUserCpf() + "não encontrado");
            return new ResponseEntity<>("Usuário não encontrado",
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Envia uma notificação push para um determinado usuário, contendo informações de um pedido específico.
     *
     * @param order pedido a ser enviado na notificação.
     * @param user usuário para o qual se deseja enviar a notificação.
     *
     * @throws IOException
     * @throws GCMRegistrationException
     * @throws InvalidDataException
     */
    private void sendMessage(Order order, User user) throws IOException,
            GCMRegistrationException, InvalidDataException {

        // Adicionando informacoes do usuario e o ID do pedido
        order.setId(generateProductId());
        order.setUserSalesId(user.getSalesId());

        Gson gson = new Gson();
        String message = gson.toJson(order);
        GCMSendMessage.sendMessage(user.getGcmToken(), "Pedido", message);
    }

    private long generateProductId() {

        int max = 1000;
        int min = 0;

        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}