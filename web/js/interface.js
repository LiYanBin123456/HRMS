/**
 * 后台访问接口
 *
 */
var base = "/hrms/verify" ;

//客户管理相关接口
var InterfaceClient = function() {
    var url = base+"/client";
    /**
     * 获取客户列表
     * @param param 查询参数
     * @param category 0-派遣方 1-合作单位 2-供应商
     * @param success
     * @param fail
     */
    this.getList = function(param,category,success,fail){
        var para = {op: "getList", param:JSON.stringify(param),category: category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取待分配管理员的客户列表
     * @param category 0-派遣方 1-合作单位
     * @param success
     * @param fail
     */
    this.getAllocating = function(category,success,fail){
        var para = {op: "getAllocating", category: category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取管理员管理的客户列表
     * @param category 0-派遣方 1-合作单位
     * @param success
     * @param fail
     */
    this.getAllocated = function(aid,category,success,fail){
        var para = {op: "getAllocated", aid:aid, category: category};
        access(url,para,1,success,fail);
    };
    /**
     * 添加客户
     * @param client 客户信息
     * @param category 0-派遣方 1-合作单位 2-供应商
     * @param success
     * @param fail
     */
    this.insert = function (client,category,success,fail) {
        var para = {op: "insert",client:JSON.stringify(client),category: category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取客户信息
     * @param id 客户id
     * @param category 0-派遣方 1-合作单位 2-供应商
     * @param success
     * @param fail
     */
    this.get = function (id,category,success,fail) {
        var para = {op:"get",id:id, category: category};
        access(url,para,1,success,fail);
    };
    /**
     * 修改客户信息
     * @param client 客户信息
     * @param category 0-派遣方 1-合作单位 2-供应商
     * @param success
     * @param fail
     */
    this.update = function (client,category,success,fail) {
        var para = {op:"update",client:JSON.stringify(client), category: category};
        access(url,para,1,success,fail);
    };

    /**
     *
     * @param id 要修改状态的客户id
     * @param category 客户类别 0—派遣方 1—合作客户 2_供应商
     * @param type  要修改的状态 0—合作 1—潜在 2—流失； 供应商 0 合作 1 流失
     * @param success
     * @param fail
     */
    this.updateStatus = function (id,category,type,success,fail) {
        var para = {op:"updateStatus",id:id, category: category,type:type};
        access(url,para,1,success,fail);
    };
    /**
     * 获取客户最新自定义工资
     * @param id 客户id
     * @param success
     * @param fail
     */
    this.getLastSalaryDefine = function (id,success,fail) {
        var para = {op:"getLastSalaryDefine",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 根据月份获取自定义工资
     * @param id 客户id
     * @param month 月份
     * @param success
     * @param fail
     */
    this.getSalaryDefine = function (id,month,success,fail) {
        var para = {op:"getSalaryDefine",id:id,month:month};
        access(url,para,1,success,fail);
    };
    /**
     * 添加自定义工资
     * @param mapSalary 自定义工资
     * @param success
     * @param fail
     */
    this.insertSalaryDefine = function (mapSalary,success,fail) {
        var para = {op: "insertSalaryDefine",mapSalary:JSON.stringify(mapSalary)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取财务信息
     * @param id 客户id
     * @param category 0-派遣方 1-合作单位
     * @param success
     * @param fail
     */
    this.getFinance = function (id,category,success,fail) {
        var para = {op:"getFinance",id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 添加财务信息
     * @param finance 财务信息
     * @param success
     * @param fail
     */
    this.insertFinance = function (finance,success,fail) {
        var para = {op: "insertFinance",finance:JSON.stringify(finance)};
        access(url,para,1,success,fail);
    };
    /**
     * 修改财务信息
     * @param finance 财务信息
     * @param success
     * @param fail
     */
    this.updateFinance = function (finance,success,fail) {
        var para = {op:"updateFinance",finance:JSON.stringify(finance)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除客户
     * @param id 客户id
     * @param category 0-派遣方 1-合作单位 2-供应商
     * @param status 0-合作客户 1-潜在客户
     * @param success
     * @param fail
     */
    this.delete = function (id,category,status,success,fail) {
        var para = {op:"delete",id:id, category: category,status: status};
        access(url,para,1,success,fail);
    };
    /**
     * 分配管理员
     * @param aid 管理员id
     * @param category 0-派遣方 1-合作单位
     * @param cids 给哪些客户分配管理员
     * @param success
     * @param fail
     */
    this.allocate = function (aid,category,cids,success,fail) {
        var para = {op:"allocate",aid:aid,category: category,cids:cids};
        access(url,para,1,success,fail);
    };
    /**
     * 获取合作客户和余额
     * @param param 查询参数
     * @param success
     * @param fail
     */
    this.getFinances = function (param,success,fail) {
        var para = {op:"getFinances",param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
};

//规则管理相关接口
var InterfaceRule = function() {
    var url = base+"/rule";//servlet的url地址
    /**
     * 获取规则列表
     * @param param 查询参数
     * @param category 类型 0-医保 1-社保 2-公积金
     * @param success
     * @param fail
     */
    this.getList = function(param,category,success,fail){
        var para = {op: "getList", param:JSON.stringify(param),category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 添加规则
     * @param rule 规则信息
     * @param category 类型 0-医保 1-社保 2-公积金
     * @param success
     * @param fail
     */
    this.insert = function (rule,category,success,fail) {
        var para = {op: "insert",rule:JSON.stringify(rule),category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 删除规则
     * @param id 规则id
     * @param category 类型 0-医保 1-社保 2-公积金
     * @param success
     * @param fail
     */
    this.delete = function (id,category,success,fail) {
        var para = {op:"delete",id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 修改规则
     * @param rule 规则信息
     * @param category 类型 0-医保 1-社保 2-公积金
     * @param success
     * @param fail
     */
    this.update = function (rule,category,success,fail) {
        var para = {op:"update",rule:JSON.stringify(rule),category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取规则详情
     * @param id 规则id
     * @param category 类型 0-医保 1-社保 2-公积金
     * @param success
     * @param fail
     */
    this.get = function (id,category,success,fail) {
        var para = {op:"get",id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取最新规则
     * @param city 城市
     * @param category 类型 0-医保 1-社保 2-公积金
     * @param success
     * @param fail
     */
    this.getLast = function (city,category,success,fail) {
        var para = {op:"getLast",city:city,category:category};
        access(url,para,1,success,fail);
    };
};

//合同管理相关接口
var InterfaceContract = function() {
    var url = base+"/contract";
    /**
     * 获取合同列表
     * @param param 查询参数
     * @param type 类型 A-平台和派遣单位合同 B-派遣单位和合作单位 C-派遣单位和员工
     * @param success
     * @param fail
     */
    this.getList = function(param,type,success,fail){
        var para = {op: "getList", param:JSON.stringify(param),type:type};
        access(url,para,1,success,fail);
    };
    /**
     * 获取最新合同
     * @param id 该客户id
     * @param type 类型 A-平台和派遣单位合同 B-派遣单位和合作单位 C-派遣单位和员工
     * @param success
     * @param fail
     */
    this.getLast = function(id,type,success,fail){
        var para = {op: "getLast", id:id,type:type};
        access(url,para,1,success,fail);
    };
    /**
     * 修改合同
     * @param contract 合同信息
     * @param success
     * @param fail
     */
    this.update = function (contract,success,fail) {
        var para = {op:"update",contract:JSON.stringify(contract)};
        access(url,para,1,success,fail);
    };
    /**
     * 添加合同
     * @param contract 合同信息
     * @param success
     * @param fail
     */
    this.insert = function (contract,success,fail) {
        var para = {op: "insert",contract:JSON.stringify(contract)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取合同详情
     * @param id 合同id
     * @param success
     * @param fail
     */
    this.get = function (id,success,fail) {
        var para = {op:"get",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 删除合同
     * @param id 合同id
     * @param success
     * @param fail
     */
    this.delete = function (id,success,fail) {
        var para = {op:"delete",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 添加服务项目
     * @param serve 服务项目信息
     * @param success
     * @param fail
     */
    this.insertService = function (serve,success,fail) {
        var para = {op: "insertService",serve:JSON.stringify(serve)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取合同服务项目详情
     * @param id 合同id
     * @param success
     * @param fail
     */
    this.getService = function (id,success,fail) {
        var para = {op: "getService",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 修改服务项目
     * @param serve 服务项目信息
     * @param success
     * @param fail
     */
    this.updateService = function (serve,success,fail) {
        var para = {op: "updateService",serve:JSON.stringify(serve)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取当前合作单位的有效服务项目列表
     * @param param 查询参数
     * @param id 该客户id
     * @param success
     * @param fail
     */
    this.getServiceList = function (param,id,success,fail) {
        var para = {op: "getServiceList",param:JSON.stringify(param),id:id};
        access(url,para,1,success,fail);
    };

    /**
     * 批量插入员工合同
     * @param contracts 员工合同集合
     * @param success
     * @param fail
     */
    this.insertContracts = function (contracts,success,fail) {
        var para = {op: "insertContracts",contracts:contracts};
        access(url,para,1,success,fail);
    }
};

//账号管理相关接口
var InterfaceAccount = function () {
    var url = base+"/account";//servlet的url地址
    /**
     * 登录
     * @param username 账号
     * @param password 密码
     * @param success
     * @param fail
     */
    this.login = function(username,password,success,fail){
        var para = {op: "login", username:username,password:password};
        access(url,para,1,success,fail);
    };
    /**
     * 注销
     * @param success
     * @param fail
     */
    this.quit = function(success,fail){
        var para = {op: "quit"};
        access(url,para,1,success,fail);
    };
    /**
     * 获取账户列表
     * @param param 查询参数
     * @param success
     * @param fail
     */
    this.getList = function(param,success,fail){
        var para = {op: "getList", param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
    /**
     * 添加账号
     * @param account 账号信息
     * @param success
     * @param fail
     */
    this.insert = function (account,success,fail) {
        var para = {op: "insert",account:JSON.stringify(account)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除账号
     * @param id 账号id
     * @param success
     * @param fail
     */
    this.delete = function (id,success,fail) {
        var para = {op: "delete",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 获取账号详情
     * @param id 账号id
     * @param success
     * @param fail
     */
    this.get = function (success,fail) {
        var para = {op:"get"};
        access(url,para,1,success,fail);
    };
    /**
     * 修改账号
     * @param account 账号信息
     * @param success
     * @param fail
     */
    this.update = function (account,success,fail) {
        var para = {op:"update",account:JSON.stringify(account)};
        access(url,para,1,success,fail);
    };
    /**
     * 设置权限
     * @param id 账号id
     * @param permission 权限
     * @param success
     * @param fail
     */
    this.permit = function (id,permission,success,fail) {
        var para = {op:"permit",id:id,permission:permission};
        access(url,para,1,success,fail);
    };
};

//公告相关接口
var InterfaceNotice = function() {
    var url = base+"/notice";
    /**
     * 获取公告列表
     */
    this.getList = function(param,success,fail){
        var para = {op: "getList", param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
    /**
     * 添加公告
     * @param notice 公告信息
     * @param success
     * @param fail
     */
    this.insert = function (notice,success,fail) {
        var para = {op: "insert",notice:JSON.stringify(notice)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除公告
     * @param id 公告id
     * @param success
     * @param fail
     */
    this.delete = function (id,success,fail) {
        var para = {op:"delete",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 获取公告详情
     * @param id 公告id
     * @param success
     * @param fail
     */
    this.get = function (id,success,fail) {
        var para = {op:"get",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 修改公告
     * @param notice 公告信息
     * @param success
     * @param fail
     */
    this.update = function (notice,success,fail) {
        var para = {op:"update",notice:JSON.stringify(notice)};
        access(url,para,1,success,fail);
    };
};

//员工管理相关接口
var InterfaceEmployee = function () {
    var url = base+"/employee";//servlet的url地址
    /**
     * 获取员工列表
     * @param param 查询参数
     * @param category 0-员工信息 1-员工补充信息
     * @param success
     * @param fail
     */
    this.getList = function(param,success,fail){
        var para = {op: "getList", param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
    /**
     * 插入员工信息
     * @param employee 员工信息
     * @param extract  员工额外信息
     * @param success  成功
     * @param fail
     */
    this.insert = function (employee,success,fail) {
        var para = {op: "insert",employee:JSON.stringify(employee)};
        access(url,para,1,success,fail);
    };

    /**
     * 插入员工补充信息
     * @param extract  员工补充信息
     * @param success  成功
     * @param fail
     */
    this.insertExtra = function (extra,success,fail) {
        var para = {op: "insertExtra",extract:JSON.stringify(extra)};
        access(url,para,1,success,fail);
    };
    /**
     * 批量导入员工信息
     * @param employees 员工信息集合
     * @param extracts 员工补充信息集合
     * @param success
     * @param fail
     */
    this.insertBatch = function (employees,success,fail) {
        var para = {op: "insertBatch",employees:employees};
        access(url,para,1,success,fail);
    };
    /**
     * 获取员工详情
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.get = function (id,success,fail) {
        var para = {op: "get",id:id};
        access(url,para,1,success,fail);
    };/**
     * 获取员工补充信息
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.getExtra = function (id,success,fail) {
        var para = {op: "getExtra",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 修改员工
     * @param employee 员工信息
     * @param category 0-员工信息 1-员工补充信息
     * @param success
     * @param fail
     */
    this.update = function (employee,success,fail) {
        var para = {op: "update",employee:JSON.stringify(employee)};
        access(url,para,1,success,fail);
    };

    /**
     * 修改员工补充信息
     * @param extra 员工补充信息
     * @param success
     * @param fail
     */
    this.updateExtra = function (extra,success,fail) {
        var para = {op: "update",extra:JSON.stringify(extra)};
        access(url,para,1,success,fail);
    };

    /**
     * 离职退休
     * @param reason 离职原因信息
     * @param category 0-离职 1-退休
     * @param date 离职或退休时间
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.leave = function (id,reason,date,category,success,fail) {
        var para = {op: "leave",reason:reason,id:id,date:date, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 删除员工
     * @param id 员工id
     * @param category 0-移入人才库 1-直接删除
     * @param success
     * @param fail
     */
    this.delete = function (id,category,success,fail) {
        var para = {op:"delete",id:id, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 添加社保设置
     * @param setting 社保设置信息
     * @param success
     * @param fail
     */
    this.insertEnsureSetting = function (setting,success,fail) {
        var para = {op: "insertEnsureSetting",setting:JSON.stringify(setting)};
        access(url,para,1,success,fail);
    };
    /**
     * 修改社保设置
     * @param setting 社保设置信息
     * @param success
     * @param fail
     */
    this.updateEnsureSetting = function (setting,success,fail) {
        var para = {op: "updateEnsureSetting",setting:JSON.stringify(setting)};
        access(url,para,1,success,fail);
    };
    /**
     * 添加个税扣除
     * @param deduct 个税扣除信息
     * @param success
     * @param fail
     */
    this.insertDeduct = function (deduct,success,fail) {
        var para = {op: "insertDeduct",deduct:JSON.stringify(deduct)};
        access(url,para,1,success,fail);
    };
    /**
     * 修改个税扣除
     * @param deduct 个税扣除信息
     * @param success
     * @param fail
     */
    this.updateDeduct = function (deduct,success,fail) {
        var para = {op: "updateDeduct",deduct:JSON.stringify(deduct)};
        access(url,para,1,success,fail);
    };
    /**
     * 导入个税扣除
     * @param deducts 个税扣除集合
     * @param success
     * @param fail
     */
    this.importDeducts = function (deducts,success,fail) {
        var para = {op: "importDeducts",deducts:JSON.stringify(deducts)};
        access(url,para,1,success,fail);
    };
    /**
     * 添加工资卡
     * @param payCard 工资卡信息
     * @param success
     * @param fail
     */
    this.insertCard = function (payCard,success,fail) {
        var para = {op: "insertCard",payCard:JSON.stringify(payCard)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取社保设置
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.getEnsureSetting = function (id,success,fail) {
        var para = {op: "getEnsureSetting",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 获取个税扣除
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.getDeduct = function (id,success,fail) {
        var para = {op: "getDeduct",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 获取员工工资卡详情
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.getCard = function (id,success,fail) {
        var para = {op: "getCard",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 修改员工工资卡
     * @param payCard 工资卡信息
     * @param success
     * @param fail
     */
    this.updateCard = function (payCard,success,fail) {
        var para = {op: "updateCard",payCard:JSON.stringify(payCard)};
        access(url,para,1,success,fail);
    };
    /**
     * 批量派遣员工
     * @param eids 员工id合集
     * @param cid 合作单位id
     * @param success
     * @param fail
     */
    this.dispatch = function (eids,cid,success,fail) {
        var para = {op: "dispatch",eids:eids,cid:cid};
        access(url,para,1,success,fail);
    };
    /**
     * 聘用人才库员工
     * @param id 员工id
     * @param category 0-内部员工 1-外部员工
     * @param success
     * @param fail
     */
    this.employ = function (id,category,success,fail) {
        var para = {op: "employ",id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取个税扣除列表
     * @param param 查询参数
     * @param success
     * @param fail
     */
    this.getDeducts = function (param,success,fail) {
        var para = {op:"getDeducts",param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除员工个税
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.deleteDeduct = function (id,success,fail) {
        var para = {op:"deleteDeduct",id:id};
        access(url,para,1,success,fail);
    };
};

//结算单管理相关接口
var InterfaceSettlement = function () {
    var url = base+"/settlement";//servlet的url地址
    /**
     * 获取结算单列表
     * @param param 查询列表
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param success
     * @param fail
     */
    this.getList = function(param,category,success,fail){
        var para = {op: "getList", param:JSON.stringify(param),category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 添加结算单
     * @param settlement 结算单信息
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param type 0_不生成明细 1_自动生成明细
     * @param success
     * @param fail
     */
    this.insert = function (settlement,category,type,success,fail) {
        var para = {op: "insert",settlement:JSON.stringify(settlement), category:category,type:type};
        access(url,para,1,success,fail);
    };
    /**
     * 删除结算单
     * @param id 结算单id
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param success
     * @param fail
     */
    this.delete = function (id,category,success,fail) {
        var para = {op:"delete",id:id,category:category};
        access(url,para,1,success,fail);
    };

    /**
     * 删除结算单明细
     * @param id 结算单明细id
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param success
     * @param fail
     */
    this.deleteDetail = function (id,category,success,fail) {
        var para = {op:"deleteDetail",id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 复制结算单
     * @param id 结算单id
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param month 月份
     * @param success
     * @param fail
     */
    this.saveAs = function (id,category,month,success,fail) {
        var para = {op:"saveAs",id:id,month:month,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 获取结算单明细列表
     * @param param 查询参数列表
     * @param id 结算单编号
     * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
     * @param success
     * @param fail
     */
    this.getDetails = function(param,id,category,success,fail){
        var para = {op: "getDetails", param:JSON.stringify(param),id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 修改结算单明细
     * @param details 结算单明细
     * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
     * @param success
     * @param fail
     */
    this.updateDetails = function (details,category,success,fail) {
        var para = {op: "updateDetails",details:JSON.stringify(details), category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 导入结算单明细
     * @param id 结算单编号
     * @param details 结算单明细集合
     * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
     * @param success
     * @param fail
     */
    this.importDetails = function (id,details,category,success,fail) {
        var para = {op: "importDetails",id:id,details:details, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 导出结算单明细
     * @param id 结算单编号
     * @param category 0-普通结算单明细 1-小时工结算单明细 2-商业保险结算单明细
     * @param success
     * @param fail
     */
    this.exportDetails = function (id,category,success,fail) {
        var para = {op: "exportDetails",id:id, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 社保补缴
     * @param id 员工id
     * @param month 补缴的月份
     * @param success
     * @param fail
     */
    this.backup = function (id,month,success,fail) {
        var para = {op: "backup",id:id, month:month};
        access(url,para,1,success,fail);
    };
    /**
     * 社保补差
     * @param id 员工id
     * @param month 补差的月份
     * @param success
     * @param fail
     */
    this.makeup = function (id,month,success,fail) {
        var para = {op: "makeup",id:id, month:month};
        access(url,para,1,success,fail);
    };
    /**
     * 提交结算单
     * @param id 结算单id
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param success
     * @param fail
     */
    this.commit = function (id,category,success,fail) {
        var para = {op: "commit",id:id, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 审核结算单
     * @param id 结算单id
     * @param category 0-普通结算单 1-小时工结算单 2-商业保险结算单
     * @param status 2-一审 3-二审 4-终审
     * @param success
     * @param fail
     */
    this.check = function (id,category,status,success,fail) {
        var para = {op: "check",id:id, category:category,status:status};
        access(url,para,1,success,fail);
    };
    /**
     * 重置结算单
     * @param id 结算单id
     * @param category 0-结算单 1-小时工结算单
     * @param success
     * @param fail
     */
    this.reset = function (id,category,success,fail) {
        var para = {op: "reset",id:id, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 扣款
     * @param id 结算单id
     * @param category 0-结算单 1-小时工结算单
     * @param success
     * @param fail
     */
    this.deduct = function (id,category,success,fail) {
        var para = {op: "deduct",id:id, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 发放工资
     * @param id 结算单id
     * @param category 0-结算单 1-小时工结算单
     * @param success
     * @param fail
     */
    this.confirm = function (id,category,success,fail) {
        var para = {op: "confirm",id:id, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 导出工资发放表
     * @param id 结算单id
     * @param bank 银行
     * @param category 0-结算单 1-小时工结算单
     * @param success
     * @param fail
     */
    this.exportBank = function (id,bank,category,success,fail) {
        var para = {op: "exportBank",id:id,bank:bank, category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 查询结算单日志
     * @param param 查询参数
     * @param id 结算单id
     * @param category 0-结算单 1-小时工结算单
     * @param success
     * @param fail
     */
    this.getLogs = function (param,id,category,success,fail) {
        var para = {op: "getLogs",param:JSON.stringify(param),id:id,category:category};
        access(url,para,1,success,fail);
    };

    /**
     * 保存结算单并且自动计算结算单明细
     * @param sid 结算单id
     * @param cid 合作单位id
     * @param category 0_普通结算单 1_小时工结算单  2_商业保险结算单
     * @param success
     * @param fail
     */
    this.saveDetail = function (sid,cid,category,success,fail) {
        var para = {op: "saveDetail",sid:sid,cid:cid,category:category};
        access(url,para,1,success,fail);
    };

    /**
     * 保存并且计算结算单
     * @param sid 结算单id
     * @param cid 合作单位id
     * @param category 0_普通结算单 1_小时工结算单 2_商业保险结算单
     * @param success
     * @param fail
     */
    this.saveSettlement = function (sid,cid,category,success,fail) {
        var para = {op: "saveSettlement",sid:sid,cid:cid,category:category};
        access(url,para,1,success,fail);
    };
};

//参保单管理相关接口
var InterfaceInsurance = function () {
    var url = base+"/insurance";//servlet的url地址
    /**
     * 获取参保列表
     * @param param 查询参数
     * @param category 0-社保 1-医保 2-公积金
     * @param success
     * @param fail
     */
    this.getList = function(param,category,success,fail){
        var para = {op: "getList", param:JSON.stringify(param),category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 导出参保单
     * @param category 0-社保 1-医保 2-公积金
     * @param success
     * @param fail
     */
    this.export = function(category,success,fail){
        var para = {op: "export",category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 校对参保单
     * @param insurance 将校对名单册导入后台
     * @param category 0-社保 1-医保 2-公积金
     * @param success
     * @param fail
     */
    this.check = function(insurance,category,success,fail){
        var para = {op: "check", insurance:JSON.stringify(insurance),category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 删除参保单
     * @param id 员工id
     * @param category 0-社保 1-医保 2-公积金
     * @param success
     * @param fail
     */
    this.delete = function (id,category,success,fail) {
        var para = {op:"delete",id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 添加参保单
     * @param insurances 参保单集合
     * @param success
     * @param fail
     */
    this.insertBatch = function(insurances,success,fail){
        var para = {op: "insertBatch", insurances:JSON.stringify(insurances)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取参保单详情
     * @param id 员工id
     * @param category 0-社保 1-医保 2-公积金
     * @param success
     * @param fail
     */
    this.get = function(id,category,success,fail){
        var para = {op: "get", id:id,category:category};
        access(url,para,1,success,fail);
    };
    /**
     * 修改参保单
     * @param insurance 参保单信息
     * @param success
     * @param fail
     */
    this.update = function(insurance,success,fail){
        var para = {op: "update", insurance:JSON.stringify(insurance)};
        access(url,para,1,success,fail);
    };
};

//保险产品管理相关接口
var InterfaceProduct = function () {
    var url = base+"/product";//servlet的url地址
    /**
     * 获取保险产品列表
     * @param param 查询参数
     * @param success
     * @param fail
     */
    this.getList = function(param,success,fail){
        var para = {op: "getList", param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除保险产品
     * @param id 保险产品id
     * @param success
     * @param fail
     */
    this.delete = function (id,success,fail) {
        var para = {op:"delete",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 添加保险产品
     * @param product 产品信息
     * @param success
     * @param fail
     */
    this.insert = function (product,success,fail) {
        var para = {op: "insert",product:JSON.stringify(product)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取保险产品详情
     * @param id 产品id
     * @param success
     * @param fail
     */
    this.get = function (id,success,fail) {
        var para = {op:"get",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 修改保险产品详情
     * @param product 要修改的产品信息
     * @param success
     * @param fail
     */
    this.update = function (product,success,fail) {
        var para = {op:"update",product:JSON.stringify(product)};
        access(url,para,1,success,fail);
    };
};

//财务管理相关接口
var InterfaceFinance = function () {
    var url = base+"/finance";//servlet的url地址
    /**
     * 到账确认
     * @param balance 金额
     * @param id 该派遣方id
     * @param success
     * @param fail
     */
    this.arrive = function(balance,id,success,fail){
        var para = {op: "arrive",balance:balance,id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 资金明细
     * @param param 查询参数
     * @param cid 合作单位编号
     * @param success
     * @param fail
     */
    this.getTransactions = function(param,cid,success,fail){
        var para = {op: "getTransactions",param:JSON.stringify(param),cid:cid};
        access(url,para,1,success,fail);
    };

    /**
     * 获取个税申报表列表
     * @param param 查询参数
     * @param success
     * @param fail
     */
    this.getTaxs = function(param,success,fail){
        var para = {op: "getTaxs",param:JSON.stringify(param)};
        access(url,para,1,success,fail);
    };
    /**
     * 导出个税申报
     * @param success
     * @param fail
     */
    this.exportTaxes = function(success,fail){
        var para = {op: "exportTaxes"};
        access(url,para,1,success,fail);
    };

};
//文件管理相关接口
var InterfaceFile = function () {
    var url = base+"/file";//servlet的url地址
    /**
     * 读取xls数据反馈给前台
     * @param success
     * @param fail
     */
    this.readXls = function(success,fail){
        var para = {op: "readXls"};
        access(url,para,1,success,fail);
    };
    /**
     * 判断模板附件是否存在，返回结果为{success:true/false,exist:true/false}
     * @param category 0 小时工模板  1  商业保险模板   2员工模板
     * @param success
     * @param fail
     */
    this.exist = function (id,category,success,fail) {
        var para = {op: "exist",id:id,category:category};
        access(url,para,1,success,fail);
    };

    /**
     * 下载合同复印件
     * @param id 合同id
     * @param success
     * @param fail
     */
    this.downloadContract = function (id,success,fail) {
        var para = {op: "downloadContract",id:id};
        access(url,para,1,success,fail);
    };
    /**
     * 上传员工头像
     * @param file 头像图片
     * @param id 员工id
     * @param success
     * @param fail
     */
    this.uploadImg = function (file,id,success,fail) {
        var para = {op: "uploadImg",id:id,file:JSON.stringify(file)};
        access(url,para,1,success,fail);
    };
};

var iClient = new InterfaceClient();//客户管理
var iRule = new InterfaceRule();//规则管理
var iContract =new InterfaceContract();//合同管理
var iAccount =new InterfaceAccount();//账号管理
var iNotice =new InterfaceNotice();//公告管理
var iEmployee = new InterfaceEmployee();//员工管理
var iSettlement = new InterfaceSettlement();//结算单管理
var iInsurance = new InterfaceInsurance();//参保单管理
var iProduct = new InterfaceProduct();//保险产品管理
var iFinance = new InterfaceFinance();//财务管理
var iFile = new InterfaceFile();//文件管理

/**
 * 内部访问函数
 * @param url 接口地址
 * @param para 接口参数
 * @param success 成功回调函数
 * @param reqType 请求类型
 * @param fail 失败回调函数
 */
function access(url,para,reqType,success,fail) {
    $.ajax({
        url: url,
        data:para,
        type: reqType==0?"get":"post",
        dataType:"json",
        success:function(data,status) {
            if(!data.success) {
                if(fail) {
                    fail(data);
                }else{
                    if(data.code == 0){
                        window.location.href = "/hrms/login.html";
                    }else {
                        layer.msg(data.msg);
                    }
                }
            }else{
                if(success) {
                    success(data);
                }
            }
        },
        error:function (data) {
            if(data.status==403){
                alert("会话已终止，请重新登录");
                window.location.href = data.responseJSON.url;
            }
        }
    });
}
