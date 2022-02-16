package com.example.shopapp.utility;

import lombok.Data;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@Data
public class PodamUtility {

    private static PodamFactory FACTORY = new PodamFactoryImpl();

    private PodamUtility() {

    }

    public static <T> T makePojo(Class<T> tClass) {
        return FACTORY.manufacturePojo(tClass);
    }
}
