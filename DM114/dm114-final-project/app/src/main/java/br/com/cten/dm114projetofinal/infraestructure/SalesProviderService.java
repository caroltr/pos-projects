package br.com.cten.dm114projetofinal.infraestructure;

import java.util.List;

import br.com.cten.dm114projetofinal.model.Order;
import br.com.cten.dm114projetofinal.model.Product;
import br.com.cten.dm114projetofinal.model.ProductDetail;
import br.com.cten.dm114projetofinal.model.Token;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SalesProviderService {

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Authorization: Basic c2llY29sYTptYXRpbGRl"
    })
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<Token> getToken(
            @Field("grant_type") String grantType,
            @Field("username") String userName,
            @Field("password") String password);

    @Headers("Content-Type: application/json")
    @GET("/api/orders/byemail")
    Call<List<Order>> getAllOrders(@Header("Authorization") String authorization, @Query("email") String email);

    // Buscar produtos
    @Headers("Content-Type: application/json")
    @GET("/api/products")
    Call<List<ProductDetail>> getAllProducts(@Header("Authorization") String authorization);

    // Buscar detalhes de um pedido
    @Headers("Content-Type: application/json")
    @GET("/api/products/{code}")
    Call<ProductDetail> getProduct(@Header("Authorization") String authorization, @Path("code") String code);
}
