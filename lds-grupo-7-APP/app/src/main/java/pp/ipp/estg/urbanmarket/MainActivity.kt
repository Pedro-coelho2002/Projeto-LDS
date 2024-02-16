package pp.ipp.estg.urbanmarket

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pp.ipp.estg.urbanmarket.paginas.LoginPage
import pp.ipp.estg.urbanmarket.paginas.MyNavigatonDrawer
import pp.ipp.estg.urbanmarket.paginas.SignupPage
import pp.ipp.estg.urbanmarket.retrofit.categoriaProdutoAPI
import pp.ipp.estg.urbanmarket.retrofit.models.categoriaProdutoRetrofit.CategoriaProduto
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto
import pp.ipp.estg.urbanmarket.retrofit.produtoAPI
import pp.ipp.estg.urbanmarket.ui.theme.UrbanMarketTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UrbanMarketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAPP()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostMyAPP(categorias: List<CategoriaProduto>, produtos: List<Produto>){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){ LoginPage(navController = navController)}

        composable("signup") { SignupPage(navController) }

        composable("Pagina Principal/{id}") { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.toInt()?.let {
                MyNavigatonDrawer(categorias = categorias, produtos = produtos, idFeirante = it, loginNavController = navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAPP(){
    var categorias by remember { mutableStateOf<List<CategoriaProduto>>(emptyList()) }
    var produtos by remember { mutableStateOf<List<Produto>>(emptyList()) }

    var loading by remember { mutableStateOf(true) }

    val categoriaProdutoAPI = categoriaProdutoAPI
    val produtoAPI = produtoAPI

    LaunchedEffect(key1 = true) {
        if (categorias.isEmpty() && loading) {
            categoriaProdutoAPI.getCategoriaProdutos().enqueue(object : Callback<List<CategoriaProduto>> {
                override fun onResponse(
                    call: Call<List<CategoriaProduto>>,
                    response: Response<List<CategoriaProduto>>
                ) {
                    if (response.isSuccessful) {
                        categorias = response.body() ?: emptyList()
                        loading = false
                    }
                }

                override fun onFailure(call: Call<List<CategoriaProduto>>, t: Throwable) {
                    Log.d("CategoriaProdutos -> MainPage fail -> getCategoriaProdutos()", t.toString())
                }
            })
        }
    }

    loading = true
    LaunchedEffect(key1 = true) {
        if (produtos.isEmpty() && loading) {
            produtoAPI.getProducts().enqueue(object : Callback<List<Produto>> {
                override fun onResponse(
                    call: Call<List<Produto>>,
                    response: Response<List<Produto>>
                ) {
                    if (response.isSuccessful) {
                        produtos = response.body() ?: emptyList()
                        loading = false
                    }
                }

                override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                    Log.d("Produtos -> MainPage fail -> getProducts()", t.toString())
                }
            })
        }
    }

    NavHostMyAPP(categorias = categorias, produtos = produtos)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UrbanMarketTheme {
        MyAPP()
    }
}