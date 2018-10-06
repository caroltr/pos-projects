package br.com.cten.dm114projetofinal.infraestructure;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import br.com.cten.dm114projetofinal.model.Order;
import br.com.cten.dm114projetofinal.model.Product;
import br.com.cten.dm114projetofinal.model.ProductDetail;
import br.com.cten.dm114projetofinal.model.RequestProduct;
import br.com.cten.dm114projetofinal.model.Token;
import br.com.cten.dm114projetofinal.model.User;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class RequestsManager {

    private static String salesAccessToken;
    private static String messageAccessToken;

    private static String login;
    private static String password;

    private static RequestsManager sInstance;
    private static Context mContext;

    private RequestsManager() {}

    public static RequestsManager getInstance(Context ctx) {

        mContext = ctx;

        SharedPreferences prefs = ctx.getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE);

        if(sInstance == null) {
            sInstance = new RequestsManager();
        }

        login = prefs.getString("login", null);
        password = prefs.getString("password", null);

        salesAccessToken = prefs.getString("salesAccessToken", null);
        messageAccessToken = prefs.getString("messageAccessToken", null);

        return sInstance;
    }

    public void getSalesAccessToken(Callback<Token> callback) {

        SalesProviderService service = RetrofitClientInstance.getRetrofitInstance().create(SalesProviderService.class);
        Call<Token> call = service.getToken("password", login, password);

        call.enqueue(callback);
    }

    public void getMessageAccessToken(Callback<Token> callback) {

        MessageProviderService service = RetrofitClientInstance.getRetrofitMessageInstance().create(MessageProviderService.class);
        Call<Token> call = service.getToken("password", login, password);
        call.enqueue(callback);
    }

    public void getAllProducts(Callback<List<ProductDetail>> callback) {

        // Get all products
        SalesProviderService service = RetrofitClientInstance.getRetrofitInstance().create(SalesProviderService.class);
        Call<List<ProductDetail>> callInside = service.getAllProducts("Bearer " + salesAccessToken);
        callInside.enqueue(callback);
    }

    public void getProductDetails(String productCode, Callback<ProductDetail> callback) {

        // Get all products
        SalesProviderService service = RetrofitClientInstance.getRetrofitInstance().create(SalesProviderService.class);
        Call<ProductDetail> callInside = service.getProduct("Bearer " + salesAccessToken, productCode);
        callInside.enqueue(callback);
    }

    public void getAllUserProducts(Callback<List<Product>> callback) {

        MessageProviderService service = RetrofitClientInstance.getRetrofitMessageInstance().create(MessageProviderService.class);
        Call<List<Product>> call = service.getAllUserProducts("Bearer " + messageAccessToken, login);

        call.enqueue(callback);
    }

    public void addNewProduct(RequestProduct product, Callback<Product> callback) {

        MessageProviderService service = RetrofitClientInstance.getRetrofitMessageInstance().create(MessageProviderService.class);
        Call<Product> call = service.addProduct("Bearer " + messageAccessToken, product);

        call.enqueue(callback);
    }

    public void deleteProductOfInterest(Product product, Callback<Product> callback) {

        MessageProviderService service = RetrofitClientInstance.getRetrofitMessageInstance().create(MessageProviderService.class);
        Call<Product> call = service.deleteProduct("Bearer " + messageAccessToken, product.getId());

        call.enqueue(callback);
    }

    public void getAllOrders(Callback<List<Order>> callback) {

        SalesProviderService service = RetrofitClientInstance.getRetrofitInstance().create(SalesProviderService.class);
        Call<List<Order>> allOrdersCall = service.getAllOrders("Bearer " + salesAccessToken, login);

        allOrdersCall.enqueue(callback);
    }

    public void updateUserRegGCM(String regGCM, Callback<User> callback) {

        MessageProviderService service = RetrofitClientInstance.getRetrofitMessageInstance().create(MessageProviderService.class);
        Call<User> updateUserRegGCM = service.updateRegGCM("Bearer " + messageAccessToken, login, regGCM);

        updateUserRegGCM.enqueue(callback);
    }
}
