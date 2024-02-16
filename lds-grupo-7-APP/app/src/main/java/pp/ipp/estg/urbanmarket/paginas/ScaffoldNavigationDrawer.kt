package pp.ipp.estg.urbanmarket.paginas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pp.ipp.estg.dispensapessoal.paginas.ComposeEmailScreen
import pp.ipp.estg.dispensapessoal.paginas.SettingsPage
import pp.ipp.estg.urbanmarket.NavHostMyAPP
import pp.ipp.estg.urbanmarket.retrofit.models.categoriaProdutoRetrofit.CategoriaProduto
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto

@Composable
fun MyLogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmação") },
        text = { Text("Deseja sair da conta?") },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Sim")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Não")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyNavigatonDrawer(
    categorias: List<CategoriaProduto>,
    produtos: List<Produto>,
    idFeirante: Int,
    loginNavController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val drawerItemList = prepareNavigationDrawerItems()
    var selectedItem by remember { mutableStateOf(drawerItemList[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                drawerItemList.forEach { item ->
                    MyDrawerItem(
                        item,
                        selectedItem,
                        { selectedItem = it },
                        navController,
                        idFeirante,
                        drawerState
                    )
                }
            }
        },
        content = {
            MyScaffold(
                drawerState = drawerState,
                navController = navController,
                categorias = categorias,
                produtos = produtos,
                idFeirante = idFeirante,
                loginNavController = loginNavController
            )
        }
    )
}

@Composable
fun MyDrawerItem(
    item: NavigationDrawerData,
    selectedItem: NavigationDrawerData,
    updateSelected: (i: NavigationDrawerData) -> Unit,
    navController: NavHostController,
    idFeirante: Int,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()
    NavigationDrawerItem(
        icon = { Icon(imageVector = item.icon, contentDescription = null) },
        label = { Text(text = item.label) },
        selected = (item == selectedItem),
        onClick = {
            coroutineScope.launch {
                if (item.label == "Pagina Principal" || item.label == "Logout" || item.label == "Sugestão") {
                    navController.navigate(item.label)
                    drawerState.close()
                } else {
                    navController.navigate(item.label + "/$idFeirante")
                    drawerState.close()
                }
            }
            updateSelected(item)
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyScaffold(
    drawerState: DrawerState,
    navController: NavHostController,
    categorias: List<CategoriaProduto>,
    produtos: List<Produto>,
    idFeirante: Int,
    loginNavController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MyTopAppBar {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                MyScaffoldContent(navController, categorias, produtos, idFeirante, loginNavController)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onNavIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "UrbanMarket") },
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Items"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyScaffoldContent(
    navController: NavHostController,
    categorias: List<CategoriaProduto>,
    produtos: List<Produto>,
    idFeirante: Int,
    loginNavController: NavHostController
) {
    NavHost(navController = navController, startDestination = "Pagina Principal") {
        composable("login") { NavHostMyAPP(categorias = categorias, produtos = produtos) }

        composable("Pagina Principal") {
            ScreenPrincipal(idFeirante, navController)
        }

        composable("Os meus Produtos/{id}") { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.toInt()?.let {
                MeusProdutos(navController, it)
            }
        }
        composable("Adicionar Produto/{id}") { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.toInt()?.let {
                AddProdutoPagina(categorias, produtos, it, navController)
            }
        }

        composable("ViewProduct/{id}") { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.toInt()
                ?.let { ViewProductPage(categorias, produtos, it, navController = navController) }
        }
        composable("Perfil/{id}") { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.toInt()
                ?.let { ProfileScreen(it, navController) }
        }

        composable("Sugestão") {
            ComposeEmailScreen()
        }

        composable("Logout") {
            MyLogoutConfirmationDialog({ loginNavController.navigate("login") },
                { navController.popBackStack() })
        }

        composable("ReserveProduct/{idProduto}/{idFeirante}") { backStackEntry ->
            backStackEntry.arguments?.getString("idProduto")?.toInt()?.let {idProduto ->
                backStackEntry.arguments?.getString("idFeirante")?.toInt()?.let { idFeirante ->
                    ReserveProductPage(idProduto, idFeirante, navController = navController)
                } }
        }

        composable("Feirantes Proximos/{id}") { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.toInt()
                ?.let { FeirasProximasScreen(it, idFeirante,navController = navController) }
        }
    }
}

private fun prepareNavigationDrawerItems(): List<NavigationDrawerData> {
    val drawerItemsList = arrayListOf<NavigationDrawerData>()
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Pagina Principal",
            icon = Icons.Filled.Home
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Os meus Produtos",
            icon = Icons.Filled.Storefront
        )
    )

    drawerItemsList.add(
        NavigationDrawerData(
            label = "Perfil",
            icon = Icons.Filled.Person
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Sugestão",
            icon = Icons.Filled.Settings
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Logout",
            icon = Icons.Default.ExitToApp
        )
    )

    return drawerItemsList
}

data class NavigationDrawerData(val label: String, val icon: ImageVector)
