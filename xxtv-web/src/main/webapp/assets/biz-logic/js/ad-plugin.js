function initSize() {
	if ($.type(top.getContentPanelSize) == 'function') {
		var size = top.getContentPanelSize();
		if (typeof resize === 'function') {
			resize(size.width, size.height);
		}
		if(typeof custom === 'function') {
			custom(size.width, size.height);
		}
	}
}

function resize(width,height){
	if(!$.isEmptyObject($('#query_con').get(0))) {
		height -= $('#query_con').height() + 7;
	}
	if(!$.isEmptyObject($('#grid').get(0))) {
		$('#grid').datagrid('resize',{width:width,height:height});
	}
}
/**
 * 引用外部JS
 * @param {} path JS的相对路径
 */
function getScript(path) {
	document.write('<script type="text/javascript" src="' + window.rootPath + "/" + path + window.resVersion + '"></script>');
}
/**
 * 引用外部CSS
 * @param {} path CSS的相对路径
 */
function getLink(path) {
	document.write('<link type="text/css" rel="stylesheet" href="' + window.rootPath + "/" + path + window.resVersion + '" />');
}
/**
 * 引入翻译显示JS
 * @param {} types
 */
function display(types){
    document.write('<script type="text/javascript" src="' + window.rootPath + "/displayjs?type=" + encodeURIComponent(types) + "&v=" + new Date() + '"></script>');
}


var weeks = [ '日', '一', '二', '三', '四', '五', '六' ];
function formatDate(date, format) {
	return format.replace(/YYYY|MM|DD|hh|mm|ss|星期/g, function(a) {
		switch (a) {
		case 'YYYY':
			return date.getFullYear();
		case 'MM':
			return ((date.getMonth() + 1 < 10) ? '0' : '')
					+ (date.getMonth() + 1);
		case 'DD':
			return (date.getDate() < 10 ? '0' : '') + date.getDate();
		case 'hh':
			return (date.getHours() < 10 ? '0' : '') + date.getHours();
		case 'mm':
			return (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
		case 'ss':
			return (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
		case '星期':
			return '星期' + weeks[date.getDay()];
		}
	});
}

Number.prototype.toPercent = function(){
	return (Math.round(this * 10000)/100).toFixed(2) + '%';
	};
Number.prototype.toQianPercent = function(){
		return (Math.round(this * 10000)/10).toFixed(2) + '‰';
};
(function($){
	var randomList = {};
	/*生产随机数*/
	function getRandom(length){
		length = length ? length : 8;
		var str;
		while(true){
			str = Math.random().toString().substr(2, length);
			if(!randomList[str]){
				randomList[str] = null;
				break;
			}
		}
		return str;
	}
	/*键盘F5刷新mainFrame中的内容*/
	$(document).bind('keydown',function(event){
		event = window.event || event;
		if(event.keyCode == 116){
			if(document.getElementById("contentBody")){
				document.getElementById("contentBody").contentWindow.location.reload();
			} else {
				document.location.reload();
			}
			event.keyCode = 0;   
	        event.cancelBubble = true;   
	        return false;
		}
	});
	/*Dom加载完成之后调用*/
	$(function(){
		if(typeof top.showFrameLoading === 'function'){ top.showFrameLoading(false); }
		$.ajaxSetup({cache:false,error:function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus == 'timeout' || textStatus == 'error'){
				$.messager.alert('请求错误','AJAX请求失败~[请检查网络]','error');
			} else {
				$.messager.alert('请求错误','AJAX请求错误~','error');
			}
			/*console.log('AJAX请求超时~');*/
		},dataFilter:function(data,type){
			return data;
		},statusCode:{
			404:function(){$.messager.alert('请求错误','请求路径错误~','error');},
			302:function(){$.messager.alert('请求错误','用户登录失效~','error');},
			500:function(){$.messager.alert('请求错误','服务端出现异常~','error');}
		}});
		/* 绑定文档加载完成事件 */
		$(window).bind('load',initSize);
		/*格式所有select*/
		$('select').each(function(i, n){
			if($(this).css('display') != 'none'){
				var width = $(this).css('width').replace('px', '');
				$(this)._pullDownList({width: Number(width)});
			}
		});
	});
	function ajaxRequest(url, params, callback, type, dataType) {
		$.ajax({
			dataType:type ? type : 'json',
			url:url,
			data:params,
			cache:false,
			type:dataType,
			success:callback
		});
	}
	/*jQuery插件$.method(params1, params2, ...)*/
	$.extend({
		_ajaxPost:function(url, params, callback, type){
			ajaxRequest(url, params, callback, type, 'POST');
		},
		_ajaxGet:function(url, params, callback, type){
			ajaxRequest(url, params, callback, type, 'GET');
		},
		formatDate: function formatDate(date, format) {
			var weeks = ['日', '一', '二', '三', '四', '五', '六'];
			return format.replace(/YYYY|MM|DD|hh|mm|ss|星期/g, function(a) {
				switch (a) {
					case 'YYYY' :
						return date.getFullYear();
					case 'MM' :
						return ((date.getMonth() + 1 < 10) ? '0' : '') + (date.getMonth() + 1);
					case 'DD' :
						return (date.getDate() < 10 ? '0' : '') + date.getDate();
					case 'hh' :
						return (date.getHours() < 10 ? '0' : '') + date.getHours();
					case 'mm' :
						return (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
					case 'ss' :
						return (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
					case '星期' :
						return '星期' + weeks[date.getDay()];
				}
			});
		}
	});
	/*jQuery插件$(select).method(params1, params2, ...)*/
	$.fn.extend({
		_jsonToForm:function(json){
			var form = $(this);
			if($.isPlainObject(json)){
				$.each(json,function(i,n){
					var field = form.find('input[name='+i+']');
					switch (field.attr('type')) {
					case 'hidden':
						field.val(n);
						break;
					case 'text':
						field.val(n);
						break;
					case 'radio':
						field.each(function(){
							var value = $(this).val();
							if(value == 'true'){value = true;}else if(value == 'false'){value = false;}
							if(value == n){$(this).get(0).checked = true;}
							});
						break;
					case 'checkbox':
						field.each(function(){
							var value = $(this).val();
							if(value == 'true'){value = true;}else if(value == 'false'){value = false;}
							if(value == n){$(this).get(0).checked = true;}
						});
						break;
					default:
						field = form.find('select[name='+i+']');
						if(!$.isEmptyObject(field.get(0))){
							field.children('option').each(function(){
								if($(this).val() == n){this.selected = true;}
							});
						}
						field = form.find('textarea[name='+i+']');
						if(!$.isEmptyObject(field.get(0))){
							field.val(n);
						}
						break;
					}
				});
			}
		},_formToJson:function(){
			var str = $(this).serialize();
				str = str.replace(/\+/g,"%20");
				str = str.replace(/&/g,"\",\"");
				str = str.replace(/=/g,"\":\"");
			var data = eval("({\""+str +"\"})");
			$.each(data,function(i,n){data[i] = $.trim(decodeURIComponent(n));});
			return data; 
		},_ajaxForm:function(callback,data){
			var target = $(this);
			var options = {url:$(this).attr('action'),type:'POST',beforeSubmit:function(){
				var submit = target.data('submit') ? false : true;
				target.data('submit', true);
				if(!submit) {
					asyncbox.tips('不可重复提交表单！', 'error');
				}
				return submit;
			},success: function(msg){
				if(typeof callback === 'function'){
					try {
						callback.call(target,msg);
					} catch (e) {
						$.messager.alert('操作提示','AJAX请求返回数据解析错误~','error');
						console.error('AJAX请求返回数据解析错误~['+e.message+']');
					}
				}
			}, complete : function(){
				target.data('submit', false);
			},data:data};
			$(this).ajaxSubmit(options);
		},_datagrid:function(params){
			var defaults = {
				pageSize : 30,
				nowrap : true,
				pagination : true,
				rownumbers : true,
				width : 'auto',
				height : 'auto',
				fitColumns : true,
				singleSelect : true,
				striped : true,
				queryParams : {},
				onLoadError:function(e){
					$.messager.alert('操作提示','加载远程数据发生错误~','error');
					console.info(e);
				}
			};
			$.extend(defaults, params);
			$(this).datagrid(defaults);
			return this;
		},
		_uploadify:function(options){
			var defaults = {
				 height		: 22,
				 width		: 60,
				 auto		: true,
				 multi		: false,
				 debug		: false,
				 swf		: window.rootPath + '/res/js/uploadify/uploadify.swf',
				 buttonImage: window.rootPath + '/res/admin/images/file_select_btn.png',
				 queueID	: 'no_queueID',
				 onUploadStart : function (file){
				 	$.messager.progress({title:'文件上传', msg: file.name + ' - 上传中……', interval:86400000});
				 	$.messager.progress('bar').progressbar('setValue', 0);
				 },
				 onUploadProgress : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal){
				 	$.messager.progress('bar').progressbar('setValue', Math.round(bytesUploaded / bytesTotal * 100));
				 },
				 onUploadComplete : function(file){
				 	$.messager.progress('close');
				 },
				 onUploadError : function(file, errorCode, errorMsg, errorString){
				 	alert(file.name + '上传失败！');
				 	console.log(errorCode + '-->' + errorMsg + '-->' + errorString);
				 }
			};
			$.extend(defaults, options || {});
			/*defaults.uploader += ';JSESSIONID=' + window.sessionId;*/
			$(this).uploadify(defaults);
		},
		_validatorGropDisabled : function(validType, disabled){
			$(this).data('validator_group_' + validType + '_disabled', disabled);
		},
		_pullDownList : function(options){/*下拉菜单*/
			var defaults = {
				width:150,
				maxHeight:200,
				compatibility:false
			};
			$.extend(defaults, options || {});
			/*defaults.width += 2;*/
			var target = $(this),targetId = $(this).attr('id');
			if(target.data('isload')) {
				if (typeof options == "string") {
					switch(options){
						case 'refresh':
							target.data('refresh', true);
							target.change();
							break;
						default :
							alert('控件未提供该方法');
					}
				}
			} else {
				var random = getRandom();
				target.hide();
				target.data('isload', true).data('random', random);
				var html = '<span class="pull-down-list-input" listid="'+random+'" style="width: ' + ($.browser.msie ? defaults.width + 2 : defaults.width) + 'px;">';
					html += '<input type="text" class="input-text" readonly="readonly"/>';
					html += '<span class="pull-down-list-button"></span></span>';
				target.after(html);
				var span_tag = target.next();
				span_tag.prepend(target);
				var input_tag = target.next();
				if(target.attr('data-options') && target.attr('data-options').indexOf('required') != -1){
					var valid = eval('({'+target.attr('data-options')+'})');
					if(valid.required){
						valid.validType = 'requiredSelect[true]';
					}
					input_tag.validatebox(valid);
					target.removeAttr('data-options').removeAttr('class').removeAttr('required');
				}
				input_tag.css({border:'solid 1px #679FD2',background:'url('+window.rootPath+'/res/ad/images/input_bg_d.jpg) repeat-x',color:'#999',height: '18px'});
				input_tag.width(defaults.width - 20).css('border-right','0px');
				$('body').append('<div class="pull-down-list-panel" listid="'+random+'"><ul class="pull-down-list"></ul></div>');
				var item_panel = $('body > div').last();
				item_panel.css({width:defaults.width - ($.browser.msie ? 0 : 2) + "px", 'max-height':defaults.maxHeight + "px"});
				/*浏览器兼容性控制*/
				if(defaults.compatibility && $.browser.msie && Number($.browser.version) < 8){
					input_tag.next().css('margin-top','1px');
				}
				var item_ul = item_panel.children('ul:first');
				function loadList(){
					target.children('option').each(function(){
						var text = $(this).text();
						var value = $(this).val();
						item_ul.append('<li class="pull-down-list-li' + ($(this).attr('selected') ? ' selected' : '') + '" val=' + value + '>' + text + '</li>');
						if($(this).attr('selected')){
							input_tag.val(text);
						}
					});
					item_ul.children('li').click(function(){
						item_panel.hide();
						input_tag.val($(this).text()).focus();
						target.val($(this).attr('val')).change();
					});
				}
				loadList();
				target.bind('change', function(){
					if(target.data('refresh')){
						item_ul.children('li').remove();
						loadList();
						target.data('refresh', false);
					}
					item_ul.children('li').removeClass('selected');
					var item_li = item_ul.children('li[val='+$(this).val()+']').addClass('selected');
					input_tag.val(item_li.text()).focus();
					item_panel.hide();
				});
				function listDivToggle(){
					if(!target.get(0).disabled){
						item_panel.toggle(0, function(){
							if($(this).css('display') == 'block'){
								listDivShow();
							} else {
								listDivHide();
							}
						});
					}
				}
				input_tag.click(listDivToggle).next().click(listDivToggle);
				function listDivShow(){
					var offset = input_tag.offset();
					item_panel.css({left:offset.left + "px", top:offset.top + input_tag.outerHeight() + 1 + "px"}).show();
					input_tag.focus();
					$(document).unbind('mousedown', mousedown).unbind('keydown',keydown);
					$(document).bind('mousedown', mousedown).bind('keydown',keydown);
				}
				function listDivHide(){
					$(document).unbind('mousedown', mousedown).unbind('keydown',keydown);
				}
				/*点击非控件内组建隐藏面板*/
				function mousedown(event){
					event = window.event || event;
					var object = event.srcElement ? event.srcElement:event.target;
					var listid = $(object).parents('span[listid]').attr('listid');
					listid = listid ? listid : $(object).parents('div[listid]').attr('listid');
					listid = listid ? listid : $(object).attr('listid');
					if(listid != random) {
						item_panel.hide();
						listDivHide();
					}
				}
				function keydown(event){
					event = window.event || event;
					if(event.keyCode == 9){
						item_panel.hide();
						listDivHide();
					}
				}
			}
		},
		_pullDownTree : function(options){/*下拉树结构*/
			var defaults = {
				width:200,
				height:250,
				expand:true,
				treeName: 'name',
				treeId: 'id',
				treePId : 'pId',
				filterParent: true,
				compatibility:false,
				data:null,
				clear:false,
				refresh:false,
				url:null,
				checkeds:null,
				qryParams:{},
				chkStyle:'checkbox',
				radioType:'level',
				onCheck:null,
				chkboxType:{ "Y" : "ps", "N" : "ps"},
				successCallback:null
			};
			$.extend(defaults, options || {});
			defaults.width += ($.browser.msie ? 4 : 2);
			var target = $(this), targetId = $(this).attr('id'), zTree;
			if(target.data('isload')){
				var zTree = $.fn.zTree.getZTreeObj(targetId + '_pull_down_tree');
				if (typeof options == "string") {
					switch (options) {
					case 'getTreeNodes' :
						return zTree.getNodes();
					case 'getTreeObj' :
						return zTree;
					case 'isLoadData' :
						return zTree.getNodes().length > 0;
					case 'clear' :
						zTree.checkAllNodes(false);
						target.val('');
						target.next().val('');
						break;
					default:
						alert('控件未提供该方法');
					}
				}
				if(defaults.clear){
					zTree.checkAllNodes(false);
					target.val('');
					target.next().val('');
				}
				if(defaults.refresh){
					zTree.destroy();
					loadTree();
				}
				if($.isArray(defaults.checkeds)){
					zTree.checkAllNodes(false);
					var ids = new Array(), names = new Array(), n = 0, node;
					for(var i in defaults.checkeds){
						node = zTree.getNodeByParam(defaults.treeId, defaults.checkeds[i][defaults.treeId], null);
						zTree.checkNode(node, true, true, false);
						ids[n] = node[defaults.treeId];
						names[n++] = node[defaults.treeName];
					}
					target.val(names.join(','));
					target.next().next().val(ids.join(','));
				}
			} else {
				var random = getRandom();
				target.data('isload', true).data('random', random).addClass('input-text');
				target.focus(function(){$(this).blur();});
				target.css({border:'solid 1px #B1C242',background:'url('+window.rootPath+'/assets/biz-logic/images/input_bg_d.jpg) repeat-x',color:'#999',height: '18px'});
				var html = '<span class="pull-down-list-input" listid="'+random+'" style="width: ' + defaults.width + 'px;">';
				html+= '<span class="pull-down-list-button"></span>';
				html+= '<input name="' + target.attr('name') + '" type="hidden" /></span>';
				target.after(html);
				target.next().prepend(target);
				target.width(defaults.width - 20).css('border-right','0px').attr('readonly','readonly').attr('name', target.attr('name') + '_show');
				$('body').append('<div class="pull-down-list-panel" listid="'+random+'"><ul class="ztree" id="' + targetId + '_pull_down_tree"></ul></div>');
				var item_panel = $('body > div').last();
				item_panel.css({width:defaults.width - ($.browser.msie ? 0 : 2) + "px", height:defaults.height + "px"});
				/*浏览器兼容性控制*/
				if(defaults.compatibility && $.browser.msie && Number($.browser.version) < 8){
					target.next().css('margin-top','1px');
				}
				function treeDivToggle(){
					item_panel.toggle(0, function(){
						if($(this).css('display') == 'block'){
							treeDivShow();
						} else {
							treeDivHide();
						}
					});
				}
				/*切换显示/隐藏*/
				target.click(treeDivToggle).next().click(treeDivToggle);
				function treeDivShow(){
					var offset = target.offset();
					item_panel.css({left:offset.left + "px", top:offset.top + target.outerHeight() + 1 + "px"}).show();
					target.focus();
					$(document).unbind('mousedown', mousedown).unbind('keydown',keydown);
					$(document).bind('mousedown', mousedown).bind('keydown',keydown);
				}
				function treeDivHide(){
					$(document).unbind('mousedown', mousedown).unbind('keydown',keydown);
				}
				/*点击非控件内组建隐藏面板*/
				function mousedown(event){
					event = window.event || event;
					var object = event.srcElement ? event.srcElement:event.target;
					var listid = $(object).parents('span[listid]').attr('listid');
					listid = listid ? listid : $(object).parents('div[listid]').attr('listid');
					listid = listid ? listid : $(object).attr('listid');
					if(listid != random) {
						item_panel.hide();
						treeDivHide();
					}
				}
				function keydown(event){
					event = window.event || event;
					if(event.keyCode == 9){
						item_panel.hide();
						treeDivHide();
					}
				}
				loadTree();
			}

			function loadTree() {
				/*初始化树结构*/
				var setting = {
					check : {
						enable : true, chkStyle : defaults.chkStyle, chkboxType : defaults.chkboxType, radioType:defaults.radioType
					},
					data : {
						simpleData : { enable : true, idKey : defaults.treeId, pIdKey : defaults.treePId},
						key : { name : defaults.treeName, url : 'noUrl' }
					},
					callback : {
						onClick : function(event, treeId, treeNode) {
							if (treeNode.isParent) {
								zTree.expandNode(treeNode, !treeNode.open, false, true);
							}
						},
						onCheck:function(event, treeId, treeNode){
							if($.isFunction(defaults.onCheck)) {
								defaults.onCheck(zTree, treeNode, target, target.next().next());
							} else {
								var nodes = zTree.getCheckedNodes(true), ids = new Array(), names = new Array(), n = 0;
								for(var i in nodes){
									if(defaults.filterParent && nodes[i].isParent){
										continue;
									}
									ids[n] = nodes[i][defaults.treeId];
									names[n++] = nodes[i][defaults.treeName];
								}
								target.val(names.join(','));
								target.next().next().val(ids.join(','));
								target.focus();
							}
						}
					},
					view : { showTitle : false, selectedMulti : false, autoCancelSelected : false }
				};
				$._ajaxPost(defaults.url,defaults.qryParams,function(r) {
					if(r.r){
						$.fn.zTree.init($('#' + targetId + '_pull_down_tree'), setting, r.d);
						zTree = $.fn.zTree.getZTreeObj(targetId + '_pull_down_tree');
						zTree.expandAll(defaults.expand);/*展开全部节点*/
						if($.isFunction(defaults.successCallback)){
							defaults.successCallback(zTree);
						}
					}else {
						alert(r.m);
					}
				});
			}
		}
	});
	/*EasyUI功能拓展*/
	$.extend($.fn.validatebox.defaults.rules, {
		requiredSelect: {
		  validator: function(value, param){
	            return param ? ($.trim(value) != '' && $.trim(value) != '==请选择==') : false;
	        },
	        message: '该输入项为必输项'
		},
	    number: {
	        validator: function(value, param){
	            return !/\D/g.test(value);
	        },
	        message: '该输入项只能输入数字'
	    },
	    doublenumber: {
	        validator: function(value, param){
	            return !isNaN(value);
	        },
	        message: '该输入项只能输入数字'
	    },
	    version: {
	    	validator: function(value, param){
	    		return /^\d{1,}(\.\d{1,}){1,}$/.test(value);
	    	},
	    	message: '请输入规范的版本号(数字和小数点的组合)！'
	    },
	    username: {
	    	validator: function(value, param){
	            return /^[a-z0-9_-]{3,16}$/.test(value);
	        },
	        message: '请输入规范的用户名'
	    },
	    password: {
	    	validator: function(value, param){
	            return /^[a-z0-9_-]{6,18}$/.test(value);
	        },
	        message: '请输入规范的密码'
	    },
	    verify_password: {
	    	validator: function(value, param){
	            return value === $('#' + param).val();
	        },
	        message: '两次输入密码不一致'
	    },
	    group: {
	    	validator: function(value, param){
	    		/*
	    		 * 组合验证：validType:'group[{\'username\':null,\'length\':[0,3]},\'nick\']';
	    		 * 启用和停止某一个验证：$('#nick')._validatorGropDisabled('length',true);(true:不验证，false:验证)
	    		 * */
	    		var valid = $.fn.validatebox.defaults.rules, target, ok = true;
	    		if(param.length > 1){
	    			target = $('#' + param[1]);
	    		}
	    		for(var i in param[0]) {
	    			if(target && target.data('validator_group_' + i + '_disabled')){
	    				continue;
	    			}
	    			ok = valid[i].validator(value, param[0][i]);
	    			if(!ok){
	    				var msg = valid[i].message;
	    				if(param[0][i]){
	    					for(var n in param[0][i]){
	    						msg = msg.replace(new RegExp("\\{" + n + "\\}", "g"), param[0][i][n]);
	    					}
	    				}
	    				param[0] = msg;
	    				break;
	    			}
	    		}
	            return ok;
	        },
	        message: '{0}'
	    }
	});
	$.extend($.fn.dialog.defaults, {
		onOpen:function(){
			$(this).find('.datebox input').css('border','0px');
			$(this).find('select').change();
		}
	});
	$.extend($.fn.validatebox.defaults.rules, {
		integer: {
	        validator: function(value, param){
	        	if(/^(0|([1-9]\d*))$/.test(value)){
	        		if(value > 100000000){
	        			return false;
	        		}
	        		return true;
	        	}else {
	        		false;
	        	}
	        },
	        message: '请输入0到99999999的正整数'
	    },
	    spending: {
	        validator: function(value, param){
	        	if(/^(0|([1-9]\d*))$/.test(value)){
	        		if(value > 100000000||value<1){
	        			return false;
	        		}
	        		return true;
	        	}else {
	        		false;
	        	}
	        },
	        message: '请输入1到99999999的正整数'
	    }
	   
	});
})(jQuery);