package com.ctosb.study.codebuild;

public class Field {

    //java字段名称
    private String fieldName;
    //java字段类型
    private String fieldType;
    //java字段注释
    private String columnRemark;
    //java方法名
    private String methodName;
    //表字段名称
    private String columnName;
    //表字段类型
    private String columnType;

    public Field() {

    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }


}
