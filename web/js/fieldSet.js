/**
 * 定义字段集合、字段格式化和字段设置相关数据和函数
 *
 */

//----------------------------------------------------------------数据集合-----------------------

var items_welfare = [
    {value:1,text:"五险一金"},
    {value:2,text:"包住"},
    {value:4,text:"包吃"},
    {value:8,text:"周末双休"},
    {value:16,text:"交通补助"},
    {value:32,text:"加班补助"},
    {value:64,text:"餐补"},
    {value:128,text:"话补"},
    {value:256,text:"房补"}
];

var items_degree = [
    {value:0,text:"高中以下"},
    {value:1,text:"高中"},
    {value:2,text:"中专"},
    {value:3,text:"大专"},
    {value:4,text:"本科"},
    {value:5,text:"硕士"},
    {value:6,text:"博士"}
];

var items_degree_limit = [{value:-1,text:"不限"}].concat(items_degree);

var states_app = [
    {value:0,text:"已离职，需求新工作"},
    {value:1,text:"在职，考虑更好职位"},
    {value:2,text:"其他"}
];

var status_interview = [
    {value:0,text:"邀请中"},
    {value:1,text:"面试中"},
    {value:2,text:"已拒绝"},
    {value:3,text:"已录用"},
    {value:4,text:"不录用"}
];
var status_job = [
    {value:0,text:"待修改"},
    {value:1,text:"待审核"},
    {value:2,text:"招聘中"},
    {value:3,text:"已下架"},
    {value:4,text:"已删除"}
];
var categorys_enterprise = [
    {value:0,text:"外资企业"},
    {value:1,text:"民营企业"},
    {value:2,text:"股份制企业"},
    {value:3,text:"集体企业"},
    {value:4,text:"上市公司"},
    {value:5,text:"国家机关"},
    {value:6,text:"事业单位"},
    {value:7,text:"其它"}
];
var works_enterprise = [
    {value:0,text:"10人以下"},
    {value:1,text:"10-50人"},
    {value:2,text:"50-200人"},
    {value:3,text:"200-500人"},
    {value:4,text:"500-1000人"},
    {value:5,text:"1000人以上"}
]

//----------------------------------------------------------------字段集合-----------------------

//派遣单位字段集合
var columns_dispatch = [[
    {field:'name', title: '客户名称'},
    {field:'contact', title: '联系人',width:250},
    {field:'phone', title: '联系电话',width:250},
    {fixed: 'right', title: '操作', toolbar: '#bar_dispatch',width:300}
]];

//医保规则字段集合
var columns_medicare = [[
    {field:'city', title: '所属城市',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'base', title: '基数',width:80},
    {field:'per1', title: '医疗单位比例',width:105},
    {field:'per2', title: '医疗个人比例',width:105},
    {field:'fin1', title: '大病单位',width:90},
    {field:'fin2', title: '大病个人',width:90},
    {field:'per3', title: '生育单位比例',width:105},
    {fixed: 'right', title: '操作', toolbar: '#bar_medicare'}
]];

//社保规则字段集合
var columns_social = [[
    {field:'city', title: '所属城市',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'base', title: '基数',width:80},
    {field:'per1', title: '养老单位比例',width:105},
    {field:'per2', title: '养老个人比例',width:105},
    {field:'per3', title: '工伤单位比例',width:105},
    {field:'extra', title: '工伤补充',width:95},
    {field:'per4', title: '失业单位比例',width:105},
    {field:'per5', title: '失业个人比例',width:105},
    {fixed: 'right', title: '操作', toolbar: '#bar_social'}
]];

//公积金规则字段集合
var columns_fund = [[
    {field:'city', title: '所属城市',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'min', title: '基金下限',width:80},
    {field:'max', title: '基金上限',width:80},
    {field:'per1', title: '比例下限',width:90},
    {field:'per2', title: '比例上限',width:90},
    {fixed: 'right', title: '操作', toolbar: '#bar_fund'}
]];

//公告字段集合
var columns_notice = [[
    {field:'title', title: '主题'},
    {field:'publisher', title: '发布人',width:100},
    {field:'date', title: '发布时间',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_notice',width:200}
]];

//账号管理字段集合
var columns_accounts  = [[
    {field:'nickname', title: '姓名',width:200},
    {field:'username', title: '登录账号',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_accounts'}
]];

//合作客户管理字段集合
var columns_cooperation = [[
    {field:'name', title: '客户名称'},
    {field:'category', title: '客户性质',width:200},
    {field:'contact', title: '联系人',width:200},
    {field:'phone', title: '联系电话',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_cooperation',width:300}
]];

//供应商管理字段集合
var columns_supplier = [[
    {field:'name', title: '名称'},
    {field:'business', title: '业务类别',width:90},
    {field:'contact', title: '联系人',width:100},
    {field:'phone', title: '联系电话',width:150},
    {fixed: 'right', title: '操作', toolbar: '#bar_supplier',width:200}
]];

//劳务派遣客户合同字段集合
var columns_contract = [[
    {field:'name', title: '合作客户'},
    {field:'service', title: '服务项目',width:130},
    {field:'start', title: '生效时间',width:130},
    {field:'end', title: '到期时间',width:130},
    {fixed:'right', title: '操作', toolbar: '#bar_contract',width:200}
]];

//内部员工管理字段集合
var columns_employee_internal = [[
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:80},
    {field:'phone', title: '联系电话',width:120},
    {field:'department', title: '所属部门'},
    {title: '操作', toolbar: '#bar_employee',width:360,fixed:"right"}
]];

//外派员工管理字段集合
var columns_employee_expatriate = [[
    {type:'checkbox'},
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:100},
    {field:'phone', title: '联系电话',width:120},
    {field:'department', title: '派遣单位'},
    { title: '操作', toolbar: '#bar_employee',width:400,fixed:"right"}
]];

//人才库字段集合
var columns_employee_spare = [[
    {field:'cardId', title: '身份证号',width:200},
    {field:'name', title: '姓名',width:200},
    {field:'degree', title: '学历',width:230},
    {field:'phone', title: '联系电话'},
    {field: 'right', title: '操作', toolbar: '#bar_spare',width:200,fixed:"right"}
]];

//员工合同字段集合
var columns_contract_employee  = [[
    {field:'cardId', title: '身份证号码',width:170},
    {field:'name', title: '姓名',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'end', title: '到期时间',width:100},
    {field:'times', title: '签订次数',width:80},
    { title: '操作', toolbar: '#bar_contract'}
]];

//普通结算单字段集合
var columns_settlement1  = [[
    {type:'checkbox',fixed:"left"},
    {field:'client', title: '用工企业',width:180,fixed:"left"},
    {field:'month', title: '月份',width:80},
    {field:'salary', title: '工资',width:70},
    {field:'social', title: '社保',width:70},
    {field:'fund', title: '公积金',width:80},
    {field:'manage', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70},
    {title:'操作', toolbar: '#bar_settlement',width:260,fixed:"right"}
]];

//面向合作普通结算单字段集合
var columns_settlement10  = [[
    {type:'checkbox',width:30},
    {field:'month', title: '月份',width:80,fixed:"left"},
    {field:'salary', title: '工资',width:70},
    {field:'social', title: '社保',width:70},
    {field:'fund', title: '公积金',width:80},
    {field:'manage', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlement',width:260,fixed:"right"}
]];

//小时工结算单字段集合
var columns_settlement2  = [[
    {type:'checkbox',fixed:"left"},
    {field:'client', title: '用工企业',width:180,fixed:"left"},
    {field:'month', title: '月份',width:80},
    {field:'hours', title: '总工时',width:70},
    {field:'price', title: '单价',width:60},
    {field:'traffic', title: '交通费',width:70},
    {field:'extra', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlement',width:260,fixed:"right"}
]];

//面向合作小时工结算单字段集合
var columns_settlement20  = [[
    {type:'checkbox',width:30,fixed:"left"},
    {field:'month', title: '月份',width:80,fixed:"left"},
    {field:'time', title: '总工时',width:70},
    {field:'price', title: '单价',width:70},
    {field:'traffic', title: '交通费',width:70},
    {field:'extra', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlement',width:260,fixed:"right"}
]];

//商业保险结算单字段集合
var columns_settlement3  = [[
    {type:'checkbox',fixed:"left"},
    {field:'client', title: '用工企业',fixed:"left"},
    {field:'month', title: '月份',width:100},
    {field:'pname', title: '保险产品',width:180},
    {field:'price', title: '保费',width:100},
    {field:'status', title: '状态',width:100},
    {title: '操作', toolbar: '#bar_settlement',width:260,fixed:"right"}
]];

//面向合作商业保险结算单字段集合
var columns_settlement30  = [[
    {type:'checkbox',fixed:"left"},
    {field:'month', title: '月份',width:100,fixed:"left"},
    {field:'pname', title: '保险产品'},
    {field:'price', title: '保费',width:100},
    {field:'status', title: '状态',width:100},
    {title: '操作', toolbar: '#bar_settlement',width:260,fixed:"right"}
]];

//工资管理明细字段集合（完整模式）
var columns_detail1_full  = [[
    {field:'cardId', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'base', title: '基本工资',width:80},
    {field:'reward', title: '绩效奖金',width:80},
    {field:'allowance', title: '岗位补贴',width:80},
    {field:'social1', title: '个人社保',width:80},
    {field:'social2', title: '单位社保',width:80},
    {field:'fund1', title: '个人公积金',width:80},
    {field:'fund2', title: '单位公积金',width:80},
    {field:'tax', title: '个税',width:80},
    {field:'f1', title: '交通补助',width:80},
    {field:'f2', title: '考勤扣款',width:80},
    {field:'payable', title: '应发',width:80},
    {field:'paid', title: '实发',width:80}
]];

//工资管理明细字段集合(录入模式）
var columns_detail1_input  = [[
    {field:'cardId', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'base', title: '基本工资',width:80,edit: 'text'},
    {field:'reward', title: '绩效奖金',width:80,edit: 'text'},
    {field:'allowance', title: '岗位补贴',width:80,edit: 'text'},
    {field:'f1', title: '交通补助',width:80,edit: 'text'},
    {field:'f2', title: '考勤扣款',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail'}
]];

//小时工工资管理明细字段集合(录入模式)
var columns_detail2_input  = [[
    {field:'cardId', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'hours', title: '工时',width:80,edit: 'text'},
    {field:'food', title: '餐费',width:80,edit: 'text'},
    {field:'traffic', title: '交通费',width:80,edit: 'text'},
    {field:'accommodation', title: '住宿费',width:80,edit: 'text'},
    {field:'utilities', title: '水电费',width:80,edit: 'text'},
    {field:'insurance', title: '保险',width:80,edit: 'text'},
    {field:'other1', title: '其他1',width:80,edit: 'text'},
    {field:'other2', title: '其他2',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail'}
]];

//小时工工资管理明细字段集合(完整模式)
var columns_detail2_full  = [[
    {field:'cardId', title: '身份证号'},
    {field:'name', title: '姓名',width:80},
    {field:'hours', title: '工时',width:80},
    {field:'price', title: '单价',width:80},
    {field:'food', title: '餐费',width:80},
    {field:'traffic', title: '交通费',width:80},
    {field:'accommodation', title: '住宿费',width:80},
    {field:'utilities', title: '水电费',width:80},
    {field:'insurance', title: '保险',width:80},
    {field:'tax', title: '个税',width:80},
    {field:'other1', title: '其他1',width:80},
    {field:'other2', title: '其他2',width:80},
    {field:'payable', title: '应付金额',width:80},
    {field:'paid', title: '实发金额',width:80},
]];

//商业保险工资管理明细字段集合
var columns_detail3  = [[
    {field:'cardId', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'place', title: '工作地点',width:80},
    {field:'post', title: '工作岗位',width:80},
    {field:'pname', title: '保险产品',width:100,edit: 'text'},
    {field:'price', title: '保费',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail'}
]];

//资金明细字段集合
var columns_detail4  = [[
    {field:'time', title: '日期'},
    {field:'money', title: '金额',width:100},
    {field:'matter', title: '事项',width:200},
]];

//工资明细添加字段集合
var columns_staff_with_base  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:200},
    {field:'name', title: '姓名',width:100},
    {field:'Medicare', title: '医保基数',width:200,edit: 'number'},
    {field:'social', title: '社保基数',width:200,edit: 'number'}
]];

//工资结算到账确认字段集合
var columns_confirm  = [[
    {field:'unit', title: '用工企业'},
    {field:'money', title: '账户余额',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_confirm',width:200}
]];

//专项扣除字段集合
var columns_deduct  = [[
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'deduct1', title: '子女教育',width:80},
    {field:'deduct2', title: '赡养老人',width:80},
    {field:'deduct3', title: '继续教育',width:80},
    {field:'deduct4', title: '大病医疗',width:80},
    {field:'deduct5', title: '住房贷款利息',width:110},
    {field:'deduct6', title: '住房租金',width:80},
    {title: '操作', toolbar: '#bar_deduct'}
]];

//个税申报字段集合
var columns_tax  = [[
    {field:'cardId', title: '身份证号',width:160,fixed:"left"},
    {field:'name', title: '姓名',width:80,fixed:"left"},
    {field:'payable', title: '本期收入',width:80},
    {field:'pension', title: '基本养老',width:80},
    {field:'medicare', title: '基本医疗',width:80},
    {field:'unemployment', title: '失业险',width:70},
    {field:'fund', title: '公积金',width:70},
    {field:'deduct1', title: '子女教育',width:80},
    {field:'deduct2', title: '赡养老人',width:80},
    {field:'deduct3', title: '继续教育',width:80},
    {field:'deduct4', title: '大病医疗',width:80},
    {field:'deduct5', title: '房贷利息',width:80},
    {field:'deduct6', title: '住房租金',width:80}
]];

//医/社保参保字段集合
var columns_insured1  = [[
    {field:'cardId', title: '身份证号',width:170},
    {field:'code', title: '个人代码',width:80},
    {field:'name', title: '姓名',width:80},
    {field:'start', title: '参保时间',width:80},
    {field:'money', title: '月缴费工资',width:100},
    {field:'entry', title: '工作时间',width:100},
    {field:'status', title: '参保状态',width:80},
    {fixed: 'right', title: '操作', toolbar: '#bar_insured'}
]];

//公积金参保字段集合
var columns_insured2  = [[
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'start', title: '起缴时间',width:80},
    {field:'money', title: '工资基数',width:100},
    {field:'status', title: '参保状态',width:80},
    {fixed: 'right', title: '操作', toolbar: '#bar_insured'}
]];

//公积金参保添加字段集合
var columns_member  = [[
    {type:'checkbox'},
    {field:'cardId', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:200},
]];

//保险产品字段集合
var columns_product = [[
    {field:'pname', title: '产品名称',width:180},
    {field:'fin1', title: '产品保额',width:80},
    {field:'fin2', title: '医疗保额',width:80},
    {field:'allowance', title: '住院津贴',width:80},
    {field:'period', title: '保障时段',width:100},
    {field:'allow', title: '可参保人员',width:130},
    {field:'min', title: '年龄下限',width:100},
    {field:'max', title: '年龄上限',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_product'}
]];

//账号管理字段集合
var columns_account  = [[
    {field:'nickname', title: '姓名',width:200},
    {field:'username', title: '登录账号',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_account'}
]];


//面向合作员工管理字段集合
var columns_expatriate2 = [[
    {type:'checkbox',width:30},
    {field:'ID', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'education', title: '学历',width:100},
    {field:'phone', title: '联系电话',width:120},
    {title: '操作', toolbar: '#bar_expatriate'}
]];

//面向合作账号管理字段集合
var columns_account2  = [[
    {field:'name', title: '姓名'},
    {field:'ID', title: '登录账号',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_account',width:200}
]];


//----------------------------------------------------------------字段格式化-----------------------
function templet_candidate(d) {
    return d.name+"/"+d.phone;
}

function templet_category_enterprise(d) {
    return format_category_enterprise(d.category);
}

function templet_works(d) {
    return format_works(d.works);
}
function templet_modtime(d) {
    return format_dateTime(d.modtime)
}
function templet_time(d) {
    return format_dateTime(d.time)
}
function templet_salary(d) {
    return "¥"+d.salary1+"-"+d.salary2;
}
function templet_status_job(d) {
    return array_value2text(status_job,d.status);
}
function templet_status_interview(d) {
    return array_value2text(status_interview,d.status);
}

function templet_sex(d) {
    return format_sex(d.sex);
}

function templet_degree(d){
    return format_degree(d.degree);
}

function templet_category(d) {
    return format_category(d.category);
}

function templet_recurit(d){
    return "收藏:"+d.favorites+",投简历:"+d.submit+",录用:"+d.pass;
}
function format_sex(value) {
    return value==0?"男":"女";
}
function format_degree(value){
    return array_value2text(items_degree_limit,value);
}
function format_state(value){
    return array_value2text(states_app,value);
}
function format_category_enterprise(value){
    return array_value2text(categorys_enterprise,value);
}
function format_works(value){
    return array_value2text(works_enterprise,value);
}
function format_welfare(value,extra){
    var res = [];
    for(var i in items_welfare){
        var w = items_welfare[i];
        if((value & w.value) != 0){
            res.push(w.text);
        }
    }
    if((value & 1)!=0){
        res.push(extra);
    }

    return res;
}

/**
 * 在数组中根据指定值转换为文本
 * @param {array} arr 数组
 * @param {*} value 指定值
 */
function array_value2text(arr,value){
    for(var i in arr){
        var obj = arr[i];
        if(obj.value == value){
            return obj.text;
        }
    }
    return "";
}

function format_date(timestamp) {
    var d = new Date(timestamp);
    var year=d.getFullYear();
    var month=d.getMonth()+1;
    var date=d.getDate();
    return year+"-"+month+"-"+date;
}

function format_dateTime(timestamp) {
    var time = new Date(timestamp);
    var year=time.getFullYear();
    var month=time.getMonth()+1;
    var date=time.getDate();
    var hour=time.getHours();
    var minute=time.getMinutes();
    return year+"-"+month+"-"+date+" "+hour+":"+minute;
}





//----------------------------------------------------------------字段设置相关-----------------------


/**
 * 根据字段值提取相应的字段
 * @param columns 所有的字段
 * @param value 字段值，相应的二进制位为1表示有该字段
 * @returns {[field]} 字段集合，形如[[field1,field2……]]。
 */
function getColumns(columns, value) {
    var result = [];
    for (var i in columns){
        if((value & Math.pow(2,i))!=0){
            result.push(columns[i]);
        }
    }
    return [result];
}


