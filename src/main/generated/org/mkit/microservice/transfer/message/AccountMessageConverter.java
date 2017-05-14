/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.mkit.microservice.transfer.message;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link org.mkit.microservice.transfer.message.AccountMessage}.
 *
 * NOTE: This class has been automatically generated from the {@link org.mkit.microservice.transfer.message.AccountMessage} original class using Vert.x codegen.
 */
public class AccountMessageConverter {

  public static void fromJson(JsonObject json, AccountMessage obj) {
    if (json.getValue("account") instanceof String) {
      obj.setAccount((String)json.getValue("account"));
    }
    if (json.getValue("amount") instanceof Number) {
      obj.setAmount(((Number)json.getValue("amount")).doubleValue());
    }
    if (json.getValue("transaction") instanceof String) {
      obj.setTransaction((String)json.getValue("transaction"));
    }
    if (json.getValue("transfer") instanceof String) {
      obj.setTransfer((String)json.getValue("transfer"));
    }
    if (json.getValue("type") instanceof String) {
      obj.setType(org.mkit.microservice.transfer.message.AccountMessage.Type.valueOf((String)json.getValue("type")));
    }
  }

  public static void toJson(AccountMessage obj, JsonObject json) {
    if (obj.getAccount() != null) {
      json.put("account", obj.getAccount());
    }
    if (obj.getAmount() != null) {
      json.put("amount", obj.getAmount());
    }
    if (obj.getTransaction() != null) {
      json.put("transaction", obj.getTransaction());
    }
    if (obj.getTransfer() != null) {
      json.put("transfer", obj.getTransfer());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType().name());
    }
  }
}