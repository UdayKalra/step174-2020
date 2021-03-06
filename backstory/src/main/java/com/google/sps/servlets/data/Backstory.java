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

/** Wrapper class representing a backstory */
public final class Backstory {
  // Represents the generated backstory text
  private final String backstory;

  /**
   * Creates a Backstory wrapper object from a backstory.
   *
   * @param backstory the generated backstory text.
   */
  public Backstory(String backstory) {
    this.backstory = backstory;
  }

  /**
   * Get the backstory text.
   *
   * @return the generated backstory text held in this wrapper object.
   */
  public String getBackstory() {
    return backstory;
  }
}
