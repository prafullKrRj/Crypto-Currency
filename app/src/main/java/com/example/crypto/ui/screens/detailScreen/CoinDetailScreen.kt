package com.example.crypto.ui.screens.detailScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.crypto.CryptoApplication
import com.example.crypto.model.coinDetail.CoinCompleteDetail
import com.example.crypto.ui.screens.commons.ErrorScreen
import com.example.crypto.ui.screens.commons.LoadingScreen
import com.example.crypto.ui.screens.detailScreen.components.Description
import com.example.crypto.ui.screens.detailScreen.components.DocumentComposable
import com.example.crypto.ui.screens.detailScreen.components.OpenSource
import com.example.crypto.ui.screens.detailScreen.components.Tags
import com.example.crypto.ui.screens.detailScreen.components.TeamMembers
import com.example.crypto.ui.screens.detailScreen.components.TitleRow

@Composable
fun CoinDetailScreen(coinId: String) {
    val x = object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CryptoApplication)
                val cryptoRepository = application.container.cryptoDetailRepository
                DetailScreenViewModel(cryptoRepository = cryptoRepository, coinId = coinId)
            }
        }
    }
    println(coinId)
    val viewModel: DetailScreenViewModel = viewModel(factory = x.Factory)
    when (val coinState = viewModel.coinDetailState) {
        is CoinDetailState.Error -> {
            ErrorScreen()
        }
        is CoinDetailState.Loading -> {
            LoadingScreen()
        }
        is CoinDetailState.Success -> {
            SuccessScreen(coinState.coinDetail, Modifier.padding(16.dp))
        }
    }
}

@Composable
private fun SuccessScreen(coin: CoinCompleteDetail, modifier: Modifier) {
    LazyColumn (modifier = modifier){
        item {
            TitleRow(coin = coin)
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (coin.description.isNotEmpty()) {
            item {
                Description(coin = coin)
                MainScreenDivider(Modifier)
            }
        }
        if (coin.tags.isNotEmpty()) {
            item {
                Tags(coin = coin)
                MainScreenDivider(Modifier)
            }
        }
        item {
            OpenSource(coin = coin)
            MainScreenDivider(Modifier)
        }
        if (coin.team.isNotEmpty()) {
            item {
                TeamMembers(list = coin.team)
                MainScreenDivider(Modifier)
            }
        }
        if (coin.whitepaper.link != null) {
            item {
                DocumentComposable(coin.whitepaper.link)
                MainScreenDivider(Modifier)
            }
        }
    }
}

@Composable
fun MainScreenDivider(modifier: Modifier) {
    Divider(
        modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(1.dp))
}