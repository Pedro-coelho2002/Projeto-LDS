package pp.ipp.estg.urbanmarket.paginas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.Feirante
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto
import pp.ipp.estg.urbanmarket.retrofit.produtoAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun FeirasProximasScreen(idProduto: Int, idFeirante: Int, navController: NavHostController) {
    var loading by remember { mutableStateOf(true) }
    var produto by remember { mutableStateOf<Produto?>(null) }
    var feirantes by remember { mutableStateOf<List<Feirante>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produtoAPI.getProduct(idProduto)
                .enqueue(object : Callback<Produto> {
                    override fun onResponse(
                        call: Call<Produto>,
                        response: Response<Produto>
                    ) {
                        if (response.isSuccessful) {
                            produto = response.body()
                            loading = false
                        } else {
                            Log.d("Response Feirante", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<Produto>, t: Throwable) {
                        Log.d("Screen Principal fail -> getFeirante()", t.toString())
                    }
                })
        }
    }

    loading = true
    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produtoAPI.getFeirantesByProduct(idProduto)
                .enqueue(object : Callback<List<Feirante>> {
                    override fun onResponse(
                        call: Call<List<Feirante>>,
                        response: Response<List<Feirante>>
                    ) {
                        if (response.isSuccessful) {
                            feirantes = response.body() ?: emptyList()
                            loading = false
                        } else {
                            Log.d("Response Produtos", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<Feirante>>, t: Throwable) {
                        Log.d("Screen Principal fail -> getProdutos()", t.toString())
                    }
                })
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        AsyncImage(
            model = produto?.linkImagem,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Feirantes com ${produto?.nome}:",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    feirantes.forEach {
                        if(!(it.id.equals(idFeirante))){
                            CardFeiranteItem(feirante = it, idProduto, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardFeiranteItem(feirante: Feirante, idProduto: Int, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { navController.navigate("ReserveProduct/$idProduto/${feirante.id}") })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = feirante.nome,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Email: ${feirante.email}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

            }
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .padding(8.dp)
            )
        }
    }
}
