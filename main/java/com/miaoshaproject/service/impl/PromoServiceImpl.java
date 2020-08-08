package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataobject.PromoDO;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author hjg
 * @date 2020/8/8 15:06
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应的商品秒杀信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        //dataobject->model
        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null) {
            return null;
        }
        //判断当前时间是否秒杀活动即将开始或正在进行
        DateTime now  =new DateTime();
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if (promoModel.getEndData().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO){
        if (promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(promoDO.getPromoItemPrice());
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndData(  new DateTime(promoDO.getStartDate()));
        return promoModel;
    }
}
