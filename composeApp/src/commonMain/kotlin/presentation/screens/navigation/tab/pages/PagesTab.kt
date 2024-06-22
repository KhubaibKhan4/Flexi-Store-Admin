package presentation.screens.navigation.tab.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions


object PagesTab : Tab {
    @Composable
    override fun Content() {

    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Pages)
            val title = "Pages"
            val index: UShort = 8u

            return TabOptions(
                index, title, icon
            )
        }
}