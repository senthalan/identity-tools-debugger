/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.developer.lsp.debug.dap.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;
import org.wso2.carbon.identity.developer.lsp.debug.dap.messages.Argument;
import org.wso2.carbon.identity.developer.lsp.debug.dap.messages.VariablesResponse;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.VariableEncoder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializes the variables response.
 */
public class VariablesResponseSerializer implements JsonSerializer<VariablesResponse> {

    private VariableEncoderRegistry variableEncoderRegistry = new VariableEncoderRegistry();

    @Override
    public JsonElement serialize(VariablesResponse response, Type type,
                                 JsonSerializationContext jsonSerializationContext) {

        JsonObject object = new JsonObject();
        object.addProperty(DAPConstants.JSON_KEY_FOR_JSONRPC, DAPConstants.JSON_RPC_VERSION);
        object.addProperty(DAPConstants.JSON_KEY_FOR_ID, response.getId());
        object.add(DAPConstants.JSON_KEY_FOR_RESULT, generateResultObject(response));

        return object;
    }

    private JsonElement generateResultObject(VariablesResponse response) {

        JsonObject object = new JsonObject();
        object.addProperty(DAPConstants.JSON_KEY_FOR_COMMAND, response.getCommand());
        object.addProperty(DAPConstants.JSON_KEY_FOR_MESSAGE, response.getMessage());

        JsonObject body = new JsonObject();
        object.add(DAPConstants.JSON_KEY_FOR_BODY, body);
        body.add(DAPConstants.JSON_KEY_FOR_VARIABLES, generateVariablesArray(response));
        return object;
    }

    private JsonElement generateVariablesArray(VariablesResponse response) {

        JsonArray jsonArray = new JsonArray();

        if (response.getBody() != null) {
            Argument<HashMap<String, Object>> body = response.getBody();
            HashMap<String, Object> variables = body.getValue();

            if (variables == null) {
                return jsonArray;
            }

            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                VariableEncoder variableEncoder = this.variableEncoderRegistry.getVariablesEncoder(entry.getKey());
                if (variableEncoder != null) {
                    JsonObject arrayElement = variableEncoder.translate(entry.getValue());
                    arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_NAME, entry.getKey());
                    jsonArray.add(arrayElement);
                }
            }
        }

        return jsonArray;
    }
}
