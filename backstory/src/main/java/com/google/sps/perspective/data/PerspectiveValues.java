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

package com.google.sps.perspective.data;

import au.com.origma.perspectiveapi.v1alpha1.models.AttributeType;
import java.util.Map;

/**
 * A container to hold text and the scores generated by that text.
 */
public class PerspectiveValues {
  /** the text that generated these scores */
  private final String text;

  /** container for Perspective analysis that maps attribute types to scores */
  private final Map<AttributeType, Float> attributeTypesToScores;

  /**
   * Constructs a PerspectiveValues object with the text that was analyzed
   * and the subsequent scores produced from Perspective stored in a map.
   *
   * @param text the text that was analyzed
   * @param attributeTypesToScores the analysis of the text to be stored as
   *    a map which maps attribute types to scores
   */
  public PerspectiveValues(String text, Map<AttributeType, Float> attributeTypesToScores) {
    this.text = text;
    this.attributeTypesToScores = attributeTypesToScores;
  }

  /**
   * Returns the text that was analyzed.
   *
   * @return text that was analyzed
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the scores from the analysis of the text as Map which maps
   * AttributeType to scores, which are floats.
   *
   * @return a Map of all AttributeTypes and the corresponding scores from the analysis
   */
  public Map<AttributeType, Float> getAttributeTypesToScores() {
    return attributeTypesToScores;
  }
}
