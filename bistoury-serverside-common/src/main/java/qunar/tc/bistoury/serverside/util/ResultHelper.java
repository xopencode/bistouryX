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

package qunar.tc.bistoury.serverside.util;

import qunar.tc.bistoury.serverside.bean.ApiResult;
import qunar.tc.bistoury.serverside.bean.ApiStatus;

/**
 * @author 肖哥弹架构
 * @date 2021/09/06
 * @describe 结果工具类
 */
public class ResultHelper {

    /**
     * 为成功状态下的空结果成功状态
     * @return
     */
    public static ApiResult success() {

        return new ApiResult<>(ApiStatus.SUCCESS.getCode(), "成功", null);
    }

    /**
     * 为成功状态下的请求结果的API包装对象
     * @param data 响应结果
     * @param <T> 结果泛型
     * @return ApiResult
     */
    public static <T> ApiResult success(T data) {

        return new ApiResult<>(ApiStatus.SUCCESS.getCode(), "成功", data);
    }

    /**
     * 为成功状态下的请求结果的API包装对象
     * @param code 响应状态码
     * @param message 响应消息
     * @param data 响应结果
     * @param <T> <T> 结果泛型
     * @return ApiResult
     */
    public static <T> ApiResult success(int code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    public static <T> ApiResult success(String message, T data) {

        return new ApiResult<>(ApiStatus.SUCCESS.getCode(), message, data);
    }

    public static ApiResult fail(String message) {

        return new ApiResult<>(ApiStatus.SYSTEM_ERROR.getCode(), message, null);
    }

    public static ApiResult fail(int status, String message) {

        return new ApiResult<>(status, message, null);
    }

    public static <T> ApiResult fail(int status, String message, T data) {

        return new ApiResult<>(status, message, data);
    }

    public static ApiResult fromStatus(ApiStatus apiStatus) {

        return new ApiResult<>(apiStatus.getCode(), apiStatus.getMsg(), null);
    }
}
