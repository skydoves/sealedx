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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.retrofit.adapters.result.onFailureSuspend
import com.skydoves.retrofit.adapters.result.onSuccessSuspend
import com.skydoves.sealedxdemo.extensive.PosterUiState
import com.skydoves.sealedxdemo.models.toExtensive
import com.skydoves.sealedxdemo.network.NetworkModule
import com.skydoves.sealedxdemo.network.PosterService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PosterMainViewModel constructor(
  private val service: PosterService = NetworkModule.posterService
) : ViewModel() {

  private val _posterUiState = MutableStateFlow<PosterUiState>(PosterUiState.Loading)
  val posterUiState: StateFlow<PosterUiState> = _posterUiState

  init {
    viewModelScope.launch {
      val posters = service.fetchPosterList()
      posters.onSuccessSuspend {
        _posterUiState.value = PosterUiState.Success(data = it.toExtensive())
      }.onFailureSuspend {
        _posterUiState.value = PosterUiState.Error
      }
    }
  }
}
