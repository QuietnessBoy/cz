package com.zhjl.tech.attest.biz.common;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.User;
import com.zhjl.tech.attest.service.attest.IUserService;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.utils.UserTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class CommonBiz {

    @Resource
    private IUserService userService;


    /**
     * 获取该订单的用户的密钥对.
     * 根据attest对象获取对应用户的keypair，同时设置attest对象的钱包地址等
     * @param attest
     * @return
     */
    @Transactional
    public SM2KeyPair genUserKeyPair(Attest attest) {
        SM2KeyPair sm2KeyPair = null;
        //校验用户信息是否存在
        User oldUser = userService.getChannelUserIdByUser(attest.getChannelUserid());

        if (oldUser == null) {
            SM2 sm2 = new SM2Impl();
            sm2KeyPair = sm2.generateValidKeyPair();

            //创建新用户，并同时生成密钥对
            User newUser= new User();
            String pubKey = sm2.encodePublicKey(sm2KeyPair.getPublicKey());
            newUser.setPublicKey(pubKey);
            newUser.setEncryptedPrikey(sm2.encodePrivateKey(sm2KeyPair.getPrivateKey()));
            String walletAddr = UserTool.getAddress(pubKey);    //通过用户公钥生成钱包地址
            newUser.setWalletAddr(walletAddr);
            newUser.setChannelId(attest.getChannelId());
            newUser.setFreezed("0");    //0 未冻结，1 冻结
            newUser.setChannelUserid(attest.getChannelUserid());

            userService.insertSelective(newUser);
            log.info("新建用户信息.user:{}", JSONObject.toJSONString(newUser));

            //更新attest对象
            attest.setWalletAddr(walletAddr);
            attest.setPublicKey(pubKey);
            log.info("更新存证订单.存储公钥、钱包地址信息.channelOrder={}", attest.getChannelOrdersn());
        } else {
            sm2KeyPair = SM2KeyPair.genByHexString( oldUser.getPublicKey(),oldUser.getEncryptedPrikey());

            attest.setWalletAddr(oldUser.getWalletAddr());
            attest.setPublicKey(oldUser.getPublicKey());
            log.info("检测到用户.存储公钥、钱包地址信息.channelOrder={}", attest.getChannelOrdersn());
        }
        return sm2KeyPair;
    }
}
