package ru.tagilov.avitotrainee.core.util

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/** Circle shimmer (e.g. for image)
 *
 */
fun Modifier.shimmerRound(
  visible: Boolean,
): Modifier = composed {
  this.placeholder(
    visible = visible,
    color = MaterialTheme.colors.onSurface,
    highlight = PlaceholderHighlight.shimmer(MaterialTheme.colors.surface),
    shape = CircleShape
  )
}


/** Rounded shimmer for content
 *
 */
fun Modifier.shimmerContent(
  visible: Boolean,
  roundSize: Dp = 12.dp,
): Modifier = composed {
  this.placeholder(
    visible = visible,
    color = MaterialTheme.colors.onSurface,
    highlight = PlaceholderHighlight.shimmer(MaterialTheme.colors.surface),
    shape = RoundedCornerShape(roundSize)
  )
}

/** Empty shimmer (fills entire size)
 * not used
 */
fun Modifier.shimmer(
  visible: Boolean,
): Modifier = composed {
  this.placeholder(
    visible = visible,
    color = MaterialTheme.colors.onSurface,
    highlight = PlaceholderHighlight.shimmer(MaterialTheme.colors.surface),
  )
}