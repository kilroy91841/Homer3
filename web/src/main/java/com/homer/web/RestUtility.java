package com.homer.web;

import com.homer.web.model.ApiResponse;

import java.util.function.Supplier;

/**
 * Created by arigolub on 8/5/16.
 */
public final class RestUtility {

    private RestUtility() {
    }

    protected static ApiResponse safelyDo(Supplier<Object> supplier)
    {
        try {
            return new ApiResponse("success", supplier.get());
        } catch (Exception e) {
            return ApiResponse.failure(e);
        }
    }
}
