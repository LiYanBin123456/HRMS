/**
 * 后台访问接口
 *
 */
var base = "/recruitment";
var InterfaceAccount = function () {
    var url = base+"/account";//servlet的url地址

    /**
     * 登录
     * @param name 账号
     * @param password 密码
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.login = function(name,password,success,fail){
        var para = {op: "login", name:name,password:password};
        access(url,para,1,success,fail);
    };
}

//公告相关接口
var 
//职位相关接口
var InterfaceJob = function () {
    var url = base+"/job";//servlet的url地址

    /**
     * 获取职位列表（先返回所有的职位，筛选条件以后再说）
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getList = function(param,success,fail){
        var para = {op: "getJobs", param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };
    /**
     * 添加职位
     * @param job 职位信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.insertJob = function (job,success,fail) {
        var para = {op: "insertJob",job:JSON.stringify(job)};
        access(url,para,1,success,fail);
    };
    /**
     * 刷新职位
     * @param jid 职位编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.freshJob = function (jid,success,fail) {
        var para = {op:"freshJob",jid:jid};
        access(url,para,0,success,fail);
    };

    /**
     * 上/ 下架职位
     * @param jid 职位编号
     * @param direction 上/下架指令，true-上架；false-下架
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.puton = function (jid,direction,success,fail) {
        var para = {op:"puton",jid:jid,direction:direction};
        access(url,para,1,success,fail);
    };
    /**
     * 批量，上架/下架职位
     * @param ids id集合字符串
     * @param direction 上架或者下架（true为上架）
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.putonBatch = function (ids,direction,success,fail) {
        var para = {op:"putonBatch",ids:ids,direction:direction};
        access(url,para,1,success,fail);
    };
    /**
     * 审核职位
     * @param jid 职位编号
     * @param direction 上/下架指令，true-上架；false-下架
     * @param reason 不通过原因
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.checkJob = function (jid,direction,reason,success,fail) {
        var para = {op:"checkJob",jid:jid,direction:direction,reason:reason};
        access(url,para,1,success,fail);
    };
    /**
     * 获取职位信息
     * @param jid 职位编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.get = function (jid,success,fail) {
        var para = {op:"getJob",jid:jid};
        access(url,para,0,success,fail);
    };
    /**
     * 收藏/取消收藏职位
     * @param jid 职位编号
     * @param cid 求职者编号
     * @param direction true-收藏；false-取消收藏
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.favoriteJob = function (jid,cid,direction,success,fail) {
        var para = {op:"favoriteJob",jid:jid,cid:cid,direction:direction};
        access(url,para,1,success,fail);
    };
    /**
     * 修改职位
     * @param job  职位信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateJob = function (job,success,fail) {
        var para = {op:"updateJob",job:JSON.stringify(job)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除职位
     * @param jid  职位编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.deleteJob = function (jid,success,fail) {
        var para = {op:"deleteJob",jid:jid};
        access(url,para,0,success,fail);
    };

    /**
     * 批量删除职位
     * @param ids  职位编号数组
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.deleteJobBatch = function (ids,success,fail) {
        var para = {op:"deleteJobBatch",ids:ids};
        access(url,para,1,success,fail);
    };
};

//简历相关接口
var InterfaceCurriculum = function () {
    var url = base+"/curriculum";//servlet的url地址

    /**
     * 获取简历列表
     * @param param 查询参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getList = function(param,success,fail){
        var para = {op: "getCurriculums",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };

    /**
     * 获取简历列表
     * @param param 查询参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getRecommends = function(param,success,fail){
        var para = {op: "getRecommends",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };

    this.getViewApps = function(success,fail,param){
        var para = {op: "getViewApps",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };
    /**
     * 审核简历
     * @param cid 求职者编号
     * @param direction true-审核通过
     * @param reason 审核不通过的原因
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.check = function (cid,direction,reason,success,fail) {
        var para = {op: "check",direction:direction,reason:reason};
        access(url,para,1,success,fail);
    };
    /**
     * 解封简历
     * @param cid 求职者编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.unseal = function (cid,success,fail) {
        var para = {op:"unseal",cid:cid};
        access(url,para,0,success,fail);
    };
    /**
     * 浏览简历
     * @param cid 求职者编号
     * @param eid 企业编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getCurriculum = function (cid,eid,success,fail) {
        var para = {op:"getCurriculum",cid:cid,eid:eid};
        access(url,para,1,success,fail);
    };
    /**
     * 刷新简历
     * @param cid 求职者编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.fresh = function (cid,success,fail) {
        var para = {op:"fresh",cid:cid};
        access(url,para,0,success,fail);
    };
    /**
     * 简历打开/关闭
     * @param cid 求职者编号
     * @param direction true-打开；false-关闭
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.start = function (cid,direction,success,fail) {
        var para = {op:"start",cid:cid,direction:direction};
        access(url,para,1,success,fail);
    };
    /**
     * 添加工作经历
     * @param work 工作经历信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.insertWork = function (work,success,fail) {
        var para = {op:"insertWork",work:JSON.stringify(work)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取教育经历
     * @param cid 求职者编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getEducations = function (cid,success,fail) {
        var para = {op:"getEducations",cid:cid};
        access(url,para,0,success,fail);
    };
    /**
     * 获取工作经历
     * @param cid 求职者编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getWorks = function (cid,success,fail) {
        var para = {op:"getWorks",cid:cid};
        access(url,para,0,success,fail);
    };
    /**
     * 修改教育经历
     * @param education 教育经历信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateEducation = function (education,success,fail) {
        var para = {op:"updateEducation",education:JSON.stringify(education)};
        access(url,para,1,success,fail);
    };
    /**
     * 修改工作经历
     * @param work 工作经历信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateWork = function (work,success,fail) {
        var para = {op:"updateWork",work:JSON.stringify(work)};
        access(url,para,1,success,fail);
    };
    /**
     * 修改技能
     * @param skill 技能信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateSkill = function (skill,success,fail) {
        var para = {op:"updateSkill",skill:JSON.stringify(skill)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除教育经历
     * @param eid 教育经历编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.deleteEducation = function (eid,success,fail) {
        var para = {op:"deleteEducation",eid:eid};
        access(url,para,0,success,fail);
    };
    /**
     * 删除工作经历
     * @param wid 工作经历编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.deleteWork = function (wid,success,fail) {
        var para = {op:"deleteWork",wid:wid};
        access(url,para,0,success,fail);
    };
    /**
     * 添加教育经历
     * @param education 教育经历信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.insertEducation = function (education,success,fail) {
        var para = {op:"insertEducation",education:JSON.stringify(education)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取求职意向
     * @param cid 求职者编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getIntentions = function (cid,success,fail) {
        var para = {op:"getIntentions",cid:cid};
        access(url,para,0,success,fail);
    };
    /**
     * 修改求职意向
     * @param intention 求职意向信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateIntention = function (intention,success,fail) {
        var para = {op:"updateIntention",intention:JSON.stringify(intention)};
        access(url,para,1,success,fail);
    };
    /**
     * 删除指定求职意向
     * @param id 求职意向编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.deleteIntention = function (id,success,fail) {
        var para = {op:"deleteIntention",id:id};
        access(url,para,0,success,fail);
    };
    /**
     * 添加求职意向
     * @param intention 求职意向信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.insertIntention = function (intention,success,fail) {
        var para = {op:"insertIntention",intention:JSON.stringify(intention)};
        access(url,para,1,success,fail);
    };
    /**
     * 修改求职者信息
     * @param candidate 求职者信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateCandidate = function (candidate,success,fail) {
        var para = {op:"updateCandidate",candidate:JSON.stringify(candidate)};
        access(url,para,1,success,fail);
    };
    /**
     * 获取求职者信息
     * @param cid 求职者编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getCandidate = function (cid,success,fail) {
        var para = {op:"getCandidate",cid:cid};
        access(url,para,0,success,fail);
    };
};

//求职相关接口
var InterfaceApp = function () {
    var url = base+"/app";//servlet的url地址

    /**
     * 申请投递
     * @param cid 求职者编号
     * @param jid 职位编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.delivery = function (cid,jid,success,fail) {
        var para = {op:"delivery",cid:cid,jid:jid};
        access(url,para,1,success,fail);
    };

    /**
     * 邀请面试
     * @param interview 面试参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.invitation = function (interview,success,fail) {
        var para = {op:"insertInterview",interview:JSON.stringify(interview)};
        access(url,para,1,success,fail);
    };

    /**
     * 接受/拒绝面试
     * @param id 面试编号
     * @param direction 接受-true;拒绝-false
     * @param reason 拒绝原因
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.accept = function (id,direction,reason,success,fail) {
        var para = {op:"accept",id:id,direction:direction,reason:reason};
        access(url,para,1,success,fail);
    };

    /**
     * 录用/不录用
     * @param id 面试编号
     * @param direction 录用-true;不录用-false
     * @param reason 拒绝或不录用原因
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.employ = function (id,direction,reason,success,fail) {
        var para = {op:"employ",id:id,direction:direction,reason:reason};
        access(url,para,1,success,fail);
    };

    /**
     * 获取求职申请列表
     * @param param  查询参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getApps = function (param,success,fail) {
        var para = {op:"getApps",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };

    /**
     * 获取面试邀请列表
     * @param param  参数待定
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getInvitations = function (param,success,fail) {
        var para = {op:"getInvitations",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };

    /**
     * 批量拒绝简历
     * @param ids 编号集合
     * @param reason
     * @param success
     * @param fail
     */
    this.rejectBatch = function (ids,reason,success,fail) {
        var para = {op:"rejectBatch",ids:ids,reason:reason};
        access(url,para,1,success,fail);
    };

    /**
     * 批量邀请面试
     * @param ids 编号集合
     * @param success
     * @param fail
     */
    this.inviteBatch = function (ids,interview,success,fail) {
        var para = {op:"inviteBatch",ids:ids,interview:JSON.stringify(interview)};
        access(url,para,1,success,fail);
    };
};

var InterfaceCollection = function () {
    var url = base+"/collection";//servlet的url地址

    /**
     * 收藏
     * @param cid 简历编号
     * @param success
     * @param fail
     */
    this.collect = function (cid,success,fail) {
        var para = {op:"collect",cid:cid};
        access(url,para,1,success,fail);
    };

    /**
     * 取消收藏
     * @param id 收藏编号
     * @param success
     * @param fail
     */
    this.cancel = function (id,success,fail) {
        var para = {op:"cancel",id:id};
        access(url,para,1,success,fail);
    };

    /**
     * 批量取消收藏
     * @param ids 收藏编号集合
     * @param success
     * @param fail
     */
    this.cancelBatch = function (ids,success,fail) {
        var para = {op:"cancelBatch",ids:ids};
        access(url,para,1,success,fail);
    };

    /**
     * 获取收藏列表
     * @param param 查询参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getList = function(param,success,fail){
        var para = {op: "getList",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };
};

var InterfaceRoof = function () {
    var url = base+"/roof";//servlet的url地址

    /**
     * 添加置顶
     * @param roof 置顶信息
     * @param success
     * @param fail
     */
    this.insert = function (roof,success,fail) {
        var para = {op:"insert",roof:JSON.stringify(roof)};
        access(url,para,1,success,fail);
    };

    /**
     * 获取置顶置顶位从明日开始被占用的日期集合
     * @param pos 置顶位置
     * @param success
     * @param fail
     */
    this.get = function (pos,success,fail) {
        var para = {op:"get",pos:pos};
        access(url,para,1,success,fail);
    };

    /**
     * 获取置顶价目表
     * @param success
     * @param fail
     */
    this.getPrice = function (success,fail) {
        var para = {op:"getPrice"};
        access(url,para,1,success,fail);
    };

    /**
     * 获取置顶职位列表
     * @param onSuccess
     * @param onFail
     */
    this.getRoofJobs = function(onSuccess,onFail){
        var data = {op:"getNow"};
        access(url,data,"GET",onSuccess,onFail);
    }

    /**
     * 判断指定位置在指定时期内是否存在置顶
     * @param pos 置顶位置
     * @param start 起始时间
     * @param end 结束时间
     * @param success
     * @param fail
     */
    this.exist = function (roof,success,fail) {
        var para = {op:"exist",roof:JSON.stringify(roof)};
        access(url,para,1,success,fail);
    };
};

//求职相关接口
var InterfaceEnterprise = function () {
    var url = base+"/enterprise";//servlet的url地址

    /**
     * 申请投递
     * @param enterprise 企业信息
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.insertEnterprise = function (enterprise,success,fail) {
        var para = {op:"insertEnterprise",enterprise:JSON.stringify(enterprise)};
        access(url,para,1,success,fail);
    };

    /**
     * 邀请面试
     * @param interview 面试参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.updateEnterprise = function (enterprise,success,fail) {
        var para = {op:"updateEnterprise",enterprise:JSON.stringify(enterprise)};
        access(url,para,1,success,fail);
    };

    /**
     * 审核
     * @param id 企业编号
     * @param direction 通过-true;不通过-false
     * @param reason 不通过原因
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.check = function (id,direction,reason,success,fail) {
        var para = {op:"check",id:id,direction:direction,reason:reason};
        access(url,para,1,success,fail);
    };

    /**
     * 获取企业
     * @param id  企业编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getEnterprise = function (id,success,fail) {
        var para = {op:"getEnterprise",id:id};
        access(url,para,0,success,fail);
    };

    /**
     * 获取企业列表
     * @param param  查询参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getEnterpriseList = function (param,success,fail) {
        var para = {op:"getEnterpriseList",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };

    /**
     * 获取推荐企业列表
     * @param param  参数待定
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getRecommends = function (success,fail) {
        var para = {op:"getRecommends"};
        access(url,para,0,success,fail);
    };

    /**
     * 批量拒绝简历
     * @param ids 编号集合
     * @param reason
     * @param success
     * @param fail
     */
    this.insertHr = function (hr,reason,success,fail) {
        var para = {op:"insertHr",hr:JSON.stringify(hr)};
        access(url,para,1,success,fail);
    };

    /**
     * 获取hr
     * @param hid  hr编号
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getHr = function (hid,success,fail) {
        var para = {op:"getHr",hid:hid};
        access(url,para,0,success,fail);
    };



    /**
     * 获取hr列表
     * @param param  查询参数
     * @param success 成功后的回调函数
     * @param fail 失败的回调函数
     */
    this.getHrList = function (param,success,fail) {
        var para = {op:"getHrList",param:JSON.stringify(param)};
        access(url,para,0,success,fail);
    };
};

var iAccount = new InterfaceAccount();
var iJob = new InterfaceJob();
var iCurriculum = new InterfaceCurriculum();
var iApp = new InterfaceApp();
var iCollection = new InterfaceCollection();
var iRoof = new InterfaceRoof();
var iEnterprise = new InterfaceEnterprise();

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
                    layer.msg(data.msg);
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
