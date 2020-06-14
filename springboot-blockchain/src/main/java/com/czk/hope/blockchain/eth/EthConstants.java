package com.czk.hope.blockchain.eth;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigInteger;

/**
 * @Author: Created by ChenZK
 * @Create: 2020/1/9 9:53
 * ETH配置类
 */
public interface EthConstants {

    /*----- 自己维护一个nonce -----*/
    BigInteger nonce = new BigInteger("0");

    String key = "";

    /*---- 建立web3j ----*/
    Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/" + key));

    /*---- 转账的手续费 ----*/
    BigInteger gasPrice = Convert.toWei("10", Convert.Unit.GWEI).toBigInteger();
    public static BigInteger gasLimit = Convert.toWei("45000", Convert.Unit.WEI).toBigInteger();

    /*---- 合约地址 ----*/
    String contractAccount = "";

    /*---- 汇手续费的地址 ----*/
    String serviceCharge = "";

    /*---- 汇手续费的私钥 ----*/
    String serviceChargePrivateKey = "";

    /*---- 代币的位数 ----*/
    Integer decimals = 6;

    /*---- 代币归集的地址 ----*/
    String ercToAddress = "";

    /*---- 获取私钥的URL ----*/
    String getPrivateKeyUrl = "";

    /*---- 存放json私钥文件地址 ----*/
    String privateKeyJsonFilePath = "D:\\wallet\\";
}
