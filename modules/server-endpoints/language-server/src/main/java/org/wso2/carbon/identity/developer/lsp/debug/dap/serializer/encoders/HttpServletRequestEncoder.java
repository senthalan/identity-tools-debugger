/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * Encode the translated the HttpServletRequest variable.
 */
public class HttpServletRequestEncoder implements VariableEncoder {

    @Override
    public JsonObject translate(Object httpServletRequestTranslated) {

        JsonObject arrayElement = new JsonObject();
        JsonObject valueObject = new JsonObject();
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
        HashMap<String, Object> requestDetails = (HashMap<String, Object>) httpServletRequestTranslated;
        valueObject.add(DAPConstants.JSON_KEY_FOR_COOKIES, this.getCookies(requestDetails));
        valueObject.add(DAPConstants.JSON_KEY_FOR_HEADERS, this.getHeaders(requestDetails));
        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, valueObject);
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
        return arrayElement;
    }

    private JsonObject getHeaders(HashMap<String, Object> requestdetails) {

        HashMap<String, String> headers =
                (HashMap<String, String>) requestdetails.get(DAPConstants.JSON_KEY_FOR_HEADERS);
        JsonObject arrayElement = new JsonObject();
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                arrayElement.addProperty(header.getKey(), header.getValue());
            }
        }
        return arrayElement;
    }

    private JsonArray getCookies(HashMap<String, Object> requestDetails) {

        Object object = requestDetails.get(DAPConstants.JSON_KEY_FOR_COOKIES);
        JsonArray cookieJsonArray = new JsonArray();
        if (object != null) {
            Cookie[] cookies = (Cookie[]) object;
            for (Cookie cookie : cookies) {
                JsonObject valueObject = new JsonObject();
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_NAME, cookie.getName());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_VALUE, cookie.getValue());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_VERSION, cookie.getVersion());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_SECURE, cookie.getSecure());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_PATH, cookie.getPath());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_MAXAGE, cookie.getMaxAge());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_DOMAIN, cookie.getDomain());
                cookieJsonArray.add(valueObject);
            }
        }
        return cookieJsonArray;
    }
}
