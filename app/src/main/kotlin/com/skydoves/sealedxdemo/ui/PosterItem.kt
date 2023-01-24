/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.sealedxdemo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.sealedxdemo.models.Poster

@Composable
fun PosterItem(poster: Poster) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    GlideImage(
      imageModel = { poster.poster },
      modifier = Modifier.aspectRatio(0.8f)
    )

    Text(
      modifier = Modifier.padding(8.dp),
      text = poster.name,
      style = MaterialTheme.typography.h2,
      textAlign = TextAlign.Center
    )

    Text(
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .padding(bottom = 12.dp),
      text = poster.playtime,
      style = MaterialTheme.typography.body1,
      textAlign = TextAlign.Center
    )
  }
}
