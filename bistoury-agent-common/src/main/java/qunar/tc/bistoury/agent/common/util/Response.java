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

package qunar.tc.bistoury.agent.common.util;

/**
 * @author 肖哥弹架构
 * @date 2022-09-19
 * @desc 请求响应结果对象
 */
public class Response {
    /**
     * 响应类型
     */
    private String type;
    /**
     * 响应状态
     */
    private int status;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应结果数据
     */
    private Object data;

    public Response(String type, int status, String message) {
        this.type = type;
        this.status = status;
        this.message = message;
    }

    public Response(String type, int status, Object data) {
        this.type = type;
        this.status = status;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
