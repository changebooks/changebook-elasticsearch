package io.github.changebooks.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * ES实例
 *
 * @author changebooks@qq.com
 */
public class EsEntity<ID> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsEntity.class);

    /**
     * 解析json
     */
    private static final ObjectMapper JSON_PARSER = new ObjectMapper();

    /**
     * 主键
     */
    @Id
    private ID id;

    /**
     * 排序
     */
    @Field(type = FieldType.Integer)
    private Integer sort;

    /**
     * 备注，公开
     */
    @Field(type = FieldType.Text)
    private String remark;

    /**
     * 内部备注，不公开
     */
    @Field(type = FieldType.Text)
    private String note;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Date updatedAt;

    @Override
    public String toString() {
        try {
            return JSON_PARSER.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            LOGGER.error("parseJson failed, id: {}, sort: {}, remark: {}, note: {}, createdAt: {}, updatedAt: {}, throwable: ",
                    id, sort, remark, note, createdAt, updatedAt, ex);
            return null;
        }
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
