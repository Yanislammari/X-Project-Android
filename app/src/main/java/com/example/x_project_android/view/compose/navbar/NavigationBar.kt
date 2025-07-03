import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.x_project_android.view.compose.navbar.BasicAppDestination
import com.example.x_project_android.view.compose.navbar.TweetScreenDest
import com.example.x_project_android.view.compose.navbar.navTabs

private var TabHeight = 56.dp

@Composable
fun BasicNavBar(
    destinations: List<BasicAppDestination>,
    onTabSelected: (BasicAppDestination) -> Unit,
    currentDestination: BasicAppDestination,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            destinations.forEach { screen ->
                BasicNavTab(
                    title = screen.name,
                    icon = screen.icon,
                    onSelected = { onTabSelected(screen) },
                    selected = currentDestination == screen
                )
            }
        }
    }
}


@Composable
fun BasicNavTab(
    title: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean
) {
    val tabTintColor = if(selected) MaterialTheme.colorScheme.primary else Color.Gray
    Column(
        modifier = Modifier.padding(16.dp)
            .height(TabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = "", tint = tabTintColor)

        Spacer(Modifier.height(12.dp))

        Text(title, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
fun BasicNavTopBarPreview() {

    BasicNavBar(
        destinations = navTabs,
        onTabSelected = { tab -> },
        currentDestination = TweetScreenDest,
        )
}


@Preview
@Composable
fun BasicNavTabPreview() {
    Surface() {
        BasicNavTab(
            title = "Home",
            icon = Icons.Default.Home,
            onSelected = {},
            selected = true
        )
    }
}