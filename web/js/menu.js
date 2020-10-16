var menu_loaded = false;
//加载菜单
function loadMenuDispatch(permission) {
    return getMenuHtml(menuItems_dispatch,permission);
}

//加载菜单
function loadMenuInstitution() {
    if(!menu_loaded) {
        var power = Number(getCookie("power",0));
        $("#panel_menu").panel({content:getMenuHtml(menuItems_institution,power)});
        menu_loaded = true;
    }
}

//加载菜单
function loadMenuAgent() {
    if(!menu_loaded) {
        $("#panel_menu").panel({content:getMenuHtml(menuItems_agent,2147483647)});
        menu_loaded = true;
    }
}
/**
 * 平台管理员菜单项
 */
var menuItems_dispatch = [
    {text:"客户管理",icon:"&#xe70b;",child:
        [
            {value:1<<0,   text:"合作单位",href:"cooperations.html",icon:"&#xe6a7;"},
            {value:1<<1,   text:"供应商",href:"suppliers.html",icon:"&#xe6a7;"},
            {value:1<<2,   text:"合同管理",href:"contracts_cooperation.html",icon:"&#xe6a7;"}
        ]
    },
    {text:"员工管理",icon:"&#xe6a7;",child:
        [
            {value:1<<3,   text:"内部员工",href:"employees1.html",icon:"&#xe6a7"},
            {value:1<<4,   text:"外派员工",href:"'employees2.html",icon:"&#xe6a7;"},
            {value:1<<5,   text:"人才库",href:"employees3.html",icon:"&#xe6a7;"},
            {value:1<<6,   text:"合同管理",href:"contracts_employee.html",icon:"&#xe6a7;"}
        ]
    },
    {text:"结算单管理",icon:"&#xe6a7;",child:
        [
            {value:1<<7,   text:"普通结算单",href:"settlements1.html",icon:"&#xe6a7"},
            {value:1<<8,   text:"人事代理结算单",href:"settlements4.html",icon:"&#xe6a7;"},
            {value:1<<9,   text:"小时工结算单",href:"'settlements2.html",icon:"&#xe6a7;"},
            {value:1<<10,   text:"商业保险结算单",href:"settlements3.html",icon:"&#xe6a7;"},
            {value:1<<11,   text:"普通结算单审核",href:"check1.html",icon:"&#xe6a7"},
            {value:1<<12,   text:"人事代理结算单审核",href:"check4.html",icon:"&#xe6a7;"},
            {value:1<<13,   text:"小时工结算单审核",href:"'check2.html",icon:"&#xe6a7;"},
            {value:1<<14,   text:"商业保险结算单审核",href:"check3.html",icon:"&#xe6a7;"}
        ]
    },
    {text:"财务管理",icon:"&#xe6a7;",child:
        [
            {value:1<<15,   text:"工资发放",href:"payroll.html",icon:"&#xe6a7"},
            {value:1<<16,   text:"到账确认",href:"'confirm.html",icon:"&#xe6a7;"},
            {value:1<<17,   text:"个税专项扣除",href:"deducts.html",icon:"&#xe6a7;"},
            {value:1<<18,   text:"个税申报",href:"taxes.html",icon:"&#xe6a7;"}
        ]
    },
    {text:"五险一金",value:1<<19,href:"insureds.html",icon:"&#xe6a7;"},
    {text:"保险产品",value:1<<20,href:"products.html",icon:"&#xe6a7;"},
    {text:"账务管理",value:1<<21,href:"accounts.html",icon:"&#xe6a7;"}
];

var menuItems_agent = [
        {value:2147483647,  text:"快速报名",href:"fast.jsp",icon:"icon-quick"},
        {value:1,  text:"报名管理",href:"app.jsp",icon:"icon-app"},
        {value:2,  text:"票据管理",href:"bills.jsp",icon:"icon-bill"},
        {value:4,  text:"邮件管理",href:"mail.jsp",icon:"icon-mail"},
        {value:8,  text:"发布招聘",href:"recruit.jsp",icon:"icon-recruit"},
        {value:16, text:"费用结算",href:"settlement.jsp",icon:"icon-fee"},
        {value:2147483647,  text:"我的信息",href:"self.jsp",icon:"icon-info"}
    ]

/**
 * 培训机构员工菜单项
 */
var menuItems_institution = [
    {value:2147483647,  text:"待办提醒",href:"remind.jsp",icon:"icon-notice"},
    {value:2147483647,  text:"快速报名",href:"fast.jsp",icon:"icon-quick"},
    {value:1,  text:"报名管理",href:"app.jsp",icon:"icon-app"},
    {value:2,  text:"培训管理",href:"train.jsp",icon:"icon-train"},
    {value:4,  text:"考核管理",href:"exam.jsp",icon:"icon-exam"},
    {value:8,  text:"制证管理",href:"accredit.jsp",icon:"icon-cert"},
    {value:16, text:"学员管理",href:"student.jsp",icon:"icon-student"},
    {value:32, text:"项目管理",href:"item.jsp",icon:"icon-item"},
    {value:64, text:"教师管理",href:"teacher.jsp",icon:"icon-teacher"},
    {value:128,text:"地址管理",href:"address.jsp",icon:"icon-address"},
    //{value:256,text:"费用结算",href:"settlement.jsp",icon:"icon-fee"},
    {value:512,text:"员工管理",href:"stuff.jsp",icon:"icon-stuff"},
    {value:1024,text:"业务推广",href:"qr.jsp",icon:"icon-qr"}
];

function getMenuHtml(items, value) {
    var html1 = "";
    for(var i in items){
        var item = items[i];
        if(!item.child){
            if(item.value & value){
                html1 += String.format("<li><a onclick=\"xadmin.add_tab('{0}','{1}')\"><i class=\"iconfont\">{2}</i><cite>{3}</cite></a></li>",item.text,item.href,item.icon,item.text);
            }
        }else{
            var html2 = "";
            for(var j in item.child){
                var child = item.child[j];
                if(child.value & value){
                    html2 += String.format("<li><a onclick=\"xadmin.add_tab('{0}','{1}')\"><i class=\"iconfont\">{2}</i><cite>{3}</cite></a></li>",child.text,child.href,child.icon,child.text);
                }
            }
            if(html2.length > 0){
                html1 += String.format("<a href=\"javascript:;\"><i class=\"iconfont\">{0};</i><cite>{1}</cite><i class=\"iconfont\">&#xe697;</i></a><ul class=\"sub-menu\">{2}</ul>",item.icon,item.text,html2);
            }
        }
    }
    return html1;
}

//字符串格式化,用法：String.format("x={0},y={1}",x,y);
String.format = function(src){
    if (arguments.length == 0) return null;
    var args = Array.prototype.slice.call(arguments, 1);
    return src.replace(/\{(\d+)\}/g, function(m, i){
        return args[i];
    });
};