package com.example.x_project_android.view.compose.navbar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
            .height(56.dp),
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
    val tabTintColor = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                indication = ripple(
                    radius = 65.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                interactionSource = interactionSource
            )
            .padding(start = 16.dp, end = 16.dp,top = 8.dp,bottom = 8.dp), // padding inside the ripple area
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = tabTintColor,
                modifier = Modifier.size(25.dp)
            )
            Text(
                title,
                style = MaterialTheme.typography.labelSmall,
                color = tabTintColor,
            )
        }
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