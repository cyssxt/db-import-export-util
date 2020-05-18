package com.cyssxt.excel;

import com.cyssxt.excel.annotations.ExcelField;
import com.cyssxt.excel.annotations.ExcelView;
import com.cyssxt.excel.generator.*;
import com.cyssxt.excel.model.View;
import lombok.Data;

import java.sql.Timestamp;

@ExcelView(name = "user")
@Data
public class TestView implements View {
    @ExcelField(unique = true,title = "主键",name = "row_id",sort = 0,export = false,generator = UUIDGenerator.class)
    private String rowId;
    @ExcelField(unique = false,title = "删除标志",name = "del_flag",sort = 0,export = true,generator = DelFlagGenerator.class)
    private Boolean delFlag;
    @ExcelField(unique = false,title = "创建时间",name = "create_time",sort = 0,export = false,generator = TimeGenerator.class)
    private Timestamp createTime;
    @ExcelField(unique = false,title = "更新时间",name = "update_time",sort = 0,export = false,generator = TimeGenerator.class)
    private Timestamp updateTime;
    @ExcelField(unique = true,title = "家长手机",name = "phone",sort = 0,export = true)
    private String phone;
    @ExcelField(unique = false,title = "家长姓名",name = "user_name",sort = 1,export = true)
    private String userName;
    @ExcelField(unique = false,title = "类型",name = "type",sort = 1,defaultValue = "1")
    private String password;
    @ExcelField(unique = false,title = "密码",name = "password",sort = 1,generator = PasswordGenerator.class)
    private String type;
    @ExcelField(unique = false,title = "需要更新",name = "need_update",sort = 1,generator = TrueGenerator.class)
    private String needUpdate;
}
