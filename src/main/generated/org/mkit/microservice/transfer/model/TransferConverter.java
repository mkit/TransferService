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

package org.mkit.microservice.transfer.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link org.mkit.microservice.transfer.model.Transfer}.
 *
 * NOTE: This class has been automatically generated from the {@link org.mkit.microservice.transfer.model.Transfer} original class using Vert.x codegen.
 */
public class TransferConverter {

  public static void fromJson(JsonObject json, Transfer obj) {
    if (json.getValue("amount") instanceof Number) {
      obj.setAmount(((Number)json.getValue("amount")).doubleValue());
    }
    if (json.getValue("createdAt") instanceof Number) {
      obj.setCreatedAt(((Number)json.getValue("createdAt")).longValue());
    }
    if (json.getValue("fromAccount") instanceof String) {
      obj.setFromAccount((String)json.getValue("fromAccount"));
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("issuer") instanceof String) {
      obj.setIssuer((String)json.getValue("issuer"));
    }
    if (json.getValue("message") instanceof String) {
      obj.setMessage((String)json.getValue("message"));
    }
    if (json.getValue("status") instanceof String) {
      obj.setStatus(org.mkit.microservice.transfer.model.Transfer.Status.valueOf((String)json.getValue("status")));
    }
    if (json.getValue("toAccount") instanceof String) {
      obj.setToAccount((String)json.getValue("toAccount"));
    }
    if (json.getValue("transaction") instanceof String) {
      obj.setTransaction((String)json.getValue("transaction"));
    }
  }

  public static void toJson(Transfer obj, JsonObject json) {
    if (obj.getAmount() != null) {
      json.put("amount", obj.getAmount());
    }
    if (obj.getCreatedAt() != null) {
      json.put("createdAt", obj.getCreatedAt());
    }
    if (obj.getFromAccount() != null) {
      json.put("fromAccount", obj.getFromAccount());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getIssuer() != null) {
      json.put("issuer", obj.getIssuer());
    }
    if (obj.getMessage() != null) {
      json.put("message", obj.getMessage());
    }
    if (obj.getStatus() != null) {
      json.put("status", obj.getStatus().name());
    }
    if (obj.getToAccount() != null) {
      json.put("toAccount", obj.getToAccount());
    }
    if (obj.getTransaction() != null) {
      json.put("transaction", obj.getTransaction());
    }
  }
}