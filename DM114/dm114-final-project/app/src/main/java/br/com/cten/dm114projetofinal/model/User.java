package br.com.cten.dm114projetofinal.model;

public class User {

    /*

    "id": 5629499534213120,
    "email": "doralice@siecola.com.br",
    "password": "doralice",
    "gcmRegId": {reggcm},
    "lastLogin": null,
    "lastGCMRegister": null,
    "role": "USER",
    "enabled": true

    * */

    private String id;
    private String email;
    private String password;
    private String gcmRegId;
    private String lastLogin;
    private String lastGCMRegister;
    private String role;
    private String enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGcmRegId() {
        return gcmRegId;
    }

    public void setGcmRegId(String gcmRegId) {
        this.gcmRegId = gcmRegId;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastGCMRegister() {
        return lastGCMRegister;
    }

    public void setLastGCMRegister(String lastGCMRegister) {
        this.lastGCMRegister = lastGCMRegister;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
