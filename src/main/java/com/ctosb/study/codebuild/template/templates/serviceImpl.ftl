package ${packageName}.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alan.core.service.BaseService;
import ${packageName}.model.${className};


@Service("${miniClassName}Service")
@Transactional
public class ${className}ServiceImpl extends BaseService{

/**
* 保存
* @param model
* @author alan
* @date ${.now}
*/
public void save(${className} model) {
dao.save(model);
}
/**
* 批量保存
* @param model
* @author alan
* @date ${.now}
*/
public void saveAll(List<${className}> models){
dao.saveAll(models);
}

/**
* 通过id查询model
* @param id
* @return
* @author alan
* @date ${.now}
*/
public ${className} get(String id) {
return dao.get(${className}.class,id);
}

/**
* 获取所有的modal
* @return
* @author alan
* @date ${.now}
*/
public List<${className}> getAll(){
return dao.getAll(${className}.class);
}
/**
* 通过id逻辑删除对象
* @param id
* @author alan
* @date ${.now}
*/
public void logicDel(String id){
${className} model = new ${className}();
model.setId(id);
model.setLogicStatus((byte)1);
dao.save(model);
}
/**
* 通过id批量逻辑删除对象
* @param id
* @author alan
* @date ${.now}
*/
public void logicDelAll(String[] ids){
for(String id : ids){
logicDel(id);
}
}
/**
* 通过id删除对象
* @param id
* @author alan
* @date ${.now}
*/
public void del(String id){
${className} model = new ${className}();
model.setId(id);
dao.delete(model);
}
/**
* 批量删除
* @param ids
* @author alan
* @date ${.now}
*/
public void delAll(String[] ids){
dao.deleteByIds(${className}.class, ids);
}

}