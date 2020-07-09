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
//简历字段集合
var columns_notice = [[
    {field:'title', title: '主题'},
    {field:'publisher', title: '发布人',width:100},
    {field:'date', title: '发布时间',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_notice',width:200}
]];

//人才字段集合
var columns_pool = [[
    {field:'ID', title: '身份证号'},
    {field:'name', title: '姓名',width:200},
    {field:'education', title: '学历',width:230},
    {field:'phone', title: '联系电话',width:230},
    {fixed: 'right', title: '操作', toolbar: '#bar_pool',width:300}
]];

//合作客户管理字段集合
var columns_cooperativeclient = [[
    {field:'name', title: '客户名称'},
    {field:'nature', title: '客户性质',width:200},
    {field:'linkman', title: '联系人',width:230},
    {field:'phone', title: '联系电话',width:230},
    {fixed: 'right', title: '操作', toolbar: '#bar_cooperativeclient',width:300}
]];

//劳务派遣客户合同字段集合
var columns_contract = [[
    {field:'client', title: '合作客户'},
    {field:'time', title: '生效时间',width:200},
    {field:'expirationtime', title: '到期时间',width:230},
    {field:'businesstype', title: '业务类型',width:230},
    {fixed: 'right', title: '操作', toolbar: '#bar_contract',width:300}
]];

//内部员工管理字段集合
var columns_internalstaff = [[
    {field:'ID', title: '身份证号'},
    {field:'name', title: '姓名',width:200},
    {field:'education', title: '学历',width:130},
    {field:'phone', title: '联系电话',width:230},
    {field:'department', title: '所属部门',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_internalstaff',width:400}
]];

//外派员工管理字段集合
var columns_expatriate = [[
    {field:'ID', title: '身份证号'},
    {field:'name', title: '姓名',width:200},
    {field:'education', title: '学历',width:130},
    {field:'phone', title: '联系电话',width:230},
    {field:'unit', title: '派遣单位',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_expatriate',width:450}
]];

//员工合同字段集合
var columns_staffcontract  = [[
    {field:'name', title: '姓名'},
    {field:'time', title: '生效时间',width:200},
    {field:'finishtime', title: '到期时间',width:230},
    {field:'number', title: '签订次数',width:230},
    {fixed: 'right', title: '操作', toolbar: '#bar_staffcontract',width:300}
]];

//简历字段集合
var columns_collection = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'name', title: '姓名',width:100},
    {field:'sex', title: '性别',width:100,templet:templet_sex},
    {field:'birthday', title: '出生年月',width:130},
    {field:'degree', title: '学历',width:100,templet:templet_degree},
    {field:'phone', title: '电话',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_collection',width:300}
]];

//简历字段集合
var columns_curriculum_recommend = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'name', title: '姓名',width:100},
    {field:'sex', title: '性别',width:100,templet:templet_sex},
    {field:'birthday', title: '出生年月',width:130},
    {field:'degree', title: '学历',width:100,templet:templet_degree},
    {field:'phone', title: '电话',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_recommend',width:300}
]];

//求职申请字段集合
var columns_app = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'title', title: '投递职位'},
    {field:'name', title: '姓名',width:100},
    {field:'sex', title: '性别',width:100,templet:templet_sex},
    {field:'age', title: '年龄',width:100},
    {field:'degree', title: '学历',width:100,templet:templet_degree},
    {field:'phone', title: '联系电话',width:130},
    {fixed: 'right', title: '操作', toolbar: '#bar_app',width:300}
]];

//职位字段集合
var columns_job = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'title', title: '标题',width:200},
    {field:'category', title: '职位类别',width:200,templet:templet_category},
    {field:'salary', title: '薪资',width:110,templet:templet_salary},
    {field:'recruit', title: '招聘情况',templet:templet_recurit},
    {fixed: 'right', title: '操作', toolbar: '#bar_job',width:340}
]];

//审核职位
var columns_checkjob = [[
    {fixed:'left',type:'checkbox'},
    {field:'enterprise',title:'公司名称'},
    {field:'title', title: '标题'},
    {field:'category', title: '职位类别',width:180,templet:templet_category},
    {field:'salary', title: '薪资',width:110,templet:templet_salary},
    {field:'status', title: '状态',templet:templet_status_job},
    {fixed: 'right', title: '操作', toolbar: '#bar_checkjob',width:350}
]];

//企业字段集合
var columns_enterprise = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'name',title:'名称',width:100},
    {field:'category',title:'企业性质',width:100,templet:templet_category_enterprise},
    {field:'industry',title:'行业',width:100},
    {field:'workers',title:'企业规模',width:100,templet:templet_works},
    {field:'brief',title:'简介',width:100},
    {field:'status',title:'状态',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_enterprise',width:350}
]];

//面试字段集合
var columns_interview = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'candidate', title: '求职者',templet:templet_candidate},
    {field:'job', title: '投递职位',width:180},
    {field:'time', title: '面试时间',width:140,templet:templet_time},
    {field:'address', title: '面试地点',width:160},
    {field:'status', title: '状态',width:110,templet:templet_status_interview},
    {fixed: 'right', title: '操作', toolbar: '#bar_interview',width:280}
]];

//企业推荐字段集合
var columns_recommend = [[
    {field:'start', title: '起始日期',width:140},
    {field:'days', title: '推荐天数',width:100},
    {field:'status', title: '状态',width:100}
]];

//积分获取记录
var columns_income = [[
    {field:'comment', title: '事项'},
    {field:'quantity', title: '获取积分',width:200},
    {field:'date', title: '日期',width:200}
]];

//积分消费记录
var columns_consume = [[
    {field:'comment', title: '事项'},
    {field:'quantity', title: '使用积分',width:200},
    {field:'date', title: '日期',width:200}
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


