package com.mybatis.generator.vo.result;

import java.io.Serializable;
import java.util.List;

/**
 * ResultVO
 *
 * @author LYJ
 * @date 2020/7/16 8:58
 * @since 1.0
 */
public class ResultVO<T> implements Serializable {
    private static final long serialVersionUID = 7060040898034477448L;

    /**
     * code
     */
    private Integer code;

    /**
     * success
     */
    private Boolean success;

    /**
     * data
     */
    private List<T> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code=" + code +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}
