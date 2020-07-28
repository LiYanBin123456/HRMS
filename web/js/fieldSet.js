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

//管理员合作客户管理字段集合
var columns_custemer = [[
    {field:'name', title: '客户名称'},
    {field:'contact', title: '联系人',width:250},
    {field:'phone', title: '联系电话',width:250},
    {fixed: 'right', title: '操作', toolbar: '#bar_custemer',width:300}
]];

//医保规则字段集合
var columns_medicalinsurance = [[
    {field:'city', title: '所属城市',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'base', title: '基数',width:80},
    {field:'per1', title: '医疗单位比例',width:105},
    {field:'per2', title: '医疗个人比例',width:105},
    {field:'fin1', title: '大病单位',width:90},
    {field:'fin2', title: '大病个人',width:90},
    {field:'per3', title: '生育单位比例',width:105},
    {fixed: 'right', title: '操作', toolbar: '#bar_medicalinsurance'}
]];

//社保规则字段集合
var columns_socialsecurity = [[
    {field:'city', title: '所属城市',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'base', title: '基数',width:80},
    {field:'per1', title: '养老单位比例',width:105},
    {field:'per2', title: '养老个人比例',width:105},
    {field:'per3', title: '工伤单位比例',width:105},
    {field:'extra', title: '工伤补充',width:95},
    {field:'per4', title: '失业单位比例',width:105},
    {field:'per5', title: '失业个人比例',width:105},
    {fixed: 'right', title: '操作', toolbar: '#bar_socialsecurity'}
]];

//公积金规则字段集合
var columns_accumulation = [[
    {field:'city', title: '所属城市',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'min', title: '基金下限',width:80},
    {field:'max', title: '基金上限',width:80},
    {field:'per1', title: '比例下限',width:90},
    {field:'per2', title: '比例上限',width:90},
    {fixed: 'right', title: '操作', toolbar: '#bar_accumulation'}
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
    {field:'name', title: '姓名'},
    {field:'ID', title: '登录账号',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_accounts',width:200}
]];

//人才字段集合
var columns_pool = [[
    {field:'cardid', title: '身份证号',width:200},
    {field:'name', title: '姓名',width:200},
    {field:'degree', title: '学历',width:230},
    {field:'phone', title: '联系电话',width:230},
    {fixed: 'right', title: '操作', toolbar: '#bar_pool'}
]];

//合作客户管理字段集合
var columns_cooperativeclient = [[
    {field:'name', title: '客户名称'},
    {field:'category', title: '客户性质',width:200},
    {field:'contact', title: '联系人',width:200},
    {field:'phone', title: '联系电话',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_cooperativeclient',width:300}
]];

//供应商管理字段集合
var columns_supplier = [[
    {field:'name', title: '名称'},
    {field:'nature', title: '性质',width:80},
    {field:'business', title: '业务类别',width:90},
    {field:'contact', title: '联系人',width:100},
    {field:'phone', title: '联系电话',width:150},
    {fixed: 'right', title: '操作', toolbar: '#bar_supplier',width:200}
]];

//劳务派遣客户合同字段集合
var columns_contract = [[
    {field:'client', title: '合作客户'},
    {field:'service', title: '服务项目',width:130},
    {field:'time', title: '生效时间',width:130},
    {field:'expirationtime', title: '到期时间',width:130},
    {fixed:'right', title: '操作', toolbar: '#bar_contract',width:200}
]];

//劳务派遣客户合同工资定义字段集合
var columns_wage = [[
    {field:'ID', title: '合作客户'},
    {field:'name', title: '生效时间',width:130},
    {field:'expirationtime', title: '到期时间',width:130},
    {fixed:'right', title: '操作', toolbar: '#bar_wage',width:200}
]];

//内部员工管理字段集合
var columns_internalstaff = [[
    {field:'cardid', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:80},
    {field:'phone', title: '联系电话',width:120},
    {field:'department', title: '所属部门',width:120},
    {title: '操作', toolbar: '#bar_internalstaff'}
]];

//外派员工管理字段集合
var columns_expatriate = [[
    {type:'checkbox'},
    {field:'cardid', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:100},
    {field:'phone', title: '联系电话',width:120},
    {field:'department', title: '派遣单位',width:150},
    { title: '操作', toolbar: '#bar_expatriate'}
]];

//员工合同字段集合
var columns_staffcontract  = [[
    {type:'checkbox'},
    {field:'cardid', title: '身份证号码',width:170},
    {field:'name', title: '姓名',width:100},
    {field:'start', title: '生效时间',width:100},
    {field:'end', title: '到期时间',width:100},
    {field:'times', title: '签订次数',width:80},
    { title: '操作', toolbar: '#bar_staffcontract'}
]];

//普通结算单字段集合
var columns_settlement1  = [[
    {type:'checkbox',width:30},
    {field:'unit', title: '用工企业',width:180},
    {field:'month', title: '月份',width:80},
    {field:'salary', title: '工资',width:70},
    {field:'social', title: '社保',width:70},
    {field:'accumulation', title: '公积金',width:80},
    {field:'manager', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'state', title: '状态',width:70},
    {title:'操作', toolbar: '#bar_settlement'}
]];

//工资管理明细字段集合（完整模式）
var columns_detail_full  = [[
    {field:'ID', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'money', title: '基本工资',width:80},
    {field:'bonus', title: '绩效奖金',width:80},
    {field:'post', title: '岗位补贴',width:80},
    {field:'post', title: '个人社保',width:80},
    {field:'post', title: '单位社保',width:80},
    {field:'post', title: '个人公积金',width:80},
    {field:'post', title: '单位公积金',width:80},
    {field:'post', title: '个税',width:80},
    {field:'traffic', title: '交通补助',width:80},
    {field:'kaoqin', title: '考勤扣款',width:80},
    {field:'kaoqin', title: '应发',width:80},
    {field:'kaoqin', title: '实发',width:80}
]];

//工资管理明细字段集合(录入模式）
var columns_detail_input  = [[
    {field:'ID', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'money', title: '基本工资',width:80,edit: 'text'},
    {field:'bonus', title: '绩效奖金',width:80,edit: 'text'},
    {field:'post', title: '岗位补贴',width:80,edit: 'text'},
    {field:'traffic', title: '交通补助',width:80,edit: 'text'},
    {field:'kaoqin', title: '考勤扣款',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail'}
]];

//工资明细添加字段集合
var columns_staff  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:100},
    {field:'unit', title: '派遣单位',width:200}
]];

//工资明细补录字段集合
var columns_addrecord  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:100},
    {field:'shebao', title: '社保基数',width:100},
    {field:'yibao', title: '医保基数',width:100},
]];

//工资查看日志字段集合
var columns_log  = [[
    {field:'time', title: '日期'},
    {field:'executor', title: '执行人',width:100},
    {field:'content', title: '操作内容',width:200},
]];

//工资结算待审核字段集合
var columns_audit  = [[
    {field:'unit', title: '用工企业'},
    {field:'month', title: '月份',width:100},
    {field:'money', title: '金额',width:100},
    {field:'state', title: '状态',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_audit',width:200}
]];

//工资结算到账确认字段集合
var columns_confirm  = [[
    {field:'unit', title: '用工企业'},
    {field:'money', title: '账户余额',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_confirm',width:200}
]];

//工资结算到账确认明细字段集合
var columns_detail4  = [[
    {field:'time', title: '日期'},
    {field:'money', title: '金额',width:100},
    {field:'matter', title: '事项',width:200},
]];

//小时工结算单字段集合
var columns_settlements2  = [[
    {type:'checkbox'},
    {field:'unit', title: '用工企业',width:180},
    {field:'month', title: '月份',width:80},
    {field:'hours', title: '总工时',width:70},
    {field:'price', title: '单价',width:70},
    {field:'traffic', title: '交通费',width:70},
    {field:'extra', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlements2'}
]];

//小时工工资管理明细字段集合(录入模式)
var columns_detail2_input  = [[
    {field:'cardid', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'hours', title: '工时',width:80,edit: 'text'},
    {field:'food', title: '餐费',width:80,edit: 'text'},
    {field:'traffic', title: '交通费',width:80,edit: 'text'},
    {field:'accommodation', title: '住宿费',width:80,edit: 'text'},
    {field:'utilities', title: '水电费',width:80,edit: 'text'},
    {field:'insurance', title: '保险',width:80,edit: 'text'},
    {field:'other1', title: '其他1',width:80,edit: 'text'},
    {field:'other2', title: '其他2',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail2'}
]];

//小时工工资管理明细字段集合(完整模式)
var columns_detail2_full  = [[
    {field:'cardid', title: '身份证号'},
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

//小时工工资明细添加字段集合
var columns_staff2  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:200},
]];

//商业保险结算单字段集合
var columns_settlements3  = [[
    {type:'checkbox'},
    {field:'unit', title: '用工企业',width:180},
    {field:'month', title: '月份',width:80},
    {field:'product', title: '保险产品',width:120},
    {field:'premium', title: '保费',width:70},
    {field:'state', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlements3'}
]];

//商业保险工资管理明细字段集合
var columns_detail3  = [[
    {field:'ID', title: '身份证号',width:160},
    {field:'name', title: '姓名',width:80},
    {field:'site', title: '工作地点',width:80},
    {field:'post', title: '工作岗位',width:80},
    {field:'product', title: '保险产品',width:100,edit: 'text'},
    {field:'premium', title: '保费',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail3'}
]];

//商业保险工资明细添加字段集合
var columns_staff3 = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:200},
]];

//导出工资发放表普通字段集合
var columns_common  = [[
    {field:'unit', title: '用工企业',width:180},
    {field:'month', title: '月份',width:80},
    {field:'salary', title: '工资',width:70},
    {field:'social', title: '社保',width:70},
    {field:'accumulation', title: '公积金',width:80},
    {field:'manager', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'state', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_common'}
]];

//导出工资发放表小时工字段集合
var columns_hourly  = [[
    {field:'unit', title: '用工企业',width1:180},
    {field:'month', title: '月份',width:80},
    {field:'time', title: '总工时',width:70},
    {field:'price', title: '单价',width:70},
    {field:'tax', title: '交通费',width:70},
    {field:'add', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'state', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_hourly'}
]];

//专项扣除字段集合
var columns_special  = [[
    {field:'ID', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'children', title: '子女教育',width:80},
    {field:'oldman', title: '赡养老人',width:80},
    {field:'continue', title: '继续教育',width:80},
    {field:'medical', title: '大病医疗',width:80},
    {field:'interest', title: '住房贷款利息',width:110},
    {field:'rent', title: '住房租金',width:80},
    {title: '操作', toolbar: '#bar_special'}
]];

//社保参保字段集合
var columns_socialsc  = [[
    {field:'cardid', title: '身份证号',width:170},
    {field:'code', title: '个人代码',width:80},
    {field:'name', title: '姓名',width:80},
    {field:'start', title: '参保时间',width:80},
    {field:'money', title: '月缴费工资',width:100},
    {field:'time', title: '工作时间',width:100},
    {field:'status', title: '参保状态',width:80},
    {fixed: 'right', title: '操作', toolbar: '#bar_socialsc'}
]];

//社保参保添加字段集合
var columns_socialscadd  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:200},
]];

//医保参保字段集合
var columns_medical  = [[
    {field:'ID', title: '身份证号',width:170},
    {field:'personal', title: '个人代码',width:80},
    {field:'name', title: '姓名',width:80},
    {field:'insured', title: '参保时间',width:80},
    {field:'wage', title: '月缴费工资',width:100},
    {field:'time', title: '工作时间',width:100},
    {field:'state', title: '参保状态',width:80},
    {fixed: 'right', title: '操作', toolbar: '#bar_medical'}
]];

//医保参保添加字段集合
var columns_medicaladd  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:200},
]];

//公积金参保字段集合
var columns_accumulations  = [[
    {field:'ID', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'insured', title: '起缴时间',width:80},
    {field:'wage', title: '工资基数',width:100},
    {field:'state', title: '参保状态',width:80},
    {fixed: 'right', title: '操作', toolbar: '#bar_accumulation'}
]];

//公积金参保添加字段集合
var columns_accumulationadd  = [[
    {type:'checkbox'},
    {field:'ID', title: '身份证号',width:400},
    {field:'name', title: '姓名',width:200},
]];

//保险产品字段集合
var columns_insurance = [[
    {field:'name', title: '产品名称',width:180},
    {field:'fin1', title: '产品保额',width:80},
    {field:'fin2', title: '医疗保额',width:80},
    {field:'allowance', title: '住院津贴',width:80},
    {field:'period', title: '保障时段',width:100},
    {field:'allow', title: '可参保人员',width:130},
    {field:'min', title: '年龄下限',width:100},
    {field:'max', title: '年龄上限',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_insurance'}
]];

//账号管理字段集合
var columns_account  = [[
    {field:'name', title: '姓名'},
    {field:'ID', title: '登录账号',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_account',width:200}
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

//面向合作普通结算单字段集合
var columns_settlement12  = [[
    {type:'checkbox',width:30},
    {field:'month', title: '月份',width:80},
    {field:'salary', title: '工资',width:70},
    {field:'social', title: '社保',width:70},
    {field:'accumulation', title: '公积金',width:80},
    {field:'manager', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'state', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlement'}
]];

//面向合作小时工结算单字段集合
var columns_settlements22  = [[
    {type:'checkbox',width:30},
    {field:'month', title: '月份',width:80},
    {field:'time', title: '总工时',width:70},
    {field:'price', title: '单价',width:70},
    {field:'tax', title: '交通费',width:70},
    {field:'add', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'state', title: '状态',width:70},
    {title: '操作', toolbar: '#bar_settlements2'}
]];

//面向合作工资结算待审核字段集合
var columns_audit2  = [[
    {field:'month', title: '月份'},
    {field:'money', title: '金额',width:100},
    {field:'state', title: '状态',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_audit2',width:200}
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


