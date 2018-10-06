package br.com.cten.dm114projetofinal.infraestructure;

import java.util.List;

import br.com.cten.dm114projetofinal.model.Product;
import br.com.cten.dm114projetofinal.model.RequestProduct;
import br.com.cten.dm114projetofinal.model.Token;
import br.com.cten.dm114projetofinal.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessageProviderService {

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

    // Atualizacao de registro GCM
    @PUT("/api/users/updatereggcm")
    Call<User> updateRegGCM(@Header("Authorization") String authorization, @Query("email") String email, @Query("reggcm") String reggcm);

    // Buscar produtos de interesse
    @Headers("Content-Type: application/json")
    @GET("/api/products/byemail")
    Call<List<Product>> getAllUserProducts(@Header("Authorization") String authorization, @Query("email") String email);

    // Adicionar produto de interesse
    @Headers("Content-Type: application/json")
    @POST("/api/products")
    Call<Product> addProduct(@Header("Authorization") String authorization, @Body RequestProduct product);

    // Remover produto de interesse
    @Headers("Content-Type: application/json")
    @DELETE("/api/products/{id}")
    Call<Product> deleteProduct(@Header("Authorization") String authorization, @Path("id") long id);

}
