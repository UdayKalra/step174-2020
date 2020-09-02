// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets.data;

import com.google.appengine.api.datastore.Entity;

/**
 * Factory pattern for Entity.
 */
public interface EntityFactory {
  /**
   * Create a new instance of Entity.
   *
   * @param entityName the type, or name, of the entity to be created.
   * @return an instance of Entity.
   */
  public Entity newInstance(String entityName);
}
