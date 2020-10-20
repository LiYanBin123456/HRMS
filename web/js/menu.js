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
    {id:7,title:"客户管理",icon:"&#xe70b",children:
        [
            {id:1<<0,   title:"合作单位",href:"cooperations.html",icon:"&#xe6a7"},
            {id:1<<1,   title:"供应商",href:"suppliers.html",icon:"&#xe6a7"},
            {id:1<<2,   title:"合同管理",href:"contracts_cooperation.html",icon:"&#xe6a7"}
        ]
    },
    {id:120,title:"员工管理",icon:"&#xe6a7",children:
        [
            {id:1<<3,   title:"内部员工",href:"employees1.html",icon:"&#xe6a7"},
            {id:1<<4,   title:"外派员工",href:"'employees2.html",icon:"&#xe6a7"},
            {id:1<<5,   title:"人才库",href:"employees3.html",icon:"&#xe6a7"},
            {id:1<<6,   title:"合同管理",href:"contracts_employee.html",icon:"&#xe6a7"}
        ]
    },
    {id:32640,title:"结算单管理",icon:"&#xe6a7",children:
        [
            {id:1<<7,   title:"普通结算单",href:"settlements1.html",icon:"&#xe6a7"},
            {id:1<<8,   title:"人事代理结算单",href:"settlements4.html",icon:"&#xe6a7"},
            {id:1<<9,   title:"小时工结算单",href:"'settlements2.html",icon:"&#xe6a7"},
            {id:1<<10,   title:"商业保险结算单",href:"settlements3.html",icon:"&#xe6a7"},
            {id:1<<11,   title:"普通结算单审核",href:"check1.html",icon:"&#xe6a7"},
            {id:1<<12,   title:"人事代理结算单审核",href:"check4.html",icon:"&#xe6a7"},
            {id:1<<13,   title:"小时工结算单审核",href:"'check2.html",icon:"&#xe6a7"},
            {id:1<<14,   title:"商业保险结算单审核",href:"check3.html",icon:"&#xe6a7"}
        ]
    },
    {id:491520,title:"财务管理",icon:"&#xe6a7",children:
        [
            {id:1<<15,   title:"工资发放",href:"payroll.html",icon:"&#xe6a7"},
            {id:1<<16,   title:"到账确认",href:"'confirm.html",icon:"&#xe6a7"},
            {id:1<<17,   title:"个税专项扣除",href:"deducts.html",icon:"&#xe6a7"},
            {id:1<<18,   title:"个税申报",href:"taxes.html",icon:"&#xe6a7"}
        ]
    },
    {title:"五险一金",id:1<<19,href:"insureds.html",icon:"&#xe6a7"},
    {title:"保险产品",id:1<<20,href:"products.html",icon:"&#xe6a7"},
    {title:"账户管理",id:1<<21,href:"accounts.html",icon:"&#xe6a7"}
];

function getMenuHtml(items, permission) {
    var html1 = "";
    for(var i in items){
        var item = items[i];
        if(!item.children){
            if(item.id & permission){
                html1 += String.format("<li><a onclick=\"xadmin.add_tab('{0}','{1}')\"><i class=\"iconfont\">{2}</i><cite>{3}</cite></a></li>",item.title,item.href,item.icon,item.title);
            }
        }else{
            var html2 = "";
            for(var j in item.children){
                var children = item.children[j];
                if(children.id & permission){
                    html2 += String.format("<li><a onclick=\"xadmin.add_tab('{0}','{1}')\"><i class=\"iconfont\">{2}</i><cite>{3}</cite></a></li>",children.title,children.href,children.icon,children.title);
                }
            }
            if(html2.length > 0){
                html1 += String.format("<li><a href=\"javascript:;\"><i class=\"iconfont\">{0};</i><cite>{1}</cite><i class=\"iconfont nav_right\">&#xe697;</i></a><ul class=\"sub-menu\">{2}</ul></li>",item.icon,item.title,html2);
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