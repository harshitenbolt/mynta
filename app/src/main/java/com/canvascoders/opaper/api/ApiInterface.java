package com.canvascoders.opaper.api;


import com.canvascoders.opaper.Beans.AddDelBoysReponse.AddDelBoyResponse;

import com.canvascoders.opaper.Beans.BankDetailResp;
import com.canvascoders.opaper.Beans.ChangeMobileResponse.ChangeMobileResponse;
import com.canvascoders.opaper.Beans.CheckEsignResponse.CheckEsignResponse;
import com.canvascoders.opaper.Beans.CommentListResponse.CommentListResponse;
import com.canvascoders.opaper.Beans.CommentResponse.CommentResponse;
import com.canvascoders.opaper.Beans.DelBoysNextScreenResponse.DelBoysNextResponse;
import com.canvascoders.opaper.Beans.DelBoysResponse.DelBoyResponse;
import com.canvascoders.opaper.Beans.DeliveryBoysListResponse.DeliveryboyListResponse;
import com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse.DrivingLicenceDetailResponse;
import com.canvascoders.opaper.Beans.EditUserResponse.EditUserResponse;
import com.canvascoders.opaper.Beans.GeneralSupportResponse.GeneralSupportResponse;
import com.canvascoders.opaper.Beans.GenerateResetPWResponse.GenerateResetPWResponse;
import com.canvascoders.opaper.Beans.GetAgentDetailResponse.GetAgentDetailResponse;
import com.canvascoders.opaper.Beans.GetPanDetailsResponse.GetPanDetailsResponse;
import com.canvascoders.opaper.Beans.GetVendorTypeDetails;
import com.canvascoders.opaper.Beans.NotificationResponse.NotificattionResponse;
import com.canvascoders.opaper.Beans.PanCardOcrResponse.PanCardSubmitResponse;
import com.canvascoders.opaper.Beans.PanImageResponse.PanImageResponse;
import com.canvascoders.opaper.Beans.PancardVerifyResponse.CommonResponse;
import com.canvascoders.opaper.Beans.ResendOTPResponse.ResendOTPResponse;
import com.canvascoders.opaper.Beans.ResetPassResponse.ResetPassResponse;
import com.canvascoders.opaper.Beans.ResignAgreeDetailResponse.ResignAgreeDetailResponse;
import com.canvascoders.opaper.Beans.ResignAgreementResponse.ResignAgreementResponse;
import com.canvascoders.opaper.Beans.SearchListResponse.SearchListResponse;
import com.canvascoders.opaper.Beans.SendInvoiceEsignResponse.SendInvoiceLinkresponse;
import com.canvascoders.opaper.Beans.SignedDocDetailResponse.SignedDocDetailResponse;
import com.canvascoders.opaper.Beans.SubmitReportResponse.SubmitReportResponse;
import com.canvascoders.opaper.Beans.SupportDetailResponse.SupportDetailResponse;
import com.canvascoders.opaper.Beans.SupportListResponse.SupportListResponse;
import com.canvascoders.opaper.Beans.SupportSubjectResponse.SupportSubjectResponse;
import com.canvascoders.opaper.Beans.ThreadCommentsResponse.CommentThreadResponse;
import com.canvascoders.opaper.Beans.UpdatePanDetailResponse.UpdatePanDetailResponse;
import com.canvascoders.opaper.Beans.UpdatePanResponse.UpdatePancardResponse;
import com.canvascoders.opaper.Beans.UserDetailTResponse.GetUserDetails;
import com.canvascoders.opaper.Beans.VendorListResponse.VendorListResponse;
import com.canvascoders.opaper.Beans.VoterDlOCRSubmitResponse.ApiSubmitOCRPanVoterDlResponse;
import com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse.VoterOCRGetDetaisResponse;
import com.canvascoders.opaper.Beans.bizdetails.GetUserDetailResponse;
import com.canvascoders.opaper.Beans.dc.GetDC;
import com.canvascoders.opaper.Beans.otp.GetOTP;
import com.canvascoders.opaper.Beans.verifylocation.GetLocationResponse;
import com.canvascoders.opaper.Beans.verifymobile.GetMobileResponse;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiInterface {

    //-----------------------------------------------------------------------------
    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("auth/agent")
    Call<GetUserDetails> getUserSignin(@Body JsonObject data);

    //-----------------------------------------------------------------------------
    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("generate-otp")
    Call<GetOTP> sendOTP(@Header("Authorization") String token, @Body JsonObject data);

    @FormUrlEncoded
    @POST("auth/agent/reset-password-otp")
    Call<GenerateResetPWResponse> sendCodeReset(@Field("mobile_number") String mobile_number);

    @FormUrlEncoded
    @POST("get-vendor-type")
    Call<GetVendorTypeDetails> getVendorType(@Header("Authorization") String token, @Field("proccess_id") String mobile_number);


    @FormUrlEncoded
    @POST("agreement-resign")
    Call<ResignAgreementResponse> ResignAgreement(@Header("Authorization") String token, @Field("proccess_id") String data);


    @FormUrlEncoded
    @POST("get-signed-doc")
    Call<SignedDocDetailResponse> signedDocDetailResponse(@Header("Authorization") String token, @FieldMap Map<String, String> param);


    @FormUrlEncoded
    @POST("auth/agent/reset-password-request")
    Call<ResetPassResponse> ResetPassword(@FieldMap Map<String, String> data);


    @Multipart
    @POST("pan-card-detail")
    Call<GetPanDetailsResponse> getPanDetails(@PartMap() Map<String, String> data, @Part MultipartBody.Part attachment);

    @FormUrlEncoded
    @POST
    Call<VendorListResponse> vendor_list(@Url String url, @Header("Authorization") String Header, @Field("agent_id") String data);

    //-----------------------------------------------------------------------------
    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("verify-mobile")
    Call<GetMobileResponse> verifyMobile(@Header("Authorization") String token, @Body JsonObject data);

    //-----------------------------------------------------------------------------
    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("verify-location")
    Call<GetLocationResponse> verifyLocation(@Header("Authorization") String token, @Body JsonObject data);

    //-----------------------------------------------------------------------------

    @POST("esign-single-invoice")
    Call<JsonObject> Storeinvoice(@Header("Authorization") String token, @QueryMap() Map<String, String> data);

    //-----------------------------------------------------------------------------
    //@FormUrlEncoded
    @POST("nocesign")
    Call<JsonObject> StoreNocDocument(@Header("Authorization") String token, @QueryMap Map<String, String> data);

    //-----------------------------------------------------------------------------

    // @FormUrlEncoded
    @POST("esign")
    Call<JsonObject> StoreAgreement(@Header("Authorization") String token, @QueryMap Map<String, String> data);

    //-----------------------------------------------------------------------------
    // @FormUrlEncoded
    @POST("gstesign")
    Call<JsonObject> StoreGstDocument(@Header("Authorization") String token, @QueryMap Map<String, String> data);

    //-----------------------------------------------------------------------------

//store aadhar
//    @Multipart
//    @POST("verify-aadhaar-card")
//    Call<GetStoreAadharResult> storeAadhar(@Part MultipartBody.Part file, @Part("adhar_card_front") RequestBody adhar_card_front, @Part("description") RequestBody desc, @Part("user_id") RequestBody user_id, @Part("product_id") RequestBody pid, @Part("is_own_product") RequestBody ownp);


    @FormUrlEncoded
    @POST("pan-card-detail/from-user-side")
    Call<PanCardSubmitResponse> SubmitPancardOCR(@FieldMap Map<String, String> apiVersionMap);


    @FormUrlEncoded
    @POST("user-side-response")
    Call<ApiSubmitOCRPanVoterDlResponse> submitDlorVoter(@FieldMap Map<String, String> apiVersionMap);

    //-----------------------------------------------------------------------------
    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("submit-details")
    Call<GetUserDetailResponse> submitBizDetails(@Header("Authorization") String token, @Body JsonObject data);


    //-----------------------------------------------------------------------------

    @POST("submit-details-validation-{NUMERIC}")
    Call<GetUserDetailResponse> submitBizDetailsValid1(@Header("Authorization") String token, @Path("NUMERIC") String URL, @Body JsonObject data);


    //-----------------------------------------------------------------------------

    //@Headers({"Content-type: application/json", "Accept: */*"})
   /* @POST("resign-rate-update")
    Call<GetUserDetailResponse> submitRateUpdate(@Body JsonObject data);*/


    @POST("resign-rate-update")
    Call<ResponseBody> submitRateUpdate(@Header("Authorization") String token, @Body JsonObject data);
    //-----------------------------------------------------------------------------


    //@Headers({"Content-type: application/json", "Accept: */*"})
    @POST("get-dc")
    Call<GetDC> getDC(@Header("Authorization") String token, @Body JsonObject data);

    @FormUrlEncoded
    @POST("support-subject")
    Call<SupportSubjectResponse> getSubject(@Header("Authorization") String token, @FieldMap Map<String, String> param);


    //-----------------------------------------------------------------------------

//    buildernew.addFormDataPart("token", sessionManager.getToken());
//                        buildernew.addFormDataPart("proccess_id", Constants.processID);
//                        buildernew.addFormDataPart("pan", panImagepath.substring(panImagepath.lastIndexOf("/") + 1), RequestBody.create(MEDIA_TYPE_PNG, new File(panImagepath)));
//
//                        buildernew.addFormDataPart("adhar_card_front", aadharImagepathFront.substring(aadharImagepathFront.lastIndexOf("/") + 1), RequestBody.create(MEDIA_TYPE_PNG, new File(aadharImagepathFront)));
//                        buildernew.addFormDataPart("adhar_card_back", aadharImagepathBack.substring(aadharImagepathBack.lastIndexOf("/") + 1), RequestBody.create(MEDIA_TYPE_PNG, new File(aadharImagepathBack)));
//                        buildernew.addFormDataPart("if_shop_act", String.valueOf(inAct));
//                        buildernew.addFormDataPart("shop_image", shopImg.substring(shopImg.lastIndexOf("/") + 1), RequestBody.create(MEDIA_TYPE_PNG, new File(shopImg)));
//
//
//                        for (int i = 0; i < shopActImage.size(); i++) {
//        buildernew.addFormDataPart(shop_act, shopActImage.get(i).substring(shopActImage.get(i).lastIndexOf("/") + 1), RequestBody.create(MEDIA_TYPE_PNG, new File(shopActImage.get(i))));
//    }
//
//                        for (int j = 0; j < cnclChkImage.size(); j++) {
//        buildernew.addFormDataPart("cancelled_cheque[]", cnclChkImage.get(j).substring(cnclChkImage.get(j).lastIndexOf("/") + 1), RequestBody.create(MEDIA_TYPE_PNG, new File(cnclChkImage.get(j))));
//    }


    @POST("get-pancard-ocr-url")
    @Multipart
    Call<PanImageResponse> getPancardOcrUrl(@Header("Authorization") String auth, @Part("token") String token,
                                            @Part("proccess_id") String proccess_id,
                                            @Part MultipartBody.Part attachment);

    //-----------------------------------------------------------------------------

   /* @Multipart
    @POST("verify-aadhaar-card")
    Call<CommonResponse> getstoreAadhar(@Header("Authorization")String token,@PartMap() Map<String, String> data,
                                        @Part MultipartBody.Part aadharcard_front,
                                        @Part MultipartBody.Part aadharcard_back);*/

    @Multipart
    @POST("verify-kyc")
    Call<CommonResponse> getstoreAadhar(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                        @Part MultipartBody.Part aadharcard_front,
                                        @Part MultipartBody.Part aadharcard_back);


    @Multipart
    @POST("voter-id-detail")
    Call<VoterOCRGetDetaisResponse> getVoterIdOCR(@PartMap() Map<String, String> data,
                                                  @Part MultipartBody.Part voter_front,
                                                  @Part MultipartBody.Part voter_back);

    @Multipart
    @POST("driving-licence-detail")
    Call<DrivingLicenceDetailResponse> getDrivingLicenceDetail(@PartMap() Map<String, String> data,
                                                               @Part MultipartBody.Part voter_front);
    //-----------------------------------------------------------------------------

    @Multipart
    @POST("verify-pan-card")
    Call<CommonResponse> getstorePancard(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                         @Part MultipartBody.Part pancard);


    @Multipart
    @POST("update-pan-details")
    Call<UpdatePancardResponse> updatePanDetails(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                                 @Part MultipartBody.Part pancard);
    //-----------------------------------------------------------------------------

    @Multipart
    @POST("verify-cheque")
    Call<CommonResponse> getstoreCheque(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                        @Part MultipartBody.Part cheque);


    @Multipart
    @POST("bank-details-updation")
    Call<CommonResponse> getStoreChequeUpdated(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                               @Part MultipartBody.Part cheque);
    //-----------------------------------------------------------------------------

    @Multipart
    @POST("shop-act-upload")
    Call<CommonResponse> getstoreDocument(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                          @Part MultipartBody.Part store_image,
                                          @Part MultipartBody.Part[] store_image_act, @Part MultipartBody.Part owner_img_act);





    // api call for add dilvery boys
    @Multipart
    @POST("delivery-boys-store")
    Call<AddDelBoyResponse> addDelBoys(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                       @Part MultipartBody.Part image, @Part MultipartBody.Part driving_licence);


    @Multipart
    @POST("delivery-boys-details-stage-validation{NUMERIC}")
    Call<AddDelBoyResponse> DeliveryBoysDetailsValid1(@Header("Authorization") String token, @Path("NUMERIC") String URL, @PartMap() Map<String, String> data, @Part MultipartBody.Part image);


    @Multipart
    @POST("delivery-boys-details-stage-validation{NUMERIC}")
    Call<AddDelBoyResponse> DeliveryBoysDetailsValid2(@Header("Authorization") String token, @Path("NUMERIC") String URL, @PartMap() Map<String, String> data);


    //-----------------------------------------------------------------------------

    // @FormUrlEncoded
    @POST("count-notifications")
    Call<NotificattionResponse> getNotification(@Header("Authorization") String token, @QueryMap Map<String, String> data);
    //-----------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("get-details")
    Call<EditUserResponse> getUserinfo(@FieldMap() Map<String, String> data);

    @FormUrlEncoded
    @POST("update-gst")
    Call<CommonResponse> gstUpdate(@Header("Authorization") String token,@FieldMap() Map<String, String> data);


    @FormUrlEncoded
    @POST("get-agent-details")
    Call<GetAgentDetailResponse> getAgentDetails(@Header("Authorization") String header, @Field("agent_id") String data);
    //-----------------------------------------------------------------------------


    @Headers({"Content-type: application/json", "Accept: */*"})
    @Multipart
    @POST("shop-act-upload")
    Call<CommonResponse> getInvoice(@PartMap() Map<String, String> data,
                                    @Part MultipartBody.Part store_image,
                                    @Part MultipartBody.Part store_image_act);
    //-----------------------------------------------------------------------------


    @FormUrlEncoded
    @POST("esign")
    Call<ResponseBody> Esign(@FieldMap Map<String, String> apiVersionMap);

    //-----------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("get-pan-details")
    Call<UpdatePanDetailResponse> pandetailResponse(@Header("Authorization") String header, @FieldMap Map<String, String> apiVersionMap);


    @FormUrlEncoded
    @POST("send-msg-for-pan-agreement-link")
    Call<com.canvascoders.opaper.Beans.CommonResponse> sendmsgForAgreementLink(@Header("Authorization") String header, @FieldMap Map<String, String> apiVersionMap);


    //@FormUrlEncodedz
    @POST("get-delivery-boys")
    Call<DeliveryboyListResponse> getDelivery_boys(@Header("Authorization") String token, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("gstesign")
    Call<ResponseBody> gstEsign(@FieldMap Map<String, String> apiVersionMap);


    @FormUrlEncoded
    @POST("get-delivery-boys")
    Call<DelBoyResponse> getDelBoys(@Header("Authorization") String token, @FieldMap Map<String, String> apiVersionMap);

    //-----------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("complete-deliery-boys-details")
    Call<DelBoysNextResponse> completeDelBoy(@Header("Authorization") String token, @FieldMap Map<String, String> apiVersionMap);

    // @FormUrlEncoded
    @POST("get-details")
    Call<ResponseBody> getDetails(@Header("Authorization") String token, @QueryMap Map<String, String> apiVersionMap);

    //-----------------------------------------------------------------------------

    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("get-details")
    Call<ResponseBody> getDetails2(@Body JsonObject data);

    //-----------------------------------------------------------------------------

    @FormUrlEncoded
    @POST("nocesign")
    Call<ResponseBody> nocEsign(@FieldMap Map<String, String> apiVersionMap);


    @FormUrlEncoded
    @POST("change-mobile")
    Call<ChangeMobileResponse> changeMobile(@Header("Authorization") String header, @FieldMap Map<String, String> apiVersionMap);

    //-----------------------------------------------------------------------------


    @POST("get-bank-details-from-ifsc")
    Call<ResponseBody> getBankDetailsFromIfsc(@Header("Authorization") String token, @QueryMap Map<String, String> apiVersionMap);

    //-----------------------------------------------------------------------------


    @Headers({"Content-type: application/json;charset=UTF-8", "Accept: */*", "accept-language: en-US,en;q=0.8"})
    @POST("agreement")
    Call<ResponseBody> getdocumentid(@Header("authorization") String authorization,
                                     @FieldMap Map<String, String> data);
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("api-esign-log")
    Call<ResponseBody> submitSigningLog(@Header("Authorization") String token, @Body JsonObject data);


    //@Headers("Content-Type: application/json")
    @POST("store-type")
    Call<ResponseBody> getStoreTypeListing(@Header("Authorization") String token, @Body JsonObject data);


    @POST("store-type-resign")
    Call<ResponseBody> getStoreTypeListing2(@Header("Authorization") String token, @Body JsonObject data);


    @POST("rate-update")
    Call<ResponseBody> setStoreTypeListing(@Header("Authorization") String token, @Body JsonObject data);

    //--------------------------------------------------------------------------------------
    // @FormUrlEncoded
    @POST("addendum-esign-save")
    Call<JsonObject> StoreAddendum(@Header("Authorization") String token, @QueryMap Map<String, String> data);
//--------------------------------------------------------------------------------------


    @Headers("Content-Type: application/json")
    @POST("vendor-type-for-addendum")
    Call<ResponseBody> getOldStoreTypeListing(@Header("Authorization") String token, @Body JsonObject data);

    @Headers("Content-Type: application/json")
    @POST("bank-details-log")
    Call<BankDetailResp> getBankDetails(@Header("Authorization") String token, @Body JsonObject data);

    //@FormUrlEncoded
    @POST("check-esign")
    Call<CheckEsignResponse> checkEsign(@Header("Authorization") String token, @QueryMap Map<String, String> data);

    //@FormUrlEncoded
    @POST("resend-otp-link")
    Call<ResendOTPResponse> resendOTP(@Header("Authorization") String token, @QueryMap Map<String, String> data);


    @POST("send-invoice-esign-link")
    Call<SendInvoiceLinkresponse> sendInvoice(@Header("Authorization") String token, @QueryMap Map<String, String> data);

    @Multipart
    @POST("invoice-support")
    Call<SubmitReportResponse> submitReportResponse(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                                    @Part MultipartBody.Part attachment);


    @Multipart
    @POST("general-support")
    Call<GeneralSupportResponse> generalSupportResponse(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                                        @Part MultipartBody.Part attachment);


/*
    @FormUrlEncoded
    @POST("support-listing")
    Call<SupportListResponse>getSupportList(@Header("Authorization")String token, @FieldMap Map<String, String> data);*/

    @FormUrlEncoded
    @POST
    Call<SupportListResponse> getSupportList(@Url String url, @Header("Authorization") String Header, @FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("support-detail")
    Call<SupportDetailResponse> getSupportDetails(@Header("Authorization") String token, @Field("support_id") String data);


    @FormUrlEncoded
    @POST("support-thread-detail")
    Call<CommentListResponse> getSupportThreadDetail(@Header("Authorization") String token, @Field("support_id") String data);


    @Multipart
    @POST("support-thread-comment")
    Call<CommentResponse> getCommentResponsewithImage(@Header("Authorization") String token, @PartMap() Map<String, String> data,
                                                      @Part MultipartBody.Part attachment);

    @FormUrlEncoded
    @POST("support-thread-comment")
    Call<CommentResponse> getCommentResponse(@Header("Authorization") String token, @FieldMap() Map<String, String> data);


    @FormUrlEncoded
    @POST("all-completed-vendors")
    Call<SearchListResponse> getSearchListResponse(@Header("Authorization") String token, @FieldMap() Map<String, String> data);


    @FormUrlEncoded
    @POST("resign-agreement-detail")
    Call<ResignAgreeDetailResponse> getDetailsResignAgreement(@Header("Authorization") String token, @FieldMap() Map<String, String> data);


}
