/*
 * Copyright (C) 2019 Qunar, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package qunar.tc.bistoury.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author zhenyu.nie created on 2018 2018/10/9 11:07
 * @author 肖哥弹架构 update on 2021 2021/09/03 19:00
 * 远程请求结果实体类结构
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonResult<T> {
    //请求状态编号
    private int status;
    //请求状态描述信息
    private String message;
    //远程请求泛化业务实体
    private T data;

    public JsonResult() {
    }

    @JsonCreator
    public JsonResult(@JsonProperty("status") int status, @JsonProperty("message") String message, @JsonProperty("data") T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isOK() {
        return status == 0;
    }
}

