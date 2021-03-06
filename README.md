## 作业要求

### routing
重复完成课堂上的Demo:
* 实现获取热搜事件列表的接口
* 注意先写测试

### data-binding
作业描述

* 根据课堂上的demo，完成下面需求
* 提供获取某一条热搜事件的接口
* 提供能够根据起始参数，获取对应范围内的热搜事件列表的接口
* 提供添加热搜事件的接口（事件包含两个字段：事件名称和关键字）
* 提供修改某条热搜事件的接口（demo没有展示，请大家自己完成）
* 提供删除某条热搜事件的接口（demo没有展示，请大家自己完成）

* 需求4、5详细描述
    * 需求4： 修改某条事件时（通过参数传递的序号，修改列表中对应的事件数据），如果RequestBody只传了eventName没有传keyword那么仅仅只修改eventName 如果只传了keyword没有传eventName，那么只修改keyword字段 如果两个字段都传了，那么都进行修改

    * 需求5： 通过参数传递的序号，删除列表中对应的某条事件数据

* 注意：所有的需求都请先写测试再写实现


### Validation
* 重构添加热搜的接口，热搜事件现在增加一个字段:user，用来表示是哪个用户添加的该热搜事件。
  用户所包含的字段以及字段的限制和demo中保持一致，请求例子：
  ```
  {
      "eventName": "添加一条热搜",
      "keyword": "娱乐",
      "user": {
        "userName": "xiaowang",
        "age": 19,
        "gender": "female",
        "email": "a@thoughtworks.com",
        "phone": 18888888888
      }
  }
  ``` 
  
* 如果userName已存在在user列表中的话则只需添加热搜事件到热搜事件列表，如果userName不存在，则将User添加到用户列表中（相当于注册用户）
* 需要对请求进行校验：其中user keyword eventName都不能为空, user的校验规则：
    ```
  名称(不超过8位字符，不能为空)
  性别（不能为空）
  年龄（18到100岁之间，不能为空）
  邮箱（符合邮箱规范）
  手机号（1开头的11位数字，不能为空）
    ```
  
* 测试需要对每个验证条件进行覆盖
notice: 注意@Valid和@Validated的配合使用

### customize-response

* 将所有接口的返回值都替换成使用ResponseEntity
* 所有post请求都返回201,并且返回的头部带上index字段（值为创建的资源在列表中的位置：eg: 添加的热搜事件在列表中的index）
* 完成demo里的练习：get /rs/list和 get/rs/{index}接口返回的数据中不包含user字段
* 添加获取所有用户的接口: get /users，期望返回的数据格式例子: 
    ```
    [{
        "user_name": "xxxx",
        "user_age": 19,
        "user_gender": "female",
        "user_email": "xxx@xx",
        "user_phone": "1xxxxxxxxxx"
    }]
* 先写测试！！！
* hint: @JsonpProperty的使用



### error-handing
* 给所有接口添加错误处理：
    1. get /rs/list时对start和end进行校验，如果超出范围则返回 400 {error:"invalid request param"}
    2. get /rs/{index} 对index进行校验，如果超出范围则返回 400 {error:"invalid index"}
    3. post /rs/event 对RsEvent进行校验，如果不满足校验规则(User和RsEvent定义的校验规则)，则返回 400 {error:"invalid param"}
    4. post /user 对User进行校验，如果不满足校验规则，则返回 400 {error:"invalid user"}
    5. 先阅读：https://www.baeldung.com/spring-boot-logging
       在我们的exceptionHandler中添加日志，记录下错误的信息（error级别），运行程序试着观察是否有日志打印
* 先写测试（除了日志）！

### jpa-1
* 注册用户：参照demo，将注册用户持久化到数据库中(docker容器内启动的mysql数据库，并非内存数据库)
* 获取用户：新增获取用户接口，传递id返回对应id的用户数据
* 删除用户：新增删除用户接口，传递id从数据库中删除对应id用户数据

* 写测试！！！

### jpa-2
* 修改添加热搜事件接口：参照demo，将添加RsEvent持久化到数据库中
    ```
    {
        “eventName”: “热搜事件名”,
         “keyword”: “关键字”,
         “userId”: “user_id”
    }
  ```
  其中user需要是已注册用户，否则添加失败返回400
  

* 修改删除用户接口：参照demo，删除用户时，需要同时删除该用户所创建的热搜事件(使用JPA提供的mapping注解@ManyToOne @OneToMany)
* 添加更新接口
   ```
    request: patch /rs/{rsEventId}
    requestBody: {
                    “eventName”: “新的热搜事件名”,
                     “keyword”: “新的关键字”,
                     “userId”: “user_id”
                }
   ```
  接口要求：当userId和rsEventId所关联的User匹配时，更新rsEvent信息
          当userId和rsEventId所关联的User不匹配时，返回400
          userId为必传字段
          当只传了eventName没传keyword时只更新eventName
          当只传了keyword没传eventName时只更新keyword
          
* 添加投票接口
    ```
    request: post /rs/vote/{rsEventId}
    request body: {
                    voteNum: 5,
                    userId: 1,
                    voteTime: "current time"
                  }  
    接口要求：如果用户剩的票数大于等于voteNum，则能成功给rsEventId对应的热搜事件投票
            如果用户剩的票数小于voteNum,则投票失败，返回400
            考虑到以后需要查询投票记录的需求（根据userId查询他投过票的所有热搜事件，票数和投票时间，根据rsEventId查询所有给他投过票的用户，票数和投票时间），
            创建一个Vote表是一个明智的选择
            目前不用考虑给热搜事件列表排序的问题
  
    ```
* 修改其余所有接口：所有读取操作都改为从数据库中读取数据（包括重构测试）
  注意：需要修改热搜事件返回的数据结构，使其返回热搜事件id和获得的票数:
    ```
        {
            eventName: "event name",
            keyword: "keyword",
            id: "id",
            voteNum: 10
        }
    ```

* 写测试！！！

### Jpa-3
* 查询投票记录接口：
    参数传入起止时间，查询在该时间范围内的所有投票记录，写测试验证
* 这是最后一堂JPA课，把前面所有没做完的作业包括课上demo的范例都补上！


### spring-bean 作业


* 去掉RsService上的@Service注解
通过使用@Bean这种方式进行spring bean的定义和注入
reference: https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html

* 阅读：https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-spring-beans-and-dependency-injection.html
修改所有的service和controller 将依赖注入的方式改为通过构造函数注入（而非直接在字段上添加@Autowired）


### Spring-test 作业

* 完成RsController的buy接口（实现rsService的buy方法）
  使其满足：
  * 实现购买热搜，请求：
    ```
    trade: {
           "userId"
            "amount": 100, （金额）
            "rank": 1      （想购买的热搜排名）
            "rsEventId"
            }
    ```
  * 如果如果该排名上的热搜没有人购买，那么用户花任意价格即可买到该位热搜
    如果该排名上热搜已被购买，用户需要花高于当前价格的钱即可买到该位热搜，原热搜将会被替换掉（删除）
    如果出价低于当前排名热搜价格，则购买失败，返回400
  * 数据库会保存每次热搜购买记录，包含：金额，购买热搜排名，对应热搜事件
  
* 修改整体代码逻辑，保证获取到的热搜时按照热度排序，同时也保证购买的热搜按上面的要求排在对应位置(这里建议可以在RsEvent中添加对应
字段来表示热搜事件当前的排名)

* 逻辑正确后，完成以下测试的编写：
  * RsController中购买热搜接口的集成测试
  * RsService中buy()方法的单元测试
  * 由于更改了逻辑，需要保证所有测试通过
  * 注意！！！测试需要覆盖到各种情况（异常情况，分支逻辑，边界情况等等）

<span style="color: red"> 注意：最终需要将改动合并到master分支 </span> 




