package br.com.tenorio.dm111projetofinal.repository;

import br.com.tenorio.dm111projetofinal.exception.InvalidDataException;
import br.com.tenorio.dm111projetofinal.exception.UserNotFoundException;
import br.com.tenorio.dm111projetofinal.model.User;
import com.google.appengine.api.datastore.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class UserRepository {

    private static final Logger log = Logger.getLogger("UserRepository");

    private static final String USER_KIND = "Users";
    private static final String USER_KEY = "UserKey";

    private static final String PROPERTY_GCM_TOKEN = "GcmToken";
    private static final String PROPERTY_EMAIL = "Email";
    private static final String PROPERTY_PASSWORD = "Password";
    private static final String PROPERTY_LAST_LOGIN = "LastLogin";
    private static final String PROPERTY_LAST_GCM_REGISTER = "LastGcmRegister";
    private static final String PROPERTY_ROLE = "Role";
    private static final String PROPERTY_CPF = "Cpf";
    private static final String PROPERTY_SALES_ID = "SalesId";
    private static final String PROPERTY_CRM_ID = "CrmId";

    private void userToEntity(User user, Entity userEntity) {

        userEntity.setProperty(PROPERTY_GCM_TOKEN, user.getGcmToken());
        userEntity.setProperty(PROPERTY_EMAIL, user.getEmail());
        userEntity.setProperty(PROPERTY_PASSWORD, user.getPassword());
        userEntity.setProperty(PROPERTY_LAST_LOGIN, user.getLastLogin());
        userEntity.setProperty(PROPERTY_LAST_GCM_REGISTER, user.getLastGcmRegister());
        userEntity.setProperty(PROPERTY_ROLE, user.getRole());
        userEntity.setProperty(PROPERTY_CPF, user.getCpf());
        userEntity.setProperty(PROPERTY_SALES_ID, user.getSalesId());
        userEntity.setProperty(PROPERTY_CRM_ID, user.getCrmId());
    }

    private User entityToUser(Entity userEntity) {

        User user = new User();

        user.setId(userEntity.getKey().getId());
        user.setGcmToken((String) userEntity.getProperty(PROPERTY_GCM_TOKEN));
        user.setEmail((String) userEntity.getProperty(PROPERTY_EMAIL));
        user.setPassword((String) userEntity.getProperty(PROPERTY_PASSWORD));
        user.setLastLogin((Date) userEntity.getProperty(PROPERTY_LAST_LOGIN));
        user.setLastGcmRegister((Date) userEntity.getProperty(PROPERTY_LAST_GCM_REGISTER));
        user.setRole((String) userEntity.getProperty(PROPERTY_ROLE));
        user.setCpf((String) userEntity.getProperty(PROPERTY_CPF));
        user.setSalesId((Long) userEntity.getProperty(PROPERTY_SALES_ID));
        user.setCrmId((Long) userEntity.getProperty(PROPERTY_CRM_ID));

        return user;
    }

    /**
     * Verifica se já existe um determinado CPF ou email cadastrado no Datastore.
     *
     * @return <code>true</code> se o CPF ou email já se encontra armazenado, ou <code>false</code> caso contrario
     */
    private boolean cpfOrEmailExists(String email, String cpf) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filterEmail = new Query.FilterPredicate(PROPERTY_EMAIL, Query.FilterOperator.EQUAL, email);
        Query.Filter filterCpf = new Query.FilterPredicate(PROPERTY_CPF, Query.FilterOperator.EQUAL, cpf);

        Query.Filter filter = Query.CompositeFilterOperator.or(filterEmail, filterCpf);

        Query query = new Query(USER_KIND).setFilter(filter);
        Entity userEntity = datastore.prepare(query).asSingleEntity();

        return userEntity != null;
    }

    /**
     * Armazena o usuario no Google Cloud Datastore, ou atualiza, caso o CPF já esteja armazenado.
     *
     * @param user usuario a ser armazenado.
     * @return o usuario.
     */
    public User saveOrUpdateUser(User user) throws InvalidDataException {

        // Validar se todos os campos obrigatórios foram enviados
        if(user.getEmail() == null || user.getEmail().isEmpty()
                || user.getRole() == null || (!user.getRole().equals("ADMIN") && !user.getRole().equals("USER"))
                || user.getCpf() == null || user.getCpf().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()) {

            throw new InvalidDataException("Faltando dados obrigatórios.");
        }

        // Update será realizado caso o CPF já tenha sido armazenado, já que este é
        // um registro unico que nao faz sentido ser modificado.
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter cpfFilter = new Query.FilterPredicate(PROPERTY_CPF, Query.FilterOperator.EQUAL, user.getCpf());
        Query query = new Query(USER_KIND).setFilter(cpfFilter);
        Entity userEntity = datastore.prepare(query).asSingleEntity();

        Date currentDate = new Date();
        if(userEntity == null) {
            // Armazenar

            // Verificar se email ou CPF já existe no datastore
            if(cpfOrEmailExists(user.getEmail(), user.getCpf())) {

                throw new InvalidDataException("CPF ou E-mail já existem.");

            } else {

                Key userKey = KeyFactory.createKey(USER_KIND, USER_KEY);
                userEntity = new Entity(USER_KIND, userKey);

                user.setSalesId(generateSalesId());
                user.setCrmId(generateCrmId());

                if(user.getGcmToken() == null) {
                    user.setGcmToken("");
                }

                user.setLastLogin(currentDate);
                user.setLastGcmRegister(currentDate);

                userToEntity(user, userEntity);
                datastore.put(userEntity);
                user.setId(userEntity.getKey().getId());
            }

        } else {
            // Atualizar

            User existentUser = entityToUser(userEntity);

            // Corrigir informacoes que sao geradas/buscadas.
            user.setLastLogin(currentDate);
            user.setLastGcmRegister(existentUser.getLastGcmRegister());
            user.setSalesId(existentUser.getSalesId());
            user.setCrmId(existentUser.getCrmId());

            if(user.getGcmToken() == null) {
                user.setGcmToken(existentUser.getGcmToken());
            }

            // Atualizar informacoes necessarias
            if(!existentUser.getGcmToken().equals(user.getGcmToken())) {
                user.setLastGcmRegister(currentDate);
            }

            userToEntity(user, userEntity);
            datastore.put(userEntity);
            user.setId(userEntity.getKey().getId());
        }

        return user;

    }

    /**
     * Busca um usuário armazenado por meio de seu email.
     *
     * @param email email do usuário a ser buscado.
     * @return o usuário.
     */
    public Optional<User> getByEmail(String email) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter filter = new Query.FilterPredicate(PROPERTY_EMAIL, Query.FilterOperator.EQUAL, email);
        Query query = new Query(USER_KIND).setFilter(filter);
        Entity userEntity = datastore.prepare(query).asSingleEntity();

        if (userEntity != null) {
            return Optional.ofNullable(entityToUser(userEntity));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Busca um usuário armazenado por meio de seu CPF.
     *
     * @param cpf CPF do usuário a ser buscado.
     * @return o usuário.
     */
    public Optional<User> getByCpf(String cpf) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter filter = new Query.FilterPredicate(PROPERTY_CPF, Query.FilterOperator.EQUAL, cpf);
        Query query = new Query(USER_KIND).setFilter(filter);
        Entity userEntity = datastore.prepare(query).asSingleEntity();

        if (userEntity != null) {
            return Optional.ofNullable(entityToUser(userEntity));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retorna todos os usuarios armazenados no Google Cloud Datasore
     *
     * @return uma lista contendo todos os usuários armazenados.
     */
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(USER_KIND).addSort(PROPERTY_EMAIL, Query.SortDirection.ASCENDING);
        List<Entity> userEntities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

        for (Entity userEntity : userEntities) {
            User user = entityToUser(userEntity);
            users.add(user);
        }
        return users;
    }

    /**
     * Remove um determinado usuario do Google Cloud Datastore.
     *
     * @param cpf CPF do usuário a ser removido.
     * @return o usuário removido.
     *
     * @throws UserNotFoundException
     */
    public User deleteUser(String cpf) throws UserNotFoundException {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter userFilter = new Query.FilterPredicate(PROPERTY_CPF, Query.FilterOperator.EQUAL, cpf);
        Query query = new Query(USER_KIND).setFilter(userFilter);
        Entity userEntity = datastore.prepare(query).asSingleEntity();

        if (userEntity != null) {
            datastore.delete(userEntity.getKey());
            return entityToUser(userEntity);
        } else {
            throw new UserNotFoundException("Usuário " + cpf + " não encontrado");
        }
    }

    /**
     * Atualiza um usuario no Google Cloud Datasore
     *
     * @param user usuario alterado
     * @return o usuario atualizado
     *
     * @throws UserNotFoundException
     */
    private User updateUser(User user) throws UserNotFoundException {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter emailFilter = new Query.FilterPredicate(PROPERTY_EMAIL, Query.FilterOperator.EQUAL, user.getEmail());
        Query query = new Query(USER_KIND).setFilter(emailFilter);
        Entity userEntity = datastore.prepare(query).asSingleEntity();

        if (userEntity != null) {
            userToEntity(user, userEntity);
            datastore.put(userEntity);
            user.setId(userEntity.getKey().getId());

            return user;
        } else {
            throw new UserNotFoundException("Usuário " +  user.getEmail() + " não encontrado");
        }
    }

    /*
     * Cria um usuario ADMIN assim que o repositorio é criado no GCD.
     */
    @PostConstruct
    public void init() {

        User adminUser;
        Optional<User> optAdminUser = this.getByEmail("caroltr.srs@gmail.com");
        try {
            if (optAdminUser.isPresent()) {

                log.fine("Atualizando usuario admin inicial");
                adminUser = optAdminUser.get();
                if (!adminUser.getRole().equals("ADMIN")) {
                    adminUser.setRole("ADMIN");
                }

                this.updateUser(adminUser);
            } else {
                log.fine("Cadastrando usuario admin inicial");
                adminUser = new User();
                adminUser.setRole("ADMIN");
                adminUser.setPassword("caroline");
                adminUser.setEmail("caroltr.srs@gmail.com");
                adminUser.setCpf("123.456.789-10");

                this.saveOrUpdateUser(adminUser);
            }
        } catch ( UserNotFoundException | InvalidDataException e) {

            log.severe("Falha ao criar usuário ADMIN: " + e.getMessage());
        }
    }

    /**
     * Gera um id de vendas.
     *
     * @return id de vendas gerado.
     */
    private long generateSalesId() {

        List<User> users = getUsers();

        if(users.isEmpty()) {
            return 0L;
        } else {

            long max = 0;
            for(User user : users) {
                long userSalesId = user.getSalesId();

                if(userSalesId > max) {
                    max = userSalesId;
                }
            }

            return max + 1;
        }
    }

    /**
     * Gera um id de CRM.
     *
     * @return id CRM gerado.
     */
    private long generateCrmId() {

        List<User> users = getUsers();

        if(users.isEmpty()) {
            return 0L;
        } else {
            long max = 0;
            for(User user : users) {
                long userSalesId = user.getCrmId();

                if(userSalesId > max) {
                    max = userSalesId;
                }
            }

            return max + 1;
        }
    }
}
