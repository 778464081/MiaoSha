package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

/**
 * @author hjg
 * @date 2020/8/8 15:04
 */
public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);
}
