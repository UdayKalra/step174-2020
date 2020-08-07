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

package com.google.sps.images;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.google.sps.images.data.AnnotatedImage;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 *
 * VisionImagesManager is an ImagesManager implemented using the Google Vision API.
 * VisionImagesManager Manages the gathering and packaging of Vision API image analytics.
 */
public final class VisionImagesManager implements ImagesManager {
  private final ImageAnnotatorClient imageAnnotatorClient;

  /**
   * Creates a vision manager object using the image annotator client from Vision API.
   */
  public VisionImagesManager() throws IOException {
    ImageAnnotatorClient imageAnnotatorClient = ImageAnnotatorClient.create();
    this.imageAnnotatorClient = imageAnnotatorClient;
  }

  /**
   * Creates a vision manager object using a mock image annotator client.
   */
  public VisionImagesManager(ImageAnnotatorClient imageAnnotatorClient) {
    this.imageAnnotatorClient = imageAnnotatorClient;
  }

  @Override
  public List<AnnotatedImage> createAnnotatedImagesFromImagesAsByteArrays(
      List<byte[]> imagesAsByteArrays) throws IOException {
    List<AnnotatedImage> annotatedImages = new ArrayList<>();

    // TODO: parallelize calls to detectLabelsFromImageBytes() since it requires a network call.
    for (byte[] rawImageData : imagesAsByteArrays) {
      List<EntityAnnotation> labelAnnotations = detectLabelsFromImageBytes(rawImageData);
      AnnotatedImage annotatedImage = new AnnotatedImage(rawImageData, labelAnnotations);
      annotatedImages.add(annotatedImage);
    }

    return annotatedImages;
  }

  /**
   * Creates and returns label annotations for the image represented in bytes, using the Vision API.
   *
   * @param bytes The raw image byte data for the image from which labels will be annotated.
   * @return The list of label annotations related to the image, with each individual label being
   *     represented as an EntityAnnotation object.
   */
  private List<EntityAnnotation> detectLabelsFromImageBytes(byte[] bytes) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();
    List<EntityAnnotation> labels;

    Image img = Image.newBuilder().setContent(ByteString.copyFrom(bytes)).build();
    Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
    AnnotateImageRequest request =
        AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
    requests.add(request);

    // The invocation of batchAnnotateImages() makes a network call.
    BatchAnnotateImagesResponse response = imageAnnotatorClient.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    // TODO: parallelize calls to detectLabelsFromImageBytes() since it requires a network call.
    if (!(responses.size() == 1)) {
      throw new IllegalArgumentException(
          "detectLabelsFromImageBytes only supports analytics on one image");
    }
    AnnotateImageResponse res = responses.get(0);

    if (res.hasError()) {
      throw new IOException(res.getError().getMessage());
    }

    // For full list of available annotations, see http://g.co/cloud/vision/docs
    labels = res.getLabelAnnotationsList();
    return labels;
  }
}