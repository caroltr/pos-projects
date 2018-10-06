package br.com.tenorio.dm111projetofinal.repository;

import br.com.tenorio.dm111projetofinal.exception.GCMRegistrationException;
import br.com.tenorio.dm111projetofinal.exception.InvalidDataException;
import br.com.tenorio.dm111projetofinal.exception.UserNotFoundException;
import br.com.tenorio.dm111projetofinal.model.ProductOfInterest;
import br.com.tenorio.dm111projetofinal.model.User;
import br.com.tenorio.dm111projetofinal.util.GCMSendMessage;
import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class ProductOfInterestRepository {

    private static final Logger log = Logger.getLogger("ProductOfInterestRepository");

    private static final String PRODUCT_KIND = "Products";
    private static final String PRODUCT_KEY = "ProductKey";

    private static final String PROPERTY_USER_ID = "UserId";
    private static final String PROPERTY_USER_CPF = "UserCpf";
    private static final String PROPERTY_PRODUCT_ID = "ProductId";
    private static final String PROPERTY_DESIRED_PRICE = "DesiredPrice";

    @Autowired
    private UserRepository userRepository;

    private void productToEntity(ProductOfInterest product, Entity userEntity) {

        userEntity.setProperty(PROPERTY_USER_ID, product.getUserSalesId());
        userEntity.setProperty(PROPERTY_USER_CPF, product.getUserCpf());
        userEntity.setProperty(PROPERTY_PRODUCT_ID, product.getProductId());
        userEntity.setProperty(PROPERTY_DESIRED_PRICE, product.getPrice());
    }

    private ProductOfInterest entityToProduct(Entity userEntity) {

        ProductOfInterest product = new ProductOfInterest();

        product.setUserSalesId(Long.parseLong(userEntity.getProperty(PROPERTY_USER_ID).toString()));
        product.setUserCpf((String) userEntity.getProperty(PROPERTY_USER_CPF));
        product.setProductId(Long.parseLong(userEntity.getProperty(PROPERTY_PRODUCT_ID).toString()));
        product.setPrice(Double.parseDouble(userEntity.getProperty(PROPERTY_DESIRED_PRICE).toString()));

        return product;
    }

    /**
     * Armazena um produto de interesse.
     *
     * @param product produto a ser armazenado.
     * @return o produto armazenado.
     *
     * @throws UserNotFoundException
     * @throws InvalidDataException
     */
    public ProductOfInterest saveProduct(ProductOfInterest product) throws UserNotFoundException, InvalidDataException {

        // Getting the user ID
        User user = getUser(product.getUserCpf());
        product.setUserSalesId(user.getSalesId());

        if(product.getPrice() <= 0 || product.getProductId() <= 0) {
            throw new InvalidDataException("Missing required data.");
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key productKey = KeyFactory.createKey(PRODUCT_KIND, PRODUCT_KEY);

        Query.Filter filterUserCpf = new Query.FilterPredicate(PROPERTY_USER_CPF, Query.FilterOperator.EQUAL, product.getUserCpf());
        Query.Filter filterProductId = new Query.FilterPredicate(PROPERTY_PRODUCT_ID, Query.FilterOperator.EQUAL, product.getProductId());
        Query.Filter compositeFilter = Query.CompositeFilterOperator.and(filterUserCpf, filterProductId);

        Query query = new Query(PRODUCT_KIND).setFilter(compositeFilter);

        Entity entity = datastore.prepare(query).asSingleEntity();

        if (entity == null) {
            entity = new Entity(PRODUCT_KIND, productKey);
        }

        productToEntity (product, entity);
        datastore.put(entity);

        return product;
    }

    /**
     * Busca todos os produtos de interesse de um determinado usuário, por meio de seu CPF.
     *
     * @param userCpf CPF do usuário.
     * @return uma lista contendo todos os produtos de interesse.
     *
     * @throws UserNotFoundException
     */
    public List<ProductOfInterest> getProductsByUser(String userCpf) throws UserNotFoundException {

        Optional<User> userOtp = userRepository.getByCpf(userCpf);

        if(userOtp.isPresent()) {

            List<ProductOfInterest> products = new ArrayList<>();
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            Query.Filter filter = new Query.FilterPredicate(PROPERTY_USER_CPF, Query.FilterOperator.EQUAL, userCpf);
            Query query = new Query(PRODUCT_KIND).setFilter(filter);
            List<Entity> productsEntities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

            log.info("Produtos: " + productsEntities.size());

            for (Entity userEntity : productsEntities) {
                ProductOfInterest product = entityToProduct(userEntity);
                products.add(product);
            }

            return products;
        } else {
            throw new UserNotFoundException("Usuario nao cadastrado.");
        }
    }

    /**
     * Remove um produto de interesse de um determinado usuário.
     *
     * @param userCpf CPF do usuário.
     * @param productId id do produto a ser removido.
     * @return o produto removido.
     *
     * @throws UserNotFoundException
     */
    public ProductOfInterest deleteProduct(String userCpf, long productId) throws UserNotFoundException {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter userFilter = new Query.FilterPredicate(PROPERTY_USER_CPF, Query.FilterOperator.EQUAL, userCpf);
        Query.Filter productFilter = new Query.FilterPredicate(PROPERTY_PRODUCT_ID, Query.FilterOperator.EQUAL, productId);

        Query.Filter compositeFilter = Query.CompositeFilterOperator.and(userFilter, productFilter);

        Query query = new Query(PRODUCT_KIND).setFilter(compositeFilter);
        Entity productEntity = datastore.prepare(query).asSingleEntity();

        if (productEntity != null) {
            datastore.delete(productEntity.getKey());
            return entityToProduct(productEntity);
        } else {
            throw new UserNotFoundException("Produto não encontrado.");
        }
    }

    /**
     * Atualiza o preço de um determinado produto, e envia uma notificação push para os usuários que
     * tem interesse neste produto e o valor desejado foi alcançado.
     *
     * @param productId id do produto a ser atualizado.
     * @param productNewPrice novo preço do produto.
     *
     * @throws UserNotFoundException
     */
    public void updateProductPrice(long productId, double productNewPrice) throws UserNotFoundException {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter productFilter = new Query.FilterPredicate(PROPERTY_PRODUCT_ID, Query.FilterOperator.EQUAL, productId);
        Query query = new Query(PRODUCT_KIND).setFilter(productFilter);
        List<Entity> productsEntities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

        if(productsEntities.isEmpty()) {
            log.info("Lista vazia");
            throw new UserNotFoundException("Produto não encontrado.");
        }

        for (Entity entity : productsEntities) {

            ProductOfInterest product = entityToProduct(entity);
            Double userDesiredPrice = product.getPrice();
            log.info("Preço desejado: " + userDesiredPrice);

            if(productNewPrice <= userDesiredPrice) {
                try {
                    sendMessage(product, getUser(product.getUserCpf()));
                    log.info("Mensagem enviada ao usuario " + product.getUserCpf());
                } catch (Exception e) {
                    log.info("Falha ao enviar mensagem: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Busca um usuário no Datastore por meio de seu CPF.
     *
     * @param cpf CPF do usuário a ser buscado.
     * @return o usuário encontrado.
     *
     * @throws UserNotFoundException
     */
    private User getUser(String cpf) throws UserNotFoundException {

        Optional<User> user = userRepository.getByCpf(cpf);

        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("Usuario nao encontrado");
        }

    }

    /**
     * Envia uma notificação push para um determinado usuário, contendo informações de um produto de
     * interesse.
     *
     * @param product produto de interesse a ser enviado na notificação.
     * @param user usuário para o qual se deseja enviar a notificação.
     *
     * @throws IOException
     * @throws GCMRegistrationException
     * @throws InvalidDataException
     */
    private void sendMessage(ProductOfInterest product, User user) throws IOException,
            GCMRegistrationException, InvalidDataException {

        Gson gson = new Gson();
        String message = gson.toJson(product);
        GCMSendMessage.sendMessage(user.getGcmToken(), "Produto", message);
    }
}
