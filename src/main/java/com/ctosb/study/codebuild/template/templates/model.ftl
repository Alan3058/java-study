package ${packageName}.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.alan.core.model.BaseModel;

/**
* ${tableRemark}
*
* @author alan
* @date ${.now}
*/
@Entity
@Table(name = "${tableName}")
public class ${className} extends BaseModel {

private static final long serialVersionUID = -4643466141706527625L;
<#list fields as field>
/**
* ${field.columnRemark}
*/
private ${field.fieldType} ${field.fieldName};
</#list>

public ${className}() {

}

<#list fields as field>
@Column(name="${field.columnName}")
public ${field.fieldType} get${field.methodName}() {
return ${field.fieldName};
}

public void set${field.methodName}(${field.fieldType} ${field.fieldName}) {
this.${field.fieldName} = ${field.fieldName};
}
</#list>


}