package com.ldonline.common.entites;

import com.ldonline.common.utils.JsonUtils;

public class JsonEntity {
    @Override
    public String toString() {
        return JsonUtils.toString(this);
    }
}
