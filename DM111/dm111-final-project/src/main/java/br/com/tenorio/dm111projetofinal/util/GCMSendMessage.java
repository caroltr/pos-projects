package br.com.tenorio.dm111projetofinal.util;

import br.com.tenorio.dm111projetofinal.exception.GCMRegistrationException;
import br.com.tenorio.dm111projetofinal.exception.InvalidDataException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.logging.Logger;

public class GCMSendMessage {

    private static final String API_KEY = "AIzaSyC0eJ3DKvzL_de9fen67UIGTgfRGqE4EOs";
    private static final Logger log = Logger.getLogger("GCMSendMessage");

    /**
     * Envia uma notificação push para o celular do usuário.
     *
     * @param gcmToken registro único do celular do usuário no GCM.
     * @param key string para identificar a notificação.
     * @param message mensagem a ser enviada ao usuário.
     *
     * @throws IOException
     * @throws GCMRegistrationException
     * @throws InvalidDataException
     */
    public static void sendMessage(String gcmToken, String key, String message) throws IOException,
            GCMRegistrationException, InvalidDataException {

        Sender sender = new Sender(API_KEY);
        Message msgBuilder = new Message.Builder().addData(key, message).build();

        if (gcmToken == null || gcmToken.isEmpty()) {
            throw new InvalidDataException("Celular do usuario nao está cadastrado.");
        } else {
            Result result = sender.send(msgBuilder, gcmToken, 5);

            if (result.getMessageId() != null) {
                String canonicalRegId = result.getCanonicalRegistrationId();

                if (canonicalRegId != null) {
                    log.severe("Usuário com mais de um registro");
                    throw new GCMRegistrationException("Usuário com mais de um registro");
                }

            } else {
                String error = result.getErrorCodeName();
                log.severe("Usuário não registrado");
                log.severe(error);

                throw new GCMRegistrationException("Usuário não registrado no GCM");
            }
        }
    }
}
