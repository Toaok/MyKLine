package com.example.administrator.mykline;

import java.util.Locale;;

/**
 * Created by Administrator on 2018/1/16.
 * okHttp请求封装类
 */

public class OkHttpBase {
    public static boolean buysellS = false;
    public static boolean isConnected = false;
    public static boolean loginstatic=false;
    public static String token = "";
    public static String lang_zh_cn = "zh_cn";
    public static String lang_en_us = "en_us";
    public static int PERMISSIONS_REQUEST_ACCESS_INTERNET = 1;
    public static Locale localedeut;
    //google验证
    public static boolean IsGoogleAuthVerify;
    //短信验证
    public static boolean IsMobileVerify;

    //URL路径API
    //接口的访问的服务地址
    public static String Url = "http://192.168.1.242/";
    public static String Urlceshi = "http://exchange.ico17.com/";

    //https://www.idax.mn/ http://192.168.1.51:8086/ http://192.168.1.83/ testwww.idax.mn/
    public static String UrlZs = "https://www.idax.mn/";

    public static String CurrentPassword="";

    public static int buy=1;
    public static int sell=2;

    public static String PUBLIC_CODE="ETH_BTC";
    public static String XS_PUBLIC_CODE="ETH/BTC";

    public static String CNYBTC="0";
    public static String USDBTC="0";
    public static String CNYETH="0";
    public static String USDETH="0";

    //绑定手机
    public static String bindMobile ="0";
    //修改密码
    public static String changePwd="1";
    //提虚拟币
    public static String withDrawCoin="2";
    //提法币
    public static String withDrawCash="3";
    //绑定银行卡
    public static String bindBankCard="4";
    //登录
    public static String login="5";
    //解绑手机
    public static String unBindMobile="6";

    //根据买卖判断跳那个页面
    public static String pull="";

    public static boolean isbook =false;

    /**
     * HOME
     */

    //获取首页幻灯片
    public static String GetSliders = "api/Home/GetSliders";
    //获取首页活动
    public static String GetActivitys = "api/Home/GetActivitys";
    //获取首页交易对
    public static String GetIndexPairs = "api/Home/GetIndexPairs";
    //获取国家列表
    public static String GetCountrys = "api/Home/GetCountrys";
    public static String GetSmsAreaCodes="api/Home/GetSmsAreaCodes";

    /**
     * User
     */
    //登录
    public static String Login = "api/Login";
    //登录时MFA验证
    public static String MfaLogin = "api/MfaLogin";

    //注册(发送验证邮件)
    public static String SendEmailForRegister = "api/SendEmailForRegister";
    //注册
    public static String Register = "api/Register";
    //获取用户登录日志
    public static String GetLogonLogsByPage = "api/User/GetLogonLogsByPage";
    //获取用户操作日志
    public static String GetOperationLogsByPage = "api/User/GetOperationLogsByPage";
    //修改用户密码
    public static String UpdateUserPwd = "api/User/UpdateUserPwd";
    //修改用户信息
    public static String UpdateUserInfo = "api/User/UpdateUserInfo";
    //查询用户信息
    public static String GetUserInfo = "api/User/GetUserInfo";
    //获取谷歌验证信息
    public static String GetGoogleSetupCode = "api/User/GetGoogleSetupCode";
    //是否开启谷歌验证
    public static String IsGoogleAuthEnabled = "api/User/IsGoogleAuthEnabled";
    //启用谷歌验证
    public static String EnableGoogleAuth = "api/User/EnableGoogleAuth?code={code}";
    //禁用谷歌验证
    public static String DisableGoogleAuth = "api/User/DisableGoogleAuth?code={code}";
    //找回密码(发送邮件 pc端)
    //public static String FindPwdBySendEmail = "api/User/FindPwdBySendEmail";
    //找回密码(发送邮件 移动端)
    public static String FindPwdBySendMobileEmail="api/User/FindPwdBySendMobileEmail";
    //找回密码(验证邮件是否合法)
    public static String CheckEmailForFindPwd = "api/User/CheckMobileEmailForFindPwd";
    //找回密码(修改密码)
    public static String ChangePwdForFindPwd = "api/User/ChangeMobilePwdForFindPwd";
    //申请身份认证
    public static String ApplyCertificate = "api/User/ApplyCertificate";
    //证件上传
    public static String CertificateUpload = "api/User/CertificateUpload";
    //创建ApiKey
    public static String CreateApiKey = "api/User/CreateApiKey?comment={comment}&code={code}&mfaType={mfaType}";
    //发送短信
    public static String SendSms = "api/User/SendSms";
    //绑定手机
    public static String BindMobile = "api/User/BindMobile";
    //解绑手机
    public static String UnBindMobile="api/User/UnBindMobile";
    //获取用户认证信息
    public static String GetUserBind = "api/User/GetUserBind";

    /**
     * Article
     */

    //查询首页新闻
    public static String GetIndexNews = "api/Article/GetIndexNews";
    //查询首页底部分类文章
    public static String GetIndexHelp = "api/Article/GetIndexHelp";
    //查询分类页面的文章
    public static String GetCategoryArticle = "api/Article/GetCategoryArticle";
    //查询文章详情
    public static String GetArticleInfo = "api/Article/GetArticleInfo";
    //查询文章列表
    public static String GetArticlesByPage = "api/Article/GetArticlesByPage";


    /**
     * Trade
     */
    //交易大厅--检查交易对Get请求
    public static String CheckPair = "api/Trade/CheckPair?pairName={pairName}";
    //交易大厅--个人资金
    public static String UserCapital = "api/Trade/UserCapital";
    //交易大厅--创建订单
    public static String CreateOrder = "api/Trade/CreateOrder";
    //交易大厅--查询自选
    public static String GetPortfolio = "api/Trade/GetPortfolio";
    //交易大厅--删除自选
    public static String DeletePortfolio = "api/Trade/DeletePortfolio";
    //交易大厅--添加自选
    public static String AddPortfolio = "api/Trade/AddPortfolio";
    //交易大厅--我的委托
    public static String GetMyOrderss = "api/Trade/GetMyOrders";
    //交易大厅--我的最新交易记录
    public static String GetMyTradess = "api/Trade/GetMyTrades";

    /**
     * Account
     */
    //查询资金列表
    public static String GetAccountByUserId = "api/Account/GetAccountAndEstimateValuesByUserId";
    //资金情况
    public static String GetAccountDeposit = "api/Account/GetAccountDeposit";
    //充值记录
    public static String GetDepositRecord = "api/Account/GetDepositRecordByPage";
    //生成钱包地址
    public static String GetDepositAddress = "api/Account/GetDepositAddress";
    //虚拟币提现
    public static String CoinWithdraw = "api/Account/CoinWithdraw";
    //资金情况
    public static String GetAccountWithdraw = "api/Account/GetAccountWithdraw";
    //虚拟币提现记录
    public static String GetWithdrawRecord = "api/Account/GetWithdrawRecordByPage";
    //成交订单列表
    public static String GetTrades = "api/Account/GetTrades";
    //所有市场
    public static String GetAccountTradeMarket = "api/Account/GetAccountTradeMarket";
    //当前委托
    public static String GetCurrentOrders = "api/Account/GetCurrentOrders";
    //历史委托
    public static String GetHistoryOrders = "api/Account/GetHistoryOrders";
    //取消委托订单
    public static String CancelOrder = "api/Account/CancelOrder";
    //查询个人邀请码
    public static String GetAccountInvite = "api/Account/GetAccountInvite";

    /**
            * LegalTender
     */
    //法币充值--网银充值
    public static String OnLineRecharge = "api/LegalTender/OnLineRecharge";
    //法币充值--网银异步通知 GET请求方式
    public static String RechangeNotityGet = "api/LegalTender/RechangeNotity";
    //法币充值--网银异步通知 POST请求方式
    public static String RechangeNotityPost = "api/LegalTender/RechangeNotity";
    //法币充值--充值订单结果查询
    public static String RechangeOrderResult = "api/LegalTender/RechangeOrderResult?content={content}";
    //法币充值--充值记录
    public static String GetRechargeRecord = "api/LegalTender/GetRechargeRecord";
    //法币提现--申请提现
    public static String ApplyWithDraw = "api/LegalTender/ApplyWithDraw";
    //法币提现--提现记录
    public static String GetWithDrawRecord = "api/LegalTender/GetWithDrawRecord";
    //法币提现--撤销提现
    public static String CancelWithDraw = "api/LegalTender/CancelWithDraw";
    //法币提现--异步通知GET
    public static String WithDrawNotityGet = "api/LegalTender/WithDrawNotity";
    //法币提现--异步通知POST
    public static String WithDrawNotityPost = "api/LegalTender/WithDrawNotity";
    //银行卡--银行列表
    public static String GetOnlineCashBanks = "api/LegalTender/GetOnlineCashBanks";
    //银行卡--查询用户银行卡
    public static String GetUserBank = "api/LegalTender/GetUserBank";
    //银行卡--绑定新卡
    public static String AddUserBank = "api/LegalTender/AddUserBank";
    //银行卡--删除银行卡
    public static String DelUserBank = "api/LegalTender/DelUserBank";
    //银行卡--设置默认选择
    public static String SetUserBankDefault = "api/LegalTender/SetUserBankDefault";
    //兑换中心--允许兑换列表
    public static String GetAllSwapSetting = "api/LegalTender/GetAllSwapSetting";
    //兑换中心--兑换
    public static String AddSwap = "api/LegalTender/AddSwap";
    //兑换中心--资金明细
    public static String GetSwapCapital = "api/LegalTender/GetSwapCapital";
    //兑换中心--兑换记录
    public static String GetSwapsbyPage = "api/LegalTender/GetSwapsbyPage";


    /**
     * Cooperation
     */
    //自主上币-详情
    public static String ApplyCurrencylist = "api/Cooperation/ApplyCurrencylist";
    //自主上币-上币申请
    public static String CurrencylistAplication = "api/Cooperation/CurrencylistAplication";
    //自主上币-图片上传
    public static String UploadImg = "api/Cooperation/UploadImg";


    /**
     * 增加的接口充值列表和提现列表
     */

    public static String GetDepositRecordByPage = "api/Account/GetDepositRecordByPage";

    public static String GetWithdrawRecordByPage = "api/Account/GetWithdrawRecordByPage";


    /**
     * 市场交易
     */
    public static String GetAllOpenMarkets = "api/Signalr/GetAllOpenMarkets";
    /**
     * 查询单个交易对
     *
     */
    public static String GetSinglePairOpenMarkets="api/Signalr/GetSinglePairOpenMarkets";
    /**
     * K线
     */
    public static String GetOhlcvByPeriod = "api/Signalr/GetOhlcvByPeriod";
    /**
     * 最新成交量
     */
    public static String GetTradeByPair = "api/Signalr/GetTradeByPair";
    /**
     * 交易订单
     */
    public static String GetAllOrderBooks = "api/Signalr/GetAllOrderBooks";
    /**
     * 当前委托
     */
    public static String GetMyOrders = "api/Signalr/GetMyOrders";
    /**
     * 我的历史委托
     */
    public static String GetMyTrades = "api/Signalr/GetMyTrades";


    private static  boolean isBooleans = false;

    /**
     * 估值
     */

    public static String GetValuationCnys="api/Trade/GetValuationCny";

    public static String GetAppUpdateVersion="api/Home/GetAppUpdateVersion";


    /**
     * 用户反馈
     */
    public static  String CreateUserFeedback="api/User/CreateUserFeedback";

    /**
     * 极验
     *
     */
    public static String GetCaptcha="api/GeeTest/GetCaptcha";
}
