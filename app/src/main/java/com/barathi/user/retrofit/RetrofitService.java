package com.barathi.user.retrofit;

import com.barathi.user.model.Address;
import com.barathi.user.model.ApplyCoupon;
import com.barathi.user.model.Area;
import com.barathi.user.model.BannerRoot;
import com.barathi.user.model.Cart;
import com.barathi.user.model.Categories;
import com.barathi.user.model.CategoryProduct;
import com.barathi.user.model.CityRoot;
import com.barathi.user.model.ComplainRoot;
import com.barathi.user.model.Coupon;
import com.barathi.user.model.FaqRoot;
import com.barathi.user.model.NotificationRoot;
import com.barathi.user.model.OrderDetailRoot;
import com.barathi.user.model.OrderRoot;
import com.barathi.user.model.ProductMain;
import com.barathi.user.model.RatingRoot;
import com.barathi.user.model.RestResponse;
import com.barathi.user.model.RestResponse2;
import com.barathi.user.model.Search;
import com.barathi.user.model.SearchCatProduct;
import com.barathi.user.model.ShippingChargeRoot;
import com.barathi.user.model.SortRoot;
import com.barathi.user.model.Summary;
import com.barathi.user.model.User;
import com.barathi.user.model.Wishlist;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetrofitService {


    @FormUrlEncoded
    @POST("User/userLogin")
    Call<User> loginUser(@Header("unique-key") String key,
                         @Field("mobile_no") String mobile,
                         @Field("device_type") String deviceType,
                         @Field("device_token") String deviceToken
    );


    @FormUrlEncoded
    @POST("User/registration")
    Call<User> registerUser(@Header("unique-key") String key,
                            @Field("mobile_no") String mobile_no,
                            @Field("first_name") String firstName,
                            @Field("last_name") String lastName,
                            @Field("email") String email,
                            @Field("login_type") String loginType,
                            @Field("identity") String identity,
                            @Field("device_type") String deviceType,
                            @Field("device_token") String deviceToken,
                            @Field("profile_image") String profileImage);

    @FormUrlEncoded
    @POST("Product/getProductList")
    Call<Categories> getAllData(@Header("unique-key") String key,
                                @Field("user_id") String uid,
                                @Field("start") int start,
                                @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Product/getProductByCategoryId")
    Call<CategoryProduct> getProductByCatogery(@Header("unique-key") String key,
                                               @Field("category_id") String cid,
                                               @Field("start") int start,
                                               @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Product/addupdateToCart")
    Call<RestResponse> addProductToCart(@Header("unique-key") String key,
                                        @Header("Authorization") String userToken,
                                        @Field("product_id[]") List<String> pids,
                                        @Field("price_unit_id[]") List<String> priceid,
                                        @Field("quantity[]") List<String> quantity);

    @GET("Product/getCartList")
    Call<Cart> getCardData(@Header("unique-key") String key,
                           @Header("Authorization") String userToken);

    @FormUrlEncoded
    @POST("Product/addupdateToWishlist")
    Call<RestResponse> addProductToWishlist(@Header("unique-key") String key,
                                            @Header("Authorization") String userToken,
                                            @Field("product_id") String pid);

    @FormUrlEncoded
    @POST("Product/removeProductFromCart")
    Call<RestResponse> removeFromCart(@Header("unique-key") String key,
                                      @Header("Authorization") String userToken,
                                      @Field("product_id") String pId,
                                      @Field("price_unit_id") String priceId);

    @Multipart
    @POST("User/updateProfile")
    Call<User> updateUser(@Header("unique-key") String key,
                          @Header("Authorization") String token,
                          @PartMap Map<String, RequestBody> partMap,
                          @Part MultipartBody.Part requestBody);


    @GET("Product/getWishlistList")
    Call<Wishlist> getWishlist(@Header("unique-key") String key,
                               @Header("Authorization") String userToken);

    @FormUrlEncoded
    @POST("Product/removeProductFromWishlist")
    Call<RestResponse> removeFromWishlist(@Header("unique-key") String key,
                                          @Header("Authorization") String userToken,
                                          @Field("product_id") String pId);

    @FormUrlEncoded
    @POST("Product/searchProduct")
    Call<Search> getSearch(@Header("unique-key") String key,
                           @Field("search_keyword") String keyword,
                           @Field("start") int start,
                           @Field("limit") int limit);

    @FormUrlEncoded
    @POST("User/addDeliveryAddress")
    Call<Address> addAddress(@Header("unique-key") String key,
                             @Header("Authorization") String token,
                             @FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("User/updateDeliveryAddress")
    Call<RestResponse> updateAddress(@Header("unique-key") String key,
                                     @Header("Authorization") String token,
                                     @FieldMap HashMap<String, String> data);

    @GET("User/getAllDeliveryAddress")
    Call<Address> getAllAddress(@Header("unique-key") String key,
                                @Header("Authorization") String userToken);

    @FormUrlEncoded
    @POST("Product/getProductById")
    Call<ProductMain> getOneProduct(@Header("unique-key") String key,
                                    @Field("user_id") String uid,
                                    @Field("product_id") String pid);


    @FormUrlEncoded
    @POST("Product/searchProductByCategory")
    Call<SearchCatProduct> getSearchCat(@Header("unique-key") String key,
                                        @Field("search_keyword") String keyword,
                                        @Field("category_id") String catId,
                                        @Field("limit") String limit);

    @FormUrlEncoded
    @POST("Order/getPaymentSummary")
    Call<Summary> getSummary(@Header("unique-key") String key,
                             @Header("Authorization") String userToken,
                             @Field("delivery_address_id") String addressId);

    @GET("Settings/getCityList")
    Call<CityRoot> getCity(@Header("unique-key") String key);

    @FormUrlEncoded
    @POST("Settings/getAreaByCity")
    Call<Area> getArea(@Header("unique-key") String key, @Field("city_id") String citiyid);


    @GET("Order/getCouponList")
    Call<Coupon> getCouponData(@Header("unique-key") String key,
                               @Header("Authorization") String userToken);

    @FormUrlEncoded
    @POST("Order/applyCoupon")
    Call<ApplyCoupon> applyCoupon(@Header("unique-key") String key,
                                  @Header("Authorization") String userToken,
                                  @Field("coupon_code") String couponCode,
                                  @Field("subtotal") String subtotal);

    @FormUrlEncoded
    @POST("Order/getMyOrderList")
    Call<OrderRoot> getMyOrders(@Header("unique-key") String key,
                                @Header("Authorization") String userToken,
                                @Field("start") int start,
                                @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Order/placeOrder")
    Call<RestResponse> placeOrder(@Header("unique-key") String key,
                                  @Header("Authorization") String userToken,
                                  @Field("total_amount") String totalAmount,
                                  @Field("payment_type") String patmentType,
                                  @Field("shipping_charge") String shipping,
                                  @Field("delivery_address_id") String address,
                                  @Field("address") String useraddress,
                                  @Field("latitude") String lat,
                                  @Field("longitude") String lang,
                                  @Field("product_id[]") List<String> pids,
                                  @Field("price_unit_id[]") List<String> priceid,
                                  @Field("quantity[]") List<String> quantity);

    @FormUrlEncoded
    @POST("Order/getOrderDetailsById")
    Call<OrderDetailRoot> getOrdersDetail(@Header("unique-key") String key,
                                          @Header("Authorization") String userToken,
                                          @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("Order/raiseComplaint")
    Call<RestResponse> raiseComplain(@Header("unique-key") String key,
                                     @Header("Authorization") String userToken,
                                     @Field("order_id") String orderId,
                                     @Field("title") String title,
                                     @Field("mobile_no") String mobileNo,
                                     @Field("description") String des);

    @GET("Settings/getBannerList")
    Call<BannerRoot> getBanner(@Header("unique-key") String key);


    @POST("User/Logout")
    Call<RestResponse2> logout(@Header("unique-key") String key,
                               @Header("Authorization") String userToken);

    @FormUrlEncoded
    @POST("Product/sortByProduct")
    Call<SortRoot> getSortAll(@Header("unique-key") String key,
                              @Field("sort_by") String sortBy,
                              @Field("limit") int limit,
                              @Field("start") int start,
                              @Field("user_id") String uid, @Field("search_keyword") String keyword);

    @FormUrlEncoded
    @POST("Product/sortByProduct")
    Call<SortRoot> getSortCategory(@Header("unique-key") String key,
                                   @Field("sort_by") String sortBy,
                                   @Field("limit") int limit,
                                   @Field("start") int start,
                                   @Field("user_id") String uid,
                                   @Field("search_keyword") String keyword,
                                   @Field("category_id") String catId);

    @FormUrlEncoded
    @POST("Order/getAllComplaint")
    Call<ComplainRoot> getComplainData(@Header("unique-key") String key,
                                       @Header("Authorization") String userToken,
                                       @Field("limit") int limit,
                                       @Field("start") int start);


    @FormUrlEncoded
    @POST("User/deleteDeliveryAddress")
    Call<RestResponse> deleteDeliveryAddress(@Header("unique-key") String key,
                                             @Header("Authorization") String userToken,
                                             @Field("delivery_address_id") String dId);


    @FormUrlEncoded
    @POST("Order/cancelledOrder")
    Call<RestResponse> cancelOrder(@Header("unique-key") String key,
                                   @Header("Authorization") String userToken,
                                   @Field("order_id") String oId);

    @GET("User/getDefaultDeliveryDetails")
    Call<Address> getDefaultAddress(@Header("unique-key") String key,
                                    @Header("Authorization") String userToken);

    @FormUrlEncoded
    @POST("getAllNotification")
    Call<NotificationRoot> getNotifications(@Header("unique-key") String key,
                                            @Header("Authorization") String userToken,
                                            @Field("start") int start,
                                            @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Order/getAllOrderRating")
    Call<RatingRoot> getRatings(@Header("unique-key") String key,
                                @Header("Authorization") String userToken,
                                @Field("start") int start,
                                @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Order/orderReviewRating")
    Call<RestResponse> setRating(@Header("unique-key") String key,
                                 @Header("Authorization") String userToken,
                                 @Field("rating") int start,
                                 @Field("order_id") String oId,
                                 @Field("review") String review);

    @GET("Order/getShippingCharge")
    Call<ShippingChargeRoot> getShippingCharge(@Header("unique-key") String key,
                                               @Header("Authorization") String userToken);


    @FormUrlEncoded
    @POST("v1/customers")
    Call<JsonObject> getCustomer(@Header("Authorization") String userToken,
                                 @Field("address[line1]") String adress1, @Field("address[line2]") String adress2,
                                 @Field("address[city]") String city,
                                 @Field("address[state]") String state,
                                 @Field("address[postal_code]") String postalcode,
                                 @Field("address[country]") String country,
                                 @Field("email") String email,
                                 @Field("phone") String mobile,
                                 @Field("name") String name,
                                 @Field("description") String description);

    @FormUrlEncoded
    @POST("v1/payment_intents")
    Call<JsonObject> getPaymentIntent(@Header("Authorization") String userToken,
                                      @Field("currency") String currency,
                                      @Field("description") String description,
                                      @Field("customer") String customer,
                                      @Field("amount") String amount);


    @GET("Settings/getFAQList")
    Call<FaqRoot> getFaqs(@Header("unique-key") String key);
}
