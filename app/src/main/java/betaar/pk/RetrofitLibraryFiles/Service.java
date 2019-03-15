package betaar.pk.RetrofitLibraryFiles;

import betaar.pk.Config.API;
import betaar.pk.Models.responseAddProduct;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {

    @Multipart
    @POST("post-product")
    Call<responseAddProduct> saleAnimal(@Part("key") RequestBody key,
                                        @Part("name") RequestBody productName,
                                        @Part("category") RequestBody category,
                                        @Part("subcategory") RequestBody subcategory,
                                        @Part("internalsubcategory") RequestBody internalsubcategory,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("unit") RequestBody unit,
                                        @Part("price") RequestBody price,
                                        @Part("description") RequestBody description,
                                        @Part("photo_type") RequestBody photo_type,
                                        @Part MultipartBody.Part image1,
                                        @Part MultipartBody.Part image2,
                                        @Part MultipartBody.Part image3,
                                        @Part MultipartBody.Part image4);

}
