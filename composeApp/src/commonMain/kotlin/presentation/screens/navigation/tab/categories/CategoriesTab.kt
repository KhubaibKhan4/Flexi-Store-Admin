package presentation.screens.navigation.tab.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions


object CategoriesTab : Tab {
    @Composable
    override fun Content() {

    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Category)
            val title = "Categories"
            val index: UShort = 2u

            return TabOptions(
                index, title, icon
            )
        }
}