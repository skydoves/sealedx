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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.skydoves.sealedxdemo.extensive.PosterUiState

@Composable
fun Main(
  viewModel: PosterMainViewModel = PosterMainViewModel()
) {
  val posterUiState by viewModel.posterUiState.collectAsState()
  PosterList(posterUiState = posterUiState)
}

@Composable
private fun PosterList(
  posterUiState: PosterUiState
) {
  Box(modifier = Modifier.fillMaxSize()) {
    when (posterUiState) {
      is PosterUiState.Success -> {
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          state = rememberLazyGridState()
        ) {
          items(
            items = posterUiState.data.posters,
            key = { it.name },
            itemContent = { PosterItem(poster = it) }
          )
        }
      }
      is PosterUiState.Loading -> {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center)
        )
      }
      is PosterUiState.Error -> {
        Text(text = "Error!", modifier = Modifier.align(Alignment.Center))
      }
    }
  }
}
