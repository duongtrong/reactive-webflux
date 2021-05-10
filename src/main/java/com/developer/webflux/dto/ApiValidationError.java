package com.developer.webflux.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class ApiValidationError<T> implements ApiSubError {
  String object;

  String field;

  T rejectedValue;

  String message;

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }
}
