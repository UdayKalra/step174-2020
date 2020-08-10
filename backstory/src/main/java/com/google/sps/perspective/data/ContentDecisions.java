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
 * Makes a decision on whether or not the story (or content) is appropriate
 * using analysis from Perspective API.
 */
public class ContentDecisions {
  /**
   * Overrides default constructor to ensure class can't be instantiated.
   */
  private ContentDecisions() {
    throw new AssertionError();
  }

  /**
   * Makes decision on whether or not text in perspective value
   * is considered appropriate based on the analysis scores from Perspective API
   * stored in PerspectiveValues object. Returns this decision as a boolean.
   * Current decision logic is based off whether text considered toxic.
   *
   * @param PerspectiveValues the object containing text to be decided on
   *     & the requested analysis from Perspective API to use in making decision.
   * @return true, if content considered appropriate; false, otherwise
   */
  public static boolean makeDecision(PerspectiveValues values) {
    // currently decision is entirely based on whether content is considered toxic

    return !isToxic(values.getAttributeTypesToScores());
  }

  /**
   * Private helper method to check if content is considered toxic.
   * Threshold for toxicity is a score greater than or equal to 70% as
   * that was the metric used by the demo on the Google Perspective API site.
   *
   * @param attributeTypesToScores a map with attribute types mapped to scores from the Perspective
   *     API
   * @return true, if toxicity score (in attributeTypesToScores) >= 70% toxic; false, if not
   * @throws IllegalArgumentException, if attributeTypesToScores is null or does not contain a
   *     toxicity score
   */
  private static boolean isToxic(Map<AttributeType, Float> attributeTypesToScores)
      throws IllegalArgumentException {
    if (attributeTypesToScores == null) {
      throw new IllegalArgumentException("Map (attributeTypesToScores) cannot be null.");
    } else if (!attributeTypesToScores.containsKey(AttributeType.TOXICITY)) {
      throw new IllegalArgumentException(
          "Map (attributeTypesToScores) does not contain a toxicity score");
    }

    float toxicity = attributeTypesToScores.get(AttributeType.TOXICITY);

    return toxicity >= .7f;
  }
}
