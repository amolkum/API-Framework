package utils;


import io.restassured.response.Response;

import java.io.IOException;

public class PetStoreApi {


    public static Response put(Object requestPetStore){
        return RestResource.put(Routes.BASE_URI+Routes.UpdateUser,
                  requestPetStore);
    }

     public static Response getInventory() throws IOException {
        return RestResource.get(Routes.BASE_URI +Routes.GetStoreInventory);
    }

    public static Response post(Object requestPetStore) throws IOException {
        return RestResource.postMethod(Routes.BASE_URI +Routes.GetStoreOrder,
                requestPetStore);
    }

    public static Response getStoreOrder(Object id) throws IOException {
        return RestResource.getMethod1(Routes.BASE_URI+Routes.GetStoreOrder1,id
                );
    }

    public static Response deleteStoreOrder(Object id){
        return RestResource.deleteMethod(Routes.BASE_URI +Routes.Delete_Order,id
        );
    }


}
