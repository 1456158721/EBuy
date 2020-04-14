package cn.qd.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_template")
public class Template implements Serializable {

    @Id
    private Integer id;
    //模板名称
    private String name;
    //规格数量
    private Integer specNum;
    //参数数量
    private Integer paraNum;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getSpecNum() {
        return specNum;
    }

    public void setSpecNum(Integer specNum) {
        this.specNum = specNum;
    }

    public Integer getParaNum() {
        return paraNum;
    }

    public void setParaNum(Integer paraNum) {
        this.paraNum = paraNum;
    }
}
