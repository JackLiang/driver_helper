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
* 1.2 手机注册

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
* 
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

 </br>
* 1.4 获取用户信息
* 
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
   </br>
 
* 1.5 修改用户信息
* 
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
#####2.消息
* 2.1,上报消息
* URL：/message//addReport.do
* PARAMS:user_id用户ID
*        title消息主题
*        type消息类型（1警察2拥堵3车祸4封路5施工）
*        desc消息描述
*        imgs多张图片地址，逗号隔开
*        location经纬度，逗号隔开（经度,纬度）
*        address事发区域，如广州市-海珠区-赤岗
* RETURN:{"code":0,"msg":"success","object":""}   

</br>

* 2.2获取消息列表
* URL:/message//getReport.do
* PARAMS:user_id用户ID
*        interest兴趣偏好二进制表示，用户关注的类型（1警察2拥堵3车祸4封路5施工）,多个偏好采用位并（例如关注警察和车祸：1<<1 | 1<<3）
*        location经纬度，逗号隔开（经度,纬度）
*        address事发区域，如广州市-海珠区-赤岗
* RETURN:{"code":0,"msg":"success","object":""} 
  `user_id` T '用户ID关联user',

  `title` '消息主题',
  
  `type`  '消息类型(二进制：1<<1警察1<<2拥堵1<<3车祸1<<4封路1<<5施工)',
  
  `desc`  '描述',
  
  `imgs`  '多张现场拍照图片(img1,img2...)',
  
  `location` '精确坐标（经度，纬度）',
  
  `address` '事发现场区域（广州市-海珠区-赤岗）',
  
  `create_time` 创建时间
  
  </br>
* 3.通用接口
* 3.1 上传图片到阿里云（注意需要模拟表单上传）
* URL:/common/uploadPicUrl.do
* PARAMS：user_id 用户ID
*         type 1水印上传2无水印上传
*         file 上传的文件流

   RETURN:{"code":0,"msg":"success","object":""} 
   返回上传到阿里云后的图片url
