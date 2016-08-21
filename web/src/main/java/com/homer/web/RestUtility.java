package com.homer.web;

import com.homer.web.model.ApiResponse;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by arigolub on 8/5/16.
 */
public final class RestUtility {

    private RestUtility() {
    }

    protected static <T> ApiResponse safelyDo(Supplier<T> supplier, Function<T, String> messageFunc)
    {
        try {
            T obj = supplier.get();
            return new ApiResponse(messageFunc.apply(obj), obj);
        } catch (Exception e) {
            return ApiResponse.failure(e);
        }
    }

    protected static <T> ApiResponse safelyDo(Supplier<T> supplier)
    {
        return safelyDo(supplier, (ignored) -> "success");
    }
}
