# driver_helper
helper for driver

aliyun OSS
----
####AccessKeyId:HaRAUYrJZIgWMUyF
####AccessKeySecret:Zsa8YdNOHIhoUeXrNPMlFhZtj7YlPj

DH接口概要
----
* 1.客户端所有传入参数名为小写，如果参数多个单词之间使用下划线`_`隔开
* 2.服务端返回客户端以json形式返回，具体形式：`{"code":0,"msg":"success","object":""}`,code表示返回码0成功，非0失败或者异常;msg表示返回的     描述;object表示返回的数据
* 3.项目根目录：`http://112.74.96.226:8080/dh-web`
</br>
  
接口
-----
#####1.用户
 * 1.1 手机登录：

  URL:/user/authenticate.do

  PRARMS:
        account：帐号（一般为手机号）

        password:密码（暂时不需要加密）
        
 RETURN：{"code":0,"msg":"success","object":""}
 
 </br>
*1.2 手机注册
  URL:/user/regist.do
  
    PRARMS:
        account：帐号（一般为手机号）

        password:密码（暂时不需要加密）
        
 RETURN：{"code":0,"msg":"success","object":""}
 
  `user_id`  '用户ID（8位数字）',
  
  `account`  '用户昵称',
  
  `password`  '密码',
  
  `open_id` '第三方登录唯一标识',
  
  `nickname` '昵称',
  
  `head_img`  '头像',
  
  `phone`  '手机号码',
  
  `sex`  '性别1男2女',
  
  `balance`  '余额（积分）',
  
  `platform`  '平台''第三方登录 0手机注册1微信 2QQ ''',
  
  `birth_day`  '生日',
  
  `license_time`  '领证日期',
       
</br>

* 1.3 第三方注册/登录
  URL:/user/registOther.do
  PRARMS:
         open_id:'第三方登录唯一标识'

         platform:'平台''第三方登录 0手机注册1微信 2QQ '''

  RETURN:{"code":0,"msg":"success","object":""}
 `user_id`  '用户ID（8位数字）',
  
  `account`  '用户昵称',
  
  `password`  '密码',
  
  `open_id` '第三方登录唯一标识',
  
  `nickname` '昵称',
  
  `head_img`  '头像',
  
  `phone`  '手机号码',
  
  `sex`  '性别1男2女',
  
  `balance`  '余额（积分）',
  
  `platform`  '平台''第三方登录 0手机注册1微信 2QQ ''',
  
  `birth_day`  '生日',
  
  `license_time`  '领证日期',

* 1.4 获取用户信息
 URL：/user/getUserInfo.do
 PARAMS:
       user_id:'用户ID（8位数字）'
 RETURN:{"code":0,"msg":"success","object":""}
  `user_id`  '用户ID（8位数字）',
  
  `account`  '用户昵称',
  
  `password`  '密码',
  
  `open_id` '第三方登录唯一标识',

 `nickname` '昵称',
  
  `head_img`  '头像',
  
  `phone`  '手机号码',
  
  `sex`  '性别1男2女',
  
  `balance`  '余额（积分）',
  
  `platform`  '平台''第三方登录 0手机注册1微信 2QQ ''',
  
  `birth_day`  '生日',
  
  `license_time`  '领证日期',
 
* 1.5 修改用户信息
 URL：/user/updateUser.do
 PARAMS:
  `user_id`  '用户ID（8位数字）',

  `nickname` '昵称',

  `head_img`  '头像',
  
  `phone`  '手机号码',
  
  `sex`  '性别1男2女',
  
  `birth_day`  '生日',
  
  `license_time`  '领证日期',
 RETURN:{"code":0,"msg":"success","object":""}
