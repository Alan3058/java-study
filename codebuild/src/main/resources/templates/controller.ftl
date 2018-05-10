package ${packageName}.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alan.core.model.Message;
import com.alan.system.model.Section;
import ${packageName}.model.${className};
import ${packageName}.service.${className}Service;
import com.alan.system.util.ResponseJson;

@Controller
@RequestMapping("${miniClassName}")
public class ${className}Controller {

@Autowired
private ${className}Service ${miniClassName}Service;

/**
* 添加
*
* @param model
* @return
* @author alan
* @date ${.now}
*/
@RequestMapping("add")
public String add(${className} model) {
${miniClassName}Service.save(model);
return "redirect:load.shtml";
}

/**
* 进入初始化编辑页面
*
* @return
* @author alan
* @date ${.now}
*/
@RequestMapping("load")
public ModelAndView load() {
ModelAndView mav = new ModelAndView();
List<${className}> ${miniClassName}s = ${miniClassName}Service.getAll();
mav.addObject("${miniClassName}s", ${miniClassName}s);
mav.setViewName("${miniClassName}");
return mav;
}

/**
* 获取所有model
*
* @return
* @author alan
* @date ${.now}
*/
@RequestMapping(value = "getAll", method = RequestMethod.POST)
@ResponseBody
public Object getAll() {
List<${className}> list = ${miniClassName}Service.getAll();
return ResponseJson.send(list,Section.class,"${miniClassName}");
}
/**
* 获取所有model给前台Grid
* @return
* @author Alan
* @date  ${.now}
*/
@RequestMapping(value = "getAll2Grid", method = RequestMethod.POST)
@ResponseBody
public Object getAll2Grid() {
List<${className}> list = ${miniClassName}Service.getAll();
return ResponseJson.send2Grid(list,Section.class,"${miniClassName}");
}


/**
* 通过id获取model
*
* @param id
* @return
* @author alan
* @date ${.now}
*/
@RequestMapping(value = "get", method = RequestMethod.POST)
@ResponseBody
public Object get(String id) {
${className} model = ${miniClassName}Service.get(id);
return ResponseJson.send(model,Section.class,"${miniClassName}");
}

/**
* 通过id删除model
*
* @param id
* @return
* @author alan
* @date ${.now}
*/
@RequestMapping(value = "del", method = RequestMethod.POST)
@ResponseBody
public Object del(String id) {
${miniClassName}Service.del(id);
Message msg = new Message();
msg.setSuccess(true);
return ResponseJson.send(msg);
}

}
