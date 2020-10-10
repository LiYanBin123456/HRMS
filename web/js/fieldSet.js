/**
 * 定义字段集合、字段格式化和字段设置相关数据和函数
 *
 */

//----------------------------------------------------------------字段集合-----------------------

//派遣单位合作客户字段集合
var columns_dispatch_partner = [[
    {field:'name', title: '客户名称'},
    {field:'contact', title: '联系人',width:250},
    {field:'phone', title: '联系电话',width:250},
    {fixed: 'right', title: '操作', toolbar: '#bar_dispatch',width:300}
]];

//派遣单位潜在客户字段集合
var columns_dispatch_potential = [[
    {field:'name', title: '客户名称'},
    {field:'contact', title: '联系人',width:250},
    {field:'phone', title: '联系电话',width:250},
    {fixed: 'right', title: '操作', toolbar: '#bar_potential',width:300}
]];

//派遣单位流失客户字段集合
var columns_dispatch_loss = [[
    {field:'name', title: '客户名称'},
    {field:'contact', title: '联系人',width:250},
    {field:'phone', title: '联系电话',width:250},
    {fixed: 'right', title: '操作', toolbar: '#bar_loss',width:300}
]];

//医保规则字段集合
var columns_medicare = [[
    {field:'city', title: '所属城市',width:130,templet:function (d) {return getCityText2(d.city)}},
    {field:'start', title: '生效时间',width:90,templet:function (d) {return format_date(d.start)}},
    {field:'base', title: '基数(元)',width:80},
    {field:'per1', title: '单位医疗(%)',width:100,templet:function (d) {return format_percent(d.per1)}},
    {field:'per2', title: '个人医疗(%)',width:100,templet:function (d) {return format_percent(d.per2)}},
    {field:'fin1', title: '单位大病(元)',width:100},
    {field:'fin2', title: '个人大病(元)',width:105},
    {field:'per3', title: '单位生育(%)',width:100,templet:function (d) {return format_percent(d.per3)}},
    {fixed: 'right', title: '操作', toolbar: '#bar_medicare'}
]];

//社保规则字段集合
var columns_social = [[
    {field:'city', title: '所属城市',width:130,templet:function (d) {return getCityText2(d.city)}},
    {field:'start', title: '生效时间',width:90,templet:function (d) {return format_date(d.start)}},
    {field:'base', title: '基数(元)',width:80},
    {field:'per1', title: '单位养老(%)',width:100,templet:function (d) {return format_percent(d.per1)}},
    {field:'per2', title: '个人养老(%)',width:100,templet:function (d) {return format_percent(d.per2)}},
    {field:'per3', title: '单位工伤(%)',width:100,templet:function (d) {return format_percent(d.per3)}},
    {field:'extra', title: '工伤补充(元)',width:105},
    {field:'per4', title: '单位失业(%)',width:100,templet:function (d) {return format_percent(d.per4)}},
    {field:'per5', title: '个人失业(%)',width:100,templet:function (d) {return format_percent(d.per5)}},
    {fixed: 'right', title: '操作', toolbar: '#bar_social'}
]];

//公积金规则字段集合
var columns_fund = [[
    {field:'city', title: '所属城市',width:130,templet:function (d) {return getCityText2(d.city)}},
    {field:'start', title: '生效时间',width:100,templet:function (d) {return format_date(d.start)}},
    {field:'min', title: '基金下限',width:80},
    {field:'max', title: '基金上限',width:80},
    {field:'per1', title: '比例下限',width:90,templet:function (d) {return format_percent(d.per1)}},
    {field:'per2', title: '比例上限',width:90,templet:function (d) {return format_percent(d.per2)}},
    {fixed: 'right', title: '操作', toolbar: '#bar_fund'}
]];

//公告字段集合
var columns_notice = [[
    {field:'title', title: '主题'},
    {field:'publisher', title: '发布人',width:100},
    {field:'date', title: '发布时间',width:130,templet:function (d) {return format_date(d.date)}},
    {fixed: 'right', title: '操作', toolbar: '#bar_notice',width:200}
]];

//账号管理字段集合
var columns_accounts  = [[
    {field:'nickname', title: '姓名',width:200},
    {field:'username', title: '登录账号',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_accounts'}
]];

//合作单位合作客户管理字段集合
var columns_cooperation_partner = [[
    {field:'name', title: '客户名称'},
    {field:'category', title: '客户性质',width:200,templet:function (d) { return array_value2text(categorys_cooperation,d.category) }},
    {field:'contact', title: '联系人',width:200},
    {field:'phone', title: '联系电话',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_cooperation',width:300}
]];

//合作单位潜在客户管理字段集合
var columns_cooperation_potential = [[
    {field:'name', title: '客户名称'},
    {field:'category', title: '客户性质',width:200,templet:function (d) { return array_value2text(categorys_cooperation,d.category) }},
    {field:'contact', title: '联系人',width:200},
    {field:'phone', title: '联系电话',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_potential',width:300}
]];

//合作单位流失客户管理字段集合
var columns_cooperation_loss = [[
    {field:'name', title: '客户名称'},
    {field:'category', title: '客户性质',width:200,templet:function (d) { return array_value2text(categorys_cooperation,d.category) }},
    {field:'contact', title: '联系人',width:200},
    {field:'phone', title: '联系电话',width:200},
    {fixed: 'right', title: '操作', toolbar: '#bar_loss',width:300}
]];

//供应商管理字段集合
var columns_supplier = [[
    {field:'name', title: '名称'},
    {field:'business', title: '业务类别',width:90,templet:function (d) { return array_value2text(business_supplier,d.business) }},
    {field:'contact', title: '联系人',width:100},
    {field:'phone', title: '联系电话',width:150},
    {fixed: 'right', title: '操作', toolbar: '#bar_supplier',width:200}
]];

//派遣方客户合同字段集合
var columns_contract_dispatch = [[
    {field:'name', title: '合作客户'},
    {field:'start', title: '生效时间',width:130,templet:function (d) {return format_date(d.start)}},
    {field:'end', title: '到期时间',width:130,templet:function (d) {return format_date(d.end)}},
    {field:'comments', title: '备注'},
    {fixed:'right', title: '操作', toolbar: '#bar_contract',width:200}
]];

//合作单位客户合同字段集合
var columns_contract_cooperation = [[
    {field:'name', title: '合作客户'},
    {field:'start', title: '生效时间',width:130,templet:function (d) {return format_date(d.start)}},
    {field:'end', title: '到期时间',width:130,templet:function (d) {return format_date(d.end)}},
    {fixed:'right', title: '操作', toolbar: '#bar_contract',width:200}
]];

//员工合同字段集合
var columns_contract_employee  = [[
    {field:'cardId', title: '身份证号码',width:170},
    {field:'name', title: '姓名',width:100},
    {field:'start', title: '生效时间',width:100,templet:function (d) {return format_date(d.start)}},
    {field:'end', title: '到期时间',width:100,templet:function (d) {return format_date(d.end)}},
    {field:'times', title: '签订次数',width:80},
    { title: '操作', toolbar: '#bar_contract'}
]];

//内部员工管理字段集合
var columns_employee_internal = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:80,templet:function (d) { return array_value2text(degrees_employee,d.degree) }},
    {field:'phone', title: '联系电话',width:120},
    {field:'department', title: '所属部门'},
    {title: '操作', toolbar: '#bar_employee',width:400,fixed:"right"}
]];

//外派员工管理字段集合
var columns_employee_expatriate = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:100,templet:function (d) { return array_value2text(degrees_employee,d.degree) }},
    {field:'phone', title: '联系电话',width:120},
    {field:'cname', title: '派遣单位'},
    { title: '操作', toolbar: '#bar_employee',width:400,fixed:"right"}
]];


//人才库字段集合
var columns_employee_spare = [[
    {field:'cardId', title: '身份证号',width:200},
    {field:'name', title: '姓名',width:200},
    {field:'degree', title: '学历',width:230,templet:function (d) { return array_value2text(degrees_employee,d.degree) }},
    {field:'phone', title: '联系电话'},
    {field: 'right', title: '操作', toolbar: '#bar_spare',width:200,fixed:"right"}
]];

//员工所有信息字段集合
var columns_employees = [[
    {fixed: 'left', type: 'checkbox'},
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'degree', title: '学历',width:80,templet:function (d) { return array_value2text(degrees_employee,d.degree) }},
    {field:'phone', title: '联系电话',width:120},
    {field:'type', title: '员工类型',width:120,templet:function (d) { return array_value2text(type_employee,d.type) }},
    {field:'entry', title: '入职时间',width:120,templet:function (d) {return format_date(d.entry)}},
    {field:'status', title: '在职状态',width:120,templet:function (d) { return array_value2text(status_employee,d.status) }},
    {field:'department', title: '部门',width:120},
    {field:'post', title: '岗位',width:120},
    {field:'category', title: '用工性质',width:120,templet:function (d) { return array_value2text(category_employee,d.category) }},
    {field:'price', title: '时间报酬',width:120},
    {field:'school', title: '毕业院校',width:120},
    {field:'major', title: '毕业专业',width:120},
    {field:'cname', title: '外派单位',width:120},
    {field:'household', title: '户口性质',width:120,templet:function (d) { return array_value2text(household_employee,d.household) }},
    {field:'address', title: '户籍地址',width:120},
    {field:'date1', title: '离职时间',width:120,templet:function (d) {return format_date(d.date1)}},
    {field:'date2', title: '退休时间',width:120,templet:function (d) {return format_date(d.date2)}},
    {field:'reason', title: '离职原因',width:120,templet:function (d) { return array_value2text(reason_employee,d.reason) }},
]];

//普通结算单字段集合
var columns_settlement1  = [[
    {field:'name', title: '用工企业',width:180,fixed:"left"},
    {field:'month', title: '月份',width:90,templet:function (d) {return format_date(d.month)}},
    {field:'salary', title: '应发工资',width:70},
    {field:'social', title: '单位社保',width:70},
    {field:'medicare', title: '单位医保',width:70},
    {field:'fund', title: '单位公积金',width:80},
    {field:'manage', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70,templet:function (d) { return array_value2text(status_settlement,d.status) }},
    {title:'操作', toolbar: '#bar_settlement',width:500,fixed:"right"}
]];
//添加结算单中的合同字段集合
var columns_contract =[[
    {field:'id', title: '合同id',width:160},
    {field:'name', title: '合同名称',width:200},
    {field:'invoice', title: '发票类型',width:120,templet:function (d) { return array_value2text(invoice_contract,d.invoice) }},
    {field:'stype', title: '服务项目',width:120,templet:function (d) { return array_value2text(stype_contract,d.stype) }},
    {field:'comment', title: '备注'}
]];

//面向合作普通结算单字段集合
var columns_settlement10  = [[
    {type:'checkbox',width:30},
    {field:'month', title: '月份',width:90,fixed:"left",templet:function (d) {return format_date(d.month)}},
    {field:'salary', title: '工资',width:70},
    {field:'social', title: '社保',width:70},
    {field:'fund', title: '公积金',width:80},
    {field:'manage', title: '管理费',width:70},
    {field:'tax', title: '税费',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70,templet:function (d) { return array_value2text(status_settlement,d.status) }},
    {title: '操作', toolbar: '#bar_settlement',width:500,fixed:"right"}
]];

//小时工结算单字段集合
var columns_settlement2  = [[
    {field:'name', title: '用工企业',width:180,fixed:"left"},
    {field:'month', title: '月份',width:90,templet:function (d) {return format_date(d.month)}},
    {field:'hours', title: '总工时',width:70},
    {field:'price', title: '单价',width:60,edit: 'text'},
    {field:'traffic', title: '交通费',width:70},
    {field:'extra', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70,templet:function (d) { return array_value2text(status_settlement,d.status) }},
    {title: '操作', toolbar: '#bar_settlement',width:500,fixed:"right"}
]];

//面向合作小时工结算单字段集合
var columns_settlement20  = [[
    {field:'month', title: '月份',width:90,fixed:"left",templet:function (d) {return format_date(d.month)}},
    {field:'time', title: '总工时',width:70},
    {field:'price', title: '单价',width:70,edit: 'text'},
    {field:'traffic', title: '交通费',width:70},
    {field:'extra', title: '附加',width:70},
    {field:'summary', title: '总额',width:80},
    {field:'status', title: '状态',width:70,templet:function (d) { return array_value2text(status_settlement,d.status) }},
    {title: '操作', toolbar: '#bar_settlement',width:500,fixed:"right"}
]];

//商业保险结算单字段集合
var columns_settlement3  = [[
    {field:'cname', title: '用工企业',fixed:"left"},
    {field:'month', title: '月份',width:100,templet:function (d) {return format_date(d.month)}},
    {field:'pname', title: '保险产品',width:180},
    {field:'price', title: '保费',width:100,edit: 'text'},
    {field:'status', title: '状态',width:100,templet:function (d) { return array_value2text(status_settlement,d.status) }},
    {title: '操作', toolbar: '#bar_settlement',width:480,fixed:"right"}
]];

//面向合作商业保险结算单字段集合
var columns_settlement30  = [[
    {field:'month', title: '月份',width:100,fixed:"left",templet:function (d) {return format_date(d.month)}},
    {field:'pname', title: '保险产品'},
    {field:'price', title: '保费',width:100,edit: 'text'},
    {field:'status', title: '状态',width:100,templet:function (d) { return array_value2text(status_settlement,d.status) }},
    {title: '操作', toolbar: '#bar_settlement',width:480,fixed:"right"}
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
    {field:'cname', title: '姓名',width:80},
    {field:'place', title: '工作地点',width:80},
    {field:'post', title: '工作岗位',width:80},
    {field:'pname', title: '保险产品',width:100,edit: 'text'},
    {field:'price', title: '保费',width:80,edit: 'text'},
    {fixed: 'right', title: '操作', toolbar: '#bar_detail'}
]];



//资金明细字段集合
var columns_detail4  = [[
    {field:'time', title: '日期',templet:function (d) {return format_date(d.time)}},
    {field:'balance', title: '金额',width:100},
    {field:'comments', title: '事项',width:200}
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
    {field:'name', title: '用工企业'},
    {field:'balance', title: '账户余额',width:100},
    {fixed: 'right', title: '操作', toolbar: '#bar_confirm',width:200}
]];

//专项扣除字段集合
var columns_deduct  = [[
    {field:'name', title: '姓名',width:80},
    {field:'cardId', title: '身份证号',width:170},
    {field:'income', title: '累计收入额',width:120},
    {field:'free', title: '累计减免扣除',width:120},
    {field:'prepaid', title: '累计已预缴税额',width:120},
    {field:'deduct1', title: '累计子女教育',width:120},
    {field:'deduct2', title: '累计赡养老人',width:120},
    {field:'deduct3', title: '累计继续教育',width:120},
    {field:'deduct4', title: '累计大病医疗',width:120},
    {field:'deduct5', title: '累计住房贷款利息',width:120},
    {field:'deduct6', title: '累计住房租金',width:120},
    {field:'deduct', title: '累计扣除总额',width:120},
    {title: '操作', toolbar: '#bar_deduct'}
]];


//个税申报字段集合
var columns_tax  = [[
    {field:'id', title: '工号',width:80,fixed:"left"},
    {field:'name', title: '姓名',width:80,fixed:"left"},
    {field:'', title: '证照类型',width:120,text:"居民身份证"},
    {field:'cardId', title: '证照号码',width:160},
    {field:'month', title: '月份',width:160,templet:function (d) {return format_date(d.month)}},
    {field:'payable', title: '本期收入',width:120},
    {field:'', title: '本期免税收入',width:120,text:0},
    {field:'pension1', title: '基本养老保险费',width:120},
    {field:'medicare1', title: '基本医疗保险费',width:120},
    {field:'unemployment1', title: '失业保险费',width:120},
    {field:'fund1', title: '住房公积金',width:120},
    {field:'deduct1', title: '累计子女教育',width:120},
    {field:'deduct3', title: '累计继续教育',width:120},
    {field:'deduct5', title: '累计住房贷款利息',width:120},
    {field:'deduct6', title: '累计住房租金',width:120},
    {field:'deduct2', title: '累计赡养老人',width:120},
    {field:'', title: '企业(职业)年金',width:120,text:0},
    {field:'', title: '商业健康保险',width:120,text:0},
    {field:'', title: '税延养老保险',width:120,text:0},
    {field:'', title: '其他',width:120,text:0},
    {field:'', title: '准予扣除的捐赠额',width:120,text:0},
    {field:'', title: '减免税额',width:120,text:0},
    {field:'', title: '备注',width:120,text:0}
]];


//医/社保参保字段集合
var columns_insured1  = [[
    {field:'cardId', title: '身份证号',width:170},
    {field:'code', title: '个人代码',width:80},
    {field:'name', title: '姓名',width:80},
    {field:'start', title: '参保时间',width:80,templet:function (d) {return format_date(d.start)}},
    {field:'money', title: '月缴费工资',width:100},
    {field:'entry', title: '工作时间',width:100},
    {field:'status', title: '参保状态',width:80,templet:function (d) { return array_value2text(status_insurance,d.status) }},
    {fixed: 'right', title: '操作', toolbar: '#bar_insured'}
]];

//公积金参保字段集合
var columns_insured2  = [[
    {field:'cardId', title: '身份证号',width:170},
    {field:'name', title: '姓名',width:80},
    {field:'start', title: '起缴时间',width:80,templet:function (d) {return format_date(d.start)}},
    {field:'money', title: '工资基数',width:100},
    {field:'status', title: '参保状态',width:80,templet:function (d) { return array_value2text(status_insurance,d.status) }},
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
    {field:'name', title: '产品名称',width:180},
    {field:'fin1', title: '产品保额',width:80},
    {field:'fin2', title: '医疗保额',width:80},
    {field:'allowance', title: '住院津贴',width:80},
    {field:'period', title: '保障时段',width:100,templet:function (d) { return array_value2text(period_product,d.period) }},
    {field:'allow', title: '可参保人员',width:130,templet:function (d) { return format_product_allow(d.allow)}},
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

//----------------------------------------------------------------数据集合-----------------------
var categorys_cooperation = [
    {value:0,text:"政府部门"},
    {value:1,text:"事业单位"},
    {value:2,text:"人才市场"},
    {value:3,text:"学校"},
    {value:4,text:"内资企业"},
    {value:5,text:"外资企业"},
    {value:6,text:"港澳台企业"},
    {value:7,text:"内资工厂"},
    {value:8,text:"外资工厂"},
    {value:9,text:"港澳台工厂"},
    {value:10,text:"其它"}
];
var business_supplier = [
    {value:0,text:"顶岗实习"},
    {value:1,text:"就业安置"},
    {value:2,text:"其他"}
];
var degrees_employee = [
    {value:0,text:"高中以下"},
    {value:1,text:"高中"},
    {value:2,text:"中专"},
    {value:3,text:"大专"},
    {value:4,text:"本科"},
    {value:5,text:"硕士"},
    {value:6,text:"博士"}
];
var type_employee = [
    {value:0,text:"内部员工"},
    {value:1,text:"外派员工"},
    {value:2,text:"人才库"}
];
var status_employee = [
    {value:0,text:"在职"},
    {value:1,text:"离职"},
    {value:2,text:"退休"},
    {value:3,text:"其他"}
];

var category_employee = [
    {value:0,text:"派遣"},
    {value:1,text:"外包"},
    {value:2,text:"小时工"}
];

var household_employee = [
    {value:0,text:"外地城镇"},
    {value:1,text:"本地城镇"},
    {value:2,text:"外地农村"},
    {value:3,text:"城镇"},
    {value:4,text:"农村"},
    {value:5,text:"港澳台"},
    {value:6,text:"外籍"}
];
var reason_employee = [
    {value:0,text:"合同到期"},
    {value:1,text:"被用人单位解除劳动合同"},
    {value:2,text:"被用人单位开除"},
    {value:3,text:"被用人单位除名"},
    {value:4,text:"被用人单位辞退"},
    {value:5,text:"公司倒闭"},
    {value:6,text:"公司破产"},
    {value:7,text:"单位人员减少"},
    {value:8,text:"养老在职转退休"},
    {value:9,text:"参军"},
    {value:10,text:"入学"},
    {value:11,text:"劳改劳教"},
    {value:12,text:"出国定居"},
    {value:13,text:"异地转移"},
    {value:14,text:"不足缴费年限"},
    {value:15,text:"人员失踪"},
    {value:16,text:"错误申报"},
    {value:17,text:"其他原因减少"}
];
var status_settlement = [
    {value:0,text:"编辑"},
    {value:1,text:"提交"},
    {value:2,text:"一审"},
    {value:3,text:"二审"},
    {value:4,text:"终审"},
    {value:5,text:"扣款"},
    {value:6,text:"发放"}
];
var status_insurance = [
    {value:0,text:"新增"},
    {value:1,text:"在保"},
    {value:2,text:"拟停"},
    {value:3,text:"停保"}
];
var period_product = [
    {value:0,text:"上班时间"},
    {value:1,text:"24小时"}
];

 var allow_product = [
     {value:1,text:"一类"},
     {value:2,text:"二类"},
     {value:4,text:"三类"},
     {value:8,text:"四类"},
     {value:16,text:"五类"},
     {value:32,text:"六类"}
 ];

//合同发票类型
var invoice_contract = [
    {value:0,text:"增值税发票(全额)"},
    {value:1,text:"增值税发票(差额)"},
    {value:2,text:"普通发票"}
];
//合同服务项目类型
var stype_contract = [
    {value:0,text:"劳务派遣"},
    {value:1,text:"人事服务代理"},
    {value:2,text:"小时工"},
    {value:3,text:"商业保险"}
];

/**
 * 管理员权限列表
 */
var permissions_admin = [
    {value:1, text:"账号管理"},
    {value:2, text:"客户管理"}
];
/**
 * 派遣单位权限列表
 */
var permissions_dispatch = [
    {value:1, text:"账号管理"},
    {value:2, text:"工资审核"},
    {value:4, text:"财务审核"},
    {value:8, text:"客户管理"}
];

//----------------------------------------------------------------字段格式化-----------------------


function format_date(timestamp) {
    var d = new Date(timestamp);
    var year=d.getFullYear();
    var month=d.getMonth()+1;
    var date=d.getDate();
    return year+"-"+month+"-"+date;
}
function format_percent(value) {
    return value+"%";
}

function format_number() {
    var d = new Date
    var num =d.parseInt(num,2);
    return  num;

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

function format_product_allow(v) {
    var str = "";
    for(var i in allow_product){
        if((allow_product[i].value&v) != 0){
            str += allow_product[i].text+",";
        }
    }
    if(str.length>0){
        str = str.substr(0,str.length-1);
    }
    return str;
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
