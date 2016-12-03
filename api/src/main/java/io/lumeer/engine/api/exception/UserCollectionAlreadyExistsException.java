/*
 * -----------------------------------------------------------------------\
 * Lumeer
 *  
 * Copyright (C) 2016 - 2017 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.lumeer.engine.api.exception;

/**
 * @author <a href="alica.kacengova@gmail.com">Alica Kačengová</a>
 */

/**
 * Thrown in case collection with given user name already exists
 */
public class UserCollectionAlreadyExistsException extends DbException {

   public UserCollectionAlreadyExistsException(final String message) {
      super(message);
   }

   public UserCollectionAlreadyExistsException(final String message, final Throwable cause) {
      super(message, cause);
   }

   public UserCollectionAlreadyExistsException(final Throwable cause) {
      super(cause);
   }

}