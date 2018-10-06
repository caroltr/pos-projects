package br.com.cten.dm114projetofinal.GCM;

import java.io.IOException;

public interface GCMRegisterEvents {

    void gcmRegisterFinished (String registrationID);
    void gcmRegisterFailed (IOException ex);
    void gcmUnregisterFinished ();
    void gcmUnregisterFailed (IOException ex);
}
