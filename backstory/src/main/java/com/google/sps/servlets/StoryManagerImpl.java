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

package com.google.sps.servlets;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import java.io.IOException;
import java.util.Scanner;
import org.json.JSONObject;

/**
 * Generates text through interface with Cloud Contained
 * GPT-2 Model.
 */
public final class StoryManagerImpl implements StoryManager {

  /**
   * Prefix, Maximum Length, and Temperature(Volatility) fields
   */
  private String prefix;
  private int maxLength;
  private Double temperature;

  /**
   * Instantiate StoryManager
   */
  public StoryManagerImpl(String prefix, int maxLength, Double temperature){
    this.prefix = prefix;
    this.maxLength = maxLength;
    this.temperature = temperature;
  }
  /**
   * Makes a post request with a JSON including GPT2 Parameters
   */
  private HttpResponse makePostRequestGPT2(
      String prefix, int textLength, Double temperature) throws IOException {
    
    String serviceUrl = "https://backstory-text-gen-pdaqhmzgva-uc.a.run.app";
        
    // Obtain Credentials
    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

    // Validate Credentials
    if (!(credentials instanceof IdTokenProvider)) {
      throw new IllegalArgumentException("Credentials are not an instance of IdTokenProvider.");
    }

    // Generate Authentication Token
    IdTokenCredentials tokenCredential = IdTokenCredentials.newBuilder()
                                             .setIdTokenProvider((IdTokenProvider) credentials)
                                             .setTargetAudience(serviceUrl)
                                             .build();

    // Configure URL
    GenericUrl genericUrl = new GenericUrl(serviceUrl);

    // Form Adapter with Authentication token
    HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(tokenCredential);
    HttpTransport transport = new NetHttpTransport();

    // Form JSON body using generation parameters
    String requestBody = "{\"length\": " + textLength
        + ",\"truncate\": \"<|endoftext|>\", \"prefix\": \"" + prefix
        + "\", \"temperature\": " + temperature + "}";

    // Build Request with Adapter and JSON Input
    HttpRequest request = transport.createRequestFactory(adapter).buildPostRequest(
        genericUrl, ByteArrayContent.fromString("application/json", requestBody));
    request.getHeaders().setContentType("application/json");

    // Wait until response received
    request.setConnectTimeout(0);
    request.setReadTimeout(0);
    return request.execute();
  }
  /**
   * Returns generated text output using fields.
   */
  public String generateText() {
    // Obtain response from Server POST Request
    try {
      HttpResponse outputResponse = makePostRequestGPT2(prefix, maxLength, temperature);

      // Parse response as JSON
      try {
        JSONObject jsonObject = new JSONObject(outputResponse.parseAsString());
        return jsonObject.getString("text");
      } catch (Exception jsonException) {
        throw new RuntimeException("Failed to convert repsonse into JSON", jsonException);
      }
    } catch (IOException serverException) {
      throw new RuntimeException("Error with server", serverException);
    }
  }

  /**
   * An executable demonstration of GPT-2 interface.
   */
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    System.out.println("Please enter a prompt: ");
    String prompt = input.nextLine();

    System.out.println("Please enter a text length: ");
    int size = input.nextInt();

    System.out.println("Please enter a temperature: ");
    Double temp = input.nextDouble();
    StoryManager sm = new StoryManagerImpl(prompt, size, temp);

    System.out.println(sm.generateText());
  }
}
