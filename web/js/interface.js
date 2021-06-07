/**
 * 后台访问接口
 *
 */
var url_base = "/verify";
var webInterface = {
    GET:"get",
    POST:"post",
    SYNC:false,
    ASYNC:true,
    count:{//获取员工和客户总数的接口，无需登录验证
       url:"/count",
        /**
         * 获取所有员工的总数
         * @param param
         */
       getEmployees:function (param) {
           param.url=this.url;
           param.data = {op: "getEmployees", param:JSON.stringify(param.param)};
           webInterface.access(param);
       },
        /**
         * 获取所有客户的总数
         * @param param
         */
       getClients:function (param) {
           param.url=this.url;
           param.data = {op: "getClients", param:JSON.stringify(param.param)};
           webInterface.access(param);
       }
    },
    account:{
        url:url_base+"/account",
        /**
         * 登录
         * @param username 账号
         * @param password 密码
         * @param success
         * @param fail
         */
        login:function(param) {
            param.url = this.url;
            param.data = {op: "login", username:param.username,password:param.password};
            webInterface.access(param);
        },
        /**
         * 注销
         * @param success
         * @param fail
         */
        quit:function(param) {
            param.url = this.url;
            param.data = {op: "quit"};
            webInterface.access(param);
        },
        /**
         * 获取账户列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param)};
            webInterface.access(param);
        },
        /**
         * 添加账号
         * @param account 账号信息
         * @param success
         * @param fail
         */
        insert:function(param) {
            param.url = this.url;
            param.data = {op: "insert",account:JSON.stringify(param.account)};
            webInterface.access(param);
        },
        insertAdmin:function(param) {
            param.url = this.url;
            param.data = {op: "insertAdmin",account:JSON.stringify(param.account)};
            webInterface.access(param);
        },
        /**
         * 删除账号
         * @param id 账号id
         * @param success
         * @param fail
         */
        delete:function(param) {
            param.url = this.url;
            param.data = {op: "delete",id:param.id};
            webInterface.access(param);
        },
        /**
         * 获取账号详情
         * @param id 账号id
         * @param success
         * @param fail
         */
        get:function(param) {
            param.url = this.url;
            param.data = {op:"get"};
            webInterface.access(param);
        },
        /**
         * 获取账号详情
         * @param id 账号id
         * @param success
         * @param fail
         */
        getAdmin:function(param) {
            param.url = this.url;
            param.data = {op:"getAdmin",account:JSON.stringify(param.account)};
            webInterface.access(param);
        },
        /**
         * 修改账号
         * @param account 账号信息
         * @param success
         * @param fail
         */
        update:function(param) {
            param.url = this.url;
            param.data = {op:"update",account:JSON.stringify(param.account)};
            webInterface.access(param);
        },
        /**
         * 设置权限
         * @param id 账号id
         * @param permission 权限
         * @param success
         * @param fail
         */
        permit:function(param) {
            param.url = this.url;
            param.data = {op:"permit",id:param.id,permission:param.permit};
            webInterface.access(param);
        }
    },
    
    client:{
        url:url_base+"/client",
        /**
         * 获取客户列表
         * @param param 查询参数
         * @param category 0-派遣单位 1-合作单位 2-供应商
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param),category: param.category};
            webInterface.access(param);
        },
        /**
         * 获取待分配管理员的客户列表
         * @param category 0-派遣方 1-合作单位
         * @param success
         * @param fail
         */
        getAllocating:function(param) {
            param.url = this.url;
            param.data = {op: "getAllocating", category: param.category};
            webInterface.access(param);
        },
        /**
         * 获取管理员管理的客户列表
         * @param category 0-派遣方 1-合作单位
         * @param success
         * @param fail
         */
        getAllocated:function(param) {
            param.url = this.url;
            param.data = {op: "getAllocated", aid:param.aid, category: param.category};
            webInterface.access(param);
        },
        /**
         * 添加客户
         * @param client 客户信息
         * @param category 0-派遣方 1-合作单位 2-供应商
         * @param success
         * @param fail
         */
        insert:function(param) {
            param.url = this.url;
            param.data = {op: "insert",client:JSON.stringify(param.client),category: param.category};
            webInterface.access(param);
        },
        /**
         * 获取客户信息
         * @param id 客户id
         * @param category 0-派遣方 1-合作单位 2-供应商
         * @param success
         * @param fail
         */
        get:function(param) {
            param.url = this.url;
            param.data = {op:"get",id:param.id, category: param.category};
            webInterface.access(param);
        },
        /**
         * 修改客户信息
         * @param client 客户信息
         * @param category 0-派遣方 1-合作单位 2-供应商
         * @param success
         * @param fail
         */
        update:function(param) {
            param.url = this.url;
            param.data = {op:"update",client:JSON.stringify(param.client), category: param.category};
            webInterface.access(param);
        },
        
        /**
         *
         * @param id 要修改状态的客户id
         * @param category 客户类别 0—派遣方 1—合作客户 2_供应商
         * @param type  要修改的状态 0—合作 1—潜在 2—流失； 供应商 0 合作 1 流失
         * @param success
         * @param fail
         */
        updateType:function(param) {
            param.url = this.url;
            param.data = {op:"updateType",id:param.id, category: param.category,type:param.type};
            webInterface.access(param);
        },
        /**
         * 获取客户最新自定义工资
         * @param id 客户id
         * @param success
         * @param fail
         */
        getLastSalaryDefine:function(param) {
            param.url = this.url;
            param.data = {op:"getLastSalaryDefine",id:param.id};
            webInterface.access(param);
        },
        /**
         * 根据月份获取自定义工资
         * @param id 客户id
         * @param month 月份
         * @param success
         * @param fail
         */
        getSalaryDefine:function(param) {
            param.url = this.url;
            param.data = {op:"getSalaryDefine",id:param.id,month:param.month};
            webInterface.access(param);
        },
        /**
         * 根据月份获取自定义工资
         * @param id
         * @param month
         * @param success
         * @param fail
         */
        selectSalaryDefineByMoth:function(param) {
            param.url = this.url;
            param.data = {op:"getSalaryDefineByMonth",id:param.id,month:param.month};
            webInterface.access(param);
        },
        
        /**
         * 添加自定义工资
         * @param mapSalary 自定义工资
         * @param success
         * @param fail
         */
        insertSalaryDefine:function(param) {
            param.url = this.url;
            param.data = {op: "insertSalaryDefine",mapSalary:JSON.stringify(param.mapSalary)};
            webInterface.access(param);
        },
        /**
         * 获取财务信息
         * @param id 客户id
         * @param category 0-派遣方 1-合作单位
         * @param success
         * @param fail
         */
        getFinance:function(param) {
            param.url = this.url;
            param.data = {op:"getFinance",id:param.id,category:param.category};
            webInterface.access(param);
        },
        /**
         * 添加财务信息
         * @param finance 财务信息
         * @param success
         * @param fail
         */
        insertFinance:function(param) {
            param.url = this.url;
            param.data = {op: "insertFinance",finance:JSON.stringify(param.finance)};
            webInterface.access(param);
        },
        /**
         * 修改财务信息
         * @param finance 财务信息
         * @param success
         * @param fail
         */
        updateFinance:function(param) {
            param.url = this.url;
            param.data = {op:"updateFinance",finance:JSON.stringify(param.finance)};
            webInterface.access(param);
        },
        /**
         * 删除客户
         * @param id 客户id
         * @param category 0-派遣方 1-合作单位 2-供应商
         * @param type 0-合作单位  1-潜在合作单位(当category=0/1时才有效)
         * @param success
         * @param fail
         */
        delete:function(param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id, category: param.category,type: param.type};
            webInterface.access(param);
        },
        /**
         * 分配管理员
         * @param aid 管理员id
         * @param category 0-派遣方 1-合作单位
         * @param cids 给哪些客户分配管理员
         * @param success
         * @param fail
         */
        allocate:function(param) {
            param.url = this.url;
            param.data = {op:"allocate",aid:param.aid,category: param.category,cids:param.cids};
            webInterface.access(param);
        },
        /**
         * 获取合作客户和余额
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getFinances:function(param) {
            param.url = this.url;
            param.data = {op:"getFinances",param:JSON.stringify(param.param)};
            webInterface.access(param);
        }
    },
    
    contract:{
        url:url_base+"/contract",
        /**
         * 获取合同列表
         * @param param 查询参数
         * @param type 类型 A-平台和派遣单位合同 B-派遣单位和合作单位 C-派遣单位和员工
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param),type:param.type};
            webInterface.access(param);
        },
        getExpireContract:function(param) {
            param.url = this.url;
            param.data = {op: "getExpireContract", param:JSON.stringify(param.param),type:param.type};
            webInterface.access(param);
        },

        /**
         * 获取最新合同
         * @param id 该客户id
         * @param type 类型 A-平台和派遣单位合同 B-派遣单位和合作单位 C-派遣单位和员工
         * @param success
         * @param fail
         */
        getLast:function(param) {
            param.url = this.url;
            param.data = {op: "getLast", id:param.id,type:param.type};
            webInterface.access(param);
        },
        /**
         * 修改合同
         * @param contract 合同信息
         * @param success
         * @param fail
         */
        update:function(param) {
            param.url = this.url;
            param.data = {op:"update",contract:JSON.stringify(param.contract)};
            webInterface.access(param);
        },
        /**
         * 添加合同
         * @param contract 合同信息
         * @param success
         * @param fail
         */
        insert:function(param) {
            param.url = this.url;
            param.data = {op: "insert",contract:JSON.stringify(param.contract)};
            webInterface.access(param);
        },
        /**
         * 获取合同详情
         * @param id 合同id
         * @param success
         * @param fail
         */
        get:function(param) {
            param.url = this.url;
            param.data = {op:"get",id:param.id};
            webInterface.access(param);
        },
        /**
         * 删除合同
         * @param id 合同id
         * @param success
         * @param fail
         */
        delete:function(param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id};
            webInterface.access(param);
        },
        /**
         * 添加服务项目
         * @param serve 服务项目信息
         * @param success
         * @param fail
         */
        insertService:function(param) {
            param.url = this.url;
            param.data = {op: "insertService",serve:JSON.stringify(param.contract)};
            webInterface.access(param);
        },
        /**
         * 获取合同服务项目详情
         * @param id 合同id
         * @param success
         * @param fail
         */
        getService:function(param) {
            param.url = this.url;
            param.data = {op: "getService",id:param.id};
            webInterface.access(param);
        },
        /**
         * 修改服务项目
         * @param serve 服务项目信息
         * @param success
         * @param fail
         */
        updateService:function(param) {
            param.url = this.url;
            param.data = {op: "updateService",serve:JSON.stringify(param.contract)};
            webInterface.access(param);
        },
        /**
         * 获取当前合作单位的有效服务项目列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getServiceList:function(param) {
            param.url = this.url;
            param.data = {op: "getServiceList",param:JSON.stringify(param.param)};
            webInterface.access(param);
        },
        
        /**
         * 批量插入员工合同
         * @param contracts 员工合同集合
         * @param success
         * @param fail
         */
        insertContracts:function(param) {
            param.url = this.url;
            param.data = {op: "insertContracts",contracts:JSON.stringify(param.contracts)};
            webInterface.access(param);
        }
    },

    employee:{
        url:url_base+"/employee",
        /**
         * 获取员工列表
         * @param param 查询参数
         * @param type 0 内部员工  1 外派员工 2人才库
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param),type:param.type};
            webInterface.access(param);
        },
        /**
         * 插入员工信息
         * @param employee 员工信息
         * @param extract  员工额外信息
         * @param success  成功
         * @param fail
         */
        insert:function (param) {
            param.url = this.url;
            param.data = {op: "insert",employee:JSON.stringify(param.employee)};
            webInterface.access(param);
        },
        
        /**
         * 插入员工补充信息
         * @param extract  员工补充信息
         * @param success  成功
         * @param fail
         */
        insertExtra:function (param) {
            param.url = this.url;
            param.data = {op: "insertExtra",extra:JSON.stringify(param.extra)};
            webInterface.access(param);
        },
        /**
         * 批量设置员工社保信息
         * @param eids
         * @param setting
         * @param success
         * @param fail
         */
        settingBatch:function (param) {
            param.url = this.url;
            param.data = {op: "settingBatch",eids:param.eids,setting:JSON.stringify(param.setting)};
            webInterface.access(param);
        },
        
        insertBatch:function (param) {
            param.url = this.url;
            param.data = {op: "insertBatch",employees:JSON.stringify(param.employees),extras:JSON.stringify(param.extras),cards:JSON.stringify(param.cards)};
            webInterface.access(param);
        },
        /**
         * 获取员工详情
         * @param id 员工id
         * @param success
         * @param fail
         */
        get:function (param) {
            param.url = this.url;
            param.data = {op: "get",id:param.id};
            webInterface.access(param);
        },/**
         * 获取员工补充信息
         * @param id 员工id
         * @param success
         * @param fail
         */
        getExtra:function (param) {
            param.url = this.url;
            param.data = {op: "getExtra",id:param.id};
            webInterface.access(param);
        },
        /**
         * 修改员工
         * @param employee 员工信息
         * @param category 0-员工信息 1-员工补充信息
         * @param success
         * @param fail
         */
        update:function (param) {
            param.url = this.url;
            param.data = {op: "update",employee:JSON.stringify(param.employee)};
            webInterface.access(param);
        },
        
        /**
         * 修改员工补充信息
         * @param extra 员工补充信息
         * @param success
         * @param fail
         */
        updateExtra:function (param) {
            param.url = this.url;
            param.data = {op: "updateExtra",extra:JSON.stringify(param.extra)};
            webInterface.access(param);
        },
        
        /**
         * 离职退休
         * @param reason 离职原因信息
         * @param category 0-离职 1-退休
         * @param date 离职或退休时间
         * @param id 员工id
         * @param success
         * @param fail
         */
        leave:function (param) {
            param.url = this.url;
            param.data = {op: "leave",id:param.id,reason:param.leave,date:param.date, category:param.category};
            webInterface.access(param);
        },
        /**
         * 删除员工
         * @param id 员工id
         * @param category 0-移入人才库 1-直接删除
         * @param success
         * @param fail
         */
        delete:function (param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id, category:param.category};
            webInterface.access(param);
        },
        /**
         * 添加社保设置
         * @param setting 社保设置信息
         * @param success
         * @param fail
         */
        insertEnsureSetting:function (param) {
            param.url = this.url;
            param.data = {op: "insertEnsureSetting",setting:JSON.stringify(param.setting)};
            webInterface.access(param);
        },
        /**
         * 修改社保设置
         * @param setting 社保设置信息
         * @param success
         * @param fail
         */
        updateEnsureSetting:function (param) {
            param.url = this.url;
            param.data = {op: "updateEnsureSetting",setting:JSON.stringify(param.setting)};
            webInterface.access(param);
        },
        /**
         * 添加个税扣除
         * @param deduct 个税扣除信息
         * @param success
         * @param fail
         */
        insertDeduct:function (param) {
            param.url = this.url;
            param.data = {op: "insertDeduct",deduct:JSON.stringify(param.deduct)};
            webInterface.access(param);
        },
        /**
         * 修改个税扣除
         * @param deduct 个税扣除信息
         * @param success
         * @param fail
         */
        updateDeduct:function (param) {
            param.url = this.url;
            param.data = {op: "updateDeduct",deduct:JSON.stringify(param.deduct)};
            webInterface.access(param);
        },
        /**
         * 导入个税扣除
         * @param deducts 个税扣除集合
         * @param success
         * @param fail
         */
        importDeducts:function (param) {
            param.url = this.url;
            param.data = {op: "importDeducts",deducts:JSON.stringify(param.deducts)};
            webInterface.access(param);
        },
        /**
         * 添加工资卡
         * @param payCard 工资卡信息
         * @param success
         * @param fail
         */
        insertCard:function (param) {
            param.url = this.url;
            param.data = {op: "insertCard",payCard:JSON.stringify(param.payCard)};
            webInterface.access(param);
        },
        /**
         * 获取社保设置
         * @param id 员工id
         * @param success
         * @param fail
         */
        getEnsureSetting:function (param) {
            param.url = this.url;
            param.data = {op: "getEnsureSetting",id:param.id};
            webInterface.access(param);
        },
        /**
         *
         * @param start 开始月份
         * @param end   结束月份
         * @param eids 员工集合
         * @param sid 结算单id
         * @param success
         * @param fail
         */
        readBase:function (param) {
            param.url = this.url;
            param.data = {op: "readBase",start:param.start,end:param.end,eids:param.eids,sid:param.sid};
            webInterface.access(param);
        },
        
        /**
         * 获取个税扣除
         * @param id 员工id
         * @param success
         * @param fail
         */
        getDeduct:function (param) {
            param.url = this.url;
            param.data = {op: "getDeduct",id:param.id};
            webInterface.access(param);
        },
        /**
         * 获取员工工资卡详情
         * @param id 员工id
         * @param success
         * @param fail
         */
        getCard:function (param) {
            param.url = this.url;
            param.data = {op: "getCard",id:param.id};
            webInterface.access(param);
        },
        /**
         * 修改员工工资卡
         * @param payCard 工资卡信息
         * @param success
         * @param fail
         */
        updateCard:function (param) {
            param.url = this.url;
            param.data = {op: "updateCard",payCard:JSON.stringify(param.payCard)};
            webInterface.access(param);
        },
        /**
         * 批量派遣员工
         * @param eids 员工id合集
         * @param cid 合作单位id
         * @param success
         * @param fail
         */
        dispatch:function (param) {
            param.url = this.url;
            param.data = {op: "dispatch",eids:param.eids,cid:param.cid};
            webInterface.access(param);
        },
        /**
         * 聘用人才库员工
         * @param id 员工id
         * @param category 0-内部员工 1-外部员工
         * @param success
         * @param fail
         */
        employ:function (param) {
            param.url = this.url;
            param.data = {op: "employ",id:param.id,category:param.category};
            webInterface.access(param);
        },
        /**
         * 获取个税扣除列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getDeducts:function (param) {
            param.url = this.url;
            param.data = {op:"getDeducts",param:JSON.stringify(param.param)};
            webInterface.access(param);
        },
        /**
         * 删除员工个税
         * @param id 员工id
         * @param success
         * @param fail
         */
        deleteDeduct:function (param) {
            param.url = this.url;
            param.data = {op:"deleteDeduct",id:param.id};
            webInterface.access(param);
        }
    },
    
    settlement:{
        url:url_base+"/settlement",
        /**
         * 获取结算单列表
         * @param param 查询列表
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param),category:param.category};
            webInterface.access(param);
        },

        /**
         * 获取结算单列表
         * @param param 查询列表
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param success
         * @param fail
         */
        getEmployeeDetail:function(param) {
            param.url = this.url;
            param.data = {op: "getEmployeeDetail", param:JSON.stringify(param.param),cardId:param.cardId};
            webInterface.access(param);
        },


        /**
         * 添加结算单
         * @param settlement 结算单信息
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param needDetail 0_不生成明细 1_自动生成明细
         * @param needCalculateSocial 0-不计算社保 1-计算社保
         * @param success
         * @param fail
         */
        insert:function (param) {
            param.url = this.url;
            param.data = {op: "insert",settlement:JSON.stringify(param.settlement), category:param.category,needDetail:param.needDetail
                ,needCalculateSocial:param.needCalculateSocial,employee_category:param.employee_category};
            webInterface.access(param);
        },
        
        update:function (param) {
            param.url = this.url;
            param.data = {op: "update",settlement:JSON.stringify(param.settlement)};
            webInterface.access(param);
        },
        
        /**
         * 删除结算单
         * @param id 结算单id
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param success
         * @param fail
         */
        delete:function (param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id,category:param.category};
            webInterface.access(param);
        },
        
        /**
         * 删除结算单明细
         * @param id 结算单明细id
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param success
         * @param fail
         */
        deleteDetail:function (param) {
            param.url = this.url;
            param.data = {op:"deleteDetail",id:param.id,category:param.category};
            webInterface.access(param);
        },
        /**
         * 复制结算单
         * @param id 结算单id
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param month 月份
         * @param success
         * @param fail
         */
        saveAs:function (param) {
            param.url = this.url;
            param.data = {op:"saveAs",id:param.id,month:param.month,category:param.category};
            webInterface.access(param);
        },
        /**
         * 获取结算单明细列表
         * @param param 查询参数列表
         * @param id 结算单编号
         * @param category 1-普通结算单明细 2-小时工结算单明细 3-商业保险结算单明细 4-特殊结算单
         * @param success
         * @param fail
         */
        getDetails:function(param) {
            param.url = this.url;
            param.data = {op: "getDetails", param:JSON.stringify(param.param),id:param.sid,category:param.category};
            webInterface.access(param);
        },
        /**
         * 修改结算单明细
         * @param details 结算单明细
         * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
         * @param success
         * @param fail
         */
        updateDetails:function (param) {
            param.url = this.url;
            param.data = {op: "updateDetails",details:JSON.stringify(param.details), category:param.category};
            webInterface.access(param);
        },
        /**
         * 导入结算单明细
         * @param id 结算单编号
         * @param details 结算单明细集合
         * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
         * @param success
         * @param fail
         */
        importDetails:function (param) {
            param.url = this.url;
            param.data = {op: "importDetails",id:param.sid,details:param.details, category:param.category};
            webInterface.access(param);
        },
        /**
         * 商业保险结算单明细替换
         * @param member1 待替换成员
         * @param member2 替换后的成员
         * @param success
         * @param fail
         */
        replaceDetails:function (param) {
            param.url = this.url;
            param.data = {op: "replaceDetails",member1:JSON.stringify(param.member1),member2:JSON.stringify(param.member2)};
            webInterface.access(param);
        },
        /**
         * 商业保险结算单确认新增
         * @param ids 待确认id序列
         * @param success
         * @param fail
         */
        confirmDetails1:function (param) {
            param.url = this.url;
            param.data = {op: "confirmDetails1",sid:param.sid,ids:param.ids,day:param.day};
            webInterface.access(param);
        },
        /**
         * 商业保险结算单确认替换
         * @param ids1 待替换成员
         * @param ids2 替换后的成员
         * @param success
         * @param fail
         */
        confirmDetails2:function (param) {
            param.url = this.url;
            param.data = {op: "confirmDetails2",sid:param.sid,ids1:param.ids1,ids2:param.ids2,day:param.day};
            webInterface.access(param);
        },
        /**
         * 导出结算单明细
         * @param id 结算单编号
         * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
         * @param success
         * @param fail
         */
        exportDetails:function (param) {
            param.url = this.url;
            param.data = {op: "exportDetails",id:param.id, category:param.category};
            webInterface.access(param);
        },
        
        /**
         * 补缴
         * @param start 起始月份
         * @param end 结束月份
         * @param sid 结算单id
         * @param employees 封装好的员工集合
         * @param success
         * @param fail
         */
        fillup:function (param) {
            param.url = this.url;
            param.data = {op: "fillup",start:param.start,end:param.end,sid:param.sid,employees:JSON.stringify(param.employees)};
            webInterface.access(param);
        },
        /**
         * 社保补差
         * @param start 起始月份
         * @param end 结束月份
         * @param sid 结算单id
         * @param eids 员工id集合
         * @param success
         * @param fail
         */
        makeup:function (param) {
            param.url = this.url;
            param.data = {op: "makeup",start:param.start,end:param.end,sid:param.sid, employees:JSON.stringify(param.employees)};
            webInterface.access(param);
        },
        /**
         * 提交结算单
         * @param id 结算单id
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param success
         * @param fail
         */
        commit:function (param) {
            param.url = this.url;
            param.data = {op: "commit",id:param.id, category:param.category};
            webInterface.access(param);
        },
        /**
         * 审核结算单
         * @param id 结算单id
         * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
         * @param level 0-初审 1-终审
         * @param pass true-通过 false-不通过
         * @param reason 不通过的原因
         * @param success
         * @param fail
         */
        check:function (param) {
            param.url = this.url;
            param.data = {op: "check",id:param.id, category:param.category,level:param.level,pass:param.pass,reason:param.reason};
            webInterface.access(param);
        },
        /**
         * 重置结算单
         * @param id 结算单id
         * @param category 0-结算单 1-小时工结算单
         * @param success
         * @param fail
         */
        reset:function (param) {
            param.url = this.url;
            param.data = {op: "reset",id:param.id, category:param.category};
            webInterface.access(param);
        },
        /**
         * 扣款
         * @param id 结算单id
         * @param category 0-结算单 1-小时工结算单
         * @param success
         * @param fail
         */
        charge:function (param) {
            param.url = this.url;
            param.data = {op: "charge",id:param.id, category:param.category};
            webInterface.access(param);
        },
        /**
         * 发放工资
         * @param id 结算单id
         * @param category 0-结算单 1-小时工结算单
         * @param success
         * @param fail
         */
        payroll:function (param) {
            param.url = this.url;
            param.data = {op: "payroll",id:param.id, category:param.category};
            webInterface.access(param);
        },
        /**
         * 导出工资发放表
         * @param id 结算单id
         * @param category 0-招商银行 1-农业银行 2-浦发银行  3-交通银行
         * @param success
         * @param fail
         */
        exportBank:function (param) {
            param.url = this.url;
            param.data = {op: "exportBank",id:param.id,category:param.category};
            webInterface.access(param);
        },
        /**
         * 查询结算单日志
         * @param param 查询参数
         * @param id 结算单id
         * @param category 0-结算单 1-小时工结算单
         * @param success
         * @param fail
         */
        getLogs:function (param) {
            param.url = this.url;
            param.data = {op: "getLogs",param:JSON.stringify(param.param),id:param.id,category:param.category};
            webInterface.access(param);
        },
        
        /**
         * 保存结算单并且自动计算结算单明细
         * @param sid 结算单id
         * @param cid 合作单位id
         * @param category 1_普通结算单 2_小时工结算单  3_商业保险结算单
         * @param success
         * @param fail
         */
        calcDetail:function (param) {
            param.url = this.url;
            param.data = {op: "calcDetail",sid:param.sid,cid:param.cid,category:param.category};
            webInterface.access(param);
        },
        
        /**
         * 保存并且计算结算单
         * @param sid 结算单id
         * @param category 0_普通结算单 1_小时工结算单 2_商业保险结算单
         * @param success
         * @param fail
         */
        calcSettlement:function (param) {
            param.url = this.url;
            param.data = {op: "calculate",sid:param.sid,category:param.category};
            webInterface.access(param);
        },
    },

    insurance:{
        url:url_base+"/insurance",
        /**
         * 获取参保列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param)};
            webInterface.access(param);
        },

        /**
         * 导出参保单
         * @param category 0-社保 1-医保 2-公积金
         * @param success
         * @param fail
         */
        export:function(param) {
            param.url = this.url;
            param.data = {op: "export",category:param.category};
            webInterface.access(param);
        },

        /**
         * 校对参保单
         * @param insurance 将校对名单册导入后台
         * @param category 0-社保 1-医保 2-公积金
         * @param success
         * @param fail
         */
        check:function(param) {
            param.url = this.url;
            param.data = {op: "check", insurance:JSON.stringify(param.insurance),category:param.category};
            webInterface.access(param);
        },

        /**
         * 校对异常参保单
         * @param insurance 将校对的异常名单册导入后台
         * @param category 0-社保 1-医保 2-公积金
         * @param success
         * @param fail
         */
        checkErr:function(param) {
            param.url = this.url;
            param.data = {op: "checkErr", insurance:JSON.stringify(param.insurance),category:param.category};
            webInterface.access(param);
        },

        /**
         * 删除参保单
         * @param id 员工id
         * @param category 0-社保 1-医保 2-公积金
         * @param success
         * @param fail
         */
        delete:function (param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id};
            webInterface.access(param);
        },

        /**
         * 添加和修改参保单
         * @param insurances 参保单集合
         * @param success
         * @param fail
         */
        insertAndupdate:function(param) {
            param.url = this.url;
            param.data = {op: "insertAndupdate", eid:param.eid,insurances_insert:JSON.stringify(param.insurances_insert),insurances_update:JSON.stringify(param.insurances_update)};
            webInterface.access(param);
        },

        /**
         * 添加参保单
         * @param insurances 参保单集合
         * @param success
         * @param fail
         */
        insertBatch:function(param) {
            param.url = this.url;
            param.data = {op: "insertBatch", eids:param.eids,insurances:JSON.stringify(param.insurances)};
            webInterface.access(param);
        },

        /**
         * 获取参保单详情
         * @param id 员工id
         * @param category 0-社保 1-医保 2-公积金
         * @param success
         * @param fail
         */
        get:function(param) {
            param.url = this.url;
            param.data = {op: "get", eid:param.eid};
            webInterface.access(param);
        },

        /**
         * 修改参保单
         * @param insurance 参保单信息
         * @param success
         * @param fail
         */
        update:function(param) {
            param.url = this.url;
            param.data = {op: "update", insurances:JSON.stringify(param.insurances)};
            webInterface.access(param);
        },
    },

    insured:{
        url:url_base+"/insured",
        /**
         * 获取参保列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param)};
            webInterface.access(param);
        },

        insert:function(param) {
            param.url = this.url;
            param.data = {op: "insert", insured:JSON.stringify(param.insured)};
            webInterface.access(param);
        },

        insertBatch:function(param) {
            param.url = this.url;
            param.data = {op: "insertBatch", insureds:param.insureds};
            webInterface.access(param);
        },

        update:function(param) {
            param.url = this.url;
            param.data = {op: "update", insured:JSON.stringify(param.insured)};
            webInterface.access(param);
        },

        delete:function(param) {
            param.url = this.url;
            param.data = {op: "delete", id:param.id};
            webInterface.access(param);
        },
        get:function(param) {
            param.url = this.url;
            param.data = {op: "get", id:param.id};
            webInterface.access(param);
        }
    },

    product:{
        url:url_base+"/product",
        /**
         * 获取保险产品列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param)};
            webInterface.access(param);
        },
        /**
         * 删除保险产品
         * @param id 保险产品id
         * @param success
         * @param fail
         */
        delete:function (param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id};
            webInterface.access(param);
        },
        /**
         * 添加保险产品
         * @param product 产品信息
         * @param success
         * @param fail
         */
        insert:function (param) {
            param.url = this.url;
            param.data = {op: "insert",product:JSON.stringify(param.product)};
            webInterface.access(param);
        },
        /**
         * 获取保险产品详情
         * @param id 产品id
         * @param success
         * @param fail
         */
        get:function (param) {
            param.url = this.url;
            param.data = {op:"get",id:param.id};
            webInterface.access(param);
        },
        /**
         * 修改保险产品详情
         * @param product 要修改的产品信息
         * @param success
         * @param fail
         */
        update:function (param) {
            param.url = this.url;
            param.data = {op:"update",product:JSON.stringify(param.product)};
            webInterface.access(param);
        },
    },
    
    finance:{
        url:url_base+"/finance",

        /**
         * 到账确认
         * @param balance 金额
         * @param id 该派遣方id
         * @param success
         * @param fail
         */
        arrive:function(param) {
            param.url = this.url;
            param.data = {op: "arrive",balance:param.balance,id:param.id};
            webInterface.access(param);
        },
        /**
         * 资金明细
         * @param param 查询参数
         * @param cid 合作单位编号
         * @param success
         * @param fail
         */
        getTransactions:function(param) {
            param.url = this.url;
            param.data = {op: "getTransactions",param:JSON.stringify(param.param),cid:param.cid};
            webInterface.access(param);
        },

        /**
         * 获取个税申报表列表
         * @param param 查询参数
         * @param success
         * @param fail
         */
        getTaxs:function(param) {
            param.url = this.url;
            param.data = {op: "getTaxs",param:JSON.stringify(param.param)};
            webInterface.access(param);
        },
        /**
         * 导出个税申报
         * @param success
         * @param fail
         */
        exportTaxes:function(param) {
            param.url = this.url;
            param.data = {op: "exportTaxes"};
            webInterface.access(param);
        }
    },

    rule:{
        url:url_base+"/rule",

        /**
         * 获取规则列表
         * @param param 查询参数
         * @param category 类型 0-医保 1-社保 2-公积金
         * @param success
         * @param fail
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param),category:param.category};
            webInterface.access(param);
        },
        /**
         * 添加规则
         * @param rule 规则信息
         * @param category 类型 0-医保 1-社保 2-公积金
         * @param success
         * @param fail
         */
        insert:function(param) {
            param.url = this.url;
            param.data = {op: "insert",rule:JSON.stringify(param.rule),category:param.category};
            webInterface.access(param);
        },
        /**
         * 删除规则
         * @param id 规则id
         * @param category 类型 0-医保 1-社保 2-公积金
         * @param success
         * @param fail
         */
        delete:function(param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id,category:param.category};
            webInterface.access(param);
        },
        /**
         * 修改规则
         * @param rule 规则信息
         * @param category 类型 0-医保 1-社保 2-公积金
         * @param success
         * @param fail
         */
        update:function(param) {
            param.url = this.url;
            param.data = {op:"update",rule:JSON.stringify(param.rule),category:param.category};
            webInterface.access(param);
        },
        /**
         * 获取规则详情
         * @param id 规则id
         * @param category 类型 0-医保 1-社保 2-公积金
         * @param success
         * @param fail
         */
        get:function(param) {
            param.url = this.url;
            param.data = {op:"get",id:param.id,category:param.category};
            webInterface.access(param);
        },
        /**
         * 获取最新规则
         * @param city 城市
         * @param category 类型 0-医保 1-社保 2-公积金
         * @param success
         * @param fail
         */
        getLast:function(param) {
            param.url = this.url;
            param.data = {op:"getLast",city:param.city,category:param.category};
            webInterface.access(param);
        }
    },

    notice:{
        url:url_base+"/notice",
        /**
         * 获取公告列表
         */
        getList:function(param) {
            param.url = this.url;
            param.data = {op: "getList", param:JSON.stringify(param.param)};
            webInterface.access(param);
        },
        /**
         * 添加公告
         * @param notice 公告信息
         * @param success
         * @param fail
         */
        insert:function (param) {
            param.url = this.url;
            param.data = {op: "insert",notice:JSON.stringify(param.notice)};
            webInterface.access(param);
        },
        /**
         * 删除公告
         * @param id 公告id
         * @param success
         * @param fail
         */
        delete:function (param) {
            param.url = this.url;
            param.data = {op:"delete",id:param.id};
            webInterface.access(param);
        },
        /**
         * 获取公告详情
         * @param id 公告id
         * @param success
         * @param fail
         */
        get:function (param) {
            param.url = this.url;
            param.data = {op:"get",id:param.id};
            webInterface.access(param);
        },
        /**
         * 修改公告
         * @param notice 公告信息
         * @param success
         * @param fail
         */
        update:function (param) {
            param.url = this.url;
            param.data = {op:"update",notice:JSON.stringify(param.notice)};
            webInterface.access(param);
        },
    },
    
    file:{
        url:url_base+"/file",
        /**
         * 读取xls数据反馈给前台
         * @param success
         * @param fail
         */
        readXls:function(param) {
            param.url = this.url;
            param.data = {op: "readXls"};
            webInterface.access(param);
        },
        /**
         * 判断附件是否存在，返回结果为{success:true/false,exist:true/false}
         * @param category
         * 0-员工头像；
         * 1-平台与派遣单位的合同；
         * 2-派遣单位与合作单位的合同；
         * 3-派遣单位与员工的合同；
         * 4-员工离职证明
         * 5-小时工结算单明细模板
         * 6-商业保险结算单明细模板
         * 7-员工信息模板
         * 8-员工合同信息模板
         * @param id 头像、合同、离职证明对应的员工或合同id
         * @param success
         * @param fail
         */
        exist:function (param) {
            param.url = this.url;
            param.data = {op: "exist",id:param.id,category:param.category};
            webInterface.access(param);
        },

        /**
         * 下载文件
         * * @param category
         * 0-员工头像；
         * 1-平台与派遣单位的合同；
         * 2-派遣单位与合作单位的合同；
         * 3-派遣单位与员工的合同；
         * 4-员工离职证明
         * 5-小时工结算单明细模板
         * 6-商业保险结算单明细模板
         * 7-员工信息模板
         * 8-员工合同信息模板
         * @param id 头像、合同、离职证明对应的员工或合同id
         * @param success
         * @param fail
         */
        download:function (param) {
            param.url = this.url;
            param.data = {op: "download",id:param.id,category:param.category};
            webInterface.access(param);
        }
    },

    /**
     * 内部访问函数
     * @param url 接口地址
     * @param para 接口参数
     * @param success 成功回调函数
     * @param reqType 请求类型
     * @param fail 失败回调函数
     */
    access:function(param) {
        if(param.requestType == undefined){
            param.requestType = this.POST;
        }
        if(param.async == undefined){
            param.async = this.ASYNC;
        }
        if(param.success == undefined){
            param.success = function() {}
        }
        if(!param.fail){
            param.fail = function(data) {
                if(data.code == 0){
                    window.location.href = "/login.html";
                }else {
                    layer.msg(data.msg);
                }
            }
        }
        $.ajax({
            url: param.url,
            data:param.data,
            type: param.requestType,
            async:param.async,
            dataType:"json",
            success:function(data,status) {
                if(!data.success) {
                    param.fail(data);
                }else{
                    param.success(data);
                }
            },
            error:function(data) {
                if(data.status==403){
                    alert("会话已终止，请重新登录");
                    window.location.href = data.responseJSON.url;
                }
            }
        });
    }

}


