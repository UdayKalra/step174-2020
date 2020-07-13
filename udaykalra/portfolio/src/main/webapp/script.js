// Copyright 2019 Google LLC
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

/**
 * Delete comments and re-get Data
 */
async function deleteCommentData() {
  await fetch(new Request('/delete-data', {method: 'post'}));
  await getCommentData;
  window.location.reload();
}
/** Creates an <li> element containing text. */
function createCommentListItem(inputText) {
  const listElement = document.createElement('li');
  listElement.innerText = inputText;
  return listElement;
}

/**
 * Fetches comments for display.
 */
function getCommentData(commentsLimit = 20) {
  fetch('/data?comment-count=' + commentsLimit)  // sends a request to /data
      .then(response => response.json())         // parses the response as JSON
      .then((comments) => {  // now we can reference the fields as an
                             // object!
        const commentsElement = document.getElementById('quote-container');
        commentsElement.innerHTML = '';
        for (var index = 0; index < comments.length; index += 1) {
          console.log(comments);
          commentsElement.appendChild(createCommentListItem(comments[index]));
        }
      });
}
