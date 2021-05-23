package com.ducnguyen46.soc.service;

import static com.ducnguyen46.soc.constant.Constant.*;

public class ApiRestUtils {

    public static UserRestService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserRestService.class);
    }

    public static ProductRestService getProductService(){
        return RetrofitClient.getClient(BASE_URL).create(ProductRestService.class);
    }

    public static ProductCartRestService getProductCartService(){
        return RetrofitClient.getClient(BASE_URL).create(ProductCartRestService.class);
    }

    public static BillRestService getBillService(){
        return RetrofitClient.getClient(BASE_URL).create(BillRestService.class);
    }
}
