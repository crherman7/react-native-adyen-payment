#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(AdyenPayment, NSObject)
RCT_EXTERN_METHOD(startPayment:(NSString *)component componentData:(NSDictionary *)componentData paymentDetails:(NSDictionary *)paymentDetails)
RCT_EXTERN_METHOD(initialize:(NSDictionary *)appServiceConfigData)
RCT_EXTERN_METHOD(startPaymentPromise:(NSString *)component componentData:(NSDictionary *)componentData paymentDetails:(NSDictionary *)paymentDetails resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(paymentMethodsResponseHandlerPromise:(NSDictionary *)paymentMethodsResponse resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(handleRedirectPromise:(NSDictionary *)actionRequest resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(handleFingerprintPromise:(NSDictionary *)actionRequest resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(handleChallengePromise:(NSDictionary *)actionRequest resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(encryptCvv:(NSString *)cvv publicKey:(NSString *)publicKey resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(setStyle:(NSDictionary *)style)
RCT_EXTERN_METHOD(encryptCardData:(NSDictionary *)formData publicKey:(NSString *)publicKey resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
@end
