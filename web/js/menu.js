var menu_loaded = false;
//加载菜单
function loadMenuDispatch(permission) {
    return getMenuHtml(menuItems_dispatch,permission);
}

function loadMenuCooperation(permission) {
    return getMenuHtml(menuItems_cooperation,permission);
}

/**
 * 派遣方菜单项
 */
var menuItems_dispatch = [
    {id:"0,7",title:"客户管理",icon:"&#xe70b",children:
        [
            {id:"0,1",   title:"合作单位",href:"cooperations.html",icon:"&#xe6a7"},
            {id:"0,2",   title:"供应商",href:"suppliers.html",icon:"&#xe6a7"},
            {id:"0,4",   title:"合同管理",href:"contracts_cooperation.html",icon:"&#xe6a7"}
        ]
    },
    {id:"0,120",title:"员工管理",icon:"&#xe6a7",children:
        [
            {id:"0,8",   title:"内部员工",href:"employees1.html",icon:"&#xe6a7"},
            {id:"0,16",   title:"外派员工",href:"employees2.html",icon:"&#xe6a7"},
            {id:"0,32",   title:"人才库",href:"employees3.html",icon:"&#xe6a7"},
            {id:"0,64",   title:"合同管理",href:"contracts_employee.html",icon:"&#xe6a7"}
        ]
    },
    {id:"0,896",title:"劳务派遣",icon:"&#xe6a7",children:
        [
            {id:"0,128",   title:"结算",href:"settlements11.html",icon:"&#xe6a7"},
            {id:"0,256",   title:"初审",href:"check11.html?type=0",icon:"&#xe6a7"},
            {id:"0,512",   title:"终审",href:"check11.html?type=1",icon:"&#xe6a7"}
        ]
    },
    {id:"0,7168",title:"劳务外包",icon:"&#xe6a7",children:
        [
            {id:"0,1024",   title:"结算",href:"settlements12.html",icon:"&#xe6a7"},
            {id:"0,2048",   title:"初审",href:"check12.html?type=0",icon:"&#xe6a7"},
            {id:"0,4096",   title:"终审",href:"check12.html?type=1",icon:"&#xe6a7"}
        ]
    },
    {id:"0,57344",title:"代发工资",icon:"&#xe6a7",children:
        [
            {id:"0,8192",   title:"结算",href:"settlements13.html",icon:"&#xe6a7"},
            {id:"0,16384",   title:"初审",href:"check13.html?type=0",icon:"&#xe6a7"},
            {id:"0,32768",   title:"终审",href:"check13.html?type=1",icon:"&#xe6a7"}
        ]
    },
    {title:"代缴社保",id:"0,458752",icon:"&#xe6a7",children:
        [
            {id:"0,65536",   title:"结算",href:"settlements14.html",icon:"&#xe6a7"},
            {id:"0,131072",   title:"初审",href:"check14.html?type=0",icon:"&#xe6a7"},
            {id:"0,262144",   title:"终审",href:"check14.html?type=1",icon:"&#xe6a7"}
        ]
    },
    {title:"小时工",id:"0,3670016",icon:"&#xe6a7",children:
        [
            {id:"0,524288",   title:"结算",href:"settlements2.html",icon:"&#xe6a7"},
            {id:"0,1048576",   title:"初审",href:"check21.html?type=0",icon:"&#xe6a7"},
            {id:"0,2097152",   title:"终审",href:"check22.html?type=1",icon:"&#xe6a7"}
        ]
    },
    {title:"商业保险",id:"0,62914560",icon:"&#xe6a7",children:
        [
            {id:"0,4194304",   title:"保险产品",href:"products.html",icon:"&#xe6a7"},
            {id:"0,8388608",   title:"结算",href:"settlements3.html",icon:"&#xe6a7"},
            {id:"0,16777216",   title:"初审",href:"check31.html?type=0",icon:"&#xe6a7"},
            {id:"0,33554432",   title:"终审",href:"check32.html?type=1",icon:"&#xe6a7"}
        ]
    },
    {title:"财务管理",id:"0,1006632960",icon:"&#xe6a7",children:
        [
            {id:"0,67108864",   title:"工资发放",href:"payroll.html",icon:"&#xe6a7"},
            {id:"0,134217728",   title:"到账确认",href:"confirm.html",icon:"&#xe6a7"},
            {id:"0,268435456",   title:"个税专项扣除",href:"deducts.html",icon:"&#xe6a7"},
            {id:"0,536870912",   title:"个税申报",href:"taxes.html",icon:"&#xe6a7"}
        ]
    },
    {title:"五险一金",id:"0,1073741824",href:"insureds.html",icon:"&#xe6a7"}
];

/**
 * 合作方菜单项
 */
var menuItems_cooperation = [
    {id:120,title:"员工管理",icon:"&#xe6a7",children:
        [
            {id:1<<3,   title:"员工管理",href:"employees1.html",icon:"&#xe6a7"},
            {id:1<<6,   title:"合同管理",href:"contracts_employee.html",icon:"&#xe6a7"}
        ]
    },
    {id:384,title:"劳务派遣",icon:"&#xe6a7",children:
        [
            {id:1<<7,   title:"结算单",href:"settlements11.html",icon:"&#xe6a7"},
            {id:1<<8,   title:"汇总统计",href:"check11.html",icon:"&#xe6a7"},
        ]
    },
    {id:1536,title:"劳务外包",icon:"&#xe6a7",children:
        [
            {id:1<<9,   title:"结算单",href:"settlements12.html",icon:"&#xe6a7"},
            {id:1<<10,   title:"汇总统计",href:"check12.html",icon:"&#xe6a7"},
        ]
    },
    {id:6144,title:"代发工资",icon:"&#xe6a7",children:
        [
            {id:1<<11,   title:"结算单",href:"settlements13.html",icon:"&#xe6a7"},
            {id:1<<12,   title:"汇总统计",href:"check13.html",icon:"&#xe6a7"},
        ]
    },
    {title:"代缴社保",id:24576,icon:"&#xe6a7",children:
        [
            {id:1<<13,   title:"结算单",href:"settlements14.html",icon:"&#xe6a7"},
            {id:1<<14,   title:"汇总统计",href:"check14.html",icon:"&#xe6a7"},
        ]
    },
    {title:"小时工",id:98304,icon:"&#xe6a7",children:
        [
            {id:1<<15,   title:"结算单",href:"settlements2.html",icon:"&#xe6a7"},
            {id:1<<16,   title:"汇总统计",href:"check2.html",icon:"&#xe6a7"},
        ]
    },
    {title:"商业保险",id:917504,icon:"&#xe6a7",children:
        [
            {id:1<<18,   title:"结算单",href:"settlements3.html",icon:"&#xe6a7"},
            {id:1<<19,   title:"新参/替换",href:"../dialog/detail3.html",icon:"&#xe6a7"},
            {id:1<<16,   title:"汇总统计",href:"check2.html",icon:"&#xe6a7"},
        ]
    }
];

function getMenuHtml(items, permission) {
    var html1 = "";
    for(var i in items){
        var item = items[i];
        if(!item.children){
            if(bit_test(item.id,permission)){
                html1 += String.format("<li><a onclick=\"xadmin.add_tab('{0}','{1}')\"><i class=\"iconfont\">{2}</i><cite>{3}</cite></a></li>",item.title,item.href,item.icon,item.title);
            }
        }else{
            var html2 = "";
            for(var j in item.children){
                var children = item.children[j];
                if(bit_test(children.id,permission)){
                    html2 += String.format("<li><a onclick=\"xadmin.add_tab('{0}','{1}')\"><i class=\"iconfont\">{2}</i><cite>{3}</cite></a></li>",children.title,children.href,children.icon,children.title);
                }
            }
            if(html2.length > 0){
                html1 += String.format("<li><a href=\"javascript:;\"><i class=\"iconfont\">{0};</i><cite>{1}</cite><i class=\"iconfont nav_right\">&#xe697;</i></a><ul class=\"sub-menu\">{2}</ul></li>",item.icon,item.title,html2);
            }
        }
    }
    if(bit_test(permission,"1073741824,1073741824")){
        html1 += "<li><a onclick=\"xadmin.add_tab('账户管理','accounts.html')\"><i class=\"iconfont\">&#xe6a7</i><cite>账户管理</cite></a></li>";
    }
    return html1;
}


function bit_test(str1,str2) {
    var s1 = str1.split(",");
    var s2 = str2.split(",");
    var n11 = Number(s1[0]);
    var n12 = Number(s1[1]);
    var n21 = Number(s2[0]);
    var n22 = Number(s2[1]);
    return ((n11&n21)!=0)||((n12&n22)!=0);
}

//字符串格式化,用法：String.format("x={0},y={1}",x,y);
String.format = function(src){
    if (arguments.length == 0) return null;
    var args = Array.prototype.slice.call(arguments, 1);
    return src.replace(/\{(\d+)\}/g, function(m, i){
        return args[i];
    });
};